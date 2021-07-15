package chineseframe;
import android.app.Service;

public abstract class 服务 extends Service
{
	public abstract void 主线程()
	@Override
	public void onCreate()
	{
		super.onCreate();
		主线程();
	}
	
}
