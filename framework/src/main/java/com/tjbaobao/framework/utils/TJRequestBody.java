package com.tjbaobao.framework.utils;

import androidx.annotation.Nullable;


import com.tjbaobao.framework.listener.OnProgressListener;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.Buffer;
import okio.BufferedSink;
import okio.ForwardingSink;
import okio.Okio;
import okio.Sink;

/**
 * Created by TJbaobao on 2018/1/12.
 */

public class TJRequestBody extends RequestBody {

    //实际的待包装请求体
    private RequestBody requestBody;
    //进度回调接口
    private OnProgressListener onProgressListener;
    private BufferedSink bufferedSink;


    /**
     * 构造函数，赋值
     * @param requestBody 待包装的请求体
     * @param onProgressListener 回调接口
     */
    public TJRequestBody(RequestBody requestBody, OnProgressListener onProgressListener) {
        this.requestBody = requestBody;
        this.onProgressListener = onProgressListener;
    }

    @Nullable
    @Override
    public MediaType contentType() {
        return requestBody.contentType();
    }

    @Override
    public long contentLength() throws IOException {
        return requestBody.contentLength();
    }

    @Override
    public void writeTo(BufferedSink sink) throws IOException {
        if (bufferedSink == null) {
            bufferedSink = Okio.buffer(new MyForwardingSink(sink));
        }
        requestBody.writeTo(bufferedSink);
        bufferedSink.flush();
    }


    private class MyForwardingSink extends ForwardingSink
    {
        long bytesWritten = 0L;
        long contentLength = 0L;
        public MyForwardingSink(Sink delegate) {
            super(delegate);
        }
        @Override
        public void write(Buffer source, long byteCount) throws IOException {
            super.write(source, byteCount);
            if (contentLength == 0) {
                contentLength = contentLength();
            }
            bytesWritten += byteCount;
            if(onProgressListener!=null)
                onProgressListener.onProgress(bytesWritten,contentLength);
        }
    }

}
