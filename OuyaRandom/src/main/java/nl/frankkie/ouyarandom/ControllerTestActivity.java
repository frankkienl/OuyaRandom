package nl.frankkie.ouyarandom;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.TextView;
import tv.ouya.console.api.OuyaController;

/**
 * Created by FrankkieNL on 29-6-13.
 */
public class ControllerTestActivity extends Activity {

    TextView tv;
    OuyaController controller;
    int counter =0;
    ControllerAxisView l2;
    ControllerAxisView ls;
    ControllerAxisView rs;
    ControllerAxisView r2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUI();
        initController();
        refreshTv();
    }

    protected void initUI() {
        setContentView(R.layout.controller);
        tv = (TextView) findViewById(R.id.controller_tv);
        l2 = (ControllerAxisView) findViewById(R.id.axis_l2);
        ls = (ControllerAxisView) findViewById(R.id.axis_ls);
        rs = (ControllerAxisView) findViewById(R.id.axis_rs);
        r2 = (ControllerAxisView) findViewById(R.id.axis_r2);
        l2.setType(ControllerAxisView.TYPE_ONE_AXIS_VERT);
        ls.setType(ControllerAxisView.TYPE_TWO_AXIS);
        rs.setType(ControllerAxisView.TYPE_TWO_AXIS);
        r2.setType(ControllerAxisView.TYPE_ONE_AXIS_VERT);
        l2.setName("L2");
        ls.setName("LS");
        rs.setName("RS");
        r2.setName("R2");
    }

    protected void initController() {
        OuyaController.init(this);
        controller = OuyaController.getControllerByPlayer(0);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        boolean handled = OuyaController.onKeyDown(keyCode, event);
        refreshTv();
        return handled || super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        boolean handled = OuyaController.onKeyUp(keyCode, event);
        refreshTv();
        return handled || super.onKeyUp(keyCode, event);
    }

    @Override
    public boolean onGenericMotionEvent(MotionEvent event) {
        boolean handled = OuyaController.onGenericMotionEvent(event);
        refreshTv();
        return handled || super.onGenericMotionEvent(event);
    }

    public void refreshTv() {
        StringBuilder sb = new StringBuilder();
        sb.append("DeviceId: " + controller.getDeviceId());
        sb.append("\n");
        sb.append("PlayerNum: " + controller.getPlayerNum());
        sb.append("\n");
        sb.append("Digital:\n");
        sb.append("O: " + controller.getButton(OuyaController.BUTTON_O));
        sb.append("\n");
        sb.append("U: " + controller.getButton(OuyaController.BUTTON_U));
        sb.append("\n");
        sb.append("Y: " + controller.getButton(OuyaController.BUTTON_Y));
        sb.append("\n");
        sb.append("A: " + controller.getButton(OuyaController.BUTTON_A));
        sb.append("\n");
        sb.append("L1: " + controller.getButton(OuyaController.BUTTON_L1));
        sb.append("\n");
        sb.append("R1: " + controller.getButton(OuyaController.BUTTON_R1));
        sb.append("\n");
        sb.append("L3: " + controller.getButton(OuyaController.BUTTON_L3));
        sb.append("\n");
        sb.append("R3: " + controller.getButton(OuyaController.BUTTON_R3));
        sb.append("\n");
        sb.append("D-Pad UP: " + controller.getButton(OuyaController.BUTTON_DPAD_UP));
        sb.append("\n");
        sb.append("D-Pad DOWN: " + controller.getButton(OuyaController.BUTTON_DPAD_DOWN));
        sb.append("\n");
        sb.append("D-Pad LEFT: " + controller.getButton(OuyaController.BUTTON_DPAD_LEFT));
        sb.append("\n");
        sb.append("D-Pad RIGHT: " + controller.getButton(OuyaController.BUTTON_DPAD_RIGHT));
        sb.append("\n");
        sb.append("Analog:\n");
        sb.append("LS.X: " + controller.getAxisValue(OuyaController.AXIS_LS_X));
        sb.append("\n");
        sb.append("LS.Y: " + controller.getAxisValue(OuyaController.AXIS_LS_Y));
        sb.append("\n");
        sb.append("RS.X: " + controller.getAxisValue(OuyaController.AXIS_RS_X));
        sb.append("\n");
        sb.append("RS.Y: " + controller.getAxisValue(OuyaController.AXIS_RS_Y));
        sb.append("\n");
        sb.append("L2: " + controller.getAxisValue(OuyaController.AXIS_L2));
        sb.append("\n");
        sb.append("R2: " + controller.getAxisValue(OuyaController.AXIS_R2));
        sb.append("\n");
        sb.append(counter++);
        tv.setText(sb.toString());
        tv.invalidate();
        //Refresh AxisViews
        l2.setX(controller.getAxisValue(OuyaController.AXIS_L2));
        r2.setX(controller.getAxisValue(OuyaController.AXIS_R2));
        ls.setXY(controller.getAxisValue(OuyaController.AXIS_LS_X),controller.getAxisValue(OuyaController.AXIS_LS_Y));
        rs.setXY(controller.getAxisValue(OuyaController.AXIS_RS_X),controller.getAxisValue(OuyaController.AXIS_RS_Y));
    }
}
