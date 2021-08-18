package bms.helper.android;
import android.util.Pair;
import android.view.View;
import android.support.v4.view.ViewCompat;

public class PairBuilder {
    public static Pair<View,String> get(View use){
        return new Pair<View,String>(use,ViewCompat.getTransitionName(use));
    }
}
