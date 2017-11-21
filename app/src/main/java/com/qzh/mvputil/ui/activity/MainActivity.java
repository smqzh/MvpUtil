package com.qzh.mvputil.ui.activity;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.reflect.TypeToken;
import com.qzh.mvputil.R;
import com.qzh.mvputil.base.BaseActivity;
import com.qzh.mvputil.di.component.DaggerNewsComponent;
import com.qzh.mvputil.di.module.NewsModule;
import com.qzh.mvputil.entity.HttpResult;
import com.qzh.mvputil.entity.News;
import com.qzh.mvputil.entity.PageBean;
import com.qzh.mvputil.mvp.presenter.NewsListPresenter;
import com.qzh.mvputil.mvp.view.ViewContract;
import com.qzh.mvputil.ui.adapter.NewsListAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class MainActivity extends BaseActivity<NewsListPresenter> implements ViewContract.NewsListView {
    protected final String TAG = this.getClass().getSimpleName();
    @BindView(R.id.rlv_news)
    RecyclerView mRlvNews;
    @BindView(R.id.swiperefresh)
    SwipeRefreshLayout mSwiperefresh;
    private NewsListAdapter mAdapter;
    private List<News> mDatas = new ArrayList<News>();
    private int mCurrentPage = 1;//当前页码
    private int mTotalPage;//总页码
    private int flag = 0;//0 -- 第一次加载或者刷新  1 -- 加载更多

    @Override
    protected int attachLayoutRes() {
        return R.layout.activity_main;
    }

    @Override
    protected void initViews() {
        /* 设置toolBar */
        getToolBar().setTitle(getResources().getString(R.string.main)).setDisplayHomeAsUpEnabled(false);
        mAdapter = new NewsListAdapter(mDatas);
        mRlvNews.setAdapter(mAdapter);
        /* item点击事件 */
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
//                Log.d(TAG, "onItemClick: "+mDatas.size());
//                adapter.getItem(position);
//                Intent intent = new Intent(HomeActivity.this,NewsDetailActivity.class);
//                intent.putExtra("url",mDatas.get(position).getUrl());
//                intent.putExtra("title",mDatas.get(position).getTitle());
//                startActivity(intent);
            }
        });
/* 刷新操作 */
        mSwiperefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                flag = 0;
                mCurrentPage = 1;
                mPresenter.getNewList(mCurrentPage, false);
            }
        });
        /* 加载更多 */
        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                if (mCurrentPage >= mTotalPage) {//数据全部加载完毕
                    mAdapter.loadMoreEnd();
                } else {//数据未加载完，继续请求加载
                    flag = 1;
                    mCurrentPage += 1;
                    mPresenter.getNewList(mCurrentPage, false);
                }
            }
        });
    }

    @Override
    protected void initData() {
        mPresenter.getNewList(mCurrentPage, true);
    }

    @Override
    protected void initInject() {
        DaggerNewsComponent.builder()
                .newsModule(new NewsModule(this))
                .build()
                .inject(this);
    }

    @Override
    protected String getOperateType() {
        return null;
    }

    @Override
    public void success(String result) {
        HttpResult<PageBean<News>> page = gson.fromJson(result, new TypeToken<HttpResult<PageBean<News>>>() {
        }.getType());
        int totalPage = page.getResult().getTotalPage();
        List<News> list = page.getResult().getList();
//获取页码
        mTotalPage = totalPage;
        if (flag == 0) {//第一次加载或者刷新
            mDatas.clear();
            if (list.size() == 0) {
                //showNonData("当前暂无数据。");
                return;
            }
            mSwiperefresh.setRefreshing(false);
        } else if (flag == 1) {//加载更多
            mAdapter.loadMoreComplete();
        }
        mAdapter.addData(list);
    }

    @Override
    public void fail(String msg) {

    }

    /* 按返回键后台运行程序 */
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);
    }
}
