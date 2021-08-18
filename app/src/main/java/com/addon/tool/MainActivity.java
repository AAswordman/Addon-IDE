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
<<<<<<< HEAD
import android.support.annotation.NonNull;
import android.os.Handler;
import android.os.Message;
import android.view.WindowManager;

public class MainActivity extends AppCompatActivity 
implements View.OnClickListener {



=======

public class MainActivity extends AppCompatActivity {
>>>>>>> aa41e3931568d839484ad45420af53d27f2b4d68
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

<<<<<<< HEAD
    private boolean alreadyLoad;

    private PromptDialog startDialogue;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            int what = msg.what;
            if (what == 0) {//update
                startDialogue.getContentTextView().setText("工程加载"+ProLoading.getProgress()+"%,请耐心等待\n文件数: "+ProLoading.now+"/"+ProLoading.total);
                if(startDialogue.isShowing()){
                    mHandler.sendEmptyMessageDelayed(0,50);
                }
            }
        }
    };
    
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
=======
>>>>>>> aa41e3931568d839484ad45420af53d27f2b4d68
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
<<<<<<< HEAD
        Config.baseDir = "/sdcard/addonHelper/";
        globalSave = new CFile(Config.baseDir + "global.json");
		loadGlobal();
        
        hideBottomUIMenu();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
=======
        Config.baseDir += "addonHelper/";
        globalSave = new CFile(Config.baseDir + "global.json");
		loadGlobal();
>>>>>>> aa41e3931568d839484ad45420af53d27f2b4d68

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
                    Animation a=new AlphaAnimation(0, 1);//透明度从250渐变至0
                    a.setFillAfter(true);//将控件设置为动画的最后状态，也就是变为透明
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
                    Animation a=new AlphaAnimation(1, 0);//透明度从250渐变至0
                    //将控件设置为动画的最后状态，也就是变为透明
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
                Animation a=new AlphaAnimation(0, 1);//透明度从250渐变至0
                a.setFillAfter(true);//将控件设置为动画的最后状态，也就是变为透明
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
            .setTitleText("提示")
            .setContentText("本软件完全免费，如果你是买来的(当然应该不会出现这个情况)，说明你被坑了\n本软件更新随缘，使用随缘，如果想更新，你可以去本软件的Github页面逛一逛\n软件作者: aa剑侠")
            .setPositiveListener("确定", new PromptDialog.OnPositiveListener() {
                @Override
                public void onClick(PromptDialog dialog) {
                    dialog.dismiss();
                }
            }).show();
    }

<<<<<<< HEAD
    @Override
    public void onClick(View p1) {
        switch (p1.getId()) {
                case R.id.help:{
=======
    private void setSidebarClick() {
        findViewById(R.id.help).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
>>>>>>> aa41e3931568d839484ad45420af53d27f2b4d68
                    new PromptDialog(MainActivity.this)
                        .setDialogType(PromptDialog.DIALOG_TYPE_HELP)
                        .setAnimationEnable(true)
                        .setTitleText("帮助")
                        .setContentText(AssetsUtil.getFromAssets(MainActivity.this, "help.txt"))
                        .setPositiveListener("确定", new PromptDialog.OnPositiveListener() {
                            @Override
                            public void onClick(PromptDialog dialog) {
                                dialog.dismiss();
                            }
                        }).show();
<<<<<<< HEAD
                    break;
                }
                case R.id.about:{
=======

                }
            });
        findViewById(R.id.about).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
>>>>>>> aa41e3931568d839484ad45420af53d27f2b4d68
                    ColorDialog dialog = new ColorDialog(MainActivity.this);
                    dialog.setTitle("关于");
                    dialog.setContentText(AssetsUtil.getFromAssets(MainActivity.this, "about.txt"));
                    dialog.setColor(0xffffffff);
                    dialog.setTitleTextColor(0xff222222);
                    dialog.setContentTextColor(0xff222222);
                    dialog.setPositiveListener("复制", new ColorDialog.OnPositiveListener() {
                            @Override
                            public void onClick(ColorDialog dialog) {
                                //复制文本
                                ClipboardManager clipboard = (ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
                                ClipData clip = ClipData.newPlainText("simple text", "https://github.com/AAswordman/Addon-IDE");
                                clipboard.setPrimaryClip(clip);
                                dialog.dismiss();
<<<<<<< HEAD


=======
                                
                                
>>>>>>> aa41e3931568d839484ad45420af53d27f2b4d68
                                new PromptDialog(MainActivity.this)
                                    .setDialogType(PromptDialog.DIALOG_TYPE_SUCCESS)
                                    .setAnimationEnable(true)
                                    .setTitleText("SUCCESS")
                                    .setContentText("路径复制成功")
                                    .setPositiveListener("确定", new PromptDialog.OnPositiveListener() {
                                        @Override
                                        public void onClick(PromptDialog dialog) {
                                            dialog.dismiss();
                                        }
                                    }).show();

                            }
                        })
                        .setNegativeListener("确定", new ColorDialog.OnNegativeListener() {
                            @Override
                            public void onClick(ColorDialog dialog) {


                                dialog.dismiss();
                                AlertDialog d = new AlertDialog.Builder(MainActivity.this)
                                    .setTitle("开放源代码许可")
                                    .setMessage(AssetsUtil.getFromAssets(MainActivity.this, "about2.txt"))
                                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dia, int which) {

                                        }
                                    })

                                    .create();
                                d.show();


                            }
                        }).show();
