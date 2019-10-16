package com.tjbaobao.tjframework.activity.example;

import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.tjbaobao.framework.base.BaseRecyclerAdapter;
import com.tjbaobao.framework.entity.ui.TitleBarInfo;
import com.tjbaobao.framework.ui.BaseRecyclerView;
import com.tjbaobao.framework.ui.BaseTitleBar;
import com.tjbaobao.framework.utils.RxJavaUtil;
import com.tjbaobao.framework.utils.Tools;
import com.tjbaobao.tjframework.R;
import com.tjbaobao.tjframework.adapter.activity.TJActivityExample1Adapter;
import com.tjbaobao.tjframework.adapter.activity.TJActivityExample2Adapter;
import com.tjbaobao.tjframework.base.AppActivity;
import com.tjbaobao.tjframework.model.AppItemTitleInfo;
import com.tjbaobao.tjframework.model.TJActivityExample1Info;
import com.tjbaobao.tjframework.model.TJActivityExample2Info;
import com.tjbaobao.tjframework.utils.AliyunOOSUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 作者:TJbaobao
 * 时间:2018/9/21  10:16
 * 说明:
 * 使用：
 * 1、创建Adapter，Holder，Info(Info需要继承{@link com.tjbaobao.framework.entity.base.BaseListInfo})
 * 2、重写Adapter的onCreateViewHolder方法，参考{@link TJActivityExample2Adapter}
 * 3、其他可选，参考{@link TJActivityExample2Adapter}
 */
public class TJActivityExample2Activity extends AppActivity {


    @BindView(R.id.recyclerView)
    BaseRecyclerView<BaseRecyclerView.BaseViewHolder, TJActivityExample2Info> recyclerView;

    //使用高级列表要求实体类继承BaseListInfo
    private List<TJActivityExample2Info> homeInfoList = new ArrayList<>();
    private TJActivityExample2Adapter homeAdapter = new TJActivityExample2Adapter(homeInfoList);

    private List<TJActivityExample1Info> example1InfoList = new ArrayList<>();
    private TJActivityExample1Adapter example1Adapter = new TJActivityExample1Adapter(example1InfoList, R.layout.app_item_list_item_layout);

    private int spanCount = Tools.isPad() ? 3 : 2;

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

        recyclerView.toGridView(spanCount);
        recyclerView.addItemDecoration(new HomeItemDecoration());
        recyclerView.setAdapter(homeAdapter);
        recyclerView.setSpanSizeConfig(homeInfoList);

        homeAdapter.setOnItemClickListener(new OnHomeItemClickListener());
        example1Adapter.setOnItemClickListener(new OnExampleItemClickListener());
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

    @Override
    protected void onLoadData() {
        RxJavaUtil.runOnIOToUI(new RxJavaUtil.RxTask<Object>(){

            @Override
            public void onIOThread() {
                loadHomeData();
                loadListData();
            }

            @Override
            public void onUIThread() {
                example1Adapter.notifyDataSetChanged();
                homeAdapter.notifyDataSetChanged();
            }
        });

    }

    private void loadHomeData() {
        homeInfoList.clear();
        homeInfoList.add(new TJActivityExample2Info(new AppItemTitleInfo("Title"), spanCount));
        homeInfoList.add(new TJActivityExample2Info(example1Adapter, spanCount));
        homeInfoList.add(new TJActivityExample2Info(new AppItemTitleInfo("Title"), spanCount));
        for (int i = 0; i < spanCount*2; i++) {
            TJActivityExample1Info info = new TJActivityExample1Info();
            info.imageUrl = AliyunOOSUtil.getUrl("lakes-02_m_2.jpg");
            homeInfoList.add(new TJActivityExample2Info(info));
        }
        homeInfoList.add(new TJActivityExample2Info(new AppItemTitleInfo("Title"), spanCount));
        homeInfoList.add(new TJActivityExample2Info(example1Adapter, spanCount));
        homeInfoList.add(new TJActivityExample2Info(new AppItemTitleInfo("Title"), spanCount));
        for (int i = 0; i <spanCount*3; i++) {
            TJActivityExample1Info info = new TJActivityExample1Info();
            info.imageUrl = AliyunOOSUtil.getUrl("lakes-02_m_2.jpg");
            homeInfoList.add(new TJActivityExample2Info(info));
        }
        homeInfoList.add(new TJActivityExample2Info(new AppItemTitleInfo("Title"), spanCount));
        homeInfoList.add(new TJActivityExample2Info(example1Adapter, spanCount));

        homeInfoList.add(new TJActivityExample2Info(new AppItemTitleInfo("Title"), spanCount));
        for (int i = 0; i <spanCount*4; i++) {
            TJActivityExample1Info info = new TJActivityExample1Info();
            info.imageUrl = AliyunOOSUtil.getUrl("lakes-02_m_2.jpg");
            homeInfoList.add(new TJActivityExample2Info(info));
        }
    }

    private void loadListData()
    {
        for(int i=0;i<5;i++)
        {
            TJActivityExample1Info info = new TJActivityExample1Info();
            info.imageUrl = AliyunOOSUtil.getUrl("lakes-07_2.jpg");
            example1InfoList.add(info);
        }
    }

    private class OnExampleItemClickListener implements BaseRecyclerAdapter.OnItemClickListener<TJActivityExample1Adapter.Holder,TJActivityExample1Info> {
        @Override
        public void onItemClick(@NonNull TJActivityExample1Adapter.Holder holder, @NonNull TJActivityExample1Info tjActivityExample1Info, int position) {

        }
    }

    private class OnHomeItemClickListener implements BaseRecyclerAdapter.OnItemClickListener<BaseRecyclerView.BaseViewHolder,TJActivityExample2Info>
    {
        @Override
        public void onItemClick(@NonNull BaseRecyclerView.BaseViewHolder baseViewHolder, @NonNull TJActivityExample2Info tjActivityExample2Info, int position) {

        }
    }

    private class HomeItemDecoration extends RecyclerView.ItemDecoration
    {
        private final int PADDING = Tools.dpToPx(8);
        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view);
            if(position>=0&&position<homeInfoList.size())
            {
                TJActivityExample2Info info = homeInfoList.get(position);
                if(info.getType()==TJActivityExample2Info.TYPE_ITEM)
                {
                    int offSet = getOffset(position);
                    if(offSet%spanCount==0)
                    {
                        outRect.left = PADDING;
                        outRect.right = PADDING /2;
                    }
                    else if(offSet%spanCount==spanCount-1)
                    {
                        outRect.left = PADDING /2;
                        outRect.right = PADDING;
                    }
                    else
                    {
                        outRect.left = PADDING /2;
                        outRect.right = PADDING /2;
                    }
                    if(offSet<spanCount)
                    {
                        outRect.top = PADDING;
                        outRect.bottom = PADDING /2;
                    }
                    else
                    {
                        outRect.top = PADDING /2;
                        outRect.bottom = PADDING /2;
                    }
                }
            }
        }
        private int getOffset(int position)
        {
            int offSet = 0;
            for(;position>=0;position--)
            {
                TJActivityExample2Info info = homeInfoList.get(position);
                if(info.getType()==TJActivityExample2Info.TYPE_ITEM)
                {
                    offSet++;
                }
            }
            return offSet-1;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        homeAdapter.destroy();
        example1Adapter.destroy();
    }
}
