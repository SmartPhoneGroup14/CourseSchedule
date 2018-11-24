package cs.hku.group14.schedule.custom;

import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;

import cs.hku.group14.schedule.R;

public class NoteMenuCreator implements SwipeMenuCreator {

    private Context mContext;

    public NoteMenuCreator(Context mContext) {
        this.mContext = mContext;
    }

    /**
     * 初始化且创建菜单
     */
    @Override
    public void create(SwipeMenu menu) {

        SwipeMenuItem deleteItem = new SwipeMenuItem(mContext.getApplicationContext());
        deleteItem.setBackground(R.color.deep_red);
        deleteItem.setWidth(dp2px(55));
        deleteItem.setIcon(R.mipmap.pic_delete);
        // deleteItem.setTitle("删除");

        //  deleteItem.setTitleSize(16);
        deleteItem.setTitleColor(Color.WHITE);
        menu.addMenuItem(deleteItem);

    }

    // 将dp转换为px
    private int dp2px(int value) {
        // 第一个参数为我们待转的数据的单位，此处为 dp（dip）
        // 第二个参数为我们待转的数据的值的大小
        // 第三个参数为此次转换使用的显示量度（Metrics），它提供屏幕显示密度（density）和缩放信息
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value,
                mContext.getResources().getDisplayMetrics());
    }

}
