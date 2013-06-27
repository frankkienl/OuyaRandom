package nl.frankkie.ouyarandom;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {

    MainActivity thisAct;
    
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        thisAct = this;
        initUI();
    }
    
    protected void initUI(){
        Button controller = (Button) findViewById(R.id.controller_btn);
        controller.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                Intent i = new Intent();
                i.setClass(thisAct, ControllerActivity.class);
                startActivity(i);
            }
        });
        Button hardware = (Button) findViewById(R.id.hardware_btn);
        hardware.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                Intent i = new Intent();
                i.setClass(thisAct, HardwareActivity.class);
                startActivity(i);
            }
        });
        Button viewport = (Button) findViewById(R.id.viewport_btn);
        viewport.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                Intent i = new Intent();
                i.setClass(thisAct, ViewportActivity.class);
                startActivity(i);
            }
        });
        Button wifikill = (Button) findViewById(R.id.wifikill_btn);
        wifikill.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                Intent i = new Intent();
                i.setClass(thisAct, WiFiKillActivity.class);
                startActivity(i);
            }
        });
    }
}

