package bms.helper.android;

import android.graphics.Point;
import android.graphics.Rect;
import android.widget.ImageView;

import java.util.Arrays;
import android.content.Context;
import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import bms.helper.android.v7.RecyclerAdapter;
import android.view.View;
import android.view.ViewManager;
import android.support.v7.widget.LinearLayoutManager;

public class ScrollHelperRecycler {
	public static Activity con;
    public static void init(Context c){
		con=(Activity) c;
	}
    public static boolean isInScreen(LinearLayoutManager list,int position){
		Point p=new Point();
		con.getWindowManager().getDefaultDisplay().getSize(p);
		int screenWidth=p.x;
		int screenHeight=p.y;
		Rect  rect=new Rect(0,0,screenWidth,screenHeight); 
		View imageView = list.findViewByPosition(position);
		int[] location = new int[2];
		imageView.getLocationInWindow(location);
		System.out.println(Arrays.toString(location));  
		// Rect ivRect=new Rect(imageView.getLeft(),imageView.getTop(),imageView.getRight(),imageView.getBottom());
		if (imageView.getLocalVisibleRect(rect)) {/*rect.contains(ivRect)*/ 
			//System.out.println("---------控件在屏幕可见区域-----显现-----------------");
			return true;
		} else {
			//imageView.setImageResource(R.drawable.p);
			//System.out.println("--------- 控件已不在屏幕可见区域（已滑出屏幕）-----隐去-----------------");
			return false;
		}
	}
    
}