<<<<<<< HEAD
                    break;
                }
                case R.id.c_b_a:{
=======

                }
            });
        findViewById(R.id.c_b_a).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

>>>>>>> aa41e3931568d839484ad45420af53d27f2b4d68
                    ColorDialog dialog = new ColorDialog(MainActivity.this);
                    dialog.setTitle("行为路径");
                    dialog.setContentText(Global.BehaviorPath);
                    dialog.setColor(0xffffffff);
                    dialog.setTitleTextColor(0xff222222);
                    dialog.setContentTextColor(0xff222222);
                    dialog.setPositiveListener("重设", new ColorDialog.OnPositiveListener() {
                            @Override
                            public void onClick(ColorDialog dialog) {
                                Global.BehaviorPath = null;
                                upDateProjectList();

                                dialog.dismiss();

                                new PromptDialog(MainActivity.this)
                                    .setDialogType(PromptDialog.DIALOG_TYPE_SUCCESS)
                                    .setAnimationEnable(true)
                                    .setTitleText("SUCCESS")
                                    .setContentText("行为路径清空成功")
                                    .setPositiveListener("确定", new PromptDialog.OnPositiveListener() {
                                        @Override
                                        public void onClick(PromptDialog dialog) {
                                            dialog.dismiss();
                                        }
                                    }).show();

                            }
                        })
                        .setNegativeListener("确定", new ColorDialog.OnNegativeListener() {
                            @Override
                            public void onClick(ColorDialog dialog) {

                                dialog.dismiss();
                            }
                        }).show();
<<<<<<< HEAD
                    break;

                }

                case R.id.c_b_b:{
=======


                }
            });
        findViewById(R.id.c_b_b).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
>>>>>>> aa41e3931568d839484ad45420af53d27f2b4d68
                    ColorDialog dialog = new ColorDialog(MainActivity.this);
                    dialog.setTitle("行为样本路径");
                    dialog.setContentText(Global.sampleBehaviorPath.toString().replace("[", "").replace(",", "\n").replace("]", "").replace("\\/", "/"));
                    dialog.setColor(0xffffffff);
                    dialog.setTitleTextColor(0xff222222);
                    dialog.setContentTextColor(0xff222222);
                    dialog.setPositiveListener("重设", new ColorDialog.OnPositiveListener() {
                            @Override
                            public void onClick(ColorDialog dialog) {
                                Global.sampleBehaviorPath = new JSONArray();

                                dialog.dismiss();

                                new PromptDialog(MainActivity.this)
                                    .setDialogType(PromptDialog.DIALOG_TYPE_SUCCESS)
                                    .setAnimationEnable(true)
                                    .setTitleText("SUCCESS")
                                    .setContentText("行为样本路径清空成功")
                                    .setPositiveListener("确定", new PromptDialog.OnPositiveListener() {
                                        @Override
                                        public void onClick(PromptDialog dialog) {
                                            dialog.dismiss();
                                        }
                                    }).show();

                            }
                        })
                        .setNegativeListener("确定", new ColorDialog.OnNegativeListener() {
                            @Override
                            public void onClick(ColorDialog dialog) {

                                dialog.dismiss();
                            }
                        }).show();
