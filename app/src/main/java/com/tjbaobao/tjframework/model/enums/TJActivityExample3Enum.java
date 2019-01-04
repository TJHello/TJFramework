package com.tjbaobao.tjframework.model.enums;

import com.tjbaobao.tjframework.R;
import com.tjbaobao.tjframework.model.TJActivityExample3Info;

/**
 * 作者:TJbaobao
 * 时间:2019/1/3  13:50
 * 说明:
 * 使用：
 */
public enum TJActivityExample3Enum {

    MUSIC_TITLE(R.drawable.ic_music_sdvanced,R.string.index_advanced_title_music,TJActivityExample3Info.TITLE_SWITCH),
    MUSIC_LIST(0,0,TJActivityExample3Info.LIST_MUSIC),
    ANIMATION_TITLE(R.drawable.ic_animation_sdvanced,R.string.index_advanced_title_animation,TJActivityExample3Info.TITLE_SWITCH),
    ANIMATION_LIST(0,0,TJActivityExample3Info.LIST_ANIM),
    SOUND_TITLE(R.drawable.ic_sound_sdvanced,R.string.index_advanced_title_sound,TJActivityExample3Info.TITLE_SWITCH),
    VIBRATION_TITLE(R.drawable.ic_taptic_sdvanced,R.string.index_advanced_title_vibration,TJActivityExample3Info.TITLE_SWITCH),
    THUMBNAIL_TITLE(R.drawable.ic_thumbnail_sdvanced,R.string.index_advanced_title_thumbnail,TJActivityExample3Info.TITLE_SWITCH),
    WATERMARK_TITLE(R.drawable.ic_watermark_sdvanced,R.string.index_advanced_title_watermark,TJActivityExample3Info.TITLE_SWITCH),
    MARGIN1(0,0,TJActivityExample3Info.MARGIN),
    TUTORIAL_TITLE(R.drawable.ic_tutorial_sdvanced,R.string.index_advanced_title_tutorial,TJActivityExample3Info.TITLE_NONE),
    TUTORIAL_LAYOUT(0,0,TJActivityExample3Info.LAYOUT_TUTORIAL),
    RATE(R.drawable.ic_rate_sdvanced,R.string.index_advanced_title_rate,TJActivityExample3Info.TITLE_MENU),
    FEEDBACK(R.drawable.ic_feedback_sdvanced,R.string.index_advanced_title_feedback,TJActivityExample3Info.TITLE_MENU),
    MARGIN2(0,0,TJActivityExample3Info.MARGIN),
    TERMS(R.drawable.ic_terms_sdvanced,R.string.index_advanced_title_terms,TJActivityExample3Info.TITLE_MENU),
    PRIVACY(R.drawable.ic_privacy_sdvanced,R.string.index_advanced_title_privacy,TJActivityExample3Info.TITLE_MENU),
    ;


    public int iconResId ;
    public int titleResId ;
    public int type ;

    TJActivityExample3Enum(int iconResId, int titleResId, int type) {
        this.iconResId = iconResId;
        this.titleResId = titleResId;
        this.type = type;
    }


}
