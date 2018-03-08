package com.tjbaobao.framework.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tjbaobao.framework.entity.ui.TitleBarInfo;
import com.tjbaobao.framework.ui.base.BaseRelativeLayout;

import java.util.List;

/**
 *多功能TitleBar，可以任意往左边或者右边或者中间加入ImageView或者TextView
 * Created by TJbaobao on 2018/1/5.
 */

@SuppressWarnings("unused")
public class BaseTitleBar extends BaseRelativeLayout {

    private static final int WRAP_CONTENT = LayoutParams.WRAP_CONTENT;
    private static final int MATCH_PARENT = LayoutParams.MATCH_PARENT;

    public static final int LAYOUT_LEFT = 0;
    public static final int LAYOUT_CENTER = 1;
    public static final int LAYOUT_RIGHT = 2;

    public BaseTitleBar(Context context) {
        super(context);
    }

    public BaseTitleBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BaseTitleBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private LinearLayout mLinearLayoutLeft ;
    private LinearLayout mLinearLayoutRight ;
    private TitleBarInfo mTitleBarInfo ;

    private List<TitleBarInfo.BaseView> leftViewList ;
    private List<TitleBarInfo.BaseView> rightViewList ;


    @Override
    protected void onInitView(Context context, AttributeSet attrs, int defStyleAttr) {
        super.onInitView(context, attrs, defStyleAttr);
        mLinearLayoutLeft = createLinearLayoutToLeft();
        mLinearLayoutRight = createLinearLayoutToRight();
        toDefTitleBar();
    }

    public void toDefTitleBar()
    {
        mTitleBarInfo = TitleBarInfo.getDefTitleBar() ;
        reLayout();
    }

    public void reLayout()
    {
        if(mLinearLayoutLeft==null||mLinearLayoutRight==null)
        {
            return ;
        }
        mLinearLayoutLeft.removeAllViews();
        mLinearLayoutRight.removeAllViews();
        removeAllViews();
        this.addView(mLinearLayoutLeft);
        this.addView(mLinearLayoutRight);
        leftViewList = mTitleBarInfo.getViewLeftList();
        rightViewList = mTitleBarInfo.getViewRightList();
        int position = 0;
        for(TitleBarInfo.BaseView baseView:leftViewList)
        {
            if(baseView instanceof TitleBarInfo.Image)
            {
                mLinearLayoutLeft.addView( createImageLeft((TitleBarInfo.Image) baseView));
            }
            else if(baseView instanceof TitleBarInfo.Text)
            {
                mLinearLayoutLeft.addView( createTextLeftView((TitleBarInfo.Text) baseView));
            }
            baseView.getView().setOnClickListener(new MyOnClickListener(LAYOUT_LEFT,position));
            position++;
        }
        position = 0;
        for(TitleBarInfo.BaseView baseView:rightViewList)
        {
            if(baseView instanceof TitleBarInfo.Image)
            {
                mLinearLayoutRight.addView( createImageLeft((TitleBarInfo.Image) baseView));
            }
            else if(baseView instanceof TitleBarInfo.Text)
            {
                mLinearLayoutRight.addView( createTextRightView((TitleBarInfo.Text) baseView));
            }
            baseView.getView().setOnClickListener(new MyOnClickListener(LAYOUT_RIGHT,position));
            position++;
        }
        TitleBarInfo.Text viewTitle = mTitleBarInfo.getViewTitle();
        if(mTitleBarInfo!=null)
        {
            if(viewTitle.getView()!=null)
            {
                this.removeView(viewTitle.getView());
            }
            this.addView(createTextTitleView(viewTitle));
        }
        super.setBackgroundColor(mTitleBarInfo.getBackGroundColor());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(mTitleBarInfo==null)
        {
            toDefTitleBar();
        }
    }

    //region================外部方法==================

    public void addImageToLeft(@DrawableRes int res)
    {
        TitleBarInfo.Image image = mTitleBarInfo.new Image(res);
        mTitleBarInfo.addLeft(image);
        reLayout();
    }

    public void addImageToRight(@DrawableRes int res)
    {
        TitleBarInfo.Image image = mTitleBarInfo.new Image(res);
        mTitleBarInfo.addRight(image);
        reLayout();
    }

