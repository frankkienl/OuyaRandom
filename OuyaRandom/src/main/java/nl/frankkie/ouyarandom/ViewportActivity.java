/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.frankkie.ouyarandom;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 *
 * @author FrankkieNL
 */
public class ViewportActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); //To change body of generated methods, choose Tools | Templates.
        setContentView(R.layout.viewport);
        TextView tv = (TextView) findViewById(R.id.viewport_info);
        tv.setText("TL;DR: Use this to test how many pixel are not visible on your screen\n\n" +
                "I have noticed that 'normal' android app fit nicely on the screen, because OUYA adds a black border automatically for those apps.\n" +
                "But when an Application requests a FullScreen window, that ouya-fix disappears. The application now has to take care of its own overscan-compensation.\n\n" +
                "This test helps checking how many pixels fall of your screen.\nGive this information to the developer who's application needs overscan-compensation.\n");
        Button btn = (Button) findViewById(R.id.viewport_btn);
        //btn.setEnabled(false); //not ready for prime time
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent();
                i.setClass(ViewportActivity.this,ViewportTestActivity.class);
                startActivity(i);
            }
        });
    }
    
}
