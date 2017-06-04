package ui.self;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.shxy.dazuoye.R;

import view.BaseActivity;

/**
 * Created by caolu on 2017/6/4.
 */

public class ChangeInfo extends BaseActivity{

    private String title,tip,info,url,param;
    private TextView titleText,tipText;
    private EditText infoEidt;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_changeinfo);
        initView();
        setInfo();

    }

    private void initView() {
        infoEidt = (EditText) findViewById(R.id.info);
        titleText = (TextView) findViewById(R.id.title);
        tipText = (TextView) findViewById(R.id.tip);
        TextView ok;
        ImageView back;
        ok = (TextView) findViewById(R.id.ok);
        back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upInfo();
            }
        });
    }

    private void upInfo() {

    }

    private void setInfo() {
        Bundle bundle = getIntent().getExtras();
        title = bundle.getString("title");
        tip = bundle.getString("tip");
        info = bundle.getString("info");
        url = bundle.getString("url");
        param = bundle.getString("param");
        tipText.setText(tip);
        titleText.setText(title);
        infoEidt.setText(info);
    }
}
