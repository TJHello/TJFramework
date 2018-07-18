package com.tjbaobao.tjframework

import com.tjbaobao.framework.listener.RxTimerTaskListener
import com.tjbaobao.framework.utils.RxTimerTask
import org.junit.Test


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        val rxTimerTask = RxTimerTask()
        rxTimerTask.setRxTimerTaskListener(object : RxTimerTaskListener() {
            override fun run(time: Long) {
                System.out.println("run$time")
                if(time==5L)
                {
                    rxTimerTask.stop()
                }
            }
            override fun runUI(time: Long) {
                System.out.println("run$time")
            }
        })
        rxTimerTask.start(0,1000)
    }
}
