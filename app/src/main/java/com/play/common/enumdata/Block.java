package com.play.common.enumdata;
import org.json.JSONObject;
import bms.helper.io.AssetsUtil;
import android.content.Context;
import bms.helper.script.json.JSONTools;
import android.graphics.Bitmap;
import java.util.Iterator;
import org.json.JSONException;
import java.util.HashMap;
import android.graphics.BitmapFactory;


public class Block {
    public int data;
    public int id;
    public String namespace;
    public Bitmap texture;

    private static Context c;
    public static JSONObject json=new JSONObject();
    public static JSONObject nametoid=new JSONObject();

	public static int Width=16,Height=16;
	
	final private static HashMap<String,Bitmap> cache=new HashMap<>();

    public static void init(Context c) {
        Block.c = c;
        json = JSONTools.parse(
            AssetsUtil.getFromAssets(c, "core/ldb/blocks/id.json"));
        Iterator it=json.keys();
        while (it.hasNext()) {
            String key=(String) it.next();
            try {
                nametoid.put(json.optJSONObject(key).optString("name"), key.split(":")[0]);
            } catch (JSONException e) {}
        }
    }
	public static int getBlockId(String name) {
		return nametoid.optInt(name);
	}
	public static void setBlock(String name, String texture) {
		int id=(int) Math.round(Math.random());
		setBlockId(name, id);
		try {
			json.put(id + ":0", new JSONObject().put("name", name).put("texture", texture));
		} catch (JSONException e) {}
	}
    public static void setBlockId(String name, int id) {
		try {
			nametoid.put(name, id);
		} catch (JSONException e) {}
	}
    public static Block getBlock(int id, int data) {
        JSONObject j= json.optJSONObject(id + ":" + data);
		if(j==null){
			
			j= json.optJSONObject("0:0");
		}
        return new Block(id, data, j.optString("name"), j.optString("texture"));
    }
    public static Block getBlock(String id, int data) {
        return getBlock(getBlockId(id), data);
    }
    public static Block getBlock(String id) {
        return getBlock(id, 0);
    }
    public static Block getBlock(int id) {
        return getBlock(id, 0);
    }
	
    public Block(int id, int data, String name, String b) {
        this.data = data;this.id = id;this.namespace = name;
		String key="core/ldb/blocks/" + b;
		Bitmap bmp;
		if (!cache.containsKey(key)) {
			if (name.indexOf("minecraft:")==-1) {
				bmp=BitmapFactory.decodeFile(b);
                
			} else {
				bmp=AssetsUtil.getImageFromAssetsFile(c, key);
                
			}
            
            this.texture = Bitmap.createBitmap(bmp, 0, 0, Width, Height);
            cache.put(key,this.texture);
            //createBitmap的注释写着:The new bitmap may be the same object as source, or a copy may have been made.
            //bmp.recycle();
		} else {
			this.texture = cache.get(key);
		}

    }
}