<<<<<<< HEAD
                    break;
                }
                case R.id.c_r_a:{
=======

                }
            });
        findViewById(R.id.c_r_a).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
>>>>>>> aa41e3931568d839484ad45420af53d27f2b4d68
                    ColorDialog dialog = new ColorDialog(MainActivity.this);
                    dialog.setTitle("材质路径");
                    dialog.setContentText(Global.ResourcePath);
                    dialog.setColor(0xffffffff);
                    dialog.setTitleTextColor(0xff222222);
                    dialog.setContentTextColor(0xff222222);
                    dialog.setPositiveListener("重设", new ColorDialog.OnPositiveListener() {
                            @Override
                            public void onClick(ColorDialog dialog) {
                                Global.ResourcePath = null;
                                upDateProjectList();

                                dialog.dismiss();

                                new PromptDialog(MainActivity.this)
                                    .setDialogType(PromptDialog.DIALOG_TYPE_SUCCESS)
                                    .setAnimationEnable(true)
                                    .setTitleText("SUCCESS")
                                    .setContentText("材质路径清空成功")
                                    .setPositiveListener("确定", new PromptDialog.OnPositiveListener() {
                                        @Override
                                        public void onClick(PromptDialog dialog) {
                                            dialog.dismiss();
                                        }
                                    }).show();

                            }
                        })
                        .setNegativeListener("确定", new ColorDialog.OnNegativeListener() {
                            @Override
                            public void onClick(ColorDialog dialog) {

                                dialog.dismiss();
                            }
                        }).show();

<<<<<<< HEAD
                    break;
                }
                case R.id.c_r_b:{
=======
                }
            });
        findViewById(R.id.c_r_b).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
>>>>>>> aa41e3931568d839484ad45420af53d27f2b4d68
                    ColorDialog dialog = new ColorDialog(MainActivity.this);
                    dialog.setTitle("材质样本路径");
                    dialog.setContentText(Global.sampleResourcePath.toString().replace("[", "").replace(",", "\n").replace("]", "").replace("\\/", "/"));
                    dialog.setColor(0xffffffff);
                    dialog.setTitleTextColor(0xff222222);
                    dialog.setContentTextColor(0xff222222);
                    dialog.setPositiveListener("重设", new ColorDialog.OnPositiveListener() {
                            @Override
                            public void onClick(ColorDialog dialog) {
                                Global.sampleResourcePath = new JSONArray();

                                dialog.dismiss();

                                new PromptDialog(MainActivity.this)
                                    .setDialogType(PromptDialog.DIALOG_TYPE_SUCCESS)
                                    .setAnimationEnable(true)
                                    .setTitleText("SUCCESS")
                                    .setContentText("材质样本路径清空成功")
                                    .setPositiveListener("确定", new PromptDialog.OnPositiveListener() {
                                        @Override
                                        public void onClick(PromptDialog dialog) {
                                            dialog.dismiss();
                                        }
                                    }).show();

                            }
                        })
                        .setNegativeListener("确定", new ColorDialog.OnNegativeListener() {
                            @Override
                            public void onClick(ColorDialog dialog) {

                                dialog.dismiss();
                            }
                        }).show();
<<<<<<< HEAD
                    break;
                }
                case R.id.c_p_a:{
=======
                }
            });
        findViewById(R.id.c_p_a).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
