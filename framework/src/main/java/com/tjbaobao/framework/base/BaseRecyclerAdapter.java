package com.tjbaobao.framework.base;

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
 * Created by TJbaobao on 2018/1/6.
 */

public abstract class BaseRecyclerAdapter<Holder extends BaseRecyclerView.BaseViewHolder,Info> extends Adapter<Holder> {

    private BaseRecyclerAdapter.OnItemClickListener<Holder,Info> mOnItemClickListener ;
    private List<Info> infoList = new ArrayList<>();
    private int itemLayoutRes ;
    private Map<Object,Holder> mapHolder = new HashMap<>();

    public BaseRecyclerAdapter(List<Info> infoList, int itemLayoutRes) {
        this.infoList = infoList;
        this.itemLayoutRes = itemLayoutRes;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(parent.getContext()).inflate(itemLayoutRes, null);
        return onGetHolder(view);
    }

    @Override
    public void onViewRecycled(Holder holder) {
        super.onViewRecycled(holder);
        mapHolder.put(holder.itemView.getTag(),null);
    }

    @Override
    public void onBindViewHolder(@Nullable Holder holder, int position) {
        Info info = infoList.get(position);
        onBindViewHolder(holder,info,position);
        Object tag = onGetItemTag(info,position);
        if(tag!=null)
        {
            mapHolder.put(tag,holder);
        }
        holder.itemView.setOnClickListener(new ItemOnClickListener(holder,info,position));
    }

    public abstract void onBindViewHolder(@Nullable Holder holder,@Nullable Info info, int position);

    public Object onGetItemTag(Info info,int position)
    {
        return null;
    }

    public abstract Holder onGetHolder(View view);

    @Override
    public int getItemCount() {
        return infoList.size();
    }


    public Holder findHolder(Object tag)
    {
        return mapHolder.get(tag);
    }

    public interface OnItemClickListener<Holder,Info>
    {
        void onItemClick(Holder holder, Info info, int position);
    }


    private class ItemOnClickListener implements View.OnClickListener
    {
        private Holder mHolder ;
        private Info info ;
        private int position;

        public ItemOnClickListener(Holder holder, Info info, int position) {
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

    public BaseRecyclerAdapter.OnItemClickListener getOnItemClickListener() {
        return mOnItemClickListener;
    }

    public void setOnItemClickListener(BaseRecyclerAdapter.OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }
}
