package com.tjbaobao.framework.utils;

import android.support.annotation.Nullable;

import com.tjbaobao.framework.listener.OnProgressListener;
import java.io.IOException;
import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;

/**
 * Created by TJbaobao on 2018/1/12.
 */

public class TJResponseBody extends ResponseBody {

    //实际的待包装响应体
    private ResponseBody responseBody;
    //进度回调接口
    private OnProgressListener onProgressListener;
    //包装完成的BufferedSource
    private BufferedSource bufferedSource;

    public TJResponseBody(ResponseBody responseBody) {
        this.responseBody = responseBody;
    }

    public OnProgressListener getOnProgressListener() {
        return onProgressListener;
    }

    public void setOnProgressListener(OnProgressListener onProgressListener) {
        this.onProgressListener = onProgressListener;
    }

    @Nullable
    @Override
    public MediaType contentType() {
        return responseBody.contentType();
    }

    @Override
    public long contentLength() {
        return responseBody.contentLength();
    }

    @Override
    public BufferedSource source() {
        if (bufferedSource == null) {
            bufferedSource = Okio.buffer(new MyForwardingSource(responseBody.source()));
        }
        return bufferedSource;
    }



    private class MyForwardingSource extends ForwardingSource
    {
        long totalBytesRead = 0L;
        public MyForwardingSource(Source delegate) {
            super(delegate);
        }
        @Override
        public long read(Buffer sink, long byteCount) throws IOException {
            long bytesRead = super.read(sink, byteCount);
            totalBytesRead += (bytesRead != -1 ? bytesRead : 0);
            if(onProgressListener!=null)
            {
                onProgressListener.onProgress(totalBytesRead,responseBody.contentLength());
            }
            return bytesRead;
        }
    }
}
