package com.tjbaobao.tjframework.model;

import com.tjbaobao.framework.base.BaseAdapter;

/**
 * Created by TJbaobao on 2018/2/9.
 */

public class MainActivityTestModel {
    private String url ;

    public MainActivityTestModel()
    {

    }

    public MainActivityTestModel(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
