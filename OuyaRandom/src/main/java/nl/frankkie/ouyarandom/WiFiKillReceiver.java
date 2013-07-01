package nl.frankkie.ouyarandom;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.wifi.WifiManager;
import android.preference.PreferenceManager;
import android.widget.Toast;

/**
 *
 * @author FrankkieNL
 */
public class WiFiKillReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context arg0, Intent arg1) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(arg0);
        if (!prefs.getBoolean("wifikill", false)) {
            return;
        }
        kill(arg0);
    }

    public void kill(Context c) {
        WifiManager wifiManager = (WifiManager) c.getSystemService(Context.WIFI_SERVICE);
        if (!wifiManager.isWifiEnabled()){
            return;
        }
        try {
            wifiManager.setWifiEnabled(true);
            wifiManager.setWifiEnabled(false);
        } catch (Exception e) {
            //
            e.printStackTrace();
        }

        try {
            Toast.makeText(c, "Killed WiFi", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            //ignore
        }
    }
}
