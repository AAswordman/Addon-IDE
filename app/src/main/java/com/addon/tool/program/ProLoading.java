package com.addon.tool.program;
import android.content.Context;
import android.widget.Toast;
import bms.helper.io.AssetsUtil;
import bms.helper.script.json.JSONArrayTool;
import bms.helper.script.json.JSONObjectTool;
import bms.helper.tools.Time;
import bms.quote.Ldb;
import chineseframe.CFile;
import com.addon.config.Config;
import com.addon.json.Global;
import com.addon.tool.MainActivity;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import bms.helper.tools.LOG;
import android.app.Activity;
import java.util.HashMap;
import com.addon.tool.program.ProLoading.Tips;
import java.util.HashSet;
import java.util.LinkedHashSet;
import com.addon.config.SpecialLdb;

public class ProLoading {
    //数据库
    private static Ldb ldb;

    //储存加载信息
    private static JSONObjectTool data;

    //线程池
    private static ArrayBlockingQueue arrayBlockingQueue;
    private static ThreadFactory namedThreadFactory;
    private static ThreadPoolExecutor tdpool;
    private static String jsonFrom;

    private static JSONObject behData;

    private static JSONObject resData;

    private static JSONObject plugData;

    public static int total;
    public static int now;


    public static JSONObject allJSONlist;
    //把指定类型加载到ldb中的入口
    public static void load(final Activity c, final int type) {

        JSONObject obj = null;
        JSONArray samplePaths = null;
        total = 1;
        now = 0;
        CFile file=new CFile(Config.baseDir + "proCompletion");

        ldb = new Ldb(file.file);
        allJSONlist = new JSONObject();
        switch (type) {
            case MainActivity.BEHAVIORS:{
                    try {
                        jsonFrom = "beh.json";
                        obj = new JSONObject(AssetsUtil.getFromAssets(c, jsonFrom));
                    } catch (JSONException e) {}
                    samplePaths = Global.sampleBehaviorPath;
                    break;
                }
            case MainActivity.RESOURCES:{
                    try {
                        jsonFrom = "res.json";
                        obj = new JSONObject(AssetsUtil.getFromAssets(c, jsonFrom));
                    } catch (JSONException e) {}
                    samplePaths = Global.sampleResourcePath;
                    break;
                }
            case MainActivity.PVZPLUG:{
                    try {
                        jsonFrom = "plug.json";
                        obj = new JSONObject(AssetsUtil.getFromAssets(c, jsonFrom));
                    } catch (JSONException e) {}
                    samplePaths = Global.samplePvzModPath;
                    break;
                }
        }
        data = new JSONObjectTool();
        JSONObjectTool nFiles=data.gju("File");

        JSONObject files=obj.optJSONObject("File");

        Iterator<String> it=files.keys();
        arrayBlockingQueue = new ArrayBlockingQueue(256);
        namedThreadFactory = new ThreadFactory(){
            @Override
            public Thread newThread(Runnable p1) {
                return new Thread(p1);
            }
        };
        tdpool = new ThreadPoolExecutor(5, 15, 5, TimeUnit.SECONDS, arrayBlockingQueue, namedThreadFactory, new ThreadPoolExecutor.CallerRunsPolicy());

        while (it.hasNext()) {
            //文件名
            String p=it.next();
            JSONObjectTool nfileMsg=nFiles.gju(p);
            JSONObject filePV=files.optJSONObject(p);

            //取样所有文件
            for (int i=0,maxL=samplePaths.length();i < maxL;i++) {
                String basePath=samplePaths.optString(i);
                handleFile(p, basePath + p, filePV, nfileMsg);

            }
        }
        tdpool.shutdown();
        while (true) {
            if (tdpool.isTerminated()) {
                System.out.println("over");
                break;
            }
        }
        it=allJSONlist.keys();
        while(it.hasNext()){
            String key= it.next();
            ldb.put(key.getBytes(),allJSONlist.opt(key).toString().getBytes());
        }
        
        ldb.close();
        try {
            new CFile(Config.baseDir + jsonFrom).write(data.toString());
        } catch (IOException e) {}


    }

    public static int getProgress() {
        return now * 100 / total;
    }

    //处理文件夹
    private static void handleFile(String belong, String p, JSONObject filePV, JSONObjectTool nfileMsg) {
        handleFile(belong, new File(p), filePV, nfileMsg);
    }
    //处理单个文件

