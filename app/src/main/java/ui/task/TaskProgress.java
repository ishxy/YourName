package ui.task;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.shxy.dazuoye.R;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import bean.Diary;
import bean.DiaryList;
import bean.DiaryListFinal;
import bean.User;
import cz.msebera.android.httpclient.Header;
import global.Global;
import util.HttpRequest;


public class TaskProgress extends AppCompatActivity {

    private ListView mListView;
    private ArrayList<Diary> mList;
    private Adapter mAdapter;
    private FragmentManager mFragmentManager;
    private Fragment mFragments[];
    private static final int FRAGMENT_NUM = 2;

    private static final int GETINFO_SUCCESS = 1;
    private static final int GETINFO_FAILUEE = -1;

    private Button changeBt,selectBt;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        Random random = new Random();
        setContentView(R.layout.ac_task);
        getInfo();
        initFragments();
    }

    private void initFragments() {
        mFragmentManager = getSupportFragmentManager();
        mFragments = new Fragment[FRAGMENT_NUM];
        mFragments[0] = mFragmentManager.findFragmentById(R.id.fragment1);
        mFragments[1] = mFragmentManager.findFragmentById(R.id.fragment2);
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        transaction.hide(mFragments[1]).show(mFragments[0]).commit();
        SelectFragment f = (SelectFragment) mFragments[0];
        //下面按键的回调
        f.setListener(new SelectFragment.Listener() {
            @Override
            public void ok(int state) {
                switch (state){
                    case SelectFragment.Listener.STATE_OK:
                        break;
                    case SelectFragment.Listener.STATE_NEXT:
                        break;
                }
            }
        });
    }

    private void initView() {
        mListView = (ListView) findViewById(R.id.list);
        mAdapter = new Adapter();
        mListView.setAdapter(mAdapter);

        ImageView back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case GETINFO_SUCCESS:
                    initView();
                    break;
                case GETINFO_FAILUEE:
                    break;
            }
        }
    };

    public void getInfo() {

        RequestParams params = new RequestParams();
        params.add("userid", Global.MAIN_USER.getId() + "");
        params.add("secretkey", Global.MAIN_USER.getSecretkey());
        Log.i("userid", Global.MAIN_USER.getId() + "");
        Log.i("secretkey", Global.MAIN_USER.getSecretkey());
        HttpRequest.get(getApplicationContext(), "others_diary/do_push_others_diary_to_user", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Log.i("info", new String(responseBody));
                /*Gson gson = new Gson();
                DiaryListFinal list = gson.fromJson(new String(responseBody),DiaryListFinal.class);*/
                getJsonInfo(new String(responseBody));
                handler.sendEmptyMessage(GETINFO_SUCCESS);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_SHORT).show();
                handler.sendEmptyMessage(GETINFO_FAILUEE);
            }
        });
    }

    /**
     * 解析获取到的json
     *
     * @param json json字符串
     */
    public void getJsonInfo(String json) {
        if (json == null || "".equals(json))
            return;
        JsonParser parser = new JsonParser();
        JsonObject object = (JsonObject) parser.parse(json);
        Integer statues = object.get("statues").getAsInt();
        String msg = object.get("msg").getAsString();
        mList = new ArrayList<>();
        if (statues == 0) {
            Log.i("msg", msg);
            return;
        }
        JsonObject data = object.getAsJsonObject("data");
        JsonArray datalist = data.getAsJsonArray("datalist");

        for (int i = 0; i < datalist.size(); i++) {
            Diary diary = new Diary();
            JsonObject each = datalist.get(i).getAsJsonObject();
            diary.setContent(each.get("content").getAsString());
            mList.add(diary);
        }
    }

    private class Adapter extends BaseAdapter {

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
            convertView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_taskprocess, null);
            TextView more = (TextView) convertView.findViewById(R.id.more);
            more.setVisibility(View.VISIBLE);
            TextView t = (TextView) convertView.findViewById(R.id.title);
            ImageView i = (ImageView) convertView.findViewById(R.id.isok);
            if (position % 2 == 0)
                i.setImageResource(R.drawable.ok);
            t.setText(mList.get(position).getContent());

            return convertView;
        }
    }
}
