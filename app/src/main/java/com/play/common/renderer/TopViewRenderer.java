package com.play.common.renderer;
import android.graphics.Bitmap;
import com.mithrilmania.blocktopograph.map.Dimension;
import com.play.common.chunk.ChunkManager;
import com.play.common.ldb.MCLdb;
import com.play.common.chunk.Chunks;
import android.graphics.Canvas;
import com.play.common.enumdata.Block;
import android.graphics.Paint;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;

public class TopViewRenderer implements MapRenderer {

	@Override
	public Bitmap getBitmap(int x, int z, ChunkManager ckm) {
		ckm.getChunks(x, z);

		Chunks cks=ckm.getChunks(x, z);
		Bitmap bmp=Bitmap.createBitmap(maxX, maxZ, Bitmap.Config.ARGB_8888);
        
        //float scale=(float)oneX/(float)maxX;
        //LOG.print(scale+"",oneX+"/"+maxX);
		Canvas canvas=new Canvas(bmp);
		for (int ux=0,uxx=15;ux <= 15;ux++,uxx--) {
			for (int uzx=15,uz=0;uzx >= 0;uzx--,uz++) {
                Bitmap map=cks.getTopBlock(uxx, uz).texture;
				//Bitmap map=Bitmap.createScaledBitmap(m,(m.getWidth()),(int)(m.getHeight()),false);
                int Startx=(ux * Block.Width),Startz=(uzx * Block.Height);
				canvas.drawBitmap(map, Startx, Startz, null);

				//LOG.print("block",cks.getTopBlock(ux,uz).namespace);
				Paint paint = new Paint();
				int besideH=cks.getHeightMapValue(uxx + 1, uz),behindH=cks.getHeightMapValue(uxx, uz + 1),
					DiagonallyH=cks.getHeightMapValue(uxx + 1, uz + 1),H=cks.getHeightMapValue(uxx, uz);
				//设置颜色

				paint.setColor(Color.argb(80, 0, 0, 0));
				paint.setStyle(Paint.Style.FILL);
				LinearGradient gradient = new LinearGradient(Startx, Startz, Startx + (map.getWidth() / 3), Startz + map.getHeight(), Color.argb(50, 0, 0, 0), Color.argb(0, 0, 0, 0), Shader.TileMode.MIRROR);
				paint.setShader(gradient);
				//只有侧面阴影

				if (besideH > H && behindH <= H) {
					gradient = new LinearGradient(Startx, Startz, Startx + map.getWidth(), Startz, Color.argb(255, 0, 0, 0), Color.argb(0, 0, 0, 0), Shader.TileMode.MIRROR);
					paint.setShader(gradient);
					canvas.drawRect(Startx, Startz, Startx + map.getWidth(), Startz + map.getHeight(), paint);
				} else
				//只有底部
				if (behindH > H && besideH <= H) {
					gradient = new LinearGradient(Startx, Startz, Startx, Startz + map.getHeight(), Color.argb(255, 0, 0, 0), Color.argb(0, 0, 0, 0), Shader.TileMode.MIRROR);
					paint.setShader(gradient);
					canvas.drawRect(Startx, Startz, Startx + map.getWidth(), Startz + map.getHeight(), paint);
				} else
				//底部+侧面
				if (behindH > H && besideH > H) {
					gradient = new LinearGradient(Startx, Startz, Startx + map.getWidth(), Startz, Color.argb(255, 0, 0, 0), Color.argb(0, 0, 0, 0), Shader.TileMode.MIRROR);
					paint.setShader(gradient);
					canvas.drawRect(Startx, Startz, Startx + map.getWidth(), Startz + map.getHeight(), paint);
					gradient = new LinearGradient(Startx, Startz, Startx, Startz + map.getHeight(), Color.argb(255, 0, 0, 0), Color.argb(0, 0, 0, 0), Shader.TileMode.MIRROR);
					paint.setShader(gradient);
					canvas.drawRect(Startx, Startz, Startx + map.getWidth(), Startz + map.getHeight(), paint);
				} else
				//斜对角
				if (DiagonallyH > H) {
					gradient = new LinearGradient(Startx, Startz, Startx + map.getWidth(), Startz + map.getHeight(), Color.argb(255, 0, 0, 0), Color.argb(0, 0, 0, 0), Shader.TileMode.MIRROR);
					paint.setShader(gradient);
					canvas.drawRect(Startx, Startz, Startx + map.getWidth(), Startz + map.getHeight(), paint);
				} else {

					/*
					 //直接一格
					 if(behindH>H||besideH>H){
					 canvas.drawRect(Startx, Startz,Startx+ map.getWidth(),Startz+ map.getHeight(), paint);
					 }
					 */
					paint.setColor(Color.argb(80, 255, 255, 255));

					if (besideH < H) {
						gradient = new LinearGradient(Startx, Startz, Startx + map.getWidth(), Startz, Color.argb(255, 255, 255, 255), Color.argb(0, 0, 0, 0), Shader.TileMode.MIRROR);
						paint.setShader(gradient);
						canvas.drawRect(Startx, Startz, Startx + map.getWidth(), Startz + map.getHeight(), paint);
					}
					if (behindH < H) {
						gradient = new LinearGradient(Startx, Startz, Startx , Startz + map.getHeight() , Color.argb(255, 255, 255, 255), Color.argb(0, 0, 0, 0), Shader.TileMode.MIRROR);
						paint.setShader(gradient);
						canvas.drawRect(Startx, Startz, Startx + map.getWidth(), Startz + map.getHeight() , paint);
					}
				}
				/*
				 if(behindH<H||besideH<H){
				 canvas.drawRect(Startx, Startz,Startx+ map.getWidth(),Startz+ map.getHeight(), paint);
				 }
				 */
			}
		}
		//ckm.destoryChunks(x,z);
        cks.DestoryTerrain();
        canvas = null;
		return bmp;
	}
	public String toColor(int num) {
		return HexToStr(num) + HexToStr(num) + HexToStr(num);
	}
	public String HexToStr(int i) {
		String s = "0123456789abcdef";
		StringBuffer sb = new StringBuffer();
		for (int j = 0; i >= 16; j++) {
			int a = i % 16;
			i /= 16;
			sb.append(s.charAt(a));
		}
		sb.append(s.charAt(i));
		return sb.reverse().toString();
	}
}
