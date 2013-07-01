package nl.frankkie.ouyarandom;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 *
 * @author FrankkieNL
 */
public class WiFiKillActivity extends Activity {

    SharedPreferences prefs;
    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); //To change body of generated methods, choose Tools | Templates.
        initUI();
    }

    protected void initUI() {
        setContentView(R.layout.wifikill);
        tv = (TextView) findViewById(R.id.wifikill_tv);
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        Button on = (Button) findViewById(R.id.wifikill_on);
        on.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                prefs.edit().putBoolean("wifikill", true).commit();
                refreshTv();
            }
        });
        Button off = (Button) findViewById(R.id.wifikill_off);
        off.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                prefs.edit().putBoolean("wifikill", false).commit();
                refreshTv();
            }
        });
        Button killnow = (Button) findViewById(R.id.wifikill_now);
        killnow.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                kill();
            }
        });
        Button refresh = (Button) findViewById(R.id.wifikill_refresh);
        refresh.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                refreshTv();
            }
        });
        refreshTv();
    }

    protected void refreshTv() {
        boolean wifikill = prefs.getBoolean("wifikill", false);
        WifiManager wifiManager = (WifiManager) this.getSystemService(Context.WIFI_SERVICE);
        StringBuilder s = new StringBuilder();
        s.append("WiFiKill is currently: ").append((wifikill) ? "ON" : "OFF");
        s.append("\nWiFi is currently: ").append((wifiManager.isWifiEnabled()) ? "ON" : "OFF");
        s.append("\n\nWiFiKill kills your WiFi Connection, so you can use your Wired connection.\n\nDisclamer: I am not responsible if this does not work!!\n\n(I heard it didnt, because ethernet wont connect if wifi is off)");
        tv.setText(s.toString());
    }

    public void kill() {
        try {
            WifiManager wifiManager = (WifiManager) this.getSystemService(Context.WIFI_SERVICE);
            wifiManager.setWifiEnabled(true);
            wifiManager.setWifiEnabled(false);
        } catch (Exception e) {
            //
            e.printStackTrace();
        }
        refreshTv();
    }
}