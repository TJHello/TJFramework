package com.tjbaobao.tjframework.dialog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;

import com.tjbaobao.framework.dialog.BaseDialog;
import com.tjbaobao.framework.utils.ImageDownloader;
import com.tjbaobao.tjframework.R;

public class ImageDialog extends BaseDialog {

    public ImageDialog(@NonNull Context context) {
        super(context, R.layout.dialog_main_activity_image_layout);
    }

    private ImageView ivImage;
    private ImageDownloader imageDownloader = ImageDownloader.getInstance();
    @Override
    public void onInitView(View view) {
        ivImage = view.findViewById(R.id.ivImage);
    }

    public void show(String url)
    {
        super.show();
        imageDownloader.load(url,ivImage);
    }

    @Override
    public void dismiss() {
        super.dismiss();
        imageDownloader.stop();
    }
}
