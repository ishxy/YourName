package ui.bottle;

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

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.shxy.dazuoye.R;

import cz.msebera.android.httpclient.Header;
import global.Global;
import util.HttpRequest;

/**
 * Created by caolu on 2017/5/26.
 */

public class UpBottleInfo extends AppCompatActivity{
    private ImageView back;
    private EditText contentEdit;
    private TextView upTextView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        setContentView(R.layout.ac_upbottleinfo);
        initView();
    }

    private void initView() {
        back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        contentEdit = (EditText) findViewById(R.id.info);
        upTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                up();
            }


        });
    }
    private void up() {
        RequestParams params = new RequestParams();
        params.add("userid", Global.MAIN_USER.getId()+"");
        params.add("secretkey",Global.MAIN_USER.getSecretkey());
        params.add("",contentEdit.getText().toString());
        HttpRequest.post(getApplicationContext(), "pushdriftingbottle", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }

}
