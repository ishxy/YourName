package ui.today;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.shxy.dazuoye.R;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

import cn.finalteam.galleryfinal.BuildConfig;
import cn.finalteam.galleryfinal.CoreConfig;
import cn.finalteam.galleryfinal.FunctionConfig;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.ImageLoader;
import cn.finalteam.galleryfinal.ThemeConfig;
import cn.finalteam.galleryfinal.model.PhotoInfo;
import cn.finalteam.galleryfinal.widget.GFImageView;
import cz.msebera.android.httpclient.Header;
import global.Global;
import util.HttpRequest;
import util.PicassoImageLoader;

/**
 * Created by caolu on 2017/5/29.
 */

public class UpToday extends AppCompatActivity {

    private TextView cancle, up;
    private EditText info;
    private GFImageView select;
    private File imgFile;
    private String fileUrl = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        setContentView(R.layout.ac_uptoday);
        initView();
    }

    private void initView() {
        cancle = (TextView) findViewById(R.id.cancle);
        up = (TextView) findViewById(R.id.up);
        info = (EditText) findViewById(R.id.info);
        select = (GFImageView) findViewById(R.id.select);
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upDetails();
            }
        });

        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectDetails();
            }
        });
    }

    private void selectDetails() {
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
                imgFile = new File(resultList.get(0).getPhotoPath());
                imageloader.displayImage(UpToday.this, resultList.get(0).getPhotoPath(), select, null, 600, 600);
            }

            @Override
            public void onHanlderFailure(int requestCode, String errorMsg) {
                Toast.makeText(getApplicationContext(), "failure", Toast.LENGTH_SHORT).show();
                select.setImageResource(R.drawable.camera);
            }
        });
    }

    private void upDetails() {

        RequestParams params = new RequestParams();
        if(imgFile!=null){
            getImageUrl();
            /*Log.i("fa - url",fileUrl);
            Toast.makeText(getApplicationContext(),fileUrl +"***" , Toast.LENGTH_SHORT).show();
            params.add("fileurl",fileUrl);*/
        }else {
            upAllInfo(params);
        }
    }

    private void upAllInfo(RequestParams params) {
        String upinfo = info.getText().toString();
        params.add("userid", Global.MAIN_USER.getId()+"");
        params.add("secretkey",Global.MAIN_USER.getSecretkey());
        params.add("content",upinfo);
        params.add("location","呼和浩特");

        HttpRequest.post(getApplicationContext(), "do_push_diary_event", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                JsonParser p = new JsonParser();
                JsonObject o = (JsonObject) p.parse(new String(responseBody));
                Integer state = o.get("statues").getAsInt();
                String msg = o.get("msg").getAsString();
                Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }

    public void getImageUrl() {

        final RequestParams params = new RequestParams();
        try {
            params.add("userid",Global.MAIN_USER.getId()+"");
            params.add("secretkey",Global.MAIN_USER.getSecretkey());
            params.put("filename",imgFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        HttpRequest.post(getApplicationContext(), "do_upload_dairy_photo", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                JsonParser p = new JsonParser();
                JsonObject object = (JsonObject) p.parse(new String(responseBody));
                String msg = object.get("msg").getAsString();
                Integer state = object.get("statues").getAsInt();
                if(state!=1)
                {
                    Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_SHORT).show();
                    return;
                }
                fileUrl = object.get("fileurl").getAsString();
                Log.i("shou - url",fileUrl);
                RequestParams params1 = new RequestParams();
                params1.add("fileurl",fileUrl);
                upAllInfo(params1);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }
}
