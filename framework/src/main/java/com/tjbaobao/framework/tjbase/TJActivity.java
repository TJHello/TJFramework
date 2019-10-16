package com.tjbaobao.framework.tjbase;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.tjbaobao.framework.R;
import com.tjbaobao.framework.base.BaseActivity;
import com.tjbaobao.framework.database.TJDataBaseHelper;
import com.tjbaobao.framework.entity.ui.TitleBarInfo;
import com.tjbaobao.framework.ui.BaseTitleBar;
import com.tjbaobao.framework.utils.Tools;

/**
 * TJActivity
 * 规范Activity编程习惯及提供多能TitleBar的快捷集成
 * Created by TJbaobao on 2018/1/10.
 */
@SuppressWarnings({"UnusedReturnValue", "unused"})
public abstract class TJActivity extends BaseActivity implements BaseTitleBar.OnTitleBarClickListener{

    protected BaseTitleBar titleBar ;
    protected boolean isAutoDestroyDB = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initValues(savedInstanceState);
        initView();
        initTitleBar();
        onLoadData();
    }

    /**
     * 初始化值
     */
    private void initValues(@Nullable Bundle savedInstanceState)
    {
        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            finish();
            return;
        }
        onInitValues(savedInstanceState);
    }

    /**
     * 初始化组件与布局
     */
    private void initView()
    {
        onInitView();
    }

    private void initTitleBar()
    {
        titleBar = this.findViewById(R.id.titleBar);
        if(titleBar!=null)
        {
            titleBar.setOnTitleBarClickListener(this);
            titleBar.setBackgroundColor(getColorById(R.color.fw_theme_color));
            onInitTitleBar(titleBar);
            titleBar.reLayout();
        }
    }

    protected abstract void onInitValues(@Nullable Bundle savedInstanceState);

    protected abstract void onInitView();

    protected abstract void onInitTitleBar(@NonNull BaseTitleBar titleBar);

    protected abstract void onLoadData();

    @Override
    public void onClick(View v) {
        super.onClick(v);
    }

    @Override
    public <V extends TitleBarInfo.BaseView> void onTitleBarClick(int layoutType, int position, V info) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(isAutoDestroyDB){
            TJDataBaseHelper.destroy();
        }
    }

    //region ***************双击退出应用开始*************
    private long startTime = 0;
    private boolean isOnclickTwoExit = false;
    private int exitTime = 2000;
    private String exitTip = Tools.getResString(R.string.fb_click_two_exit_tip);
    /**
     * 双击两次退出应用
     */
    public void isOnclickTwoExit()
    {
        setOnclickTwoExit(exitTime);
    }
    public void isOnclickTwoExit(boolean isExit)
    {
        isOnclickTwoExit = isExit;
        if(isExit)
        {
            setOnclickTwoExit(exitTime);
        }
    }
    public void setOnclickTwoExitTip(String exitTip) {
        this.exitTip = exitTip;
    }

    public void setOnclickTwoExit(int time)
    {
        isOnclickTwoExit = true ;
        exitTime = time;
    }

    @Override
    public void onBackPressed() {
        long nowTime = System.currentTimeMillis();
        if (nowTime - startTime < exitTime||!isOnclickTwoExit)
        {
            super.onBackPressed();
        }
        else
        {
            startTime = System.currentTimeMillis();
            onClickTwoExit();
        }
    }

    protected boolean onClickTwoExit()
    {
        Tools.showToast(exitTip);
        return true;
    }
    //endregion ***************双击退出应用结束*************
}
