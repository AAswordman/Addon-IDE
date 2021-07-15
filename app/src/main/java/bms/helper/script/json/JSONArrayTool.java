package bms.helper.script.json;
import java.lang.reflect.Field;
import java.util.ArrayList;
import org.json.JSONArray;
import java.util.List;
import java.util.Iterator;
import java.util.Collection;
import java.util.ListIterator;
import org.json.JSONException;
import org.json.JSONObject;

public class JSONArrayTool implements List<Object> {
public JSONObject toObjAll() {
        JSONObject j=new JSONObject();
        int k=0;
        for(Object o:this){
            if(o instanceof JSONObject){
                try {
                    j.put(k+"", new JSONObjectTool((JSONObject)o).toObjAll());
                } catch (JSONException e) {}
            }else if(o instanceof JSONArray){
                try {
                    j.put(k+"", new JSONArrayTool((JSONArray)o).toObjAll());
                } catch (JSONException e) {}
            }else{
                try {
                    j.put(k+"", o);
                } catch (JSONException e) {}
            }
            
            k++;
        }
        
        return j;
    }
    @Override
    public int size() {
        return arr.length();
    }

    @Override
    public boolean isEmpty() {
        return arr.length() == 0;
    }

    @Override
    public boolean contains(Object p1) {
        return false;
    }

    @Override
    public Iterator<Object> iterator() {
        return new IteratorArr(this);
    }

    @Override
    public Object[] toArray() {
        return null;
    }

    @Override
    public <T extends Object> T[] toArray(T[] p1) {
        return null;
    }

    @Override
    public boolean add(Object p1) {
        arr.put(p1);
        return true;
    }

    @Override
    public boolean remove(Object p1) {

        return false;
    }

    @Override
    public boolean containsAll(Collection<?> p1) {
        return false;
    }

    @Override
    public boolean addAll(Collection<?> p1) {
        return false;
    }

    @Override
    public boolean addAll(int p1, Collection<?> p2) {
        return false;
    }

    @Override
    public boolean removeAll(Collection<?> p1) {
        return false;
    }

    @Override
    public boolean retainAll(Collection<?> p1) {
        return false;
    }

    @Override
    public void clear() {
        arr = new JSONArray();
    }

    @Override
    public Object get(int p1) {
        return arr.opt(p1);
    }

    @Override
    public Object set(int p1, Object p2) {
        return null;
    }

    @Override
    public void add(int p1, Object p2) {
        try {
            arr.put(p1, p2);
        } catch (JSONException e) {}
    }

    @Override
    public Object remove(int p1) {
        return arr.remove(p1);
    }

    @Override
    public int indexOf(Object p1) {
        return 0;
    }

    @Override
    public int lastIndexOf(Object p1) {
        return 0;
    }

    @Override
    public ListIterator<Object> listIterator() {
        return null;
    }

    @Override
    public ListIterator<Object> listIterator(int p1) {
        return null;
    }

    @Override
    public List<Object> subList(int p1, int p2) {
        return null;
    }


    protected JSONArray arr;
    public JSONArrayTool() {
        arr = new JSONArray();
    }
    public JSONArrayTool(JSONArray h) {
        this.arr = h;
    }
    public ArrayList<Object> toArrayList() {
        try {
            final Field field = JSONArray.class.getDeclaredField("myArrayList");
            field.setAccessible(true);

            final ArrayList<Object> obj = (ArrayList<Object>) field.get(arr);
            return obj;
        } catch (IllegalArgumentException e) {} catch (IllegalAccessException e) {} catch (NoSuchFieldException e) {}
        return null;
    }
    public JSONObject toObj(){
        JSONObject j=new JSONObject();
        int k=0;
        for(Object o:this){
            try {
                j.put(k + "", o);
            } catch (JSONException e) {}
            k++;
        }
        return j;
    }
    public static class IteratorArr implements Iterator<Object> {
        private int num=0;
        private int max;
        private JSONArrayTool k;
        IteratorArr(JSONArrayTool k){
            this.k=k;
            this.max=k.size();
        }
        @Override
        public boolean hasNext() {
            return num<max;
        }

        @Override
        public Object next() {
            num++;
            return k.get(num-1);
        }

        @Override
        public void remove() {
        }
        
        
    }
}
