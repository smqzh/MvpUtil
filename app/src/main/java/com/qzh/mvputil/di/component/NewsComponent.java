package com.qzh.mvputil.di.component;

import com.qzh.mvputil.di.module.AppModule;
import com.qzh.mvputil.di.module.NewsModule;
import com.qzh.mvputil.ui.activity.MainActivity;

import dagger.Component;

/**
 * Created by Q_zh  on 6/6/17.
 */
@Component(modules = NewsModule.class)
public interface NewsComponent {

    void inject(MainActivity activity);
}
