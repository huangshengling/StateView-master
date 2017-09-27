package cn.holy.stateview;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.annotation.IntDef;
import android.support.annotation.LayoutRes;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.FrameLayout;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


/**
 * author : Holy Spirit
 * time   : 2017/09/26
 * desc   :
 * version: 1.0
 */
public class StateLayout extends FrameLayout {

    private static final int DEFAULT_ANIM_DURATION = 300;
    private static StateConfiguration.Builder mCommonConfiguration;
    private SparseArray<View> mCustomStateViewArray;

    private View mContentView;
    private View mLoadingView;
    private View mEmptyView;
    private View mErrorView;
    private View mNetworkErrorView;

    private int mEmptyResId;
    private int mErrorResId;
    private int mLoadingResId;
    private int mNetworkErrorResId;

    private int mAnimDuration;
    private boolean mAnimEnable;
    private LayoutInflater mInflater;

    private ObjectAnimator mAlphaAnimator;
    private TransitionAnimatorLoader mTransitionAnimatorLoader;
    private OnStateViewCreatedListener mOnStateViewCreatedListener;

    @IntDef({State.CONTENT, State.EMPTY, State.LOADING, State.ERROR, State.NETWORK_ERROR})
    @Retention(RetentionPolicy.SOURCE)
    public @interface State {
        int CONTENT = 0;
        int EMPTY = 1;
        int LOADING = 2;
        int ERROR = 3;
        int NETWORK_ERROR = 4;
    }

    private @State int mCurState = State.CONTENT;
    private int mCurCustomStateKey;
    private boolean mIsSystemState;

    public StateLayout(Context context) {
        this(context, null);
    }

