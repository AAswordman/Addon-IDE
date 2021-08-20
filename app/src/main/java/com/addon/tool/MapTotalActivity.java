package com.addon.tool;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import com.play.common.ldb.MCLdb;
import java.io.File;
import com.litl.leveldb.Iterator;
import com.play.common.ldb.MCKey;
import com.play.common.chunk.ChunkTag;
import bms.helper.tools.LOG;
import java.util.HashMap;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import android.widget.TextView;
import android.text.SpannableStringBuilder;
import java.util.ArrayList;
import java.util.Map;
import java.util.List;
import java.util.Collections;
import java.util.Comparator;
import com.play.common.chunk.ChunkManager;
import com.mithrilmania.blocktopograph.map.Dimension;
import com.mithrilmania.blocktopograph.util.Noise;

public class MapTotalActivity extends AppCompatActivity {

    private String path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.maptotal);
        ((TextView)findViewById(R.id.output)).setText(new SpannableStringBuilder());

        this.path = getIntent().getStringExtra("path");
        findViewById(R.id.maptotalButton).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new Thread(new Runnable(){
                            @Override
                            public void run() {
                                MCLdb ldb=new MCLdb(new File(path + "db"));

                                int chunkNum=0,
                                    subChunkNum=0,
                                    chunkSize=0,
                                    entityNum=0,
                                    entitySize=0;
                                HashMap<String,Integer> entityTimes=new HashMap<>();
                                Iterator it=ldb.getIterator();
                                
                                Pattern entityIdP=Pattern.compile("identifier(?:(?![[a-z]|_|[A-Z]]).)*([[a-z]|_|[A-Z]]*\\:[[a-z]|_|[A-Z]]*)");
                                for (it.seekToFirst();it.isValid();it.next()) {
                                    MCKey k=new MCKey(it.getKey());
                                    byte dataId=k.getDataID();
                                    byte[] v=it.getValue();
                                    //LOG.print("id",new byte[]{dataId});
                                    if (dataId == ChunkTag.TERRAIN.dataID) {
                                        subChunkNum++;
                                        chunkSize+=it.getValue().length;
                                        
                                        
                                    } else if (dataId == ChunkTag.ENTITY.dataID) {
                                        entityNum++;
                                        entitySize+=v.length;
                                        Matcher m=entityIdP.matcher(new String(v));
                                        while(m.find()){
                                            String id=m.group(1);
                                            entityTimes.put(id,entityTimes.getOrDefault(id,0)+1);
                                        }
                                        
                                        
                                    } else if (dataId == ChunkTag.DATA_2D_LEGACY.dataID) {

                                    } else if (dataId == ChunkTag.ChunkVersion.dataID) {
                                        chunkNum++;
                                    }
                                }
                                List<Map.Entry<String, Integer>> list = new ArrayList<Map.Entry<String, Integer>>(entityTimes.entrySet());
                                Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
                                        @Override
                                        public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                                            return o2.getValue().compareTo(o1.getValue());
                                        }
                                    });
                                p("区块分割数量",subChunkNum+"个");
                                p("区块数量",chunkNum+"个");
                                p("区块方块大小",(chunkSize/1024/1024)+"MB");
                                p("实体数量",entityNum+"个");
                                p("实体大小",(entitySize/1024/1024)+"MB");
                                
                                p("实体频次","");
                                for(Map.Entry<String, Integer> m :list){
                                    p(m.getKey(),m.getValue()+"");
                                }
                                /*
                                
                                ChunkManager ckm=new ChunkManager(Dimension.OVERWORLD, ldb);
                                Noise n=new Noise(23452);
                                ckm.setNoise(n);
                                for(int x=0;x<50;x++){
                                    p("noise"+x,""+ckm.getChunks((int)Math.floor(x/16),0).getNoise(x%16,0));
                                }

                                */
                                ldb.close();
                            }

                        }).start();
                }
            });



    }
    public void p(final String i,final String k){
        runOnUiThread(new Runnable(){

                @Override
                public void run() {
                    ((SpannableStringBuilder)((TextView)findViewById(R.id.output)).getText()).append("\n"+i+" : "+k);
                }
            });
        
    }


}
