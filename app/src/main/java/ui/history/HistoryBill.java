package ui.history;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListView;

import com.shxy.dazuoye.R;

import java.util.ArrayList;

import ui.history.HistroyAdapter;

/**
 * Created by caolu on 2017/5/10.
 */

public class HistoryBill extends AppCompatActivity{

    private ArrayList<String> mInfo;
    private ListView mListView;
    private HistroyAdapter mAdapter;
    private ImageView backButton;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        setContentView(R.layout.ac_historybill);
        initView();
    }

    private void initView() {
        mListView = (ListView)findViewById(R.id.listview);
        mInfo = new ArrayList<>();
        mInfo.add("123");
        mInfo.add("123");
        mAdapter = new HistroyAdapter(this,mInfo);
        mListView.setAdapter(mAdapter);
        backButton = (ImageView)findViewById(R.id.back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


}
