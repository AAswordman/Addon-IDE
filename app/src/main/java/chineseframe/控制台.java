package chineseframe;
import android.app.Application;

public abstract class 控制台 extends Application
{
	public static 活动 当前活动;
	public abstract void 主线程();

	@Override
	public void onCreate()
	{
		super.onCreate();
		主线程();
	}
	
}
