package ui.bottle;

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
    private TextView mTitle;
    private TextView mContent;
    private final String space = "    ";
    private String testTitle = "鹊桥仙";
    private String testContent = "纤云弄巧，飞星传恨，银汉迢迢暗度。金风玉露一相逢，便胜却人间无数。\n柔情似水，佳期如梦，忍顾鹊桥归路。两情若是久长时，又岂在朝朝暮暮";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        setContentView(R.layout.ac_reveivedetails);
        initView();
    }

    private void initView() {
        mTitle = (TextView) findViewById(R.id.title);
        mContent = (TextView) findViewById(R.id.info);
        mTitle.setText(testTitle);
        mContent.setText(space + testContent);
        //mContent.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
