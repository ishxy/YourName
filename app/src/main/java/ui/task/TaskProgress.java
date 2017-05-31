package ui.task;

import android.os.Bundle;
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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.shxy.dazuoye.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import bean.Diary;
import bean.DiaryList;
import bean.DiaryListFinal;
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
    private boolean res = false;

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
    }

    private void initView() {
        mListView = (ListView) findViewById(R.id.list);
        mList = new ArrayList(Arrays.asList("第一个任务", "第二个任务", "第三个任务", "第四个任务"));
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

    public boolean getInfo() {

        RequestParams params = new RequestParams();
        params.add("userid", Global.MAIN_USER.getId() + "");
        params.add("secretkey",Global.MAIN_USER.getSecretkey());
        Log.i("userid",Global.MAIN_USER.getId()+"");
        Log.i("secretkey",Global.MAIN_USER.getSecretkey());
        HttpRequest.get(getApplicationContext(), "others_diary/do_push_others_diary_to_user", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Log.i("info",new String(responseBody));
                Gson gson = new Gson();
                DiaryListFinal list = gson.fromJson(new String(responseBody),DiaryListFinal.class);
                if (list == null)
                    res = false;
                else{

                    mList = list.getData().getDatalist();
                    initView();
                }
                res= true;
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(getApplicationContext(),"error",Toast.LENGTH_SHORT).show();
                res = false;
            }
        });
        return res;
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
