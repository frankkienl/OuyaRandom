package nl.frankkie.ouyarandom;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

/**
 *
 * @author Gebruiker
 */
public class ControllerActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); //To change body of generated methods, choose Tools | Templates.
        TextView tv = new TextView(this);
        tv.setText("TODO");
        setContentView(tv);
    }
    
    
    
}
