//
// Decompiled by FernFlower - 786ms
//
package bms.helper.app;


import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.LayoutParams;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import com.addon.tool.R;

public class BaseActivityUse extends AppCompatActivity {
    protected View actionBarview;
    //protected CrashHandler crashHandler;

    @Override
    protected void onCreate(Bundle var1) {
        super.onCreate(var1);
        //this.crashHandler = CrashHandler.getInstance();
        //this.crashHandler.init(this.getApplicationContext());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu var1) {
        boolean var2 = super.onCreateOptionsMenu(var1);
        this.getMenuInflater().inflate(R.menu.main, var1);
        return var2;
    }

    protected void setActionBarLayout(int var1) {
        ActionBar var2 = this.getSupportActionBar();
        if (var2 != null) {
            var2.setDisplayShowHomeEnabled(false);
            var2.setDisplayShowCustomEnabled(true);
            this.actionBarview = ((LayoutInflater)this.getSystemService("layout_inflater")).inflate(var1, (ViewGroup)null);
            LayoutParams var3 = new LayoutParams(-1, -1);
            var2.setCustomView(this.actionBarview, var3);
        }

    }
}


