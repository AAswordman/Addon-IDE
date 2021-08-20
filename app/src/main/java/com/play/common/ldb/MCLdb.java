package com.play.common.ldb;
import bms.helper.tools.LOG;
import com.litl.leveldb.DB;
import com.litl.leveldb.Iterator;
import com.mithrilmania.blocktopograph.map.Dimension;
import com.play.common.chunk.ChunkTag;
import java.io.File;
import java.nio.charset.Charset;
import bms.quote.Ldb;

public class MCLdb extends Ldb{
    
    public MCLdb(File f){
        super(f);
    }
    
    
    
    
}
