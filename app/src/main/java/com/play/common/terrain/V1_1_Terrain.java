package com.play.common.terrain;
import com.play.common.ldb.MCLdb;
import java.nio.ByteBuffer;

public class V1_1_Terrain extends Terrain {

    @Override
    public int getHeightMapValue(int x, int z) {
        short h = data2D.getShort(POS_HEIGHTMAP + (get2Di(x, z) << 1));
        return ((h & 0xff) << 8) | ((h >> 8) & 0xff);//little endian to big endian
    }
    private int get2Di(int x, int z) {
        return z * chunkL + x;
    }
    
    public V1_1_Terrain(String path){
        super(path);
    }
    public V1_1_Terrain(MCLdb db){
        super(db);
    }
    @Override
    public void loadTerrain(byte[] key) {
        super.loadTerrain(key);
    }

    static{
        chunkW = 16;chunkL = 16;chunkH = 16;
	}
    public static final int area = chunkW * chunkL;
    public static final int vol = area * chunkH;

    public static final int POS_VERSION = 0;
    public static final int POS_BLOCK_IDS = POS_VERSION + 1;
    public static final int POS_META_DATA = POS_BLOCK_IDS + vol;
    public static final int POS_SKY_LIGHT = POS_META_DATA + (vol >> 1);
    public static final int TERRAIN_LENGTH = POS_SKY_LIGHT + (vol >> 1);

    public static final int POS_HEIGHTMAP = 0;
    // it looks like each biome takes 2 bytes, and the first 1 byte of every 2 bytes is always 0!?
    public static final int POS_BIOME_DATA = POS_HEIGHTMAP + area + area;
    public static final int DATA2D_LENGTH = POS_BIOME_DATA + area;


    private static int getOffset(int x, int y, int z) {
        return (x * chunkW + z) * chunkH + y;
    }

    @Override
    public int getBlockTypeId(int x, int y, int z) {
        if (x >= chunkW || y >= chunkH || z >= chunkL || x < 0 || y < 0 || z < 0) {
            return 0;
        }
        return terrainData.get(POS_BLOCK_IDS + getOffset(x, y, z))&0xff;
    }
    @Override
    public byte getBlockData(int x, int y, int z) {
        if (x >= chunkW || y >= chunkH || z >= chunkL || x < 0 || y < 0 || z < 0) {
            return 0;
        }
        int offset = getOffset(x, y, z);
        byte dualData = terrainData.get(POS_META_DATA + (offset >>> 1));
        return (byte) ((offset & 1) == 1 ? ((dualData >>> 4) & 0xf) : (dualData & 0xf));
    }
}
