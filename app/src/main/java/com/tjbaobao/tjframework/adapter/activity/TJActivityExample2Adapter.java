package com.tjbaobao.tjframework.adapter.activity;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tjbaobao.framework.base.BaseItemDecoration;
import com.tjbaobao.framework.base.BaseRecyclerAdapter;
import com.tjbaobao.framework.ui.BaseRecyclerView;
import com.tjbaobao.framework.utils.DeviceUtil;
import com.tjbaobao.framework.utils.ImageDownloader;
import com.tjbaobao.framework.utils.Tools;
import com.tjbaobao.tjframework.R;
import com.tjbaobao.tjframework.model.AppItemTitleInfo;
import com.tjbaobao.tjframework.model.TJActivityExample1Info;
import com.tjbaobao.tjframework.model.TJActivityExample2Info;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 作者:TJbaobao
 * 时间:2018/9/21  10:51
 * 说明:
 * 使用：
 */
public class TJActivityExample2Adapter extends BaseRecyclerAdapter<BaseRecyclerView.BaseViewHolder,TJActivityExample2Info> {

    //使用了ImageDownloader之后，你会发现其已经具备了缓存、列表滑动防止错位、裁剪压缩、防止重复下载等等功能。
    private ImageDownloader imageDownloader = ImageDownloader.getInstance();

    public TJActivityExample2Adapter(List<TJActivityExample2Info> tjActivityExample2Infos) {
        super(tjActivityExample2Infos);
        imageDownloader.setDefaultImgSize(DeviceUtil.getScreenWidth()/2,DeviceUtil.getScreenWidth()/2);
    }

    /**
     * 重写onCreateViewHolder实现不同布局的初始化(还有方法二，调用adatper.setLayoutConfig()方法，BaseRecyclerAdapter会自动进行初始化)
     * @param parent parent
     * @param viewType viewType
     * @return holder
     */
    @NonNull
    @Override
    public BaseRecyclerView.BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view ;
        switch (viewType)
        {
            case TJActivityExample2Info.TYPE_ITEM:{
                view = LayoutInflater.from(parent.getContext()).inflate( R.layout.tj_activity_example_1_activity_item_layout,parent,false);
                break;
            }
            case TJActivityExample2Info.TYPE_TITLE:{
                view = LayoutInflater.from(parent.getContext()).inflate( R.layout.app_item_title_layout,parent,false);
                break;
            }
            case TJActivityExample2Info.TYPE_LIST:{
                view = LayoutInflater.from(parent.getContext()).inflate( R.layout.app_item_list_layout,parent,false);
                break;
            }
            default:
                view = LayoutInflater.from(parent.getContext()).inflate( R.layout.tj_activity_example_1_activity_item_layout,parent,false);
        }
        return onGetHolder(view,viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull BaseRecyclerView.BaseViewHolder holder, @NonNull TJActivityExample2Info infoBase, int position) {
        int type = infoBase.getType();
        switch (type)
        {
            case TJActivityExample2Info.TYPE_ITEM:{
                if(holder instanceof HolderItem)
                {
                    HolderItem holderItem = (HolderItem) holder;
                    Object info = infoBase.getInfo();
                    if(info instanceof TJActivityExample1Info)
                    {
                        imageDownloader.load(R.drawable.ic_animation_sdvanced,holderItem.ivImage);
//                        imageDownloader.load(((TJActivityExample1Info)info).imageUrl,holderItem.ivImage);
                    }
                }
            }
            case TJActivityExample2Info.TYPE_TITLE:{
                if(holder instanceof HolderTitle)
                {
                    HolderTitle holderTitle = (HolderTitle) holder;
                    Object info = infoBase.getInfo();
                    if(info instanceof AppItemTitleInfo)
                    {
                        holderTitle.tvTitle.setText(((AppItemTitleInfo)info).getTitle());
                    }
                }
            }
            case TJActivityExample2Info.TYPE_LIST:{

            }
        }
    }

    @NonNull
    @Override
    public BaseRecyclerView.BaseViewHolder onGetHolder(View view, int viewType) {
        switch (viewType)
        {
            case TJActivityExample2Info.TYPE_ITEM:
                return new HolderItem(view);
            case TJActivityExample2Info.TYPE_TITLE:
                return new HolderTitle(view);
            case TJActivityExample2Info.TYPE_LIST:
                return new HolderList(view);
            default:
                return new HolderItem(view);
        }
    }

    public class HolderItem extends BaseRecyclerView.BaseViewHolder
    {
        @BindView(R.id.ivImage)
        ImageView ivImage ;

        HolderItem(View itemView) {
            super(itemView);
        }

        @Override
        public void onInitView(View itemView) {
            ButterKnife.bind(this,itemView);
        }
    }

    public class HolderTitle extends BaseRecyclerView.BaseViewHolder
    {
        @BindView(R.id.tvTitle)
        TextView tvTitle ;

        HolderTitle(View itemView) {
            super(itemView);
        }

        @Override
        public void onInitView(View itemView) {
            ButterKnife.bind(this,itemView);
        }
    }

    public class HolderList extends BaseRecyclerView.BaseViewHolder
    {
        @BindView(R.id.recyclerView)
        BaseRecyclerView<TJActivityExample1Adapter.Holder,TJActivityExample1Info> recyclerView;

        HolderList(View itemView) {
            super(itemView);
        }

        @Override
        public void onInitView(View itemView) {
            ButterKnife.bind(this,itemView);
            recyclerView.toListView(RecyclerView.HORIZONTAL,false);
            recyclerView.addItemDecoration(BaseItemDecoration.getLineHorizontalDecoration(Tools.dpToPx(8)));//添加默认间隔
        }

        /**
         * 初始化adapter
         * @param adapter adapter
         */
        @Override
        public void onInitAdapter(@NonNull RecyclerView.Adapter adapter) {
            recyclerView.setAdapter(adapter);
        }
    }

    public void destroy()
    {
        imageDownloader.stop();
    }
}
