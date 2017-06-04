package ui.history;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.shxy.dazuoye.R;

import java.util.ArrayList;

import bean.Diary;
import cz.msebera.android.httpclient.Header;
import global.Global;
import util.GetDatetime;
import util.HttpRequest;

/**
 * Created by caolu on 2017/5/10.
 */

public class HistoryBill extends AppCompatActivity {

    private ArrayList<Diary> mInfo;
    private ArrayList<ArrayList<Diary>> mAllInfo;
    private ListView mListView;
    private HistoryAdapter mAdapter;
    private ImageView backButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        setContentView(R.layout.ac_historybill);
        initView();
        getNetInfo();
    }

    private void initView() {
        mListView = (ListView) findViewById(R.id.listview);
        mInfo = new ArrayList<>();
        mAllInfo = new ArrayList<>();
        mAdapter = new HistoryAdapter(this, mInfo);
        mListView.setAdapter(mAdapter);
        backButton = (ImageView) findViewById(R.id.back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(HistoryBill.this, HistoryDay.class);
                intent.putExtra("list", mAllInfo.get(position));
                startActivity(intent);
                if (Global.DEBUG_FINISH) {
                    finish();
                }
            }
        });
    }


    public void getNetInfo() {
        RequestParams params = new RequestParams();
        params.add("userid", Global.MAIN_USER.getId() + "");
        params.add("secretkey", Global.MAIN_USER.getSecretkey());
        HttpRequest.get(getApplicationContext(), "achieve_diary_events/do_achieve_history_diary_events", params, new AsyncHttpResponseHandler() {
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
        Log.i("json", s);
        JsonParser parser = new JsonParser();
        JsonObject object = (JsonObject) parser.parse(s);
        String msg = object.get("msg").getAsString();
        Integer state = object.get("statues").getAsInt();
        if (state == 0) {
            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
            return;
        }
        JsonArray array = object.get("datalist").getAsJsonArray();
        for (int i = 0; i < array.size(); i++) {
            //内层list 相当于某一天的日记
            JsonArray arr = array.get(i).getAsJsonArray();
            ArrayList<Diary> small = new ArrayList<>();
            for (int j = 0; j < arr.size(); j++) {
                Diary d = new Diary();
                JsonObject o = arr.get(j).getAsJsonObject();
                d.setContentphote(o.get("contentphote").getAsString());
                d.setContenttime(GetDatetime.stringToDate(o.get("contenttime").getAsString()));
                d.setLocation(o.get("location").getAsString());
                d.setContent(o.get("content").getAsString());
                if (j==0){
                    mInfo.add(d);
                }
                small.add(d);

            }
            mAllInfo.add(small);
        }
        Log.i("mInfo.size()", mInfo.size() + "");
        mAdapter.notifyDataSetChanged();
    }
}
