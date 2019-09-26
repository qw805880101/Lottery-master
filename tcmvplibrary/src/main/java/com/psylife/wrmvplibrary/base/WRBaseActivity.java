package com.psylife.wrmvplibrary.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.psylife.wrmvplibrary.AppManager;
import com.psylife.wrmvplibrary.R;
import com.psylife.wrmvplibrary.utils.LogUtil;
import com.psylife.wrmvplibrary.utils.SpUtil;
import com.psylife.wrmvplibrary.utils.StatusBarUtil;
import com.psylife.wrmvplibrary.utils.TUtil;
import com.psylife.wrmvplibrary.utils.ThemeUtil;
import com.psylife.wrmvplibrary.utils.TitleBuilder;
import com.psylife.wrmvplibrary.utils.ToastUtils;
import com.psylife.wrmvplibrary.widget.CustomProgressDialog;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by psylife00 on 2016/11/29.
 */

public abstract class WRBaseActivity<T extends WRBasePresenter, E extends WRBaseModel> extends Activity {

    protected String TAG;

    public T mPresenter;
    public E mModel;
    protected Context mContext;

    private ImageView ivShadow;
    private boolean isOpen = true;
    private boolean hasData = false;
    Unbinder binder;

    private View titleView;
    public TitleBuilder mTitleBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置状态栏透明
        Window window = getWindow();
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        int flag = LayoutParams.FLAG_FULLSCREEN;
        window.setFlags(flag, flag);
        hideBottomUIMenu();

        hideNavigation();

        if (Build.VERSION.SDK_INT >= VERSION_CODES.KITKAT_WATCH) {
            setStatusBarColor();
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        init(savedInstanceState);
    }

    //隐藏虚拟按键：
    protected void hideBottomUIMenu() {
        //隐藏虚拟按键，并且全屏
        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api
            View v = this.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else if (Build.VERSION.SDK_INT >= 19) {
            //for new api versions.
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }

    private void init(Bundle savedInstanceState) {
        TAG = getClass().getSimpleName();

        setTheme(ThemeUtil.themeArr[SpUtil.getThemeIndex(this)][
                SpUtil.getNightModel(this) ? 1 : 0]);
        this.setContentView(this.getLayoutId());
        binder = ButterKnife.bind(this);
        mContext = this;
        mPresenter = TUtil.getT(this, 0);
        mModel = TUtil.getT(this, 1);
        this.initView(savedInstanceState);
        if (this instanceof WRBaseView) mPresenter.attachVM(this, mModel);
        initdata();

        AppManager.getAppManager().addActivity(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppManager.getAppManager().finishActivity(this);
        if (binder != null) binder.unbind();
        if (mPresenter != null) mPresenter.detachVM();
    }

    public void reload() {
        Intent intent = getIntent();
        overridePendingTransition(0, 0);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        finish();
        overridePendingTransition(0, 0);
        startActivity(intent);
    }

    @Override
    public void setContentView(int layoutResID) {
        View title = getTitleView();
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        if (title != null) {
            LinearLayout rootlayout = new LinearLayout(this);
            rootlayout.setOrientation(LinearLayout.VERTICAL);
            rootlayout.addView(title);
            View view = LayoutInflater.from(this).inflate(layoutResID, null);
            rootlayout.addView(view, params);
            super.setContentView(rootlayout);
        } else {
            LinearLayout rootlayout = new LinearLayout(this);
            rootlayout.setOrientation(LinearLayout.VERTICAL);
            View view = LayoutInflater.from(this).inflate(layoutResID, null);
            rootlayout.addView(view, params);
            super.setContentView(rootlayout);
        }
    }

    public View getTitleView() {
        if (titleView == null) {
//            titleView = LayoutInflater.from(this).inflate(R.layout.include_titlebar, null);
            titleView = initBackTitle("").getRootView();
        }
        return titleView;
    }

    public abstract int getLayoutId();

    public abstract void initView(Bundle savedInstanceState);

    public abstract void initdata();

//    @Override
//    public void onBackPressedSupport() {
//        supportFinishAfterTransition();
//    }

//    @Override
//    protected FragmentAnimator onCreateFragmentAnimator() {
//        // 设置横向(和安卓4.x动画相同)
//        return new DefaultHorizontalAnimator();
//        // 设置无动画
////        return new DefaultNoAnimator();
//        // 设置自定义动画
//        // return new FragmentAnimator(enter,exit,popEnter,popExit);
//        // 默认竖向(和安卓5.0以上的动画相同)
////        return super.onCreateFragmentAnimator();
//    }

    public void setStatusBarColor() {
        StatusBarUtil.setTransparent(this);
//        StatusBarUtil.setTranslucent(this);
    }

//    protected void setToolBar(Toolbar toolbar, String title) {
//        toolbar.setTitle(title);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
//        toolbar.setNavigationIcon(R.mipmap.ic_arrow_back_white_24dp);
//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                onBackPressedSupport();
//            }
//        });
//    }

    /**
     * 左侧有返回键的标题栏
     * <p>如果在此基础上还要加其他内容,比如右侧有文字按钮,可以获取该方法返回值继续设置其他内容
     *
     * @param title 标题
     */
    private TitleBuilder initBackTitle(String title) {
        mTitleBuilder = new TitleBuilder(this)
                .setTitleText(title)
                .setLeftImage(R.drawable.icon_back)
                .setLeftOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });
        return mTitleBuilder;
    }

