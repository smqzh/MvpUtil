package com.qzh.mvputil.api;

import android.util.Log;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;


/**
 * RxJava错误重试
 * @author Q_zh
 * @date 2017/10/23.
*/
public class RetryWithDelay implements
		Function<Observable<? extends Throwable>, ObservableSource<?>> {

	protected final String TAG = this.getClass().getSimpleName();
	private final int maxRetries;
	private final int retryDelayMillis;
	private int retryCount;

	public RetryWithDelay(int maxRetries, int retryDelayMillis) {
		this.maxRetries = maxRetries;
		this.retryDelayMillis = retryDelayMillis;
	}

	@Override
	public Observable<?> apply(Observable<? extends Throwable> attempts) {
		return attempts
				.flatMap(new Function<Throwable, ObservableSource<?>>() {
					@Override
					public Observable<?> apply(Throwable throwable) {
						if (++retryCount <= maxRetries) {
							Log.d(TAG, "call: retry");
							return Observable.timer(retryDelayMillis,
									TimeUnit.MILLISECONDS);
						}
						return Observable.error(throwable);
					}
				});
	}
}