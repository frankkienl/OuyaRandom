package nl.frankkie.ouyarandom;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import eu.chainfire.libsuperuser.Shell;

import javax.net.ssl.HandshakeCompletedListener;
import java.io.*;
import java.util.List;

/**
 * @author FrankkieNL
 */
public class DiscoverTestActivity extends Activity {

    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); //To change body of generated methods, choose Tools | Templates.
        initUI();
    }

    protected void initUI() {
        setContentView(R.layout.discover_test);
        Button btnNormal = (Button) findViewById(R.id.discover_discover_normal);
        Button btnOld = (Button) findViewById(R.id.discover_discover_old);
        Button btnRoot1 = (Button) findViewById(R.id.discover_discover_root1);
        Button btnRoot2 = (Button) findViewById(R.id.discover_discover_root2);
        Button btnOff = (Button) findViewById(R.id.discover_off);
        Button btnOffRoot1 = (Button) findViewById(R.id.discover_off_root);

        btnNormal.setOnClickListener(new View.OnClickListener() {
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
        btnOld.setOnClickListener(new View.OnClickListener() {
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
        btnRoot1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StartDiscoverRootAsyncTask task = new StartDiscoverRootAsyncTask();
                task.execute();
            }
        });
        btnOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent localIntent = new Intent("tv.ouya.console.action.TURN_OFF");
                try {
                    sendBroadcast(localIntent);
                } catch (Exception e) {
                    ShowException.showException(e, DiscoverTestActivity.this);
                }
            }
        });
        btnOffRoot1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TurnOffAsyncTask task = new TurnOffAsyncTask();
                task.execute();
            }
        });

        btnRoot2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StartDiscoverRoot2AsyncTask task = new StartDiscoverRoot2AsyncTask();
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
                String temp = "Result from SU:\n";
                for (String s : suResult){
                    temp+=s + "\n";
                }
                final String toastLine = temp;
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(DiscoverTestActivity.this,toastLine, Toast.LENGTH_SHORT).show();
                    }
                });
            }
            return null;
        }

    }

    private class TurnOffAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            // Let's do some SU stuff
            boolean suAvailable = Shell.SU.available();
            if (suAvailable) {
                //String suVersion = Shell.SU.version(false);
                //String suVersionInternal = Shell.SU.version(true);
                List<String> suResult = Shell.SU.run(new String[]{
                        "am broadcast --user 0 -a tv.ouya.console.action.TURN_OFF"
                });

            }
            return null;
        }

    }

    private class StartDiscoverRoot2AsyncTask extends AsyncTask<Void,Void,Void>{
        @Override
        protected Void doInBackground(Void... voids) {
            Shell.Builder b = new Shell.Builder();
            Shell.Interactive interactive =  b.open();
            interactive.addCommand("su");
            interactive.waitForIdle();
            interactive.addCommand("am start --user 0 -n tv.ouya.console/tv.ouya.console.launcher.store.adapter.DiscoverActivity");
            interactive.waitForIdle();
            interactive.close();
            return null;
        }
    }

    private class ProcessInThread extends Thread {
        InputStream in;

        ProcessInThread(InputStream inputStream) {
            this.in = inputStream;
        }


        @Override
        public void run() {
            InputStreamReader reader = new InputStreamReader(in);
            BufferedReader bin = new BufferedReader(reader);
            String line = null;
            boolean go = true;
            try {
                while (go) {
                    line = bin.readLine();
                    Log.i("DiscoverTest", line);
                    final String toastLine = line;
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(DiscoverTestActivity.this,toastLine, Toast.LENGTH_SHORT).show();
                        }
                    });
                    if (line == null) {
                        break;
                    }
                }
            } catch (final IOException ioe) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        ShowException.showException(ioe, DiscoverTestActivity.this);
                    }
                });
            }
        }
    }

}
