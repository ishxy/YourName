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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.shxy.dazuoye.R;
import com.squareup.picasso.Picasso;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import bean.BaseBean;
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
    private ArrayList<Integer> taskState;
    private Adapter mAdapter;
    private FragmentManager mFragmentManager;
    private Fragment mFragments[];
    private static final int FRAGMENT_NUM = 2;

    private static final int GETINFO_SUCCESS = 1;
    private static final int GETINFO_FAILUEE = -1;

    private Button changeBt, selectBt;
    private Integer flag;
    private Double nowProgress = 0D;
    private static final int SELECT_FRAGMENT = 0, PROGRESS_FRAGMENT = 1;
    private static final int[] FRAGMENT_NAME = {SELECT_FRAGMENT, PROGRESS_FRAGMENT};
    private int randuserid = -1;
    private String choisenday = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        Random random = new Random();
        mList = new ArrayList<>();
        setContentView(R.layout.ac_task);
        getInfo();
        initFragments();
    }

    /**
     * 初始化Fragment以及不需要网络的控件
     */
    private void initFragments() {
        ImageView back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mFragmentManager = getSupportFragmentManager();
        mFragments = new Fragment[FRAGMENT_NUM];
        mFragments[0] = mFragmentManager.findFragmentById(R.id.fragment1);
        mFragments[1] = mFragmentManager.findFragmentById(R.id.fragment2);
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        transaction.hide(mFragments[1]).hide(mFragments[0]).commit();
        SelectFragment f = (SelectFragment) mFragments[0];
        //下面按键的回调
        f.setListener(new SelectFragment.Listener() {
            @Override
            public void ok(int state) {
                switch (state) {
                    case SelectFragment.Listener.STATE_OK:
                        check();
                        break;
                    case SelectFragment.Listener.STATE_NEXT:
                        getInfo();
                        break;
                }
            }
        });
    }

    /**
     * 确认选择该任务
     */
    private void check() {
        if (randuserid == -1)
            return;
        RequestParams params = new RequestParams();
        params.add("userid", Global.MAIN_USER.getId() + "");
        params.add("secretkey", Global.MAIN_USER.getSecretkey());
        params.add("randuserid",randuserid+"");
        params.add("choisenday",choisenday);
        HttpRequest.post(TaskProgress.this, "others_diary/do_choice_others_diary", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                BaseBean b;
                Gson gson = new Gson();
                b = gson.fromJson(new String(responseBody), BaseBean.class);
                if (b != null) {
                    Toast.makeText(getApplicationContext(), b.getMsg(), Toast.LENGTH_SHORT).show();
                    if (b.getStatues() == 1) {
                        showFragment(PROGRESS_FRAGMENT);
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(getApplicationContext(), "网络错误", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 根据网络请求结果初始化界面
     */
    private void initView() {

        if (mAdapter == null)
            mAdapter = new Adapter();
        else
            mAdapter.notifyDataSetChanged();
        if (mListView == null) {
            mListView = (ListView) findViewById(R.id.list);

            mListView.setAdapter(mAdapter);

        }
        Double temp = nowProgress * 100;
        ((ProgressFragment) mFragments[1]).setProgress(temp.intValue());
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

    /**
     * 请求网络资源
     */
    public void getInfo() {

        RequestParams params = new RequestParams();
        params.add("userid", Global.MAIN_USER.getId() + "");
        params.add("secretkey", Global.MAIN_USER.getSecretkey());
        Log.i("userid", Global.MAIN_USER.getId() + "");
        Log.i("secretkey", Global.MAIN_USER.getSecretkey());
        HttpRequest.get(TaskProgress.this, "others_diary/do_push_others_diary_to_user", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Log.i("info", new String(responseBody));
                getJsonInfo(new String(responseBody));
                handler.sendEmptyMessage(GETINFO_SUCCESS);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(TaskProgress.this, "error", Toast.LENGTH_SHORT).show();
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
        if(statues == 0)
            return;
        flag = object.get("flag").getAsInt();
        if (flag == 1) {
            mFragmentManager.beginTransaction().hide(mFragments[0]).show(mFragments[1]).commit();
            nowProgress = object.get("completiondegree").getAsDouble();
        } else {
            mFragmentManager.beginTransaction().hide(mFragments[1]).show(mFragments[0]).commit();
            randuserid = object.get("randuserid").getAsInt();
            choisenday = object.get("choisenday").getAsString();
        }

        if (statues == 0) {
            Log.i("msg", msg);
            return;
        }


        JsonArray datalist = object.getAsJsonArray("datalist");
        taskState = new ArrayList<>();
        if (flag == 1) {
            JsonArray state = object.getAsJsonArray("compareresultlist");
            for (int i = 0; i < state.size(); i++) {
                Log.i("ljnint", state.get(i).getAsInt() + "");
                taskState.add(state.get(i).getAsInt());
            }
        }
        if(mList!=null)
        mList.clear();
        for (int i = 0; i < datalist.size(); i++) {
            Diary diary = new Diary();
            JsonObject each = datalist.get(i).getAsJsonObject();
            diary.setContent(each.get("content").getAsString());
            diary.setContentphote(each.get("contentphote").getAsString());

            mList.add(diary);
        }
    }

    private void showFragment(int which) {
        FragmentTransaction f = mFragmentManager.beginTransaction();
        for (int i = 0; i < FRAGMENT_NAME.length; i++) {
            if (FRAGMENT_NAME[i] == which)
                f.show(mFragments[i]);
            else
                f.hide(mFragments[i]);
        }
        f.commit();
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
            convertView = LayoutInflater.from(TaskProgress.this).inflate(R.layout.item_taskprocess, null);
            //TextView more = (TextView) convertView.findViewById(R.id.more);
            //more.setVisibility(View.VISIBLE);
            TextView t = (TextView) convertView.findViewById(R.id.title);
            ImageView i = (ImageView) convertView.findViewById(R.id.isok);
            ImageView img = (ImageView) convertView.findViewById(R.id.img);

            if (flag == 1) {
                if (taskState.get(position) == 1) {
                    i.setImageResource(R.drawable.ok);
                }
            }
            t.setText(mList.get(position).getContent());
            Picasso.with(TaskProgress.this)
                    .load(mList.get(position).getContentphote())
                    .into(img);
            return convertView;
        }
    }
}
