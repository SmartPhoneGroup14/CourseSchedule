package hku.cs.group14.timetableview.listener;

import android.graphics.drawable.GradientDrawable;
import android.text.TextUtils;
import android.widget.FrameLayout;
import android.widget.TextView;

import hku.cs.group14.timetableview.model.Schedule;


/**
 * Item构建监听器的默认实现.
 */

public class OnItemBuildAdapter implements ISchedule.OnItemBuildListener {
    @Override
    public String getItemText(Schedule schedule, boolean isThisWeek) {
        if (schedule == null || TextUtils.isEmpty(schedule.getName())) return "unknown";
        if (schedule.getRoom() == null) {
//            if (!isThisWeek)
//                return "[Non this week]" + schedule.getName();
            return schedule.getName();
        }

        String name = schedule.getName();
        if (name.equals("Analysis and design of enterprise applications in UML")) {
            name = "UML";
        }

        String r = name + " @" + schedule.getRoom();
//        if (!isThisWeek) {
//            r = "[非本周]" + r;
//        }
        if (name.equals("Holiday") || name.equals("Reading Week")) {
            r = name;
        }
        return r;
    }

    @Override
    public void onItemUpdate(FrameLayout layout, TextView textView, TextView countTextView, Schedule schedule, GradientDrawable gd) {
    }
}
