package ui.bottle;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.shxy.dazuoye.R;

import bean.BaseBean;
import cz.msebera.android.httpclient.Header;
import global.Global;
import util.HttpRequest;

/**
 * Created by caolu on 2017/5/26.
 */

public class UpBottleInfo extends AppCompatActivity {
    private TextView back;
    private EditText contentEdit;
    private TextView upTextView;
    private Integer targetId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        targetId = bundle.getInt("id", -1);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        setContentView(R.layout.ac_upbottleinfo);
        initView();
    }

    private void initView() {
        back = (TextView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        contentEdit = (EditText) findViewById(R.id.info);
        upTextView = (TextView) findViewById(R.id.up);
        upTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                up();
            }


        });
    }

    private void up() {
        if (targetId == -1)
            return;

        RequestParams params = new RequestParams();
        params.add("userid", Global.MAIN_USER.getId() + "");
        params.add("secretkey", Global.MAIN_USER.getSecretkey());
        params.add("bottlecontent", contentEdit.getText().toString());
        params.add("receiveuserid", targetId + "");
        HttpRequest.post(getApplicationContext(), "pushdriftingbottle", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Gson gson = new Gson();
                BaseBean bean = gson.fromJson(new String(responseBody), BaseBean.class);
                Toast.makeText(getApplicationContext(),bean.getMsg(),Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(getApplicationContext(),new String(responseBody),Toast.LENGTH_SHORT).show();
            }
        });
    }

}
