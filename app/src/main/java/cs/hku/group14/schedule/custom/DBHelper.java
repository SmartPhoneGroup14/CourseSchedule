package cs.hku.group14.schedule.custom;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {

    // 数据库版本号
    private static Integer Version = 1;

    private Context mContext;

    private static DBHelper dbHelper = null;

    /**
     * 构造函数
     * 在SQLiteOpenHelper的子类中，必须有该构造函数
     */
    private DBHelper(Context context) {
        //数据库名
        super(context, "Notes.db", null, 5);
        this.mContext = context;
    }

    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,
                    int version) {
        // 参数说明
        // context：上下文对象
        // name：数据库名称
        // param：一个可选的游标工厂（通常是 Null）
        // version：当前数据库的版本，值必须是整数并且是递增的状态

        // 必须通过super调用父类的构造函数
        super(context, name, factory, version);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        // 创建数据库 note表
        // 通过execSQL（）执行SQL语句
        String sql = "create table notes(id integer primary key autoincrement, username varchar(128)," +
                "title varchar(255), body blob, date varchar(20))";
        db.execSQL(sql);

        // 注：数据库实际上是没被创建 / 打开的（因该方法还没调用）
        // 直到getWritableDatabase() / getReadableDatabase() 第一次被调用时才会进行创建 / 打开
    }

    /**
     * 复写onUpgrade（）
     * 调用时刻：当数据库升级时则自动调用（即 数据库版本 发生变化时）
     * 作用：更新数据库表结构
     * 注：创建SQLiteOpenHelper子类对象时,必须传入一个version参数，该参数 = 当前数据库版本, 若该版本高于之前版本, 就调用onUpgrade()
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // 参数说明：
        // db ： 数据库
        // oldVersion ： 旧版本数据库
        // newVersion ： 新版本数据库
        Log.i("DBHelper", "onUpgrade oldVersion : " + oldVersion + ", newVersion : " + newVersion);

//        Cursor c = db.rawQuery("SELECT count(*) FROM sqlite_master WHERE type='table' AND name='session'", null);
//
//        if (c.getInt(0)==0) {
//            Log.i("DBHelper","session table is not exist");
//            db.execSQL("create table session(id integer primary key, username varchar(128), pwd varchar(128), time long)");
//        }
//        c.close();
//        switch (newVersion) {
//            case 4:
//                db.execSQL("create table session(id integer primary key, username varchar(128), pwd varchar(128), time long)");
//                break;
//        }
        // 使用 SQL的ALTER语句
//        String sql = "alter table person add sex varchar(8)";
//        db.execSQL(sql);
    }

    /**
     * 单例模式
     */
    public static DBHelper getInstance(Context context) {
        if (dbHelper == null) {
            dbHelper = new DBHelper(context);
        }
        return dbHelper;
    }
}
