package com.tjbaobao.framework.entity.ui;

import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tjbaobao.framework.R;
import com.tjbaobao.framework.util.Tools;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by TJbaobao on 2018/1/5.
 */

public class TitleBarInfo {

    private List<BaseView> viewLeftList = new ArrayList<>();
    private List<BaseView> viewRightList = new ArrayList<>();

    private Text viewTitle = null;

    private int btWidth = Tools.dpToPx(56);
    private int btHeight = Tools.dpToPx(56);

    private int backGroundColor =Tools.getResColor(R.color.fw_theme_color);
    private String titleText = null;
    private int textColor = Color.WHITE ;
    private int textSize = 18 ;
    private int imagePadding = Tools.dpToPx(16);
    private int viewBackground = R.drawable.fw_ripple;

    public class BaseView<V extends View>
    {
        private V view ;
        private int width,height;

        public V getView() {
            return view;
        }

        public void setView(V view) {
            this.view = view;
        }

        public int getWidth() {
            return width;
        }

        public void setWidth(int width) {
            this.width = width;
        }

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }
    }

    public class Image extends BaseView<ImageView>{
        private int imageRes ;
        private int padding =  imagePadding;
        private int background = viewBackground;

        public Image(int imageRes) {
            this.imageRes = imageRes;
        }

        public int getImageRes() {
            return imageRes;
        }

        public void setImageRes(int imageRes) {
            this.imageRes = imageRes;
        }

        public int getPadding() {
            return padding;
        }

        public void setPadding(int padding) {
            this.padding = padding;
        }

        public int getBackground() {
            return background;
        }

        public void setBackground(int background) {
            this.background = background;
        }
    }

    public class Text extends BaseView<TextView>
    {
        private String text ;
        private int textColor = TitleBarInfo.this.textColor;
        private float textSize = TitleBarInfo.this.textSize;
        private int background = viewBackground;

        public Text(String text) {
            this.text = text;
        }

        public Text(String text, int textColor) {
            this.text = text;
            this.textColor = textColor;
        }

        public Text(String text, int textColor, float textSize) {
            this.text = text;
            this.textColor = textColor;
            this.textSize = textSize;
        }

        public Text(String text, float textSize) {
            this.text = text;
            this.textSize = textSize;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public int getTextColor() {
            return textColor;
        }

        public void setTextColor(int textColor) {
            this.textColor = textColor;
        }

        public float getTextSize() {
            return textSize;
        }

        public void setTextSize(float textSize) {
            this.textSize = textSize;
        }


        public int getBackground() {
            return background;
        }

        public void setBackground(int background) {
            this.background = background;
        }
    }

    public <V extends BaseView> void  addLeftFirst(V view)
    {
        viewLeftList.add(0,view);
    }
    public <V extends BaseView> void  addRightFirst(V view)
    {
        addLeft(0,view);
    }

    public <V extends BaseView> void  addLeft(V view)
    {
        viewLeftList.add(view);
    }

    public <V extends BaseView> void addLeft(int position ,V view)
    {
        if(position<0)
        {
            position = 0;
        }
        viewLeftList.add(position,view);
    }

    public <V extends BaseView> void addRight(int position,V view)
    {
        if(position<0)
        {
            position = 0;
        }
        viewRightList.add(position,view);
    }

    public <V extends BaseView> void  addRight(V view)
    {
        viewRightList.add(view);
    }

    public static TitleBarInfo getDefTitleBar()
    {
        TitleBarInfo titleBarInfo = new TitleBarInfo() ;
        titleBarInfo.setViewTitle(titleBarInfo.new Text(Tools.getResString(R.string.fb_titlebar_text_title)));
        return titleBarInfo;
    }

    public List<BaseView> getViewLeftList() {
        return viewLeftList;
    }

    public void setViewLeftList(List<BaseView> viewLeftList) {
        this.viewLeftList = viewLeftList;
    }

    public List<BaseView> getViewRightList() {
        return viewRightList;
    }

    public void setViewRightList(List<BaseView> viewRightList) {
        this.viewRightList = viewRightList;
    }


    public Text getViewTitle() {
        return viewTitle;
    }

    public void setViewTitle(Text viewTitle) {
        this.viewTitle = viewTitle;
    }

    public int getBtWidth() {
        return btWidth;
    }

    public void setBtWidth(int btWidth) {
        this.btWidth = btWidth;
    }

    public int getBtHeight() {
        return btHeight;
    }

    public void setBtHeight(int btHeight) {
        this.btHeight = btHeight;
    }

    public int getBackGroundColor() {
        return backGroundColor;
    }

    public void setBackGroundColor(int backGroundColor) {
        this.backGroundColor = backGroundColor;
    }

    public String getTitleText() {
        return titleText;
    }

    public void setTitleText(String titleText) {
        this.titleText = titleText;
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    public int getTextSize() {
        return textSize;
    }

    public void setTextSize(int textSize) {
        this.textSize = textSize;
    }
}
