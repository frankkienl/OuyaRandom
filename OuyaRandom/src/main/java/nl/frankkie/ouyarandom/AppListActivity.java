package nl.frankkie.ouyarandom;

import android.app.*;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.*;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ProgressBar;
//import android.widget.
import java.util.List;
import android.widget.*;
import android.provider.*;
import android.net.*;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AppListActivity extends Activity {

    LinearLayout container;

    @Override
    public void onCreate(Bundle b) {
        super.onCreate(b);
        initUI();
    }

    private void initUI() {
        ScrollView scrollView = new ScrollView(this);
        container = new LinearLayout(this);
        container.setOrientation(LinearLayout.VERTICAL);
        scrollView.addView(container);
        setContentView(scrollView);
        getAppList();
    }

    private void getAppList() {
        PackageManager pm = getPackageManager();
        List<PackageInfo> installedPackages = pm.getInstalledPackages(0);
        for (final PackageInfo pak : installedPackages) {
            Button btn = new Button(this);
            btn.setText(getApplicationName(pak) + " - " + pak.packageName);
            btn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    showInstalledAppDetails(AppListActivity.this,pak.packageName);
                }
            });
            container.addView(btn);
        }
    }

    public String getApplicationName(PackageInfo pak) {
        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(pak.packageName.toString(), 0);
            String applicationName = getPackageManager().getApplicationLabel(packageInfo.applicationInfo).toString();
            return applicationName;
        } catch (PackageManager.NameNotFoundException ex) {
            Logger.getLogger(AppListActivity.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "App";
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add("Export");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        shareAppList();
        return true;
    }

    public void shareAppList() {
        PackageManager pm = getPackageManager();
        List<PackageInfo> installedPackages = pm.getInstalledPackages(0);
        StringBuilder s = new StringBuilder();
        String[] packagesArr = new String[installedPackages.size()];
        int i = 0;
        for (PackageInfo pak : installedPackages) {
            s.append(pak.packageName).append("\n");
            packagesArr[i++] = pak.packageName;
        }
        String packagesList = s.toString();
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, packagesList);
        intent.putExtra(Intent.EXTRA_TITLE, "Installed Packages");
        intent.setType("text/plain");
        intent.putExtra("nl.frankkie.randomapp.installedPackagesList", packagesList);
        intent.putExtra("nl.frankkie.randomapp.installedPackagesArray", packagesArr);
        startActivity(intent);
    }
    //////////////////?//////////////////////
    private static final String SCHEME = "package";
    private static final String APP_PKG_NAME_21 = "com.android.settings.ApplicationPkgName";
    private static final String APP_PKG_NAME_22 = "pkg";
    private static final String APP_DETAILS_PACKAGE_NAME = "com.android.settings";
    private static final String APP_DETAILS_CLASS_NAME = "com.android.settings.InstalledAppDetails";

    public static void showInstalledAppDetails(Context context, String packageName) {
        Intent intent = new Intent();
        final int apiLevel = Build.VERSION.SDK_INT;
        if (apiLevel >= 9) { // above 2.3
            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts(SCHEME, packageName, null);
            intent.setData(uri);
        } else { // below 2.3
            final String appPkgName = (apiLevel == 8 ? APP_PKG_NAME_22
                    : APP_PKG_NAME_21);
            intent.setAction(Intent.ACTION_VIEW);
            intent.setClassName(APP_DETAILS_PACKAGE_NAME,
                    APP_DETAILS_CLASS_NAME);
            intent.putExtra(appPkgName, packageName);
        }
        context.startActivity(intent);
    }
}
