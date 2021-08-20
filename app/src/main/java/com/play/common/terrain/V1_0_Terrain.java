package com.play.common.terrain;
import com.play.common.ldb.MCLdb;

public class V1_0_Terrain extends Terrain {

    @Override
    public int getHeightMapValue(int x, int z) {
        return 0;
    }
    

    @Override
    public int getBlockTypeId(int x, int y, int z) {
        return 0;
    }

    @Override
    public byte getBlockData(int x, int y, int z) {
        return 0;
    }
    
    public V1_0_Terrain(String path){
        super(path);
    }
    public V1_0_Terrain(MCLdb db){
        super(db);
    }
    
    
}
