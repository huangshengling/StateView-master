package cn.holy.stateview;

import android.os.Message;

/**
 * author : Holy Spirit
 * time   : 2017/09/26
 * desc   :
 * version: 1.0
 */
public class AndroidTimer {
    private final int START_TIMER = 0;
    private final int STOP_TIMER  = 1;

    private AndroidWorker androidChannel;
    private int interval=0;
    private OnTimer onTimer;

    volatile  boolean loop = false;

    public AndroidTimer(int interval, OnTimer onTimer) {
        this.interval = (interval < 0) ? (interval*-1) : (interval);
        this.onTimer = onTimer;

        androidChannel = new AndroidWorker(new AndroidWorker.UiCallback() {
            @Override
            public boolean handleUiMessage(Message msg) {
                AndroidTimer.this.onTimer.onTime(AndroidTimer.this);
                return false;
            }
        }, new AndroidWorker.WorkerCallback() {


            Thread jobThread = null;

            @Override
            public boolean handleWorkerMessage(Message msg) {

                switch (msg.what) {
                    case START_TIMER:
                        loop = true;
                        break;
                    case STOP_TIMER:
                        loop = false;
                        jobThread = null;
                        break;
                }

                if(msg.what == START_TIMER && jobThread == null) {
                    jobThread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            while (loop) {
                                try {
                                    Thread.sleep(AndroidTimer.this.interval);
                                    Message msg = androidChannel.toUI().obtainMessage();
                                    androidChannel.toUI().sendMessage(msg);
                                } catch (InterruptedException e) {
                                }
                            }
                        }
                    });
                }

                switch (msg.what) {
                    case START_TIMER:
                        loop = true;
                        jobThread.start();
                        break;
                    case STOP_TIMER:
                        loop = false;
                        jobThread = null;
                        break;
                }


                return false;
            }
        });
    }


    public void start() {
        androidChannel.toWorker().sendEmptyMessage(START_TIMER);
    }

    public void stop() {
        androidChannel.toWorker().sendEmptyMessage(STOP_TIMER);
    }

    public boolean isAlive() {
        return loop;
    }

    public interface OnTimer {
        void onTime(AndroidTimer timer);
    }
}