package view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shxy.dazuoye.R;

/**
 * Created by caolu on 2017/5/10.
 */

public class MyNewImageView extends RelativeLayout {

    private boolean once = true;
    private ImageView mImageView;
    private TextView mTextView;
    int textcolor;
    int imageid ;
    String text;
    float size;
    private int width = 0, height = 0;
    private int mImageHeight = 0, mTextViewHeight = 0;

    public MyNewImageView(Context context) {
        this(context, null);
    }

    public MyNewImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyNewImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.MyNewImageView);

        int textcolor = array.getColor(R.styleable.MyNewImageView_textcolor, 0);
        int imageid = array.getResourceId(R.styleable.MyNewImageView_src, R.mipmap.ic_launcher);
        String text = array.getString(R.styleable.MyNewImageView_text);
        float size = array.getDimensionPixelSize(R.styleable.MyNewImageView_textsize, 14);

        Log.i("info", "info");
        array.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (once){
            once = false;
            initView();
            width = MeasureSpec.getSize(widthMeasureSpec);
            height = MeasureSpec.getSize(heightMeasureSpec);
            mImageHeight = 4 * width / 5;
            mTextViewHeight = 1 * width / 5;
            int index = 0;
            RelativeLayout.LayoutParams lp1 = new LayoutParams(width, mImageHeight);
            mImageView.setId(index + 1);
            mImageView.setLayoutParams(lp1);
            //addView(mImageView);
            lp1 = new LayoutParams(width, mTextViewHeight);
            lp1.addRule(RelativeLayout.BELOW, index + 1);
            mTextView.setLayoutParams(lp1);
            Log.i("width,height", width + "   " + height);
            mImageView.setImageResource(imageid);
            addView(mImageView);
            mTextView.setText(text);
            mTextView.setTextColor(textcolor);
            mTextView.setTextSize(size);
            addView(mTextView);
        }

    }

    private void initView() {
        mTextView = new TextView(getContext());
        mImageView = new ImageView(getContext());
    }


}
