package com.tjbaobao.tjframework.activity.example;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.tjbaobao.framework.base.BaseRecyclerAdapter;
import com.tjbaobao.framework.entity.ui.TitleBarInfo;
import com.tjbaobao.framework.ui.BaseRecyclerView;
import com.tjbaobao.framework.ui.BaseTitleBar;
import com.tjbaobao.framework.utils.Tools;
import com.tjbaobao.tjframework.R;
import com.tjbaobao.tjframework.adapter.activity.TJActivityExample1Adapter;
import com.tjbaobao.tjframework.base.AppActivity;
import com.tjbaobao.tjframework.model.TJActivityExample1Info;
import com.tjbaobao.tjframework.utils.AliyunOOSUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 作者:TJbaobao
 * 时间:2018/9/13  10:44
 * 说明:TJActivity的使用示例-配合列表使用
 * 使用：
 * 1、创建一个AppActivity继承TJActivity，定制自己的喜好内容
 * 2、在onInitValues,onInitView,onLoadData里面分别初始化值、初始化组件、加载数据。
 */
public class TJActivityExample1Activity extends AppActivity {

    //使用BaseRecyclerView之前需要先创建对应的Adapter，而创建Adapter就需要创建Holder和Info实体
    private List<TJActivityExample1Info> infoList = new ArrayList<>();
    private TJActivityExample1Adapter adapter = new TJActivityExample1Adapter(infoList,R.layout.tj_activity_example_1_activity_item_layout);

    //其实用了Kotlin之后就不需要findViewById或者butterknife这类的框架了，但我觉得kotlin写出来的代码不够美观，不适合用来做示例
    @BindView(R.id.recyclerView)
    BaseRecyclerView<TJActivityExample1Adapter.Holder,TJActivityExample1Info> recyclerView;

    private String title ;

    @Override
    protected void onInitValues(@Nullable Bundle savedInstanceState) {
        super.onInitValues(savedInstanceState);
        title = this.getIntent().getStringExtra("title");
    }

    @Override
    protected void onInitView() {
        setContentView(R.layout.tj_activity_example_actyivity_layout);
        ButterKnife.bind(getActivity());
        recyclerView.toGridView(2);
        //recyclerView.toListView();//普通列表
        //recyclerView.toStaggeredGridView();//瀑布列表
        recyclerView.addGridAverageCenterDecoration(Tools.dpToPx(8),Tools.dpToPx(8));//添加默认间隔
        //recyclerView.addItemDecoration(BaseItemDecoration.getLineVerticalDecoration(Tools.dpToPx(8)));//添加自定义间隔
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new OnItemClickListener());
        adapter.setOnItemLongClickListener((holder, tjActivityExample1Info, position) -> {
            Tools.showToast("OnItemLongClickListener");
        });

    }

    @Override
    protected void onLoadData() {
        infoList.clear();
        for(int i=0;i<10;i++)
        {
            TJActivityExample1Info info = new TJActivityExample1Info();
            info.imageUrl = AliyunOOSUtil.getUrl("lakes-04_m_2.jpg");
            infoList.add(info);
        }
        adapter.notifyDataSetChanged();
    }

    //需要在布局中使用BaseTitleBar并且添加android:id="@+id/titleBar",当且仅当这个id存在的时候才会调用这个方法
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

    //列表Item点击监听器
    private class OnItemClickListener implements BaseRecyclerAdapter.OnItemClickListener<TJActivityExample1Adapter.Holder,TJActivityExample1Info>
    {
        @Override
        public void onItemClick(@NonNull TJActivityExample1Adapter.Holder holder, @NonNull TJActivityExample1Info info, int position) {
            Tools.showToast("点击了:"+position);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        adapter.destroy();
    }
}
