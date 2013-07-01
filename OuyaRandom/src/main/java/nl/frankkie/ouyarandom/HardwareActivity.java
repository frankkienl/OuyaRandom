package nl.frankkie.ouyarandom;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.TextView;

/**
 *
 * @author FrankkieNL
 */
public class HardwareActivity extends Activity {

    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUI();
    }

    protected void initUI() {
        setContentView(R.layout.hardware);
        tv = (TextView) findViewById(R.id.hardware_tv);
        go();
    }

    protected void go() {

        final StringBuilder s = new StringBuilder();
        //
        try {
            s.append("=== Build ===\n");
            s.append("Build.BOARD: ").append(Build.BOARD).append("\n");
            s.append("Build.BOOTLOADER: ").append(Build.BOOTLOADER).append("\n");
            s.append("Build.BRAND: ").append(Build.BRAND).append("\n");
            s.append("Build.CPU_ABI: ").append(Build.CPU_ABI).append("\n");
            s.append("Build.CPU_ABI2: ").append(Build.CPU_ABI2).append("\n");
            s.append("Build.DEVICE: ").append(Build.DEVICE).append("\n");
            s.append("Build.DISPLAY: ").append(Build.DISPLAY).append("\n");
            s.append("Build.FINGERPRINT: ").append(Build.FINGERPRINT).append("\n");
            s.append("Build.HARDWARE: ").append(Build.HARDWARE).append("\n");
            s.append("Build.HOST: ").append(Build.HOST).append("\n");
            s.append("Build.ID: ").append(Build.ID).append("\n");
            s.append("Build.MANUFACTURER: ").append(Build.MANUFACTURER).append("\n");
            s.append("Build.MODEL: ").append(Build.MODEL).append("\n");
            s.append("Build.PRODUCT: ").append(Build.PRODUCT).append("\n");
            s.append("Build.RADIO: ").append(Build.RADIO).append("\n");
            s.append("Build.SERIAL: ").append(Build.SERIAL).append("\n");
            s.append("Build.TAGS: ").append(Build.TAGS).append("\n");
            s.append("Build.TIME: ").append(Build.TIME).append("\n");
            s.append("Build.TYPE: ").append(Build.TYPE).append("\n");
            s.append("Build.UNKNOWN: ").append(Build.UNKNOWN).append("\n");
            s.append("Build.USER: ").append(Build.USER).append("\n");
//      s.append("Build.getRadioVersion(): ").append(Build.getRadioVersion()).append("\n");
            s.append("Build.VERSION.CODENAME: ").append(Build.VERSION.CODENAME).append("\n");
            s.append("Build.VERSION.INCREMENTAL: ").append(Build.VERSION.INCREMENTAL).append("\n");
            s.append("Build.VERSION.RELEASE: ").append(Build.VERSION.RELEASE).append("\n");
            s.append("Build.VERSION.SDK: ").append(Build.VERSION.SDK).append("\n");
            s.append("Build.VERSION.SDK_INT: ").append(Build.VERSION.SDK_INT).append("\n");
            s.append("\n");
            ///////
            ////////
            s.append("=== DisplayMetrics ===\n");
            DisplayMetrics dm = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(dm);
            s.append("dm.density: ").append(dm.density).append("\n");
            s.append("dm.densityDpi: ").append(dm.densityDpi).append("\n");
            s.append("dm.heightPixels: ").append(dm.heightPixels).append("\n");
            s.append("dm.scaledDensity: ").append(dm.scaledDensity).append("\n");
            s.append("dm.widthPixels: ").append(dm.widthPixels).append("\n");
            s.append("dm.xdpi: ").append(dm.xdpi).append("\n");
            s.append("dm.ydpi: ").append(dm.ydpi).append("\n");
            s.append("dm.toString(): ").append(dm.toString()).append("\n");
            s.append("\n");
        } catch (Exception e) {
            ShowException.showException(e, this);
        }
        //
        tv.setText(s.toString());
    }
}
