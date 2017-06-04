package ui.history;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.shxy.dazuoye.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import bean.Diary;
import util.GetDatetime;

/**
 * Created by caolu on 2017/5/12.
 */

public class HistoryAdapter extends BaseAdapter{
    private List<Diary> mList;
    private Context mContext;

    HistoryAdapter(Context context, List list){
        this.mContext = context;
        this.mList = list;
    }


    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(mContext).inflate(R.layout.item_history_new, null);
        ImageView img = (ImageView) convertView.findViewById(R.id.img);
        TextView timeText = (TextView) convertView.findViewById(R.id.time);
        TextView locationText = (TextView) convertView.findViewById(R.id.location);
        Diary d = mList.get(position);
        Picasso.with(mContext)
                .load(d.getContentphote())
                .into(img);
        timeText.setText(GetDatetime.dateToString(d.getContenttime()).substring(0,10));
        locationText.setText(d.getLocation());
        return convertView;
    }
}
