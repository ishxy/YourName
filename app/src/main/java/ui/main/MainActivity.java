package ui.main;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.shxy.dazuoye.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;

import bean.User;
import global.Global;
import ui.login.Login;
import ui.self.SelfMain;
import ui.task.TaskProgress;
import ui.bottle.BottleCatalog;
import ui.history.HistoryBill;
import ui.today.TodayMain;

public class MainActivity extends AppCompatActivity {

    private ListView mListView;
    private MyAdapter mAdapter;
    private ArrayList<String> mInfo;
    private ImageView img ;
    private TextView name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (isFirst()){
            int requestCode = 1;
            startActivityForResult(new Intent(getApplication(), Login.class),requestCode);
        }
        setInfo();
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        setContentView(R.layout.activity_main);
        initListItem();
        initView();

    }

    private void initView() {
        img = (ImageView) findViewById(R.id.image);
        Picasso.with(getApplicationContext())
                .load(Global.MAIN_USER.getUserphoto())
                .into(img);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), SelfMain.class));
                if (Global.DEBUG_FINISH){
                    finish();
                }
            }
        });
        name = (TextView) findViewById(R.id.name);
        name.setText(Global.MAIN_USER.getUsername());
    }

    private void setInfo() {
        if(Global.MAIN_USER == null){
            SharedPreferences preferences = getSharedPreferences("firstinfo",MODE_PRIVATE);
            Integer id = preferences.getInt("id",0);
            String sec = preferences.getString("sec","null");
            String username = preferences.getString("username","null");
            String photo = preferences.getString("photo",null);
            Log.i("photo",photo);
            User u = new User();
            u.setId(id);
            u.setSecretkey(sec);
            u.setUsername(username);
            u.setUserphoto(photo);
            Global.MAIN_USER = u;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i("re",requestCode + " " + resultCode);
        if (requestCode == 1){
            if (resultCode == Login.RESULT_FINISH)
                finish();
        }
    }

    private boolean isFirst()
    {
        SharedPreferences preferences = getSharedPreferences("firstinfo",MODE_PRIVATE);
        Log.i("isfirst",preferences.getBoolean("first",true)+"");
        return preferences.getBoolean("first",true);
    }

    private void initListItem() {
        mListView = (ListView) findViewById(R.id.listview);
        mInfo = new ArrayList<String>(Arrays.asList("历史","瓶子","今日","任务"));
        mAdapter = new MyAdapter(getApplicationContext(),mInfo);
        mListView.setAdapter(mAdapter);
        final Class<?>[] classes = {HistoryBill.class,BottleCatalog.class, TodayMain.class, TaskProgress.class};
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(MainActivity.this,classes[position]));
                if (Global.DEBUG_FINISH)
                   finish();
            }
        });
    }


}
