package com.tjbaobao.framework.utils;

import java.io.Closeable;
import java.io.IOException;

@SuppressWarnings("WeakerAccess")
public class CloseUtil {
    /**
     * 关闭IO(静默操作)
     *
     * @param closeables closeables
     */
    public static void closeIO(final Closeable... closeables) {
        if (closeables == null) return;
        for (Closeable closeable : closeables) {
            if (closeable != null) {
                try {
                    closeable.close();
                } catch (IOException e) {
                    LogUtil.exception(e);
                }
            }
        }
    }
}
