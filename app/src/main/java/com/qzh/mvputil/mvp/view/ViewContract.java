package com.qzh.mvputil.mvp.view;

import com.qzh.mvputil.base.BaseView;

import java.util.List;


/**
 * Created by lcx on 2017/6/5.
 */

public interface ViewContract {
	interface NewsListView extends BaseView {
		void success(String result);
		void fail(String msg);
	}
}
