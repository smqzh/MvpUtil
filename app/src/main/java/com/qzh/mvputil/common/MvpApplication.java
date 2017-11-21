package com.qzh.mvputil.common;

import android.app.Application;
import android.content.Context;

import com.qzh.mvputil.di.component.AppComponent;
import com.qzh.mvputil.di.component.DaggerAppComponent;
import com.qzh.mvputil.di.module.AppModule;
import com.qzh.mvputil.ui.activity.LoginActivity;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;


/**
 * Created by Administrator on 2017/11/21 0021.
 */

public class MvpApplication extends Application {

    private static MvpApplication instance;
    /**
     *  activity管理类
     */
    private ActivityManager activityManager = null;
    private RefWatcher refWatcher;
    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return;
        }
        refWatcher = LeakCanary.install(this);
        // 获得activity管理类的实例
        getAppComponent();
        activityManager = ActivityManager.getInstance();
        //全局异常处理

    }
    public static RefWatcher getRefWatcher(Context context) {
        MvpApplication application = (MvpApplication) context.getApplicationContext();
        return application.refWatcher;
    }
    public static MvpApplication getInstance() {
        return instance;
    }


    public ActivityManager getActivityManager() {
        return activityManager;
    }

    public AppComponent getAppComponent(){
        return DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();
    }

    public void finishAllActivity(){
        activityManager.finishAllActivity();
    }

    public void finishExcepionLogin(){
        activityManager.finishAllActivityExceptOne(LoginActivity.class);
    }
}
