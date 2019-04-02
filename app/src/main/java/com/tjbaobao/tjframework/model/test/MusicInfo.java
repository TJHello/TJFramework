package com.tjbaobao.tjframework.model.test;

import java.io.Serializable;

/**
 * 作者:TJbaobao
 * 时间:2019/4/2  10:14
 * 说明:
 * 使用：
 */
public class MusicInfo implements Serializable {

    public String code ;
    public String headImg ;
    public String screenImg;
    public String musicUrl ;
    public String lrcUrl ;
    public String name ;
    public String singer ;//歌手
    public String category ;//类目
    public boolean isFree ;
    public boolean isLike ;
    public int currentPosition ;
    public int duration ;

    public MusicInfo toInfo(MusicResponseInfo info){
        this.code = info.code;
        this.headImg = info.headImg;
        this.screenImg = info.screenImg;
        this.musicUrl = info.musicUrl;
        this.lrcUrl = info.lrcUrl;
        this.name = info.name;
        this.singer = info.singer;
        this.category = info.category;
        this.isFree = info.isFree;
        this.isLike = info.isLike;
        return this;
    }

    public MusicInfo setCurrentPosition(int currentPosition) {
        this.currentPosition = currentPosition;
        return this;
    }

    public MusicInfo setDuration(int duration) {
        this.duration = duration;
        return this;
    }
}
