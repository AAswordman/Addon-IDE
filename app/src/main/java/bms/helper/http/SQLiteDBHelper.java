package bms.helper.http;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.content.Context;

public class SQLiteDBHelper extends SQLiteOpenHelper {
    
    private static final int DB_VERSION = 1;//数据库版本
    private static final String DB_NAME = "more.db";//数据库名称
    public static final String TABLE_NAME = "Orders";//表的名称

    public SQLiteDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    /**
     * 这个方法
     * 1、在第一次打开数据库的时候才会走
     * 2、在清除数据之后再次运行-->打开数据库，这个方法会走
     * 3、没有清除数据，不会走这个方法
     * 4、数据库升级的时候这个方法不会走
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        //初始化数据表，可以再这里面对多个表进行处理
        String sql = "create table if not exists " + TABLE_NAME + " (studentid text primary key, name text, sex text, age integer)";
        db.execSQL(sql);
    }

    /**
     * 数据库升级
     * 1、第一次创建数据库的时候，这个方法不会走
     * 2、清除数据后再次运行(相当于第一次创建)这个方法不会走
     * 3、数据库已经存在，而且版本升高的时候，这个方法才会调用
     *
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "DROP TABLE IF EXISTS " + TABLE_NAME;
        db.execSQL(sql);
        onCreate(db);
    }

    /**
     * 执行数据库的降级操作
     * 1、只有新版本比旧版本低的时候才会执行
     * 2、如果不执行降级操作，会抛出异常
     */
    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onDowngrade(db, oldVersion, newVersion);
    }






}
