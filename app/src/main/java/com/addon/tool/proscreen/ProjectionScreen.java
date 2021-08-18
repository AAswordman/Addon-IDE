package com.addon.tool.proscreen;
import org.java_websocket.WebSocket;
import android.graphics.Bitmap;
import android.view.View;
import android.content.Context;
import android.app.Activity;
import bms.helper.encryption.Base64Helper;

public class ProjectionScreen {
    public static ServerManager serverManager=new ServerManager();
    public static void start() {
        serverManager.Start(1921, new CoreWS.Function(){
                @Override
                public void OnResult(String message) {
                }

                @Override
                public void OnConnect(WebSocket conn) {
                }
            });
    }
    public static void tick(final Activity c) {
        View view = c.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        final Bitmap b=view.getDrawingCache();
        new Thread(new Runnable(){
                @Override
                public void run() {
                    serverManager.SendMessageToAll("data:image/png;base64," + Base64Helper.getDiskBitmap2Base64(b));

                }
            }).start();
    }

}
