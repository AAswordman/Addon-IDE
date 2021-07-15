package bms.helper.tools;
import android.util.Log;
import bms.helper.io.CreateFile;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import bms.helper.Global;

public class LOG {
    public static void print(String head, String msg) {
		CreateFile.WriteAppend("/sdcard/"+Global.dir+"/printlog/log.txt", head + " : " + msg);
	}
	

    public static void print(Object obj) {
        Class<?> aClass = obj.getClass();
        // aClass.getFields() 获取 public 类型的成员
        Field[] declaredFields = aClass.getDeclaredFields(); //获取所有成员包含 private
        
        for (Field field:
        declaredFields) {
            try {
                field.setAccessible(true);
                String fn = field.getName();
                print(fn,field.get(obj).toString());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
}
