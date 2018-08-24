package com.tjbaobao.tjframework.activity

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.support.v4.content.FileProvider
import android.view.View
import android.widget.ImageView
import com.tjbaobao.framework.base.BaseRecyclerAdapter
import com.tjbaobao.framework.entity.ui.TitleBarInfo
import com.tjbaobao.framework.listener.OnProgressListener
import com.tjbaobao.framework.tjbase.TJActivity
import com.tjbaobao.framework.ui.BaseRecyclerView
import com.tjbaobao.framework.ui.BaseTitleBar
import com.tjbaobao.framework.utils.*
import com.tjbaobao.tjframework.dialog.ImageDialog
import com.tjbaobao.tjframework.R
import com.tjbaobao.tjframework.dialog.TJDialogExample
import com.tjbaobao.tjframework.model.MainActivityTestModel
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.io.InputStream

class MainActivity : TJActivity() {


    private lateinit var adapter : MyAdapter
    private val infoList : MutableList <MainActivityTestModel> = ArrayList()
    private var resourcesGetTools : ResourcesGetTools ?= null

    override fun onInitValues(savedInstanceState: Bundle?) {
    }

    override fun onInitView() {
        setContentView(R.layout.activity_main)
        recyclerView.toGridView(2)
        recyclerView.addGridAverageCenterDecoration(Tools.dpToPx(8f),Tools.dpToPx(8f))
        adapter = MyAdapter(infoList,R.layout.main_activity_item_layout)
        recyclerView.adapter = adapter
        adapter.setOnItemClickListener(OnItemClickListener())
        resourcesGetTools = ResourcesGetTools(this)
        resourcesGetTools!!.onResourcesGetListener = OnResourcesGetListener()

    }

    override fun onInitTitleBar(titleBar: BaseTitleBar) {
        titleBar.addTextToRight("导入相片")
    }

    override fun <V : TitleBarInfo.BaseView<*>?> onTitleBarClick(layoutType: Int, position: Int, info: V) {
        if(layoutType==BaseTitleBar.LAYOUT_RIGHT)
        {
            if(position==0)
            {
                resourcesGetTools!!.getImgByGallery()
            }
        }
    }

    override fun onLoadData() {
        infoList.clear()
        infoList.add(MainActivityTestModel("https://firebasestorage.googleapis.com/v0/b/jigsaw-puzzle-52ba2.appspot.com/o/deer%2Fdeer%2Fdeer-01_4.jpg?alt=media"))
        infoList.add(MainActivityTestModel("https://firebasestorage.googleapis.com/v0/b/jigsaw-puzzle-52ba2.appspot.com/o/deer%2Fdeer%2Fdeer-02_4.jpg?alt=media"))
        infoList.add(MainActivityTestModel("https://firebasestorage.googleapis.com/v0/b/jigsaw-puzzle-52ba2.appspot.com/o/deer%2Fdeer%2Fdeer-03_m_4.jpg?alt=media"))
        infoList.add(MainActivityTestModel("https://firebasestorage.googleapis.com/v0/b/jigsaw-puzzle-52ba2.appspot.com/o/deer%2Fdeer%2Fdeer-04_4.jpg?alt=media"))
        infoList.add(MainActivityTestModel("https://firebasestorage.googleapis.com/v0/b/jigsaw-puzzle-52ba2.appspot.com/o/deer%2Fdeer%2Fdeer-05_m_4.jpg?alt=media"))
        infoList.add(MainActivityTestModel("https://firebasestorage.googleapis.com/v0/b/jigsaw-puzzle-52ba2.appspot.com/o/deer%2Fdeer%2Fdeer-06_m_4.jpg?alt=media"))
        infoList.add(MainActivityTestModel("https://firebasestorage.googleapis.com/v0/b/jigsaw-puzzle-52ba2.appspot.com/o/deer%2Fdeer%2Fdeer-07_m_4.jpg?alt=media"))
        infoList.add(MainActivityTestModel("https://firebasestorage.googleapis.com/v0/b/jigsaw-puzzle-52ba2.appspot.com/o/deer%2Fdeer%2Fdeer-08_4.jpg?alt=media"))
        infoList.add(MainActivityTestModel("https://firebasestorage.googleapis.com/v0/b/jigsaw-puzzle-52ba2.appspot.com/o/deer%2Fdeer%2Fdeer-09_m_4.jpg?alt=media"))
        infoList.add(MainActivityTestModel("https://firebasestorage.googleapis.com/v0/b/jigsaw-puzzle-52ba2.appspot.com/o/deer%2Fdeer%2Fdeer-10_m_4.jpg?alt=media"))
        adapter.notifyDataSetChanged()
    }

    private inner class MyAdapter(infoList: MutableList <MainActivityTestModel>?, itemLayoutRes: Int) : BaseRecyclerAdapter<MainActivity.Holder, MainActivityTestModel>(infoList, itemLayoutRes),ImageDownloader.OnImageLoaderListener
    {
        override fun onBindViewHolder(holder: Holder, info: MainActivityTestModel, position: Int) {
            imageDownloader.load(info.url,holder.ivImage, OnItemProgressListener())
        }

        private var imageDownloader :ImageDownloader = ImageDownloader.getInstance()

        init {
            imageDownloader.onImageLoaderListener = this
            imageDownloader.setDefaultImgSize(300,300)
        }

        override fun onGetHolder(view: View?,viewType : Int): Holder {
            return Holder(view)
        }

        override fun onSuccess(url: String?, path: String?, bitmap: Bitmap?) {
            Tools.printLog("加载成功:$url")
        }

        override fun onFail(url: String?) {
            Tools.printLog("加载失败:$url")
        }

        inner class OnItemProgressListener : OnProgressListener()
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

    private inner class Holder(itemView: View?) : BaseRecyclerView.BaseViewHolder(itemView) {
        var ivImage :ImageView ?= null
        override fun onInitView(itemView: View?) {
            ivImage = itemView!!.findViewById(R.id.ivImage)
        }
    }

    private var imageDialog : TJDialogExample? = null

    private inner class OnItemClickListener : BaseRecyclerAdapter.OnItemClickListener<Holder,MainActivityTestModel>
    {
        override fun onItemClick(holder: Holder, info: MainActivityTestModel, position: Int) {
            if(imageDialog==null)
            {
                imageDialog = TJDialogExample(context)
            }
            imageDialog!!.show(info.url)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        resourcesGetTools!!.onActivityResult(requestCode,resultCode,data)
    }

    private inner class OnResourcesGetListener : ResourcesGetTools.OnResourcesGetListener
    {
        override fun onSuccess(requestCode: Int, inputStream: InputStream?, data: Intent?) {
            val inPath = ConstantUtil.getImageCachePath()+"test_in.jpg"
            val outPath = ConstantUtil.getImageCachePath()+"test_out.jpg"
            FileUtil.copyFile(inputStream,inPath)
            val uriIn = FileProvider.getUriForFile(context,getString(R.string.authorities), File(inPath))
            val uriOut = FileProvider.getUriForFile(context,getString(R.string.authorities), File(outPath))
            resourcesGetTools!!.startCutFromGallery(uriIn,uriOut)
        }

        override fun onSuccess(requestCode: Int, path: String?, data: Intent?) {
            if(requestCode==105)
            {
                if(imageDialog==null)
                {
                    imageDialog = TJDialogExample(context)
                }
                imageDialog!!.show(path)
            }
        }

        override fun onFail(requestCode: Int, resultCode: Int) {

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