    //在处理加载进度时以这里为主
    private static void handleFile(final String belong, File p, final JSONObject filePV, final JSONObjectTool nfileMsg) {
        final CFile file=new CFile(p);
        if (file.file.isDirectory()) {
            for (File f:file.file.listFiles()) {
                handleFile(belong, f, filePV, nfileMsg);
            }
        } else {
            total += 1;
            Runnable run=namedThreadFactory.newThread(new Runnable(){
                    @Override
                    public void run() {
                        JSONObject json;
                        try {
                            json = new JSONObject(file.read());
                        } catch (JSONException e) {
                            return;
                        } catch (IOException e) {
                            return;
                        }
                        //下一步操作
                        Iterator<String> everyJsonKey=filePV.keys();
                        ArrayList<Tips> tips;
                        while (everyJsonKey.hasNext()) {

                            //json的需要被补全部分
                            final String k=everyJsonKey.next();

                            JSONObject everyKeyFrom=filePV.optJSONObject(k);

                            Iterator<String> samplePath=everyKeyFrom.keys();

                            tips = new ArrayList<>();

                            boolean additional=false;
                            while (samplePath.hasNext()) {
                                //采样位置

                                String oldKey=samplePath.next();
                                String t=everyKeyFrom.optString(oldKey);
                                if (t.equals("kv"))additional = true;
                                if (oldKey.equals("~")) oldKey = k;
                                String[] layer;
                                if (oldKey.length() == 1) {
                                    layer = new String[]{};
                                } else {
                                    layer = oldKey.substring(1, oldKey.length() - 1).split("/");
                                }


                                processJsonPiece(json, tips, layer, -1, t);



                            }

                            //存档记录k和v全部信息key的位置
                            JSONArray kUseAer;
                            if (additional) {
                                kUseAer = nfileMsg.gju(k.replace("*", "[^/]*") + ".*").gau("keys");
                            } else {
                                kUseAer = nfileMsg.gju(k.replace("*", "[^/]*")).gau("keys");
                            }

                            //合并内容的储存前缀
                            //final String merge= nfileMsg.gju(k).gs("merge");
                            final String prefix=belong + "//" + k;



                            addMergeJSON(ldb, prefix, tips);

                            if (kUseAer.length() >= 1) {
                                try {
                                    String customaryKey=kUseAer.optLong(kUseAer.length() - 1) + "";
                                    JSONArray customaryArr= new JSONArray(new String(ldb.get(customaryKey)));


                                    Set<String> obj=new HashSet<>();

                                    for (Tips t : tips) {
                                        obj.add(t.toString());
                                    }

                                    if (customaryArr.length() + tips.size() < 100) {
                                        for (int i=0;i < customaryArr.length();i++) {
                                            obj.add(customaryArr.opt(i).toString());

                                        }
                                        //ldb.delete(key.getBytes());
                                        ldb.put(customaryKey.getBytes(), obj.toString().getBytes());
                                    } else {
                                        long nkey=new Time().getTime();
                                        ldb.put((nkey + "").getBytes(), tips.toString().getBytes());
                                        kUseAer.put(nkey);
                                    }

                                } catch (JSONException e) {}

                            } else {
                                long nkey=new Time().getTime();
                                ldb.put((nkey + "").getBytes(), tips.toString().getBytes());
                                kUseAer.put(nkey);
                            }


                        }
                        now += 1;

                    }


                });

            tdpool.execute(run);



        }
    }


