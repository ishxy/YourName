package ui.main;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.ImageView;

import com.shxy.dazuoye.R;

import ui.login.Login;
import view.BaseActivity;

/**
 * Created by caolu on 2017/6/6.
 */

public class Welcome extends BaseActivity {

    private static final int MSG_BEGIN = 100;
    private boolean first = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_welcome);
        ImageView img = (ImageView) findViewById(R.id.begin);;
        img.setScaleType(ImageView.ScaleType.FIT_XY);
        handler.sendEmptyMessageDelayed(MSG_BEGIN, 2000);
    }

    private boolean isFirst() {
        SharedPreferences preferences = getSharedPreferences("firstinfo", MODE_PRIVATE);
        Log.i("isfirst", preferences.getBoolean("first", true) + "");
        return preferences.getBoolean("first", true);
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_BEGIN:
                    if (isFirst())
                        startActivity(new Intent(getApplicationContext(), Login.class));
                    else
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    finish();
                    break;
            }
        }
    };
}
