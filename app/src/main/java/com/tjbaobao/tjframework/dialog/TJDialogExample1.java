package com.tjbaobao.tjframework.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.KeyboardShortcutGroup;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;

import com.tjbaobao.framework.dialog.TJDialog;
import com.tjbaobao.framework.listener.OnTJDialogListener;
import com.tjbaobao.framework.utils.ImageDownloader;
import com.tjbaobao.tjframework.R;

import java.util.List;

/**
 * 作者:TJbaobao
 * 时间:2018/7/18  19:54
 * 说明:
 * 使用：
 */
public class TJDialogExample1 extends TJDialog {


    public TJDialogExample1(@NonNull Context context) {
        super(context, R.layout.dialog_tj_dialog_example_layout);
    }


    @Nullable
    @Override
    protected int[] onInitClick() {
        return new int[0];
    }

    @Override
    protected void onInitView(View baseView) {

    }
}
