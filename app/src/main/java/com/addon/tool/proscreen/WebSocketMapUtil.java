package com.addon.tool.proscreen;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
 
public class WebSocketMapUtil {
 
public static ConcurrentMap<String, CoreWS> webSocketMap = new ConcurrentHashMap<>();
 
    public static void put(String key, CoreWS myWebSocketServer){
     webSocketMap.put(key, myWebSocketServer);
    }
 
    public static CoreWS get(String key){
         return webSocketMap.get(key);
    }
 
    public static void remove(String key){
         webSocketMap.remove(key);
    }
 
    public static Collection<CoreWS> getValues(){
        return webSocketMap.values();
    }
}

