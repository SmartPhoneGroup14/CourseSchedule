package cs.hku.group14.schedule.util;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import cs.hku.group14.schedule.model.ClassEntity;
import cs.hku.group14.schedule.model.ExamEntity;

public class ClassPraseUtil {

    /**
     * 对class json字符串进行解析
     *
     * @param parseString
     * @return
     */
    public static List<ClassEntity> parse(String parseString) {
        Type listType = new TypeToken<List<ClassEntity>>() {
        }.getType();

        List<ClassEntity> courses = new Gson().fromJson(parseString, listType);

        return courses;
    }

    /**
     * exam json字符串进行解析
     */
    public static List<ExamEntity> parseExam(String parseString) {
        Type listType = new TypeToken<List<ExamEntity>>() {
        }.getType();

        List<ExamEntity> courses = new Gson().fromJson(parseString, listType);

        return courses;
    }
}