>>>>>>> aa41e3931568d839484ad45420af53d27f2b4d68
                    ColorDialog dialog = new ColorDialog(MainActivity.this);
                    dialog.setTitle("PVZMOD路径");
                    dialog.setContentText(Global.PvzModPath);
                    dialog.setColor(0xffffffff);
                    dialog.setTitleTextColor(0xff222222);
                    dialog.setContentTextColor(0xff222222);
                    dialog.setPositiveListener("重设", new ColorDialog.OnPositiveListener() {
                            @Override
                            public void onClick(ColorDialog dialog) {
                                Global.PvzModPath = null;
                                upDateProjectList();

                                dialog.dismiss();

                                new PromptDialog(MainActivity.this)
                                    .setDialogType(PromptDialog.DIALOG_TYPE_SUCCESS)
                                    .setAnimationEnable(true)
                                    .setTitleText("SUCCESS")
                                    .setContentText("PVZMOD路径清空成功")
                                    .setPositiveListener("确定", new PromptDialog.OnPositiveListener() {
                                        @Override
                                        public void onClick(PromptDialog dialog) {
                                            dialog.dismiss();
                                        }
                                    }).show();

                            }
                        })
                        .setNegativeListener("确定", new ColorDialog.OnNegativeListener() {
                            @Override
                            public void onClick(ColorDialog dialog) {

                                dialog.dismiss();
                            }
                        }).show();

<<<<<<< HEAD
                    break;
                }
                case R.id.c_p_b:{
=======
                }
            });
        findViewById(R.id.c_p_b).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
>>>>>>> aa41e3931568d839484ad45420af53d27f2b4d68
                    ColorDialog dialog = new ColorDialog(MainActivity.this);
                    dialog.setTitle("PVZMOD样本路径");
                    dialog.setContentText(Global.samplePvzModPath.toString().replace("[", "").replace(",", "\n").replace("]", "").replace("\\/", "/"));
                    dialog.setColor(0xffffffff);
                    dialog.setTitleTextColor(0xff222222);
                    dialog.setContentTextColor(0xff222222);
                    dialog.setPositiveListener("重设", new ColorDialog.OnPositiveListener() {
                            @Override
                            public void onClick(ColorDialog dialog) {
                                Global.samplePvzModPath = new JSONArray();

                                dialog.dismiss();

                                new PromptDialog(MainActivity.this)
                                    .setDialogType(PromptDialog.DIALOG_TYPE_SUCCESS)
                                    .setAnimationEnable(true)
                                    .setTitleText("SUCCESS")
                                    .setContentText("PVZMOD样本路径清空成功")
                                    .setPositiveListener("确定", new PromptDialog.OnPositiveListener() {
                                        @Override
                                        public void onClick(PromptDialog dialog) {
                                            dialog.dismiss();
                                        }
                                    }).show();

                            }
                        })
                        .setNegativeListener("确定", new ColorDialog.OnNegativeListener() {
                            @Override
                            public void onClick(ColorDialog dialog) {

                                dialog.dismiss();
                            }
                        }).show();
<<<<<<< HEAD
                    break;
                }
                case R.id.start:{
                    if (alreadyLoad)return;
                    this.alreadyLoad = true;
                    startDialogue=new PromptDialog(MainActivity.this)
=======

                }

            });
        findViewById(R.id.start).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new PromptDialog(MainActivity.this)
>>>>>>> aa41e3931568d839484ad45420af53d27f2b4d68
                        .setDialogType(PromptDialog.DIALOG_TYPE_DEFAULT)
                        .setAnimationEnable(true)
                        .setTitleText("加载中")
                        .setContentText("正在加载内容，请保持手机亮屏并在该界面等待")
                        .setPositiveListener("确定", new PromptDialog.OnPositiveListener() {
                            @Override
                            public void onClick(PromptDialog dialog) {
                                dialog.dismiss();
                            }
<<<<<<< HEAD
                        });
                    startDialogue.show();
                    
                    mHandler.sendEmptyMessage(0);
                    
=======
                        }).show();

