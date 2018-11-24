package cs.hku.group14.schedule.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

import cs.hku.group14.schedule.R;
import cs.hku.group14.schedule.custom.DBManager;
import cs.hku.group14.schedule.custom.NoteManager;
import cs.hku.group14.schedule.model.NoteEntity;

public class NewNotesFragment extends Fragment {
    private static final String TAG = "NewNotes";

    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");

    //false 新建模式   true 编辑模式
    private boolean edit;

    private String username;
    private String title;
    private String body;
    private String date;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i(TAG, " onCreateView");
        View view = inflater.inflate(R.layout.fragment_new_note, container, false);

        //获取NotesFragment传的参数
        Bundle bundle = getArguments();
        edit = bundle.getBoolean("edit");
        username = bundle.getString("username");
        if (edit) {
            title = bundle.getString("title");
            body = bundle.getString("body");
            date = bundle.getString("date");
        } else {
            date = sdf.format(new Date(System.currentTimeMillis()));
        }

        initToolbar(view);
        initView(view);

        return view;
    }


    private void initToolbar(View view) {

        Toolbar toolbar = view.findViewById(R.id.toolbar);

        //设置取消图标
        toolbar.setNavigationIcon(R.mipmap.icon_backward);
        //取消按钮事件监听
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //退出fragment
                ((NotesFragment) getParentFragment()).returnToNoteList();
            }
        });

        //设置右上角的填充菜单
        toolbar.inflateMenu(R.menu.menu_create);

        if (edit) {
            //编辑模式
            toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    EditText title = getView().findViewById(R.id.title_note);
                    EditText body = getView().findViewById(R.id.content_note);
                    date = sdf.format(new Date(System.currentTimeMillis()));
                    NoteEntity tmp = new NoteEntity(username, title.getText().toString(), body.getText().toString(), date);

                    NoteManager noteManager = new NoteManager(getActivity());
                    noteManager.updateNote(tmp);

                    ((NotesFragment) getParentFragment()).returnToNoteList();
                    Log.i(TAG,"更新 note : " + title);
                    return false;
                }
            });
        } else {
            //新建模式
            toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {

                    EditText title = getView().findViewById(R.id.title_note);
                    EditText body = getView().findViewById(R.id.content_note);
                    date = sdf.format(new Date(System.currentTimeMillis()));
                    NoteEntity tmp = new NoteEntity(username, title.getText().toString(), body.getText().toString(), date);

                    NoteManager noteManager = new NoteManager(getActivity());
                    noteManager.addNote(tmp);

                    ((NotesFragment) getParentFragment()).returnToNoteList();
                    Log.i(TAG,"新增 note : " + title);
                    return false;
                }
            });
        }
    }

    //初始化控件内容
    private void initView(View view) {
        EditText titleView = view.findViewById(R.id.title_note);
        titleView.setText(title);
        EditText bodyView = view.findViewById(R.id.content_note);
        bodyView.setText(body);
        TextView dateView = view.findViewById(R.id.note_date);
        dateView.setText("LastModify : " + date);
    }
}
