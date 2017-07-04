package ui.login;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.shxy.dazuoye.R;

import cz.msebera.android.httpclient.Header;
import util.HttpRequest;
import view.BaseActivity;


public class Register extends BaseActivity implements View.OnClickListener {
    private Button getCheckButton;
    private Button accomplishButton;
    private EditText numberEditText;
    private EditText checkCodeEditText;
    private EditText passwordEditText1;
    private EditText passwordEditText2;
    private EditText nickNameEditText;
    private int checkTime = 0;
    private int time = 60;
    private String phone;
    private boolean isGetCheckCode = false; //判断是否已经获取了验证码

    private static int BEGIN_TIME = 0;

    public interface FRegisterClickListener {
        void onFRegisterClick();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_register);
        initView();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.register_getcheckcode:
                postPhone();
                break;
            case R.id.register_next:
                postAll();
                break;
        }
    }

    private void initView() {
        getCheckButton = (Button) findViewById(R.id.register_getcheckcode);
        accomplishButton = (Button) findViewById(R.id.register_next);
        numberEditText = (EditText) findViewById(R.id.register_phone_number);
        checkCodeEditText = (EditText) findViewById(R.id.register_checkcode);
        passwordEditText1 = (EditText) findViewById(R.id.register_password1);
        passwordEditText2 = (EditText) findViewById(R.id.register_password2);
        nickNameEditText = (EditText) findViewById(R.id.register_nickname);
        getCheckButton.setOnClickListener(this);
        accomplishButton.setOnClickListener(this);
        isGetCheckCode = false;
    }

    private void postPhone() {

        phone = numberEditText.getText().toString();
        if (phone.length() <= 0) {
            Toast.makeText(getApplicationContext(), "手机号不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        if (checkTime == 1) {
            Toast.makeText(getApplicationContext(), "稍后再试", Toast.LENGTH_SHORT).show();
            return;
        }
        RequestParams requestParams = new RequestParams();
        requestParams.add("userphone", phone);
        HttpRequest.post(getApplicationContext(), "reigister1", requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String json = new String(responseBody);
                JsonParser p = new JsonParser();
                JsonObject object = (JsonObject) p.parse(json);
                Integer state = object.get("statues").getAsInt();
                String msg = object.get("msg").getAsString();
                if (state == 1){
                    onGetCheckCodeSuccess();
                }else{
                    Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(getApplicationContext(), new String(responseBody), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void postAll() {
        String checkCode = checkCodeEditText.getText().toString();
        String password1 = passwordEditText1.getText().toString();
        String password2 = passwordEditText2.getText().toString();
        String nickname = nickNameEditText.getText().toString();

        if (!isGetCheckCode) {
            Toast.makeText(getApplicationContext(), "请先获取验证码", Toast.LENGTH_SHORT).show();
            return;
        }

        if (checkCode.length() <= 0 || password1.length() <= 0 || password2.length() <= 0) {
            Toast.makeText(getApplicationContext(), "请填写完毕后再提交", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password1.equals(password2)) {
            Toast.makeText(getApplicationContext(), "两次密码不相同，请重新输入", Toast.LENGTH_SHORT).show();
            passwordEditText1.setText("");
            passwordEditText2.setText("");
            return;
        }

        /*if ((!CheckString.compare(password1, getApplicationContext())) || (!CheckString.compare(nickname, getApplicationContext()))) {
            Toast.makeText(getApplicationContext(), "...", Toast.LENGTH_SHORT).show();
            return;
        }*/
        /*
        String Md5_password = Md5.getMD5(password1);
*/
        RequestParams requestParams = new RequestParams();
        requestParams.put("userphone", phone);
        requestParams.put("checkcode", checkCode);
        requestParams.put("password", password1);
        //requestParams.put("NickName", nickname);

        HttpRequest.post(getApplicationContext(), "reigister2", requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String json = new String(responseBody);
                JsonParser p = new JsonParser();
                JsonObject object = (JsonObject) p.parse(json);
                Integer state = object.get("statues").getAsInt();
                String msg = object.get("msg").getAsString();
                if (state == 1){
                    Toast.makeText(getApplicationContext(),"success",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(getApplicationContext(),new String(responseBody),Toast.LENGTH_SHORT).show();
            }
        });

    }


    //成功获取验证码后
    private void onGetCheckCodeSuccess() {
        checkTime = 1;
        handler.sendEmptyMessageDelayed(BEGIN_TIME, 1000);
        isGetCheckCode = true;
        numberEditText.setFocusable(false);
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == BEGIN_TIME) {
                getCheckButton.setText(time-- + "s后再次获取");
                handler.sendEmptyMessageDelayed(BEGIN_TIME, 1000);
                if (time == 0) {
                    time = 60;
                    checkTime = 0;
                    getCheckButton.setText("获取验证码");
                    handler.removeMessages(BEGIN_TIME);
                }
            }
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeMessages(BEGIN_TIME);
    }
}
