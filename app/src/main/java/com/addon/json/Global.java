package com.addon.json;
import org.json.JSONArray;
import bms.helper.script.json.JSONObjectBase;
import bms.helper.script.json.JSONObjectTag;

public class Global extends JSONObjectBase{
    public Global(){
        
    }
    public static boolean isInProject=false;
    
    public static int isInWhat;
    
    public static JSONArray sampleBehaviorPath=new JSONArray();
    public static JSONArray samplePvzModPath=new JSONArray();
    public static JSONArray sampleResourcePath=new JSONArray();
    public static String BehaviorPath=null;
    public static String ResourcePath=null;
    public static String PvzModPath=null;
    
    public static String importMapPath=null;
    public static String importPVZPath=null;
    public static JSONObjectTag<Global> i=new JSONObjectTag<>();
    
    
}
