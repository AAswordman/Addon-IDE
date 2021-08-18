package chineseframe;
import android.graphics.Paint;

public class 画笔
{
	public static Paint paint;
	public 画笔(Paint paint){
		this.paint=paint;
	}
	public void 设置颜色(int 颜色){
		paint.setColor(颜色);
	}
	public void 设置大小(int 大小){
		paint.setTextSize(大小);
	}
}
