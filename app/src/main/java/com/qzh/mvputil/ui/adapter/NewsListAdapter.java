package com.qzh.mvputil.ui.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qzh.mvputil.R;
import com.qzh.mvputil.entity.News;

import java.util.List;


/**
 * Created by Q_zh on 2017/6/8.
 */

public class NewsListAdapter extends BaseQuickAdapter<News,BaseViewHolder> {
	public NewsListAdapter(@Nullable List data) {
		super(R.layout.item_news, data);
	}

	@Override
	protected void convert(BaseViewHolder helper, News item) {
		helper.setText(R.id.c_title,item.getTitle())
			.setText(R.id.c_content,"来源:"+item.getSource());
		Glide.with(mContext).load(item.getFirstImg()).crossFade().into((ImageView) helper.getView(R.id.c_img));
	}
}
