package cs.hku.group14.schedule.custom;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

import cs.hku.group14.schedule.model.NoteEntity;
import cs.hku.group14.schedule.model.SessionEntity;

public class DBManager {
    private static final String TAG = "DBManager";

    private Context mContext;

    public DBManager(Context context) {
        mContext = context;
    }

    /**
     * 插入一个Note
     */
    public void insertNote(NoteEntity noteEntity) {
        Object[] bindArgs = {noteEntity.getUsername(), noteEntity.getTitle(), noteEntity.getBody(), noteEntity.getDate()};

        DBHelper dbHelper = DBHelper.getInstance(mContext);
        SQLiteDatabase database = dbHelper.getWritableDatabase();

        String sql = "insert into notes (username,title,body,date) values (?,?,?,?)";
        database.execSQL(sql, bindArgs);

        Log.i(TAG, "insert note : " + noteEntity.getDate());

        database.close();
    }

    /**
     * 更新一个Note
     */
    public void updateNote(NoteEntity noteEntity) {
        Object[] bindArgs = {noteEntity.getTitle(), noteEntity.getBody(), noteEntity.getId(), noteEntity.getUsername()};

        DBHelper dbHelper = DBHelper.getInstance(mContext);
        SQLiteDatabase database = dbHelper.getWritableDatabase();

        String sql = "update notes set title=?,body=? where id=? and username=?";
        database.execSQL(sql, bindArgs);

        Log.i(TAG, "update note : " + noteEntity.getId());

        database.close();
    }

    /**
     * 删除一个Note
     */
    public void deleteNote(NoteEntity noteEntity) {
        Object[] bindArgs = {noteEntity.getId(), noteEntity.getUsername()};

        DBHelper dbHelper = DBHelper.getInstance(mContext);
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        String sql = "delete from notes where id=? and username=?";
        database.execSQL(sql, bindArgs);

        Log.i(TAG, "delete note : " + noteEntity.getId());

        database.close();
    }

    /**
     * 查询用户所有note
     */
    public ArrayList<NoteEntity> queryByUsername(String username) {
        ArrayList<NoteEntity> result = new ArrayList<>();
        DBHelper dbHelper = DBHelper.getInstance(mContext);
        SQLiteDatabase database = dbHelper.getReadableDatabase();

        String sql = "select * from notes where username=?";
        Cursor cursor = database.rawQuery(sql, new String[]{username});

        while (cursor.moveToNext()) {
            NoteEntity tmp = new NoteEntity();
            tmp.setId(cursor.getString(0));
            tmp.setUsername(cursor.getString(1));
            tmp.setTitle(cursor.getString(2));
            tmp.setBody(cursor.getString(3));
            tmp.setDate(cursor.getString(4));
            if (tmp != null && tmp.getDate() != null) {
                result.add(tmp);
            } else {
                Log.i(TAG, "query note item is null, id : " + tmp.getId());
            }
        }

        cursor.close();

        return result;
    }


    /**
     * 打开app时，查询session 记住密码内容
    */
    public SessionEntity querySession() {
        DBHelper dbHelper = DBHelper.getInstance(mContext);
        SQLiteDatabase database = dbHelper.getReadableDatabase();

        String sql = "select * from session where id=0";
        Cursor cursor = database.rawQuery(sql, new String[]{});

        SessionEntity entity = new SessionEntity();
        if (cursor.moveToFirst()) {
            entity.setId(cursor.getString(0));
            entity.setUsername(cursor.getString(1));
            entity.setPwd(cursor.getString(2));
            entity.setTime(cursor.getLong(3));

        } else {
            entity = null;
        }

        cursor.close();
        return entity;
    }

    /**
     * 上次登录时选择记住密码，登陆成功后，存储用户信息入session 表
     */
    public void saveSession(SessionEntity entity) {
        DBHelper dbHelper = DBHelper.getInstance(mContext);
        SQLiteDatabase database = dbHelper.getWritableDatabase();

        ContentValues initialValues = new ContentValues();
        initialValues.put("id", 0);
        initialValues.put("username", entity.getUsername());
        initialValues.put("pwd", entity.getPwd());
        initialValues.put("time", entity.getTime());

        int id = (int) database.insertWithOnConflict("session", null, initialValues, SQLiteDatabase.CONFLICT_IGNORE);

        if (id == -1) {
            database.update("session", initialValues, "id=?", new String[]{"0"});
        }

        Log.i(TAG, "save session : " + entity.getUsername());

        database.close();
    }

    /**
     * 当上次登录时，未选择保存密码，清除Session
     */
    public void clearSession() {
        DBHelper dbHelper = DBHelper.getInstance(mContext);
        SQLiteDatabase database = dbHelper.getWritableDatabase();

        int del = database.delete("session", "id=?", new String[]{"0"});

        Log.i(TAG, "clearSession, del : " + del);

        database.close();
    }
}
