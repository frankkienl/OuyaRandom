package nl.frankkie.ouyarandom;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.*;
import tv.ouya.console.api.OuyaController;

/**
 * Created by Gebruiker on 7-7-13.
 */
public class ViewportTestActivity extends Activity {

    ViewportTestView viewportTestView;
    OuyaController controller;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUI();
        initController();
    }

    public void initUI() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        viewportTestView = new ViewportTestView(this);
        setContentView(viewportTestView);
    }

    protected void initController() {
        OuyaController.init(this);
        controller = OuyaController.getControllerByPlayer(0);
    }

    public void refreshView(){
        viewportTestView.invalidate();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == OuyaController.BUTTON_MENU){
            return super.onKeyDown(keyCode,event);
        }
        if (event.getKeyCode() == OuyaController.BUTTON_A){
            return super.onKeyDown(keyCode,event);
        }
        if (event.getKeyCode() == OuyaController.BUTTON_DPAD_DOWN || event.getKeyCode() == OuyaController.BUTTON_DPAD_UP || event.getKeyCode() == OuyaController.BUTTON_DPAD_LEFT || event.getKeyCode() == OuyaController.BUTTON_DPAD_RIGHT){
            viewportTestView.pressedDpad(event.getKeyCode());
        }
        ///
        boolean handled = OuyaController.onKeyDown(keyCode, event);
        refreshView();
        return handled || super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        boolean handled = OuyaController.onKeyUp(keyCode, event);
        refreshView();
        return handled || super.onKeyUp(keyCode, event);
    }

    @Override
    public boolean onGenericMotionEvent(MotionEvent event) {
        boolean handled = OuyaController.onGenericMotionEvent(event);
        refreshView();
        return handled || super.onGenericMotionEvent(event);
    }


    public class ViewportTestView extends View {

        int top = 150;
        int bottom = 150;
        int left = 150;
        int right = 150;
        String[] names = new String[]{"Top", "Bottom", "Left", "Right"};
        int nowChanging = 0;
        Rect screen;
        Rect viewport;
        Paint paint;
        Context context;

        public ViewportTestView(Context context) {
            super(context);
            this.context = context;
            paint = new Paint();
            paint.setStyle(Paint.Style.FILL);
            paint.setTextSize(38);
            paint.setFakeBoldText(true);
            screen = new Rect(0,0,1920,1080);
            viewport = new Rect(0+left, 0+top, 1920-bottom,1080-bottom);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            paint.setColor(Color.BLUE);
            canvas.drawRect(screen, paint);
            paint.setColor(Color.BLACK);
            canvas.drawRect(viewport,paint);
            paint.setColor(Color.RED);
            canvas.drawText("Top: " + top + "; Bottom: " + bottom + "; Left: " + left + "; Right: " + right,200,200,paint);
            canvas.drawText("Changes values until you don't see any Blue anymore, press O for next value.", 200, 270,paint);
            canvas.drawText("Now changing: " + names[nowChanging], 200, 370,paint);
        }


        public void pressedDpad(int keycode){
            switch (nowChanging){
                case 0: { //Top
//                   if ()
                }
            }
        }
    }
}
