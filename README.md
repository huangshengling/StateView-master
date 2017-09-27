A customize state layout for Android.

![](https://github.com/huangshengling/StateView-master/blob/master/art/screen_shot.gif?raw=true)

### Usage

##### Edit your layout XML:
<cn.holy.stateview.StateLayout
        android:id="@+id/state_layout"
        state:layout_network_error="@layout/layout_custom_network_error"
        state:animEnable="true"
        state:animDuration="300">

        <TextView
            android:id="@+id/tv_content"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_gravity="center"
            android:text="Go to second"/>

    </cn.holy.stateview.StateLayout>

##### Common Configuration

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
                       .setCommonLoadingLayout(R.layout.layout_loading).setDrawableList(list);
                StateLayout.setConfiguration(builder);
    }
}

##### How to change state?

mStateLayout.setState(StateLayout.State.CONTENT);

mStateLayout.setState(StateLayout.State.EMPTY);

mStateLayout.setState(StateLayout.State.LOADING);

mStateLayout.setState(StateLayout.State.ERROR);

mStateLayout.setState(StateLayout.State.NETWORK_ERROR);

mStateLayout.setOnStateViewCreatedListener(new OnStateViewCreatedListener() {

    @Override
    public void onViewCreated(View view, int state) {
        switch (state) {
            case StateLayout.State.NETWORK_ERROR:
                view.findViewById(R.id.btn_reload).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                    }
                });
                break;
            ...
            default:
                break;
        }
    }
});

##### How to add customize state view?

View customStateView = LayoutInflater.from(this).inflate(R.layout.layout_custom_notice, mStateLayout, false);
mStateLayout.putCustomStateView(KEY_CUSTOM_STATE, customStateView);

##### Show customize state view.

mStateLayout.setCustomState(KEY_CUSTOM_STATE);


##### How to customise transition animation?
mStateLayout.setTransitionAnimator(

new TransitionAnimatorLoader() {

    @Override
    public ObjectAnimator loadAnimator(View targetView) {
        ObjectAnimator customAnimator = ObjectAnimator.ofFloat(targetView, "alpha", 0.0f, 1.0f)
                                                      .setDuration(500);
        customAnimator.setInterpolator(new AccelerateInterpolator());
        return customAnimator;
    }
});

##### 如何添加自定义的状态视图?

        View viewCustomNotice = LayoutInflater.from(this).inflate(R.layout.layout_custom_notice, mStateLayout, false);
        mStateLayout.putCustomStateView(KEY_CUSTOM_NOTICE, viewCustomNotice);

##### 切换至自定义状态

mStateLayout.setCustomState(KEY_CUSTOM_STATE);



##### 如何切换自定义状态时的过渡动画?

mStateLayout.setTransitionAnimator(

new TransitionAnimatorLoader() {

    @Override
    public ObjectAnimator loadAnimator(View targetView) {
        ObjectAnimator customAnimator = ObjectAnimator.ofFloat(targetView, "alpha", 0.0f, 1.0f)
                                                      .setDuration(500);
        customAnimator.setInterpolator(new AccelerateInterpolator());
        return customAnimator;
    }
});
