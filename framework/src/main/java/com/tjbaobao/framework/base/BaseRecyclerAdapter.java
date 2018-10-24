package com.tjbaobao.framework.base;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.tjbaobao.framework.entity.base.BaseListInfo;
import com.tjbaobao.framework.ui.BaseRecyclerView;

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
    private OnItemLongClickListener<Holder,Info> mOnItemLongClickListener ;

    protected List<Info> infoList ;
    protected int itemLayoutRes ;
    private Map<Object,Holder> mapHolder = new HashMap<>();

    public BaseRecyclerAdapter(List<Info> infoList, int itemLayoutRes) {
        this.infoList = infoList;
        this.itemLayoutRes = itemLayoutRes;
    }

    public BaseRecyclerAdapter(List<Info> infoList) {
        this(infoList,-1);
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(itemLayoutRes==-1)
        {
            return null;
        }
        View view  = LayoutInflater.from(parent.getContext()).inflate(itemLayoutRes, parent,false);
        if(view!=null)
        {
            return onGetHolder(view,viewType);
        }
        return null;
    }

    @Override
    public void onViewRecycled(@NonNull Holder holder) {
        mapHolder.put(holder.tag,null);
        holder.tag = null;
        super.onViewRecycled(holder);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        if(infoList==null||position<0||position>=infoList.size())
        {
            return ;
        }
        Info info = infoList.get(position);
        if(info!=null)
        {
            Object tag = onGetItemTag(info,position);
            if(tag!=null)
            {
                holder.tag = tag;
                mapHolder.put(tag,holder);
            }
            if(holder.itemView!=null)
            {
                holder.itemView.setOnClickListener(new ItemOnClickListener(holder,info,position));
                holder.itemView.setOnLongClickListener(new ItemOnLongClickListener(holder,info,position));
            }
            if(info instanceof BaseListInfo)
            {
                Adapter adapter = ((BaseListInfo) info).getAdapter();
                if(adapter!=null)
                {
                    holder.onInitAdapter(adapter);
                }
            }
            onBindViewHolder(holder,info,position);
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
    @NonNull
    public abstract Holder onGetHolder(@NonNull View view , int viewType);

    @Override
    public int getItemCount() {
        if(infoList==null)
        {
            return 0;
        }
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

    @Override
    public int getItemViewType(int position) {
        if(position<0||position>=infoList.size())
        {
            return 0;
        }
        Info info = infoList.get(position);
        if(info instanceof BaseListInfo)
        {
            return ((BaseListInfo) info).getType();
        }
        return super.getItemViewType(position);
    }

    /**
     * 通过标签寻找{@link Holder},此功能需要与{@link #onGetItemTag}配合使用
     * @param tag tag
     * @return {@link Holder}
     */
    @Nullable
    protected Holder findHolder(Object tag)
    {
        if(mapHolder.containsKey(tag))
        {
            return mapHolder.get(tag);
        }
        return null;
    }

    /**
     * Item点击监听器
     * @param <Holder> {@link Holder}
     * @param <Info> 数据实体类
     */
    public interface OnItemClickListener<Holder,Info>
    {
        void onItemClick(@NonNull Holder holder,@NonNull Info info, int position);
    }

    /**
     * Item长按监听器
     * @param <Holder> {@link Holder}
     * @param <Info> 数据实体类
     */
    public interface OnItemLongClickListener<Holder,Info>
    {
        void onItemLongClick(@NonNull Holder holder,@NonNull Info info, int position);
    }

    private class ItemOnClickListener implements View.OnClickListener
    {
        private Holder mHolder ;
        private Info info ;
        private int position;

        ItemOnClickListener(@NonNull Holder holder, @NonNull Info info, int position) {
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

    private class ItemOnLongClickListener  implements View.OnLongClickListener{

        private Holder mHolder ;
        private Info info ;
        private int position;

        ItemOnLongClickListener(@NonNull Holder holder, @NonNull Info info, int position)
        {
            mHolder = holder;
            this.info = info;
            this.position = position;
        }

        @Override
        public boolean onLongClick(View v) {
            if(mOnItemLongClickListener!=null)
            {
                mOnItemLongClickListener.onItemLongClick(mHolder,info,position);
            }
            return true;
        }
    }

    /**
     * 设置监听器
     * @param onItemClickListener {@link OnItemClickListener}
     */
    public void setOnItemClickListener(BaseRecyclerAdapter.OnItemClickListener<Holder,Info> onItemClickListener) {
        if(onItemClickListener!=null)
        {
            mOnItemClickListener = onItemClickListener;
        }
    }

    /**
     * 设置长按监听器
     * @param onItemLongClickListener {@link OnItemLongClickListener}
     */
    public void setOnItemLongClickListener(BaseRecyclerAdapter.OnItemLongClickListener<Holder,Info> onItemLongClickListener) {
        if(onItemLongClickListener!=null)
        {
            mOnItemLongClickListener = onItemLongClickListener;
        }
    }

}
