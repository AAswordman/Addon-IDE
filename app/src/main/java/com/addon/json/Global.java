package com.addon.json;
import org.json.JSONArray;
import bms.helper.script.json.JSONObjectBase;
import bms.helper.script.json.JSONObjectTag;
import org.json.JSONException;
import bms.helper.script.json.JSONArrayTool;

public class Global extends JSONObjectBase {
    public Global() {

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

    public static JSONArray openFilePaths=new JSONArray();
    public static int addOpenFile(String s) {
        int i=0;
        for (Object o:new JSONArrayTool(openFilePaths)) {
            if (o.equals(s)) {
                return i;
            }
            i++;
        }
        if (openFilePaths.length() > 7) {
            openFilePaths.remove(0);
        }

        openFilePaths.put(s);

        return -1;
    }
    public static boolean openFileWillExceed(String path) {
        for (Object o:new JSONArrayTool(openFilePaths)) {
            if (o.equals(path)) {
                return false;
            }
        }
        return openFilePaths.length() > 7;
    }

}
