package bms.helper.encryption;

import java.math.BigInteger;
import java.security.MessageDigest;

public class MD5Helper {
	private String str;
	public MD5Helper(String base) {
		str = base;
	}
    public String getMD5String() {
        try {
            // 生成一个MD5加密计算摘要
            MessageDigest md = MessageDigest.getInstance("MD5");
            // 计算md5函数
            md.update(str.getBytes());
            // digest()最后确定返回md5 hash值，返回值为8位字符串。因为md5 hash值是16位的hex值，实际上就是8位的字符
            // BigInteger函数则将8位的字符串转换成16位hex值，用字符串来表示；得到字符串形式的hash值
            String md5code=new BigInteger(1, md.digest()).toString(16);
            for (int i = 0; i < 32 - md5code.length(); i++) {
                md5code = "0" + md5code;
            }
            //一个byte是八位二进制，也就是2位十六进制字符（2的8次方等于16的2次方）
            return md5code;
        } catch (Exception e) {
			e.printStackTrace();
			return null;
        }
    }
}
