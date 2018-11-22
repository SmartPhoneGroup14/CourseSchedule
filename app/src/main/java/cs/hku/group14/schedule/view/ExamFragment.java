package cs.hku.group14.schedule.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import cs.hku.group14.schedule.R;
import cs.hku.group14.schedule.model.ExamEntity;
import cs.hku.group14.schedule.util.ClassPraseUtil;

public class ExamFragment extends Fragment {
    private static final String TAG = "ExamFragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i(TAG, " onCreateView");
        View view = inflater.inflate(R.layout.fragment_exam, container, false);

        initCardView(view);
        return view;
    }

    private void initCardView(View view) {
        LinearLayout cardViewLayout = view.findViewById(R.id.exam_linear_layout);

        LayoutInflater layoutInflater = getLayoutInflater();

        Bundle bundle = getArguments();
        if (bundle == null) {
            Log.e(TAG, "bundle is null");
            return;
        }

        ArrayList<String> tmpCourseName = bundle.getStringArrayList("CourseName");
        ArrayList<String> courseName = new ArrayList<>();
        String examJson = bundle.getString("examJsonStr");

        if (tmpCourseName == null || examJson == null) {
            Log.e(TAG, "courseName | examJson is null");
            return;
        }

        // 处理下CourseName中的AB班，去掉AB
        for (String course : tmpCourseName) {
            if (course.endsWith("A") || course.endsWith("B")) {
                courseName.add(course.substring(0, course.length() - 1));
            } else {
                courseName.add(course);
            }
        }

        // 解析exam 信息字符串
        List<ExamEntity> examEntities = ClassPraseUtil.parseExam(examJson);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

        for (ExamEntity element : examEntities) {
            if (courseName.contains(element.getCourse())) {
                View cardView = layoutInflater.inflate(R.layout.cardview_news, null);

                TextView courseView = cardView.findViewById(R.id.card_course);
                TextView dateView = cardView.findViewById(R.id.card_date);
                TextView venueView = cardView.findViewById(R.id.card_venue);
                TextView remarkView = cardView.findViewById(R.id.card_remark);

                setText(courseView, element.getCourse() + "\n" + element.getDescription());
                setText(dateView, "Date : " + element.getDate());
                setText(venueView, "Venue : " + element.getVenue());

                int comma = element.getRemark().indexOf(",");
                String dateStr = element.getRemark().substring(0, comma);
                String note = element.getRemark().substring(comma + 1);
                long differ = 0;
                try {
                    differ = ((sdf.parse(dateStr).getTime() - System.currentTimeMillis()) / (24 * 60 * 60 * 1000));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                setText(remarkView, "Remain " + differ + " days. \nNote:" + note);

                addCardView(cardView, cardViewLayout);
            }
        }
    }

//    private void setBitmap(final ImageView imageView, final Bitmap bitmap) {
//        getActivity().runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                imageView.setImageBitmap(bitmap);
//            }
//        });
//    }

    private void setText(final TextView text, final String value) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (value.equals("None")) {
                    text.setVisibility(View.GONE);
                } else {
                    text.setText(value);
                }
            }
        });
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


//    private void showContentView() {
//        getActivity().runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                mLoadingView.showContentView();
//            }
//        });
//    }
}
