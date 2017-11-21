package com.qzh.mvputil.di.module;

import com.qzh.mvputil.mvp.model.NewsListModel;
import com.qzh.mvputil.mvp.presenter.NewsListPresenter;
import com.qzh.mvputil.mvp.view.ViewContract;

import dagger.Module;
import dagger.Provides;

/**
 * Created by fjlcx on 6/6/17.
 */
@Module
public class NewsModule {
    private ViewContract.NewsListView mView;

    public NewsModule(ViewContract.NewsListView view) {
        this.mView = view;
    }
    @Provides
    public NewsListPresenter getNewsListPresenter(NewsListModel model){
        return new NewsListPresenter(model,mView);
    }

    @Provides
    public NewsListModel getNewsListModel(){
        return new NewsListModel();
    }

}
