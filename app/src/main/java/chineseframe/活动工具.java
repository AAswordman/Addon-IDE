package chineseframe;

import android.app.Activity;
import android.content.Intent;

public class 活动工具
{
	public static Object[] 数据;
	public static void 活动跳转(Activity act1,Activity act2){
		act1.startActivity(new Intent(act1,act2.getClass()));
		act1.finish();
	}
}
