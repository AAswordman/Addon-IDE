package bms.helper.script.json;

import java.lang.reflect.Field;
import org.json.JSONObject;
import org.json.JSONException;

public class JSONObjectBase {
    public JSONObject OriginalJson;

    @Override
    public String toString() {
        return toJSON().toString();
    }
    
    public JSONObject toJSON(){
        Class<?> aClass = this.getClass();
        JSONObject json=new JSONObject();
        // aClass.getFields() 获取 public 类型的成员
        Field[] declaredFields = aClass.getDeclaredFields(); //获取所有成员包含 private
        for (Field field:
        declaredFields) {
            try {
                field.setAccessible(true);
                Object obj=field.get(this);
                if(obj instanceof JSONObjectBase){
                    try {
                        json.put(field.getName(), ((JSONObjectBase)obj).toJSON());
                    } catch (JSONException e) {}
                } else if (obj instanceof JSONObjectTag) {

                }else{
                    try {
                        json.put(field.getName(), field.get(this));
                    } catch (JSONException e) {}
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return json;
    }
    
    
}
