package com.play.common.renderer;
import android.graphics.Bitmap;
import com.mithrilmania.blocktopograph.map.Dimension;
import com.play.common.ldb.MCLdb;
import com.play.common.chunk.ChunkManager;

public interface MapRenderer {
	public static int maxX=256,maxZ=256;
    Bitmap getBitmap(int x,int y,ChunkManager ckm);
}
