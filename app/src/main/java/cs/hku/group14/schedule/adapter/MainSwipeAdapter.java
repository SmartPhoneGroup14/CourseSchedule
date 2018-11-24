package cs.hku.group14.schedule.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import cs.hku.group14.schedule.R;
import cs.hku.group14.schedule.model.NoteEntity;
import cs.hku.group14.schedule.util.StringUtil;

/**
 * 主菜单适配器
 */

public class MainSwipeAdapter extends BaseAdapter {
    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");
    private List<NoteEntity> mData;
    private Context mContext;

    public MainSwipeAdapter(Context mContext, List<NoteEntity> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (null == convertView) {
            convertView = View.inflate(mContext, R.layout.note_item_main, null);
        }

        NoteEntity note = ((NoteEntity) getItem(position));

        Date date = null;
        try {
            date = new Date(sdf.parse(note.getDate()).getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int weekday = cal.get(Calendar.DAY_OF_WEEK);

        TextView titleView = convertView.findViewById(R.id.title_item);
        TextView contentView = convertView.findViewById(R.id.content_item);
        TextView dateView = convertView.findViewById(R.id.date_item);
        TextView monthView = convertView.findViewById(R.id.month_item);
        TextView dayView = convertView.findViewById(R.id.day_item);
        //设置标题
        titleView.setText(note.getTitle());
        //设置星期、日期
        switch (weekday) {
            case Calendar.MONDAY:
                dateView.setText("Monday");
                break;
            case Calendar.TUESDAY:
                dateView.setText("Tuesday");
                break;
            case Calendar.WEDNESDAY:
                dateView.setText("Wednesday");
                break;
            case Calendar.THURSDAY:
                dateView.setText("Thursday");
                break;
            case Calendar.FRIDAY:
                dateView.setText("Friday");
                break;
            case Calendar.SATURDAY:
                dateView.setText("Saturday");
                break;
            case Calendar.SUNDAY:
                dateView.setText("Sunday");
                break;
            default:
                break;
        }
        monthView.setText(month + 1);
        dayView.setText(day);

        //设置内容
        String content = note.getBody();
        StringBuilder sb = new StringBuilder();
        if (StringUtil.isEmpty(content)) {
            sb.append(" ");
        } else {
            String clearContent = StringUtil.clearHtml(content);
//            contentView.setVisibility(View.VISIBLE);
            if (clearContent.length() < 10) {
                sb.append(clearContent);
            } else {
                sb.append(clearContent.substring(0, 10)).append("...");
            }
        }
        contentView.setText(StringUtil.clearEnter(sb.toString()));

        return convertView;
    }


    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


}
