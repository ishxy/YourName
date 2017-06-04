package ui.history;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.shxy.dazuoye.R;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import bean.Diary;
import ui.today.TodayMain;
import util.GetDatetime;
import view.BaseActivity;

/**
 * Created by caolu on 2017/6/4.
 */

public class HistoryDay extends BaseActivity{

    private MyAdapter mAdapter;
    private ArrayList<Diary> mList;
    private ImageView back;
    private TextView time;
    private ListView mListView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_historyday);
        getInfo();
        initView();
    }

    private void initView() {
        mAdapter = new MyAdapter();
        mListView = (ListView) findViewById(R.id.listview);
        back = (ImageView) findViewById(R.id.back);
        time = (TextView) findViewById(R.id.time);

        //SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        time.setText(GetDatetime.dateToString(mList.get(0).getContenttime()).substring(0,10));
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mListView.setAdapter(mAdapter);
    }

    public void getInfo() {
        Intent intent = getIntent();
        mList = (ArrayList<Diary>) intent.getSerializableExtra("list");
    }

    private class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            TextView info;
            convertView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_today, null);

            ImageView img = (ImageView) convertView.findViewById(R.id.title);
            info = (TextView) convertView.findViewById(R.id.content);
            Picasso.with(HistoryDay.this)
                    .load(mList.get(position).getContentphote())
                    .into(img);
            Date temp = mList.get(position).getContenttime();
            String date = temp.toString().substring(11,19);
            date = date+"   ";

            String c = mList.get(position).getContent();
            SpannableStringBuilder builder = new SpannableStringBuilder(date + c);
            ForegroundColorSpan greenSpan = new ForegroundColorSpan(Color.GREEN);
            ForegroundColorSpan graySpan = new ForegroundColorSpan(Color.parseColor("#757575"));
            builder.setSpan(greenSpan, 0, date.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            builder.setSpan(graySpan, date.length(), date.length() + c.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            info.setText(builder);
            return convertView;
        }
    }
}
