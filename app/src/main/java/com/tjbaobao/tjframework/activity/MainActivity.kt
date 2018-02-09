package com.tjbaobao.tjframework.activity

import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import com.tjbaobao.framework.base.BaseRecyclerAdapter
import com.tjbaobao.framework.listener.OnProgressListener
import com.tjbaobao.framework.tjbase.TJActivity
import com.tjbaobao.framework.ui.BaseRecyclerView
import com.tjbaobao.framework.ui.BaseTitleBar
import com.tjbaobao.framework.utils.ImageDownloader
import com.tjbaobao.framework.utils.Tools
import com.tjbaobao.tjframework.R
import com.tjbaobao.tjframework.model.MainActivityTestModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : TJActivity() {

    private lateinit var adapter : MyAdapter
    private val infoList : MutableList <MainActivityTestModel> = ArrayList()

    override fun onInitValues(savedInstanceState: Bundle?) {

    }

    override fun onInitView() {
        setContentView(R.layout.activity_main)
        recyclerView.toListView()
        recyclerView.addListViewItemDecoration()

        adapter = MyAdapter(infoList,R.layout.main_activity_item_layout)
        recyclerView.adapter = adapter
    }

    override fun onInitTitleBar(titleBar: BaseTitleBar?) {

    }

    override fun onLoadData() {
        infoList.clear()
        for(i in 0..10)
        {
            infoList.add(MainActivityTestModel("https://firebasestorage.googleapis.com/v0/b/jigsaw-puzzle-52ba2.appspot.com/o/deer%2Fdeer%2Fdeer-01_4.jpg?alt=media"))
        }
        adapter.notifyDataSetChanged()
    }

    class MyAdapter(infoList: MutableList <MainActivityTestModel>?, itemLayoutRes: Int) : BaseRecyclerAdapter<MainActivity.Holder, MainActivityTestModel>(infoList, itemLayoutRes),ImageDownloader.OnImageLoaderListener
    {
        var imageDownloader :ImageDownloader = ImageDownloader.getInstance()

        init {
            imageDownloader.onImageLoaderListener = this
        }

        override fun onBindViewHolder(holder: Holder?, info: MainActivityTestModel?, position: Int) {
            imageDownloader.load(info!!.url,holder!!.imageView,onItemProgressListener())
        }

        override fun onGetHolder(view: View?): Holder {
            return Holder(view)
        }

        override fun onSuccess(url: String?, path: String?, bitmap: Bitmap?) {
            Tools.printLog("加载成功:$url")
        }

        override fun onFail(url: String?) {
            Tools.printLog("加载失败:$url")
        }

        class onItemProgressListener : OnProgressListener
        {
            override fun onProgress(progress: Float, isFinish: Boolean) {

            }

            override fun onProgress(sizeProgress: Long, sizeTotal: Long) {
                super.onProgress(sizeProgress, sizeTotal)
                Tools.printLog("onProgress")
            }

            override fun onProgress(tag: Any?, sizeProgress: Long, sizeTotal: Long) {
                super.onProgress(tag, sizeProgress, sizeTotal)
                Tools.printLog("onProgress$tag")
            }
        }

    }

    class Holder(itemView: View?) : BaseRecyclerView.BaseViewHolder(itemView) {
        var imageView :ImageView ?= null
        override fun onInitView(itemView: View?) {
            imageView = itemView!!.findViewById(R.id.imageView)
        }
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    external fun stringFromJNI(): String

    companion object {

        // Used to load the 'native-lib' library on application startup.
        init {
            System.loadLibrary("native-lib")
        }
    }
}
