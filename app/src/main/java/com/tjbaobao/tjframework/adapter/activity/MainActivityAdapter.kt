package com.tjbaobao.tjframework.adapter.activity

import android.view.View
import com.tjbaobao.framework.base.BaseRecyclerAdapter
import com.tjbaobao.framework.ui.BaseRecyclerView
import com.tjbaobao.framework.utils.DeviceUtil
import com.tjbaobao.framework.utils.ImageDownloader
import com.tjbaobao.tjframework.model.MainActivityInfo
import kotlinx.android.synthetic.main.main_activity_item_layout.view.*

/**
 * 作者:TJbaobao
 * 时间:2018/9/13  11:28
 * 说明:
 * 使用：
 */
class MainActivityAdapter(infoList: MutableList<MainActivityInfo>?, itemLayoutRes: Int) : BaseRecyclerAdapter<MainActivityAdapter.Holder, MainActivityInfo>(infoList, itemLayoutRes) {

    override fun onBindViewHolder(holder: Holder, info: MainActivityInfo, position: Int) {
        holder.itemView.tvDescription.text = info.description
        holder.itemView.tvName.text = info.name

    }

    override fun onGetHolder(view: View?, type: Int): Holder {
        return Holder(view)
    }

    class Holder(itemView: View?) : BaseRecyclerView.BaseViewHolder(itemView) {
        override fun onInitView(itemView: View?) {

        }
    }

}