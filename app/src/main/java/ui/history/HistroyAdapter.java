package ui.history;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Toast;

import com.shxy.dazuoye.R;

import java.util.List;

/**
 * Created by caolu on 2017/5/12.
 */

public class HistroyAdapter extends BaseAdapter{
    private List<String> mList;
    private Context mContext;

    HistroyAdapter(Context context,List list){
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
        return getViewFirst(position,convertView,parent);
    }

    private View getViewFirst(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(mContext).inflate(R.layout.item_history, null);
        ViewGroup vg = (ViewGroup) convertView;
        View v1 = vg.getChildAt(0);
        View v2 = vg.getChildAt(1);
        v1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "1", Toast.LENGTH_SHORT).show();
            }
        });
        v2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "2", Toast.LENGTH_SHORT).show();
            }
        });

        return convertView;
    }
}
