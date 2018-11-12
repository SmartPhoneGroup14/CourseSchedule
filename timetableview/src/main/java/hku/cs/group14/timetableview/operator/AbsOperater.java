package hku.cs.group14.timetableview.operator;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import hku.cs.group14.timetableview.TimetableView;


/**
 * 抽象的业务逻辑
 */
public abstract class AbsOperater {
    public void init(Context context, AttributeSet attrs, TimetableView view){};

    public void showView(){};

    public void updateDateView(){};

    public void updateSlideView(){};

    public void changeWeek(int week, boolean isCurWeek){};

    public LinearLayout getFlagLayout(){return null;};

    public LinearLayout getDateLayout(){return null;};

    public void setWeekendsVisiable(boolean isShow){};
}
