package ui.bottle;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.shxy.dazuoye.R;

/**
 * Created by caolu on 2017/5/15.
 */

public class ReveiveDetails extends AppCompatActivity{

    private ImageView back;
    private TextView mContent;
    private final String space = "    ";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        setContentView(R.layout.ac_reveivedetails);
        initView();
    }

    private void initView() {
        mContent = (TextView) findViewById(R.id.info);
        back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Bundle bundle = getIntent().getExtras();
        mContent.setText(bundle.getString("info"));
        final Integer id = bundle.getInt("id");
        TextView reback = (TextView) findViewById(R.id.reback);
        reback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt("id", id);
                Intent intent = new Intent(ReveiveDetails.this, UpBottleInfo.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }
}
