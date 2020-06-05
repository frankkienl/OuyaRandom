package nl.frankkie.ouyarandom.minigame;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;
import tv.ouya.console.api.OuyaController;

/**
 * Created by FrankkieNL on 29-6-13.
 */
public class ShootGameActivity extends Activity {

    ShootGameView view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (OuyaController.getControllerByPlayer(0) == null) {
            Toast.makeText(this, "OUYA Controller not found!", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        initUI();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return view.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        return view.onKeyUp(keyCode, event);
    }

    @Override
    public boolean onGenericMotionEvent(MotionEvent event) {
        return view.onGenericMotionEvent(event);
    }

    protected void initUI() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        view = new ShootGameView(this);
        setContentView(view);
    }
}
