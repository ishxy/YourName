package ui.self;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.shxy.dazuoye.R;
import com.squareup.picasso.Picasso;

import cz.msebera.android.httpclient.Header;
import global.Global;
import util.HttpRequest;

/**
 * Created by caolu on 2017/6/3.
 */

public class SelfMain extends AppCompatActivity{

    private ImageView img ,back;
    private TextView nameText,sexText,ageText;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_selfinfo);

        initView();
        setInfo();
    }

    private void setInfo() {
        RequestParams params = new RequestParams();
        params.add("userid", Global.MAIN_USER.getId()+"");
        params.add("secretkey",Global.MAIN_USER.getSecretkey());
        HttpRequest.get(getApplicationContext(), "getmyinfo", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        parserJson(new String(responseBody));
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(getApplicationContext(),"网络错误",Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void parserJson(String jsonString) {
        JsonParser p = new JsonParser();
        JsonObject o = (JsonObject) p.parse(jsonString);
        Integer state = o.get("statues").getAsInt();
        String msg = o.get("msg").getAsString();
        if(state == 0){
            Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_SHORT).show();
            return;
        }
        Log.i("json",jsonString);
        String name = o.get("username").getAsString();
        Integer age = o.get("age").getAsInt();
        String sex = o.get("sex").getAsString();
        String photoUrl = o.get("photo").getAsString();
        Picasso.with(getApplicationContext())
                .load(photoUrl)
                .into(img);
        nameText.setText(name);
        ageText.setText(age+"");
        sexText.setText(sex);
    }

    private void initView() {
        back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        img = (ImageView) findViewById(R.id.img);
        nameText = (TextView) findViewById(R.id.name);
        sexText = (TextView) findViewById(R.id.sex);
        ageText = (TextView) findViewById(R.id.age);
    }
}
