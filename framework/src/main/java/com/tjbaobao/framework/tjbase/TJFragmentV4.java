package com.tjbaobao.framework.tjbase;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tjbaobao.framework.R;
import com.tjbaobao.framework.base.BaseV4Fragment;
import com.tjbaobao.framework.entity.ui.TitleBarInfo;
import com.tjbaobao.framework.ui.BaseTitleBar;

/**
 *
 * Created by TJbaobao on 2018/3/21.
 */

public abstract class TJFragmentV4 extends BaseV4Fragment implements BaseTitleBar.OnTitleBarClickListener{

    protected View baseView ;
    protected BaseTitleBar titleBar ;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
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

    protected abstract void onInitValue(@Nullable Bundle savedInstanceState);

    protected abstract View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container);

    protected abstract void onInitView(@NonNull View baseView);

    protected abstract void onInitTitleBar(@NonNull BaseTitleBar titleBar);

    protected abstract void onLoadData();

    public void callLoadData(){
        onLoadData();
    }

    @Override
    public <V extends TitleBarInfo.BaseView> void onTitleBarClick(int layoutType, int position, V info) {

    }

}
