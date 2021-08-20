package bms.helper.script.json;

public class JSONObjectTag<T>{
    private T t;
    public JSONObjectTag(T t){
        this.t=t;
    }
    public JSONObjectTag(){
    }
    public T get(){
        return t;
    }
    public void set(T t){
        this.t=t;
    }

    @Override
    public String toString() {
        return t.toString();
    }
    
}
