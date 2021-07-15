package bms.helper.android;


import android.graphics.Outline;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.app.Activity;

public class TextureVideoViewOutlineProvider extends ViewOutlineProvider {
    @Override
    public void getOutline(View view, Outline outline) {
        /*
        int left = 0;
        int top = (view.getHeight() - view.getWidth()) / 2;
        int right = view.getWidth();
        int bottom = (view.getHeight() - view.getWidth()) / 2 + view.getWidth();
        outline.setOval(left, top, right, bottom);
        */
        outline.setOval(0,0,view.getWidth(),view.getHeight());
    }
	public static void round(Activity act,int id) {
		act.findViewById(id).setOutlineProvider(new TextureVideoViewOutlineProvider());
		act.findViewById(id).setClipToOutline(true);
	}
    public static void round(View v) {
        v.setOutlineProvider(new TextureVideoViewOutlineProvider());
        v.setClipToOutline(true);
	}
}