    public TitleBuilder getmTitleBuilder() {
        return mTitleBuilder;
    }

    /**
     * 跳转页面,无extra简易型
     *
     * @param tarActivity 目标页面
     */
    public void startActivity(Class<? extends Activity> tarActivity, Bundle options) {
        Intent intent = new Intent(this, tarActivity);
        intent.putExtras(options);
        startActivity(intent);
    }

    public void startActivity(Class<? extends Activity> tarActivity) {
        Intent intent = new Intent(this, tarActivity);
        startActivity(intent);
//        this.finish();
    }

    public void showToast(String msg) {
        ToastUtils.showToast(this, msg, Toast.LENGTH_SHORT);
    }

    public void showLog(String msg) {
        LogUtil.i(TAG, msg);// TODO: 16/10/12 Log需要自己从新搞一下
    }

    private CustomProgressDialog progressDialog = null;

    /**
     * 开始loading
     *
     * @param context
     */
    public void startProgressDialog(Context context, String hint) {
        if (progressDialog == null) {
            progressDialog = CustomProgressDialog.createDialog(context);
            progressDialog.setCanceledOnTouchOutside(false);
            // progressDialog.setCancelable(false);
            progressDialog.setMessage(hint);
        }

        progressDialog.show();
    }

    public void startProgressDialog(Context context) {
        startProgressDialog(context, "正在加载中...");
    }

    /**
     * 结束loading
     */
    public void stopProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }
    public boolean hideNavigation(){
        boolean ishide;
        try
        {
            String command;
            command = "LD_LIBRARY_PATH=/vendor/lib:/system/lib service call activity 42 s16 com.android.systemui";
            Process proc = Runtime.getRuntime().exec(new String[] { "su", "-c", command });
            proc.waitFor();
            ishide = true;
        }
        catch(Exception ex)
        {
            Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
            ishide = false;
        }
        return ishide;
    }
    /*显示虚拟按键*/
    public boolean showNavigation(){
        boolean isshow;
        try
        {
            String command;
            command = "LD_LIBRARY_PATH=/vendor/lib:/system/lib am startservice -n com.android.systemui/.SystemUIService";
            Process proc = Runtime.getRuntime().exec(new String[] { "su", "-c", command });
            proc.waitFor();
            isshow = true;
        }
        catch (Exception e)
        {
            isshow = false;
            e.printStackTrace();
        }
        return isshow;
    }


}
