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

import com.shxy.dazuoye.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import cn.finalteam.galleryfinal.BuildConfig;
import cn.finalteam.galleryfinal.CoreConfig;
import cn.finalteam.galleryfinal.FunctionConfig;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.ImageLoader;
import cn.finalteam.galleryfinal.ThemeConfig;
import cn.finalteam.galleryfinal.model.PhotoInfo;
import cn.finalteam.galleryfinal.widget.GFImageView;
import util.PicassoImageLoader;

/**
 * Created by caolu on 2017/5/29.
 */

public class UpToday extends AppCompatActivity {

    private TextView cancle, up;
    private EditText info;
    private GFImageView select;

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
        String upinfo = info.getText().toString();

    }

}