>>>>>>> aa41e3931568d839484ad45420af53d27f2b4d68
                    new Thread(new Runnable(){

                            @Override
                            public void run() {
                                CFile file=new CFile(Config.baseDir + "proCompletion");
                                file.deleteDir();
                                if (Global.BehaviorPath != null) ProLoading.load(MainActivity.this, BEHAVIORS);

                                if (Global.ResourcePath != null) ProLoading.load(MainActivity.this, RESOURCES);

                                if (Global.PvzModPath != null) ProLoading.load(MainActivity.this, PVZPLUG);
                                runOnUiThread(
                                    new Runnable(){
                                        @Override
                                        public void run() {
<<<<<<< HEAD
                                            startDialogue.dismiss();
=======

>>>>>>> aa41e3931568d839484ad45420af53d27f2b4d68
                                            Global.isInProject = true;
                                            upDateProjectList();
                                            ProLoading.load();

                                            new PromptDialog(MainActivity.this)
                                                .setDialogType(PromptDialog.DIALOG_TYPE_SUCCESS)
                                                .setAnimationEnable(true)
                                                .setTitleText("SUCCESS")
                                                .setContentText("工程载入完毕，你可以开始使用了")
                                                .setPositiveListener("确定", new PromptDialog.OnPositiveListener() {
                                                    @Override
                                                    public void onClick(PromptDialog dialog) {
                                                        dialog.dismiss();
                                                    }
                                                }).show();

                                        }
                                    });

                            }}).start();
<<<<<<< HEAD
                    break;
                }
                case R.id.end:{
=======

                }
            });
        findViewById(R.id.end).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
>>>>>>> aa41e3931568d839484ad45420af53d27f2b4d68
                    Toast.makeText(getApplication(), "退出完毕", Toast.LENGTH_SHORT).show();
                    Global.isInProject = false;
                    ProLoading.close();
                    upDateProjectList();
                    saveGlobal();
<<<<<<< HEAD
                    this.alreadyLoad = false;
=======

>>>>>>> aa41e3931568d839484ad45420af53d27f2b4d68

                    //ldb如果不退出会在第二次加载炸
                    //我也不知道为什么
                    System.exit(0);
<<<<<<< HEAD
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
=======
                }
            });
>>>>>>> aa41e3931568d839484ad45420af53d27f2b4d68
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
                Toast.makeText(this, "再按一次返回键退出程序", Toast.LENGTH_SHORT).show();
                firstBackTime = System.currentTimeMillis();
                return;
            }
            System.exit(1);
        }
    }
    //使用lastIndexOf()结合subString()获取后缀名
    public static String lastName(File file) {
        if (file == null) return null;
        String filename = file.getName();
        return lastName(filename);

        //return filename.subString(filename.lastIndexOf(".")+1);// 这种返回的是没有.的后缀名

        // 下面这种如果对于String类型可能有问题，如 以.结尾的字符串，会报错。但是文件没有以点结尾的
    }
    //使用lastIndexOf()结合subString()获取后缀名
    public static String lastName(String filename) {
        if (filename.lastIndexOf(".") == -1) {
            return "";//文件没有后缀名的情况
        }
        //此时返回的是带有 . 的后缀名，
        return filename.substring(filename.lastIndexOf("."));

        //return filename.subString(filename.lastIndexOf(".")+1);// 这种返回的是没有.的后缀名

        // 下面这种如果对于String类型可能有问题，如 以.结尾的字符串，会报错。但是文件没有以点结尾的
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
<<<<<<< HEAD

        setPath(p.toString());
        return true;
    }

=======
        setPath(p.toString());
        return true;
    }
>>>>>>> aa41e3931568d839484ad45420af53d27f2b4d68
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
<<<<<<< HEAD
            View[] id=new View[]{findViewById(R.id.inDir),findViewById(R.id.importa),findViewById(R.id.mapUse)};
=======
            View[] id=new View[]{findViewById(R.id.inDir),findViewById(R.id.importa),findViewById(R.id.importb)};
