package chineseframe;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class 日期工具 {
    private static final SimpleDateFormat DATE_FORMAT_DATETIME = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final SimpleDateFormat DATE_FORMAT_DATE = new SimpleDateFormat("yyyy-MM-dd");
    private static final SimpleDateFormat DATE_FORMAT_TIME = new SimpleDateFormat("HH:mm:ss");

    public static String 格式化日期时间(long date) {
        return DATE_FORMAT_DATETIME.format(new Date(date));
    }

    public static String 格式化日期(long date) {
        return DATE_FORMAT_DATE.format(new Date(date));
    }

    public static String 格式化时间(long date) {
        return DATE_FORMAT_TIME.format(new Date(date));
    }

    public static String 自定义格式的格式化日期时间(String beginDate, String format) {
        return new SimpleDateFormat(format).format(new Date(Long.parseLong(beginDate)));
    }

    public static String 自定义格式的格式化日期时间(Date beginDate, String format) {
        return new SimpleDateFormat(format).format(beginDate);
    }

    public static Date 将时间字符串转换成Date(String s, String style) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat();
        simpleDateFormat.applyPattern(style);
        Date date = null;
        if (s == null || s.length() < 6) {
            return null;
        }
        try {
            date = simpleDateFormat.parse(s);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static String 获取系统时间() {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(System.currentTimeMillis());
        return cal.get(Calendar.HOUR_OF_DAY) + ":" + cal.get(Calendar.MINUTE) + ":" + cal.get(Calendar.SECOND);
    }

    public static String 获取系统日期() {
        return new SimpleDateFormat("yyyyMMdd").format(System.currentTimeMillis());
    }
    public static String 获取系统日期时间(){
        return DATE_FORMAT_DATETIME.format(System.currentTimeMillis());
    }
    public static String 获取系统日期时间(String format){
        return new SimpleDateFormat(format).format(System.currentTimeMillis());
    }

    public static long 计算两个时间差(Date dateStart, Date dateEnd) {
        return dateEnd.getTime() - dateStart.getTime();
    }

    public static Date 得到几天后的时间(Date d, int day) {
        Calendar now = Calendar.getInstance();
        now.setTime(d);
        now.set(Calendar.DATE, now.get(Calendar.DATE) + day);
        return now.getTime();
    }

    public static int 获取当前时间为本月的第几周() {
        Calendar calendar = Calendar.getInstance();
        int week = calendar.get(Calendar.WEEK_OF_MONTH);
        return week - 1;
    }

    public static int 获取当前时间为本周的第几天() {
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        if (day == 1) {
            day = 7;
        } else {
            day = day - 1;
        }
        return day;
    }
    
    public static long 毫秒时间戳() {
        return System.currentTimeMillis(); 
    }
    
    public static long 秒时间戳() {
        return System.currentTimeMillis() / 1000;
    }
    
    public static long 分钟时间戳() {
        return System.currentTimeMillis() / 1000 / 60;
    }
    
    public static long 小时时间戳() {
        return System.currentTimeMillis() / 1000 / (60 * 60);
    }
    
    public static long 天时间戳() {
        return System.currentTimeMillis() / 1000 / (60 * 60 * 24);
    }
    
}
