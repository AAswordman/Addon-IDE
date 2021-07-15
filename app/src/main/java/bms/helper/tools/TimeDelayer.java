package bms.helper.tools;
import android.text.format.Time;
import java.util.Calendar;


public class TimeDelayer {
	public int delay;
	private int LastTime=0;
    public TimeDelayer(int p){
		SetDelay(p);
	}
    public void SetDelay(int p){
		delay=p;
	}
	public void UpdateLastTime(){
		LastTime=GetTime();
	}
	public boolean IsExceed(){
		if(LastTime+delay<GetTime()){
			UpdateLastTime();
			return true;
		}
		//LOG.print(LastTime+delay+"不通过"+LastTime,""+GetTime());
		return false;
	}
	public static int GetTime(){
		Calendar t=Calendar.getInstance();
		return t.get(Calendar.HOUR_OF_DAY)*60*60*1000 + 
			t.get(Calendar.MINUTE)*60*1000+
			t.get(Calendar.SECOND)*1000+
			t.get(Calendar.MILLISECOND);
	}
	public static void Stop(TimeDelayer delay){
		while(!delay.IsExceed()){
			try {
				Thread.sleep(delay.delay);
				//LOG.print("线程休眠",delay.delay+"");
			} catch (InterruptedException e) {}
		}
	}
}
