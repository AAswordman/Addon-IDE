package com.play.common.terrain;
import com.mithrilmania.blocktopograph.map.BlockNameResolver;
import com.mithrilmania.blocktopograph.nbt.convert.NBTInputStream;
import com.mithrilmania.blocktopograph.nbt.tags.CompoundTag;
import com.mithrilmania.blocktopograph.nbt.tags.ShortTag;
import com.mithrilmania.blocktopograph.nbt.tags.StringTag;
import com.play.common.ldb.MCLdb;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;
import com.play.common.enumdata.Block;


public class V1_2_Terrain extends Terrain {
    private int get2Di(int x, int z) {
        return z * chunkL + x;
    }

    @Override
    public int getHeightMapValue(int x, int z) {
        short h = data2D.getShort(POS_HEIGHTMAP + (get2Di(x, z) << 1));
        return ((h & 0xff) << 8) | ((h >> 8) & 0xff);//little endian to big endian
    }
    

    @Override
    public int getBlockTypeId(int x, int y, int z) {
        if (x >= chunkW || y >= chunkH || z >= chunkL || x < 0 || y < 0 || z < 0) {
            return 0;
        }
		
        return (getBlockState(x, y, z) >>> 8);
    }

    @Override
    public byte getBlockData(int x, int y, int z) {
        if (x >= chunkW || y >= chunkH || z >= chunkL || x < 0 || y < 0 || z < 0) {
            return 0;
        }
        return (byte) (getBlockState(x, y, z) & 0xf);
    }
    private int getBlockState(int x, int y, int z) {
        int codeOffset = getOffset(x, y, z);
        int intCapa = 32 / blockCodeLenth;
        int stick = mainStorage.get(codeOffset / intCapa);
        int ind = (stick >> (codeOffset % intCapa * blockCodeLenth)) & msk[blockCodeLenth - 1];
        return palette.get(ind);
    }

    @Override
    public void load2DData(byte[] key) {
        super.load2DData(key);
    }
    
    
    
    @Override
    public void loadTerrain(byte[] key) {
        byte[] rawData=db.get(key);
		//LOG.print("data",rawData);
        terrainData = ByteBuffer.wrap(db.get(key));
        terrainData.order(ByteOrder.LITTLE_ENDIAN);
        switch (rawData[0]) {
            case 0:
                terrainData.position(1);
                break;
            case 8:
                terrainData.position(2);
                break;
        }
        try {
            loadBlockStorage(terrainData);
        } catch (IOException e) {}
    }
    private void loadBlockStorage(ByteBuffer raw) throws IOException {

        //Read BlockState length.
        //this byte = (length << 2) | serializedType.
        blockCodeLenth = (raw.get() & 0xff) >> 1;

        //We use this much of bytes to store BlockStates.
        int bufsize = (4095 / (32 / blockCodeLenth) + 1) << 2;
        ByteBuffer byteBuffer = ByteBuffer.allocate(bufsize);

        byteBuffer.order(ByteOrder.LITTLE_ENDIAN);

        mainStorage = byteBuffer.asIntBuffer();

        //No convenient way copy these stuff.
        byteBuffer.put(raw.array(), raw.position(), bufsize);
        raw.position(raw.position() + bufsize);

        //Palette items count.
        int psize = raw.getInt();

        //Construct the palette. Each is a piece of nbt data.
        palette = new ArrayList<>(16);
        ByteArrayInputStream bais = new ByteArrayInputStream(raw.array());
        bais.skip(raw.position());
        NBTInputStream nis = new NBTInputStream(bais, false);
		//LOG.print("nbt",nis.readTag().toString());
        for (int i = 0; i < psize; i++) {
            CompoundTag tag = (CompoundTag) nis.readTag();
            System.out.println(tag.toString());
            String name = ((StringTag) tag.getChildTagByKey("name")).getValue();
            if (tag.getChildTagByKey("val") != null) {
                int data = ((ShortTag) tag.getChildTagByKey("val")).getValue();
                palette.add(Block.getBlockId(name) << 8 | data);
            } else {
				//LOG.print(name,Block.getBlockId(name)+"");
                palette.add(Block.getBlockId(name) << 8);
            }
        }
        raw.position(raw.position() + nis.getReadCount());
    }
    
    public volatile IntBuffer mainStorage;
    public volatile List<Integer> palette;
    public volatile int blockTypes, blockCodeLenth;
	static{
        chunkW = 16;chunkL = 16;chunkH = 16;
	}
    public static final int area = chunkW * chunkL;
    public static final int vol = area * chunkH;

    public static final int POS_HEIGHTMAP = 0;
    // it looks like each biome takes 2 bytes, and the first 1 byte of every 2 bytes is always 0!?
    public static final int POS_BIOME_DATA = POS_HEIGHTMAP + area + area;
    public static final int DATA2D_LENGTH = POS_BIOME_DATA + area;

    private static final int[] msk = {0b1, 0b11, 0b111, 0b1111, 0b11111, 0b111111, 0b1111111,
        0b11111111,
        0b111111111, 0b1111111111, 0b11111111111,
        0b111111111111,
        0b1111111111111, 0b11111111111111, 0b11111111111111};

    private static int getOffset(int x, int y, int z) {
        return (((x << 4) | z) << 4) | y;
    }
    public V1_2_Terrain(String path){
        super(path);
    }
    public V1_2_Terrain(MCLdb db){
        super(db);
    }
    
    
}
