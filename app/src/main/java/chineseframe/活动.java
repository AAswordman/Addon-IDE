package chineseframe;

import android.view.View.*;
import android.content.pm.PackageManager.*;
import android.app.ActivityManager.MemoryInfo;
import android.content.Intent.ShortcutIconResource;
import android.app.Activity;
import android.view.View;
import android.os.Bundle;
import android.util.Log;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.app.ActivityManager;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.content.Context;
import android.net.NetworkInfo;
import android.content.ContentResolver;
import android.content.ComponentName;
import java.util.List;
import java.security.MessageDigest;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;

import java.io.File;
import java.io.FileFilter;
import java.util.regex.Pattern;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.security.NoSuchAlgorithmException;
import android.database.Cursor;
import android.widget.Toast;
import android.content.pm.ApplicationInfo;
import android.graphics.drawable.Drawable;
import java.io.DataOutputStream;
import android.text.TextUtils;
import java.util.Calendar;
import java.text.SimpleDateFormat;
import android.content.res.Resources;
import android.os.Handler;
import android.location.LocationManager;

/*
快速开发框架
*/
public abstract class 活动 extends Activity
{
	public boolean 副线程=false;
	public long 副线程休眠时间=1000;
	
	public abstract void 主线程()
	public abstract void Ui线程()
	public abstract void 副线程()
	public abstract void 子线程()
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
	
