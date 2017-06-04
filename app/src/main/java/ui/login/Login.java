package ui.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.shxy.dazuoye.R;

import bean.User;
import cz.msebera.android.httpclient.Header;
import global.Global;
import util.HttpRequest;
import view.BaseActivity;


public class Login extends BaseActivity implements View.OnClickListener {


    public static Integer RESULT_FINISH = -1;
    public static Integer RESULT_OK = 1;
    private EditText usernameEditText, passwordEditText;
   // private TextView registerText, loseText;
    private ImageView img;
    private Button loginButton;
    private TextView toRegister;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_login);
        initView();
    }

    private void initView() {
        usernameEditText = (EditText) findViewById(R.id.login_zhanghao);
        passwordEditText = (EditText) findViewById(R.id.login_password);
        //registerText = (TextView) findViewById(R.id.login_to_register);
        //loseText = (TextView) findViewById(R.id.login_to_losspass);
        loginButton = (Button) findViewById(R.id.login_button);
        img = (ImageView) findViewById(R.id.userimage);
        loginButton.setOnClickListener(this);
        toRegister = (TextView) findViewById(R.id.register);
        toRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_button:
                final String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                RequestParams params = new RequestParams();
                params.put("userphone",username);
                params.put("password",password);
                HttpRequest.post(getApplication(), "login", params, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        Gson gson = new Gson();
                        Log.i("liujiannanshabi",new String(responseBody));

                        User user = gson.fromJson(new String(responseBody), User.class);
                        if (user.getStatues() == 1){
                            setResult(RESULT_OK);

                            SharedPreferences preferences = getSharedPreferences("firstinfo",MODE_PRIVATE);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putBoolean("first",false);
                            editor.putInt("id",user.getId());
                            editor.putString("sec",user.getSecretkey());
                            editor.putString("username",user.getUsername());
                            editor.putString("photo",user.getUserphoto());
                            editor.commit();
                            Global.MAIN_USER = user;
                            finish();
                        }else{
                            Toast.makeText(getApplicationContext(),user.getMsg(),Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        Toast.makeText(getApplicationContext(),"网络错误",Toast.LENGTH_SHORT).show();
                    }
                });
                break;
            case R.id.register:
                startActivity(new Intent(Login.this,Register.class));
                break;
            /*case R.id.login_to_losspass:
                break;*/
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            setResult(RESULT_FINISH);
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        setResult(RESULT_FINISH);
    }
}
