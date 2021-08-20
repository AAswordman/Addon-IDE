package bms.helper.tools;
import java.util.Date;
import java.util.Calendar;
import java.io.Serializable;

public class Time implements Serializable{
    private Calendar calendar;
    private static final long serialVersionUID = 1L;
    public Time(){
        calendar=Calendar.getInstance();
    }
    public Time(long f){
        calendar=Calendar.getInstance();
        calendar.setTimeInMillis(f);
    }
    @Override
    public Object clone() {
        Time clone=new Time();
        clone.setTime(getYear(),getMonth(),getDay(),getHours(),getMinutes(),getSeconds());
        clone.getCalendar().set(Calendar.MILLISECOND,getMillisecond());
        return clone;
    }
    
    public Calendar getCalendar(){
        return calendar;
    }
    
    public long getTime() {
        return calendar.getTimeInMillis();
    }

    
    public int getDay() {
        return calendar.get(calendar.DAY_OF_MONTH);
    }
    
    public int getDayOfYear() {
        return calendar.get(calendar.DAY_OF_YEAR);
    }
    
    
    public int getHours() {
        return calendar.get(calendar.HOUR_OF_DAY);
    }

    
    public int getMinutes() {
        return calendar.get(calendar.MINUTE);
    }

    
    public int getMonth() {
        return calendar.get(calendar.MONTH)+1;
    }

    
    public int getSeconds() {
        return calendar.get(calendar.SECOND);
    }
    public int getMillisecond() {
        return calendar.get(calendar.MILLISECOND);
    }
    
    public int getYear() {
        return calendar.get(calendar.YEAR);
    }
    public float getDisparityHours(Time time){
        return Math.abs(getTime()-time.getTime())/1000/60/60;
    }

    
    public void setTime(long time) {
        calendar.setTimeInMillis(time);
    }
    public void setTime(int year,int mouth,int date,int hour,int min,int s){
        calendar.set(year,mouth-1,date,hour,min,s);
    }
    public void setTime(int mouth,int date,int hour,int min,int s){
        setTime(getYear(),mouth,date,hour,min,s);
    }
    public void setTime(int date,int hour,int min,int s){
        setTime(getMonth(),date,hour,min,s);
    }
    
    public void addDate(int m){
        calendar.add(Calendar.DAY_OF_MONTH,m);
    }

    @Override
    public String toString() {
        return getYear()+","+getMonth()+"/"+getDay()+","+getHours()+":"+getMinutes()+":"+getSeconds()+"，"+getMillisecond();
    }
    
}