    //实话说后面这一坨我也不知道我写了什么鬼东西
    //随缘看懂吧
    //总之能跑就行
    /*
     json合并思路:
     array是用来储存多个结果的
     原本的所有Array全部转化成obj，并且所有的obj合并到0的位置


     */
    private static void addMergeJSON(Ldb ldb, String prefix, ArrayList<Tips> tips) {

        for (final Tips t:tips) {

            final String k=(prefix + t.map.get("k"));
            final Object v=allJSONlist.opt(k);
            if (v != null) {
                final Object ox=t.map.get("v");
                if (ox != null) {
                    Object prepare = null;
                    if (v instanceof JSONObject) {

                        JSONObject jx=(JSONObject)(v);
                        if (ox instanceof JSONObject) {
                            addMergeJSON((JSONObject)jx, (JSONObject)ox);
                        } else if (ox instanceof JSONArray) {
                            addMergeJSON((JSONObject)jx, new MyJSONArrayTool((JSONArray)ox).toObjAll());
                        } else {

                            prepare = addMergeUnknown(jx, ox);

                        }
                        prepare = jx;


                    } else if (v instanceof JSONArray) {

                        JSONArray ja=(JSONArray)(v);

                        //这里也是代表多个结果
                        if (ox instanceof JSONObject) {
                            int i= arrayHasE(ja, ox);
                            if (i == -1) {
                                ja.put(ox);
                            } else if (i == -2) {

                            } else {
                                addMergeJSON(ja.optJSONObject(i), (JSONObject)ox);
                            }
                        } else if (ox instanceof JSONArray) {
                            JSONObject oj=new MyJSONArrayTool((JSONArray)ox).toObjAll();
                            int i= arrayHasE(ja, oj);

                            if (i == -1) {
                                ja.put(oj);
                            } else if (i == -2) {

                            } else {
                                addMergeJSON(ja.optJSONObject(i), oj);
                            }
                        } else {
                            int i= arrayHasE(ja, ox);
                            if (i == -1) {
                                ja.put(ox);
                            } else if (i == -2) {
                            }
                        }
                        prepare = ja;
                        

                    } else {
                        prepare = addMergeUnknown(v, ox);
                    }

                    if (prepare != null) {
                        try {
                            allJSONlist.put(k, prepare);
                        } catch (JSONException e) {}

                    }

                }
            } else {

                Object sv=t.map.get("v");

                if (sv != null) {
                    if (sv instanceof JSONObject) {
                        try {
                            allJSONlist.put(k, new MyJSONObjectTool((JSONObject)sv).toObjAll());
                        } catch (JSONException e) {}
                    } else if (sv instanceof JSONArray) {
                        try {
                            allJSONlist.put(k, new MyJSONArrayTool((JSONArray)sv).toObjAll());
                        } catch (JSONException e) {}
                    } else {
                        try {
                            allJSONlist.put(k, new JSONArray().put(sv).toString());
                        } catch (JSONException e) {}
                    }
                }

                //LOG.print("放入",new String(k));
            }

        }


    }

    private static void addMergeJSON(JSONObject j, JSONObject o) {

        Iterator<String> it=o.keys();
        while (it.hasNext()) {
            String n= it.next();
            if (j.has(n)) {
                Object jx,ox = (ox = o.opt(n));
                if ((jx = j.opt(n)) instanceof JSONObject) {
                    if (ox instanceof JSONObject) {
                        addMergeJSON((JSONObject)jx, (JSONObject)ox);
                    } else if (ox instanceof JSONArray) {
                        addMergeJSON((JSONObject)jx, new MyJSONArrayTool((JSONArray)ox).toObjAll());
                    } else {
                        try {
                            j.put(n, addMergeUnknown(jx, ox));
                        } catch (JSONException e) {}
                    }
                } else if (jx instanceof JSONArray) {
                    JSONArray ja=(JSONArray)jx;
                    //这里也是代表多个结果
                    if (ox instanceof JSONObject) {
                        int i= arrayHasE(ja, ox);
                        if (i == -1) {
                            ja.put(ox);
                        } else if (i == -2) {

                        } else {
                            addMergeJSON(ja.optJSONObject(i), (JSONObject)ox);
                        }
                    } else if (ox instanceof JSONArray) {
                        //理论上这里的两个都是合并后的，所以加起来就行了
                        Set set=new LinkedHashSet();
                        for (Object obj : new JSONArrayTool((JSONArray)jx)) {
                            set.add(obj);
                        }

                        for (int i=0;i < ((JSONArray)ox).length();i++) {
                            Object obj=((JSONArray)ox).opt(i);
                            if (obj instanceof JSONObject) {
                                if (((JSONArray)jx).opt(0) instanceof JSONObject) {
                                    addMergeJSON(((JSONArray)jx).optJSONObject(0), (JSONObject)obj);
                                } else {
                                    try {
                                        ((JSONArray)jx).put(0, new JSONObject());
                                    } catch (JSONException e) {}
                                    addMergeJSON(((JSONArray)jx).optJSONObject(0), (JSONObject)obj);
                                }
                            } else {
                                set.add(obj);
                            }

                        }
                        try {
                            j.put(n, new JSONArray(set));
                        } catch (JSONException e) {}
                    } else {
                        int i= arrayHasE(ja, ox);
                        if (i == -1) {
                            ja.put(ox);
                        } else if (i == -2) {
                        }
                    }


                } else {
                    try {
                        j.put(n, addMergeUnknown(jx, ox));
                    } catch (JSONException e) {}
                }
            } else {

                try {

                    Object obj=o.opt(n);
                    if (obj instanceof JSONObject) {
                        j.put(n, new MyJSONObjectTool((JSONObject)obj).toObjAll());
                    } else if (obj instanceof JSONArray) {
                        j.put(n, new MyJSONArrayTool((JSONArray)obj).toObjAll());
                    } else {
                        j.put(n, obj);
                    }
                } catch (JSONException e) {}
            }
        }
    }

