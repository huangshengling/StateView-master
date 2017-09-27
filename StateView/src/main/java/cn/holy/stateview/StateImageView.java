package cn.holy.stateview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

import java.util.ArrayList;


/**
 * author : Holy Spirit
 * time   : 2017/09/26
 * desc   :
 * version: 1.0
 */
public class StateImageView extends ImageView {

    interface OnStartAnimationListener {
        void onStartAnimation();
    }

    interface OnFinishAnimationListener {
        void onFinishAnimation(boolean isLoopAnimation);
    }

    interface OnFrameChangedListener {
        void onFrameChanged(int index);
    }

    private final static int DEFAULT_INTERVAL = 100;

    private AndroidTimer timer;
    private int interval = DEFAULT_INTERVAL;

    private ArrayList<Drawable> drawableList;
    private int currentFrameIndex = -1;
    private boolean loop = true;
    private boolean didStoppedAnimation = true;
    private int animationRepeatCount = 1;
    boolean restoreFirstFrameWhenFinishAnimation = true;

    private OnStartAnimationListener startAnimationListener = null;
    private OnFrameChangedListener frameChangedListener = null;
    private OnFinishAnimationListener finishAnimationListener = null;

    public StateImageView(Context context) {
        this(context, null);
    }

    public StateImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StateImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    public void addImageFrame(int resId) {
        if (drawableList == null) {
            drawableList = new ArrayList<>();
            setImageDrawable(getContext().getResources().getDrawable(resId));
        }
        drawableList.add(getContext().getResources().getDrawable(resId));

    }

    public void addImageFrame(Drawable drawable) {
        if (drawableList == null) {
            drawableList = new ArrayList<>();
            setImageDrawable(drawable);
        }
        drawableList.add(drawable);
    }

    public void addImageFrame(Bitmap bitmap) {
        Drawable bitmapDrawable = new BitmapDrawable(getResources(), bitmap);
        addImageFrame(bitmapDrawable);
    }

    public void startAnimation() {
        if (drawableList == null || drawableList.size() == 0) {
            throw new IllegalStateException("You should add at least one picture");
        }

        if (!didStoppedAnimation) return;

        didStoppedAnimation = false;

        if (startAnimationListener != null) {
            startAnimationListener.onStartAnimation();
        }

        if (currentFrameIndex == -1) {
            currentFrameIndex = 0;
        }
        setImageDrawable(drawableList.get(currentFrameIndex));

        if (timer == null) {
            timer = new AndroidTimer(interval, new AndroidTimer.OnTimer() {
                @Override
                public void onTime(AndroidTimer timer) {
                    if (didStoppedAnimation) return;

                    currentFrameIndex++;
                    if (currentFrameIndex == drawableList.size()) {
                        if (loop) {
                            if (finishAnimationListener != null) {
                                finishAnimationListener.onFinishAnimation(true);
                            }
                            currentFrameIndex = 0;
                        } else {
                            animationRepeatCount--;

                            if (animationRepeatCount <= 0) {
                                currentFrameIndex = drawableList.size() - 1;

                                stopAnimation();

                                if (finishAnimationListener != null) {
                                    finishAnimationListener.onFinishAnimation(loop);
                                }

                            } else {
                                currentFrameIndex = 0;
                            }
                        }
                    }

                    if (!didStoppedAnimation) {
                        if (frameChangedListener != null) {
                            frameChangedListener.onFrameChanged(currentFrameIndex);
                        }
                        setImageDrawable(drawableList.get(currentFrameIndex));
                    } else {
                        if (restoreFirstFrameWhenFinishAnimation) {
                            setImageDrawable(drawableList.get(0));
                        }
                        currentFrameIndex = -1;
                    }
                }
            });
            timer.stop();
        }
        if (!timer.isAlive()) {
            timer.start();
        }
    }

    public boolean isAnimating() {
        return !didStoppedAnimation;
    }

    public void stopAnimation() {
        if (timer != null && timer.isAlive()) {
            timer.stop();
        }
        timer = null;
        didStoppedAnimation = true;

    }

    public void setLoop(boolean loop) {
        this.loop = loop;
    }

    public void setRestoreFirstFrameWhenFinishAnimation(boolean restore) {
        this.restoreFirstFrameWhenFinishAnimation = restore;
    }

    public void setAnimationRepeatCount(int animationRepeatCount) {
        this.animationRepeatCount = animationRepeatCount;
    }

    public void reset() {
        stopAnimation();
        if (drawableList != null) {
            drawableList.clear();
            drawableList = null;
        }
        currentFrameIndex = -1;
    }

    public void setOnStartAnimationListener(OnStartAnimationListener listener) {
        startAnimationListener = listener;
    }

    public void setOnFrameChangedListener(OnFrameChangedListener listener) {
        frameChangedListener = listener;
    }

    public void setOnFinishAnimationListener(OnFinishAnimationListener listener) {
        finishAnimationListener = listener;
    }
}
