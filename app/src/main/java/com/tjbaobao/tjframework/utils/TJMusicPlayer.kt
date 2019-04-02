package com.tjbaobao.tjframework.utils

import android.content.Intent
import android.media.MediaPlayer
import android.os.Handler
import com.tjbaobao.framework.base.BaseApplication
import com.tjbaobao.framework.database.dao.TbFileDAO
import com.tjbaobao.framework.utils.*
import com.tjbaobao.tjframework.model.test.MusicInfo
import com.tjbaobao.tjframework.model.test.MusicResponseInfo
import io.paperdb.Paper
import io.paperdb.Paper.book
import java.io.File
import java.util.*


/**
 * 作者:TJbaobao
 * 时间:2019/4/2  10:09
 * 说明:
 * 使用：
 */
class TJMusicPlayer private constructor() {

    private val handler = BaseHandler(Handler.Callback {
        when(it.what){
            MESSAGE_WHAT_PRE_FILE->{
                if(musicInfo!=null){
                    preThenPlay(musicInfo!!.lrcUrl)
                }
            }
        }
        true
    })

    companion object {

        private const val PAPER_BOOK_INFO = "tj_music_player"
        private const val PAPER_BOOK_INFO_KEY = "info"

        private const val PAPER_BOOK_LIST = "tj_music_player_list"

        private const val MESSAGE_WHAT_PRE_FILE = 101

        const val BROADCAST_ACTION = "music_player_broadcast"

        private val musicPlayer = TJMusicPlayer()
        fun build():TJMusicPlayer{
            return musicPlayer
        }

        enum class PlayMode{
            LOOP_LIST,//列表循环
            LOOP_ONE,//单曲循环
            LOOP_RANDOM,//随机循环
            ONLY_ONE

        }

    }

    private var mediaPlayer : MediaPlayer ?= null
    private var musicInfo : MusicInfo? = Paper.book(PAPER_BOOK_INFO).read<MusicInfo>(PAPER_BOOK_INFO_KEY,null)
    private var isPlaying = false
    private var isDestroy = false
    var playMode = PlayMode.LOOP_LIST
    private var musicPlayList: MutableList<MusicResponseInfo> = mutableListOf()
    private var musicPlayPosition = -1

    init {
        for(key in Paper.book(PAPER_BOOK_LIST).allKeys){
            val info = Paper.book(PAPER_BOOK_LIST).read<MusicResponseInfo>(key)
            musicPlayList.add(info)
        }
        if(musicInfo!=null){
            musicPlayPosition = contains(musicInfo!!.code)
        }
    }

    private val timerTask = object :BaseTimerTask(){
        override fun run() {
            if(musicInfo!=null&&isPlaying){
                sendBroadcast(musicInfo!!)
            }
        }
    }

    fun play(){
        if(isDestroy){
            if(musicInfo!=null&&!isPlaying){
                preThenPlay(musicInfo!!.musicUrl)
            }
        }else{
            if(!isPlaying){
                mediaPlayer?.start()
                timerTask.startTimer(0,1000)
            }
        }
    }

    fun pause(){
        if(isPlaying){
            isPlaying = false
            mediaPlayer?.pause()
            if(musicInfo!=null){
                sendBroadcast(musicInfo!!)
            }
            timerTask.stopTimer()
        }
    }

    fun play(info: MusicResponseInfo){
        val isEquals = musicInfo!=null&&musicInfo!!.code == info.code
        if(!isPlaying||!isEquals){
            val findPosition = contains(info.code)
            if(findPosition!=-1){
                //如果播放列表中已经存在该歌曲
                musicPlayPosition = findPosition
            }else{
                //否则，则添加到播放列表
                musicPlayPosition++
                musicPlayList.add(musicPlayPosition,info)
                Paper.book(PAPER_BOOK_LIST).write(info.code,info)
            }
            preThenPlay(info.musicUrl)
            musicInfo = MusicInfo().toInfo(info)
            Paper.book(PAPER_BOOK_INFO).write(PAPER_BOOK_INFO_KEY,musicInfo)
        }
    }

    fun remove(info:MusicResponseInfo){
        Paper.book(PAPER_BOOK_LIST).delete(info.code)
        remove(info.code)
        if(musicInfo!=null){
            if(musicInfo!!.code==info.code){
                musicInfo = MusicInfo().toInfo(musicPlayList[musicPlayPosition])
                if(isPlaying){
                    play()
                }
            }else{
                musicPlayPosition = contains(info.code)
            }
        }
    }

    fun destroy(){
        if(isPlaying){
            isPlaying = false
            isDestroy = true
            mediaPlayer?.stop()
            mediaPlayer?.release()
            mediaPlayer = null
        }
    }

    private fun preThenPlay(url:String,delayed:Long=3000L){
        var path = TbFileDAO.getFilePathByUrl(url)
        if(path.isNullOrEmpty()||!FileUtil.exists(path)){
            path = ConstantUtil.getCachePath()+"music"+ File.separator+UUID.randomUUID().toString()+"."+FileUtil.getPrefix(path)
            RxJavaUtil.runOnIOThread<Any> {
                OKHttpUtil.download(url,path)
                handler.removeMessages(MESSAGE_WHAT_PRE_FILE)
                initMediaPlayer(path)
            }
            handler.removeMessages(MESSAGE_WHAT_PRE_FILE)
            handler.sendEmptyMessageDelayed(MESSAGE_WHAT_PRE_FILE,delayed)
        }else{
            initMediaPlayer(path)
        }
    }

    private fun initMediaPlayer(path:String){
        destroy()
        mediaPlayer = MediaPlayer()
        mediaPlayer?.setOnPreparedListener {
            it.seekTo(musicInfo?.currentPosition?:0)
            it.start()
            timerTask.startTimer(0,1000)
            isPlaying = true
        }
        mediaPlayer?.setOnErrorListener { mp, what, extra ->
            isPlaying = false
            if(musicInfo!=null){
                preThenPlay(musicInfo!!.lrcUrl)
            }
            false
        }
        mediaPlayer?.setDataSource(path)
        mediaPlayer?.prepareAsync()
        isDestroy = false
    }

    private fun sendBroadcast(info:MusicInfo){
        val intent = Intent(BROADCAST_ACTION)
        intent.putExtra("info",info)
        intent.putExtra("isPlaying",isPlaying)
        BaseApplication.context.sendBroadcast(intent)
    }

    private fun contains(code:String):Int{
        for((i, tempInfo) in musicPlayList.withIndex()){
            if(tempInfo.code==code){
                return i
            }
        }
        return -1
    }

    private fun remove(code:String){
        for((i, tempInfo) in musicPlayList.withIndex()){
            if(tempInfo.code==code){
                musicPlayList.removeAt(i)
                return
            }
        }
    }

}