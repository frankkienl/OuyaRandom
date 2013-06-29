package tv.ouya.controllertest;

import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public class MetricsCPU
{
    /**
     * The line starts to look for
     */

    private static final char[] INDEX_IDS = { '0', '1', '2', '3' };

    /**
     * The reader for CPU stats
     */
    private RandomAccessFile mReader = null;

	private long[] mUser;
    private long[] mSystem;
    private long[] mTotal;
	
	public MetricsCPU()
        throws IOException
	{
		mUser = new long[] {0, 0, 0, 0};
		mSystem = new long[] {0, 0, 0, 0};
		mTotal = new long[] {0, 0, 0, 0};

        String fileName = "/proc/stat";
        File f = new File(fileName);
        if (!f.exists()) {
            throw new RuntimeException("/proc/stat unavailable");
        }

        mReader = new RandomAccessFile(fileName, "r");
	}
	
    public synchronized void readUsage(double[] mCpuStats)
    {
        try {
            mReader.seek(0L);

            int filled = 0;
            String line;
            while ((line = mReader.readLine()) != null && filled < 4)
            {
                if (line.startsWith("cpu"))
                {
                    for(int i = 0 ; i < INDEX_IDS.length ; i++) {
                        if(line.charAt(3) == INDEX_IDS[i]) {
                            mCpuStats[i] = updateStats(line.trim().split("[ ]+"), i);
                            filled++;
                            break;
                        }
                    }
                }
            }
        } catch (IOException ex) {
            Log.i("MetricsCPU", "Problem reading metrics", ex);
        }
    }

    public synchronized void close()
        throws IOException {
        if(mReader != null) {
            mReader.close();
        }
    }
    
    private double updateStats(String[] segs, int index)
    {
        // user = user + nice
        long user = Long.parseLong(segs[1]) + Long.parseLong(segs[2]);
        // system = system + intr + soft_irq
        long system = Long.parseLong(segs[3]) + Long.parseLong(segs[6]) + Long.parseLong(segs[7]);
        // total = user + system + idle + io_wait
        long total = user + system + Long.parseLong(segs[4]) + Long.parseLong(segs[5]);

        if (mTotal[index] != 0 || total >= mTotal[index])
        {
                long duser = user - mUser[index];
                long dsystem = system - mSystem[index];
                long dtotal = total - mTotal[index];
                return (double)(duser+dsystem)*100.0/dtotal;                
        }
        mUser[index] = user;
        mSystem[index] = system;
        mTotal[index] = total;
        
        return 0.0;
    }
}