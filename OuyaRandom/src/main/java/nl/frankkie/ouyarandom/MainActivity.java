package nl.frankkie.ouyarandom;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import nl.frankkie.ouyarandom.minigame.ShootGameActivity;

import java.util.ArrayList;

public class MainActivity extends Activity {

    MainActivity thisAct;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        thisAct = this;
        initUI();
    }

    protected void initUI() {
        makeTests();
        makeButtons();
    }
    ArrayList<RandomTest> tests = new ArrayList<RandomTest>();

    public void makeTests() {
        tests.clear();
        tests.add(new RandomTest("WiFiKill", WiFiKillActivity.class));
        tests.add(new RandomTest("Hardware", HardwareActivity.class));
        tests.add(new RandomTest("AppList", AppListActivity.class));
        tests.add(new RandomTest("DataURI", DataUriActivity.class));
        tests.add(new RandomTest("Controller", ControllerActivity.class));
        tests.add(new RandomTest("Viewport", ViewportActivity.class));
        tests.add(new RandomTest("Discover", DiscoverTestActivity.class));
        tests.add(new RandomTest("Minigame", ShootGameActivity.class));
    }

    public void makeButtons() {
        LinearLayout container = (LinearLayout) findViewById(R.id.main_container);
        container.removeAllViews();
        for (int i = 0; i < tests.size(); i++) {
            final RandomTest test = tests.get(i);
            Button btn = new Button(this);
            btn.setText(test.title);
            btn.setOnClickListener(new View.OnClickListener() {
                public void onClick(View arg0) {
                    Intent i = new Intent();
                    i.setClass(thisAct, test.activity);
                    try {
                        startActivity(i);
                    } catch (Exception e) {
                        ShowException.showException(e, thisAct);
                    }
                }
            });
            container.addView(btn);
        }
    }

    public class RandomTest {

        public Class<? extends Activity> activity;
        public String title;

        public RandomTest(String title, Class<? extends Activity> activity) {
            this.activity = activity;
            this.title = title;
        }
    }
}
