package com.addon.tool;
<<<<<<< HEAD
import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
=======
import android.graphics.Rect;
import android.os.Bundle;
>>>>>>> aa41e3931568d839484ad45420af53d27f2b4d68
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
<<<<<<< HEAD
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import bms.helper.android.HeightProvider;
import bms.helper.android.v7.RecyclerAdapter;
import bms.helper.app.EXPHelper;
import bms.helper.script.json.JSONArrayTool;
import bms.helper.script.json.JSONObjectTool;
import bms.helper.tools.LOG;
import bms.helper.tools.Time;
import chineseframe.CFile;
import chineseframe.屏幕工具;
import cn.refactor.lib.colordialog.ColorDialog;
import com.addon.json.Global;
import com.addon.tool.R;
import com.addon.tool.program.ProLoading;
import com.addon.tool.proscreen.ProjectionScreen;
=======
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import bms.helper.android.v7.RecyclerAdapter;
import bms.helper.script.json.JSONObjectTool;
import chineseframe.CFile;
import com.addon.json.Global;
import com.addon.tool.R;
import com.addon.tool.program.ProLoading;
>>>>>>> aa41e3931568d839484ad45420af53d27f2b4d68
import com.jsdroid.editor.CodePane;
import com.jsdroid.editor.CodeText;
import com.jsdroid.editor.GrammarCheck;
import com.jsdroid.editor.PreformEdit;
<<<<<<< HEAD
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.List;
import android.widget.ImageView;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

public class EditActivity extends AppCompatActivity {

    Handler handler=new Handler();  
    Runnable runnable=new Runnable() {  
        @Override  
        public void run() {  
            // TODO Auto-generated method stub  
            //要做的事情  
            ProjectionScreen.tick(EditActivity.this);
            handler.postDelayed(this, 300);  
        }  
    };  

    private PreformEdit[] preformEdit;

    private Toolbar toolbar;

    //天晓得为什么我没用ArrayList

    private CFile[] f;
    private long[] filesLastTime;
    private RecyclerAdapter adp;
    private CodePane[] codePane;
=======
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
>>>>>>> aa41e3931568d839484ad45420af53d27f2b4d68
    private ArrayList<JSONObject> tips=new ArrayList<>();
    private String file;



    private int mIndex=0;
    private String mInput;
<<<<<<< HEAD

    private ViewPager viewPager;

    private ArrayList<View> pageview;

    private String[] mItems;

    private int nowChoose;

    private View popupView;

    private PopupWindow popupWindow;

    private Spinner spinner;

    private PagerAdapter mPagerAdapter;
    protected List<File> files;

    private RecyclerAdapter sideAdp;
    public boolean back() {
        setPath(new File(getPath()).getParent());
        return true;
    }
    public void setPath(String p) {
        ((TextView)findViewById(R.id.side_path)).setText(p);
        reload(p);
    }
    private void reload(String p) {
        files = new ArrayList<>(Arrays.asList(new File(p).listFiles()));
        if (files == null)files = new ArrayList<>();
        Collections.sort(files, new Comparator<File>() {
                @Override
                public int compare(File o1, File o2) {
                    if (o1.isDirectory() && o2.isFile())
                        return -1;
                    if (o1.isFile() && o2.isDirectory())
                        return 1;
                    return o1.getName().compareTo(o2.getName());

                }

            });
        //判定存在
        files.add(0, new File("/"));
        sideAdp.notifyDataSetChanged();

    }

    public String getPath() {
        return ((TextView)findViewById(R.id.side_path)).getText().toString();
    }
    /**
     * 隐藏虚拟按键，并且全屏
     */
    protected void hideBottomUIMenu() {
        //隐藏虚拟按键，并且全屏
        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api
            View v = this.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else if (Build.VERSION.SDK_INT >= 19) {
            //for new api versions.
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }
    public void addPath(String p) {
        Global.openFilePaths.put(p);

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);



    }

=======
>>>>>>> aa41e3931568d839484ad45420af53d27f2b4d68
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.edit);
<<<<<<< HEAD
        hideBottomUIMenu();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //codePane = findViewById(R.id.code);
