package chineseframe;



public class 进制工具 {
    //16->2
    public static String 十六转二(String hexString) {
        if (hexString.length() % 2 != 0) {
            hexString = "0" + hexString;
        }
        String bString = "", tmp;
        for (int i = 0; i < hexString.length(); i++) {
            tmp = "0000" + Integer.toBinaryString(Integer.parseInt(hexString.substring(i, i + 1), 16));
            bString += tmp.substring(tmp.length() - 4);
        }
        return bString;
    }

    //2->16
    public static String 二转十六(String bString) {
        if (bString.length() % 8 != 0) {
            String sbuwei = "00000000";
            bString = sbuwei.substring(0, sbuwei.length() - bString.length() % 8) + bString;
        }
        StringBuilder tmp = new StringBuilder();
        int iTmp = 0;
        for (int i = 0; i < bString.length(); i += 4) {
            iTmp = 0;
            for (int j = 0; j < 4; j++) {
                iTmp += Integer.parseInt(bString.substring(i + j, i + j + 1)) << (4 - j - 1);
            }
            tmp.append(Integer.toHexString(iTmp));
        }
        return tmp.toString();
    }

}
