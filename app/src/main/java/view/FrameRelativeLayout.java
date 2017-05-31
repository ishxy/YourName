package view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

/**
 * Created by caolu on 2017/5/15.
 */

public class FrameRelativeLayout extends RelativeLayout {

    private int width = 0, height = 0;
    private Paint mPaint = null;
    private Path mPath = null;
    ;
    private final String LINE_COLOR = "#e0e0e0";
    private final int LINDE_WIDTH = 2;

    public FrameRelativeLayout(Context context) {
        this(context, null);
    }

    public FrameRelativeLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FrameRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setWillNotDraw(false);
        // this.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        mPath = new Path();
        mPaint = new Paint(Color.parseColor(LINE_COLOR));
        mPaint.setDither(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(LINDE_WIDTH);
        mPaint.setAntiAlias(true);
        Log.i("con", "con");
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = MeasureSpec.getSize(widthMeasureSpec);
        height = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.i("size", getLeft() + " " + getRight() + " " + getTop() + " " + getBottom());
//        canvas.drawRect(getLeft() + getPaddingLeft(), getTop() + getPaddingTop(),
//                getRight() - getPaddingRight(), getBottom() - getPaddingBottom(), new Paint(Color.parseColor("#77ff0000")));
        initPath();
        canvas.drawPath(mPath, mPaint);

        /*canvas.drawLine(50,50,200,200,paint);
        canvas.drawLine(50,50,200,200,paint);
        canvas.drawLine(50,50,200,200,paint);
        canvas.drawLine(50,50,200,200,paint);
        canvas.drawLine(50,50,200,200,paint);
        canvas.drawLine(50,50,200,200,paint);
        canvas.drawLine(50,50,200,200,paint);*/
    }

    private void initPath() {
        Log.i("padding", getPaddingLeft() + " " + getPaddingRight() + " " + getPaddingTop() + " " + getPaddingBottom());
        int x = (int) (getPaddingLeft() / 5f * 3);
        int y = (int) (getPaddingTop() / 5f * 3);
        int smallwidth = getPaddingLeft() / 5;
        int y0 = (int) (3 / 4f * (height - getPaddingBottom()) + 1 / 4f * getPaddingTop());
        Log.i("y0", y0 + "");
        //开始点
        mPath.moveTo(x, y);
        //贝塞尔
        mPath.quadTo(getPaddingLeft(), y0 / 2, x, y0);
        mPath.lineTo(x + smallwidth, y0 + 40);
        mPath.lineTo(x, y0 + 100);
        mPath.lineTo(x + smallwidth * 3 / 2f, y0 + 140);
        mPath.lineTo(x + smallwidth, y0 + 180);
        mPath.lineTo(x, y0 + 260);
        mPath.lineTo(x + smallwidth, y0 + 300);
        //终止点
        mPath.lineTo(x, height - getPaddingBottom() / 5f * 3);


        mPath.moveTo(x, height - getPaddingBottom() / 5f * 3);
        mPath.quadTo(1 / 2f * (width - getPaddingRight() + 1 / 2f * getPaddingLeft()), height - getPaddingBottom()*3/5f, width - 3 * smallwidth, height - 3 * smallwidth);


        mPath.lineTo(width-3*smallwidth,3*smallwidth);


        mPath.lineTo(x,y);
    }
}