>>>>>>> aa41e3931568d839484ad45420af53d27f2b4d68
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


            //加入监听
            switch (type) {
                    case BEHAVIORS:{
                        findViewById(R.id.add_p).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Toast.makeText(getApplication(), "行为工程导入成功", Toast.LENGTH_SHORT).show();
                                    Global.BehaviorPath = getPath();
                                    upDateProjectList();

                                }
                            });
                        findViewById(R.id.add_s).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Toast.makeText(getApplication(), "行为样本导入成功", Toast.LENGTH_SHORT).show();
                                    Global.sampleBehaviorPath.put(getPath());
                                }
                            });
<<<<<<< HEAD

=======
>>>>>>> aa41e3931568d839484ad45420af53d27f2b4d68
                        break;
                    }
                    case RESOURCES:{
                        findViewById(R.id.add_p).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Toast.makeText(getApplication(), "材质工程导入成功", Toast.LENGTH_SHORT).show();
                                    Global.ResourcePath = getPath();
                                    upDateProjectList();
                                }
                            });
                        findViewById(R.id.add_s).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Toast.makeText(getApplication(), "材质样本导入成功", Toast.LENGTH_SHORT).show();
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
                                        Toast.makeText(getApplication(), "PVZMOD工程导入成功", Toast.LENGTH_SHORT).show();
                                        Global.PvzModPath = getPath();
                                        upDateProjectList();
                                    } else {
                                        Toast.makeText(getApplication(), "你当然导入类型为MC相关，不可再导入PVZ相关", Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });
                        findViewById(R.id.add_s).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Toast.makeText(getApplication(), "PVZMOD样本导入成功", Toast.LENGTH_SHORT).show();
                                    Global.samplePvzModPath.put(getPath());
                                }
                            });
                        break;
                    }
            }
            findViewById(R.id.importa).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(getApplication(), "PVZ CORE路径设置完毕，请保持该文件存在", Toast.LENGTH_SHORT).show();
                        Global.importPVZPath = getPath();
                    }
                });
            findViewById(R.id.importb).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(getApplication(), "地图导入路径设置成功", Toast.LENGTH_SHORT).show();
                        Global.importMapPath = getPath();
                    }
                });
<<<<<<< HEAD
            findViewById(R.id.map_total).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent in=new Intent(MainActivity.this,MapTotalActivity.class);
                        in.putExtra("path",getPath());
                        startActivity(in);
                    }
                });
=======

>>>>>>> aa41e3931568d839484ad45420af53d27f2b4d68

        }
    }

    private void upDateProjectList() {
        if (!Global.isInProject) {
            findViewById(R.id.c).setVisibility(0);
            findViewById(R.id.w).setVisibility(8);
            findViewById(R.id.c_b).setVisibility(Global.BehaviorPath == null ?8: 0);
            findViewById(R.id.c_r).setVisibility(Global.ResourcePath == null ?8: 0);
            findViewById(R.id.c_p).setVisibility(Global.PvzModPath == null ?8: 0);
<<<<<<< HEAD

        } else {
            findViewById(R.id.c).setVisibility(8);
            findViewById(R.id.w).setVisibility(0);
            findViewById(R.id.w_b).setVisibility(Global.BehaviorPath == null ?8: 0);
            findViewById(R.id.w_r).setVisibility(Global.ResourcePath == null ?8: 0);
            findViewById(R.id.w_p).setVisibility(Global.PvzModPath == null ?8: 0);
        }

        saveGlobal();
=======
        } else {
            findViewById(R.id.c).setVisibility(8);
            findViewById(R.id.w).setVisibility(0);
        }
>>>>>>> aa41e3931568d839484ad45420af53d27f2b4d68
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
        //判定存在

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
        //申请权限
        if (Build.VERSION.SDK_INT >= 23) {
            int REQUEST_CODE_CONTACT = 101;
            String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.INTERNET};
            //验证是否许可权限
            for (String str : permissions) {
                if (this.checkSelfPermission(str) != PackageManager.PERMISSION_GRANTED) {
                    //申请权限
                    this.requestPermissions(permissions, REQUEST_CODE_CONTACT);
                }
            }
        }
<<<<<<< HEAD

=======
        
>>>>>>> aa41e3931568d839484ad45420af53d27f2b4d68
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
