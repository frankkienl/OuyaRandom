package nl.frankkie.ouyarandom;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * @author FrankkieNL
 */
public class ControllerActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); //To change body of generated methods, choose Tools | Templates.
        LinearLayout container = new LinearLayout(this);
        container.setOrientation(LinearLayout.VERTICAL);
        Button btn = new Button(this);
        btn.setText("Click here to start Controller Test (from OUYA SDK)");
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent();
                i.setClass(ControllerActivity.this, tv.ouya.controllertest.ControllerTestActivity.class);
                try {
                    startActivity(i);
                } catch (Exception e) {
                    ShowException.showException(e, ControllerActivity.this);
                }
            }
        });
        Button btn2 = new Button(this);
        btn2.setText("Click here to start Random OUYA Controller Test");
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent();
                i.setClass(ControllerActivity.this, ControllerTestActivity.class);
                try {
                    startActivity(i);
                } catch (Exception e) {
                    ShowException.showException(e, ControllerActivity.this);
                }
            }
        });
        Button btn3 = new Button(this);
        btn3.setText("Click here to start the Universal Test (for other controllers)");
        btn3.setEnabled(false); //not ready for real-world testers !
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent();
                i.setClass(ControllerActivity.this, UniversalControllerActivity.class);
                try {
                    startActivity(i);
                } catch (Exception e) {
                    ShowException.showException(e, ControllerActivity.this);
                }
            }
        });
        container.addView(btn2);
        container.addView(btn3);
        container.addView(btn);
        setContentView(container);
    }
}