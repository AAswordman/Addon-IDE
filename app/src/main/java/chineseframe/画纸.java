package chineseframe;
import android.graphics.Canvas;

public class 画纸 
{
	public static Canvas canvas;
	public 画纸(Canvas canvas){
		this.canvas=canvas;
	}
	public void 画直线(float 开始x,float 开始y,float 结束x,float 结束y, 画笔 画笔){
		canvas.drawLine(开始x,开始y,结束x,结束y,画笔.paint);
	}
	public void 画点(float 开始x,float 开始y,画笔 画笔){
		canvas.drawPoint(开始x,开始y,画笔.paint);
	}
}
