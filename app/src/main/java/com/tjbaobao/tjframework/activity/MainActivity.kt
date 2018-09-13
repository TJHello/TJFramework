package com.tjbaobao.tjframework.activity

import android.os.Bundle
import com.tjbaobao.framework.base.BaseRecyclerAdapter
import com.tjbaobao.framework.ui.BaseTitleBar
import com.tjbaobao.framework.utils.*
import com.tjbaobao.tjframework.R
import com.tjbaobao.tjframework.adapter.activity.MainActivityAdapter
import com.tjbaobao.tjframework.adapter.activity.TJActivityExampleAdapter
import com.tjbaobao.tjframework.base.AppActivity
import com.tjbaobao.tjframework.model.MainActivityInfo
import com.tjbaobao.tjframework.model.enums.MainActivityEnum
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppActivity() {


    private val infoList = mutableListOf<MainActivityInfo>()
    private var adapter = MainActivityAdapter(infoList,R.layout.main_activity_item_layout)

    override fun onInitValues(savedInstanceState: Bundle?) {
    }

    override fun onInitView() {
        setContentView(R.layout.activity_main)
        recyclerView.toGridView(2)
        recyclerView.addGridAverageCenterDecoration(Tools.dpToPx(8),Tools.dpToPx(8))
        recyclerView.adapter = adapter
        adapter.setOnItemClickListener(OnItemClickListener())
    }

    override fun onLoadData() {
        infoList.clear()
        for(mainEnum in MainActivityEnum.values())
        {
            infoList.add(MainActivityInfo(mainEnum.description,mainEnum.activityClass))
        }
        adapter.notifyDataSetChanged()
    }

    override fun onInitTitleBar(titleBar: BaseTitleBar) {
        titleBar.setTitle(getString(R.string.app_name))
    }

    private inner class OnItemClickListener : BaseRecyclerAdapter.OnItemClickListener<MainActivityAdapter.Holder, MainActivityInfo>
    {
        override fun onItemClick(holder: MainActivityAdapter.Holder, info: MainActivityInfo, position: Int) {

            this@MainActivity.startActivity(info.activityClass, arrayOf("title"),info.name)

        }
    }
}
