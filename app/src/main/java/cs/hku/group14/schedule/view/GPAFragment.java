package cs.hku.group14.schedule.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cs.hku.group14.schedule.R;

public class GPAFragment extends Fragment {
    private static final String TAG = "NotesFragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i(TAG, " onCreateView");
        View view = inflater.inflate(R.layout.activity_calculator, container, false);


        return view;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
//        Log.i(TAG, TAG + " onHiddenChanged hidden : " + hidden);
    }
}
