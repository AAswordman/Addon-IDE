package bms.quote;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Map;
import com.litl.leveldb.DB;
import com.litl.leveldb.Iterator;
import java.io.Closeable;

public class Ldb implements Closeable{
    public DB db;
    private static final Charset CHARSET = Charset.forName("UTF-8");

    private DB.Snapshot snapshot;

    private boolean open;
    public Ldb(File f){
        load(f);
        
        
    }
    public Ldb(){
        
    }
    public void load(File f){
        db=new DB(f);
        open();
        upDateSnapshot();
        
    }
    public Iterator getIterator(){
        return db.iterator();
    }
    public byte[] get(String key){
        return db.get(snapshot,key.getBytes(CHARSET));
    }
    public byte[] get(byte[] key){
        return db.get(snapshot,key);
    }
    public void put(byte[] key,byte[] v){
        db.put(key,v);
    }
    public void delete(byte[] key){
        db.delete(key);
        
    }
    public void upDateSnapshot(){
        snapshot = db.getSnapshot();
    }
    public Iterator keys(){
        return db.iterator(snapshot);
    }
    public boolean isOpen(){
        return open;
    }
    @Override
    public void close(){
        db.close();
        open=false;
    }
    private final static char[] hexArray = "0123456789ABCDEF".toCharArray();

    public static String byteArrToHex(byte... bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }
    public void open(){
        this.open=true;
        db.open();
    }

}

