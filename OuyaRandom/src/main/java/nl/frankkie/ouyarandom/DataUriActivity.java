package nl.frankkie.ouyarandom;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

/**
 *
 * @author frankkie
 */
public class DataUriActivity extends Activity {
  /** Called when the activity is first created. */
  
  private static DataUriActivity instance;

  public static DataUriActivity getInstance() {
    return instance;
  }
  
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.datauri);
    instance = this;
    initUI();
  }

  private void initUI() {
    initDataUriTest();
  }

  private void initDataUriTest() {
    final EditText ed = (EditText) findViewById(R.id.datauri_ed);
    Button btn = (Button) findViewById(R.id.datauri_btn);
    btn.setOnClickListener(new OnClickListener() {

      public void onClick(View v) {
        String dataUri = ed.getText().toString();
        Intent i = new Intent();
        try {
          i.setAction(Intent.ACTION_VIEW);
          i.setData(Uri.parse(dataUri));
          startActivity(i);
        } catch (Exception e) {
          ShowException.showException(e,DataUriActivity.this);
        }
      }
    });
  }
}
