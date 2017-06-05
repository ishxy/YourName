package ui.self;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.shxy.dazuoye.R;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

import bean.BaseBean;
import cn.finalteam.galleryfinal.BuildConfig;
import cn.finalteam.galleryfinal.CoreConfig;
import cn.finalteam.galleryfinal.FunctionConfig;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.ThemeConfig;
import cn.finalteam.galleryfinal.model.PhotoInfo;
import cn.finalteam.galleryfinal.widget.GFImageView;
import cz.msebera.android.httpclient.Header;
import global.Global;
import ui.today.UpToday;
import util.HttpRequest;
import util.PicassoImageLoader;
import view.BaseActivity;

/**
 * Created by caolu on 2017/6/3.
 */

public class SelfMain extends BaseActivity implements View.OnClickListener {

    private ImageView back;
    private GFImageView img;
    private TextView nameText, sexText, ageText;
    private File imgFile;
    private String imgfileString;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_selfinfo);

        initView();
        setInfo();
    }

    private void setInfo() {
        RequestParams params = new RequestParams();
        params.add("userid", Global.MAIN_USER.getId() + "");
        params.add("secretkey", Global.MAIN_USER.getSecretkey());
        HttpRequest.get(getApplicationContext(), "getmyinfo", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                parserJson(new String(responseBody));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(getApplicationContext(), "网络错误", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void parserJson(String jsonString) {
        JsonParser p = new JsonParser();
        JsonObject o = (JsonObject) p.parse(jsonString);
        Integer state = o.get("statues").getAsInt();
        String msg = o.get("msg").getAsString();
        if (state == 0) {
            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
            return;
        }
        Log.i("json", jsonString);
        String name = o.get("username").getAsString();
        Integer age = o.get("age").getAsInt();
        String sex = o.get("sex").getAsString();
        String photoUrl = o.get("photo").getAsString();
        Picasso.with(getApplicationContext())
                .load(photoUrl)
                .into(img);
        nameText.setText(name);
        ageText.setText(age + "");
        sexText.setText(sex);
    }

    private void initView() {
        back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        img = (GFImageView) findViewById(R.id.img);
        nameText = (TextView) findViewById(R.id.name);
        sexText = (TextView) findViewById(R.id.sex);
        ageText = (TextView) findViewById(R.id.age);
        RelativeLayout r1 = (RelativeLayout) findViewById(R.id.imgL);
        RelativeLayout r2 = (RelativeLayout) findViewById(R.id.nameL);
        RelativeLayout r3 = (RelativeLayout) findViewById(R.id.sexL);
        RelativeLayout r4 = (RelativeLayout) findViewById(R.id.ageL);
        r1.setOnClickListener(this);
        r2.setOnClickListener(this);
        r3.setOnClickListener(this);
        r4.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgL:
                selectAndUpImg();
                break;
            case R.id.nameL:
                Bundle bundle = new Bundle();
                String title, tip, info, url, param;
                bundle.putString("title", "修改名字");
                bundle.putString("tip", "一个好的名字可以朋友更好的记住");
                bundle.putString("info", nameText.getText().toString());
                bundle.putString("url", "");
                bundle.putString("param", "");
                Intent intent = new Intent(SelfMain.this, ChangeInfo.class);
                intent.putExtras(bundle);
                startActivityForResult(intent, 0);
                break;
            case R.id.sexL:
                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("性别").setSingleChoiceItems(new String[]{"男", "女"}, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        upSexInfo(which);
                        dialog.cancel();
                    }


                }).show();
                break;
            case R.id.ageL:
                AlertDialog.Builder b = new AlertDialog.Builder(this);
                final EditText ed = new EditText(this);
                b.setTitle("年龄").setView(ed).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        upAgeInfo(ed.getText().toString());
                        ageText.setText(ed.getText().toString());
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
                break;
        }
    }

    private void upAgeInfo(String s) {
        /**
         * 上传年龄
         */
        RequestParams params = new RequestParams();
        params.add("userid", Global.MAIN_USER.getId() + "");
        params.add("secretkey", Global.MAIN_USER.getSecretkey());
        params.add("age", s);
        HttpRequest.post(getApplicationContext(), "ChangePersonalInformation", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Gson gson = new Gson();
                BaseBean b = gson.fromJson(new String(responseBody), BaseBean.class);
                Toast.makeText(getApplicationContext(), b.getMsg(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(getApplicationContext(), new String(responseBody), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void selectAndUpImg() {
        //配置主题
//ThemeConfig.CYAN
        ThemeConfig theme = new ThemeConfig.Builder()
                .setTitleBarBgColor(Color.parseColor("#24bd41"))
                .build();
//配置功能
        FunctionConfig functionConfig = new FunctionConfig.Builder()
                .setEnableCamera(true)
                .setEnableEdit(true)
                .setEnableCrop(true)
                .setEnableRotate(true)
                .setCropSquare(true)
                .setEnablePreview(true)
                .build();

//配置imageloader
        final PicassoImageLoader imageloader = new PicassoImageLoader();

//设置核心配置信息
        CoreConfig coreConfig = new CoreConfig.Builder(getApplicationContext(), imageloader, theme)
                .setDebug(BuildConfig.DEBUG)
                .setFunctionConfig(functionConfig)
                .build();
        GalleryFinal.init(coreConfig);
        int REQUEST_CODE_GALLERY = 0;
        GalleryFinal.openGallerySingle(REQUEST_CODE_GALLERY, functionConfig, new GalleryFinal.OnHanlderResultCallback() {
            @Override
            public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
                Toast.makeText(getApplicationContext(), "file://" + resultList.get(0).getPhotoPath(), Toast.LENGTH_SHORT).show();
                imgfileString = resultList.get(0).getPhotoPath();
                imgFile = new File(imgfileString);
                imageloader.displayImage(SelfMain.this, resultList.get(0).getPhotoPath(), img, null, 600, 600);
                /**
                 * 上传图片
                 */
                RequestParams params = new RequestParams();
                params.add("userid", Global.MAIN_USER.getId() + "");
                params.add("secretkey", Global.MAIN_USER.getSecretkey());
                try {
                    params.put("userphoto", imgFile);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                HttpRequest.post(getApplicationContext(), "do_change_head_photo", params, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        Gson gson = new Gson();
                        SharedPreferences preferences = getSharedPreferences("firstinfo",MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();

                        editor.putString("photo",imgfileString);
                        Log.i("imgfileString + " , imgfileString);
                        editor.commit();
                        Global.MAIN_USER.setUserphoto(imgfileString);
                        BaseBean b = gson.fromJson(new String(responseBody), BaseBean.class);
                        Toast.makeText(getApplicationContext(), b.getMsg(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        Toast.makeText(getApplicationContext(), new String(responseBody), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onHanlderFailure(int requestCode, String errorMsg) {
                Toast.makeText(getApplicationContext(), "failure", Toast.LENGTH_SHORT).show();
                img.setImageResource(R.drawable.camera);
            }
        });
    }


    private void upSexInfo(int which) {
        String[] sex = {"男", "女"};
        //Toast.makeText(getApplicationContext(),sex[which],Toast.LENGTH_SHORT).show();
        sexText.setText(sex[which]);
        /**
         * 上传性别逻辑
         */
        RequestParams params = new RequestParams();
        params.add("userid", Global.MAIN_USER.getId() + "");
        params.add("secretkey", Global.MAIN_USER.getSecretkey());
        params.add("sex", sex[which]);
        //params.put("userphoto",new File(""));
        HttpRequest.post(getApplicationContext(), "ChangePersonalInformation", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Gson gson = new Gson();
                BaseBean b = gson.fromJson(new String(responseBody), BaseBean.class);
                Toast.makeText(getApplicationContext(), b.getMsg(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(getApplicationContext(), new String(responseBody), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //返回名字
        if (requestCode == 0 && resultCode == 1) {
            Bundle b = data.getExtras();
            final String name = b.getString("info");
            nameText.setText(b.getString("info"));
            RequestParams params = new RequestParams();
            params.add("userid", Global.MAIN_USER.getId() + "");
            params.add("secretkey", Global.MAIN_USER.getSecretkey());
            params.add("username", b.getString("info"));
            HttpRequest.post(getApplicationContext(), "ChangePersonalInformation", params, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    Gson gson = new Gson();
                    BaseBean b = gson.fromJson(new String(responseBody), BaseBean.class);
                    Toast.makeText(getApplicationContext(), b.getMsg(), Toast.LENGTH_SHORT).show();
                    Global.MAIN_USER.setUsername(name);
                    SharedPreferences preferences = getSharedPreferences("firstinfo",MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("username",name);
                    editor.commit();
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    Toast.makeText(getApplicationContext(), new String(responseBody), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
