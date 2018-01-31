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
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;

import com.tjbaobao.framework.R;


/**
 * Created by TJbaobao on 2017/7/24.
 */

public class BaseDialog extends Dialog implements View.OnClickListener{

    private static final int MATCH_PARENT =  ViewGroup.LayoutParams.MATCH_PARENT;
    private static final int WRAP_CONTENT =  ViewGroup.LayoutParams.WRAP_CONTENT;
    private int windowAnimExitId = R.anim.fw_windows_anim_exit;
    private int windowAnimEnterId = R.anim.fw_windows_anim_enter;
    private int contentAnimEnterId = R.anim.fw_windows_content_anim_enter;
    private int contentAnimExitId = R.anim.fw_windows_content_anim_exit;
    private static final int Handler_What_Anim_Stop = 1001;

    private int width,height ;


    protected Context context ;
    private int layoutId ;
    protected View baseView,ll_windows_index,ll_index ;
    protected View bt_cancel,bt_continue ;
    private TextView tv_help_title ;
    protected boolean isStartAnim = true ;

    private boolean canOutsideClose = true ;

    public BaseDialog(@NonNull Context context, int layoutId) {
        this(context,layoutId,MATCH_PARENT,MATCH_PARENT);
    }
    public BaseDialog(@NonNull Context context, int layoutId, int width, int height)
    {
        super(context,R.style.FW_Dialog);
        this.context = context;
        this.layoutId = layoutId;
        this.width = width;
        this.height = height;
        baseView = LayoutInflater.from(context).inflate(layoutId,null);
        setContentView(baseView);
        this.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                BaseDialog.this.onDismiss();
            }
        });
        initView(baseView);
    }

    private void initView(View view)
    {
        ll_windows_index = view.findViewById(R.id.ll_windows_index);
        ll_index = view.findViewById(R.id.ll_index);
        bt_cancel = view.findViewById(R.id.bt_cancel);
        bt_continue = view.findViewById(R.id.bt_continue);
        tv_help_title = (TextView) view.findViewById(R.id.tv_help_title);
        if(bt_cancel!=null)
        {
            bt_cancel.setOnClickListener(this);
        }
        if(bt_continue!=null)
            bt_continue.setOnClickListener(this);
        if(ll_windows_index!=null)
        {
            ll_windows_index.setOnClickListener(this);
            ll_windows_index.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.fw_circular_smaill));
        }
        if(ll_index!=null)
        {
            ll_index.setOnClickListener(this);
        }
        onInitView(view);
    }
    
    public void show()
    {
        isDismiss = false;
        if(ll_windows_index!=null)
        {
            Animation animation = AnimationUtils.loadAnimation(context,contentAnimEnterId);
            long durationMillis =450;
            long delayMillis = 150;
            animation.setInterpolator(new DecelerateInterpolator(2));
            animation.setDuration(durationMillis);
            animation.setStartOffset(delayMillis);
            ll_windows_index.setAnimation(animation);
            ll_windows_index.startAnimation(animation);
        }
        if(isStartAnim)
        {
            Animation animationView = AnimationUtils.loadAnimation(context,windowAnimEnterId);
            animationView.setStartOffset(100);
            baseView.setAnimation(animationView);
            baseView.startAnimation(animationView);
        }
        baseView.setVisibility(View.VISIBLE);
        try{
            getWindow().setWindowAnimations(-1);
            super.show();
            WindowManager.LayoutParams layoutParams= getWindow().getAttributes();
            if(layoutParams!=null)
            {
                layoutParams.width = width;
                layoutParams.height = height;
            }
            this.getWindow().setAttributes(layoutParams);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void onInitView(View view){};

    public void setMyTitle(String title)
    {
        if(tv_help_title!=null)
        {
            tv_help_title.setText(title);
        }
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.bt_continue) {
            onContinueClick();

        } else if (i == R.id.bt_cancel) {
            onCancel();

        } else if (i == R.id.ll_windows_index) {

        } else if (i == R.id.ll_index) {
            if (canOutsideClose) {
                onClose();
                this.dismiss();
            }

        } else {
            onClick(v, v.getId());

        }
    }

    public void onClick(View view, int id)
    {

    }
    private boolean isDismiss = false;
    @Override
    public void dismiss() {
        if(!isDismiss)
        {
            final Animation animation = AnimationUtils.loadAnimation(context,contentAnimExitId);
            if(ll_windows_index!=null)
            {

                ll_windows_index.setAnimation(animation);
                ll_windows_index.startAnimation(animation);
            }
            final Animation animationView = AnimationUtils.loadAnimation(context,windowAnimExitId);
            animationView.setFillAfter(true);

            baseView.setAnimation(animationView);
            baseView.startAnimation(animationView);
            animationView.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    sendMessage(Handler_What_Anim_Stop,null);
                    baseView.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        }
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            onHandlerMessage(msg,msg.what,msg.obj);
            super.handleMessage(msg);
        }
    };
    protected void sendMessage(int what,Object obj)
    {
        Message msg = new Message();
        msg.what = what;
        msg.obj = obj;
        handler.sendMessage(msg);
    }
    protected void sendMessage(int what,int arg1,Object obj)
    {
        Message msg = new Message();
        msg.what = what;
        msg.obj = obj;
        msg.arg1 = arg1;
        handler.sendMessage(msg);
    }
    protected void onHandlerMessage(Message msg, int what, Object obj)
    {
        switch (what)
        {
            case Handler_What_Anim_Stop:
                super.dismiss();
                break;
        }
    }

    public void onCancel()
    {
        dismiss();
    }
    public void onClose()
    {
        dismiss();
    }
    public void onContinueClick(){
        dismiss();
    };
    public void onDismiss(){};


    public boolean isCanOutsideClose() {
        return canOutsideClose;
    }

    public void setCanOutsideClose(boolean canOutsideClose) {
        this.canOutsideClose = canOutsideClose;
    }

    //region===============Tools=================
    protected String getStringById(int id)
    {
        return context.getString(id);
    }
    protected int getColorById(int id)
    {
        return context.getResources().getColor(id);
    }
    //endregion
}
