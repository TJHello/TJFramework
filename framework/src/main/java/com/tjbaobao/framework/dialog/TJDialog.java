package com.tjbaobao.framework.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.annotation.AnimRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StyleRes;

import com.tjbaobao.framework.R;
import com.tjbaobao.framework.imp.HandlerToolsImp;
import com.tjbaobao.framework.imp.TJDialogImp;
import com.tjbaobao.framework.listener.OnTJDialogListener;
import com.tjbaobao.framework.utils.BaseHandler;
import com.tjbaobao.framework.utils.LogUtil;

/**
 * 作者:TJbaobao
 * 时间:2018/7/18  18:17
 * 说明:基于Dialog进行了封装，支持弹窗动画，支持一些常用按钮的点击回调事件。
 * 使用：
 * 1、创建布局
 * 如果要使用该弹窗的快捷功能，则需要在布局对应地方使用指定的id，当然，也可以通过重写以下方法来自定义id,或者不设置。
 * {@link #getViewWinBgId()} 设置弹窗背景层id，设置了之后会有一个渐变动画
 * {@link #getViewWinBoxId()} 设置弹窗窗体层id，设置了之后才会有弹窗动画
 * {@link #getViewWinTitleId()} 设置弹窗标题id(设置标题{@link #setTitle(int)},{@link #setTitle(CharSequence)})
 * {@link #getViewWinBtContinue()} 设置继续按钮id(点击监听{@link #onBtContinueClick(View)})
 * {@link #getViewWinBtCancel()} 设置取消按钮id(点击监听{@link #onBtCancelClick(View)})
 * {@link #getViewWinBtClose()} 设置关闭按钮id(点击监听{@link #onBtCloseClick(View)})
 *
 * 2、继承使用
 * 需要自己创建一个AppDialog继承TJDialog，并且实现特定的抽象方法。然后创建弹窗的时候只需要继承AppDialog就行了
 *
 * XXDialog xxDialog = null ;
 *
 * xxDialog = new XXDialog(context,R.layout.dialog_layout);//不建议通过{***}重写方法的方式来接收回调事件
 * xxDialog.setOnTJDialogListener()；//通过这个监听器，就可以处理任何事件的回调，而且不会有任何内存泄漏的风险
 * xxDialog.show();
 *
 * 3、回收内存
 *
 * @Override
 * protected void onDestroy() {
 *    super.onDestroy();
 *    xxDialog.destroy();
 * }
 *
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public abstract class TJDialog extends Dialog  implements View.OnClickListener,HandlerToolsImp,TJDialogImp {

    public static final int MATCH_PARENT = ViewGroup.LayoutParams.MATCH_PARENT;
    public static final int WRAP_CONTENT = ViewGroup.LayoutParams.WRAP_CONTENT;
    private static final int DEF_STYLE_ID = R.style.FW_Dialog;

    private final int VIEW_WIN_BG_ID;
    private final int VIEW_WIN_BOX_ID;
    private final int VIEW_WIN_TITLE_ID;
    private final int VIEW_WIN_BT_CONTINUE_ID;
    private final int VIEW_WIN_BT_CANCEL_ID;
    private final int VIEW_WIN_BT_CLOSE_ID;

    /**
     *
     */
    protected BaseHandler baseHandler = new BaseHandler(message -> {
        onHandleMessage(message,message.what,message.obj);
        return false;
    });
    protected View baseView,winBgView,winBoxView,winTitleView,winBtContinue,winBtCancel,winBtClose;
    protected int width ,height;
    protected boolean isShow = false;//是否已经显示(直到动画完成之后才是隐藏或者显示)
    protected boolean canOutsideClose = true;//是否点击viewWinBg时关闭弹窗
    protected boolean isShowWinBgAnim = true,isShowWinBoxAnim = true;
    private boolean isBtClickClose = true;//点击任何按钮之后关闭弹窗
    private int state ;//自定义的状态
    private int windowAnimExitId = R.anim.fw_windows_anim_exit;
    private int windowAnimEnterId = R.anim.fw_windows_anim_enter;
    private int contentAnimEnterId = R.anim.fw_windows_content_anim_enter;
    private int contentAnimExitId = R.anim.fw_windows_content_anim_exit;
    private OnShowListener onShowListener ;
    private OnDismissListener onDismissListener ;
    private OnTJDialogListener onTJDialogListener ;
    private static final int DISMISS = 0x43;

    private Animation animationWinBgEnter,animationWinBoxEnter,animationWinBgExit,animationWinBoxExit;

    public TJDialog(@NonNull Context context, @LayoutRes int layoutId) {
        this(context,layoutId,MATCH_PARENT,MATCH_PARENT);
    }

    public TJDialog(@NonNull Context context, @LayoutRes int layoutId,@StyleRes int styleId) {
        this(context,layoutId,MATCH_PARENT,MATCH_PARENT,styleId);
    }

    public TJDialog(@NonNull Context context, @LayoutRes int layoutId, int width, int height)
    {
        this(context,layoutId,width,height, DEF_STYLE_ID);
    }

    /**
     *
     * @param context content
     * @param layoutId 布局
     * @param width 可以指定具体数值，也可以用 {@link #WRAP_CONTENT} {@link #MATCH_PARENT}
     * @param height 可以指定具体数值，也可以用 {@link #WRAP_CONTENT} {@link #MATCH_PARENT}
     * @param styleId 默认style见 {@link #DEF_STYLE_ID},注意，默认style是全屏的。
     */
    public TJDialog(@NonNull Context context, @LayoutRes int layoutId, int width, int height,int styleId)
    {
        super(context,styleId);
        this.width = width;
        this.height = height;
        VIEW_WIN_BG_ID = getViewWinBgId();
        VIEW_WIN_BOX_ID = getViewWinBoxId();
        VIEW_WIN_TITLE_ID = getViewWinTitleId();
        VIEW_WIN_BT_CONTINUE_ID = getViewWinBtContinue();
        VIEW_WIN_BT_CANCEL_ID = getViewWinBtCancel();
        VIEW_WIN_BT_CLOSE_ID = getViewWinBtClose();
        baseView = LayoutInflater.from(context).inflate(layoutId, null);
        setContentView(baseView);
        initView();
    }

    private void initView()
    {
        winBgView = baseView.findViewById(VIEW_WIN_BG_ID);
        winBoxView = baseView.findViewById(VIEW_WIN_BOX_ID);
        winTitleView = baseView.findViewById(VIEW_WIN_TITLE_ID);
        winBtContinue = baseView.findViewById(VIEW_WIN_BT_CONTINUE_ID);
        winBtCancel = baseView.findViewById(VIEW_WIN_BT_CANCEL_ID);
        winBtClose = baseView.findViewById(VIEW_WIN_BT_CLOSE_ID);
        setOnClickListener(winBgView);
        setOnClickListener(winBoxView);
        setOnClickListener(winBtContinue);
        setOnClickListener(winBtCancel);
        setOnClickListener(winBtClose);
        int[] ids = onInitClick();
        if(ids!=null)
        {
            for(int id:ids)
            {
                View view = baseView.findViewById(id);
                setOnClickListener(view);
            }
        }
        onInitView(baseView);
    }

    /**
     * 返回需要监听的按钮id数组
     * @return id数组
     */
    @Nullable
    protected abstract int[] onInitClick();

    protected abstract void onInitView(@NonNull View baseView);

    @Override
    public void show() {
        if(baseView==null) return;
        if(!isShow)
        {
            super.setOnShowListener(dialog -> {
                super.setOnShowListener(null);
                if(onShowListener!=null) {
                    onShowListener.onShow(dialog);
                }
                if(onTJDialogListener!=null) {
                    onTJDialogListener.onShow(dialog,state);
                }
            });
            baseView.setVisibility(View.VISIBLE);
            if(winBgView!=null&&isShowWinBgAnim)
            {
                if(animationWinBgEnter==null) {
                    animationWinBgEnter = AnimationUtils.loadAnimation(getContext(), windowAnimEnterId);
                } else {
                    animationWinBgEnter.reset();
                }
                winBgView.startAnimation(animationWinBgEnter);
            }
            if (winBoxView != null&&isShowWinBoxAnim) {
                if(animationWinBoxEnter==null) {
                    animationWinBoxEnter = AnimationUtils.loadAnimation(getContext(), contentAnimEnterId);
                } else {
                    animationWinBoxEnter.reset();
                }
                winBoxView.startAnimation(animationWinBoxEnter);
            }
            Window window = getWindow();
            if(window!=null)
            {
                window.setWindowAnimations(-1);
            }
            try {
                super.show();
                isShow = true;
            }catch (Exception e){
                LogUtil.exception(e);
            }
            try{
                WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
                if (layoutParams != null) {
                    layoutParams.width = width;
                    layoutParams.height = height;
                }
                this.getWindow().setAttributes(layoutParams);
            }catch (Exception e){
                LogUtil.exception(e);
            }
        }
    }

    @Override
    public void dismiss() {
        if(baseView==null) return;
        if(isShow)
        {
            super.setOnDismissListener(dialog -> {
                super.setOnDismissListener(null);
                if(onDismissListener!=null) {
                    onDismissListener.onDismiss(dialog);
                }
                if(onTJDialogListener!=null) {
                    onTJDialogListener.onDismiss(dialog,state);
                }
            });
            isShow = false;
            if(winBoxView!=null&&isShowWinBoxAnim)
            {
                if(animationWinBoxExit==null) {
                    animationWinBoxExit = AnimationUtils.loadAnimation(getContext(), contentAnimExitId);
                } else {
                    animationWinBoxExit.reset();
                }
                winBoxView.startAnimation(animationWinBoxExit);
            }
            if(winBgView!=null)
            {
                if(animationWinBgExit==null) {
                    animationWinBgExit = AnimationUtils.loadAnimation(getContext(), windowAnimExitId);
                } else {
                    animationWinBgExit.reset();
                }
                winBgView.startAnimation(animationWinBgExit);
                animationWinBgExit.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        TJDialog.super.dismiss();
                        if(baseView!=null){
                            baseView.setVisibility(View.INVISIBLE);
                        }
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
            }
            else
            {
                super.dismiss();
            }
        }
    }

    @Override
    public void destroy() {
        setOnDismissListener(null);
        setOnShowListener(null);
        setOnTJDialogListener(null);
        super.setOnDismissListener(null);
        super.setOnShowListener(null);
        onShowListener = null;
        onDismissListener = null;
        baseHandler.removeMessages(DISMISS);
        baseHandler.removeCallbacksAndMessages(null);
        baseView = null;
        winBgView = null;
        winBoxView= null;
        winTitleView= null;
        winBtContinue= null;
        winBtCancel= null;
        winBtClose= null;
    }

    @Override
    public void isCantClose() {
        canOutsideClose = false;
        setCanceledOnTouchOutside(false);
        setCancelable(false);
    }

    @Override
    public void onClick(@NonNull View v) {
        int id = v.getId();
        if(id==VIEW_WIN_BG_ID)
        {
            onWinBgClick(v);
        }
        else if(id==VIEW_WIN_BOX_ID)
        {
            onWinBoxClick(v);
        }
        else if(id==VIEW_WIN_TITLE_ID)
        {
            onTitleClick(v);
        }
        else if(id==VIEW_WIN_BT_CONTINUE_ID)
        {
            onBtContinueClick(v);
            if(onTJDialogListener!=null){
                onTJDialogListener.onBtContinueClick(v);
            }
        }
        else if(id==VIEW_WIN_BT_CANCEL_ID)
        {
            onBtCancelClick(v);
            if(onTJDialogListener!=null){
                onTJDialogListener.onBtCancelClick(v);
            }
        }
        else if(id==VIEW_WIN_BT_CLOSE_ID)
        {
            onBtCloseClick(v);
            if(onTJDialogListener!=null){
                onTJDialogListener.onBtCloseClick(v);
            }
        }
        if(onTJDialogListener!=null)
        {
            state = onTJDialogListener.onTJClick(v);
        }
        if(id!=VIEW_WIN_BOX_ID&&id!=VIEW_WIN_BG_ID&&isBtClickClose)
        {
            dismiss();
        }
    }

    @Override
    public void onWinBgClick(@NonNull View view) {
        if(canOutsideClose)
        {
            dismiss();
        }
    }

    @Override
    public void onWinBoxClick(@NonNull View view) {

    }

    @Override
    public void onTitleClick(@NonNull View view) {

    }

    @Override
    public void onBtContinueClick(@NonNull View view) {

    }

    @Override
    public void onBtCancelClick(@NonNull View view) {
        dismiss();
    }

    @Override
    public void onBtCloseClick(@NonNull View view) {
        dismiss();
    }

    @Override
    public void onHandleMessage(Message msg, int what, @Nullable Object obj) {

    }

    @Override
    public void sendMessage(int what) {

    }

    @Override
    public void sendMessage(int what,@Nullable Object obj) {

    }

    @Override
    public void sendMessage(int what,@Nullable Object obj, int arg1) {

    }

    private void setOnClickListener(@Nullable View view) {
        if(view!=null)
        {
            view.setOnClickListener(this);
        }
    }

    @Override
    public int getViewWinBgId() {
        return R.id.fw_dialog_win_bg;
    }

    @Override
    public int getViewWinBoxId() {
        return R.id.fw_dialog_win_box;
    }

    @Override
    public int getViewWinTitleId() {
        return R.id.fw_dialog_win_title;
    }

    @Override
    public int getViewWinBtContinue() {
        return R.id.fw_dialog_win_bt_continue;
    }

    @Override
    public int getViewWinBtCancel() {
        return R.id.fw_dialog_win_bt_cancel;
    }

    @Override
    public int getViewWinBtClose() {
        return R.id.fw_dialog_win_bt_close;
    }

    @Override
    public void setTitle(int titleId) {
        super.setTitle(titleId);
        if(winTitleView!=null)
        {
            if(winTitleView instanceof TextView)
            {
                ((TextView)winTitleView).setText(titleId);
            }
        }
    }

    @Override
    public void setTitle(@Nullable CharSequence title) {
        super.setTitle(title);
        if(winTitleView!=null)
        {
            if(winTitleView instanceof TextView)
            {
                ((TextView)winTitleView).setText(title);
            }
        }
    }

    @Override
    public void setOnShowListener(OnShowListener onShowListener) {
        this.onShowListener = onShowListener;
    }

    @Override
    public void setOnDismissListener(OnDismissListener onDismissListener) {
        this.onDismissListener = onDismissListener;
    }

    public void setCanOutsideClose(boolean canOutsideClose) {
        this.canOutsideClose = canOutsideClose;
    }

    public void setContentEnterAnim(@AnimRes int animId)
    {
        contentAnimEnterId = animId;
    }

    public void setContentAnimExitId(@AnimRes int animId)
    {
        contentAnimExitId =  animId;
    }

    public void setWindowAnimEnterId(@AnimRes int animEnterId)
    {
        windowAnimEnterId = animEnterId;
    }

    public void setWindowAnimExitId(@AnimRes int animExitId)
    {
        windowAnimExitId = animExitId;
    }

    public void setOnTJDialogListener(OnTJDialogListener onTJDialogListener) {
        this.onTJDialogListener = onTJDialogListener;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public void setBtClickClose(boolean btClickClose) {
        isBtClickClose = btClickClose;
    }
}
