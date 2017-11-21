package com.qzh.mvputil.base;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v13.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.qzh.mvputil.R;
import com.qzh.mvputil.common.MvpApplication;
import com.qzh.mvputil.entity.SubmitParams;
import com.qzh.mvputil.ui.activity.LoginActivity;
import com.qzh.mvputil.util.AppUtil;
import com.qzh.mvputil.util.NetUtils;
import com.qzh.mvputil.util.ToastUtil;
import com.qzh.mvputil.widget.ToolBarSet;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import cn.fjlcx.android.stateframelayout.StateAttr;


/**
 * 基类的Activity
 *
 * @author ling_cx
 * @date 2017/5/4.
 */

public abstract class BaseActivity<P extends BasePresenter> extends AppCompatActivity implements BaseView, ActivityCompat.OnRequestPermissionsResultCallback {
	protected final String TAG = this.getClass().getSimpleName();

	private TextView mTvCenterTitle;

	private Toolbar mToolbar;
	@Inject
	protected P mPresenter;
	public Context mContext;
	private ToolBarSet mToolBarSet;
	public MvpApplication app;

	public SubmitParams mSubmitParams = null;
	public Gson gson;
	public MaterialDialog progress;

	/**
	 * 需要进行检测的权限数组
	 */
	protected String[] mNeedPermissions = {};
	private static final int PERMISSION_REQUEST_CODE = 0;
	/**
	 * 判断是否需要检测，防止无限弹框申请权限
	 */
	private boolean isNeedCheckPermission = true;
	/**
	 * 登录超时标记
	 */
	private int loginTimeOutCode = 2;

