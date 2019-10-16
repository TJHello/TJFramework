package com.tjbaobao.tjframework.adapter.activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tjbaobao.framework.base.BaseItemDecoration;
import com.tjbaobao.framework.base.BaseRecyclerAdapter;
import com.tjbaobao.framework.ui.BaseRecyclerView;
import com.tjbaobao.framework.utils.Tools;
import com.tjbaobao.tjframework.R;
import com.tjbaobao.tjframework.model.TJActivityExample3Info;

import java.util.List;

/**
 * 作者:TJbaobao
 * 时间:2019/1/4  16:16
 * 说明:这里演示的是怎么通过BaseRecyclerAdapter来快速搭建复杂列表。
 * 使用：
 */
public class TJActivityExample3Adapter extends BaseRecyclerAdapter<TJActivityExample3Adapter.Holder,TJActivityExample3Info> {


    public TJActivityExample3Adapter(List<TJActivityExample3Info> tjActivityExample3Infos) {
        super(tjActivityExample3Infos);
    }

    @Override
    protected int onGetLayout(int viewType) {
        switch (viewType){
            case TJActivityExample3Info.TITLE_SWITCH:{
                return R.layout.tj_activity_example_3_item_1_layout;
            }
            case TJActivityExample3Info.TITLE_MENU:{
                return R.layout.tj_activity_example_3_item_2_layout;
            }
            case TJActivityExample3Info.TITLE_NONE:{
                return R.layout.tj_activity_example_3_item_3_layout;
            }
            case TJActivityExample3Info.LAYOUT_TUTORIAL:{
                return R.layout.tj_activity_example_3_item_4_layout;
            }
            case TJActivityExample3Info.LIST_MUSIC:{
                return R.layout.app_item_list_layout;
            }
            default:{
                return R.layout.tj_activity_example_3_item_5_layout;
            }
        }
    }

    @NonNull
    @Override
    public Holder onGetHolder(@NonNull View view, int viewType) {
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, @NonNull TJActivityExample3Info info, int position) {
        switch (info.getType()){
            case TJActivityExample3Info.TITLE_SWITCH:{
                bindTitleSwitch(holder,info,position);
            }break;
            case TJActivityExample3Info.TITLE_MENU:{
                bindTitleMenu(holder,info,position);
            }break;
            case TJActivityExample3Info.TITLE_NONE:{
                bindTitleNone(holder,info,position);
            }break;
            case TJActivityExample3Info.LAYOUT_TUTORIAL:{

            }break;
            default:{
            }
        }
    }

    private void bindTitleSwitch(@NonNull Holder holder, @NonNull TJActivityExample3Info info, int position){
        holder.ivIcon.setImageResource(info.example3Enum.iconResId);
        holder.tvTitle.setText(info.example3Enum.titleResId);
        holder.switchView.setChecked(info.isSelect);
    }

    private void bindTitleMenu(@NonNull Holder holder, @NonNull TJActivityExample3Info info, int position){
        holder.ivIcon.setImageResource(info.example3Enum.iconResId);
        holder.tvTitle.setText(info.example3Enum.titleResId);
    }

    private void bindTitleNone(@NonNull Holder holder, @NonNull TJActivityExample3Info info, int position){
        holder.ivIcon.setImageResource(info.example3Enum.iconResId);
        holder.tvTitle.setText(info.example3Enum.titleResId);
    }

    public class Holder extends BaseRecyclerView.BaseViewHolder {

        ImageView ivIcon ;
        TextView tvTitle ;
        Switch switchView ;
        BaseRecyclerView recyclerView ;

        Holder(View itemView) {
            super(itemView);
        }

        @Override
        public void onInitView(View itemView) {
            ivIcon = findViewById(itemView,R.id.ivIcon);
            tvTitle = findViewById(itemView,R.id.tvTitle);
            switchView = findViewById(itemView,R.id.switchView);
            recyclerView = findViewById(itemView,R.id.recyclerView);
            if(recyclerView!=null){
                recyclerView.toListView(RecyclerView.HORIZONTAL,false);
                recyclerView.addItemDecoration(BaseItemDecoration.getLineHorizontalDecoration(Tools.dpToPx(8)));
            }
        }

        @Override
        public void onInitAdapter(@NonNull RecyclerView.Adapter adapter, int position) {
            if(recyclerView==null) return;
            TJActivityExample3Info info = infoList.get(position);
            if(info.getType()==TJActivityExample3Info.LIST_MUSIC){
                recyclerView.setAdapter(adapter);
            }
        }
    }
}
