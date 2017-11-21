package com.qzh.mvputil.di.module;

import android.content.Context;

import com.qzh.mvputil.api.ApiEngine;
import com.qzh.mvputil.api.ApiService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * @author ling_cx
 * @date 2017/5/4.
 */
@Module
public class AppModule {
    private Context context;

    public AppModule(Context context) {
        this.context = context;
    }

    @Provides
    @Singleton
    Context provideApplicationContext() {
        return context;
    }


    @Provides
    @Singleton
    public ApiService provideApiService() {
        return ApiEngine.getInstance().getApiService();
    }
}
