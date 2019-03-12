package com.tjbaobao.tjframework.adapter.activity;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;

import com.tjbaobao.framework.base.BaseRecyclerAdapter;
import com.tjbaobao.framework.ui.BaseRecyclerView;
import com.tjbaobao.framework.utils.DeviceUtil;
import com.tjbaobao.framework.utils.ImageDownloader;
import com.tjbaobao.tjframework.R;
import com.tjbaobao.tjframework.model.TJActivityExample1Info;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 作者:TJbaobao
 * 时间:2018/9/13  10:59
 * 说明:
 * 使用：
 */
public class TJActivityExample1Adapter extends BaseRecyclerAdapter<TJActivityExample1Adapter.Holder,TJActivityExample1Info> {

    //使用了ImageDownloader之后，你会发现其已经具备了缓存、列表滑动防止错位、裁剪压缩、防止重复下载等等功能。
    private ImageDownloader imageDownloader = ImageDownloader.getInstance();

    public TJActivityExample1Adapter(List<TJActivityExample1Info> tjActivityExampleInfos, int itemLayoutRes) {
        super(tjActivityExampleInfos, itemLayoutRes);
        imageDownloader.setDefaultImgSize(DeviceUtil.getScreenWidth()/2,DeviceUtil.getScreenWidth()/2);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, @NonNull TJActivityExample1Info info, int position) {
        imageDownloader.load(info.imageUrl,holder.ivImage);
    }

    @Override
    public Holder onGetHolder(View view, int type) {
        return new Holder(view);
    }

    public class Holder extends BaseRecyclerView.BaseViewHolder{

        @BindView(R.id.ivImage)
        ImageView ivImage ;

        Holder(View itemView) {
            super(itemView);
        }

        @Override
        public void onInitView(View itemView) {
            ButterKnife.bind(this,itemView);
        }
    }


    @Override
    public void destroy() {
        super.destroy();
        imageDownloader.stop();
    }
}
