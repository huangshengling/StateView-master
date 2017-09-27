package cn.holy.demo;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import cn.holy.stateview.StateLayout;


/**
 * author : Holy Spirit
 * time   : 2017/09/26
 * desc   :
 * version: 1.0
 */
public class SecondFragment extends Fragment {

    private StateLayout mMultiStateLayout;

    public static SecondFragment newInstance() {
        return new SecondFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_second, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mMultiStateLayout = (StateLayout) view.findViewById(R.id.multi_state_layout);
        mMultiStateLayout.setState(StateLayout.State.LOADING);

        mMultiStateLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                mMultiStateLayout.setState(StateLayout.State.NETWORK_ERROR);
            }
        }, 2000);

        View networkErrorView = mMultiStateLayout.getNetworkErrorView();
        if (null != networkErrorView) {
            ((TextView) networkErrorView.findViewById(R.id.tv_msg)).setText(R.string.mock_text);
            networkErrorView.findViewById(R.id.btn_reload).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mMultiStateLayout.setState(StateLayout.State.CONTENT);
                }
            });
        }
    }
}
