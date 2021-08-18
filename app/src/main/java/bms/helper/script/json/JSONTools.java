package bms.helper.script.json;

import java.lang.reflect.Field;
import org.json.JSONException;
import org.json.JSONObject;

public class JSONTools {


    public static JSONObject parse(String var0) {
        JSONObject var1 = (JSONObject)null;

        JSONObject var2;
        JSONObject var4;
        try {
            var2 = new JSONObject(var0);
        } catch (JSONException var3) {
            var4 = var1;
            return var4;
        }

        var4 = var2;
        return var4;
    }
    public static JSONObjectBase parse(Class<? extends JSONObjectBase> objcls, String jsonstr) {
        JSONObject json = null;
        try {
            json = new JSONObject(jsonstr);
        } catch (JSONException e) {}
        return parse(objcls,json);
    }
    public static JSONObjectBase parse(Class<? extends JSONObjectBase> objcls, JSONObject json) {

        JSONObjectBase obj = null;
        if (json.has("_*example")) {
            obj = (JSONObjectBase) json.opt("_*example");
        } else {
            try {
                obj = objcls.newInstance();
                try {
                    json.put("_*example", obj);
                } catch (JSONException e) {}
            } catch (InstantiationException e) {} catch (IllegalAccessException e) {}
        }
        parse(obj, json);
        return obj;
    }
    private static void parse(JSONObjectBase obj, JSONObject json) {
        obj.OriginalJson = json;

        Class<?> aClass =obj.getClass();
        Field[] declaredFields = aClass.getDeclaredFields(); //获取所有成员包含 private
        for (Field field:
        declaredFields) {
            try {
                field.setAccessible(true);
                Object var=field.get(obj);
                if (var instanceof JSONObjectBase) {
                    if (json.has(field.getName())) {
                        JSONTools.parse((JSONObjectBase)var, json.optJSONObject(field.getName()));
                    }
                } else if (var instanceof JSONObjectTag) {

                } else {
                    if (json.has(field.getName())) {
                        field.set(obj, json.opt(field.getName()));
                    }
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
}


