package com.tjbaobao.tjframework.activity.example;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.tjbaobao.framework.base.BaseRecyclerAdapter;
import com.tjbaobao.framework.entity.ui.TitleBarInfo;
import com.tjbaobao.framework.ui.BaseRecyclerView;
import com.tjbaobao.framework.ui.BaseTitleBar;
import com.tjbaobao.tjframework.R;
import com.tjbaobao.tjframework.adapter.activity.TJActivityExample1Adapter;
import com.tjbaobao.tjframework.adapter.activity.TJActivityExample3Adapter;
import com.tjbaobao.tjframework.base.AppActivity;
import com.tjbaobao.tjframework.model.TJActivityExample1Info;
import com.tjbaobao.tjframework.model.TJActivityExample3Info;
import com.tjbaobao.tjframework.model.enums.TJActivityExample3Enum;
import com.tjbaobao.tjframework.utils.AliyunOOSUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者:TJbaobao
 * 时间:2019/1/4  16:15
 * 说明:
 * 使用：
 */
public class TJActivityExample3Activity extends AppActivity {

    @BindView(R.id.recyclerView)
    BaseRecyclerView<TJActivityExample3Adapter.Holder, TJActivityExample3Info> recyclerView;

    private List<TJActivityExample3Info> infoList = new ArrayList<>();
    private TJActivityExample3Adapter adapter = new TJActivityExample3Adapter(infoList);

    private List<TJActivityExample1Info> example1InfoList = new ArrayList<>();
    private TJActivityExample1Adapter example1Adapter = new TJActivityExample1Adapter(example1InfoList, R.layout.app_item_list_item_layout);


    private String title ;

    @Override
    protected void onInitValues(@Nullable Bundle savedInstanceState) {
        super.onInitValues(savedInstanceState);
        title = this.getIntent().getStringExtra("title");
    }

    @Override
    protected void onInitView() {
        setContentView(R.layout.tj_activity_example_actyivity_layout);
        ButterKnife.bind(this);
        recyclerView.toListView();
//        recyclerView.addListViewItemDecoration();
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new OnItemClickListener());
    }

    @Override
    protected void onLoadData() {
        for(TJActivityExample3Enum tjEnum : TJActivityExample3Enum.values()){
            TJActivityExample3Info info = new TJActivityExample3Info(tjEnum.type);
            info.iconResId = tjEnum.iconResId;
            info.titleResId = tjEnum.titleResId;
            if(tjEnum == TJActivityExample3Enum.MUSIC_LIST){
                info.setAdapter(example1Adapter);
            }
            infoList.add(info);
        }

        for(int i=0;i<5;i++)
        {
            TJActivityExample1Info info = new TJActivityExample1Info();
            info.imageUrl = AliyunOOSUtil.getUrl("lakes-07_2.jpg");
            example1InfoList.add(info);
        }

        adapter.notifyDataSetChanged();
        example1Adapter.notifyDataSetChanged();
    }

    //region===================================初始化标题组件=================================

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

    //endregion

    //设置Item点击事件监听器
    private class OnItemClickListener implements
            BaseRecyclerAdapter.OnItemClickListener<TJActivityExample3Adapter.Holder, TJActivityExample3Info>{
        @Override
        public void onItemClick(@NonNull TJActivityExample3Adapter.Holder baseViewHolder, @NonNull TJActivityExample3Info info, int position) {

        }
    }
}
