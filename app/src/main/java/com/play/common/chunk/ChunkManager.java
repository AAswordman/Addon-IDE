package com.play.common.chunk;
import com.mithrilmania.blocktopograph.map.Dimension;
import com.play.common.enumdata.Block;

import java.util.HashMap;
import com.play.common.ldb.MCLdb;
import com.mithrilmania.blocktopograph.util.Noise;

public class ChunkManager {
	Dimension d;
	MCLdb db;
	HashMap<Integer,HashMap<Integer,Chunks>> map=new HashMap<>();

    private Noise noise;
    public ChunkManager(Dimension d,MCLdb db){
		this.db=db;
		this.d=d;
	}

    public void setNoise(Noise noise) {
        this.noise = noise;
    }

    public Noise getNoise() {
        return noise;
    }
    public Chunks getChunks(int x,int z){
		if(hasChunks(x,z)){
			return map.get(x).get(z);
		}else{
			return registerChunks(x,z);
		}
	}
    public static int xzToChunk(int x){
		return (int)Math.floor(x/16);
	}
	public boolean hasChunks(int x,int z){
		if(!map.containsKey(x)){
			return false;
		}
		if(!map.get(x).containsKey(z)){
			return false;
		}
		return true;
	}
	public Chunks registerChunks(int x,int z){
		HashMap<Integer,Chunks> use;
		if(map.containsKey(x)){
			use=map.get(x);
		}else{
			use=new HashMap<>();
			map.put(x,use);
		}
		Chunks cks=new Chunks(x,z,d,db,this);
		use.put(z,cks);
		return cks;
	}
	public void destroyChunks(int x,int z){
		map.get(x).remove(z);
		if(map.get(x).isEmpty()){
			map.remove(x);
		}
	}
	public void destroyAllChunks(){
		map.clear();
	}
    
}
