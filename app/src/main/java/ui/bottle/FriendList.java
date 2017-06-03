package ui.bottle;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.shxy.dazuoye.R;

import java.util.ArrayList;

import bean.User;


public class FriendList extends AppCompatActivity{

    private ListView mListView;
    private ArrayList<User> mList;
    private MyAdapter adapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_friendlist);
        initView();
    }

    private void initView() {
        mListView = (ListView) findViewById(R.id.list);
        adapter = new MyAdapter();
        mListView.setAdapter(adapter);
    }


    private class MyAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_friendlist,null);
            return convertView;
        }
    }
}
