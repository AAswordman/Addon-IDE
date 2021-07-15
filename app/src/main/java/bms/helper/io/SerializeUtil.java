package bms.helper.io;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import bms.helper.tools.LOG;
import bms.helper.app.EXPHelper;

public class SerializeUtil {
    private static String file_name;
    public SerializeUtil(String name) {
        this.file_name = name;
    }
    //序列化方法
    public void writeObj(Serializable s) {
        ObjectOutputStream oos;
        try {
            oos = new ObjectOutputStream(new FileOutputStream(file_name));
            oos.writeObject(s);
            
            oos.close();
        } catch (IOException e) {
            LOG.print("错误",EXPHelper.get(e));
        }
    }
    public Object readObj() {
        Object obj = null;
        
        try {
            ObjectInputStream input = new ObjectInputStream(new FileInputStream(file_name));
            try {
                obj = input.readObject();
            } catch (ClassNotFoundException e) {
                
            } catch (IOException e) {
                
            }
            input.close();
        } catch (IOException e) {
            
        }
        
        return obj;
    }
}
