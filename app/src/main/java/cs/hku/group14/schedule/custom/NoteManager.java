package cs.hku.group14.schedule.custom;

import android.content.Context;
import android.util.Log;
import android.widget.BaseAdapter;

import java.util.List;

import cs.hku.group14.schedule.model.NoteEntity;

public class NoteManager {
    private static final String TAG = "NoteManager";
    private Context mContext;

    //数据库管理类
    private DBManager dbManager;

    //适配器
    private BaseAdapter adapter;

    public NoteManager(Context context) {
        this.mContext = context;
        dbManager = new DBManager(mContext);
    }

    public NoteManager(Context context, BaseAdapter adapter) {
        this(context);
        this.adapter = adapter;
    }

    //新增note
    public void addNote(NoteEntity noteEntity) {
        dbManager.insertNote(noteEntity);
    }

    //更新note
    public void updateNote(NoteEntity noteEntity) {
        dbManager.updateNote(noteEntity);
    }

    //删除note
    public void deleteNote(NoteEntity noteEntity) {
        adapter.notifyDataSetChanged();
        dbManager.deleteNote(noteEntity);
    }

    //查询用户note list
    public List<NoteEntity> queryNoteList(String username) {
        return dbManager.queryByUsername(username);
    }

}
