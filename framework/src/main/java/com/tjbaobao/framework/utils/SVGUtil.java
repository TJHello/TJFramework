package com.tjbaobao.framework.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Path;
import android.graphics.RectF;

import android.support.annotation.NonNull;
import com.caverock.androidsvg.PreserveAspectRatio;
import com.caverock.androidsvg.SVG;
import com.caverock.androidsvg.SVGParseException;

import java.io.*;

/**
 * Created by TJbaobao on 2017/8/18.
 */

public class SVGUtil {

    public static Bitmap getSvgBitmap(Context context, int resId, int width, int height){
        SVG svg;
        try {
            svg = SVG.getFromResource(context, resId);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        svg.setDocumentPreserveAspectRatio(PreserveAspectRatio.UNSCALED);
        Bitmap svgBitmap= Bitmap.createBitmap(width,height, Bitmap.Config.ARGB_8888);
        Canvas canvas=new Canvas(svgBitmap){
            @Override
            public void drawPath(@NonNull Path path, Paint paint) {
                paint.setStrokeCap(Paint.Cap.ROUND);
                paint.setStrokeJoin(Paint.Join.ROUND);
                super.drawPath(path, paint);
            }
        };
        RectF viewBox=svg.getDocumentViewBox();
        float canvasScale= Math.min(width
                        / (viewBox.width() + 5f),
                height / (viewBox.height() + 5f));
        canvas.translate((width - viewBox.width() * canvasScale) / 2.0f,
                (height - viewBox.height() * canvasScale) / 2.0f);
        canvas.scale(canvasScale,canvasScale);
        svg.renderToCanvas(canvas);
        return svgBitmap;
    }

    public static Bitmap getSvgBitmap(String path, int width, int height){
        InputStream is = null;
        try {
            is = new FileInputStream(new File(path));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        SVG svg = null ;
        if(is!=null)
        {
            try {
                svg = SVG.getFromInputStream(is);
            } catch (SVGParseException e) {
                e.printStackTrace();
                try {
                    is.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        if(svg == null)
        {
            return null;
        }
        svg.setDocumentPreserveAspectRatio(PreserveAspectRatio.UNSCALED);
        Bitmap svgBitmap= Bitmap.createBitmap(width,height, Bitmap.Config.ARGB_8888);
        Canvas canvas=new Canvas(svgBitmap){
            @Override
            public void drawPath(@NonNull Path path, Paint paint) {
                paint.setStrokeCap(Paint.Cap.ROUND);
                paint.setStrokeJoin(Paint.Join.ROUND);
                paint.setAntiAlias(true);
                super.drawPath( path, paint);
            }
        };
        canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
        RectF viewBox=svg.getDocumentViewBox();
        float canvasScale= Math.min(width
                        / (viewBox.width() + 5f),
                height / (viewBox.height() + 5f));
        canvas.translate((width - viewBox.width() * canvasScale) / 2.0f,
                (height - viewBox.height() * canvasScale) / 2.0f);
        canvas.scale(canvasScale,canvasScale);
        svg.renderToCanvas(canvas);
        return svgBitmap;
    }

}
