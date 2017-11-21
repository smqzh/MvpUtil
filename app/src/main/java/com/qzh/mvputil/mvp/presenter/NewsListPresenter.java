package com.qzh.mvputil.mvp.presenter;

import android.util.Log;

import com.qzh.mvputil.base.BasePresenter;
import com.qzh.mvputil.di.scope.ActivityScope;
import com.qzh.mvputil.mvp.model.NewsListModel;
import com.qzh.mvputil.mvp.view.ViewContract;

import javax.inject.Inject;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;


/**
 * Created by lcx on 2017/6/6.
 */
@ActivityScope
public class NewsListPresenter extends BasePresenter<NewsListModel,ViewContract.NewsListView> {
	private Disposable subscribe;
	@Inject
	public NewsListPresenter(NewsListModel model, ViewContract.NewsListView view) {
		this.mModel = model;
		this.mView = view;
	}

	public void getNewList(int pno, final boolean isFirst) {
		 mModel.getNewsListModel(pno, 15, "d975b5fe029c0691fe5d683cb68b86ac", "json")
				 .subscribe(new Observer<String>() {
					 @Override
					 public void onError(Throwable e) {
						 mView.requestFail(e.getMessage());
					 }
					 @Override
					 public void onComplete() {
					 }
					 @Override
					 public void onSubscribe(@NonNull Disposable d) {
						 subscribe = d;
					 }

					 @Override
					 public void onNext(String result) {
						 Log.d(TAG, "onNext: "+result.toString());
						 mView.success(result);
					 }
				 });
		addSubscribe(subscribe);
	}




}
