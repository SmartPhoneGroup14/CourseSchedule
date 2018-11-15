package cs.hku.group14.test;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.junit.Test;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cs.hku.group14.schedule.model.ClassEntity;
import cs.hku.group14.schedule.model.SubjectRepertory;

import static org.junit.Assert.assertEquals;

public class ClassPraseTest {
    @Test
    public void test() throws Exception {
        Gson gson = new Gson();

        ClassEntity tmp = new ClassEntity();
        tmp.setTerm("18-19 Semester1");
        tmp.setCourse("COMP7103A");
        tmp.setName("Data mining");
        tmp.setTeacher("Prof. Ben Kao");
        tmp.setRoom("TT-403");
        tmp.setWeekList(SubjectRepertory.getWeekList("1-4,6,8-12"));
        tmp.setColorRandom(2);
        tmp.setDay(1);
        tmp.setStart(0);
        tmp.setStep(3);

        List<ClassEntity> array = new ArrayList<>();
        array.add(tmp);

        String jsonArray = gson.toJson(array);
        System.out.println("jsonArray : " + jsonArray);

        List<ClassEntity> contacts;
        Type listType = new TypeToken<List<ClassEntity>>() {
        }.getType();

        contacts = new Gson().fromJson(jsonArray, listType);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Date begin = sdf.parse("20180903");

        long beginTime = sdf.parse("20180903").getTime();
        System.out.println("begin : " + begin);
        System.out.println("beginTime : " + beginTime);

        int differ = differentDaysByMillisecond(begin, new Date());
        System.out.println("differ : " + differ);


    }

    public static int differentDaysByMillisecond(Date date1, Date date2) {
        int days = (int) ((date2.getTime() - date1.getTime()) / (1000 * 3600 * 24));
        return days;
    }

}
