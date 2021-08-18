package com.play.common.terrain;
import com.play.common.ldb.MCLdb;
import java.io.File;
import java.nio.ByteBuffer;

public abstract class Terrain {
    public MCLdb db;
    public volatile ByteBuffer terrainData,data2D;
	public static int chunkW, chunkL, chunkH;
    public Terrain(String path) {
        db = new MCLdb(new File(path));
    }
    public Terrain(MCLdb path) {
        db = path;
    }
    public void loadTerrain(byte[] key){
        terrainData=ByteBuffer.wrap(db.get(key));
    };
    public void DestoryTerrain(){
        terrainData=null;
    };
    public void load2DData(byte[] key){
        data2D=ByteBuffer.wrap(db.get(key));
    };
    
    public abstract int getBlockTypeId(int x, int y, int z)
    public abstract byte getBlockData(int x, int y, int z)
    public abstract int getHeightMapValue(int x, int z)
}
