package cs.hku.group14.schedule.custom;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import cs.hku.group14.schedule.model.NoteEntity;

public class DBManager {
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
        database.close();
    }

    /**
     * 查询用户所有note
     */
    public ArrayList<NoteEntity> queryByUsername(String username) {
        ArrayList<NoteEntity> result = new ArrayList<>();
        DBHelper dbHelper = DBHelper.getInstance(mContext);
        SQLiteDatabase database = dbHelper.getReadableDatabase();

        Cursor cursor = database.query("notes", new String[]{"id", "username", "title", "body", "date"}, "username=?",
                new String[]{"1"}, null, null, null);

        while (cursor.moveToNext()) {
            NoteEntity tmp = new NoteEntity();
            tmp.setId(cursor.getString(0));
            tmp.setUsername(cursor.getString(1));
            tmp.setTitle(cursor.getString(2));
            tmp.setBody(cursor.getString(3));
            tmp.setDate(cursor.getString(4));

            result.add(tmp);
        }

        cursor.close();

        return result;
    }
}
