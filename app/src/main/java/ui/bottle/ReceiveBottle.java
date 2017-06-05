package ui.bottle;

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

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.shxy.dazuoye.R;

import java.util.ArrayList;
import java.util.Arrays;

import bean.DriftingBottle;
import cz.msebera.android.httpclient.Header;
import global.Global;
import util.HttpRequest;

/**
 * Created by caolu on 2017/5/12.
 */

public class ReceiveBottle extends AppCompatActivity {

    private ListView mListView;
    private ArrayList<DriftingBottle> mInfo;
    //private String[] ss = {"后端组制作", "朝鲁 - 常静娜", "何树林", "刘健楠", "郭满荣", "5个不够", "再来一个"};
    private ReceiveBottleAdapter mAdapter;
    private ImageView back;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        setContentView(R.layout.ac_receivebottle);
        initView();
        initInfo();
    }

    private void initInfo() {
        RequestParams params = new RequestParams();
        params.add("userid", Global.MAIN_USER.getId() + "");
        params.add("secretkey", Global.MAIN_USER.getSecretkey());
        params.add("flag",0+"");
        HttpRequest.get(getApplicationContext(), "achieve_received_bottles", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Log.i("json",new String(responseBody));
                jsonInfo(new String(responseBody));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }

    private void jsonInfo(String s) {
        JsonParser parser = new JsonParser();
        JsonObject object = (JsonObject) parser.parse(s);
        Integer state = object.get("statues").getAsInt();
        if (state == 0)
            return;
        JsonArray arr = object.get("datalist").getAsJsonArray();
        for(int i =0;i<arr.size();i++){
            DriftingBottle bottle = new DriftingBottle();
            JsonObject o = arr.get(i).getAsJsonObject();
            bottle.setPostuserid(o.get("postuserid").getAsInt());
            bottle.setReceiveuserid(o.get("receiveuserid").getAsInt());
            bottle.setBottlecontent(o.get("bottlecontent").getAsString());
            bottle.setTime(o.get("time").getAsString());
            bottle.setId(o.get("id").getAsInt());
            mInfo.add(bottle);
        }
        mAdapter.notifyDataSetChanged();
    }

    private void initView() {
        mListView = (ListView) findViewById(R.id.listview);
        mInfo = new ArrayList<>();
        mAdapter = new ReceiveBottleAdapter(this, mInfo);
        mListView.setAdapter(mAdapter);
        back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), ReveiveDetails.class);
                Bundle bundle = new Bundle();
                bundle.putString("info",mInfo.get(position).getBottlecontent());
                bundle.putInt("id",mInfo.get(position).getPostuserid());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }
}
