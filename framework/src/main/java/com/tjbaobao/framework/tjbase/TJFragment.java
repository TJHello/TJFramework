package com.tjbaobao.framework.tjbase;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tjbaobao.framework.R;
import com.tjbaobao.framework.base.BaseFragment;
import com.tjbaobao.framework.entity.ui.TitleBarInfo;
import com.tjbaobao.framework.ui.BaseTitleBar;

/**
 * TJFragment
 * 规范Fragment的编程规范以及提供多能TitleBar的快捷集成
 * Created by TJbaobao on 2018/1/10.
 */

public abstract class TJFragment extends BaseFragment implements BaseTitleBar.OnTitleBarClickListener{

    protected View baseView ;
    protected BaseTitleBar titleBar ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        onInitValue(savedInstanceState);
        baseView = onCreateView(inflater,container);
        initTitleBar();
        onInitView(baseView);
        onLoadData();
        return baseView;
    }


    protected void initTitleBar()
    {
        titleBar = baseView.findViewById(R.id.titleBar);
        if(titleBar!=null)
        {
            titleBar.setOnTitleBarClickListener(this);
            titleBar.setBackgroundColor(getColorById(R.color.fw_theme_color));
            onInitTitleBar(titleBar);
        }
    }

    protected abstract void onInitValue(Bundle savedInstanceState);

    protected abstract View onCreateView(LayoutInflater inflater, ViewGroup container);

    protected abstract void onInitView(View baseView);

    protected abstract void onInitTitleBar(BaseTitleBar titleBar);

    protected abstract void onLoadData();

    @Override
    public <V extends TitleBarInfo.BaseView> void onTitleBarClick(int layoutType, int position, V info) {

    }

}