    public StateLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        init(attrs);
    }

    public StateLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public StateLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.StateLayout);
        mEmptyResId = ta.getResourceId(R.styleable.StateLayout_layout_empty, getCommonLayoutResIdByState(State.EMPTY));
        mErrorResId = ta.getResourceId(R.styleable.StateLayout_layout_error, getCommonLayoutResIdByState(State.ERROR));
        mLoadingResId = ta.getResourceId(R.styleable.StateLayout_layout_loading, getCommonLayoutResIdByState(State.LOADING));
        mNetworkErrorResId = ta.getResourceId(R.styleable.StateLayout_layout_network_error, getCommonLayoutResIdByState(State.NETWORK_ERROR));

        mAnimEnable = ta.getBoolean(R.styleable.StateLayout_animEnable, isCommonAnimEnable());
        mAnimDuration = ta.getInt(R.styleable.StateLayout_animDuration, getCommonAnimDuration());
        ta.recycle();

        mInflater = LayoutInflater.from(getContext());
        mCustomStateViewArray = new SparseArray<>();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        if (getChildCount() > 1) {
            throw new IllegalStateException("You should add at least one layout");
        } else if (getChildCount() == 1) {
            mContentView = getChildAt(State.CONTENT);
        } else {
            mContentView = null;
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        clearTargetViewAnimation();
    }

    @SuppressWarnings("unused")
    public static void setConfiguration(StateConfiguration.Builder builder) {
        mCommonConfiguration = builder;
    }

    @SuppressWarnings("unused")
    public void setOnStateViewCreatedListener(OnStateViewCreatedListener l) {
        mOnStateViewCreatedListener = l;
    }

    @SuppressLint("Assert")
    public void setState(@State int state) {
        setState(state, false);
    }

    @SuppressLint("Assert")
    public void setState(@State int state, boolean displayContentLayout) {
        assert !(state < State.CONTENT || state > State.NETWORK_ERROR);
        clearTargetViewAnimation();
        if (mIsSystemState) {
            hideViewByState(mCurState, displayContentLayout);
        } else {
            hideCustomViewByState(mCurCustomStateKey, displayContentLayout);
        }
        showViewByState(state);
    }

    @SuppressWarnings("unused")
    public void setCustomState(int customStateKey) {
        setCustomState(customStateKey, false);
    }

    @SuppressWarnings("unused")
    public void setCustomState(int customStateKey, boolean displayContentLayout) {
        clearTargetViewAnimation();
        if (mIsSystemState) {
            hideViewByState(mCurState, displayContentLayout);
        } else {
            hideCustomViewByState(mCurCustomStateKey, displayContentLayout);
        }
        showCustomViewByState(customStateKey);
    }

    @SuppressWarnings("unused")
    public void putCustomStateView(int customStateKey, View stateView) {
        mCustomStateViewArray.put(customStateKey, stateView);
        addView(stateView, stateView.getLayoutParams());
        stateView.setVisibility(GONE);
    }

    @SuppressWarnings("unused")
    public int getState() {
        return mIsSystemState ? mCurState : mCurCustomStateKey;
    }

    @SuppressWarnings("unused")
    public boolean isCustomizeState() {
        return !mIsSystemState;
    }

    @SuppressWarnings("unused")
    public View findCustomStateViewByKey(int customStateKey) {
        return mCustomStateViewArray.get(customStateKey);
    }

    @SuppressWarnings("unused")
    public void setEmptyView(@LayoutRes int resId) {
        if (null != mEmptyView) {
            removeView(mEmptyView);
            mEmptyView = null;
        }
        mEmptyResId = resId;
    }

    @SuppressWarnings("unused")
    public void setEmptyView(View emptyView) {
        removeView(mEmptyView);
        mEmptyView = emptyView;
        mEmptyView.setVisibility(GONE);
        addView(mEmptyView);
    }

    @SuppressWarnings("unused")
    public View getEmptyView() {
        if (null == mEmptyView) {
            if (mEmptyResId > -1) {
                mEmptyView = mInflater.inflate(mEmptyResId, this, false);
                addView(mEmptyView, mEmptyView.getLayoutParams());
                mEmptyView.setVisibility(GONE);
            }
        }
        return mEmptyView;
    }

    @SuppressWarnings("unused")
    public void setLoadingView(@LayoutRes int resId) {
        if (null != mLoadingView) {
            removeView(mLoadingView);
            mLoadingView = null;
        }
        mLoadingResId = resId;
    }

    @SuppressWarnings("unused")
    public void setLoadingView(View loadingView) {
        removeView(mLoadingView);
        mLoadingView = loadingView;
        mLoadingView.setVisibility(GONE);
        addView(mLoadingView);
    }

    @SuppressWarnings("unused")
    public View getLoadingView() {
        if (null == mLoadingView) {
            if (mLoadingResId > -1) {
                mLoadingView = mInflater.inflate(mLoadingResId, this, false);

                addView(mLoadingView, mLoadingView.getLayoutParams());
                mLoadingView.setVisibility(GONE);
            }
        }
        return mLoadingView;
    }

    @SuppressWarnings("unused")
    public void setErrorView(@LayoutRes int resId) {
        if (null != mErrorView) {
            removeView(mErrorView);
            mErrorView = null;
        }
        mErrorResId = resId;
    }

    @SuppressWarnings("unused")
    public void setErrorView(View errorView) {
        removeView(mErrorView);
        mErrorView = errorView;
        mErrorView.setVisibility(GONE);
        addView(mErrorView);
    }

    @SuppressWarnings("unused")
    public View getErrorView() {
        if (null == mErrorView) {
            if (mErrorResId > -1) {
                mErrorView = mInflater.inflate(mErrorResId, this, false);
                addView(mErrorView, mErrorView.getLayoutParams());
                mErrorView.setVisibility(GONE);
            }
        }
        return mErrorView;
    }

    @SuppressWarnings("unused")
    public void setNetworkErrorView(@LayoutRes int resId) {
        if (null != mNetworkErrorView) {
            removeView(mNetworkErrorView);
            mNetworkErrorView = null;
        }
        mNetworkErrorResId = resId;
    }

    @SuppressWarnings("unused")
    public void setNetworkErrorView(View networkErrorView) {
        removeView(mNetworkErrorView);
        mNetworkErrorView = networkErrorView;
        mNetworkErrorView.setVisibility(GONE);
        addView(mNetworkErrorView);
    }

    @SuppressWarnings("unused")
    public View getNetworkErrorView() {
        if (null == mNetworkErrorView) {
            if (mNetworkErrorResId > -1) {
                mNetworkErrorView = mInflater.inflate(mNetworkErrorResId, this, false);
                addView(mNetworkErrorView, mNetworkErrorView.getLayoutParams());
                mNetworkErrorView.setVisibility(GONE);
            }
        }
        return mNetworkErrorView;
    }

    @SuppressWarnings("unused")
    public void setAnimEnable(boolean animEnable) {
        mAnimEnable = animEnable;
    }

    @SuppressWarnings("unused")
    public boolean isAnimEnable() {
        return mAnimEnable;
    }

    @SuppressWarnings("unused")
    public void setAnimDuration(int duration) {
        mAnimDuration = duration;
    }

    @SuppressWarnings("unused")
    public int getAnimDuration() {
        return mAnimDuration;
    }

    @SuppressWarnings("unused")
    public void setTransitionAnimator(TransitionAnimatorLoader animatorLoader) {
        mTransitionAnimatorLoader = animatorLoader;
    }
    private void clearTargetViewAnimation() {
        if (null != mAlphaAnimator && mAlphaAnimator.isRunning()) {
            mAlphaAnimator.cancel();
        }
    }

    private void hideViewByState(@State int state, boolean displayContentLayout) {
        if (null != mContentView) {
            mContentView.setVisibility(displayContentLayout ? VISIBLE : GONE);
        }

        switch (state) {
            case State.CONTENT:
                break;
            case State.EMPTY:
                if (null != mEmptyView) {
                    mEmptyView.setVisibility(GONE);
                }
                break;
            case State.LOADING:
                if (null != mLoadingView) {
                    if (mLoadingView instanceof StateImageView) {
                        ((StateImageView)mLoadingView).stopAnimation();
                    }
                    mLoadingView.setVisibility(GONE);
                }
                break;
            case State.ERROR:
                if (null != mErrorView) {
                    mErrorView.setVisibility(GONE);
                }
                break;
            case State.NETWORK_ERROR:
                if (null != mNetworkErrorView) {
                    mNetworkErrorView.setVisibility(GONE);
                }
                break;
            default:
                break;
        }
    }

    private void hideCustomViewByState(int customStateKey, boolean displayContentLayout) {
        if (null != mContentView) {
            mContentView.setVisibility(displayContentLayout ? VISIBLE : GONE);
        }

        View customStateView = findCustomStateViewByKey(customStateKey);
        if (null != customStateView) {
            customStateView.setVisibility(GONE);
        }
    }

    private void showViewByState(@State int state) {
        switch (state) {
            case State.CONTENT:
                showContentView();
                break;
            case State.EMPTY:
                showEmptyView();
                break;
            case State.LOADING:
                showLoadingView();
                break;
            case State.ERROR:
                showErrorView();
                break;
            case State.NETWORK_ERROR:
                showNetworkErrorView();
                break;
            default:
                break;
        }
        mCurState = state;
        mIsSystemState = true;
    }

    private void showCustomViewByState(int customStateKey) {
        View customStateView = findCustomStateViewByKey(customStateKey);
        if (null != customStateView) {
            customStateView.setVisibility(VISIBLE);
            if (mAnimEnable) {
                execAlphaAnimation(customStateView);
            }
        }

        mCurCustomStateKey = customStateKey;
        mIsSystemState = false;
    }

    private void execAlphaAnimation(View targetView) {
        if (null == targetView) {
            return;
        }

        if (null == mTransitionAnimatorLoader || null == mTransitionAnimatorLoader.onCreateAnimator(targetView)) {
            mAlphaAnimator = ObjectAnimator.ofFloat(targetView, "alpha", 0.0f, 1.0f);
            mAlphaAnimator.setInterpolator(new AccelerateInterpolator());
            mAlphaAnimator.setDuration(mAnimDuration);
        } else {
            mAlphaAnimator = mTransitionAnimatorLoader.onCreateAnimator(targetView);
        }
        mAlphaAnimator.start();
    }

    private void showContentView() {
        if (null != mContentView) {
            mContentView.setVisibility(VISIBLE);
        }
    }

    private void showEmptyView() {
        if (null == mEmptyView && mEmptyResId > -1) {
            mEmptyView = mInflater.inflate(mEmptyResId, this, false);
            addView(mEmptyView, mEmptyView.getLayoutParams());
            callViewCreated(mEmptyView, State.EMPTY);
        }
        if (null != mEmptyView) {
            mEmptyView.setVisibility(VISIBLE);
            if (mAnimEnable) {
                execAlphaAnimation(mEmptyView);
            }
        } else {
            throw new NullPointerException("No empty layout is set");
        }
    }

    private void showLoadingView() {
        if (null == mLoadingView && mLoadingResId > -1) {
            mLoadingView = mInflater.inflate(mLoadingResId, this, false);
            if (mLoadingView instanceof StateImageView) {
                if (mCommonConfiguration.getDrawableList() != null && mCommonConfiguration.getDrawableList().size() > 0) {
                    for (int i = 0; i < mCommonConfiguration.getDrawableList().size(); i++) {
                        ((StateImageView)mLoadingView).addImageFrame(mCommonConfiguration.getDrawableList().get(i));
                    }
                }else{
                    throw new NullPointerException("You should add at least one picture");
                }
            }
            addView(mLoadingView, mLoadingView.getLayoutParams());
            callViewCreated(mLoadingView, State.LOADING);
        }
        if (null != mLoadingView) {
            mLoadingView.setVisibility(VISIBLE);
            if (mAnimEnable) {
                execAlphaAnimation(mLoadingView);
            }
            if (mLoadingView instanceof StateImageView) {
                ((StateImageView)mLoadingView).startAnimation();
            }

        } else {
            throw new NullPointerException("No load layout is set");
        }
    }

    private void showErrorView() {
        if (null == mErrorView && mErrorResId > -1) {
            mErrorView = mInflater.inflate(mErrorResId, this, false);
            addView(mErrorView, mErrorView.getLayoutParams());
            callViewCreated(mErrorView, State.ERROR);
        }
        if (null != mErrorView) {
            mErrorView.setVisibility(VISIBLE);
            if (mAnimEnable) {
                execAlphaAnimation(mErrorView);
            }
        } else {
            throw new NullPointerException("No error layout is set");
        }
    }

    private void showNetworkErrorView() {
        if (null == mNetworkErrorView && mNetworkErrorResId > -1) {
            mNetworkErrorView = mInflater.inflate(mNetworkErrorResId, this, false);
            addView(mNetworkErrorView, mNetworkErrorView.getLayoutParams());
            callViewCreated(mNetworkErrorView, State.NETWORK_ERROR);
        }
        if (null != mNetworkErrorView) {
            mNetworkErrorView.setVisibility(VISIBLE);
            if (mAnimEnable) {
                execAlphaAnimation(mNetworkErrorView);
            }
        } else {
            throw new NullPointerException("No network exception layout was set");
        }
    }

    private int getCommonLayoutResIdByState(@State int state) {
        switch (state) {
            case State.EMPTY:
                return null == mCommonConfiguration ? -1 : mCommonConfiguration.getCommonEmptyLayout();
            case State.LOADING:
                return null == mCommonConfiguration ? -1 : mCommonConfiguration.getCommonLoadingLayout();
            case State.ERROR:
                return null == mCommonConfiguration ? -1 : mCommonConfiguration.getCommonErrorLayout();
            case State.NETWORK_ERROR:
                return null == mCommonConfiguration ? -1 : mCommonConfiguration.getCommonNetworkErrorLayout();
            case State.CONTENT:
                return -1;
        }
        return 0;
    }

    private boolean isCommonAnimEnable() {
        return null != mCommonConfiguration && mCommonConfiguration.isAnimEnable();
    }

    private int getCommonAnimDuration() {
        return null == mCommonConfiguration ? DEFAULT_ANIM_DURATION : mCommonConfiguration.getAnimDuration();
    }

    private void callViewCreated(View view, int state) {
        if (null != mOnStateViewCreatedListener) {
            mOnStateViewCreatedListener.onViewCreated(view, state);
        }
    }

    public interface TransitionAnimatorLoader {
        ObjectAnimator onCreateAnimator(View targetView);
    }

    public interface OnStateViewCreatedListener {

        void onViewCreated(View view, int state);
    }
}
