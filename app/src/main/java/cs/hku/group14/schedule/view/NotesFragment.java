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
import android.widget.LinearLayout;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

import cs.hku.group14.schedule.R;
import cs.hku.group14.schedule.model.NoteEntity;

public class NotesFragment extends Fragment {
    private static final String TAG = "NotesFragment";

    private Fragment noteBodyFragment;
    private String username;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i(TAG, " onCreateView");
        View view = inflater.inflate(R.layout.fragment_note, container, false);

        FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        Bundle bundle = getArguments();
        username = bundle.getString("username");

        noteBodyFragment = new NoteBodyFragment();
        noteBodyFragment.setArguments(bundle);

        fragmentTransaction
                .setCustomAnimations(R.anim.in_from_right, R.anim.out_to_left)
                .add(R.id.note_body_layout, noteBodyFragment)
                .commit();

        return view;
    }


    //新增note fragment
    public void addNote() {
        FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        NewNotesFragment fragment = new NewNotesFragment();
        Bundle bundle = new Bundle();
        bundle.putString("username", username);
        bundle.putBoolean("edit", false);
        fragment.setArguments(bundle);

        fragmentTransaction
                .setCustomAnimations(R.anim.in_from_right, R.anim.out_to_left)
                .replace(R.id.note_body_layout, fragment)
                .commit();
    }

    //编辑note fragment
    public void editNote(NoteEntity noteEntity){
        FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        NewNotesFragment fragment = new NewNotesFragment();

        Bundle bundle = new Bundle();
        bundle.putString("username", username);
        bundle.putBoolean("edit", true);
        bundle.putString("title",noteEntity.getTitle());
        bundle.putString("body",noteEntity.getBody());
        bundle.putString("date",noteEntity.getDate());

        fragment.setArguments(bundle);

        fragmentTransaction
                .setCustomAnimations(R.anim.in_from_right, R.anim.out_to_left)
                .replace(R.id.note_body_layout, fragment)
                .commit();
    }

    //返回notelist
    public void returnToNoteList() {
        FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction
                .setCustomAnimations(R.anim.in_from_left, R.anim.out_to_right)
                .replace(R.id.note_body_layout, noteBodyFragment)
                .commit();
    }

}
