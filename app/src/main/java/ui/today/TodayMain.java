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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.shxy.dazuoye.R;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import bean.Diary;
import cz.msebera.android.httpclient.Header;
import global.Global;
import util.GetDatetime;
import util.HttpRequest;

/**
 * Created by caolu on 2017/5/28.
 */

public class TodayMain extends AppCompatActivity {
    private ListView mListView;

    private ArrayList<Diary> mList;
    private MyAdapter mAdapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        setContentView(R.layout.ac_todaymain);
        initView();
        getNetInfo();
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
                startActivityForResult(new Intent(getApplicationContext(), UpToday.class),0);
                if (Global.DEBUG_FINISH)
                    finish();
            }
        });
        ImageView back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 0 && resultCode == 1){
            /**
             * 刷新list
             */
            getNetInfo();
        }
    }

    private void getNetInfo() {
        RequestParams params = new RequestParams();
        params.add("userid", Global.MAIN_USER.getId() + "");
        params.add("secretkey", Global.MAIN_USER.getSecretkey());
        HttpRequest.get(getApplicationContext(), "achieve_diary_events/do_achieve_today_diary_events", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                getJsonInfo(new String(responseBody));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }

    private void getJsonInfo(String s) {
        Log.i("json",s);
        JsonParser parser = new JsonParser();
        JsonObject object = (JsonObject) parser.parse(s);
        String msg = object.get("msg").getAsString();
        Integer state = object.get("statues").getAsInt();
        JsonArray array = object.get("datalist").getAsJsonArray();
        mList.clear();
        for(int i =0;i<array.size();i++){
            Diary d = new Diary();
            JsonObject o = (JsonObject) array.get(i);
            d.setContent(o.get("content").getAsString());
            String temp = o.get("contenttime").getAsString();
            Log.i("ljn",temp);
            Date date = GetDatetime.stringToDate(temp,"yyyy-MM-dd hh:mm:ss");
            Log.i("date",date.toString());
            d.setContenttime(date);
            d.setContentphote(o.get("contentphote").getAsString());
            mList.add(d);
        }
        mAdapter.notifyDataSetChanged();
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
            Picasso.with(TodayMain.this)
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
