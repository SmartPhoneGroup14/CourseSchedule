package cs.hku.group14.schedule.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;

import java.util.ArrayList;

import cs.hku.group14.schedule.R;

public class BottomNavigationActivity extends AppCompatActivity implements BottomNavigationBar.OnTabSelectedListener {

    public static final String TAG = "BottomNavigation";

    private BottomNavigationBar bottomNavigationBar;
    private Fragment fragmentCourse;
    private Fragment fragmentGpa;
    private Fragment fragmentExam;
    private Fragment fragmentNote;
    //当前页
    private Fragment currentFragment;
    //底部选中块
    int lastSelectedPosition = 0;

    private ArrayList<String> courseName;
    private String classJson;
    private String examJson;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_bottom_navigation);

        bottomNavigationBar = findViewById(R.id.bottom_navigation_bar);

        initBottomNavigation();
        initFragment();
    }

    private void initBottomNavigation() {
        //要先设计模式后再添加图标！
        //设置按钮模式  MODE_FIXED表示固定   MODE_SHIFTING 表示转移
        bottomNavigationBar.setMode(BottomNavigationBar.MODE_SHIFTING);
        //设置背景风格
        // BACKGROUND_STYLE_STATIC 表示静态的
        //BACKGROUND_STYLE_RIPPLE 表示涟漪的，也就是可以变化的 ，跟随setActiveColor里面的颜色变化
        bottomNavigationBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_RIPPLE);
        //添加并设置图标、图标的颜色和文字
        bottomNavigationBar
                .addItem(new BottomNavigationItem(R.drawable.ic_home_white_24dp, "Course")).setActiveColor(R.color.blue)
                .addItem(new BottomNavigationItem(R.drawable.ic_remind_later_light, "GPA")).setActiveColor(R.color.red)
                .addItem(new BottomNavigationItem(R.drawable.ic_alarm_white_24dp, "Exam")).setActiveColor(R.color.orange)
                .addItem(new BottomNavigationItem(R.drawable.ic_book_white_24dp, "Notes")).setActiveColor(R.color.brown)
                .setFirstSelectedPosition(lastSelectedPosition)
                .initialise();

        bottomNavigationBar.setTabSelectedListener(this);
    }

    private void initFragment() {
        // 从MainActivity获取数据
        Intent intent = this.getIntent();
        courseName = intent.getStringArrayListExtra("CourseName");
//        ArrayList<String> teachers = intent.getStringArrayListExtra("Teachers");
        classJson = intent.getStringExtra("classJsonStr");
        examJson = intent.getStringExtra("examJsonStr");
        username = intent.getStringExtra("username");

        fragmentCourse = new CourseFragment();
        Bundle bundle = new Bundle();
        bundle.putStringArrayList("CourseName", courseName);
        bundle.putString("classJsonStr", classJson);
        fragmentCourse.setArguments(bundle);
        switchFragment(fragmentCourse);

//        fragmentExam = new ExamFragment();
//        Bundle bundle = new Bundle();
//        bundle.putStringArrayList("CourseName", courseName);
//        bundle.putString("examJsonStr", examJson);
//        fragmentExam.setArguments(bundle);
//        switchFragment(fragmentExam);
    }

    //切换fragment
    private void switchFragment(Fragment targetFragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if (targetFragment == null) {
            Log.e(TAG, "switchFragment targetFragment is null !");
            return;
        }
        if (currentFragment != null) {
            Log.i(TAG, "switchFragment 隐藏 fragment : " + currentFragment.getClass());
            fragmentTransaction.hide(currentFragment);
        }
        if (!targetFragment.isAdded()) {
            Log.i(TAG, "添加 fragment : " + targetFragment.getClass());
            fragmentTransaction.add(R.id.frame_layout, targetFragment);
        } else {
            Log.i(TAG, "显示 fragment : " + targetFragment.getClass());
            fragmentTransaction.show(targetFragment);
        }

        currentFragment = targetFragment;
        fragmentTransaction.commit();
    }


    @Override
    public void onTabSelected(int position) {
        Log.i(TAG, "onTabSelected : " + position);

        switch (position) {
            case 0:
                if (fragmentCourse == null) {
                    fragmentCourse = new CourseFragment();
                    Bundle bundle = new Bundle();
                    bundle.putStringArrayList("CourseName", courseName);
                    bundle.putString("classJsonStr", classJson);
                    fragmentCourse.setArguments(bundle);
                }
                switchFragment(fragmentCourse);
                break;
            case 1:
                if (fragmentGpa == null) {
                    fragmentGpa = new GPAFragment();
                }
                switchFragment(fragmentGpa);
                break;
            case 2:
                if (fragmentExam == null) {
                    fragmentExam = new ExamFragment();
                    Bundle bundle = new Bundle();
                    bundle.putStringArrayList("CourseName", courseName);
                    bundle.putString("examJsonStr", examJson);
                    fragmentExam.setArguments(bundle);
                }
                switchFragment(fragmentExam);
                break;
            case 3:
                if (fragmentNote == null) {
                    fragmentNote = new NotesFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("username", username);
                    fragmentNote.setArguments(bundle);
                }
                switchFragment(fragmentNote);
                break;
            default:
                break;
        }
    }

    @Override
    public void onTabUnselected(int position) {
        Log.d(TAG, "onTabUnselected() called with: " + "position = [" + position + "]");
        lastSelectedPosition = position;
    }

    @Override
    public void onTabReselected(int position) {

    }

}