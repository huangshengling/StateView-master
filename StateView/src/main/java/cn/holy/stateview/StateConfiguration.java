package cn.holy.stateview;

import android.support.annotation.LayoutRes;

import java.util.List;

/**
 * author : Holy Spirit
 * time   : 2017/09/26
 * desc   :
 * version: 1.0
 */
public class StateConfiguration {

    public static class Builder {

        private int mEmptyResId   = -1;
        private int mErrorResId   = -1;
        private int mLoadingResId = -1;
        private int mNetworkErrorResId = -1;
        private int mAnimDuration = 300;
        private boolean mAnimEnable;
        private List<Integer> mDrawableList;

        public Builder() {
        }

        @SuppressWarnings("unused")
        public Builder setCommonEmptyLayout(@LayoutRes int resId) {
            mEmptyResId = resId;
            return this;
        }

        @SuppressWarnings("unused")
        public Builder setCommonLoadingLayout(@LayoutRes int resId) {
            mLoadingResId = resId;
            return this;
        }

        @SuppressWarnings("unused")
        public Builder setCommonErrorLayout(@LayoutRes int resId) {
            mErrorResId = resId;
            return this;
        }

        @SuppressWarnings("unused")
        public Builder setCommonNetworkErrorLayout(@LayoutRes int resId) {
            mNetworkErrorResId = resId;
            return this;
        }

        @SuppressWarnings("unused")
        public Builder setAnimDuration(int duration) {
            mAnimDuration = duration;
            return this;
        }

        @SuppressWarnings("unused")
        public Builder setAnimEnable(boolean animEnable) {
            mAnimEnable = animEnable;
            return this;
        }

        @SuppressWarnings("unused")
        public Builder setDrawableList(List<Integer> mDrawableList) {
            this.mDrawableList = mDrawableList;
            return this;
        }

        @SuppressWarnings("unused")
        public int getCommonEmptyLayout() {
            return mEmptyResId;
        }

        @SuppressWarnings("unused")
        public int getCommonLoadingLayout() {
            return mLoadingResId;
        }

        @SuppressWarnings("unused")
        public int getCommonErrorLayout() {
            return mErrorResId;
        }

        @SuppressWarnings("unused")
        public int getCommonNetworkErrorLayout() {
            return mNetworkErrorResId;
        }

        @SuppressWarnings("unused")
        public int getAnimDuration() {
            return mAnimDuration;
        }

        @SuppressWarnings("unused")
        public boolean isAnimEnable() {
            return mAnimEnable;
        }

        @SuppressWarnings("unused")
        public List<Integer> getDrawableList() {
            return mDrawableList;
        }
    }
}
