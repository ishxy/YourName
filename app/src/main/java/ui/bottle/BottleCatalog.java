package ui.bottle;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.shxy.dazuoye.R;

import java.util.ArrayList;
import java.util.Arrays;

import global.Global;


public class BottleCatalog extends Activity {

    private ListView mListView;
    private ArrayList<String> mList;
    private ImageView back;
    private final Class<?>[] mTargets = {UpBottleInfo.class, ReceiveBottle.class,ReceiveBottle.class};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        setContentView(R.layout.ac_bottlecatalog);
        initView();

    }

    private void initView() {
        back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        initListView();
    }

    private void initListView() {
        mListView = (ListView) findViewById(R.id.list);
        mList = new ArrayList<>(Arrays.asList("发瓶子", "已发送的瓶子", "收到的瓶子"));
        MyAdapter adapter = new MyAdapter(this, mList);
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(getApplication(),mTargets[position]));
                if (Global.DEBUG_FINISH)finish();
            }
        });
    }

    public class MyAdapter extends BaseAdapter {

        private Context mContext;
        private ArrayList mInfo;
        MyAdapter(Context context , ArrayList list){
            mInfo = list;
            mContext = context;
        }

        @Override
        public int getCount() {
            return mInfo.size();
        }

        @Override
        public Object getItem(int position) {
            return mInfo.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.main_item,null);
            TextView t = (TextView)convertView.findViewById(R.id.text);
            t.setText(mInfo.get(position).toString());
            return convertView;
        }
    }
}
