package cs.hku.group14.schedule.model;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据类，加载课程数据
 */
public class SubjectRepertory {

    public static List<Integer> getWeekList(String weeksString) {
        List<Integer> weekList = new ArrayList<>();
        if (weeksString == null || weeksString.length() == 0) return weekList;

        weeksString = weeksString.replaceAll("[^\\d\\-\\,]", "");
        if (weeksString.indexOf(",") != -1) {
            String[] arr = weeksString.split(",");
            for (int i = 0; i < arr.length; i++) {
                weekList.addAll(getWeekList2(arr[i]));
            }
        } else {
            weekList.addAll(getWeekList2(weeksString));
        }
        return weekList;
    }

    public static List<Integer> getWeekList2(String weeksString) {
        List<Integer> weekList = new ArrayList<>();
        int first = -1, end = -1, index = -1;
        if ((index = weeksString.indexOf("-")) != -1) {
            first = Integer.parseInt(weeksString.substring(0, index));
            end = Integer.parseInt(weeksString.substring(index + 1));
        } else {
            first = Integer.parseInt(weeksString);
            end = first;
        }
        for (int i = first; i <= end; i++)
            weekList.add(i);
        return weekList;
    }
}
