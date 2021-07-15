package bms.helper.tools;
import java.util.ArrayList;

public class ArrayListSafe<T> extends ArrayList<T> {
    private ArrayList<T> addlist=new ArrayList<>();
    private ArrayList<T> relist=new ArrayList<>();
    private boolean lock;
    public ArrayListSafe() {
        super();
    }

    public void lock() {
        lock=true;
    }
    public void unlock() {
        lock=false;
        
        
        for(T i : relist){
            super.remove(i);
        }
        for(T i : addlist){
            super.add(i);
        }
        relist.clear();
        addlist.clear();
    }
    @Override
    public boolean remove(T o) {
        if (lock) {
            return relist.add(o);
        } else {
            return super.remove(o);
        }
    }

    @Override
    public boolean add(T e) {
        if (lock) {
            return addlist.add(e);
        } else {
            return super.add(e);
        }
    }

}