=======
        codePane = findViewById(R.id.code);
>>>>>>> aa41e3931568d839484ad45420af53d27f2b4d68

        toolbar = (Toolbar)findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

<<<<<<< HEAD
        CFile nf = new CFile(getIntent().getStringExtra("p"));

        toolbar.setTitle(nf.file.getName());

        String type=MainActivity.lastName(nf.file).replace(".", "");
=======
        f = new CFile(getIntent().getStringExtra("p"));

        toolbar.setTitle(f.file.getName());

        String type=MainActivity.lastName(f.file).replace(".", "");
>>>>>>> aa41e3931568d839484ad45420af53d27f2b4d68
        toolbar.setSubtitle(type);
        //toolbar.setSubtitleTextColor(0x999999);



<<<<<<< HEAD

        viewPager = (ViewPager) findViewById(R.id.code_pager);
        pageview = new ArrayList<View>();
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener(){
                @Override
                public void onPageScrolled(int p1, float p2, int p3) {
                }

                @Override
                public void onPageSelected(int p1) {
                    updateChoosePager(p1);
                }

                @Override
                public void onPageScrollStateChanged(int p1) {
                }
            });

        Global.addOpenFile(nf.file.getAbsolutePath());
        initChooseSpinner();

        mPagerAdapter = new PagerAdapter(){
            public int getItemPosition(Object o) {
                return POSITION_NONE;
            }
            @Override
            //获取当前窗体界面数
            public int getCount() {
                // TODO Auto-generated method stub
                return pageview.size();
            }

            @Override
            //判断是否由对象生成界面
            public boolean isViewFromObject(View arg0, Object arg1) {
                // TODO Auto-generated method stub
                return arg0 == arg1;
            }
            //使从ViewGroup中移出当前View
            public void destroyItem(View arg0, int arg1, Object arg2) {
                ((ViewPager) arg0).removeView((View)arg2);

            }

            //返回一个对象，这个对象表明了PagerAdapter适配器选择哪个对象放在当前的ViewPager中
            public Object instantiateItem(View arg0, int arg1) {
                ((ViewPager)arg0).addView(pageview.get(arg1));
                return pageview.get(arg1);
            }
        };
        //绑定适配器
        viewPager.setAdapter(mPagerAdapter);

        updateChoosePager(Global.addOpenFile(nf.file.getAbsolutePath()));
        //updateChoosePager(Global.openFilePaths.length() - 1);


        String p=nf.file.getAbsolutePath();

        if (Global.isInProject) {
            if (p.startsWith(Global.BehaviorPath + "")) {
                Global.isInWhat = MainActivity.BEHAVIORS;
                this.file = p.replace(Global.BehaviorPath, "");
            }
            if (p.startsWith(Global.ResourcePath + "")) {
                Global.isInWhat = MainActivity.RESOURCES;
                this.file = p.replace(Global.ResourcePath, "");
            }
            if (p.startsWith(Global.PvzModPath + "")) {
=======
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
>>>>>>> aa41e3931568d839484ad45420af53d27f2b4d68
                Global.isInWhat = MainActivity.PVZPLUG;
                this.file = p.replace(Global.PvzModPath, "");
            }

            //LOG.print(Global.isInWhat + "", file);
        }
