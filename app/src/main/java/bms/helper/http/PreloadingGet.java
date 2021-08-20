package bms.helper.http;

import okhttp3.OkHttpClient;
import org.json.JSONObject;
import android.os.Message;
import org.json.JSONException;
import bms.helper.tools.TimeDelayer;

public class PreloadingGet {
    private static JSONObject getLoading=new JSONObject();
    public static void get(final String url, JSONObject json,final int time){
        new SendMain(url, json, new SendMain.Function(){
                @Override
                public void OnReturn(String result) {
                    try {
                        getLoading.put(url, new Data(result,time));
                    } catch (JSONException e) {}
                }

                @Override
                public void MainThread(Message msg) {
                }
            }).getUseCookie();
    }
    public static void get(OkHttpClient client, JSONObject text, final String uri, JSONObject json,final int time){
        new SendMain(client, text, uri, json, new SendMain.Function(){
                @Override
                public void OnReturn(String result) {
                    try {
                        getLoading.put(uri, new Data(result, time));
                    } catch (JSONException e) {}
                }

                @Override
                public void MainThread(Message msg) {
                }
            }).getUseCookie();
    }
    public static boolean hasUrl(String url){
        if(getLoading.has(url)){
            if(((Data)getLoading.opt(url)).delay.IsExceed()){
                getLoading.remove(url);
                return false;
            }else{
                return true;
            }
        }else{
            return false;
        }
    }
    public static String getResult(String url){
        Data data=(PreloadingGet.Data) getLoading.opt(url);
        getLoading.remove(url);
        return data.data;
    }
    public static void reset(){
        getLoading=new JSONObject();
    }
    public static class Data{
        public TimeDelayer delay;
        public String data;
        public Data(String data,int time){
            delay=new TimeDelayer(time);
            this.data=data;
            delay.UpdateLastTime();
        }
    }
}
