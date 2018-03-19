package com.tjbaobao.framework.base;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tjbaobao.framework.ui.BaseRecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * RecyclerAdapter
 * 对RecyclerView.Adapter进行封装
 * 将RecyclerView,Adapter,Holder,OnItemClickListener进行连贯统一
 *
 * Created by TJbaobao on 2018/1/6.
 */

@SuppressWarnings({"unused", "WeakerAccess"})
public abstract class BaseRecyclerAdapter<Holder extends BaseRecyclerView.BaseViewHolder,Info> extends Adapter<Holder> {

    private OnItemClickListener<Holder,Info> mOnItemClickListener ;
    private List<Info> infoList = new ArrayList<>();
    private int itemLayoutRes ;
    private Map<Object,Holder> mapHolder = new HashMap<>();

    public BaseRecyclerAdapter(List<Info> infoList, int itemLayoutRes) {
        this.infoList = infoList;
        this.itemLayoutRes = itemLayoutRes;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(parent.getContext()).inflate(itemLayoutRes, null);
        return onGetHolder(view);
    }

    @Override
    public void onViewRecycled(@NonNull Holder holder) {
        super.onViewRecycled(holder);
        mapHolder.put(holder.itemView.getTag(),null);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        Info info = infoList.get(position);
        onBindViewHolder(holder,info,position);
        Object tag = onGetItemTag(info,position);
        if(tag!=null)
        {
            mapHolder.put(tag,holder);
        }
        if(holder.itemView!=null)
        {
            holder.itemView.setOnClickListener(new ItemOnClickListener(holder,info,position));
        }
    }

    /**
     * 处理item
     * @param holder 布局统一对象{@link Holder}
     * @param info 数据实体类
     * @param position 序号
     */
    public abstract void onBindViewHolder(@NonNull Holder holder,@NonNull Info info, int position);


    /**
     * 获取Holder,通常是new一个自定义的{@link Holder}
     * @param view 主布局,在new的时候传入{@link Holder}
     * @return 返回自定义的 {@link Holder}
     */
    public abstract Holder onGetHolder(View view);

    @Override
    public int getItemCount() {
        return infoList.size();
    }

    /**
     * 按需重写，自由给Item赋值标签
     * @param info 数据实体类
     * @param position 序号
     * @return 你自定义的标签，例如(url)
     */
    protected Object onGetItemTag(Info info,int position)
    {
        return null;
    }

    /**
     * 通过标签寻找{@link Holder},此功能需要与{@link #onGetItemTag}配合使用
     * @param tag tag
     * @return {@link Holder}
     */
    @Nullable
    protected Holder findHolder(Object tag)
    {
        return mapHolder.get(tag);
    }

    /**
     * Item点击监听器
     * @param <Holder> {@link Holder}
     * @param <Info> 数据实体类
     */
    public interface OnItemClickListener<Holder,Info>
    {
        void onItemClick(Holder holder, Info info, int position);
    }

    private class ItemOnClickListener implements View.OnClickListener
    {
        private Holder mHolder ;
        private Info info ;
        private int position;

        ItemOnClickListener(Holder holder, Info info, int position) {
            mHolder = holder;
            this.info = info;
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            if(mOnItemClickListener!=null)
            {
                mOnItemClickListener.onItemClick(mHolder,info,position);
            }
        }
    }

    /**
     * 设置监听器
     * @param onItemClickListener {@link OnItemClickListener}
     */
    public void setOnItemClickListener(BaseRecyclerAdapter.OnItemClickListener<Holder,Info> onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

}
