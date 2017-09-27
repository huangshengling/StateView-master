package cn.holy.demo;

import android.app.Application;

import java.util.ArrayList;
import java.util.List;

import cn.holy.stateview.StateConfiguration;
import cn.holy.stateview.StateLayout;


/**
 * author : Holy Spirit
 * time   : 2017/09/26
 * desc   :
 * version: 1.0
 */
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        List<Integer> list = new ArrayList<>();
        list.add(R.drawable.refreshing_image_frame_01);
        list.add(R.drawable.refreshing_image_frame_02);
        list.add(R.drawable.refreshing_image_frame_03);
        list.add(R.drawable.refreshing_image_frame_04);
        list.add(R.drawable.refreshing_image_frame_05);
        list.add(R.drawable.refreshing_image_frame_06);
        list.add(R.drawable.refreshing_image_frame_07);
        list.add(R.drawable.refreshing_image_frame_08);

        StateConfiguration.Builder builder = new StateConfiguration.Builder();
        builder.setCommonEmptyLayout(R.layout.layout_empty)
                .setCommonErrorLayout(R.layout.layout_error)
                .setCommonLoadingLayout(R.layout.layout_loading)
                .setDrawableList(list);
        StateLayout.setConfiguration(builder);
    }
}
