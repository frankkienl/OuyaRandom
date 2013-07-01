package nl.frankkie.ouyarandom;

import android.app.*;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.*;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by FrankkieNL on 1-7-13.
 */
public class UpdateCheckActivity extends Activity {

    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUI();
    }

    protected void initUI() {
        setContentView(R.layout.updatecheck);
        Button btn = (Button) findViewById(R.id.update_btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateCheck();
            }
        });
    }

    public void toast(final String s) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(UpdateCheckActivity.this, s, Toast.LENGTH_LONG).show();
            }
        });
    }

    protected void updateCheck() {
        UpdateCheckAsyncTask task = new UpdateCheckAsyncTask();
        task.execute("Go !"); //some random string, does not matter
    }

    public class UpdateCheckAsyncTask extends AsyncTask<Object, Object, JSONObject> {

        Dialog dialog;

        @Override
        protected void onPreExecute() {
            dialog = ProgressDialog.show(UpdateCheckActivity.this, "Version Check", "Please Wait...");
        }

        @Override
        protected JSONObject doInBackground(Object... objects) {
            HttpClient client = new DefaultHttpClient();
            HttpGet request = new HttpGet("https://raw.github.com/frankkienl/OuyaRandom/master/version.json");
            try {
                HttpResponse response = client.execute(request);
                JSONObject jsonObject = new JSONObject(EntityUtils.toString(response.getEntity()));
                int version = jsonObject.getInt("newestVersion");
                String changes = jsonObject.getString("changes");
                try {
                    //Get versionCode from Manifest
                    int myVersion = UpdateCheckActivity.this.getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;
                    if (myVersion < version) {
                        return jsonObject; //boolean as Object
                    } else {
                        toast("This app is up to date :-) !");
                        return null;
                    }
                } catch (PackageManager.NameNotFoundException nnfe) {
                    toast("Version check failed.. (packagename not found)");
                }
            } catch (IOException e) {
                toast("Version check failed.. (internet is not working)");
            } catch (JSONException je) {
                toast("Version check failed.. (json-parser is not working)");
            }
            return null;
        }


        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            try {
                dialog.dismiss();
            } catch (Exception e) {
                //ignore
            }

            if (jsonObject != null) {
                AlertDialog.Builder builder = new AlertDialog.Builder(UpdateCheckActivity.this);
                builder.setTitle("New Version Available !");
                String changes = "";
                try {
                    String temp = jsonObject.getString("changes");
                    changes = "Changes:\n" + temp + "\n";
                } catch (JSONException e){
                    //ignore
                }
                builder.setMessage(changes + "Download now?");
                builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        downloadViaAsyncTask();
                    }
                });
                builder.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //nothing, just remove dialog
                    }
                });
                builder.create().show();
            }
        }
    }

    /**
     * Not used, because i'm to lazy to make a broadcastreceiver
     * (You'll get an broadcast when the download is complete)
     */
    public void downloadViaDownloadManager() {
        String url = "https://github.com/frankkienl/OuyaRandom/raw/master/OuyaRandom/OuyaRandom.apk";
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        request.setDescription("Ouya RandomApp by FrankkieNL");
        request.setTitle("Ouya RandomApp");
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "OuyaRandom.apk");
        // get download service and enqueue file
        DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        manager.enqueue(request);
        Toast.makeText(UpdateCheckActivity.this, "Downloading...", Toast.LENGTH_LONG).show();
    }

    public void downloadViaAsyncTask() {
        DownloadAsyncTask task = new DownloadAsyncTask();
        task.execute("Sure"); //some string, doesn't care
    }

    public class DownloadAsyncTask extends AsyncTask<Object, Integer, Boolean> {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(UpdateCheckActivity.this);
            dialog.setTitle("Downloading Update");
            dialog.setMessage("Please Wait...");
            dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            dialog.setProgress(0);
            dialog.setMax(100);
            dialog.setProgressNumberFormat("Progress: %1d - %2d");
            dialog.setIndeterminate(false);
            dialog.show();
        }

        @Override
        protected Boolean doInBackground(Object... objects) {
            try {
                //Thanks to:
                //http://stackoverflow.com/questions/3028306/download-a-file-with-android-and-showing-the-progress-in-a-progressdialog
                URL url = new URL("https://github.com/frankkienl/OuyaRandom/raw/master/OuyaRandom/OuyaRandom.apk");
                URLConnection connection = url.openConnection();
                connection.connect();
                // this will be useful so that you can show a typical 0-100% progress bar
                int fileLength = connection.getContentLength();

                // download the file
                InputStream input = new BufferedInputStream(url.openStream());
                OutputStream output = new FileOutputStream(Environment.getExternalStorageDirectory() + "/OuyaRandom.apk");

                byte data[] = new byte[1024];
                long total = 0;
                int count;
                while ((count = input.read(data)) != -1) {
                    total += count;
                    // publishing the progress....
                    publishProgress((int) (total * 100 / fileLength));
                    output.write(data, 0, count);
                }

                output.flush();
                output.close();
                input.close();
                return true;
            } catch (Exception e) {
                toast("Download failed.. (internet is not working)");
            }
            return false;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            dialog.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(Boolean success) {
            try {
                dialog.dismiss();
            } catch (Exception e) {
                //ignore
            }
            if (success) {
                try {
                    Intent i = new Intent();
                    i.setAction(Intent.ACTION_INSTALL_PACKAGE);
                    i.setData(Uri.parse("file://" + Environment.getExternalStorageDirectory().getPath() + "/OuyaRandom.apk"));
                    startActivity(i);
                } catch (Exception e) {
                    toast("Cannot update application.. Please use a filemanager (like Total Commander) and select 'OuyaRandom.apk'.");
                }
            }
        }
    }
}