    private static int arrayHasE(JSONArray jx, Object ox) {
        for (int i=0;i < jx.length();i++) {
            if (jx.opt(i).equals(ox)) {
                return -2;
            }
            if (jx.opt(i) instanceof JSONObject) {
                return i;
            }
        }
        return -1;
    }

    private static JSONArray addMergeUnknown(Object jx, Object ox) {
        //两者作为集合增加
        Set s=new HashSet<Object>();
        s.add(jx);
        s.add(ox);
        return new JSONArray(s);
    }





    private static void processJsonPiece(JSONObject json, ArrayList<Tips> tips, String[] layer, int i, String t) {

        i += 1;
        if (i + 1 > layer.length) {
            Iterator<String> it=json.keys();
            while (it.hasNext()) {
                String k=it.next();
                if (t.equals("k")) {
                    Tips str=new Tips(k);
                    if (!tips.contains(str)) {
                        tips.add(str);
                    }
                } else {
                    ProLoading.Tips tip=new Tips();

                    if (t.endsWith("v")) {

                        tip.map.put("v", json.opt(k));


                    }
                    if (t.startsWith("k")) {

                        tip.map.put("k", k + "");

                    }
                    tips.add(tip);
                }
            }
            return;
        }
        if (!layer[i].equals("*")) {
            callProcess(layer[i], json, layer, i, tips, t);
        } else {
            Iterator<String> it=json.keys();
            while (it.hasNext()) {

                callProcess(it.next(), json, layer, i, tips, t);
            }
        }


    }

    private static boolean callProcess(String key, JSONObject json, String[] layer, int i, ArrayList<Tips> tips, String t) {
        if (json.has(key)) {
            try {
                json = json.getJSONObject(key);
                processJsonPiece(json, tips, layer, i, t);
                return true;
            } catch (JSONException e) {
                try {
                    JSONArray j = json.getJSONArray(key);
                    processJsonPiece(j, tips, layer, i, t);
                } catch (JSONException u) {
                    return false;
                }

                return true;
            }

        } else {
            return false;
        }

    }
    private static void processJsonPiece(JSONArray json, ArrayList<Tips> tips, String[] layer, int i, String t) {

        i += 1;
        if (i + 1 > layer.length) {
            int max=json.length(),k=0;
            while (k < max) {
                if (t.equals("k")) {
                    Tips str=new Tips(k + "");
                    if (!tips.contains(str)) {
                        tips.add(str);
                    }
                } else {
                    ProLoading.Tips tip=new Tips();
                    if (t.endsWith("v")) {

                        tip.map.put("v", json.opt(k));


                    }
                    if (t.startsWith("k")) {

                        tip.map.put("k", k + "");

                    }

                    tips.add(tip);
                }
                k++;

            }
            return;
        }
        if (!layer[i].equals("*")) {
            callProcess(Integer.parseInt(layer[i]), json, layer, i, tips, t);
        } else {
            int it=0;
            while (it < json.length()) {

                callProcess(it, json, layer, i, tips, t);
                it++;
            }
        }


    }

    private static boolean callProcess(int key, JSONArray json, String[] layer, int i, ArrayList<Tips> tips, String t) {
        if (key >= 0 && key < json.length()) {
            try {
                JSONObject j = json.getJSONObject(key);
                processJsonPiece(j, tips, layer, i, t);
                return true;
            } catch (JSONException e) {
                try {
                    JSONArray j = json.getJSONArray(key);
                    processJsonPiece(j, tips, layer, i, t);
                    return true;
                } catch (JSONException u) {
                    return false;
                }


            }

        } else {
            return false;
        }

    }
    public static class Tips {
        public HashMap map=new HashMap<String,Object>();
        public Tips(String k, String v) {
            map.put("k", k);
            map.put("v", v);
        }
        public Tips(String k, JSONObject v) {
            map.put("k", k);
            map.put("v", v);

        }
        public Tips(String k, JSONArray v) {

            map.put("k", k);
            map.put("v", v);


        }
        public Tips(String k) {

            map.put("k", k);



        }
        public Tips() {

        }

