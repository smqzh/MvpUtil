package com.qzh.mvputil.mvp.model;


import com.qzh.mvputil.api.ApiEngine;
import com.qzh.mvputil.api.RetryWithDelay;
import com.qzh.mvputil.api.RxHelper;
import com.qzh.mvputil.base.BaseModel;
import com.qzh.mvputil.di.scope.ActivityScope;
import com.qzh.mvputil.entity.News;
import com.qzh.mvputil.entity.PageBean;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by lcx on 2017/6/5.
 */
@ActivityScope
public class NewsListModel implements BaseModel {

	public Observable<String> getNewsListModel(int pno, int ps, String key, String dtype){
		return ApiEngine.getInstance().getApiService()
				.getNewsList(pno,ps,key,dtype)
				.subscribeOn(Schedulers.io())
				.unsubscribeOn(Schedulers.io())
				.subscribeOn(AndroidSchedulers.mainThread())
				.observeOn(AndroidSchedulers.mainThread())
				.retryWhen(new RetryWithDelay(3,3000));

	}

}
