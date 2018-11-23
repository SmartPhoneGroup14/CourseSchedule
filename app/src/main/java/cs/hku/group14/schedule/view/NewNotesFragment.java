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
import android.widget.LinearLayout;

import cs.hku.group14.schedule.R;

public class NewNotesFragment extends Fragment {
    private static final String TAG = "NewNotes";

    //新建的文件夹
    private String currentFolderName;

    //(false 新建模式   true 编辑模式)
    private boolean model;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i(TAG, " onCreateView");
        View view = inflater.inflate(R.layout.fragment_new_note, container, false);

        //是否带有文件夹名
        Bundle bundle = new Bundle();
        currentFolderName = bundle.getString("currentFolderName");

        if (currentFolderName == null) {//编辑模式
            model = true;//更改状态
//            currentFolderName = edit_Note.getFolderName();
        } else {
            model = false;
        }

        init_Toolbar(view);

        return view;
    }


    private void init_Toolbar(View view) {

        Toolbar toolbar = view.findViewById(R.id.toolbar);

        toolbar.setNavigationIcon(R.mipmap.icon_backward);//设置取消图标
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //退出fragment
                ((NotesFragment) getParentFragment()).returnToNoteList();
            }
        });

        toolbar.inflateMenu(R.menu.menu_create);//设置右上角的填充菜单

        if (model) {//编辑模式
            toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {

                    ((NotesFragment) getParentFragment()).returnToNoteList();
                    return false;
                }
            });


        } else {//新建模式
            toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {

                    ((NotesFragment) getParentFragment()).returnToNoteList();
                    return false;
                }
            });

        }


    }
}
