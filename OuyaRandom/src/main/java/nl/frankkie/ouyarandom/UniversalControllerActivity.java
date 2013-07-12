package nl.frankkie.ouyarandom;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.LinearLayout;
import android.widget.TextView;
import tv.ouya.console.api.OuyaController;

import java.util.ArrayList;

/**
 * Created by Gebruiker on 3-7-13.
 */
public class UniversalControllerActivity extends Activity {

    LinearLayout[] linControllers = new LinearLayout[4];
    TextView[] tvs = new TextView[4];
    ControllerPlayer[] players = new ControllerPlayer[4];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        OuyaController.init(this);
        initUI();
    }

    private void initUI() {
        setContentView(R.layout.universal_controller_test);

        linControllers[0] = (LinearLayout) findViewById(R.id.universal_controller_1);
        linControllers[1] = (LinearLayout) findViewById(R.id.universal_controller_2);
        linControllers[2] = (LinearLayout) findViewById(R.id.universal_controller_3);
        linControllers[3] = (LinearLayout) findViewById(R.id.universal_controller_4);
        //
        for (int i = 0; i < 4; i++) {
            tvs[i] = new TextView(this);
            players[i] = new ControllerPlayer();
            linControllers[i].addView(tvs[i]);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        boolean handled = OuyaController.onKeyDown(keyCode, event);
        checkButtonDown(event);
        refreshTv();
        return handled || super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        boolean handled = OuyaController.onKeyUp(keyCode, event);
        checkButtonUp(event);
        refreshTv();
        return handled || super.onKeyUp(keyCode, event);
    }

    @Override
    public boolean onGenericMotionEvent(MotionEvent event) {
        boolean handled = OuyaController.onGenericMotionEvent(event);
        checkAxis(event);
        refreshTv();
        return handled || super.onGenericMotionEvent(event);
    }

    private void checkAxis(MotionEvent event) {
        int playerNumber = OuyaController.getPlayerNumByDeviceId(event.getDeviceId());
//        ControllerButton btn = players[playerNumber].getButton(event.);
//        btn.value = true;
    }

    private void checkButtonDown(KeyEvent event) {
        int playerNumber = OuyaController.getPlayerNumByDeviceId(event.getDeviceId());
        if (playerNumber < 0) {
            playerNumber = 0;
        }
        ControllerButton btn = players[playerNumber].getButton(event.getKeyCode());
        btn.value = true;
    }

    private void checkButtonUp(KeyEvent event) {
        int playerNumber = OuyaController.getPlayerNumByDeviceId(event.getDeviceId());
        if (playerNumber < 0) {
            playerNumber = 0;
        }
        ControllerButton btn = players[playerNumber].getButton(event.getKeyCode());
        btn.value = false;
    }

    private void refreshTv() {
        for (int i = 0; i < 4; i++) {
            StringBuilder sb = new StringBuilder();
            sb.append("player ").append(i + 1).append("\n");
            ControllerPlayer p = players[i];
            sb.append("deviceid ").append("??\n");
            for (ControllerButton btn : p.buttons) {
                sb.append("Button ").append(btn.keyCode).append(" : ").append(btn.value).append("\n");
            }
            tvs[i].setText(sb.toString());
        }
    }


    public class ControllerPlayer {
        public ControllerAxis getAxis(int id) {
            for (int i = 0; i < axis.size(); i++) {
                if (axis.get(i).axisNumber == id) {
                    return axis.get(i);
                }
            }
            ControllerAxis axe = new ControllerAxis();
            axe.axisNumber = id;
            axis.add(axe);
            return axe;
        }

        public ControllerButton getButton(int id) {
            for (int i = 0; i < buttons.size(); i++) {
                if (buttons.get(i).keyCode == id) {
                    return buttons.get(i);
                }
            }
            ControllerButton axe = new ControllerButton();
            axe.keyCode = id;
            buttons.add(axe);
            return axe;
        }

        ArrayList<ControllerAxis> axis = new ArrayList<ControllerAxis>();
        ArrayList<ControllerButton> buttons = new ArrayList<ControllerButton>();
    }

    public class ControllerAxis {
        int axisNumber;
        float value;
    }

    public class ControllerButton {
        int keyCode;
        boolean value;
    }

}
