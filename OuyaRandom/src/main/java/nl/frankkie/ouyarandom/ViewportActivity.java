/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.frankkie.ouyarandom;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

/**
 *
 * @author FrankkieNL
 */
public class ViewportActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); //To change body of generated methods, choose Tools | Templates.
        TextView tv = new TextView(this);
        tv.setText("TODO");
        setContentView(tv);
    }
    
}
