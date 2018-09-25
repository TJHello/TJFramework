package com.tjbaobao.tjframework.activity.example;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.tjbaobao.framework.base.BaseRecyclerAdapter;
import com.tjbaobao.framework.entity.ui.TitleBarInfo;
import com.tjbaobao.framework.ui.BaseRecyclerView;
import com.tjbaobao.framework.ui.BaseTitleBar;
import com.tjbaobao.framework.utils.Tools;
import com.tjbaobao.tjframework.R;
import com.tjbaobao.tjframework.adapter.activity.DataBaseExampleAdapter;
import com.tjbaobao.tjframework.base.AppActivity;
import com.tjbaobao.tjframework.database.dao.TbEventDAO;
import com.tjbaobao.tjframework.database.obj.TbEventObj;
import com.tjbaobao.tjframework.model.DataBaseExampleInfo;
import com.tjbaobao.tjframework.model.enums.MainActivityEnum;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 作者:TJbaobao
 * 时间:2018/9/25  11:19
 * 说明:
 *
 * 使用：
 * 1、设置数据库初始化接口，参考{@link com.tjbaobao.tjframework.base.TJApplication}
 * 2、实现数据库增删改查逻辑，参考{@link TbEventDAO}
 */
public class DataBaseExampleActivity extends AppActivity {

    @BindView(R.id.recyclerView)
    BaseRecyclerView<DataBaseExampleAdapter.Holder,DataBaseExampleInfo> recyclerView ;

    private List<DataBaseExampleInfo> infoList = new ArrayList<>();
    private DataBaseExampleAdapter adapter = new DataBaseExampleAdapter(infoList,R.layout.database_example_activity_item_layout);

    private int spanCount = Tools.isPad() ? 3 : 2;

    private String title ;

    @Override
    protected void onInitValues(@Nullable Bundle savedInstanceState) {
        super.onInitValues(savedInstanceState);
        title = this.getIntent().getStringExtra("title");
    }

    @Override
    protected void onInitView() {
        setContentView(R.layout.database_example_activity_layout);
        ButterKnife.bind(this);
        recyclerView.toGridView(spanCount);
        recyclerView.addGridAverageCenterDecoration(Tools.dpToPx(8),Tools.dpToPx(8));
        recyclerView.setOnItemClickListener(new OnItemClickListener());
        recyclerView.setAdapter(adapter);
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
        infoList.clear();
        for(MainActivityEnum activityEnum:MainActivityEnum.values())
        {
            DataBaseExampleInfo info = new DataBaseExampleInfo();
            info.name = activityEnum.title;
            List<TbEventObj> objList = TbEventDAO.getDataListByName(info.name);
            if(objList!=null)
            {
                info.num = objList.size();
            }
            else
            {
                info.num = 0;
            }
            infoList.add(info);
        }
        adapter.notifyDataSetChanged();
    }

    private class OnItemClickListener implements BaseRecyclerAdapter.OnItemClickListener<DataBaseExampleAdapter.Holder,DataBaseExampleInfo>
    {
        @Override
        public void onItemClick(@NonNull DataBaseExampleAdapter.Holder holder, @NonNull DataBaseExampleInfo dataBaseExampleInfo, int position) {

        }
    }

}
