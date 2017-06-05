package ui.bottle;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.shxy.dazuoye.R;

import view.BaseActivity;

/**
 * Created by caolu on 2017/6/4.
 */

public class SendDetails extends BaseActivity {

    private TextView infoText, reback;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_reveivedetails);

        initView();
        initBundle();
    }

    private void initBundle() {
        Bundle bundle = getIntent().getExtras();
        String info = bundle.getString("info");
         final Integer id = bundle.getInt("id");
        infoText.setText(info);
        reback = (TextView) findViewById(R.id.reback);
        reback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt("id", id);
                Intent intent = new Intent(SendDetails.this, UpBottleInfo.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    private void initView() {
        ImageView back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        infoText = (TextView) findViewById(R.id.info);
    }
}
