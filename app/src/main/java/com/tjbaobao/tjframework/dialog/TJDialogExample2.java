package com.tjbaobao.tjframework.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.tjbaobao.framework.dialog.TJDialog;
import com.tjbaobao.framework.listener.OnTJDialogListener;
import com.tjbaobao.framework.utils.DeviceUtil;
import com.tjbaobao.framework.utils.ImageDownloader;
import com.tjbaobao.framework.utils.Tools;
import com.tjbaobao.tjframework.R;

/**
 * 作者:TJbaobao
 * 时间:2018/9/21  18:36
 * 说明:
 * 使用：
 */
public class TJDialogExample2 extends TJDialog implements OnTJDialogListener{

    private ImageDownloader imageDownloader = ImageDownloader.getInstance();
    private ImageView ivImage ;

    public TJDialogExample2(@NonNull Context context) {
        super(context, R.layout.dialog_tj_dialog_example_2_layout);
        imageDownloader.setDefaultImgSize((int)(DeviceUtil.getScreenWidth()*0.8f),(int)(DeviceUtil.getScreenWidth()*0.8f));
        this.setOnTJDialogListener(this);
        setBtClickClose(false);
    }


    @Nullable
    @Override
    protected int[] onInitClick() {
        return new int[]{R.id.ivDel,R.id.ivRate,R.id.ivShare,R.id.ivNew};
    }

    @Override
    protected void onInitView(View baseView) {
        ivImage = baseView.findViewById(R.id.ivImage);
    }

    @Override
    public int onTJClick(View view) {
        switch (view.getId())
        {
            case R.id.ivDel:
                Tools.showToast("删除");
                break;
            case R.id.ivRate:
                Tools.showToast("收藏");
                break;
            case R.id.ivShare:
                Tools.showToast("分享");
                break;
            case R.id.ivNew:
                Tools.showToast("新增");
                break;
        }
        return 0;
    }

    @Override
    public void onDismiss(DialogInterface dialog, int state) {
        imageDownloader.stop();
    }

    @Override
    public void onShow(DialogInterface dialog, int state) {
        imageDownloader.load("https://tjbaobao.oss-cn-shenzhen.aliyuncs.com/TJFramework/lakes-02_m_2.jpg?Expires=1537505751&OSSAccessKeyId=TMP.AQH4yZtdZeRJKgOqXNEwE6Qx_ZT2pKxgi4PSOIPDw5TK9Olsbh43WoZaHrGjAAAwLAIUUsEYARoIEqatBvwlHduBBAnTZs4CFF3iDvSzxG8iQ418tfjTKEn5R-FZ&Signature=Z1GlU3Ej%2F4CUiuo5Y3U6suntSHI%3D"
                ,ivImage);
    }


}
