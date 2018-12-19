package com.tjbaobao.tjframework.activity.example;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

import com.tjbaobao.framework.entity.ui.TitleBarInfo;
import com.tjbaobao.framework.ui.BaseTitleBar;
import com.tjbaobao.framework.utils.Tools;
import com.tjbaobao.tjframework.R;
import com.tjbaobao.tjframework.base.AppActivity;
import com.tjbaobao.tjframework.dialog.TJDialogExample1;
import com.tjbaobao.tjframework.dialog.TJDialogExample2;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 作者:TJbaobao
 * 时间:2018/9/21  17:29
 * 说明:
 * 使用：
 */
public class TJDialogExample1Activity extends AppActivity{

    @BindView(R.id.btShow1)
    Button btShow1;

    @BindView(R.id.btShow2)
    Button btShow2;

    private String title ;

    private TJDialogExample1 tjDialogExample1;

    private TJDialogExample2 tjDialogExample2;

    @Override
    protected void onInitValues(@Nullable Bundle savedInstanceState) {
        super.onInitValues(savedInstanceState);
        title = this.getIntent().getStringExtra("title");
    }

    @Override
    protected void onInitView() {
        setContentView(R.layout.tj_dialog_example_1_activity_layout);
        ButterKnife.bind(this);

        tjDialogExample1 = new TJDialogExample1(this){
            @Override
            public void onBtContinueClick(@NonNull View view) {
                super.onBtContinueClick(view);
                Tools.showToast("确定");
            }

            @Override
            public void onBtCancelClick(@NonNull View view) {
                super.onBtCancelClick(view);
                Tools.showToast("取消");
            }
        };
        tjDialogExample1.setTitle("我是一个简单的弹窗");

        tjDialogExample2 = new TJDialogExample2(this){
            @Override
            public void onBtContinueClick(@NonNull View view) {
                super.onBtContinueClick(view);
                Tools.showToast("确定");
            }

            @Override
            public void onBtCancelClick(@NonNull View view) {
                super.onBtCancelClick(view);
                Tools.showToast("取消");
            }
        };
    }

    @Override
    protected void onLoadData() {

    }

    @Override
    protected void onInitTitleBar(@NonNull BaseTitleBar titleBar) {
        titleBar.addImageToLeft(R.drawable.fw_ic_back);
        titleBar.setTitle(title);
    }

    @Override
    public <V extends TitleBarInfo.BaseView> void onTitleBarClick(int layoutType, int position, V info) {
        if(layoutType==BaseTitleBar.LAYOUT_LEFT&&position==0)
        {
            finish();
        }
    }

    @OnClick({R.id.btShow1,R.id.btShow2})
    void onShowClick(View view)
    {
        if(view.getId()==R.id.btShow1)
        {
            tjDialogExample1.show();
        }
        else
        {
            tjDialogExample2.show();
        }
    }


}
