package com.qzh.mvputil.api;

import com.qzh.mvputil.entity.HttpResult;
import com.qzh.mvputil.entity.News;
import com.qzh.mvputil.entity.PageBean;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * 请求接口
 * @author Q_zh
 * @date 2017/5/4.
 */

public interface ApiService {
	@GET("weixin/query")
	Observable<String> getNewsList(@Query("pno") int pno,
													   @Query("ps") int ps,
													   @Query("key") String key,
													   @Query("dtype") String dtype);
}
