# MultiStateLayout
[![](https://jitpack.io/v/andyxialm/MultiStateLayout.svg)](https://jitpack.io/#andyxialm/MultiStateLayout)

(中文版本请参看[这里](#README_cn))

A customize multiple state layout for Android.

![](https://github.com/andyxialm/MultiStateLayout/blob/master/art/screenshot.gif?raw=true)
### Usage

#### Gradle
##### Step 1. Add the JitPack repository to your build file
~~~ xml
allprojects {
    repositories {
        ...
        maven { url "https://jitpack.io" }
    }
}
~~~

##### Step 2. Add the dependency
~~~ xml
dependencies {
    compile 'com.github.andyxialm:MultiStateLayout:0.1.0'
}
~~~

#### Maven
##### Step 1. Add the JitPack repository to your build file
~~~ xml
<repositories>
    <repository>
        <id>jitpack.io</id>
	<url>https://jitpack.io</url>
    </repository>
</repositories>
~~~

##### Step 2. Add the dependency
~~~ xml
<dependency>
    <groupId>com.github.andyxialm</groupId>
    <artifactId>MultiStateLayout</artifactId>
    <version>0.1.0</version>
</dependency>
~~~

##### Edit your layout XML:

~~~ xml
<cn.refactor.multistatelayout.MultiStateLayout
    android:id="@+id/multi_state_layout"
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:state="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    state:layout_network_error="@layout/layout_custom_network_error"
    state:animEnable="true"
    state:animDuration="500">

    <!-- content layout -->
    <TextView
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_gravity="center"
        android:text="Hello World!"/>

</cn.refactor.multistatelayout.MultiStateLayout>
~~~

##### Common Configuration
```java
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        MultiStateConfiguration.Builder builder = new MultiStateConfiguration.Builder();
        builder.setCommonEmptyLayout(R.layout.layout_empty)
               .setCommonErrorLayout(R.layout.layout_error)
               .setCommonLoadingLayout(R.layout.layout_loading);
        MultiStateLayout.setConfiguration(builder);
    }
}
```

##### How to change state?
```java

mMultiStateLayout.setState(MultiStateLayout.State.CONTENT);

mMultiStateLayout.setState(MultiStateLayout.State.EMPTY);

mMultiStateLayout.setState(MultiStateLayout.State.LOADING);

mMultiStateLayout.setState(MultiStateLayout.State.ERROR);

mMultiStateLayout.setState(MultiStateLayout.State.NETWORK_ERROR);

mMultiStateLayout.setOnStateViewCreatedListener(new OnStateViewCreatedListener() {
    @Override
    public void onViewCreated(View view, int state) {
        switch (state) {
            case MultiStateLayout.State.NETWORK_ERROR:
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

```

##### How to add customize state view?
```java
View customStateView = LayoutInflater.from(this).inflate(R.layout.layout_custom_notice, mStateLayout, false);
mStateLayout.putCustomStateView(KEY_CUSTOM_STATE, customStateView);
```
##### Show customize state view.
```java
mStateLayout.setCustomState(KEY_CUSTOM_STATE);
```

##### How to customise transition animation?
```java
mStateLayout.setTransitionAnimator(new TransitionAnimatorLoader() {
    @Override
    public ObjectAnimator loadAnimator(View targetView) {
        ObjectAnimator customAnimator = ObjectAnimator.ofFloat(targetView, "alpha", 0.0f, 1.0f)
                                                      .setDuration(500);
        customAnimator.setInterpolator(new AccelerateInterpolator());
        return customAnimator;
    }
});
```

------------------------------
## <a name="README_cn">MultiStateLayout</a>

[![](https://jitpack.io/v/andyxialm/MultiStateLayout.svg)](https://jitpack.io/#andyxialm/MultiStateLayout)

可支持自定义状态的多状态视图组件。

![](https://github.com/andyxialm/MultiStateLayout/blob/master/art/screenshot.gif?raw=true)
### 引入方式

#### Gradle
##### Step 1. Add the JitPack repository to your build file
~~~ xml
allprojects {
    repositories {
        ...
        maven { url "https://jitpack.io" }
    }
}
~~~

##### Step 2. Add the dependency
~~~ xml
dependencies {
    compile 'com.github.andyxialm:MultiStateLayout:0.1.0'
}
~~~

#### Maven
##### Step 1. Add the JitPack repository to your build file
~~~ xml
<repositories>
    <repository>
        <id>jitpack.io</id>
	<url>https://jitpack.io</url>
    </repository>
</repositories>
~~~

##### Step 2. Add the dependency
~~~ xml
<dependency>
    <groupId>com.github.andyxialm</groupId>
    <artifactId>MultiStateLayout</artifactId>
    <version>0.1.0</version>
</dependency>
~~~

##### 布局文件中增加 MultiStateLayout，包裹内容布局:

~~~ xml
<cn.refactor.multistatelayout.MultiStateLayout
    android:id="@+id/multi_state_layout"
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:state="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    state:layout_network_error="@layout/layout_custom_network_error"
    state:animEnable="true"
    state:animDuration="500">

    <!-- content layout -->
    <TextView
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_gravity="center"
        android:text="Hello World!"/>

</cn.refactor.multistatelayout.MultiStateLayout>
~~~

##### 配置公共属性
```java
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        MultiStateConfiguration.Builder builder = new MultiStateConfiguration.Builder();
        builder.setCommonEmptyLayout(R.layout.layout_empty)
               .setCommonErrorLayout(R.layout.layout_error)
               .setCommonLoadingLayout(R.layout.layout_loading);
        MultiStateLayout.setConfiguration(builder);
    }
}
```

##### 如何切换状态?
```java

mMultiStateLayout.setState(MultiStateLayout.State.CONTENT);

mMultiStateLayout.setState(MultiStateLayout.State.EMPTY);

mMultiStateLayout.setState(MultiStateLayout.State.LOADING);

mMultiStateLayout.setState(MultiStateLayout.State.ERROR);

mMultiStateLayout.setState(MultiStateLayout.State.NETWORK_ERROR);

mMultiStateLayout.setOnStateViewCreatedListener(new OnStateViewCreatedListener() {
    @Override
    public void onViewCreated(View view, int state) {
        switch (state) {
            case MultiStateLayout.State.NETWORK_ERROR:
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

```

##### 如何添加自定义的状态视图?
```java
View customStateView = LayoutInflater.from(this).inflate(R.layout.layout_custom_notice, mStateLayout, false);
mStateLayout.putCustomStateView(KEY_CUSTOM_STATE, customStateView);
```
##### 切换至自定义状态
```java
mStateLayout.setCustomState(KEY_CUSTOM_STATE);
```

##### 如何自定义切换状态时的过渡动画?
```java
mStateLayout.setTransitionAnimator(new TransitionAnimatorLoader() {
    @Override
    public ObjectAnimator loadAnimator(View targetView) {
        ObjectAnimator customAnimator = ObjectAnimator.ofFloat(targetView, "alpha", 0.0f, 1.0f)
                                                      .setDuration(500);
        customAnimator.setInterpolator(new AccelerateInterpolator());
        return customAnimator;
    }
});
```

### License

    Copyright 2017 andy (https://github.com/andyxialm)

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.