package ui.bottle;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.shxy.dazuoye.R;

import java.util.ArrayList;

/**
 * Created by caolu on 2017/5/10.
 */
public class ReceiveBottleAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<String> mInfo;
    ReceiveBottleAdapter(Context context , ArrayList<String> list){
        mInfo = list;
        mContext = context;
    }

    @Override
    public int getCount() {
        return mInfo.size();
    }

    @Override
    public Object getItem(int position) {
        return mInfo.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(mContext).inflate(R.layout.item_receive,null);
        TextView textView = (TextView) convertView.findViewById(R.id.text);
        textView.setText(mInfo.get(position).toString());
        return convertView;
    }
}
