package com.tjbaobao.tjframework.base;

import android.database.sqlite.SQLiteDatabase;

import com.squareup.leakcanary.LeakCanary;
import com.tjbaobao.framework.base.BaseApplication;
import com.tjbaobao.framework.database.DataBaseHelper;
import com.tjbaobao.framework.database.TJDataBaseHelper;
import com.tjbaobao.framework.imp.DataBaseImp;
import com.tjbaobao.framework.utils.ImageDownloader;
import com.tjbaobao.tjframework.utils.AliyunOOSUtil;
import io.paperdb.Paper;

/**
 * 作者:TJbaobao
 * 时间:2018/9/25  10:22
 * 说明:
 * 使用：
 */
public class TJApplication extends BaseApplication implements DataBaseImp {

    @Override
    public void onCreate() {
        super.onCreate();
        //如果不继承，则使用BaseApplication.init(this);进行初始化
        //必须在manifests文件中设置以下代码，无论你是否打算用到BaseDataBaseHelper
        /*
            <meta-data android:name="database_name" android:value="TjFramework" />
            <meta-data android:name="database_version" android:value="1"/>
         */
//        DataBaseHelper.setDataBaseImp(this);//设置数据库初始化接口
        TJDataBaseHelper.setDataBaseImp(this);

        //配置图片加载器
        long maxMemory = Runtime.getRuntime().maxMemory();
        int useMemory = (int) (maxMemory/7);
        int bestMemory = 60*1024*1024;
        if(useMemory>bestMemory)
        {
            useMemory = bestMemory;
        }
        ImageDownloader.setCacheSize(useMemory);//自定义缓存池大小
        ImageDownloader.setLoadThreadNum(3);//自定义线程数量
        ImageDownloader.setIsStrictMode(false);//严格模式，开启后会立刻主动回收已经超出了缓存池的图片。(慎用，不建议普通场景使用,详细请进入方法内查看。)
        ImageDownloader.setIsSizeStrictMode(true);//尺寸严格模式，开启后则会根据setDefaultImgSize来将图片按照比例裁剪到指定的最小尺寸

        AliyunOOSUtil.init();

        Paper.init(this);

        LeakCanary.install(this);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //创建数据库
        String sql ;

        //测试表
        sql = "Create Table tb_event "
                +"("
                +"code varchar(36) primary key,"//唯一标志,UUID
                +"name varchar(36),"//名字
                +"path varchar(255),"//地址
                +"event varchar(16),"//链接
                +"create_time varchar(19),"//创建时间
                +"change_time varchar(19)"//最后一次修改时间
                +");";

        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //数据库升级
        //升级版本号只需要修改manifests中的database_version

    }
}
