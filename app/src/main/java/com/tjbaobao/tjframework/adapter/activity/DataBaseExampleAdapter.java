package com.tjbaobao.tjframework.adapter.activity;

import androidx.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.tjbaobao.framework.base.BaseRecyclerAdapter;
import com.tjbaobao.framework.ui.BaseRecyclerView;
import com.tjbaobao.tjframework.R;
import com.tjbaobao.tjframework.model.DataBaseExampleInfo;

import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 作者:TJbaobao
 * 时间:2018/9/25  11:22
 * 说明:
 * 使用：
 */
public class DataBaseExampleAdapter extends BaseRecyclerAdapter<DataBaseExampleAdapter.Holder,DataBaseExampleInfo> {

    public DataBaseExampleAdapter(List<DataBaseExampleInfo> dataBaseExampleInfos, int itemLayoutRes) {
        super(dataBaseExampleInfos, itemLayoutRes);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, @NonNull DataBaseExampleInfo info, int position) {
        holder.tvName.setText(info.name);
        holder.tvNum.setText(String.format(Locale.getDefault(),"%d", info.num));
    }

    @NonNull
    @Override
    public Holder onGetHolder(View view, int viewType) {
        return new Holder(view);
    }

    public class Holder extends BaseRecyclerView.BaseViewHolder
    {

        @BindView(R.id.tvName)
        TextView tvName ;

        @BindView(R.id.tvNum)
        TextView tvNum ;

        Holder(View itemView) {
            super(itemView);
        }

        @Override
        public void onInitView(View itemView) {
            ButterKnife.bind(this,itemView);
        }
    }
}
