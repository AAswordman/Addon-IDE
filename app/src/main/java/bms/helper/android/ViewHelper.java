package bms.helper.android;

import android.view.View;
import android.view.ViewGroup;

public class ViewHelper {
    public static void setViewSize(View view, int i, int i2) {
        View view2 = view;
        ViewGroup.LayoutParams layoutParams = view2.getLayoutParams();
        layoutParams.width = i;
        layoutParams.height = i2;
        view2.setLayoutParams(layoutParams);
    }

    public ViewHelper() {
    }
}

