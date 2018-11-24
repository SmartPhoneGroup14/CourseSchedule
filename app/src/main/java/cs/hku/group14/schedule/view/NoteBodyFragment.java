package cs.hku.group14.schedule.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

import java.util.List;

import cs.hku.group14.schedule.R;
import cs.hku.group14.schedule.adapter.MainSwipeAdapter;
import cs.hku.group14.schedule.custom.NoteManager;
import cs.hku.group14.schedule.custom.NoteMenuCreator;
import cs.hku.group14.schedule.model.NoteEntity;

public class NoteBodyFragment extends Fragment {
    private static final String TAG = "NoteBodyFragment";

    private LinearLayout nothingView;
    private NoteListView mListView;

    private FloatingActionsMenu menu;

    //Note管理类
    private NoteManager noteManager;
    //当前noteList 列表
    private List<NoteEntity> noteList;

    //当前用户名
    private String username;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i(TAG, " onCreateView");
        View view = inflater.inflate(R.layout.fragment_note_body, container, false);

        nothingView = view.findViewById(R.id.nothing_background);
        mListView = view.findViewById(R.id.list_view);

        Bundle bundle = getArguments();
        username = bundle.getString("username");

        menu = view.findViewById(R.id.action_menu);
        menu.bringToFront();

        initNote();
        fab_setting(view);

        return view;
    }

    //查询账号已有的笔记
    private void initNote() {
        new Thread() {
            @Override
            public void run() {
                try {
                    synchronized (this) {
                        noteManager = new NoteManager(getActivity());
                        noteList = noteManager.queryNoteList(username);
                        if (noteList == null || noteList.size() <= 0) {
                            Log.i(TAG, "initNote , notelist is null");
                        } else {
                            //隐藏nothing 背景
                            updateNothingView(nothingView, View.GONE);

                            //设置适配器
                            MainSwipeAdapter adapter = new MainSwipeAdapter(getActivity(), noteList);

                            //noteManager = new NoteManager(getActivity(), adapter);

                            NoteMenuCreator mainCreator = new NoteMenuCreator(getActivity());

                            //设置noteListView 的基本信息
                            mListView.setMenuCreator(mainCreator);
                            mListView.setSwipeDirection(SwipeMenuListView.DIRECTION_LEFT);
                            mListView.setAdapter(adapter);

                            //设置noteListView 事件监听
                            //点击监听--打开/编辑Note
                            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                    final NoteEntity select_item = noteList.get(position);

                                    ((NotesFragment) getParentFragment()).editNote(select_item);
                                }
                            });

                            mListView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {

                                @Override
                                public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {//具体实现

                                    switch (index) {
                                        //delete
                                        case 0:
                                            final NoteEntity select_item = noteList.get(position);
                                            noteList.remove(select_item);
                                            noteManager.deleteNote(select_item);
                                            updateListView();
                                        default:
                                            break;
                                    }
                                    return true;
                                }
                            });

                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    //更新nothingView
    private void updateNothingView(final LinearLayout target, final int visible) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                target.setVisibility(visible);
            }
        });
    }

    //更新NoteListView
    private void updateNoteListView(final NoteListView target, final int visible) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                target.setVisibility(visible);
            }
        });
    }

    //更新NoteList 显示
    private void updateListView() {
        int number = noteList != null ? noteList.size() : 0;

        if (number == 0) {
            //list 数量归零了,显示nothingView.
            updateNothingView(nothingView, View.VISIBLE);

            updateNoteListView(mListView, View.GONE);

        } else {
            //隐藏nothingView
            updateNothingView(nothingView, View.GONE);

            updateNoteListView(mListView, View.VISIBLE);
        }
    }

    //设置fab按钮
    private void fab_setting(View view) {
        FloatingActionButton fab = view.findViewById(R.id.action_note);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG,"点击fab按钮");
                ((NotesFragment) getParentFragment()).addNote();
            }
        });
    }


}
