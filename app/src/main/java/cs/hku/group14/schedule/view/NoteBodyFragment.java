package cs.hku.group14.schedule.view;

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
import android.widget.LinearLayout;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

import cs.hku.group14.schedule.R;

public class NoteBodyFragment extends Fragment {
    private static final String TAG = "NoteBodyFragment";

    private LinearLayout nothingView;
    private FloatingActionsMenu menu;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i(TAG, " onCreateView");
        View view = inflater.inflate(R.layout.fragment_note_body, container, false);

        nothingView = view.findViewById(R.id.nothing_background);
        FloatingActionsMenu menu = view.findViewById(R.id.action_menu);

        fab_setting(view);
        initNote();

        return view;
    }

    //查询账号已有的笔记
    private void initNote() {
        new Thread() {
            @Override
            public void run() {
                try {
                    synchronized (this) {

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    //隐藏nothing背景
    private void hideNothingView() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                nothingView.setVisibility(View.GONE);
            }
        });
    }

    //设置fab按钮
    private void fab_setting(View view) {
        FloatingActionButton fab = view.findViewById(R.id.action_note);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((NotesFragment) getParentFragment()).addNote();
            }
        });
    }
}