		Ui线程();
		主线程();
		副线程();
		子线程();
	

	}
	public void 加载布局(int 资源id)
	{
		setContentView(资源id);
	}
	public void 加载控件(View 视图)
	{
		setContentView(视图);
	}
	public int 获取版本号()
	{
        int verCode = -1;
        try
		{
            String packageName =this.getPackageName();
            verCode = this.getPackageManager()
				.getPackageInfo(packageName, 0).versionCode;
        }
		catch (PackageManager.NameNotFoundException e)
		{
            e.printStackTrace();
        }
        return verCode;
    }

    public static long 获取应用运行的最大内存()
	{
        return Runtime.getRuntime().maxMemory() / 1024;
    }
	public void 安装Apk(File Apk路径)
	{
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(Apk路径),
							  "application/vnd.android.package-archive");
        this.startActivity(intent);
    }
	public void 卸载Apk(String 包名)
	{
        Intent intent = new Intent(Intent.ACTION_DELETE);
        Uri packageURI = Uri.parse("package:" + 包名);
        intent.setData(packageURI);
        this.startActivity(intent);
    }
	public static int 获取CPU核心数()
	{
        try
		{
            File dir = new File("/sys/devices/system/cpu/");
            File[] files = dir.listFiles(new FileFilter() {
					@Override public boolean accept(File pathname)
					{
						if (Pattern.matches("cpu[0-9]", pathname.getName()))
						{
							return true;
						}
						return false;
					}
				});
            return files.length;
        }
		catch (Exception e)
		{
            return 1;
        }
    }
	public int 获取可用空间大小()
	{
        ActivityManager am = (ActivityManager) this.getSystemService(
			Context.ACTIVITY_SERVICE);
        MemoryInfo mi = new MemoryInfo();
        am.getMemoryInfo(mi);

        return (int) (mi.availMem / (1024 * 1024));
    }

	public int 获取手机系统SDK版本()
	{
        return android.os.Build.VERSION.SDK_INT;
    }

    public String 获取asset文件下的文件(String 路径)
	{

        try
		{
            InputStreamReader inputReader = new InputStreamReader(
				this.getAssets().open(路径));
            BufferedReader bufReader = new BufferedReader(inputReader);
            String line = "";
            String Result = "";
            while ((line = bufReader.readLine()) != null)
			{
                Result += line;
            }
            return Result;
        }
		catch (Exception e)
		{
            e.printStackTrace();
        }
        return null;
    }
	public boolean 获取Wifi连接状态()
	{
        ConnectivityManager connectivityManager
			= (ConnectivityManager) this.getSystemService(
			Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiNetworkInfo = connectivityManager.getNetworkInfo(
			ConnectivityManager.TYPE_WIFI);
        if (wifiNetworkInfo.isConnected())
		{
            return true;
        }
        return false;
    }
	
	
	public void 提示(String 内容, int 显示时间)
	{
		Toast.makeText(this, 内容, 显示时间).show();
	}

	
	public boolean 获取网络状态()
	{
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null)
		{
            NetworkInfo networkInfo = cm.getActiveNetworkInfo();
            if (networkInfo != null)
			{
                return true;
            }
        }

        return false;
    }
	public boolean 获取Gps状态()
	{
        LocationManager lm = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        List<String> accessibleProviders = lm.getProviders(true);
        for (String name : accessibleProviders)
		{
            if ("gps".equals(name))
			{
                return true;
            }
        }
        return false;
    }
	public void Log排查(String 标题, String 内容)
	{
		Log.e(标题, 内容);
	}
	public  String md5加密(String 内容)
	{
        String result = "";
        try
		{
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(内容.getBytes());
            byte b[] = md.digest();

            int i;

            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++)
			{
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            result = buf.toString().toLowerCase();// 32位的加密（转成小写）

            buf.toString().substring(8, 24);// 16位的加密

        }
		catch (NoSuchAlgorithmException e)
		{
            e.printStackTrace();
        }
        return result;
    }
	public String 获取应用名称( String 包名) {
        PackageManager pm = this.getPackageManager();
        String appName = null;
        try {
            ApplicationInfo applicationInfo = pm.getApplicationInfo(包名, 0);
            appName = String.valueOf(pm.getApplicationLabel(applicationInfo));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return appName;
    }
	public Drawable 获取应用图标( String 包名) {
        PackageManager pm = this.getPackageManager();
        Drawable appIcon = null;
        try {
            ApplicationInfo applicationInfo = pm.getApplicationInfo(包名, 0);
            appIcon = applicationInfo.loadIcon(pm);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return appIcon;
    }
	
	public long 获取应用大小(String 包名) {
        long appSize = 0;
        try {
            ApplicationInfo applicationInfo = this.getPackageManager().getApplicationInfo(包名, 0);
            appSize = new File(applicationInfo.sourceDir).length();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return appSize;
    }
	public boolean 获取Root权限() {
        String packageCodePath = this.getPackageCodePath();
        Process process = null;
        DataOutputStream os = null;
        try {
            String cmd = "chmod 777 " + packageCodePath;
            process = Runtime.getRuntime().exec("su");
            os = new DataOutputStream(process.getOutputStream());
            os.writeBytes(cmd + "\n");
            os.writeBytes("exit\n");
            os.flush();
            process.waitFor();
        } catch (Exception e) {
            return false;
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
                process.destroy();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return true;
    }
	public  boolean 获取权限状态(String 权限名) {
        if (this != null && !TextUtils.isEmpty(权限名)) {
            try {
                PackageManager packageManager = this.getPackageManager();
                if (packageManager != null) {
                    if (PackageManager.PERMISSION_GRANTED == packageManager.checkPermission(权限名, this
																							.getPackageName())) {
                        return true;
                    }
                    Log.d("AppUtils", "Have you  declared permission " + 权限名 + " in AndroidManifest.xml ?");
                }
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    public boolean 获取应用安装状态(String 包名) {
        boolean installed = false;
        if (TextUtils.isEmpty(包名)) {
            return false;
        }
        List<ApplicationInfo> installedApplications = this.getPackageManager().getInstalledApplications(0);
        for (ApplicationInfo in : installedApplications) {
            if (包名.equals(in.packageName)) {
                installed = true;
                break;
            } else {
                installed = false;
            }
        }
        return installed;
    }
	public boolean 是否系统应用(String 包名) {
        boolean isSys = false;
        PackageManager pm = this.getPackageManager();
        try {
            ApplicationInfo applicationInfo = pm.getApplicationInfo(包名, 0);
            if (applicationInfo != null && (applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) > 0) {
                isSys = true;
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            isSys = false;
        }
        return isSys;
    }

    public String 获取系统时间() {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(System.currentTimeMillis());
        return cal.get(Calendar.HOUR_OF_DAY) + ":" + cal.get(Calendar.MINUTE) + ":" + cal.get(Calendar.SECOND);
    }

    public String 获取系统日期() {
        return new SimpleDateFormat("yyyyMMdd").format(System.currentTimeMillis());
    }
	public  int 获取屏幕长度() {
        return this.getResources().getDisplayMetrics().widthPixels;
    }

    public  int 获取屏幕宽度() {
        return this.getResources().getDisplayMetrics().heightPixels;
    }

    public int 获取标题栏宽度() {
        Resources resources = this.getResources();
        int identifier = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        return resources.getDimensionPixelOffset(identifier);
    }
    public  int 获取状态栏宽度() {
        Class<?> c;
        Object obj;
        Field field;
        int statusBarHeight = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            int x = Integer.parseInt(field.get(obj).toString());
            statusBarHeight = this.getResources().getDimensionPixelSize(x);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return statusBarHeight;
    }
	public void 活动结束(){
		this.finish();
	}

    public void 返回桌面() {
        Intent intent = new Intent("android.intent.action.MAIN");
        intent.setFlags(268435456);
        intent.addCategory("android.intent.category.HOME");
        this.startActivity(intent);
    }
	public void 设置标题(String 标题){
		setTitle(标题);
	}
	public 控制台 获取控制台(){
		return (控制台)getApplication();
	}
};