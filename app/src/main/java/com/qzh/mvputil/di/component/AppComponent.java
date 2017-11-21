package com.qzh.mvputil.di.component;

import android.content.Context;

import com.qzh.mvputil.api.ApiService;
import com.qzh.mvputil.di.module.AppModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Q_zh  on 6/6/17.
 */
@Singleton
@Component(modules = AppModule.class)
public interface AppComponent {
    /**
     * 提供Applicaiton的Context
     * @return
     */
    Context context();

    /**
     * 提供ApiService
     * @return
     */
    ApiService apiService();
}
