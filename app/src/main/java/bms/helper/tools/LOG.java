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
    public static void print(String head, byte[] msg) {
        CreateFile.WriteAppend("/sdcard/"+Global.dir+"/printlog/log.txt", head + " : " + byteArrayToHexStr(msg));
	}
	public static String byteArrayToHexStr(byte[] byteArray) {
        if (byteArray == null) {
            return null;
        }
        char[] hexArray = "0123456789ABCDEF".toCharArray();
        char[] hexChars = new char[byteArray.length * 2];
        for (int j = 0; j < byteArray.length; j++) {
            int v = byteArray[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
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
