package bms.helper.app;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Message;
import android.os.Process;
import android.text.TextUtils;
import bms.helper.http.SendMain;
import chineseframe.CFile;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;
import okhttp3.OkHttpClient;
import java.util.Date;
import bms.helper.tools.Time;
import bms.helper.tools.LOG;
import bms.helper.Global;


public class CrashHandler implements UncaughtExceptionHandler{


    private static CrashHandler INSTANCE = null;
    private CrashHandler(){

    }
    public static CrashHandler getInstance(){
        if (INSTANCE == null) {
            synchronized (CrashHandler.class) {
                if (INSTANCE == null) {
                    INSTANCE = new CrashHandler();
                }
            }
        }
        return INSTANCE;
    }

    private Context mContext;
    private UncaughtExceptionHandler mDefautHandler;
    

    public void init(Context context){
        mContext = context;
        mDefautHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }


    @Override
    public void uncaughtException(Thread thread, Throwable ex) {

        if (!handlerCrash(ex) && mDefautHandler != null) {
            // 没有处理还交给系统默认的处理器
            mDefautHandler.uncaughtException(thread, ex);
        }else{
            // 已经处理，结束进程
            Process.killProcess(Process.myPid());
            System.exit(1);
            
        }


    }
    
    /**
     * 自定义处理策略
     * @return true：已处理
     */
    private boolean handlerCrash(Throwable ex) {
        if (ex == null) {
            return false;
        }

        // 收集设备信息、版本信息、异常信息
        String info = collectDeviceInfo(mContext,ex);
        // 本地固化存储
        saveInfo(info);

        // 上传服务器（该功能可以独立到外边，定时上传或者进入应用时检测上传）
        
        return true;
    }

    /**
     * 收集设备信息
     * @param mContext2
     * @param ex 
     * @param infos
     */
    private String collectDeviceInfo(Context c, Throwable ex) {
        Map<String, String> infos = new HashMap<String, String>();
        // 收集版本信息
        try {
            PackageManager pm = c.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(c.getPackageName(), PackageManager.GET_ACTIVITIES);
            if (pi != null) {
                String versionCode = pi.versionCode +"";
                String versionName = TextUtils.isEmpty(pi.versionName) ? "没有版本名称" : pi.versionName;
                infos.put("versionCode", versionCode);
                infos.put("versionName", versionName);
            }
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        
        
        // 收集设备信息
        Field[] fields = Build.class.getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                infos.put(field.getName(), field.get(null).toString());
            } catch (Exception e) {
            }
        }
        
        // 收集异常信息
        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        ex.printStackTrace(printWriter);
        Throwable cause = ex.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        printWriter.close();
        String result = writer.toString();

        // 转化为字符串
        StringBuffer sb = new StringBuffer();
        for (Map.Entry<String, String> entry : infos.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            sb.append(key + "=" + value + "\n");
        }
        sb.append(result);

        return sb.toString();
    }


    /**
     * 保存异常信息到本地
     * @param infos
     */
    private void saveInfo(String infos) {
        // 把采集到的信息写入到本地文件
        Time time=new Time();
        String sdf=time.getMonth()+"-"+time.getDay()+"-"+time.getHours()+"-"+time.getMinutes();
        try {
            new CFile("/sdcard/"+Global.dir+"/bug/"+sdf+".log")
                .write(infos);
            new CFile("/sdcard/zk/report").write("/sdcard/zk/bug/"+sdf+".log");
        } catch (IOException e) {
            LOG.print("报错",EXPHelper.get(e));
        }
    }
}

