package cs.hku.group14.schedule.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import cs.hku.group14.schedule.R;
import cs.hku.group14.schedule.model.ExamEntity;
import cs.hku.group14.schedule.util.ClassPraseUtil;

public class GPAFragment extends Fragment {
    private static final String TAG = "NotesFragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i(TAG, " onCreateView");
        View view = inflater.inflate(R.layout.fragment_calculator, container, false);

        initCardView(view);

        return view;
    }


    private void initCardView(View view) {
        LinearLayout cardViewLayout = view.findViewById(R.id.calculator_linear_layout);

        LayoutInflater layoutInflater = getLayoutInflater();

        Bundle bundle = getArguments();
        if (bundle == null) {
            Log.e(TAG, "bundle is null");
            return;
        }

        ArrayList<String> tmpCourseName = bundle.getStringArrayList("CourseName");
        ArrayList<String> courseNames = new ArrayList<>();
        String examJson = bundle.getString("examJsonStr");

        if (tmpCourseName == null) {
            Log.e(TAG, "courseName is null");
            return;
        }

        // 处理下CourseName中的AB班，去掉AB
        for (String course : tmpCourseName) {
            if (course.endsWith("A") || course.endsWith("B")) {
                courseNames.add(course.substring(0, course.length() - 1));
            } else {
                courseNames.add(course);
            }
        }

        // 解析exam 信息字符串
        List<ExamEntity> examEntities = ClassPraseUtil.parseExam(examJson);

        for (ExamEntity element : examEntities) {
            if (courseNames.contains(element.getCourse())) {
                View cardView = layoutInflater.inflate(R.layout.cardview_grade, null);

                TextView courseView = cardView.findViewById(R.id.card_course_grade);

                courseView.setText(element.getCourse()+" "+element.getDescription());

                addCardView(cardView, cardViewLayout);
            }
        }
    }

    private void addCardView(final View cardView, final LinearLayout layout) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (cardView.getParent() != null) {
                    Log.i(TAG, "cardview getParent is not null, removeView ");
                    ((ViewGroup) cardView.getParent()).removeView(cardView);
                }
                layout.addView(cardView);
            }
        });
    }
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
//        Log.i(TAG, TAG + " onHiddenChanged hidden : " + hidden);
    }
}
