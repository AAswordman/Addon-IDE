package bms.helper.tools;
import java.util.ArrayList;

public class StringSplicing extends ArrayList<String>{
    private String use="";
    
    public StringSplicing(){
        
    }
    public void setInterval(String d){
        use=d;
    }

    @Override
    public String toString() {
        StringBuilder sb=new StringBuilder();
        boolean f=false;
        for(String g : this){
            if(f){
                sb.append(use+g);
            }else{
                f=true;
                sb.append(g);
            }
        }
        return sb.toString();
    }
    
}
