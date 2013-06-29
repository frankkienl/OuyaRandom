package tv.ouya.controllertest;

import java.io.IOException;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader.TileMode;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class OuyaPlotFPS extends View {

	private int m_plotSize = 128;
	public TextView m_fpsText = null;
	public TextView m_cpu1Text = null;
	public TextView m_cpu2Text = null;
	public TextView m_cpu3Text = null;
	public TextView m_cpu4Text = null;
	public TextView m_keyDownText = null;
	public TextView m_keyUpText = null;
	public TextView m_genericMotionText = null;
	private double[] mCpuStats = new double[4];

	public double m_keyDownTime = 0.0;
	public double m_keyUpTime = 0.0;
	public double m_genericMotionTime = 0.0;
	
	private MetricsCPU m_metricsCPU = null;

	private void Init()
        throws IOException
	{
		m_metricsCPU = new MetricsCPU();

		m_counts = new Double[m_plotSize];
		for (int index = 0; index < m_plotSize; ++index)
		{
			m_counts[index] = 0.0;
		}
	}
	
	public void Quit()
	{
        try {
            m_metricsCPU.close();
        } catch (IOException e) {
            Log.e("OUYAPlotFPS", "Error closing metrics stream", e);
        }
		Log.i("OuyaPlotFPS", "Quitting...", null);
	}
	
    public OuyaPlotFPS(Context context, AttributeSet attrs)
        throws IOException {
        super(context, attrs);
        Init();
    }

    public OuyaPlotFPS(Context context, AttributeSet attrs, int defStyle)
            throws IOException  {
        super(context, attrs, defStyle);
        Init();
    }

    public OuyaPlotFPS(Context context)
            throws IOException  {
        super(context);
        Init();
    }
    
    private long m_timer = -1;
    private Double[] m_counts = null;
    private int m_time = 0;
    
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		
		if (null == m_counts)
		{
			return;
		}
		
		//timer to update fps label
		if (m_timer < System.nanoTime())
		{
			m_timer = System.nanoTime() + 1000000000;
			if (null != m_fpsText)
			{
				m_fpsText.setText(String.format("FPS: %.2f", 1.0 / (System.nanoTime() / 1000000000.0 - getDrawingTime() / 1000.0)));
			}

            m_metricsCPU.readUsage(mCpuStats);
			if (null != m_cpu1Text)
			{
				m_cpu1Text.setText(String.format("CPU1: %.2f", mCpuStats[0]));
			}
			if (null != m_cpu2Text)
			{
				m_cpu2Text.setText(String.format("CPU2: %.2f", mCpuStats[1]));
			}
			if (null != m_cpu3Text)
			{
				m_cpu3Text.setText(String.format("CPU3: %.2f", mCpuStats[2]));
			}
			if (null != m_cpu4Text)
			{
				m_cpu4Text.setText(String.format("CPU4: %.2f", mCpuStats[3]));
			}
			if (null != m_keyDownText)
			{
				m_keyDownText.setText(String.format("KeyDown: %.2f ms", 1000.0 * m_keyDownTime));
			}
			if (null != m_keyUpText)
			{
				m_keyUpText.setText(String.format("KeyUp: %.2f ms", 1000.0 * m_keyUpTime));
			}
			if (null != m_genericMotionText)
			{
				m_genericMotionText.setText(String.format("Trackpad: %.2f ms", 1000.0 * m_genericMotionTime));
			}
		}
			
		m_counts[m_time] = 1.0 / (System.nanoTime() / 1000000000.0 - getDrawingTime() / 1000.0);
		++m_time;
		if (m_time >= m_plotSize)
		{
			m_time = 0;
		}
		
		/*
		drawBackground(canvas);
		
		Paint paint1 = new Paint();		
		paint1.setShader(new LinearGradient(0, 0, 4, canvas.getHeight() / 2, Color.argb(255, 255, 128, 0), Color.argb(255, 64, 0, 0), TileMode.MIRROR));
		
		Paint paint2 = new Paint();
		paint2.setShader(new LinearGradient(0, 0, 4, canvas.getHeight() / 2, Color.BLACK, Color.argb(255, 128, 255, 255), TileMode.MIRROR));
		
		Rect ourRect = new Rect();
		for (int index = 0; index < m_plotSize; ++index)
		{
			int readIndex = (index + m_time) % m_plotSize;
			Double size = m_counts[readIndex] / 4;
			ourRect.set(index*8, (int)(canvas.getHeight()), (index+1)*8, (int)(canvas.getHeight() - size));
			
			if ((readIndex/8 % 2) == 0)
			{
				canvas.drawRect(ourRect, paint1);
			}
			else
			{
				canvas.drawRect(ourRect, paint2);
			}
		}
		*/
		
		invalidate();		
		
	}
}
