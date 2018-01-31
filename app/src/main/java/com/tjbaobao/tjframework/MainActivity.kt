package com.tjbaobao.tjframework

import android.os.Bundle
import com.tjbaobao.framework.tjbase.TJActivity
import com.tjbaobao.framework.ui.BaseTitleBar
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : TJActivity() {


    override fun onInitValues(savedInstanceState: Bundle?) {

    }

    override fun onInitView() {
        setContentView(R.layout.activity_main)
        recyclerView.toListView()
        recyclerView.addListViewItemDecoration()
    }

    override fun onInitTitleBar(titleBar: BaseTitleBar?) {

    }

    override fun onLoadData() {

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
