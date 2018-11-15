package hku.cs.group14.timetableview.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import cs.hku.group14.timetableview.R;
import hku.cs.group14.timetableview.model.Schedule;
import hku.cs.group14.timetableview.model.ScheduleEnable;
import hku.cs.group14.timetableview.model.ScheduleSupport;
import hku.cs.group14.timetableview.utils.ScreenUtils;

/**
 * 周次选择栏的每项自定义View,表示某周的有课情况.
 * 绘制6*6的矩阵
 * 6列表示：周一到周六
 * 6行表示：9点半-12点半，13点到22点中的（将时间按照30min分为1节，共27节，具体逻辑见代码）
 * 有课的地方用亮色的圆点，没课的地方用暗色的圆点
 */

public class PerWeekView extends View {

    private static final String TAG = "PerWeekView";

    //控件宽度
    private int width;

    //圆点半径
    private int radius;

    //亮色画笔
    private Paint lightPaint;

    //暗色画笔
    private Paint grayPaint;

    //亮色
    private int lightColor;

    //暗色
    private int grayColor;

    //数据源
    private List<Schedule> dataSource;

    /**
     * 获取亮色的画笔
     *
     * @return
     */
    public Paint getLightPaint() {
        return lightPaint;
    }

    /**
     * 获取暗色的画笔
     *
     * @return
     */
    public Paint getGrayPaint() {
        return grayPaint;
    }

    /**
     * 设置亮色
     *
     * @param lightColor 亮色
     * @return
     */
    public PerWeekView setLightColor(int lightColor) {
        this.lightColor = lightColor;
        invalidate();
        return this;
    }

    /**
     * 设置暗色
     *
     * @param grayColor 暗色
     * @return
     */
    public PerWeekView setGrayColor(int grayColor) {
        this.grayColor = grayColor;
        invalidate();
        return this;
    }

    /**
     * 获取数据源
     *
     * @return
     */
    public List<Schedule> getDataSource() {
        if (dataSource == null) dataSource = new ArrayList<>();
        return dataSource;
    }

    /**
     * 设置数据源
     *
     * @param list
     * @param curWeek
     * @return
     */
    public PerWeekView setSource(List<? extends ScheduleEnable> list, int curWeek) {
        if (list == null) return this;
        setData(ScheduleSupport.transform(list), curWeek);
        return this;
    }

    /**
     * 设置数据源
     *
     * @param list
     * @param curWeek
     * @return
     */
    public PerWeekView setData(List<Schedule> list, int curWeek) {
        if (list == null) return this;
        getDataSource().clear();
        for (int i = 0; i < list.size(); i++) {
            Schedule schedule = list.get(i);
            if (ScheduleSupport.isThisWeek(schedule, curWeek)
                    && !schedule.getName().equals("Holiday")
                    && !schedule.getName().equals("Reading Week")
                    && schedule.getDay() <= 6) {
                getDataSource().add(schedule);
            }
        }
        invalidate();
        return this;
    }

    /**
     * 设置半径Px
     *
     * @param radiusPx 半径
     * @return
     */
    public PerWeekView setRadiusInPx(int radiusPx) {
        this.radius = radius;
        return this;
    }

    /**
     * 设置半径Dp
     *
     * @param radiusDp 半径
     * @return
     */
    public PerWeekView setRadiusInDp(int radiusDp) {
        setRadiusInPx(ScreenUtils.dip2px(getContext(), radiusDp));
        return this;
    }

    public PerWeekView(Context context) {
        this(context, null);
    }

    public PerWeekView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttr(attrs);

        lightPaint = new Paint();
        lightPaint.setColor(lightColor);
        lightPaint.setAntiAlias(true);
        lightPaint.setStyle(Paint.Style.FILL);

        grayPaint = new Paint();
        grayPaint.setColor(grayColor);
        grayPaint.setAntiAlias(true);
        grayPaint.setStyle(Paint.Style.FILL);
    }

    private void initAttr(AttributeSet attrs) {
        int defRadius = ScreenUtils.dip2px(getContext(), 2);
        TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.PerWeekView);
        grayColor = ta.getColor(R.styleable.PerWeekView_gray_color, Color.rgb(207, 219, 219));
        lightColor = ta.getColor(R.styleable.PerWeekView_light_color, Color.parseColor("#3FCAB8"));
        radius = (int) ta.getDimension(R.styleable.PerWeekView_radius, defRadius);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int mar = (width - 10 * radius) / 6;
        lightPaint.setColor(lightColor);
        grayPaint.setColor(grayColor);

        int[][] tmp = getArray();

        //绘制点
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                if (tmp[i][j] == 0) {
                    drawPoint(canvas, mar + radius + (mar + 2 * radius) * j, mar + radius + (mar + 2 * radius) * i, radius, grayPaint);
                } else {
                    drawPoint(canvas, mar + radius + (mar + 2 * radius) * j, mar + radius + (mar + 2 * radius) * i, radius, lightPaint);
                }
            }
        }
    }

    /**
     * 根据此数据源分析出一个二维数组.
     *
     * @return
     */
    public int[][] getArray() {
        int[][] arr = new int[27][6];
        int[][] divide_array = new int[12][6];
        int[][] tmp = new int[6][6];

        // 初始化数组
        for (int i = 0; i < 27; i++) {
            for (int j = 0; j < 6; j++) {
                arr[i][j] = 0;
            }
        }

        // 标记上课的位置
        // 遍历课程集合，将在某课程的上课区间的位置都标记上
        int start, end;
        for (int i = 0; i < getDataSource().size(); i++) {
            Schedule schedule = getDataSource().get(i);
            start = schedule.getStart();
            end = schedule.getStart() + schedule.getStep() - 1;
            if (end > 27) end = 27;

            //先标记区间的所有位置
            for (int m = start; m <= end; m++) {
                arr[m - 1][schedule.getDay() - 1] = 1;
            }
        }

        // 合并分组标记
        // 用到了27小节的数据来标记
        // 27小节被分为了6组分别来表示6行的上课状态
        // 9:30-12:30,13:00-22:00每1小时为一组
        // 即27小节的[1,6],[8-26]每2个为一组,共有12小组
        // 每个分组中只要有一个有课，那么该组对外的状态应该为有课
        int t = 0;
        for (int i = 1; i < 26; i += 2) {
            if (i == 7) {
                i = 6;
                continue;
            }
            for (int j = 0; j < 6; j++) {
                if (arr[i][j] == 0 && arr[i + 1][j] == 0) {
                    divide_array[t][j] = 0;
                } else {
                    divide_array[t][j] = 1;
                }
            }
            t++;
        }

        //再对12小组（行）化为6组（行）
        t = 0;
        for (int i = 0; i < 12; i += 2) {
            for (int j = 0; j < 6; j++) {
                if (divide_array[i][j] == 0 && divide_array[i + 1][j] == 0) {
                    tmp[t][j] = 0;
                } else {
                    tmp[t][j] = 1;
                }
            }
            t++;
        }

        return tmp;
    }

    /**
     * 画点
     *
     * @param canvas
     * @param x      圆心x
     * @param y      圆心y
     * @param radius 半径
     * @param p      画笔
     */
    public void drawPoint(Canvas canvas, int x, int y, int radius, Paint p) {
        canvas.drawCircle(x, y, radius, p);
    }
}
