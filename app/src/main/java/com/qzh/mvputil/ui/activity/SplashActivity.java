package com.qzh.mvputil.ui.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.qzh.mvputil.R;
import com.qzh.mvputil.util.ActivityUtils;
import com.qzh.mvputil.util.ImageLoader;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 引导页
 * @author Q_zh
 * @date 2017/8/4.
 */

public class SplashActivity extends AppCompatActivity{

	@BindView(R.id.ivSplsh)
	ImageView mIvSplsh;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//设置无标题
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//设置全屏
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_splash);
		ButterKnife.bind(this);
		initViews();
		initData();
	}

	protected void initViews() {
		mIvSplsh.setImageBitmap(ImageLoader.load(this, R.mipmap.splash_bg));
	}

	protected void initData() {
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				ActivityUtils.toNextActivity(SplashActivity.this, MainActivity.class);
				finish();
				overridePendingTransition(R.anim.hold_in, R.anim.zoom_in_exit);
			}
		}, 2000);
	}
}
