package ui.today;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.shxy.dazuoye.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import cz.msebera.android.httpclient.Header;
import global.Global;
import util.HttpRequest;

/**
 * Created by caolu on 2017/5/28.
 */

public class TodayMain extends AppCompatActivity {
    private ListView mListView;

    private ArrayList<String> mList;
    private MyAdapter mAdapter;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        setContentView(R.layout.ac_todaymain);
        initView();

    }

    private void initView() {
        //mToolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView timeText = (TextView) findViewById(R.id.time);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        timeText.setText(df.format(new Date()));
        mListView = (ListView) findViewById(R.id.listview);
        mList = new ArrayList<>();
        mAdapter = new MyAdapter();
        mListView.setAdapter(mAdapter);
        ImageView img = (ImageView) findViewById(R.id.add);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), UpToday.class));
                if (Global.DEBUG_FINISH)
                    finish();
            }
        });
    }

    private class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return 10;
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
           /* if (position == 0) {
                convertView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_today_time, null);
                info = (TextView) convertView.findViewById(R.id.time);
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                info.setText(df.format(new Date()));
            } else {*/
            convertView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_today, null);

            ImageView img = (ImageView) convertView.findViewById(R.id.title);
            info = (TextView) convertView.findViewById(R.id.content);
            img.setImageResource(R.drawable.te);
            String date = "2017/5/28  ";
            String c = "柔情似水，佳期如梦";
            SpannableStringBuilder builder = new SpannableStringBuilder(date + c);
            ForegroundColorSpan greenSpan = new ForegroundColorSpan(Color.GREEN);
            ForegroundColorSpan graySpan = new ForegroundColorSpan(Color.parseColor("#757575"));
            builder.setSpan(greenSpan, 0, date.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            builder.setSpan(graySpan, date.length(), date.length() + c.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            info.setText(builder);
            // }
            return convertView;
        }
    }
}
