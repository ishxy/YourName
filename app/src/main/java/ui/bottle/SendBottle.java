package ui.bottle;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.shxy.dazuoye.R;

import java.util.ArrayList;
import java.util.Arrays;

import view.BaseActivity;

/**
 * Created by caolu on 2017/6/3.
 */

public class SendBottle extends BaseActivity{
    private ListView mListView;
    private ArrayList<String> mInfo;
    private String[] ss = {"后端组制作", "朝鲁 - 常静娜", "何树林", "刘健楠", "郭满荣", "5个不够", "再来一个"};
    private ReceiveBottleAdapter mAdapter;
    private ImageView back;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        setContentView(R.layout.ac_sendbottle);
        initView();
    }

    private void initView() {
        mListView = (ListView) findViewById(R.id.listview);
        mInfo = new ArrayList<>();
        mInfo.addAll(Arrays.asList(ss));
        mAdapter = new ReceiveBottleAdapter(this, mInfo);
        mListView.setAdapter(mAdapter);
        back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(getApplicationContext(), ReveiveDetails.class));
                finish();
            }
        });
    }
}
