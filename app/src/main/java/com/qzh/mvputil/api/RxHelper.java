package com.qzh.mvputil.api;

import android.util.Log;

import com.qzh.mvputil.entity.HttpResult;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Rxava结果预处理
 * @author Q_zh
 * @date 2017/10/23.
 */

public class RxHelper {
	/**
	 * 获得结果并作判断
	 * @param <T>
	 * @return
	 */
	public static <T> ObservableTransformer<HttpResult<T>, T> handleResult() {
		return new ObservableTransformer<HttpResult<T>, T>() {
			@Override
			public Observable<T> apply(Observable<HttpResult<T>> tObservable) {
				return tObservable.flatMap(new Function<HttpResult<T>, ObservableSource<T>>() {
					@Override
					public Observable<T> apply(HttpResult<T> result) {
						Log.d("code", "call: "+result.getError_code());
						if (result.getError_code() == 0) {
							return createData(result.getResult());
						} else {
							return Observable.error(new ApiException(result.getError_code()));
						}
					}
				}).subscribeOn(Schedulers.io())
						.unsubscribeOn(Schedulers.io())
						.observeOn(AndroidSchedulers.mainThread());
			}
		};
	}

	/**
	 * 创建成功的数据
	 * @param data
	 * @param <T>
	 * @return
	 */
	private static <T> Observable<T> createData(final T data) {
		return Observable.create(new ObservableOnSubscribe<T>() {
			@Override
			public void subscribe(@NonNull ObservableEmitter<T> subscriber) throws Exception {
				try {
					subscriber.onNext(data);
					subscriber.onComplete();
				} catch (Exception e) {
					subscriber.onError(e);
				}
			}

		});
	}
}
