package com.addon.config;
import bms.quote.Ldb;
import java.util.HashMap;
import java.util.concurrent.locks.ReentrantLock;
import java.io.File;
import bms.helper.tools.LOG;
import java.util.Arrays;

public class SpecialLdb extends Ldb {
    HashMap<byte[],Object> find=new HashMap<>();
    HashMap<byte[],Object> lock=new HashMap<>();
    Object value=new Object();

    private byte[] onlyKey;

    private int first=200;
    public SpecialLdb(File f) {
        super(f);
    }

    @Override
    public byte[] get(String s) {
        byte[] key=s.getBytes();
        return get(key);
    }
    
    @Override
    public byte[] get(byte[] key) {
        if (find.containsKey(key)) {
            Object obj=new Object();
            
            synchronized (obj) {
                if(Arrays.equals( key,onlyKey))LOG.print("","结束处理");
                lock.put(key,obj);
                try {
                    lock.get(key).wait();
                } catch (InterruptedException e) {
                }
            }
            if(Arrays.equals( key,onlyKey))LOG.print("","开始处理");
            return super.get(key);
        } else {
            find.put(key, value);
            if(Arrays.equals( key,onlyKey))LOG.print("","(第一个)开始处理");
            return super.get(key);
        }

    }

    @Override
    public void put(byte[] key, byte[] v) {
        super.put(key, v);
        first--;
        if(first==0){
            this.onlyKey=key;
            first=999999999;
        }
        if(Arrays.equals( key,onlyKey))LOG.print("","结束处理");
        if (lock.containsKey(key)) {
            find.remove(key);
            lock.get(key).notifyAll();
        } else {
            find.remove(key);
        }

    }
}
