package in.appnow.astrobuddy.helper;

import java.util.TimerTask;

import in.appnow.astrobuddy.interfaces.OnTimerChangeListener;

/**
 * Created by sonu on 14/02/18.
 */

public class MyTimerTask extends TimerTask {
    private int timeCounter = 0;
    private OnTimerChangeListener onTimerChangeListener;

    public  MyTimerTask(int timeCounter, OnTimerChangeListener onTimerChangeListener) {
        this.timeCounter = timeCounter;
        this.onTimerChangeListener = onTimerChangeListener;
    }

    @Override
    public void run() {
        if (timeCounter == 0) {
            onTimerChangeListener.stopTimer();
            onTimerChangeListener.updateTimerProgress(timeCounter);
            return;
        }
        onTimerChangeListener.updateTimerProgress(timeCounter);
        timeCounter--;
    }
}