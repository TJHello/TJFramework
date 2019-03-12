package com.tjbaobao.tjframework.activity.example;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.tjbaobao.framework.base.BaseRecyclerAdapter;
import com.tjbaobao.framework.entity.ui.TitleBarInfo;
import com.tjbaobao.framework.ui.BaseRecyclerView;
import com.tjbaobao.framework.ui.BaseTitleBar;
import com.tjbaobao.framework.utils.FileUtil;
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
import java.util.UUID;

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

    @BindView(R.id.btInset)
    Button btInset ;

    @BindView(R.id.btSelect)
    Button btSelect ;

    @BindView(R.id.btClean)
    Button btClean ;

    @BindView(R.id.tvResult)
    TextView tvResult;

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

        btInset.setOnClickListener(v -> inset1000());
        btSelect.setOnClickListener(v -> selectAll());
        btClean.setOnClickListener(v -> cleanAll());
    }

    private void inset1000(){
        List<TbEventObj> tbEventObjList = new ArrayList<>();
        for(int i=0;i<1000;i++){
            TbEventObj eventObj = new TbEventObj();
            eventObj.setCode(UUID.randomUUID().toString());
            eventObj.setName("Test");
            eventObj.setEvent(TbEventObj.EVENT_TEST);
            eventObj.setPath("Test");
            tbEventObjList.add(eventObj);
        }
        FileUtil.CurrentTime.begin();
        TbEventDAO.addTransaction(tbEventObjList);
        long time = FileUtil.CurrentTime.stop("addTransaction1000");
        tvResult.setText("插入1000调数据耗时:"+time);
    }

    private void selectAll(){
        FileUtil.CurrentTime.begin();
        List<TbEventObj> list1 = TbEventDAO.getDataList();
        long time1 = FileUtil.CurrentTime.stop("selectAll_Old");

        FileUtil.CurrentTime.begin();
        List<TbEventObj> list2 = TbEventDAO.getDataListNew();
        long time2 = FileUtil.CurrentTime.stop("selectAll_New");
        if(list1!=null&&list2!=null){
            tvResult.setText("旧的方法:数据数:"+list1.size()+",耗时:"+time1+"\n新方法：数据数:"+list2.size()+",耗时:"+time2);
        }else{
            tvResult.setText("获取失败");
        }
    }

    private void cleanAll(){
        TbEventDAO.delAll();
        tvResult.setText("成功清除所有数据");
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
