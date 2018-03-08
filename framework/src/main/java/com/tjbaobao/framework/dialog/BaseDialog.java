package com.tjbaobao.framework.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;

import com.tjbaobao.framework.R;
import com.tjbaobao.framework.imp.HandlerToolsImp;
import com.tjbaobao.framework.utils.BaseHandler;


/**
 *Dialog弹窗封装
 * 使用方法:
 * 1、创建布局
 * 布局建议包含,最外层框(id为ll_windows_index,触摸外层关闭弹窗),内容框View(id为ll_index，控制动画),
 * 以及标题TextView(id为tv_title)、确认按钮View(id为bt_continue),取消按钮View(id为bt_cancel)
 * 以上view可以不带，id也可以不同，但可能会丢失一些对应的功能，以及相关的快捷功能
 * 2、创建实例，传入布局id
 * 3、show()
 *
 * Created by TJbaobao on 2017/7/24.
 */

@SuppressWarnings({"unused", "WeakerAccess"})
public abstract class BaseDialog extends Dialog implements View.OnClickListener,HandlerToolsImp {

    private static final int MATCH_PARENT = ViewGroup.LayoutParams.MATCH_PARENT;
    private static final int WRAP_CONTENT = ViewGroup.LayoutParams.WRAP_CONTENT;
    private int windowAnimExitId = R.anim.fw_windows_anim_exit;
    private int windowAnimEnterId = R.anim.fw_windows_anim_enter;
    private int contentAnimEnterId = R.anim.fw_windows_content_anim_enter;
    private int contentAnimExitId = R.anim.fw_windows_content_anim_exit;
    private static final int Handler_What_Anim_Stop = 1001;

    private int width, height;


    protected Context context;
    protected View baseView, ll_windows_index, ll_index;
    protected View bt_cancel, bt_continue;
    private TextView tvTitle;
    protected boolean isStartAnim = true;
    private boolean canOutsideClose = true;

    public BaseDialog(@NonNull Context context, int layoutId) {
        this(context, layoutId, MATCH_PARENT, MATCH_PARENT);
    }

    public BaseDialog(@NonNull Context context, int layoutId, int width, int height) {
        super(context, R.style.FW_Dialog);
        this.context = context;
        this.width = width;
        this.height = height;
        baseView = LayoutInflater.from(context).inflate(layoutId, null);
        setContentView(baseView);
        initView(baseView);
        this.setOnDismissListener(dialog -> BaseDialog.this.onDismiss());
        this.setOnShowListener(dialog ->BaseDialog.this.onShow());
    }

    private void initView(View view) {
        ll_windows_index = view.findViewById(R.id.ll_windows_index);
        ll_index = view.findViewById(R.id.ll_index);
        bt_cancel = view.findViewById(R.id.bt_cancel);
        bt_continue = view.findViewById(R.id.bt_continue);
        tvTitle = view.findViewById(R.id.tv_title);
        if (bt_cancel != null) {
            bt_cancel.setOnClickListener(this);
        }
        if (bt_continue != null) {
            bt_continue.setOnClickListener(this);
        }
        if (ll_windows_index != null) {
            ll_windows_index.setOnClickListener(this);
            ll_windows_index.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.fw_circular_smaill));
        }
        if (ll_index != null) {
            ll_index.setOnClickListener(this);
        }
        onInitView(view);
    }

    public void show() {
        isDismiss = false;
        if (ll_windows_index != null) {
            Animation animation = AnimationUtils.loadAnimation(context, contentAnimEnterId);
            long durationMillis = 450;
            long delayMillis = 150;
            animation.setInterpolator(new DecelerateInterpolator(2));
            animation.setDuration(durationMillis);
            animation.setStartOffset(delayMillis);
            ll_windows_index.setAnimation(animation);
            ll_windows_index.startAnimation(animation);
        }
        if (isStartAnim) {
            Animation animationView = AnimationUtils.loadAnimation(context, windowAnimEnterId);
            animationView.setStartOffset(100);
            baseView.setAnimation(animationView);
            baseView.startAnimation(animationView);
        }
        baseView.setVisibility(View.VISIBLE);
        try {
            Window window = getWindow();
            if(window!=null)
            {
                window.setWindowAnimations(-1);
            }
            super.show();
            WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
            if (layoutParams != null) {
                layoutParams.width = width;
                layoutParams.height = height;
            }
            this.getWindow().setAttributes(layoutParams);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 初始化View
     * @param view 主布局view
     */
    public abstract void onInitView(View view);

    /**
     * 设置标题
     * @param title 标题
     */
    public void setMyTitle(String title) {
        if (tvTitle != null) {
            tvTitle.setText(title);
        }
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.bt_continue) {
            onContinueClick();

        } else if (i == R.id.bt_cancel) {
            onCancel();

        }else if (i == R.id.ll_index) {
            if (canOutsideClose) {
                onClose();
                this.dismiss();
            }
        } else {
            onClick(v, v.getId());
        }
    }

    public void onClick(View view, int id) {

    }

    private boolean isDismiss = false;

    @Override
    public void dismiss() {
        if (!isDismiss) {
            isDismiss = true;
            final Animation animation = AnimationUtils.loadAnimation(context, contentAnimExitId);
            if (ll_windows_index != null) {

                ll_windows_index.setAnimation(animation);
                ll_windows_index.startAnimation(animation);
            }
            final Animation animationView = AnimationUtils.loadAnimation(context, windowAnimExitId);
            animationView.setFillAfter(true);

            baseView.setAnimation(animationView);
            baseView.startAnimation(animationView);
            animationView.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    sendMessage(Handler_What_Anim_Stop, null);
                    baseView.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        }
    }

    private BaseHandler handler = new BaseHandler(new Callback());
    private class Callback implements Handler.Callback
    {
        @Override
        public boolean handleMessage(Message msg) {
            onHandleMessage(msg,msg.what,msg.obj);
            return false;
        }
    }

    @Override
    public void onHandleMessage(Message msg, int what, Object obj) {
        switch (what) {
            case Handler_What_Anim_Stop:
                super.dismiss();
                break;
        }
    }

    @Override
    public void sendMessage(int what) {
        handler.sendMessage(what);
    }

    @Override
    public void sendMessage(int what, Object obj) {
        handler.sendMessage(what,obj);
    }

    @Override
    public void sendMessage(int what, Object obj, int arg1) {
        handler.sendMessage(what,obj,arg1);
    }

    protected void onCancel() {
        dismiss();
    }

    protected void onShow(){}

    protected void onClose() {
        dismiss();
    }

    protected void onContinueClick() {
        dismiss();
    }

    public void onDismiss() {
    }

    public boolean isCanOutsideClose() {
        return canOutsideClose;
    }

    public void setCanOutsideClose(boolean canOutsideClose) {
        this.canOutsideClose = canOutsideClose;
    }

    //region===============Tools=================
    protected String getStringById(int id) {
        return context.getString(id);
    }

    protected int getColorById(int id) {
        return context.getResources().getColor(id);
    }
    //endregion
}