    public void addImageToLeftFirst(@DrawableRes int res)
    {
        TitleBarInfo.Image image = mTitleBarInfo.new Image(res);
        mTitleBarInfo.addLeftFirst(image);
        reLayout();
    }

    public void addImageToRightFirst(@DrawableRes int res)
    {
        TitleBarInfo.Image image = mTitleBarInfo.new Image(res);
        mTitleBarInfo.addRightFirst(image);
        reLayout();
    }

    public void addImageToLeft(int position,TitleBarInfo.Image image)
    {
        mTitleBarInfo.addLeft(position,image);
        reLayout();
    }

    public void addImageToRight(int position,TitleBarInfo.Image image)
    {
        mTitleBarInfo.addRight(position,image);
        reLayout();
    }

    public void addTextToLeftFirst(String txt)
    {
        TitleBarInfo.Text text = mTitleBarInfo.new Text(txt);
        mTitleBarInfo.addLeftFirst(text);
        reLayout();
    }
    public void addTextToLeft(String txt)
    {
        TitleBarInfo.Text text = mTitleBarInfo.new Text(txt);
        mTitleBarInfo.addLeft(text);
        reLayout();
    }
    public void addTextToLeft(int position,TitleBarInfo.Text text)
    {
        mTitleBarInfo.addLeft(position,text);
        reLayout();
    }

    public void addTextToLeftRight(String txt)
    {
        TitleBarInfo.Text text = mTitleBarInfo.new Text(txt);
        mTitleBarInfo.addRightFirst(text);
        reLayout();
    }
    public void addTextToRight(String txt)
    {
        TitleBarInfo.Text text = mTitleBarInfo.new Text(txt);
        mTitleBarInfo.addRight(text);
        reLayout();
    }
    public void addTextToRight(int position,TitleBarInfo.Text text)
    {
        mTitleBarInfo.addRight(position,text);
        reLayout();
    }

    public TitleBarInfo getTitleBarInfo() {
        return mTitleBarInfo;
    }

    public void setTitleBarInfo(TitleBarInfo titleBarInfo) {
        mTitleBarInfo = titleBarInfo;
    }

    public void setTitle(String title)
    {
        mTitleBarInfo.getViewTitle().setText(title);
        reLayout();
    }

    public void removeView(int layoutType,int position)
    {
        if(layoutType==LAYOUT_LEFT)
        {
            leftViewList.remove(position);
        }
        else if(layoutType==LAYOUT_RIGHT)
        {
            rightViewList.remove(position);
        }
        else
        {
            mTitleBarInfo.setViewTitle(null);
        }
        reLayout();
    }

    @Override
    public void setBackgroundColor(@ColorRes int color)
    {
        mTitleBarInfo.setBackGroundColor(color);
    }
    //endregion

    //region================内部方法===================

