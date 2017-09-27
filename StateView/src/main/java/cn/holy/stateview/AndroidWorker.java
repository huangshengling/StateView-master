package cn.holy.stateview;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;

/**
 * author : Holy Spirit
 * time   : 2017/09/26
 * desc   :
 * version: 1.0
 */
public class AndroidWorker {

    public interface UiCallback {
        boolean handleUiMessage(Message msg);
    }

    public interface WorkerCallback {
        boolean handleWorkerMessage(Message msg);
    }

    private Handler mainThreadHandler = null;
    private Handler workerThreadHandler = null;
    private UiCallback uiCallback = null;
    private WorkerCallback workerCallback = null;
    private boolean isChannelOpened = false;

    public AndroidWorker(final UiCallback uiCallback, final WorkerCallback workerCallback) {
        this.uiCallback = uiCallback;
        this.workerCallback = workerCallback;
        open();
    }

    public Handler toUI() {
        return mainThreadHandler;
    }

    public Handler toWorker() {
        return workerThreadHandler;
    }

    public boolean open() {
        if (isChannelOpened) {
            return true;
        }
        if (uiCallback == null || workerCallback == null)
            return false;
        mainThreadHandler = new Handler(Looper.getMainLooper(), new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                return uiCallback.handleUiMessage(msg);
            }
        });
        HandlerThread workerThread = new HandlerThread("android-channel-worker-thread");
        workerThread.start();
        workerThreadHandler = new Handler(workerThread.getLooper(), new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                return workerCallback.handleWorkerMessage(msg);
            }
        });
        isChannelOpened = true;
        return true;
    }


}
