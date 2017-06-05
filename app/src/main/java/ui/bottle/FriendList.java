package ui.bottle;

import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.shxy.dazuoye.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import bean.User;
import cz.msebera.android.httpclient.Header;
import global.Global;
import util.HttpRequest;
import view.BaseActivity;


public class FriendList extends BaseActivity {

    private ListView mListView;
    private ArrayList<User> mList;
    private MyAdapter adapter;
    private TextView back;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_friendlist);
        initView();


    }

    private void initView() {
        mListView = (ListView) findViewById(R.id.list);
        back = (TextView) findViewById(R.id.cancle);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        setInfo();
    }

    private void setInfo() {
        final RequestParams params = new RequestParams();
        params.add("userid", Global.MAIN_USER.getId() + "");
        params.add("secretkey", Global.MAIN_USER.getSecretkey());
        HttpRequest.get(getApplicationContext(), "get_contact_list", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String jsonString = new String(responseBody);
                JsonParser parser = new JsonParser();
                JsonObject object = (JsonObject) parser.parse(jsonString);
                String msg = object.get("msg").getAsString();
                Integer state = object.get("statues").getAsInt();
                Log.i("jsonString",jsonString);
                if (state == 0) {
                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    JsonArray array = object.get("data").getAsJsonArray();
                    mList = new ArrayList<User>();
                    for (int i = 0; i < array.size(); i++) {
                        JsonObject o = array.get(i).getAsJsonObject();

                        User user = new User();
                        user.setUserphoto(o.get("userphoto").getAsString());
                        user.setUsername(o.get("username").getAsString());
                        user.setId(o.get("id").getAsInt());
                        mList.add(user);
                    }

                    adapter = new MyAdapter();
                    mListView.setAdapter(adapter);
                    mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Bundle bundle = new Bundle();
                            bundle.putInt("id",mList.get(position).getId());

                            Intent intent = new Intent(FriendList.this,UpBottleInfo.class);
                            intent.putExtras(bundle);
                            startActivity(intent);
                        }
                    });
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(getApplicationContext(), "网络错误", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private class MyAdapter extends BaseAdapter {

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
            convertView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_friendlist, null);
            ImageView img = (ImageView) convertView.findViewById(R.id.img);
            TextView t = (TextView) convertView.findViewById(R.id.name);
            t.setText("姓名：" + mList.get(position).getUsername());
            Picasso.with(getApplicationContext())
                    .load(mList.get(position).getUserphoto())
                    .into(img);
            return convertView;
        }
    }
}
