package com.tjbaobao.tjframework.dialog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;

import com.tjbaobao.framework.dialog.TJDialog;
import com.tjbaobao.framework.utils.ImageDownloader;
import com.tjbaobao.tjframework.R;

/**
 * 作者:TJbaobao
 * 时间:2018/7/18  19:54
 * 说明:
 * 使用：
 */
public class TJDialogExample extends TJDialog {

    private ImageView ivImage;
    private ImageDownloader imageDownloader = ImageDownloader.getInstance();

    public TJDialogExample(@NonNull Context context) {
        super(context, R.layout.dialog_tj_dialog_example_layout);
    }

    @Override
    protected void onInitView(View baseView) {
        ivImage = baseView.findViewById(R.id.ivImage);
    }

    public void show(String url) {
        super.show();
        imageDownloader.load(url,ivImage);
    }

    @Override
    public void dismiss() {
        super.dismiss();
        imageDownloader.stop();
    }
}