<<<<<<< HEAD

        final DrawerLayout drawerLayout = findViewById(R.id.drawer);
        toolbar.setNavigationOnClickListener(new View.OnClickListener(){

                private boolean open;
                @Override
                public void onClick(View p1) {
                    if (!open) {
                        drawerLayout.openDrawer(Gravity.LEFT);
                    } else {
                        drawerLayout.closeDrawer(Gravity.LEFT);
                    }
                    this.open = open == false;
                }
            });


        /*因为设置了adjustNothing，这里不再生效
         getWindow().getDecorView().getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener(){
         //当键盘弹出隐藏的时候会 调用此方法。
         @Override
         public void onGlobalLayout() {
         Rect rect = new Rect();
         //获取当前界面可视部分
         EditActivity.this.getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
         //此处就是用来获取键盘的高度的， 在键盘没有弹出的时候 此高度为0 键盘弹出的时候为一个正数
         int heightDifference = EditActivity.this.getWindow().getDecorView().getRootView().getHeight() - rect.bottom;
         //动态监听键盘高度，更改最底部的view的高度将其他view挤上去
         LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) findViewById(R.id.seizeSeat). getLayoutParams();
         layoutParams.height = heightDifference;
         findViewById(R.id.seizeSeat).setLayoutParams(layoutParams);
         }
         });
         */
        new HeightProvider(this).init().setHeightListener(new HeightProvider.HeightListener() {
                @Override
                public void onHeightChanged(int height) {
                    LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) findViewById(R.id.seizeSeat). getLayoutParams();
                    layoutParams.height = height;
                    findViewById(R.id.seizeSeat).setLayoutParams(layoutParams);

                }
            });
        RecyclerView recycler_view2 =findViewById(R.id.choosefast);
        recycler_view2.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recycler_view2.setHasFixedSize(true);
        recycler_view2.setNestedScrollingEnabled(false);

        final String[] fast=new String[]{
            "{","}","(",")",":","[","]",",","\"",".","&","|","!","<",">","=","_"
        };

        recycler_view2.setAdapter(new RecyclerAdapter(){
                @Override
                public RecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
                    return new ViewHolder(CreateView(viewGroup, R.layout.choose_text));
                }

                @Override
                public void onBindViewHolder(RecyclerAdapter.ViewHolder viewHolder, final int position) {
                    View v=viewHolder.v;
                    ((TextView)v.findViewById(R.id.choose_textTextView)).setText(fast[position]);
                    v.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                CodeText t=codePane[nowChoose].getCodeText();
                                t.getText().insert(t.getSelectionStart(), fast[position]);
                            }
                        });
                }

                @Override
                public int getItemCount() {
                    return fast.length;
                }
            });
        //侧边栏配置
        sideAdp = new RecyclerAdapter(){
            @Override
            public RecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
                return new ViewHolder(CreateView(viewGroup, R.layout.sidebar_one));
            }

            @Override
            public void onBindViewHolder(RecyclerAdapter.ViewHolder viewHolder, final int position) {
                View v=viewHolder.v;
                if (position == 0) {
                    ((TextView)v.findViewById(R.id.sidebaroneTextView1)).setText("...");
                    ((ImageView)v.findViewById(R.id.sidebaroneImageView1)).setImageResource(R.drawable.ic_arrow_top_left_bold_outline);
                } else {
                    ((TextView)v.findViewById(R.id.sidebaroneTextView1)).setText(files.get(position).getName());
                    ImageView img=((ImageView)v.findViewById(R.id.sidebaroneImageView1));

                    String last=MainActivity.lastName(files.get(position));
                    boolean pd=false;

                    if (!pd) {
                        if (files.get(position).isDirectory()) {
                            img.setImageResource(R.drawable.ic_folder);
                        } else {
                            img.setImageResource(R.drawable.ic_file_code);
                        }
                    }
                }
                v.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (position == 0) {
                                if (!back()) {
                                    Toast.makeText(getApplication(), "返回失败", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                if (files.get(position).isDirectory()) {
                                    setPath(getPath() + "/" + files.get(position).getName());
                                } else {

                                    final String name=files.get(position).getName();
                                    if (Global.openFileWillExceed(getPath() + "/" + name)) {
                                        if (preformEdit[0].alreadyWrite) {
                                            ColorDialog dialog = new ColorDialog(EditActivity.this);
                                            dialog.setTitle("关闭文件");
                                            dialog.setContentText("ide将会关闭" + f[0].file.getName() + "，检测到你编辑过该文件，是否保存？");

                                            dialog.setColor(0xffffffff);
                                            dialog.setTitleTextColor(0xff222222);
                                            dialog.setContentTextColor(0xff222222);
                                            dialog.setPositiveListener("保存", new ColorDialog.OnPositiveListener() {
                                                    @Override
                                                    public void onClick(ColorDialog dialog) {
                                                        save(0);
                                                        deleteChoosePager(0);
                                                        addChoosePager(getPath() + "/" + name);
                                                        dialog.dismiss();
                                                    }
                                                })
                                                .setNegativeListener("不保存", new ColorDialog.OnNegativeListener() {
                                                    @Override
                                                    public void onClick(ColorDialog dialog) {
                                                        save(0);
                                                        deleteChoosePager(0);
                                                        addChoosePager(getPath() + "/" + name);
                                                        dialog.dismiss();
                                                    }
                                                }).show();

                                        } else {
                                            deleteChoosePager(0);
                                            addChoosePager(getPath() + "/" + name);
                                        }
                                    } else {

                                        addChoosePager(getPath() + "/" + name);
                                    }
                                }
                            }
                        }
                    });
                Animation a=new AlphaAnimation(0, 1);//透明度从250渐变至0
                a.setFillAfter(true);//将控件设置为动画的最后状态，也就是变为透明
                a.setDuration(80);
                v.startAnimation(a);
            }

            @Override
            public int getItemCount() {

                return files.size();
            }

            @Override
            public long getItemId(int pos) {
                return files.get(pos).hashCode();
            }

        };

        setPath(nf.file.getParent());

        RecyclerView sideList=findViewById(R.id.side_list);
        sideAdp.setHasStableIds(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(EditActivity.this);
        sideList.setLayoutManager(mLayoutManager);
        sideList.setHasFixedSize(true);
        sideList.setAdapter(sideAdp);

        sideList.setNestedScrollingEnabled(false);

    }

    //加载所有配置
    public void initChooseSpinner() {
        final LayoutInflater inflater = getLayoutInflater();
        popupView = inflater.inflate(R.layout.popup_entry_layout, null);// 创建PopupWindow 对象
        popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT); // 第二、第三个参数用来设置弹窗的大小,也可以用WRAP_CONTENT
