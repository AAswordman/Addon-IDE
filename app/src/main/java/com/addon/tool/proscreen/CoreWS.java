package com.addon.tool.proscreen;

import android.util.Log;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.UUID;
import bms.helper.app.EXPHelper;
import bms.helper.tools.LOG;

/**
 * Created by Roc on 2018/10/9.
 */
public class CoreWS extends WebSocketServer {

    private ServerManager _serverManager;
    private Function r;
    public CoreWS(ServerManager serverManager,int port,Function run) throws UnknownHostException {
        super(new InetSocketAddress(port));
        _serverManager=serverManager;
        r=run;
    }


    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        Log.i("TAG","Some one Connected...");
        _serverManager.UserLogin(UUID.randomUUID().toString(),conn);
        r.OnConnect(conn);
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        _serverManager.UserLeave(conn);
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        r.OnResult(message);
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        LOG.print("TAG","Socket Exception:"+EXPHelper.get(ex));
        _serverManager.Stop();
        
    }

    @Override
    public void onStart() {

    }
    public static interface Function{
        void OnResult(String message)
        void OnConnect(WebSocket conn)
    }
}

