package com.play.common.chunk;
import com.mithrilmania.blocktopograph.map.Dimension;
import com.play.common.ldb.MCLdb;
import com.play.common.terrain.Terrain;
import com.play.common.terrain.V0_9_Terrain;
import com.play.common.terrain.V1_0_Terrain;
import com.play.common.terrain.V1_1_Terrain;
import com.play.common.terrain.V1_2_Terrain;
import com.play.common.ldb.MCKey;

public class Chunk {
    public final int x,z;
    private final Terrain ter;
    public final Dimension dimension;
    
    public  final static int NULL=-1;
    public final static int ERROR=-2;
    public final static int[] V0_9=new int[]{2};
    public final static int[] V1_0=new int[]{3};
    public final static int[] V1_1=new int[]{0,2,4,5,6,7};
    public final static int[] V1_2=new int[]{1,8};
    
	
	private byte[] tkey,dkey;
    public Chunk(int x,int z,Dimension d,byte num,MCLdb db){
        this.x=x;this.z=z;this.dimension=d;
        
        tkey=MCKey.getChunkDataKey(x,z,ChunkTag.TERRAIN,d,num,true);
        dkey=MCKey.getChunkDataKey(x,z,ChunkTag.DATA_2D,d,num,false);
        //LOG.print("dbkey",tkey);
        int v=getVersion(db.get(tkey));
        
        if(isInRang(V0_9,v)){
            ter=new V0_9_Terrain(db);
        }else if(isInRang(V1_0,v)){
            ter=new V1_0_Terrain(db);
        }else if(isInRang(V1_1,v)){
            ter=new V1_1_Terrain(db);
        }else if(isInRang(V1_2,v)){
            ter=new V1_2_Terrain(db);
        }else{
            ter=new V1_2_Terrain(db);
        }
    }
	public boolean isInRang(int[] i,int u){
		for(int x : i){
			if(x==u)return true;
		}
		return false;
	}
	public Chunk loadTerrain(){
		ter.loadTerrain(tkey);
		return this;
	}
    public Chunk DestoryTerrain(){
        ter.DestoryTerrain();
        return this;
	}
	public Chunk load2DData(){
		ter.load2DData(dkey);
		return this;
	}
    public static int getVersion(byte[] data) {
        if (data == null || data.length <= 0) {
            return 0;
        } else {
            int versionNumber = data[0] & 0xff;

            //fallback version
            //You can't just do this...
            if (versionNumber > V1_2[1]) {
                versionNumber = V1_2[1];
            }
            //check if the returned version exists, fallback on ERROR otherwise.
            return versionNumber;
        }
    }
	public Terrain getTerrain(){
		return ter;
	}
}
