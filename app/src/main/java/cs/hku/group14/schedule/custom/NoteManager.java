package cs.hku.group14.schedule.custom;

import android.content.Context;

import java.util.List;

import cs.hku.group14.schedule.model.NoteEntity;

public class NoteManager {

    private Context mContext;

    //数据库管理类
    private DBManager dbManager;

    public NoteManager(Context context) {
        this.mContext = context;
        dbManager = new DBManager(mContext);
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
        dbManager.deleteNote(noteEntity);
    }

    //查询用户note list
    public List<NoteEntity> queryNoteList(String username) {
        return dbManager.queryByUsername(username);
    }

}
