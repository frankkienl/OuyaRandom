package nl.frankkie.ouyarandom;

import android.content.Context;
import android.graphics.*;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by FrankkieNL on 1-7-13.
 */
public class ControllerAxisView extends View {
    Context context;
    public static final int TYPE_NONE = 0;
    public static final int TYPE_TWO_AXIS = 2;
    public static final int TYPE_ONE_AXIS_HOR = 10;
    public static final int TYPE_ONE_AXIS_VERT = 11;
    int type = TYPE_NONE;
    String name = "";
    float x = 0f, y = 0f;
    Paint paint;

    public ControllerAxisView(Context context) {
        super(context);
        this.context = context;
        paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
    }

    public ControllerAxisView(Context context, AttributeSet attributeSet){
        super(context,attributeSet);
        this.context = context;
        paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setFakeBoldText(true);
        paint.setTextSize(23f);
    }

    public void setName(String name) {
        this.name = name;
        invalidate();
    }

    public void setType(int type) {
        this.type = type;
        invalidate();
    }

    public void setX(float x) {
        this.x = x;
        invalidate();
    }

    public void setXY(float x, float y) {
        this.x = x;
        this.y = y;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //super.onDraw(canvas);
        paint.setColor(Color.BLACK);
        canvas.drawRect(new Rect(0, 0, getWidth(), getHeight()), paint);
        if (type == TYPE_TWO_AXIS) {
            paint.setColor(Color.RED);
            canvas.drawCircle(map(x,-1f,1f,0f,1f) * getWidth(), map(y,-1f,1f,0f,1f) * getHeight(), 15, paint);
            paint.setColor(Color.BLUE);
            canvas.drawText(name, 10, 20, paint);
            canvas.drawText("" + x, 10, 50, paint);
            canvas.drawText("" + y, 10, 80, paint);
        } else if (type == TYPE_ONE_AXIS_HOR) {
            paint.setColor(Color.RED);
            canvas.drawRect(new RectF(0,0,x * getWidth(), getHeight()), paint);
            paint.setColor(Color.BLUE);
            canvas.drawText(name, 10, 20, paint);
            canvas.drawText("" + x, 10, 50, paint);
        } else if (type == TYPE_ONE_AXIS_VERT){
            paint.setColor(Color.RED);
            canvas.drawRect(new RectF(0, map(x,0f,1f,1f,0f)*getHeight(),getWidth(),getHeight()), paint);
            paint.setColor(Color.BLUE);
            canvas.drawText(name, 10, 20, paint);
            canvas.drawText("" + x, 10, 50, paint);
        }
    }

    float map(float x, float in_min, float in_max, float out_min, float out_max)
    {
        return (x - in_min) * (out_max - out_min) / (in_max - in_min) + out_min;
    }
}
