package cs.hku.group14.schedule.view;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import cs.hku.group14.schedule.R;
import cs.hku.group14.schedule.model.ClassEntity;
import cs.hku.group14.schedule.util.CalendarUtil;
import cs.hku.group14.schedule.util.ClassPraseUtil;
import hku.cs.group14.timetableview.TimetableView;
import hku.cs.group14.timetableview.listener.ISchedule;
import hku.cs.group14.timetableview.listener.IWeekView;
import hku.cs.group14.timetableview.listener.OnSlideBuildAdapter;
import hku.cs.group14.timetableview.model.Schedule;
import hku.cs.group14.timetableview.view.WeekView;

/**
 * 基础功能：
 * 展示所选课程到timetableview中
 * 添加时间事件到日历
 */
public class CourseFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "CourseFragment";
    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

    //控件
    TimetableView mTimetableView;
    WeekView mWeekView;

    Button moreButton;
    LinearLayout layout;
    TextView titleTextView;
    List<ClassEntity> mySubjects;

    //记录切换的周次，不一定是当前周
    int target = -1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i(TAG, " onCreateView");
        View view = inflater.inflate(R.layout.fragment_courses_chedule, container, false);

//        暂时不使用more 按钮
//        moreButton = view.findViewById(R.id.id_more);
//        moreButton.setVisibility(View.GONE);
//        moreButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                showPopmenu();
//            }
//        });

        titleTextView = view.findViewById(R.id.id_title);
        layout = view.findViewById(R.id.id_layout);
        layout.setOnClickListener(this);
        //获取控件
        mWeekView = view.findViewById(R.id.id_weekview);
        mTimetableView = view.findViewById(R.id.id_timetableView);

        initTimetableView();

        return view;
    }

    /**
     * 初始化课程控件--多线程后台更新
     */
    private void initTimetableView() {

        new Thread() {
            @Override
            public void run() {
                try {
                    synchronized (this) {
                        //初始化数据
                        Bundle bundle = getArguments();
                        if (bundle == null) {
                            Log.e(TAG, "bundle is null");
                            return;
                        }
                        ArrayList<String> courseName = bundle.getStringArrayList("CourseName");
                        String classJson = bundle.getString("classJsonStr");

                        if (courseName == null || classJson == null) {
                            Log.e(TAG, "courseName or classJson is null");
                            return;
                        }

                        List<ClassEntity> tmp_mySubjects = ClassPraseUtil.parse(classJson);
                        mySubjects = new ArrayList<>();
                        for (ClassEntity entity : tmp_mySubjects) {
                            if (courseName.contains(entity.getCourse())) {
                                mySubjects.add(entity);
                            } else if (entity.getCourse().equals("Holiday") || entity.getCourse().equals("Reading")) {
                                mySubjects.add(entity);
                            }
                        }

                        //计算当前周次
                        Date begin = begin = sdf.parse("20180903");
                        int differ = differentDaysByMillisecond(begin, new Date());
                        int curWeek = differ / 7 + 1;

                        //设置周次选择属性
                        mWeekView.source(mySubjects)
                                .curWeek(curWeek) //设置当前周
                                .callback(new IWeekView.OnWeekItemClickedListener() {
                                    @Override
                                    public void onWeekClicked(int week) {
                                        int cur = mTimetableView.curWeek();
                                        //更新切换后的日期，从当前周cur->切换的周week
                                        mTimetableView.onDateBuildListener()
                                                .onUpdateDate(cur, week);
                                        mTimetableView.changeWeekOnly(week);
                                    }
                                })
                                .callback(new IWeekView.OnWeekLeftClickedListener() {
                                    @Override
                                    public void onWeekLeftClicked() {
                                        onWeekLeftLayoutClicked();
                                    }
                                })
                                .isShow(false);//设置隐藏，默认显示

                        mTimetableView.source(mySubjects)
                                .curWeek(curWeek)
                                .curTerm("18-19 Semester1")
                                .maxSlideItem(27)   //设置27节课，30分钟一节
                                .monthWidthDp(30)
                                .isShowNotCurWeek(false)
                                //透明度
                                //日期栏0.1f、侧边栏0.1f，周次选择栏0.6f
                                //透明度范围为0->1，0为全透明，1为不透明
                                // .alpha(0.1f, 0.1f, 0.6f)
                                .callback(new ISchedule.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(View v, List<Schedule> scheduleList) {
                                        display(scheduleList);
                                    }
                                })
                                .callback(new ISchedule.OnItemLongClickListener() {
                                    @Override
                                    public void onLongClick(View v, Schedule schedule, int curWeek) {
                                        int day = schedule.getDay();
                                        int start = schedule.getStart();
                                        long beginTime = 1535904000000L + ((curWeek - 1) * 7 * 24 * 3600
                                                + (day - 1) * 24 * 3600 + 9 * 3600 + (start - 1) * 30 * 60) * 1000L;
                                        AddToCalendar(beginTime, "Add Course to Calendar", schedule.getName());
                                    }
                                })
                                .callback(new ISchedule.OnWeekChangedListener() {
                                    @SuppressLint("SetTextI18n")
                                    @Override
                                    public void onWeekChanged(int curWeek) {
//                                        titleTextView.setText("Week " + curWeek);
                                        updateText(titleTextView, "Week " + curWeek);
                                    }
                                });

                        showTime();

                        updateViewOnUiThread();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();

    }


    /**
     * 在UiThread中更新页面
     */
    private void updateViewOnUiThread() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mWeekView.showView();
                mTimetableView.showView();
                mTimetableView.updateSlideView();
            }
        });
    }

    /**
     * 在UiThread中更新页面
     */
    private void updateText(final TextView target, final String text) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                target.setText(text);
            }
        });
    }

    /**
     * 周次选择布局的左侧被点击时回调
     * 对话框修改当前周次
     */
    protected void onWeekLeftLayoutClicked() {
        final String items[] = new String[16];
        int itemCount = mWeekView.itemCount();
        for (int i = 0; i < itemCount; i++) {
            items[i] = "Week " + (i + 1);
        }
        target = -1;
        AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));
        builder.setTitle("Set current week");
        builder.setSingleChoiceItems(items, mTimetableView.curWeek() - 1,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        target = i;
                    }
                });
        builder.setPositiveButton("Set to current week", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (target != -1) {
                    //修正weekView 文本内容
                    mWeekView.curWeek(target + 1).updateView();

                    //切换timeTableView的cur周次且修改为当前周
                    mTimetableView.changeWeekForce(target + 1);
                }

            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.create().show();
    }

    /**
     * 显示内容
     */
    protected void display(List<Schedule> beans) {
        StringBuilder stringBuffer = new StringBuilder();
        for (Schedule bean : beans) {
            if (bean.getName().equals("Holiday") || bean.getName().equals("Reading Week")) {
                stringBuffer.append(bean.getName());
                break;
            } else {
                String startTime = String.valueOf((bean.getStart() - 1) / 2 + 9) + ":"
                        + ((bean.getStart() - 1) % 2 == 0 ? "00" : "30");
                String endTime = String.valueOf((bean.getStart() + bean.getStep() - 1) / 2 + 9) + ":"
                        + ((bean.getStart() + bean.getStep() - 1) % 2 == 0 ? "00" : "30");
                stringBuffer.append(bean.getName()).append("\n")
                        .append(bean.getTeacher()).append(", ").append(bean.getRoom())
                        .append("\nfrom ").append(startTime).append(" to ").append(endTime);
            }
        }
        Toast.makeText(getActivity(), stringBuffer.toString(), Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.id_layout:
                //如果周次选择已经显示了，那么将它隐藏，更新课程、日期
                //否则，显示
                if (mWeekView.isShowing()) {
                    mWeekView.isShow(false);
                    titleTextView.setTextColor(getResources().getColor(R.color.app_course_textcolor_blue));
                    int cur = mTimetableView.curWeek();
                    mTimetableView.onDateBuildListener()
                            .onUpdateDate(cur, cur);
                    mTimetableView.changeWeekOnly(cur);
                } else {
                    mWeekView.isShow(true);
                    titleTextView.setTextColor(getResources().getColor(R.color.app_red));
                }
                break;
        }
    }


    /**
     * 添加课程到日历中
     */
    protected void AddToCalendar(final long begintime, final String title, final String desc) {
        AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));
        builder.setTitle(title);
        builder.setMessage(desc);
        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Boolean result = CalendarUtil.addCalendarEvent(getActivity(), title, desc, begintime);
                Log.i("AddToCalendar", "添加课程到日历,begintime : " + begintime);
                if (result) {
                    Toast.makeText(getActivity(),
                            "Add Course to Calendar Successfully",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(),
                            "Add Course Failed",
                            Toast.LENGTH_SHORT).show();
                }
            }

        });
        builder.setNegativeButton("Cancel", null);
        builder.create().show();
    }

    /**
     * 显示时间
     * 设置侧边栏构建监听，TimeSlideAdapter是控件实现的可显示时间的侧边栏
     */
    protected void showTime() {
        String[] times = new String[]{
                "9:00", "9:30", "10:00", "10:30",
                "11:00", "11:30", "12:00", "12:30",
                "13:00", "13:30", "14:00", "14:30",
                "15:00", "15:30", "16:00", "16:30",
                "17:00", "17:30", "18:00", "18:30",
                "19:00", "19:30", "20:00", "20:30",
                "21:00", "21:30", "22:00"
        };
        OnSlideBuildAdapter listener = (OnSlideBuildAdapter) mTimetableView.onSlideBuildListener();
        listener.setTimes(times)
                .setTimeTextColor(Color.BLACK);
//        mTimetableView.updateSlideView();
    }


    public int differentDaysByMillisecond(Date date1, Date date2) {
        return (int) ((date2.getTime() - date1.getTime()) / (1000 * 3600 * 24));
    }

//    /**
//     * 调整高度
//     */
//    public void updateItemHeight(int height) {
//        ISchedule.OnItemBuildListener listener = mTimetableView.onItemBuildListener();
//    }
}
