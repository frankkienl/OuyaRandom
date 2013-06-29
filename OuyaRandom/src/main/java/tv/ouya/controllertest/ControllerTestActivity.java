/*
 * Copyright (C) 2012 OUYA, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package tv.ouya.controllertest;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.TextView;
import tv.ouya.console.api.OuyaController;
import nl.frankkie.ouyarandom.R;

public class ControllerTestActivity extends Activity {
	
	private OuyaPlotFPS m_plot = null;

    /**
     * Array holding all the controller views for easy lookup.
     */
    private View[] mControllerViews;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_controller);
        OuyaController.init(this);
        
        m_plot = (OuyaPlotFPS) findViewById(R.id.ouyaPlotFPS1);
        m_plot.m_fpsText = (TextView) findViewById(R.id.fpsText);
        m_plot.m_cpu1Text = (TextView) findViewById(R.id.cpu1Text);
        m_plot.m_cpu2Text = (TextView) findViewById(R.id.cpu2Text);
        m_plot.m_cpu3Text = (TextView) findViewById(R.id.cpu3Text);
        m_plot.m_cpu4Text = (TextView) findViewById(R.id.cpu4Text);
        m_plot.m_keyDownText = (TextView) findViewById(R.id.keyDownTime);
        m_plot.m_keyUpText = (TextView) findViewById(R.id.keyUpTime);
        m_plot.m_genericMotionText = (TextView) findViewById(R.id.genericMotionTime);

        mControllerViews = new View[4];
        mControllerViews[0] = findViewById(R.id.controllerView1);
        mControllerViews[1] = findViewById(R.id.controllerView2);
        mControllerViews[2] = findViewById(R.id.controllerView3);
        mControllerViews[3] = findViewById(R.id.controllerView4);
    }
    
    @Override
    public void onDestroy()
    {
    	super.onDestroy();   	

		OuyaPlotFPS plot = (OuyaPlotFPS) findViewById(R.id.ouyaPlotFPS1);
		plot.Quit();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
    	m_plot.m_keyDownTime = System.nanoTime() / 1000000000.0 - event.getEventTime() / 1000.0;
        View controllerView = getControllerView(event);
        controllerView.setVisibility(View.VISIBLE);
        return controllerView.onKeyDown(keyCode,  event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
    	m_plot.m_keyUpTime = System.nanoTime() / 1000000000.0 - event.getEventTime() / 1000.0;
        View controllerView = getControllerView(event);
        controllerView.setVisibility(View.VISIBLE);
        return controllerView.onKeyUp(keyCode,  event);
    }

    @Override
    public boolean onGenericMotionEvent(MotionEvent event) {
    	
    	m_plot.m_genericMotionTime = System.nanoTime() / 1000000000.0 - event.getEventTime() / 1000.0;
    	
        if((event.getSource() & InputDevice.SOURCE_CLASS_JOYSTICK) == 0){
            //Not a joystick movement, so ignore it.
            return false;
        }
        View controllerView = getControllerView(event);
        controllerView.setVisibility(View.VISIBLE);
        return controllerView.onGenericMotionEvent(event);
    }

    private View getControllerView(InputEvent event) {
        int playerNum = OuyaController.getPlayerNumByDeviceId(event.getDeviceId());
        if(playerNum >=0 && playerNum < mControllerViews.length) {
            return mControllerViews[playerNum];
        }
        return mControllerViews[0];
    }
}
