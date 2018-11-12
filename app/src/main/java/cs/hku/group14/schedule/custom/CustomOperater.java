package cs.hku.group14.schedule.custom;

import android.widget.LinearLayout;

import hku.cs.group14.timetableview.operator.SimpleOperater;


/**
 * 自定义的课表业务操作者，以实现列宽设置。
 */
public class CustomOperater extends SimpleOperater {
    private static final String TAG = "CustomOperater";
    private float[] weights;//宽度权重

    public CustomOperater() {
        weights = new float[7];
        for (int i = 0; i < weights.length; i++) {
            weights[i] = 1;
        }
    }

    @Override
    public void applyWidthConfig() {
        super.applyWidthConfig();
        if (weights == null) return;
        for (int i = 0; i < panels.length; i++) {
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, weights[i]);
            panels[i].setLayoutParams(lp);
        }
    }

    public void setWidthWeights(float[] weights) {
        if (weights == null || weights.length < 7) return;
        this.weights = weights;
    }

    public float[] getWeights() {
        return weights;
    }
}
