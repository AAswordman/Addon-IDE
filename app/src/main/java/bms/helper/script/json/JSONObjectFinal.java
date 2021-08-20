package bms.helper.script.json;
import org.json.JSONObject;

public class JSONObjectFinal extends JSONObjectBase {

    @Override
    public JSONObject toJSON() {
        if(OriginalJson==null){
            OriginalJson=new JSONObject();
        }
        return OriginalJson;
    }
    
    public JSONObjectTool getJSON(){
        return new JSONObjectTool(toJSON());
    }
    
}