	/**
	 *沉浸式状态栏管理器
	 */
	private SystemBarTintManager tintManager;
	/**
	 * 沉浸式状态栏颜色
	 */
	private final static int SystemBarColor = R.color.main;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(attachLayoutRes());
		initStateBar();
		ButterKnife.bind(this);
		mNeedPermissions = getNeedPermissions();
		checkPermissions(mNeedPermissions);
		init();
		MvpApplication.getInstance().getActivityManager().pushActivity(this);
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	/**
	 * 重新唤醒后重新判断是否拥有权限
	 */
	@Override
	protected void onResume() {
		super.onResume();
		if (isNeedCheckPermission) {
			checkPermissions(mNeedPermissions);
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		Log.d(TAG, "onDestroy: BaseOnDestory");
		super.onDestroy();
		progress.dismiss();
		if (mPresenter != null) {
			mPresenter.unSubscribe();
		}
		gson = null;
		app.getActivityManager().popActivity(this);
	}

	/**
	 * 初始化沉浸式
	 */
	private void initStateBar() {
		if (isNeedLoadStatusBar()) {
			loadStateBar();
		}
	}

	private void loadStateBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			setTranslucentStatus(true);
		}
		tintManager = new SystemBarTintManager(this);
		// 激活状态栏设置
		tintManager.setStatusBarTintEnabled(true);
		// 激活导航栏设置
		tintManager.setNavigationBarTintEnabled(true);
		// 设置一个状态栏颜色
		tintManager.setStatusBarTintResource(SystemBarColor);
	}

	@TargetApi(19)
	private void setTranslucentStatus(boolean on) {
		Window win = getWindow();
		WindowManager.LayoutParams winParams = win.getAttributes();
		final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
		if (on) {
			winParams.flags |= bits;
		} else {
			winParams.flags &= ~bits;
		}
		win.setAttributes(winParams);
	}

	/**
	 * 子类是否需要实现沉浸式,默认需要
	 *
	 * @return
	 */
	protected boolean isNeedLoadStatusBar() {
		return true;
	}

	@Override
	public void setContentView(@LayoutRes int layoutResID) {
		View view = getLayoutInflater().inflate(R.layout.base_layout, null);
		mTvCenterTitle = (TextView) view.findViewById(R.id.tv_center_title);
		super.setContentView(view);
		initDefaultView(layoutResID);
	}

	private void initDefaultView(int layoutResId) {
		mToolbar = (Toolbar) findViewById(R.id.toolbar);
		FrameLayout container = (FrameLayout) findViewById(R.id.container);
		View childView = LayoutInflater.from(this).inflate(layoutResId, null);
		container.addView(childView, 0);
	}

	private void init() {
		mContext = this;
		gson = new GsonBuilder().serializeNulls().create();
		mToolBarSet = new ToolBarSet(mToolbar, mTvCenterTitle, this);
		app = (MvpApplication) getApplication();
//		if (getOperateType() != null) {
//			mSubmitParams = new SubmitParams(app.getTicket(), getOperateType(), "Json", true);
//		}
		progress = new MaterialDialog.Builder(mContext)
				.content(getResources().getString(R.string.loading))
				.progress(true, 0)
				.cancelable(false)
				.build();
		initInject();
		initViews();
		if (NetUtils.isConnected(mContext)) {
			initData();
		} else {
			showNetError();
		}
	}


	/**
	 * 获取屏幕尺寸，但是不包括虚拟功能高度
	 *
	 * @return
	 */
	public int getNoHasVirtualKey() {
		int height = getWindowManager().getDefaultDisplay().getHeight();
		return height;
	}


	/**
	 * 通过反射，获取包含虚拟键的整体屏幕高度
	 *
	 * @return
	 */
	public int getHasVirtualKey() {
		int dpi = 0;
		Display display = getWindowManager().getDefaultDisplay();
		DisplayMetrics dm = new DisplayMetrics();
		@SuppressWarnings("rawtypes")
		Class c;
		try {
			c = Class.forName("android.view.Display");
			@SuppressWarnings("unchecked")
			Method method = c.getMethod("getRealMetrics", DisplayMetrics.class);
			method.invoke(display, dm);
			dpi = dm.heightPixels;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dpi;
	}

	/**
	 * 绑定布局文件
	 *
	 * @return 布局文件ID
	 */
	@LayoutRes
	protected abstract int attachLayoutRes();

	/**
	 * 初始化视图控件
	 */
	protected abstract void initViews();

	/**
	 * 初始化数据
	 */
	protected abstract void initData();

	/**
	 * 初始化dagger2
	 */
	protected abstract void initInject();

	/**
	 * 获取所需的危险权限进行请求
	 * @return
	 */
	protected String[] getNeedPermissions(){
		return new String[0];
	}

	/**
	 * 获取请求类别
	 *  @return
	 */
	protected abstract String getOperateType();

	/**
	 * 获取Toolbar对象
	 *
	 * @return
	 */
	public ToolBarSet getToolBar() {
		if (mToolBarSet == null) {
			mToolBarSet = new ToolBarSet(mToolbar, mTvCenterTitle, this);
		}
		return mToolBarSet;
	}



	@Override
	public void showLoding() {

	}

	@Override
	public void showNetError() {

	}

	@Override
	public void showErr(String err) {

	}


	@Override
	public void showNonData() {

	}

	@Override
	public void showData() {

	}

	/**
	 * 设置activity之间的切换动画
	 */
	@Override
	public void startActivity(Intent intent) {
		super.startActivity(intent);
	}

	@Override
	public void finish() {
		super.finish();
	}

	/**
	 * 页面跳转
	 *
	 * @param clz
	 */
	@Override
	public void startActivity(Class<?> clz) {
		startActivity(new Intent(BaseActivity.this, clz));
	}

	/**
	 * 携带数据的页面跳转
	 *
	 * @param clz
	 * @param bundle
	 */
	@Override
	public void startActivity(Class<?> clz, Bundle bundle) {
		Intent intent = new Intent();
		intent.setClass(this, clz);
		if (bundle != null) {
			intent.putExtras(bundle);
		}
		startActivity(intent);
	}

	/**
	 * 通过反射的方式获取状态栏高度
	 *
	 * @return
	 */
	private int getStatusBarHeight() {
		try {
			Class<?> c = Class.forName("com.android.internal.R$dimen");
			Object obj = c.newInstance();
			Field field = c.getField("status_bar_height");
			int x = Integer.parseInt(field.get(obj).toString());
			return getResources().getDimensionPixelSize(x);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * 请求失败异常处理
	 *
	 * @param message
	 */
	@Override
	public void requestFail(final String message) {
		Log.d(TAG, "requestFail: " + message);
		progress.dismiss();
		showErr(message);
	}

	/**
	 * 操作失败异常处理
	 *
	 * @param code
	 * @param message
	 */
	@Override
	public void operateFail(int code, final String message) {
		progress.dismiss();
		if (code == loginTimeOutCode) {
			ToastUtil.show(mContext, getResources().getString(R.string.login_timeout));
			Intent intent = new Intent(mContext, LoginActivity.class);
			Bundle bundle = new Bundle();
			bundle.putInt("logout", 1);
			intent.putExtras(bundle);
			startActivity(intent);
			finish();
			app.finishExcepionLogin();
		} else {
			ToastUtil.show(mContext, message);
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					finish();
				}
			}, 1000);
		}
	}

	/**
	 * 校验权限是否已获得
	 *
	 * @param permissions
	 */
	private void checkPermissions(String... permissions) {
		List<String> needRequestPermissionList = findDeniedPermissions(permissions);
		if (null != needRequestPermissionList
				&& needRequestPermissionList.size() > 0) {
			ActivityCompat.requestPermissions(this,
					needRequestPermissionList.toArray(
							new String[needRequestPermissionList.size()]),
					PERMISSION_REQUEST_CODE);
		}
	}

	/**
	 * 获取权限集中需要申请权限的列表
	 *
	 * @param permissions
	 * @return
	 */
	private List<String> findDeniedPermissions(String[] permissions) {
		List<String> needRequestPermissionList = new ArrayList<>();
		for (String perm : permissions) {
			if (ContextCompat.checkSelfPermission(this, perm) != PackageManager.PERMISSION_GRANTED
					|| ActivityCompat.shouldShowRequestPermissionRationale(this, perm)) {
				needRequestPermissionList.add(perm);
			}
		}
		return needRequestPermissionList;
	}

	/**
	 * 检测是否需要的权限都已经授权
	 *
	 * @param grantResults
	 * @return
	 */
	private boolean verifyPermissions(int[] grantResults) {
		for (int result : grantResults) {
			if (result != PackageManager.PERMISSION_GRANTED) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 请求权限的结果
	 *
	 * @param requestCode
	 * @param permissions
	 * @param grantResults
	 */
	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		if (requestCode == PERMISSION_REQUEST_CODE) {
			if (!verifyPermissions(grantResults)) {
				showMissingPermissionDialog();
				isNeedCheckPermission = false;
			}
		}
	}

	/**
	 * 显示提示信息
	 */
	private void showMissingPermissionDialog() {
		new MaterialDialog.Builder(this)
				.title("提示信息")
				.content("缺少必要权限，应用将不能正常使用。<br>\\r请点击\\\"去设置\\\"-\\\"权限\\\"-打开所需权限。")
				.positiveText("去设置")
				.onPositive(new MaterialDialog.SingleButtonCallback() {
					@Override
					public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
						AppUtil.startAppSettings(mContext);
					}
				})
				.negativeText("拒绝")
				.onNegative(new MaterialDialog.SingleButtonCallback() {
					@Override
					public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
						finish();
					}
				})
				.show();
	}

}