        @Override
        public String toString() {

            try {
                JSONObject j=new JSONObject().put("k", map.get("k").toString());
                if (map.containsKey("v"))j.put("v", map.get("v").toString());
                return j.toString();
            } catch (JSONException e) {}
            return null;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null)return false;
            if (obj == this)return true;

            if (obj instanceof Tips) {
                ProLoading.Tips o=(Tips)obj;
                if (o.map.get("k").equals(map.get("k"))) {
                    if (map.containsKey("v") != o.map.containsKey("v")) {
                        return false;
                    } else {
                        if (map.containsKey("v")) {
                            return o.map.get("v").equals(map.get("v"));
                        } else {
                            return true;
                        }
                    }
                } else {
                    return false;
                }
            } else {
                return false;
            }
        }


    }
    public static void load() {
        CFile file=new CFile(Config.baseDir + "proCompletion");



        if (Global.isInProject) {
            if (ldb == null) {
                ldb = new Ldb(file.file);
            }
            if (!ldb.isOpen())ldb.open();
            try {
                behData = new JSONObject(new CFile(Config.baseDir + "beh.json").read());
            } catch (JSONException e) {} catch (IOException e) {}

            try {
                resData = new JSONObject(new CFile(Config.baseDir + "res.json").read());
            } catch (JSONException|IOException e) {}
            try {
                plugData = new JSONObject(new CFile(Config.baseDir + "plug.json").read());
            } catch (JSONException|IOException e) {}
        }
    }
    public static ArrayList<JSONObject> getTips(int type, String file, String jpath, String input, int index) {

        try {

            JSONObject d = null;
            if (jpath == null)return null;
            ArrayList<JSONObject> arr=new ArrayList<>();
            switch (type) {
                case MainActivity.BEHAVIORS:{
                        d = behData;
                        break;
                    }
                case MainActivity.RESOURCES:{
                        d = resData;
                        break;
                    }
                case MainActivity.PVZPLUG:{
                        d = plugData;
                        break;
                    }

            }
            JSONObject files=d.optJSONObject("File");
            Iterator<String> it=files.keys();
            while (it.hasNext()) {
                String k=it.next();
                if (file.startsWith(k)) {
                    JSONObject jsonPathData=files.optJSONObject(k);
                    Iterator<String> i=jsonPathData.keys();

                    while (i.hasNext()) {
                        String key=i.next();
                        //匹配json路径
                        if (jpath.matches(key)) {
                            if (key.endsWith(".*")) {
                                String process=key.substring(0, key.length() - 2);
                                String oringin=process.replace("[^/]*", "*");
                                if (!jpath.matches(process)) {
                                    if (index >= 2) {
                                        return arr;
                                    }
                                    String s=jpath.replaceAll(process, "");

                                    String n=new String(ldb.get(k + "//" + oringin + s.substring(0, s.indexOf("/"))));

                                    Object use = null;
                                    try {
                                        use = new JSONObject(n);
                                    } catch (JSONException e) {}
                                    s = s.replace(s.substring(0, s.indexOf("/") + 1), "");
                                    String[] everySurface=s.split("/");

                                    for (String surface : everySurface) {
                                        if (use instanceof JSONObject) {
                                            if (surface == null || "".equals(surface)) {
                                                break;
                                            } else {
                                                if (surface.matches(("[0-9]+"))) {
                                                    surface = 0 + "";
                                                }
                                                use = ((JSONObject)use).opt(surface);
                                            }
                                        } else if (use instanceof JSONArray) {
                                            use = ((JSONArray)use).opt(0);
                                        } else {
                                            return arr;
                                        }
                                    }

                                    if (use == null) {
                                        return arr;
                                    }
                                    ArrayList<JSONObject> res=new ArrayList<>();
                                    if (use instanceof JSONObject) {
                                        Iterator<String> nkeys=((JSONObject)use).keys();
                                        while (nkeys.hasNext()) {
                                            try {
                                                arr.add(new JSONObject().put("k", nkeys.next()));
                                            } catch (JSONException e) {}
                                        }
                                    } else if (use instanceof JSONArray) {
                                        for (Object o:new JSONArrayTool((JSONArray)use)) {
                                            try {
                                                arr.add(new JSONObject().put("k", o.toString()));
                                            } catch (JSONException e) {}
                                        }
                                    } else {
                                        try {
                                            arr.add(new JSONObject().put("k", use.toString()));
                                        } catch (JSONException e) {}
                                    }
                                    for (JSONObject f: arr) {
                                        if (f.optString("k").startsWith(input))res.add(f);
                                    }
                                    return res;
                                }
                            }
                            JSONObject use=jsonPathData.optJSONObject(key);
                            try {
                                byte[] data=ldb.get((use.optJSONArray("keys").getLong(index) + "").getBytes());
                                if (data == null)continue;


                                JSONArray a=new JSONArray(new String(data));
                                for (int g=0;g < a.length();g++) {

                                    JSONObject j= (JSONObject)a.opt(g);
                                    //LOG.print("",j.toString());
                                    if (j.has("k")) {
                                        if (j.optString("k").startsWith(input) || input.equals(""))arr.add(j);
                                    }

                                }
                                return arr;
                            } catch (JSONException e) {
                                return null;
                            }

                        }

                    }

                }
            }

            return null;
        } catch (Exception e) {
            return null;
        }
    }
    public static void close() {
        ldb.close();
    }


    public static class MyJSONArrayTool extends JSONArrayTool {
        @Override
        public JSONObject toObjAll() {
            JSONObject j=new JSONObject();
            Object res=new JSONObject();
            try {
                j.put("0", res);
            } catch (JSONException e) {}
            for (Object o:this) {

                if (res instanceof JSONObject) {
                    if (o instanceof JSONObject) {

                        addMergeJSON((JSONObject)res, new MyJSONObjectTool((JSONObject)o).toObjAll());

                    } else if (o instanceof JSONArray) {

                        addMergeJSON((JSONObject)res, new MyJSONArrayTool((JSONArray)o).toObjAll());

                    } else {
                        if (((JSONObject)res).length() == 0) {
                            res = new JSONArray().put(o);
                        } else {
                            res = new JSONArray().put(res).put(o);
                        }

                        try {
                            j.put("0", res);
                        } catch (Exception e) {}
                    }
                } else if (res instanceof JSONArray) {
                    JSONObject a=((JSONArray)res).optJSONObject(0);
                    if (o instanceof JSONObject) {


                        int i= arrayHasE((JSONArray)res, o);
                        if (i == -1) {
                            try {
                                ((JSONArray)res).put(0, o);
                            } catch (JSONException e) {}
                        } else if (i == -2) {

                        } else {
                            addMergeJSON(a, new MyJSONObjectTool((JSONObject)o).toObjAll());

                        }
                    } else if (o instanceof JSONArray) {
                        int i= arrayHasE((JSONArray)res, o);
                        if (i == -1) {
                            try {
                                //((JSONArray)res).put(0, o);
                            } catch (Exception e) {}
                        } else if (i == -2) {

                        } else {
                            addMergeJSON(a, new MyJSONArrayTool((JSONArray)o).toObjAll());

                        }

                    } else {
                        int i= arrayHasE((JSONArray)res, o);
                        if (i == -1) {
                            ((JSONArray)res).put(o);
                        } else if (i == -2) {
                        }

                    }

                }



            }
            System.out.println(j);
            return j;
        }
        public MyJSONArrayTool(JSONArray j) {
            super(j);
        }


    }
    public static class MyJSONObjectTool extends JSONObjectTool {

        public MyJSONObjectTool(JSONObject j) {
            super(j);
        }

        @Override
        public JSONObject toObjAll() {
            JSONObject u=new JSONObject();
            Iterator<String> it=json.keys();
            while (it.hasNext()) {
                String str=it.next();
                Object o=json.opt(str);

                if (o instanceof JSONObject) {
                    try {
                        u.put(str, new MyJSONObjectTool((JSONObject)o).toObjAll());
                    } catch (JSONException e) {}
                } else if (o instanceof JSONArray) {
                    try {
                        u.put(str, new MyJSONArrayTool((JSONArray)o).toObjAll());
                    } catch (JSONException e) {}
                } else {
                    try {
                        u.put(str, o);
                    } catch (JSONException e) {}
                }
            }

            return u;
        }
    }
}
