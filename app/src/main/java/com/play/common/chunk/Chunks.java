package com.play.common.chunk;
import com.mithrilmania.blocktopograph.map.Dimension;
import com.play.common.ldb.MCLdb;
import com.play.common.enumdata.Block;
import com.play.common.terrain.Terrain;
import com.mithrilmania.blocktopograph.util.Noise;

public class Chunks {
	private Chunk[] ck=new Chunk[16];
	public int x,z;
	public Dimension d;
	public MCLdb db;
	private int h;
	private ChunkManager ckm;
    public Chunks(int x,int z,Dimension d,MCLdb db,ChunkManager ckm){
		this.x=x;
		this.z=z;
		this.d=d;
		this.ckm=ckm;
		this.db=db;
		//h=register(0).load2DData().getTerrain().chunkH;
	}
	public int getChunkWidth(){
		return ck[0].getTerrain().chunkW;
	}
	public int getChunkLength(){
		return ck[0].getTerrain().chunkL;
	}
	public Chunk get(int num){
		return ck[num];
	}
    public Chunk register(int num){
		ck[num]=new Chunk(x,z,d,(byte)num,db);
		return ck[num];
	}
	public int yToChunk(int y){
		return (int) Math.floor((float)y/h);
	}
	public int getHeightMapValue(int x,int z){
		int[] ChunkPos=new int[]{this.x,this.z};
		if(x<0){
			ChunkPos[0]-=1;
			x+=getChunkWidth();
		}else if(x>=getChunkWidth()){
			ChunkPos[0]+=1;
			x-=getChunkWidth();
		}
		if(z<0){
			ChunkPos[1]-=1;
			z+=getChunkLength();
		}else if(z>=getChunkLength()){
			ChunkPos[1]+=1;
			z-=getChunkLength();
		}
		//LOG.print(x+"",z+"");
		Chunks cks=ckm.getChunks(ChunkPos[0],ChunkPos[1]);
		//return cks.getHeightMapValue(cks.getChunkWidth()+x,cks.getChunkLength()+z);
		return cks.get(0).getTerrain().getHeightMapValue(x,z);
	}
    public void DestoryTerrain(){
        for(Chunk c:ck){
            if(c!=null)c.DestoryTerrain();
        }
	}
	public Block getTopBlock(int usex,int usez){
		int useh=getHeightMapValue(usex,usez)-1;
		if(ck[yToChunk(useh)]==null){
			register(yToChunk(useh));
		}
		Chunk use=ck[yToChunk(useh)];
		if(use.getTerrain().terrainData==null){
			use.loadTerrain();
		}
		Terrain ter=use.getTerrain();
		return Block.getBlock(ter.getBlockTypeId(usex,useh%h,usez),ter.getBlockData(usex,useh%h,usez));
	}
	public int getNoise(int x, int z) {
        Noise noise=ckm.getNoise();
        // noise values are between -1 and 1
        // 0.0001 is added to the coordinates because integer values result in 0
        double xval = (this.x << 4) | x;
        double zval = (this.z << 4) | z;
        double oct1 = noise.noise(
                (xval / 100.0) % 256 + 0.0001,
                (zval / 100.0) % 256 + 0.0001);
        double oct2 = noise.noise(
                (xval / 20.0) % 256 + 0.0001,
                (zval / 20.0) % 256 + 0.0001);
        double oct3 = noise.noise(
                (xval / 3.0) % 256 + 0.0001,
                (zval / 3.0) % 256 + 0.0001);
        return (int) (60 + (40 * oct1) + (14 * oct2) + (6 * oct3));
    }
}