=======
        try {
            preformEdit.setDefaultText(f.read());
        } catch (IOException e) {}
        codePane.getCodeText().setSelection(0);

        LayoutInflater inflater = getLayoutInflater();
        final View popupView = inflater.inflate(R.layout.popup_entry_layout, null);// 创建PopupWindow 对象

        final PopupWindow popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT); // 第二、第三个参数用来设置弹窗的大小,也可以用WRAP_CONTENT

>>>>>>> aa41e3931568d839484ad45420af53d27f2b4d68
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
<<<<<<< HEAD
                                CodeText t=codePane[nowChoose].getCodeText();
=======
                                CodeText t=codePane.getCodeText();
>>>>>>> aa41e3931568d839484ad45420af53d27f2b4d68
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

<<<<<<< HEAD
                                ArrayList<JSONObject> a=ProLoading.getTips(Global.isInWhat, file, codePane[nowChoose].getCodeText().jsonPath, mInput, mIndex);
=======
                                ArrayList<JSONObject> a=ProLoading.getTips(Global.isInWhat, file, codePane.getCodeText().jsonPath, mInput, mIndex);
>>>>>>> aa41e3931568d839484ad45420af53d27f2b4d68
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

<<<<<<< HEAD
        filesLastTime = new long[Global.openFilePaths.length()];
        f = new CFile[Global.openFilePaths.length()];
        codePane = new CodePane[Global.openFilePaths.length()];
        mItems = new String[Global.openFilePaths.length()];
        preformEdit = new PreformEdit[Global.openFilePaths.length()];
        int index=0;
        for (Object o:new JSONArrayTool(Global.openFilePaths)) {

            CFile mf=new CFile((String)o);

            CodePane view0 = updateOneChooseSpinner(index, mf);

            pageview.add(view0);

            index++;
        }



        // 初始化控件
        reloadSpinner();
    }

    //重加载上方的下拉菜单
    private void reloadSpinner() {
        spinner = (Spinner) findViewById(R.id.editSpinner);
        // 建立数据源
        ChangeSpinner(this, spinner, mItems);
        spinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, 
                                           int pos, long id) {

                    String[] languages = mItems;
                    //Toast.makeText(EditActivity.this, "你点击的是:" + languages[pos], 2000).show();
                    updateChoosePager(pos);

                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    // Another interface callback
                }
            });
    }

    //刷新单个文件的全部内容，包括重新定义
    private CodePane updateOneChooseSpinner(final int index, final CFile mf) {
        final CodePane view0 = new CodePane(this);
        if (!mf.file.exists())return view0;
        filesLastTime[index] = mf.file.lastModified();
        codePane[index] = view0;
        f[index] = mf;
        mItems[index] = mf.file.getName();
        preformEdit[index] = new PreformEdit(view0.getCodeText());
        new Thread(new Runnable(){
                @Override
                public void run() {
                    preformEdit[index].setDefaultTextBuilderStart();
                    BufferedReader bufferedReader = null;
                    final StringBuilder sb=new StringBuilder();
                    try {
                        if (mf.file.exists()) {
                            bufferedReader = new BufferedReader(new FileReader(mf.file.getAbsolutePath()));
                            String s;
                            boolean first=false;
                            while ((s = bufferedReader.readLine()) != null) {
                                if (first) {
                                    sb.append("\n");
                                    sb.append(s);
                                } else {
                                    first = true;
                                    sb.append(s);
                                }
                            }
                            runOnUiThread(new Runnable(){

                                    @Override
                                    public void run() {
                                        preformEdit[index].setDefaultTextBuilder(sb);
                                        view0.getCodeText().setSelection(0);
                                        preformEdit[index].setDefaultTextBuilderEnd();
                                    }
                                });

                        } else {
                            runOnUiThread(new Runnable(){
                                    @Override
                                    public void run() {
                                        Toast.makeText(getApplication(), mf.file.getAbsolutePath() + "未找到", Toast.LENGTH_SHORT).show();
                                    }
                                });
                        }

                    } catch (IOException e) {
                        LOG.print(EXPHelper.get(e));
                    } finally {
                        if (bufferedReader != null) {
                            try {
                                bufferedReader.close();
                            } catch (IOException e) {}
                        }
                    }

                }
            }).start();

        String type=MainActivity.lastName(mf.file).replace(".", "");
        switch (type) {
            case "json":{
                    codePane[index].getCodeText().setCheck(new GrammarCheck.JSON());
                    codePane[index].getCodeText().use = new CodeText.CompletionMsg(){
=======

        switch (type) {
                case "json":{
                    codePane.getCodeText().setCheck(new GrammarCheck.JSON());
                    codePane.getCodeText().use = new CodeText.CompletionMsg(){
>>>>>>> aa41e3931568d839484ad45420af53d27f2b4d68

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
<<<<<<< HEAD
                                Rect r;
                                try {
                                    r = view0.getCodeText().getCursorRect();
                                } catch (Exception e) {
                                    return;
                                }
                                int[] l=new int[]{r.left,r.bottom};
                                l[0] -= view0.getScrollX();
                                l[1] = l[1] - view0.getScrollY() + viewPager.getTop() + (r.bottom - r.top) * 2;
=======
                                Rect r=codePane.getCodeText().getCursorRect();
                                int[] l=new int[]{r.left,r.bottom};
                                l[0] -= codePane.getScrollX();
                                l[1] = l[1] - codePane.getScrollY() + codePane.getTop() + (r.bottom - r.top) * 2;
>>>>>>> aa41e3931568d839484ad45420af53d27f2b4d68
                                //codePane.getCodeText().getLocationInWindow(l);
                                if (!first) {
                                    popupWindow.update(l[0], l[1], ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                } else {
                                    popupWindow.showAtLocation(popupView, Gravity.NO_GRAVITY, l[0], l[1]);
                                    first = false;
                                }
<<<<<<< HEAD
                                int i=0;
                                while (i <= 0) {
                                    ArrayList<JSONObject> let = ProLoading.getTips(Global.isInWhat, file, view0.getCodeText().jsonPath, input, 0);

                                    i++;
=======
                                int index=0;
                                while (index <= 0) {
                                    ArrayList<JSONObject> let = ProLoading.getTips(Global.isInWhat, file, codePane.getCodeText().jsonPath, input, 0);

                                    index++;
>>>>>>> aa41e3931568d839484ad45420af53d27f2b4d68
                                    tips = let;
                                }

                                if (tips == null) {
<<<<<<< HEAD
                                    popupWindow.dismiss();
                                    first = true;
                                    return;
=======
                                    tips = new ArrayList<>();
>>>>>>> aa41e3931568d839484ad45420af53d27f2b4d68
                                } else if (tips.size() == 0) {
                                    try {
                                        tips.add(new JSONObject().put("k", "下滑加载"));
                                    } catch (JSONException e) {}
                                }
                                adp.notifyDataSetChanged();

                                mInput = input;
<<<<<<< HEAD
                                mIndex = i;
=======
                                mIndex = index;
>>>>>>> aa41e3931568d839484ad45420af53d27f2b4d68
                            }
                        }


                    };
                    break;
                }
        }
<<<<<<< HEAD
        return view0;
    }

    //选择某一个文件
    public void updateChoosePager(int num) {
        viewPager.setCurrentItem(num);
        spinner.setSelection(num);
        this.nowChoose = num;
    }
    //增加一页
    public void addChoosePager(String path) {

        if (Global.addOpenFile(path) == -1) {

            //全部拷贝一份后update
            CFile mf=new CFile(path);
            CFile[] f2 = new CFile[Global.openFilePaths.length()];
            CodePane[] codePane2 = new CodePane[Global.openFilePaths.length()];
            String[] mItems2 = new String[Global.openFilePaths.length()];
            PreformEdit[] preformEdit2 = new PreformEdit[Global.openFilePaths.length()];
            long[] filesLastTime2=new long[Global.openFilePaths.length()];

            System.arraycopy(f, 0, f2, 0, Global.openFilePaths.length() - 1);
            System.arraycopy(codePane, 0, codePane2, 0, Global.openFilePaths.length() - 1);
            System.arraycopy(mItems, 0, mItems2, 0, Global.openFilePaths.length() - 1);
            System.arraycopy(preformEdit, 0, preformEdit2, 0, Global.openFilePaths.length() - 1);
            System.arraycopy(filesLastTime, 0, filesLastTime2, 0, Global.openFilePaths.length() - 1);


            f = f2;
            filesLastTime = filesLastTime2;
            mItems = mItems2;
            codePane = codePane2;
            preformEdit = preformEdit2;

            pageview.add(updateOneChooseSpinner(Global.openFilePaths.length() - 1, mf));
            mPagerAdapter.notifyDataSetChanged();
            reloadSpinner();
            updateChoosePager(Global.openFilePaths.length() - 1);


        } else {
            updateChoosePager(Global.addOpenFile(path));
        }
    }

    //删除某页
    public void deleteChoosePager(int num) {
        //copy一份
        Global.openFilePaths.remove(num);
        if (Global.openFilePaths.length() == 0)finish();

        CFile[] f2 = new CFile[Global.openFilePaths.length()];
        CodePane[] codePane2 = new CodePane[Global.openFilePaths.length()];
        String[] mItems2 = new String[Global.openFilePaths.length()];
        PreformEdit[] preformEdit2 = new PreformEdit[Global.openFilePaths.length()];
        long[] filesLastTime2=new long[Global.openFilePaths.length()];
        int i=0;
        for (int index=0;index < f.length;index++) {
            if (index != num) {
                f2[i] = f[index];
                codePane2[i] = codePane[index];
                preformEdit2[i] = preformEdit[index];
                mItems2[i] = mItems[index];
                filesLastTime2[i] = filesLastTime[index];
                i++;
            }

        }
        pageview.remove(num);
        mPagerAdapter.notifyDataSetChanged();
        f = f2;
        filesLastTime = filesLastTime2;
        mItems = mItems2;
        codePane = codePane2;
        preformEdit = preformEdit2;

        reloadSpinner();
        updateChoosePager(0);

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        updateAllText();
        //Toast.makeText(getApplication(), "重新启动", Toast.LENGTH_SHORT).show();
    }

    /*
     @Override
     protected void onActivityResult(int requestCode, int resultCode, Intent data) {
     super.onActivityResult(requestCode, resultCode, data);

     Toast.makeText(getApplication(), "返回界面", Toast.LENGTH_SHORT).show();

     }
     */

    //如果后来被其他软件修改过，那么更新那里的文本
    private void updateAllText() {
        int index=0;
        for (CFile c:f) {
            if (!c.file.exists())continue;
            if (c.file.lastModified() > filesLastTime[index]) {
                try {
                    preformEdit[index].setDefaultText(c.read());
                } catch (IOException e) {}
                updateFileTime(index);
            }
            index++;
        }

    }

    //刷新单个文件的上次修改时间
    private void updateFileTime(int index) {
        filesLastTime[index] = new Time().getTime();
    }


    /**
     * Spinner自定义样式
     * 1、Spinner内的TextView样式：item_select
     * 2、Spinner下拉中每个item的TextView样式：item_drop
     * 3、Spinner下拉框样式，属性设置
     * */
    public static void ChangeSpinner(Context con, Spinner mSpinnerSimple, String[] spinnerItems) {
        //mSpinnerSimple.setDropDownWidth(400); //下拉宽度
        mSpinnerSimple.setDropDownHorizontalOffset(0); //下拉的横向偏移
        mSpinnerSimple.setDropDownVerticalOffset(屏幕工具.dp转像素(con, 55)); //下拉的纵向偏移
        //mSpinnerSimple.setBackgroundColor(AppUtil.getColor(instance,R.color.wx_bg_gray)); //下拉的背景色
        //spinner mode ： dropdown or dialog , just edit in layout xml
        //mSpinnerSimple.setPrompt("Spinner Title"); //弹出框标题，在dialog下有效
        //String[] spinnerItems = {"10","200","400"};
        //自定义选择填充后的字体样式
        //只能是textview样式，否则报错：ArrayAdapter requires the resource ID to be a TextView
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(con, R.layout.selected_item, spinnerItems);
        //自定义下拉的字体样式
        spinnerAdapter.setDropDownViewResource(R.layout.selected_item);
        //这个在不同的Theme下，显示的效果是不同的
        //spinnerAdapter.setDropDownViewTheme(Theme.LIGHT);
        mSpinnerSimple.setAdapter(spinnerAdapter);
    }

    @Override
    public void onBackPressed() {
        ColorDialog dialog = new ColorDialog(EditActivity.this);
        dialog.setTitle("即将退出编辑");
        dialog.setContentText("是否保存从上次保存到目前编辑的全部内容？");

        dialog.setColor(0xffffffff);
        dialog.setTitleTextColor(0xff222222);
        dialog.setContentTextColor(0xff222222);
        dialog.setPositiveListener("保存", new ColorDialog.OnPositiveListener() {
                @Override
                public void onClick(ColorDialog dialog) {
                    saveAll();
                    EditActivity.super.onBackPressed();
                    dialog.dismiss();
                }
            })
            .setNegativeListener("不保存", new ColorDialog.OnNegativeListener() {
                @Override
                public void onClick(ColorDialog dialog) {
                    EditActivity.super.onBackPressed();
                    dialog.dismiss();
                }
            }).show();

    }
    public void save(int i) {
        CFile c=f[i];
        try {
            c.write(codePane[i].getCodeText().getText().toString());
        } catch (IOException e) {
            runOnUiThread(new Runnable(){
                    @Override
                    public void run() {
                        Toast.makeText(getApplication(), "保存失败！", Toast.LENGTH_SHORT).show();
                    }
                });
            return;
        }
        updateFileTime(i);
        runOnUiThread(new Runnable(){
                @Override
                public void run() {
                    Toast.makeText(getApplication(), "保存成功！", Toast.LENGTH_SHORT).show();
                }
            });
        preformEdit[i].alreadyWrite = false;
    }
    public void saveAll() {
        new Thread(new Runnable(){
                @Override
                public void run() {
                    for (int i=0;i < f.length;i++) {
                        final CFile c=f[i];
                        try {
                            c.write(codePane[i].getCodeText().getText().toString());
                            updateFileTime(i);
                            preformEdit[i].alreadyWrite = false;
                        } catch (IOException e) {


                            runOnUiThread(new Runnable(){
                                    @Override
                                    public void run() {
                                        Toast.makeText(getApplication(), c.file.getName() + "保存失败！", Toast.LENGTH_SHORT).show();
                                    }
                                });

                            continue;
                        }

                    }
                    runOnUiThread(new Runnable(){
                            @Override
                            public void run() {
                                Toast.makeText(getApplication(), "保存完毕", Toast.LENGTH_SHORT).show();
                            }
                        });
                }
            }).start();
    }

=======


    }
>>>>>>> aa41e3931568d839484ad45420af53d27f2b4d68
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
<<<<<<< HEAD
                        case R.id.item1:{
                                if (preformEdit != null) {
                                    preformEdit[nowChoose].undo();
                                }
                                break;
                            }
                        case R.id.item2:{
                                if (preformEdit != null) {
                                    preformEdit[nowChoose].redo();
                                }
                                break;
                            }
                        case R.id.item3:{
                                //Toast.makeText(ToolbarActivity.this, "菜单项3", Toast.LENGTH_SHORT).show();
                                saveAll();
                                break;
                            }
                        case R.id.item5:{
                                //Toast.makeText(ToolbarActivity.this, "菜单项4", Toast.LENGTH_SHORT).show();
                                try {
                                    JSONObject json = new JSONObject(codePane[nowChoose].getCodeText().getText().toString());
                                    codePane[nowChoose].getCodeText().setText(new JSONObjectTool(json).toString(2));
                                } catch (JSONException e) {
                                    Toast.makeText(getApplication(), "请检查json是否正确", Toast.LENGTH_SHORT).show();
                                    GrammarCheck.ShowError err=codePane[nowChoose].getCodeText().getErrorShow();
                                    if (err != null) {
                                        Toast.makeText(getApplication(), "参考错误:第" + err.line + "第" + err.visualChar + "个字符", Toast.LENGTH_SHORT).show();
                                    }
=======
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
>>>>>>> aa41e3931568d839484ad45420af53d27f2b4d68
                                };


                            }
                            break;
<<<<<<< HEAD
                        case R.id.t_file_close:{
                                deleteChoosePager(nowChoose);
                                break;
                            }
                        case R.id.t_file_close_a:{
                                Global.openFilePaths = new JSONArray();
                                finish();
                                break;
                            }
                        case R.id.t_file_save_a:{
                                saveAll();
                                break;
                            }
                        case R.id.t_file_save:{
                                int i=nowChoose;
                                save(i);
                                break;
                            }
                        case R.id.t_tool_screen:{
                                ProjectionScreen.start();
                                handler.postDelayed(runnable, 1000);//每两秒执行一次runnable
                                break;
                            }
                        default:{
=======
                            default:{
>>>>>>> aa41e3931568d839484ad45420af53d27f2b4d68
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
