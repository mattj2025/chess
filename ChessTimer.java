import java.awt.event.*;
import java.util.Date;
import javax.swing.*;

public class ChessTimer extends java.util.Timer
{
    private long delay;
    private long initialDelay;
    private final ActionListener listener;
    private final Object queueLock = new Object();
    private Task task;
    private long queue;
    private long endTime;

    public ChessTimer(long d, ActionListener l)
    {
        super();

        initialDelay = delay = d;
        listener = l;
    }
    
    public void setDelay(int d)
    {
        if (d < 0) 
            throw new IllegalArgumentException("Invalid delay: " + d);
        initialDelay = delay = d;
    }

    public long getDelay()
    {
        return delay;
    }

    public long getEndTime()
    {
        return endTime;
    }

    public boolean isRunning()
    {
        return task != null;
    }

    public void start()
    {
        Task t = task;
        if (t == null)
        {
            t = new Task();
            schedule(t, delay);
            task = t;
        }
        endTime = new Date().getTime() + delay;
    }

    public void stop()
    {
        delay = endTime - new Date().getTime();
        Task t = task;
        if (t != null)
        {
            t.cancel();
            task = null;
        }
    }

    public void reset()
    {
        stop();
        delay = initialDelay;
    }

    void queueEvent()
    {
        synchronized(queueLock)
        {
            queue++;
            if (queue == 1)
                SwingUtilities.invokeLater(drainer);
        }
    }

    void drainEvents()
    {
        synchronized (queueLock)
        {
        while (queue > 0)
        {
            listener.actionPerformed(new ActionEvent(new Object(), 0, null));
            queue--;
        }
        queue = 0;
        }
    }

    private class Task extends java.util.TimerTask
    {
        @Override
        public void run()
        {
            queueEvent();
            task = null;
        }
    }

    private final Runnable drainer = () -> {
        drainEvents();
    };
}