package view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

/**
 * Created by caolu on 2017/5/16.
 */

public class LineTextView extends TextView {

    private int width = 0, height = 0;
    private int textSize = 0;

    public LineTextView(Context context) {
        this(context, null);
    }

    public LineTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LineTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setWillNotDraw(false);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = measureWidth(widthMeasureSpec);
        height = measureHeight(heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int lines = (int) ((getBottom() - getTop() - getPaddingTop() - getPaddingBottom()) / getTextSize());
        Log.i("top",getTop()+"");
        Log.i("bottom",getBottom()+"");
        Log.i("padding",getPaddingLeft()+" " + getPaddingTop()+ " "+ getPaddingRight() + " " + getPaddingBottom());
        Log.i("lines", lines + "");
        Log.i("height",height+"");
        Log.i("textsize",getTextSize()+"");

        Path path = new Path();
        int y = getTop() - getPaddingTop();
        View temp = (View) getParent();
        int startX = getLeft() - getPaddingLeft() - temp.getLeft();
        int endX = getRight() - getPaddingRight();
        Log.i("startX",startX+"");
        Log.i("endX",endX+"");
        Paint paint = new Paint();
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.STROKE);
        paint.setDither(true);
        paint.setAntiAlias(true);
        canvas.drawPath(path,paint);
        for (int i = 0; i < lines; i++) {
            path.moveTo(startX, y + i * getTextSize());
            path.lineTo(endX, y + i * getTextSize());

        }
        canvas.drawPath(path,paint);
    }
    private int measureWidth(int measureSpec) {
        int result = 200;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        switch (specMode) {
            case MeasureSpec.UNSPECIFIED:
                result = specSize;
                break;
            case MeasureSpec.AT_MOST:
                result = Math.min(result, specSize);
                break;
            case MeasureSpec.EXACTLY:
                result = specSize;
                break;
        }
        return result;
    }

    private int measureHeight(int measureSpec) {
        int result = 200;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        switch (specMode) {
            case MeasureSpec.UNSPECIFIED:
                result = specSize;
                break;
            case MeasureSpec.AT_MOST:
                result = Math.min(result, specSize);
                break;
            case MeasureSpec.EXACTLY:
                result = specSize;
                break;
        }
        return result;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.i("touch x" ,event.getRawX()+ "");
        Log.i("touch.y" , event.getRawY()+"");
        Log.i("touch x" ,event.getX()+ "");
        Log.i("touch.y" , event.getY()+"");
        return super.onTouchEvent(event);

    }
}