    private LinearLayout createLinearLayoutToLeft()
    {
        LinearLayout linearLayout = new LinearLayout(context);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(WRAP_CONTENT,MATCH_PARENT);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT,RelativeLayout.TRUE);
        layoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
        linearLayout.setLayoutParams(layoutParams);
        return linearLayout;
    }

    private LinearLayout createLinearLayoutToRight()
    {
        LinearLayout linearLayout = new LinearLayout(context);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(WRAP_CONTENT,MATCH_PARENT);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT,RelativeLayout.TRUE);
        layoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
        linearLayout.setLayoutParams(layoutParams);
        return linearLayout;
    }

    private TextView createTextTitleView(TitleBarInfo.Text text)
    {
        TextView textView = createTextView(text);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(WRAP_CONTENT,WRAP_CONTENT);
        layoutParams.addRule(CENTER_IN_PARENT);
        textView.setLayoutParams(layoutParams);
        return textView;
    }

    @SuppressLint("RtlHardcoded")
    private TextView createTextLeftView(TitleBarInfo.Text text)
    {
        TextView textView = createTextView(text);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(mTitleBarInfo.getBtWidth(),mTitleBarInfo.getBtHeight());
        layoutParams.gravity = Gravity.LEFT|CENTER_VERTICAL;
        textView.setLayoutParams(layoutParams);
        return textView;
    }

    @SuppressLint("RtlHardcoded")
    private TextView createTextRightView(TitleBarInfo.Text text)
    {
        TextView textView = createTextView(text);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(mTitleBarInfo.getBtWidth(),mTitleBarInfo.getBtHeight());
        layoutParams.gravity = Gravity.RIGHT |CENTER_VERTICAL;
        textView.setLayoutParams(layoutParams);
        return textView;
    }

    private TextView createTextView(TitleBarInfo.Text text)
    {
        TextView textView = new TextView(context);
        text.setView(textView);
        textView.setTextColor(text.getTextColor());
        textView.setText(text.getText());
        textView.setTextSize(text.getTextSize());
        textView.setBackgroundResource(text.getBackground());
        textView.setGravity(Gravity.CENTER);
        textView.setClickable(true);
        return textView;
    }

    @SuppressLint("RtlHardcoded")
    private ImageView createImageLeft(TitleBarInfo.Image image)
    {
        ImageView imageView = createImage(image);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(mTitleBarInfo.getBtWidth(),mTitleBarInfo.getBtHeight());
        layoutParams.gravity = Gravity.LEFT |Gravity.CENTER_VERTICAL;
        imageView.setLayoutParams(layoutParams);
        return imageView;
    }

    @SuppressLint("RtlHardcoded")
    private ImageView createImageRight(TitleBarInfo.Image image)
    {
        ImageView imageView = createImage(image);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(mTitleBarInfo.getBtWidth(),mTitleBarInfo.getBtHeight());
        layoutParams.gravity = Gravity.RIGHT |Gravity.CENTER_VERTICAL ;
        imageView.setLayoutParams(layoutParams);
        return imageView;
    }

    private ImageView createImageTitle(TitleBarInfo.Image image)
    {
        ImageView imageView = createImage(image);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(mTitleBarInfo.getBtWidth(),mTitleBarInfo.getBtHeight());
        layoutParams.addRule(CENTER_IN_PARENT);
        imageView.setLayoutParams(layoutParams);
        return imageView;
    }

    private ImageView createImage(TitleBarInfo.Image image)
    {
        ImageView imageView = new ImageView(context);
        imageView.setImageResource(image.getImageRes());
        image.setView(imageView);
        imageView.setPadding(image.getPadding(),image.getPadding(),image.getPadding(),image.getPadding());
        imageView.setBackgroundResource(image.getBackground());
        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        imageView.setClickable(true);
        return imageView;
    }

    //endregion


    private class MyOnClickListener implements OnClickListener
    {
        private int layoutType ;
        private int position ;

        MyOnClickListener(int layoutType, int position) {
            this.layoutType = layoutType;
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            if(layoutType==LAYOUT_LEFT)
            {
                TitleBarInfo.BaseView baseView = leftViewList.get(position);
                if(mOnTitleBarClickListener!=null)
                {
                    mOnTitleBarClickListener.onTitleBarClick(layoutType,position,baseView);
                }
            }
            else if(layoutType==LAYOUT_RIGHT)
            {
                TitleBarInfo.BaseView baseView = rightViewList.get(position);
                if(mOnTitleBarClickListener!=null)
                {
                    mOnTitleBarClickListener.onTitleBarClick(layoutType,position,baseView);
                }
            }
        }
    }

    private OnTitleBarClickListener mOnTitleBarClickListener;
    public interface OnTitleBarClickListener
    {
        /**
         * TitleBar的点击事件监听器
         * @param layoutType 布局位置
         *                   LAYOUT_LEFT左边
         *                   LAYOUT_RIGHT 右边
         *                   LAYOUT_CENTER 中间
         * @param position 序号
         * @param viewInfo {@link TitleBarInfo.BaseView}
         * @param <V> {@link TitleBarInfo.BaseView}
         */
        <V extends TitleBarInfo.BaseView> void onTitleBarClick(int layoutType, int position,V viewInfo);
    }

    public OnTitleBarClickListener getOnTitleBarClickListener() {
        return mOnTitleBarClickListener;
    }

    public void setOnTitleBarClickListener(OnTitleBarClickListener onTitleBarClickListener) {
        mOnTitleBarClickListener = onTitleBarClickListener;
    }
}
