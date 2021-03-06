package com.addon.tool;

import android.animation.Animator;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import bms.helper.android.v7.RecyclerAdapter;
import bms.helper.app.CrashHandler;
import bms.helper.io.AssetsUtil;
import bms.helper.script.json.JSONTools;
import chineseframe.CFile;
import cn.refactor.lib.colordialog.ColorDialog;
import cn.refactor.lib.colordialog.PromptDialog;
import com.addon.config.Config;
import com.addon.json.Global;
import com.addon.tool.EditActivity;
import com.addon.tool.MainActivity;
import com.addon.tool.R;
import com.addon.tool.program.ProLoading;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.Manifest;
import android.support.annotation.NonNull;
import android.os.Handler;
import android.os.Message;
import android.view.WindowManager;

public class MainActivity extends AppCompatActivity 
implements View.OnClickListener {



    protected CrashHandler crashHandler;
    protected RecyclerView rv;
    protected RecyclerAdapter adp;
    protected List<File> files;
    protected int type;
    protected CFile globalSave;

    public static final int 
    RESOURCES    = 0,
    BEHAVIORS    = 1,
    PVZPLUG      = 2;

    protected HashMap<String,Integer> getImg=new HashMap<>();

    private boolean utwIsClose;

    private boolean alreadyLoad;

    private PromptDialog startDialogue;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            int what = msg.what;
            if (what == 0) {//update
                startDialogue.getContentTextView().setText("????????????" + ProLoading.getProgress() + "%,???????????????\n?????????: " + ProLoading.now + "/" + ProLoading.total);
                if (startDialogue.isShowing()) {
                    mHandler.sendEmptyMessageDelayed(0, 50);
                }
            }
        }
    };

    /**
     * ?????????????????????????????????
     */
    protected void hideBottomUIMenu() {
        //?????????????????????????????????
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkPermission();
        this.crashHandler = CrashHandler.getInstance();
        this.crashHandler.init(this.getApplicationContext());
        setContentView(R.layout.activity_main);

		Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		bms.helper.Global.dir = "addonHelper";
        Config.baseDir = "/sdcard/addonHelper/";
        globalSave = new CFile(Config.baseDir + "global.json");
		loadGlobal();

        hideBottomUIMenu();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        findViewById(R.id.tool).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final View iv2=findViewById(R.id.menu);
                    Animator animator = ViewAnimationUtils.createCircularReveal(iv2, -iv2.getWidth(), iv2.getHeight() / 2, 0, (float) Math.hypot(iv2.getWidth() * 2, iv2.getHeight()));
                    animator.setInterpolator(new AccelerateDecelerateInterpolator());
                    animator.setDuration(500);
                    //animator.setInterpolator(new AccelerateDecelerateInterpolator());
                    animator.start();

                    final View g=findViewById(R.id.gery);
                    Animation a=new AlphaAnimation(0, 1);//????????????250?????????0
                    a.setFillAfter(true);//???????????????????????????????????????????????????????????????
                    a.setDuration(500);
                    g.startAnimation(a);

                    g.setVisibility(0);
                    iv2.setVisibility(0);



                }
            });
        findViewById(R.id.tool2).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final View iv2=findViewById(R.id.menu);
                    Animator animator = ViewAnimationUtils.createCircularReveal(iv2, -iv2.getWidth(), iv2.getHeight() / 2, (float) Math.hypot(iv2.getWidth() * 2, iv2.getHeight()), 0);
                    animator.setInterpolator(new AccelerateDecelerateInterpolator());
                    animator.setDuration(500);
                    //animator.setInterpolator(new AccelerateDecelerateInterpolator());
                    animator.start();

                    final View g=findViewById(R.id.gery);
                    Animation a=new AlphaAnimation(1, 0);//????????????250?????????0
                    //???????????????????????????????????????????????????????????????
                    a.setDuration(500);
                    g.startAnimation(a);
                    new Thread(new Runnable(){
                            @Override
                            public void run() {
                                try {
                                    Thread.sleep(500);
                                } catch (InterruptedException e) {}
                                runOnUiThread(new Runnable(){
                                        @Override
                                        public void run() {
                                            g.setVisibility(8);
                                            iv2.setVisibility(8);
                                        }
                                    });

                            }
                        }).start();
                }
            });
        getImg.put(".js", R.drawable.ic_nodejs);
        getImg.put(".json", R.drawable.ic_code_json);
        getImg.put(".png", R.drawable.ic_image_outline);
        getImg.put(".jpg", R.drawable.ic_image_outline);
        getImg.put(".tga", R.drawable.ic_image_outline);
        adp = new RecyclerAdapter(){
            @Override
            public RecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
                return new ViewHolder(CreateView(viewGroup, R.layout.dir_list));
            }

            @Override
            public void onBindViewHolder(RecyclerAdapter.ViewHolder viewHolder, final int position) {
                View v=viewHolder.v;
                ((TextView)v.findViewById(R.id.name)).setText(files.get(position).getName());
                ImageView img=((ImageView)v.findViewById(R.id.img));

                String last=lastName(files.get(position));
                boolean pd=false;
                for (HashMap.Entry<String, Integer> entry : getImg.entrySet()) {
                    if (entry.getKey().equals(last)) {
                        img.setImageResource(entry.getValue());
                        pd = true;
                        break;
                    }

                }
                if (!pd) {
                    if (files.get(position).isDirectory()) {
                        img.setImageResource(R.drawable.ic_folder_outline);
                    } else {
                        img.setImageResource(R.drawable.ic_file_outline);
                    }
                }
                v.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (files.get(position).isDirectory()) {
                                setPath(getPath() + files.get(position).getName() + "/");
                            } else {
                                String name=files.get(position).getName();
                                if (lastName(name).endsWith("apk")) {
                                    setPath(getPath() + name);
                                } else {
                                    Intent in=new Intent(MainActivity.this, EditActivity.class);
                                    in.putExtra("p", getPath() + name);
                                    startActivity(in);
                                }
                            }
                        }
                    });
                Animation a=new AlphaAnimation(0, 1);//????????????250?????????0
                a.setFillAfter(true);//???????????????????????????????????????????????????????????????
                a.setDuration(250);
                v.startAnimation(a);
            }

            @Override
            public int getItemCount() {
                return files.size();
            }
        };
        RecyclerView recycler_view = findViewById(R.id.list);
        GridLayoutManager mLayoutManager = new GridLayoutManager(this, 4);
        recycler_view.setLayoutManager(mLayoutManager);
        recycler_view.setHasFixedSize(true);
        recycler_view.setAdapter(adp);
        //recycler_view.setNestedScrollingEnabled(false);

        findViewById(R.id.utw).setVisibility(8);
        reload(getPath());
        ((EditText)findViewById(R.id.path)).clearFocus();
        findViewById(R.id.gery).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
        setSidebarClick();
        upDateProjectList();
        ProLoading.load();
        new PromptDialog(MainActivity.this)
            .setDialogType(PromptDialog.DIALOG_TYPE_WARNING)
            .setAnimationEnable(true)
            .setTitleText("??????")
            .setContentText("?????????????????????????????????????????????(????????????????????????????????????)?????????????????????\n?????????????????????????????????????????????????????????????????????????????????Github???????????????\n????????????: aa??????")
            .setPositiveListener("??????", new PromptDialog.OnPositiveListener() {
                @Override
                public void onClick(PromptDialog dialog) {
                    dialog.dismiss();
                }
            }).show();
    }

    @Override
    public void onClick(View p1) {
        switch (p1.getId()) {
            case R.id.help:{
                    new PromptDialog(MainActivity.this)
                        .setDialogType(PromptDialog.DIALOG_TYPE_HELP)
                        .setAnimationEnable(true)
                        .setTitleText("??????")
                        .setContentText(AssetsUtil.getFromAssets(MainActivity.this, "help.txt"))
                        .setPositiveListener("??????", new PromptDialog.OnPositiveListener() {
                            @Override
                            public void onClick(PromptDialog dialog) {
                                dialog.dismiss();
                            }
                        }).show();
                    break;
                }
            case R.id.about:{
                    ColorDialog dialog = new ColorDialog(MainActivity.this);
                    dialog.setTitle("??????");
                    dialog.setContentText(AssetsUtil.getFromAssets(MainActivity.this, "about.txt"));
                    dialog.setColor(0xffffffff);
                    dialog.setTitleTextColor(0xff222222);
                    dialog.setContentTextColor(0xff222222);
                    dialog.setPositiveListener("??????", new ColorDialog.OnPositiveListener() {
                            @Override
                            public void onClick(ColorDialog dialog) {
                                //????????????
                                ClipboardManager clipboard = (ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
                                ClipData clip = ClipData.newPlainText("simple text", "https://github.com/AAswordman/Addon-IDE");
                                clipboard.setPrimaryClip(clip);
                                dialog.dismiss();


                                new PromptDialog(MainActivity.this)
                                    .setDialogType(PromptDialog.DIALOG_TYPE_SUCCESS)
                                    .setAnimationEnable(true)
                                    .setTitleText("SUCCESS")
                                    .setContentText("??????????????????")
                                    .setPositiveListener("??????", new PromptDialog.OnPositiveListener() {
                                        @Override
                                        public void onClick(PromptDialog dialog) {
                                            dialog.dismiss();
                                        }
                                    }).show();

                            }
                        })
                        .setNegativeListener("??????", new ColorDialog.OnNegativeListener() {
                            @Override
                            public void onClick(ColorDialog dialog) {


                                dialog.dismiss();
                                AlertDialog d = new AlertDialog.Builder(MainActivity.this)
                                    .setTitle("?????????????????????")
                                    .setMessage(AssetsUtil.getFromAssets(MainActivity.this, "about2.txt"))
                                    .setPositiveButton("??????", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dia, int which) {

                                        }
                                    })

                                    .create();
                                d.show();


                            }
                        }).show();
                    break;
                }
            case R.id.c_b_a:{
                    ColorDialog dialog = new ColorDialog(MainActivity.this);
                    dialog.setTitle("????????????");
                    dialog.setContentText(Global.BehaviorPath);
                    dialog.setColor(0xffffffff);
                    dialog.setTitleTextColor(0xff222222);
                    dialog.setContentTextColor(0xff222222);
                    dialog.setPositiveListener("??????", new ColorDialog.OnPositiveListener() {
                            @Override
                            public void onClick(ColorDialog dialog) {
                                Global.BehaviorPath = null;
                                upDateProjectList();

                                dialog.dismiss();

                                new PromptDialog(MainActivity.this)
                                    .setDialogType(PromptDialog.DIALOG_TYPE_SUCCESS)
                                    .setAnimationEnable(true)
                                    .setTitleText("SUCCESS")
                                    .setContentText("????????????????????????")
                                    .setPositiveListener("??????", new PromptDialog.OnPositiveListener() {
                                        @Override
                                        public void onClick(PromptDialog dialog) {
                                            dialog.dismiss();
                                        }
                                    }).show();

                            }
                        })
                        .setNegativeListener("??????", new ColorDialog.OnNegativeListener() {
                            @Override
                            public void onClick(ColorDialog dialog) {

                                dialog.dismiss();
                            }
                        }).show();
                    break;

                }

            case R.id.c_b_b:{
                    ColorDialog dialog = new ColorDialog(MainActivity.this);
                    dialog.setTitle("??????????????????");
                    dialog.setContentText(Global.sampleBehaviorPath.toString().replace("[", "").replace(",", "\n").replace("]", "").replace("\\/", "/"));
                    dialog.setColor(0xffffffff);
                    dialog.setTitleTextColor(0xff222222);
                    dialog.setContentTextColor(0xff222222);
                    dialog.setPositiveListener("??????", new ColorDialog.OnPositiveListener() {
                            @Override
                            public void onClick(ColorDialog dialog) {
                                Global.sampleBehaviorPath = new JSONArray();

                                dialog.dismiss();

                                new PromptDialog(MainActivity.this)
                                    .setDialogType(PromptDialog.DIALOG_TYPE_SUCCESS)
                                    .setAnimationEnable(true)
                                    .setTitleText("SUCCESS")
                                    .setContentText("??????????????????????????????")
                                    .setPositiveListener("??????", new PromptDialog.OnPositiveListener() {
                                        @Override
                                        public void onClick(PromptDialog dialog) {
                                            dialog.dismiss();
                                        }
                                    }).show();

                            }
                        })
                        .setNegativeListener("??????", new ColorDialog.OnNegativeListener() {
                            @Override
                            public void onClick(ColorDialog dialog) {

                                dialog.dismiss();
                            }
                        }).show();
                    break;
                }
            case R.id.c_r_a:{
                    ColorDialog dialog = new ColorDialog(MainActivity.this);
                    dialog.setTitle("????????????");
                    dialog.setContentText(Global.ResourcePath);
                    dialog.setColor(0xffffffff);
                    dialog.setTitleTextColor(0xff222222);
                    dialog.setContentTextColor(0xff222222);
                    dialog.setPositiveListener("??????", new ColorDialog.OnPositiveListener() {
                            @Override
                            public void onClick(ColorDialog dialog) {
                                Global.ResourcePath = null;
                                upDateProjectList();

                                dialog.dismiss();

                                new PromptDialog(MainActivity.this)
                                    .setDialogType(PromptDialog.DIALOG_TYPE_SUCCESS)
                                    .setAnimationEnable(true)
                                    .setTitleText("SUCCESS")
                                    .setContentText("????????????????????????")
                                    .setPositiveListener("??????", new PromptDialog.OnPositiveListener() {
                                        @Override
                                        public void onClick(PromptDialog dialog) {
                                            dialog.dismiss();
                                        }
                                    }).show();

                            }
                        })
                        .setNegativeListener("??????", new ColorDialog.OnNegativeListener() {
                            @Override
                            public void onClick(ColorDialog dialog) {

                                dialog.dismiss();
                            }
                        }).show();

                    break;
                }
            case R.id.c_r_b:{
                    ColorDialog dialog = new ColorDialog(MainActivity.this);
                    dialog.setTitle("??????????????????");
                    dialog.setContentText(Global.sampleResourcePath.toString().replace("[", "").replace(",", "\n").replace("]", "").replace("\\/", "/"));
                    dialog.setColor(0xffffffff);
                    dialog.setTitleTextColor(0xff222222);
                    dialog.setContentTextColor(0xff222222);
                    dialog.setPositiveListener("??????", new ColorDialog.OnPositiveListener() {
                            @Override
                            public void onClick(ColorDialog dialog) {
                                Global.sampleResourcePath = new JSONArray();

                                dialog.dismiss();

                                new PromptDialog(MainActivity.this)
                                    .setDialogType(PromptDialog.DIALOG_TYPE_SUCCESS)
                                    .setAnimationEnable(true)
                                    .setTitleText("SUCCESS")
                                    .setContentText("??????????????????????????????")
                                    .setPositiveListener("??????", new PromptDialog.OnPositiveListener() {
                                        @Override
                                        public void onClick(PromptDialog dialog) {
                                            dialog.dismiss();
                                        }
                                    }).show();

                            }
                        })
                        .setNegativeListener("??????", new ColorDialog.OnNegativeListener() {
                            @Override
                            public void onClick(ColorDialog dialog) {

                                dialog.dismiss();
                            }
                        }).show();
                    break;
                }
            case R.id.c_p_a:{
                    ColorDialog dialog = new ColorDialog(MainActivity.this);
                    dialog.setTitle("PVZMOD??????");
                    dialog.setContentText(Global.PvzModPath);
                    dialog.setColor(0xffffffff);
                    dialog.setTitleTextColor(0xff222222);
                    dialog.setContentTextColor(0xff222222);
                    dialog.setPositiveListener("??????", new ColorDialog.OnPositiveListener() {
                            @Override
                            public void onClick(ColorDialog dialog) {
                                Global.PvzModPath = null;
                                upDateProjectList();

                                dialog.dismiss();

                                new PromptDialog(MainActivity.this)
                                    .setDialogType(PromptDialog.DIALOG_TYPE_SUCCESS)
                                    .setAnimationEnable(true)
                                    .setTitleText("SUCCESS")
                                    .setContentText("PVZMOD??????????????????")
                                    .setPositiveListener("??????", new PromptDialog.OnPositiveListener() {
                                        @Override
                                        public void onClick(PromptDialog dialog) {
                                            dialog.dismiss();
                                        }
                                    }).show();

                            }
                        })
                        .setNegativeListener("??????", new ColorDialog.OnNegativeListener() {
                            @Override
                            public void onClick(ColorDialog dialog) {

                                dialog.dismiss();
                            }
                        }).show();

                    break;
                }
            case R.id.c_p_b:{
                    ColorDialog dialog = new ColorDialog(MainActivity.this);
                    dialog.setTitle("PVZMOD????????????");
                    dialog.setContentText(Global.samplePvzModPath.toString().replace("[", "").replace(",", "\n").replace("]", "").replace("\\/", "/"));
                    dialog.setColor(0xffffffff);
                    dialog.setTitleTextColor(0xff222222);
                    dialog.setContentTextColor(0xff222222);
                    dialog.setPositiveListener("??????", new ColorDialog.OnPositiveListener() {
                            @Override
                            public void onClick(ColorDialog dialog) {
                                Global.samplePvzModPath = new JSONArray();

                                dialog.dismiss();

                                new PromptDialog(MainActivity.this)
                                    .setDialogType(PromptDialog.DIALOG_TYPE_SUCCESS)
                                    .setAnimationEnable(true)
                                    .setTitleText("SUCCESS")
                                    .setContentText("PVZMOD????????????????????????")
                                    .setPositiveListener("??????", new PromptDialog.OnPositiveListener() {
                                        @Override
                                        public void onClick(PromptDialog dialog) {
                                            dialog.dismiss();
                                        }
                                    }).show();

                            }
                        })
                        .setNegativeListener("??????", new ColorDialog.OnNegativeListener() {
                            @Override
                            public void onClick(ColorDialog dialog) {

                                dialog.dismiss();
                            }
                        }).show();
                    break;
                }
            case R.id.start:{
                    if (alreadyLoad)return;
                    this.alreadyLoad = true;
                    startDialogue = new PromptDialog(MainActivity.this)
                        .setDialogType(PromptDialog.DIALOG_TYPE_DEFAULT)
                        .setAnimationEnable(true)
                        .setTitleText("?????????")
                        .setContentText("???????????????????????????????????????????????????????????????")
                        .setPositiveListener("??????", new PromptDialog.OnPositiveListener() {
                            @Override
                            public void onClick(PromptDialog dialog) {
                                dialog.dismiss();
                            }
                        });
                    startDialogue.show();

                    mHandler.sendEmptyMessage(0);

                    new Thread(new Runnable(){

                            @Override
                            public void run() {
                                CFile file=new CFile(Config.baseDir + "proCompletion");
                                file.deleteDir();
                                if (Global.BehaviorPath != null) ProLoading.load(MainActivity.this, BEHAVIORS);
                                try {
                                    Thread.sleep(500);
                                } catch (InterruptedException e) {}
                                if (Global.ResourcePath != null) ProLoading.load(MainActivity.this, RESOURCES);
                                try {
                                    Thread.sleep(500);
                                } catch (InterruptedException e) {}
                                if (Global.PvzModPath != null) ProLoading.load(MainActivity.this, PVZPLUG);
                                runOnUiThread(
                                    new Runnable(){
                                        @Override
                                        public void run() {
                                            startDialogue.dismiss();
                                            Global.isInProject = true;
                                            upDateProjectList();
                                            ProLoading.load();

                                            new PromptDialog(MainActivity.this)
                                                .setDialogType(PromptDialog.DIALOG_TYPE_SUCCESS)
                                                .setAnimationEnable(true)
                                                .setTitleText("SUCCESS")
                                                .setContentText("?????????????????????????????????????????????")
                                                .setPositiveListener("??????", new PromptDialog.OnPositiveListener() {
                                                    @Override
                                                    public void onClick(PromptDialog dialog) {
                                                        dialog.dismiss();
                                                    }
                                                }).show();

                                        }
                                    });

                            }}).start();
                    break;
                }
            case R.id.end:{
                    Toast.makeText(getApplication(), "????????????", Toast.LENGTH_SHORT).show();
                    Global.isInProject = false;
                    ProLoading.close();
                    upDateProjectList();
                    saveGlobal();
                    this.alreadyLoad = false;

                    //ldb???????????????????????????????????????
                    //????????????????????????
                    System.exit(0);
                    break;
                }
            case R.id.w_b_a:{
                    setPath(Global.BehaviorPath);
                    break;
                }
            case R.id.w_r_a:{
                    setPath(Global.ResourcePath);
                    break;
                }
            case R.id.w_p_a:{
                    setPath(Global.PvzModPath);
                    break;
                }
        }
    }
    private void setSidebarClick() {
        findViewById(R.id.help).setOnClickListener(this);
        findViewById(R.id.about).setOnClickListener(this);
        findViewById(R.id.c_b_a).setOnClickListener(this);
        findViewById(R.id.c_b_b).setOnClickListener(this);
        findViewById(R.id.c_r_a).setOnClickListener(this);
        findViewById(R.id.c_r_b).setOnClickListener(this);
        findViewById(R.id.c_p_a).setOnClickListener(this);
        findViewById(R.id.c_p_b).setOnClickListener(this);
        findViewById(R.id.start).setOnClickListener(this);
        findViewById(R.id.end).setOnClickListener(this);
        findViewById(R.id.w_b_a).setOnClickListener(this);
        findViewById(R.id.w_r_a).setOnClickListener(this);
        findViewById(R.id.w_p_a).setOnClickListener(this);
    }

    private void loadGlobal() {
        if (globalSave.file.exists()) {
            try {
                Global.i.set((Global)JSONTools.parse(Global.class, globalSave.read()));

            } catch (IOException e) {}
        } else {
            Global.i.set(new Global());
        }
    }

    @Override
    public void onBackPressed() {
        if (!back()) {
            if (System.currentTimeMillis() - firstBackTime > 2000) {
                Toast.makeText(this, "?????????????????????????????????", Toast.LENGTH_SHORT).show();
                firstBackTime = System.currentTimeMillis();
                return;
            }
            System.exit(1);
        }
    }
    //??????lastIndexOf()??????subString()???????????????
    public static String lastName(File file) {
        if (file == null) return null;
        String filename = file.getName();
        return lastName(filename);

        //return filename.subString(filename.lastIndexOf(".")+1);// ????????????????????????.????????????

        // ????????????????????????String??????????????????????????? ???.??????????????????????????????????????????????????????????????????
    }
    //??????lastIndexOf()??????subString()???????????????
    public static String lastName(String filename) {
        if (filename.lastIndexOf(".") == -1) {
            return "";//??????????????????????????????
        }
        //???????????????????????? . ???????????????
        return filename.substring(filename.lastIndexOf("."));

        //return filename.subString(filename.lastIndexOf(".")+1);// ????????????????????????.????????????

        // ????????????????????????String??????????????????????????? ???.??????????????????????????????????????????????????????????????????
    }


    public String getPath() {
        return ((TextView)findViewById(R.id.path)).getText().toString();
    }


    public boolean back() {
        StringBuilder p=new StringBuilder();
        String[] arr=getPath().split("/");
        if (arr.length == 4)return false;
        int i=0;
        for (String nr :arr) {
            p.append(nr + "/");
            if (i >= arr.length - 2) {
                break;
            }
            i++;
        }

        setPath(p.toString());
        return true;
    }

    public void setPath(String p) {
        ((TextView)findViewById(R.id.path)).setText(p);
        if (p.endsWith("apk")) {
            showUse(1);
        } else if (p.indexOf("com.mojang/minecraftWorlds/") != -1) {
            showUse(2);
            reload(p);
        } else {
            //closeUse();
            reload(p);
            proEdit();
        }
    }

    private void showUse(int showIndex) {
        if (!Global.isInProject) {
            View[] id=new View[]{findViewById(R.id.inDir),findViewById(R.id.importa),findViewById(R.id.mapUse)};
            int i=0;
            for (View v :id) {
                if (showIndex == i) {
                    v.setVisibility(0);
                } else {
                    v.setVisibility(8);
                }

                i++;
            }
            View w=findViewById(R.id.utw);
            w.setVisibility(0);
            Animation a=new TranslateAnimation(-w.getWidth() - w.getLeft(), 0, 0, 0);

            a.setDuration(800);
            w.startAnimation(a);
            utwIsClose = false;


            //????????????
            switch (type) {
                case BEHAVIORS:{
                        findViewById(R.id.add_p).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Toast.makeText(getApplication(), "????????????????????????", Toast.LENGTH_SHORT).show();
                                    Global.BehaviorPath = getPath();
                                    upDateProjectList();

                                }
                            });
                        findViewById(R.id.add_s).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Toast.makeText(getApplication(), "????????????????????????", Toast.LENGTH_SHORT).show();
                                    Global.sampleBehaviorPath.put(getPath());
                                }
                            });

                        break;
                    }
                case RESOURCES:{
                        findViewById(R.id.add_p).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Toast.makeText(getApplication(), "????????????????????????", Toast.LENGTH_SHORT).show();
                                    Global.ResourcePath = getPath();
                                    upDateProjectList();
                                }
                            });
                        findViewById(R.id.add_s).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Toast.makeText(getApplication(), "????????????????????????", Toast.LENGTH_SHORT).show();
                                    Global.sampleResourcePath.put(getPath());
                                }
                            });
                        break;
                    }
                case PVZPLUG:{
                        findViewById(R.id.add_p).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if (Global.BehaviorPath == null && Global.ResourcePath == null) {
                                        Toast.makeText(getApplication(), "PVZMOD??????????????????", Toast.LENGTH_SHORT).show();
                                        Global.PvzModPath = getPath();
                                        upDateProjectList();
                                    } else {
                                        Toast.makeText(getApplication(), "????????????????????????MC????????????????????????PVZ??????", Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });
                        findViewById(R.id.add_s).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Toast.makeText(getApplication(), "PVZMOD??????????????????", Toast.LENGTH_SHORT).show();
                                    Global.samplePvzModPath.put(getPath());
                                }
                            });
                        break;
                    }
            }
            findViewById(R.id.importa).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(getApplication(), "PVZ CORE?????????????????????????????????????????????", Toast.LENGTH_SHORT).show();
                        Global.importPVZPath = getPath();
                    }
                });
            findViewById(R.id.importb).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(getApplication(), "??????????????????????????????", Toast.LENGTH_SHORT).show();
                        Global.importMapPath = getPath();
                    }
                });
            findViewById(R.id.map_total).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent in=new Intent(MainActivity.this, MapTotalActivity.class);
                        in.putExtra("path", getPath());
                        startActivity(in);
                    }
                });

        }
    }

    private void upDateProjectList() {
        if (!Global.isInProject) {
            findViewById(R.id.c).setVisibility(0);
            findViewById(R.id.w).setVisibility(8);
            findViewById(R.id.c_b).setVisibility(Global.BehaviorPath == null ?8: 0);
            findViewById(R.id.c_r).setVisibility(Global.ResourcePath == null ?8: 0);
            findViewById(R.id.c_p).setVisibility(Global.PvzModPath == null ?8: 0);

        } else {
            findViewById(R.id.c).setVisibility(8);
            findViewById(R.id.w).setVisibility(0);
            findViewById(R.id.w_b).setVisibility(Global.BehaviorPath == null ?8: 0);
            findViewById(R.id.w_r).setVisibility(Global.ResourcePath == null ?8: 0);
            findViewById(R.id.w_p).setVisibility(Global.PvzModPath == null ?8: 0);
        }

        saveGlobal();
    }



    private void closeUse() {


        if (!utwIsClose) {
            final View w=findViewById(R.id.utw);
            utwIsClose = true;
            Animation a=new TranslateAnimation(0, -w.getWidth() - w.getLeft(), 0, 0);

            a.setDuration(800);
            w.startAnimation(a);
            new Thread(new Runnable(){
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(800);
                        } catch (InterruptedException e) {}
                        if (utwIsClose) {
                            runOnUiThread(new Runnable(){
                                    @Override
                                    public void run() {
                                        w.setVisibility(8);
                                    }
                                });
                        }

                    }
                }).start();
        }

    }
    private long firstBackTime;

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
        //????????????

        adp.notifyDataSetChanged();

    }

    private void proEdit() {
        new Thread(new selectFile()).start();
    }

    @Override
    protected void onStop() {
        saveGlobal();
        super.onStop();
    }

    private void saveGlobal() {
        try {
            globalSave.write(Global.i.toString());
        } catch (IOException e) {}
    }



    @Override
    protected void onStart() {
        super.onStart();
        checkPermission();
    }

    private void checkPermission() {
        //????????????
        if (Build.VERSION.SDK_INT >= 23) {
            int REQUEST_CODE_CONTACT = 101;
            String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.INTERNET};
            //????????????????????????
            for (String str : permissions) {
                if (this.checkSelfPermission(str) != PackageManager.PERMISSION_GRANTED) {
                    //????????????
                    this.requestPermissions(permissions, REQUEST_CODE_CONTACT);
                }
            }
        }

    }
    public class selectFile implements Runnable {
        public boolean complete=false;
        @Override
        public void run() {
            if (!complete) {
                complete = true;
                boolean exist=false;
                CFile usefile;
                if ((usefile = new CFile(getPath() + "plug.json")).file.exists()) {
                    //pvz mod
                    try {
                        JSONObject nr=new JSONObject(usefile.read());
                        nr.getJSONObject("camera");
                        exist = true;
                        type = PVZPLUG;
                    } catch (IOException|JSONException e) {

                    }

                }
                if ((usefile = new CFile(getPath() + "manifest.json")).file.exists()) {
                    //addon
                    try {
                        JSONObject nr=new JSONObject(usefile.read());
                        String typex=nr.getJSONArray("modules").getJSONObject(0).getString("type");
                        if (typex.equals("data")) {

                            type = BEHAVIORS;
                        } else if (typex.equals("resources")) {
                            type = RESOURCES;
                        } else {

                        }
                        exist = true;
                    } catch (IOException|JSONException e) {

                    }
                }
                if (exist) {
                    runOnUiThread(new Runnable(){
                            @Override
                            public void run() {

                                showUse(0);
                            }
                        });
                } else {
                    runOnUiThread(new Runnable(){
                            @Override
                            public void run() {
                                closeUse();
                            }
                        });
                }
                complete = false;
            }
        }
    }
}
