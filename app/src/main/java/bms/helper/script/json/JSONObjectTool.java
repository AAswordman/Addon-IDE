package bms.helper.script.json;

//
// Decompiled by FernFlower - 744ms
//
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import bms.helper.tools.StringBuilderMemory;
import bms.helper.tools.LOG;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.Iterator;

public class JSONObjectTool {

    
    public JSONObject toObjAll() {
        Iterator<String> it=json.keys();
        while (it.hasNext()) {
            String str=it.next();
            Object o=json.opt(str);
            if (o instanceof JSONObject) {
                try {
                    json.put(str, new JSONObjectTool((JSONObject)o).toObjAll());
                } catch (JSONException e) {}
            } else if (o instanceof JSONArray) {
                try {
                    json.put(str, new JSONArrayTool((JSONArray)o).toObjAll());
                } catch (JSONException e) {}
            }
        }
        return json;
    }
    protected JSONObject json;

    public JSONObjectTool(JSONObject var1) {
        this.json = var1;
    }
    public JSONObjectTool() {
        this.json = new JSONObject();
    }
    public Object g(String var1) {
        return this.json.opt(var1);
    }

    public JSONArray ga(String var1) {
        return this.json.optJSONArray(var1);
    }
    public synchronized JSONArray gau(String key) {
        if (json.has(key)) {
            return ga(key);
        } else {
            JSONArray obj=new JSONArray();
            p(key, obj);
            return obj;
        }
    }
    public double gd(String var1) {
        return this.json.optDouble(var1, 0);
    }
    public int gi(String var1) {
        return this.json.optInt(var1);
    }
    public float gf(String var1) {
        return (float)this.json.optDouble(var1);
    }

    public JSONObjectTool gj(String var1) {
        return new JSONObjectTool(this.json.optJSONObject(var1));
    }
    public synchronized JSONObjectTool gju(String key) {
        if (json.has(key)) {
            return gj(key);
        } else {
            JSONObjectTool obj=new JSONObjectTool();
            p(key, obj.toJSON());
            return obj;
        }
    }
    public long gl(String var1) {
        return this.json.optLong(var1);
    }

    public String gs(String var1) {
        return this.json.optString(var1);
    }

    public JSONObjectTool d(String key) {
        json.remove(key);
        return this;
    }
    public boolean h(String key) {
        return json.has(key);
    }
    public JSONObjectTool p(String var1, Object var2) {
        try {
            this.json.put(var1, var2);
        } catch (JSONException var3) {
        }

        return this;
    }

    public JSONObject toJSON() {
        return this.json;
    }

    @Override
    public String toString() {
        return json.toString();
    }


    public String toString(int s) {
        String j = null;
        try {
            j = json.toString(s);
        } catch (JSONException e) {}
        StringBuilderMemory sb=new StringBuilderMemory().append(j);
        Matcher m=Pattern.compile("\\[((?:-?[0-9]*\\.?[0-9]*)*|,| |\n)*\\]").matcher(j);
        while (m.find()) {
            sb.replace(m.group(0), m.group(0).replaceAll("( |\n)", ""));
        }
        sb.replace("\\/", "/");
        return sb.toString();
    }
}


