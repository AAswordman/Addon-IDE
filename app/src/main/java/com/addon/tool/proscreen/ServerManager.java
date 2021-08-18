package com.addon.tool.proscreen;

import android.util.Log;
 
import org.java_websocket.WebSocket;
import org.java_websocket.WebSocketImpl;
 
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import bms.helper.tools.LOG;

 
/**
 * Created by Roc on 2018/10/9.
 */
public class ServerManager {
    private CoreWS serverSocket=null;
    private Map<WebSocket, String> userMap=new HashMap<WebSocket, String>();
 
    public ServerManager(){
 
    }
    public int getUserNum(){
        return userMap.keySet().size();
    }
    public void UserLogin(String userName,WebSocket socket){
        if (userName!=null||socket!=null) {
            userMap.put(socket, userName);
            LOG.print("TAG","LOGIN:"+userName);
            SendMessageToAll(userName+"...Login...");
        }
    }
 
    public void UserLeave(WebSocket socket){
        if (userMap.containsKey(socket)) {
            String userName=userMap.get(socket);
            LOG.print("TAG","Leave:"+userName);
            
            userMap.remove(socket);
            SendMessageToAll(userName+"...Leave...");
        }
    }
 
    public void SendMessageToUser(WebSocket socket,String message){
        if (socket!=null) {
            socket.send(message);
        }
    }
 
    public void SendMessageToUser(String userName,String message){
        Set<WebSocket> ketSet=userMap.keySet();
        for(WebSocket socket : ketSet){
            String name=userMap.get(socket);
            if (name!=null) {
                if (name.equals(userName)) {
                    socket.send(message);
                    break;
                }
            }
        }
    }
 
    public void SendMessageToAll(String message){
        Set<WebSocket> ketSet=userMap.keySet();
        for(WebSocket socket : ketSet){
            String name=userMap.get(socket);
            //LOG.print("发送数据"+name,message);
            if (name!=null) {
                socket.send(message);
            }
        }
    }
 
    public boolean Start(int port,CoreWS.Function fun){
 
        if (port<0) {
            LOG.print("TAG","Port error...");
            return false;
        }
 
        Log.i("TAG","Start ServerSocket...");
        //WebSocketImpl.DEBUG=false;
        try {
            serverSocket=new CoreWS(this,port,fun);
            serverSocket.start();
            LOG.print("TAG","Start ServerSocket Success...");
            return true;
        } catch (Exception e) {
            LOG.print("TAG","Start Failed...");
            e.printStackTrace();
            return false;
        }
    }
 
    public boolean Stop(){
        try {
            serverSocket.stop();
            LOG.print("TAG","Stop ServerSocket Success...");
            return true;
        } catch (Exception e) {
            LOG.print("TAG","Stop ServerSocket Failed...");
            e.printStackTrace();
            return false;
        }
    }
 
 
}

