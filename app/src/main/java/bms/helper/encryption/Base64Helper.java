package bms.helper.encryption;
import android.graphics.Bitmap;
import android.util.Base64;
import java.io.ByteArrayOutputStream;

public class Base64Helper {
    /**
     * 由本地路径获取图片
     * 再将Bitmap转换成Base64字符串
     * @param pathString 本地图片路径
     * @return
     */
    public static String getDiskBitmap2Base64(Bitmap bitmap){
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        //参数2：压缩率，40表示压缩掉60%; 如果不压缩是100，表示压缩率为0
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
        byte[] bytes = bos.toByteArray();
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }
    
    
    
}
