package com.tjbaobao.tjframework.activity.example;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Switch;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.tjbaobao.framework.base.BaseRecyclerAdapter;
import com.tjbaobao.framework.entity.ui.TitleBarInfo;
import com.tjbaobao.framework.listener.OnTJHolderItemClickListener;
import com.tjbaobao.framework.ui.BaseRecyclerView;
import com.tjbaobao.framework.ui.BaseTitleBar;
import com.tjbaobao.framework.utils.Tools;
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
 * 示例快速搭建复杂列表
 *
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
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new OnItemClickListener());
//        adapter.setOnTJHolderItemClickListener(new OnMYTJHolderItemClickListener());
//        adapter.setOnTJHolderItemClickListener(new OnMYTJHolderItemClickListener(),TJActivityExample3Info.TITLE_SWITCH);
        adapter.setOnTJHolderItemIdClickListener(new OnMYTJHolderItemClickListener(),R.id.switchView);
    }

    @Override
    protected void onLoadData() {
        for(TJActivityExample3Enum tjEnum : TJActivityExample3Enum.values()){
            TJActivityExample3Info info = new TJActivityExample3Info(tjEnum.type);
            info.example3Enum = tjEnum;
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
            switch (info.example3Enum){
                case RATE:{
                    Tools.showToast(info.example3Enum.name());
                }break;
                case FEEDBACK:{
                    Tools.showToast(info.example3Enum.name());
                }break;
                case TERMS:{
                    Tools.showToast(info.example3Enum.name());
                }break;
                case PRIVACY:{
                    Tools.showToast(info.example3Enum.name());
                }break;
            }
        }
    }

    //设置Item内子View的点击事件监听器
    private class OnMYTJHolderItemClickListener implements OnTJHolderItemClickListener<TJActivityExample3Info>{

        @Override
        public void onClick(@NonNull View view, @NonNull TJActivityExample3Info info, int position) {
            if(view.getId()==R.id.switchView){
                //开关按钮点击
                onClick(info);
                if(view instanceof Switch){
                    if(((Switch) view).isChecked()){
                        Tools.showToast("打开"+info.example3Enum.name());
                    }else{
                        Tools.showToast("关闭"+info.example3Enum.name());
                    }
                }
            }
        }

        private void onClick( @NonNull TJActivityExample3Info info){
            switch (info.example3Enum){
                case MUSIC_TITLE:{

                }break;
                case ANIMATION_TITLE:{

                }break;
                case SOUND_TITLE:{

                }break;
                case VIBRATION_TITLE:{

                }break;
                case THUMBNAIL_TITLE:{

                }break;
                case WATERMARK_TITLE:{

                }break;
            }
        }
    }

}
