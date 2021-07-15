package com.addon.tool;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import bms.helper.android.v7.RecyclerAdapter;
import bms.helper.script.json.JSONObjectTool;
import chineseframe.CFile;
import com.addon.json.Global;
import com.addon.tool.R;
import com.addon.tool.program.ProLoading;
import com.jsdroid.editor.CodePane;
import com.jsdroid.editor.CodeText;
import com.jsdroid.editor.GrammarCheck;
import com.jsdroid.editor.PreformEdit;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import org.json.JSONException;
import org.json.JSONObject;

public class EditActivity extends AppCompatActivity {

    private PreformEdit preformEdit;

    private Toolbar toolbar;

    private CFile f;
    private RecyclerAdapter adp;
    private CodePane codePane;
    private ArrayList<JSONObject> tips=new ArrayList<>();
    private String file;



    private int mIndex=0;
    private String mInput;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.edit);
        codePane = findViewById(R.id.code);

        toolbar = (Toolbar)findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

        f = new CFile(getIntent().getStringExtra("p"));

        toolbar.setTitle(f.file.getName());

        String type=MainActivity.lastName(f.file).replace(".", "");
        toolbar.setSubtitle(type);
        //toolbar.setSubtitleTextColor(0x999999);



        preformEdit = new PreformEdit(codePane.getCodeText());
        String p=f.file.getAbsolutePath();

        if (Global.isInProject) {
            if (p.startsWith(Global.BehaviorPath+"")) {
                Global.isInWhat = MainActivity.BEHAVIORS;
                this.file = p.replace(Global.BehaviorPath, "");
            }
            if (p.startsWith(Global.ResourcePath+"")) {
                Global.isInWhat = MainActivity.RESOURCES;
                this.file = p.replace(Global.ResourcePath, "");
            }
            if (p.startsWith(Global.PvzModPath+"")) {
                Global.isInWhat = MainActivity.PVZPLUG;
                this.file = p.replace(Global.PvzModPath, "");
            }

            //LOG.print(Global.isInWhat + "", file);
        }
        try {
            preformEdit.setDefaultText(f.read());
        } catch (IOException e) {}
        codePane.getCodeText().setSelection(0);

        LayoutInflater inflater = getLayoutInflater();
        final View popupView = inflater.inflate(R.layout.popup_entry_layout, null);// 创建PopupWindow 对象

        final PopupWindow popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT); // 第二、第三个参数用来设置弹窗的大小,也可以用WRAP_CONTENT

        final RecyclerView recycler_view = popupView.findViewById(R.id.list);
        adp = new RecyclerAdapter(){
            @Override
            public RecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
                return new ViewHolder(CreateView(viewGroup, R.layout.popup_entry_item));
            }

            @Override
            public void onBindViewHolder(RecyclerAdapter.ViewHolder viewHolder, int position) {
                View v=viewHolder.v;
                final String k=tips.get(position).optString("k");
                final String value;
                try {
                    value = new JSONObjectTool(new JSONObject(tips.get(position).optString("v", null))).toString(2);
                } catch (Exception e) {
                    value = tips.get(position).optString("v", null);
                }
                ((TextView)v.findViewById(R.id.msg)).setText(k);
                v.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (!"下滑加载".equals(k)) {
                                CodeText t=codePane.getCodeText();
                                int mouse=t.getSelectionStart();
                                if (value != null) {
                                    t.getText().insert(mouse, "\":" + value);
                                }
                                t.getText().insert(mouse, k.substring(mInput.length()));



                                popupWindow.dismiss();
                            }
                        }
                    });
            }
            @Override
            public long getItemId(int position) {
                return tips.get(position).hashCode();
            }

            @Override
            public int getItemCount() {
                return tips.size();
            }
        };
        recycler_view.setOnTouchListener(new View.OnTouchListener(){
                @Override
                public boolean onTouch(View view, MotionEvent event) {
                    int action = event.getAction();
                    if (action == MotionEvent.ACTION_DOWN) {
                        //点击
                    } else if (action == MotionEvent.ACTION_UP) {
                        //松开  
                    } else if (action == MotionEvent.ACTION_MOVE) {
                        //移动
                        if (isVisBottom(recycler_view)) {
                            //加载更多
                            for (int i=0;i <= 6;i++) {

                                ArrayList<JSONObject> a=ProLoading.getTips(Global.isInWhat, file, codePane.getCodeText().jsonPath, mInput, mIndex);
                                if (a != null)tips.addAll(a);
                                mIndex++;
                                adp.notifyDataSetChanged();
                            }
                        }
                    }
                    return false;
                }
            });
        adp.setHasStableIds(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(EditActivity.this);
        recycler_view.setLayoutManager(mLayoutManager);
        recycler_view.setHasFixedSize(true);
        recycler_view.setAdapter(adp);
        recycler_view.setNestedScrollingEnabled(false);


        switch (type) {
                case "json":{
                    codePane.getCodeText().setCheck(new GrammarCheck.JSON());
                    codePane.getCodeText().use = new CodeText.CompletionMsg(){

                        private boolean first=true;

                        @Override
                        public void close() {
                            popupWindow.dismiss();
                            first = true;
                        }

                        @Override
                        public void change(String input) {
                            if (Global.isInProject) {


                                // 设置位置
                                Rect r=codePane.getCodeText().getCursorRect();
                                int[] l=new int[]{r.left,r.bottom};
                                l[0] -= codePane.getScrollX();
                                l[1] = l[1] - codePane.getScrollY() + codePane.getTop() + (r.bottom - r.top) * 2;
                                //codePane.getCodeText().getLocationInWindow(l);
                                if (!first) {
                                    popupWindow.update(l[0], l[1], ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                } else {
                                    popupWindow.showAtLocation(popupView, Gravity.NO_GRAVITY, l[0], l[1]);
                                    first = false;
                                }
                                int index=0;
                                while (index <= 0) {
                                    ArrayList<JSONObject> let = ProLoading.getTips(Global.isInWhat, file, codePane.getCodeText().jsonPath, input, 0);

                                    index++;
                                    tips = let;
                                }

                                if (tips == null) {
                                    tips = new ArrayList<>();
                                } else if (tips.size() == 0) {
                                    try {
                                        tips.add(new JSONObject().put("k", "下滑加载"));
                                    } catch (JSONException e) {}
                                }
                                adp.notifyDataSetChanged();

                                mInput = input;
                                mIndex = index;
                            }
                        }


                    };
                    break;
                }
        }


    }
    public static boolean isVisBottom(RecyclerView recyclerView) {  
        LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();  
        //屏幕中最后一个可见子项的position
        int lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();  
        //当前屏幕所看到的子项个数
        int visibleItemCount = layoutManager.getChildCount();  
        //当前RecyclerView的所有子项个数
        int totalItemCount = layoutManager.getItemCount();  
        //RecyclerView的滑动状态
        int state = recyclerView.getScrollState();  
        if (visibleItemCount > 0 && lastVisibleItemPosition == totalItemCount - 1) {   
            return true; 
        } else {   
            return false;  
        }
	}
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // 关联toolbar和menu，只需这一句代码菜单就可以正常显示了
        toolbar.inflateMenu(R.menu.edit_menu);
        // 手动设置溢出菜单项的图标
        toolbar.setOverflowIcon(getResources().getDrawable(R.drawable.ic_more));
        // 设置菜单点击事件监听
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    switch (item.getItemId()) {
                            case R.id.item1:{
                                if (preformEdit != null) {
                                    preformEdit.undo();
                                }
                                break;
                            }
                            case R.id.item2:{
                                if (preformEdit != null) {
                                    preformEdit.redo();
                                }
                                break;
                            }
                            case R.id.item3:{
                                //Toast.makeText(ToolbarActivity.this, "菜单项3", Toast.LENGTH_SHORT).show();
                                new Thread(new Runnable(){
                                        @Override
                                        public void run() {
                                            try {
                                                f.write(codePane.getCodeText().getText().toString());
                                            } catch (IOException e) {
                                                runOnUiThread(new Runnable(){
                                                        @Override
                                                        public void run() {
                                                            Toast.makeText(getApplication(), "保存失败！", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });

                                                return;
                                            }
                                            runOnUiThread(new Runnable(){
                                                    @Override
                                                    public void run() {
                                                        Toast.makeText(getApplication(), "保存成功", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                        }
                                    }).start();

                                break;
                            }
                            case R.id.item4:{
                                //Toast.makeText(ToolbarActivity.this, "菜单项4", Toast.LENGTH_SHORT).show();
                                try {
                                    JSONObject json = new JSONObject(codePane.getCodeText().getText().toString());
                                    codePane.getCodeText().setText(new JSONObjectTool(json).toString(2));
                                } catch (JSONException e) {
                                    Toast.makeText(getApplication(), "请检查json是否正确", Toast.LENGTH_SHORT).show();
                                };


                            }
                            break;
                            default:{
                                break;
                            }
                    }
                    return false;
                }
            });

        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        if (menu != null) {
            if (menu.getClass().getSimpleName().equalsIgnoreCase("MenuBuilder")) {
                try {
                    Method method = menu.getClass().getDeclaredMethod("setOptionalIconsVisible", Boolean.TYPE);
                    method.setAccessible(true);
                    method.invoke(menu, true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return super.onMenuOpened(featureId, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }

}
