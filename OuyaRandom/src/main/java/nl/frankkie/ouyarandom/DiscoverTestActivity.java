package nl.frankkie.ouyarandom;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import eu.chainfire.libsuperuser.Shell;

import java.util.List;

/**
 * @author FrankkieNL
 */
public class DiscoverTestActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); //To change body of generated methods, choose Tools | Templates.
        initUI();
    }

    protected void initUI() {
        setContentView(R.layout.discover_test);
        Button btn1 = (Button) findViewById(R.id.discover1);
        Button btn2 = (Button) findViewById(R.id.discover2);
        Button btn3 = (Button) findViewById(R.id.discover3);

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent();
                i.setClassName("tv.ouya.console", "tv.ouya.console.launcher.store.adapter.DiscoverActivity");
                //i.setClassName("tv.ouya.console", "tv.ouya.console.launcher.store.OldDiscoverActivity");
                try {
                    startActivity(i);
                } catch (Exception e) {
                    ShowException.showException(e, DiscoverTestActivity.this);
                }
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent();
                //i.setClassName("tv.ouya.console", "tv.ouya.console.launcher.store.adapter.DiscoverActivity");
                i.setClassName("tv.ouya.console", "tv.ouya.console.launcher.store.OldDiscoverActivity");
                try {
                    startActivity(i);
                } catch (Exception e) {
                    ShowException.showException(e, DiscoverTestActivity.this);
                }
            }
        });
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StartDiscoverRootAsyncTask task = new StartDiscoverRootAsyncTask();
                task.execute();
            }
        });
    }


    private class StartDiscoverRootAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            // Let's do some SU stuff
            boolean suAvailable = Shell.SU.available();
            if (suAvailable) {
                //String suVersion = Shell.SU.version(false);
                //String suVersionInternal = Shell.SU.version(true);
                List<String> suResult = Shell.SU.run(new String[]{
                        "am start --user 0 -n tv.ouya.console/tv.ouya.console.launcher.store.adapter.DiscoverActivity"
                });

            }
            return null;
        }

    }

}
