package com.tjbaobao.framework.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Path;
import android.graphics.RectF;

import com.caverock.androidsvg.PreserveAspectRatio;
import com.caverock.androidsvg.SVG;
import com.caverock.androidsvg.SVGParseException;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by TJbaobao on 2017/8/18.
 */

public class SVGUtil {

    public static Bitmap getSvgBitmap(Context context, int resId, int width, int height, final boolean bold, int c){
        SVG svg;
        InputStream is = null;
        try {
            //is = context.getAssets().open(resId);
            svg = SVG.getFromResource(context, resId);
            //svg = SVG.getFromString(Base64DesUtil.decryptIsToStr(is));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }finally {
            if(is!=null){
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        svg.setDocumentPreserveAspectRatio(PreserveAspectRatio.UNSCALED);
        final int color;
        if(c==0){
            color= Color.WHITE;
        }else {
            color=c|0xff000000;
        }
        Bitmap svgBitmap= Bitmap.createBitmap(width,height, Bitmap.Config.ARGB_8888);
        Canvas canvas=new Canvas(svgBitmap){
            //private final Matrix mMatrix = new Matrix();
            /**
             * 重写该方法以便重置画笔参数
             * @param path
             * @param paint
             */
            @Override
            public void drawPath(Path path, Paint paint) {
                paint.setStrokeCap(Paint.Cap.ROUND);
                paint.setStrokeJoin(Paint.Join.ROUND);
               /* getMatrix(mMatrix);
                path.transform(mMatrix);*/
             /*
                paint.setStyle(Paint.Style.STROKE);*/
                //paint.setColor(color);
                if(bold){
                    paint.setStrokeWidth(paint.getStrokeWidth()*2f);
                }
                super.drawPath(path, paint);
            }
        };

        /*Path clipPath = new Path();
        clipPath.addRoundRect(new RectF(0, 0, width, height), radius, radius, Path.Direction.CW);
        canvas.clipPath(clipPath);*/

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

    public static Bitmap getSvgBitmap(String fileName, int width, int height,final int color){
        SVG svg = null ;
        InputStream is = Tools.getAssetsInputSteam(fileName);
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
            /**
             * 重写该方法以便重置画笔参数
             * @param path
             * @param paint
             */
            @Override
            public void drawPath(Path path, Paint paint) {
                paint.setColor(color);
                //paint.setStyle(Paint.Style.FILL);
                paint.setStrokeCap(Paint.Cap.ROUND);
                paint.setStrokeJoin(Paint.Join.ROUND);
                paint.setAntiAlias(true);
                super.drawPath( path, paint);
            }
        };
        canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
        Paint paint = new Paint();
        paint.setAntiAlias(true);

//        paint.setStyle(Paint.Style.STROKE);
//        paint.setColor(Color.WHITE);
//        canvas.drawRect(0,0,width,height,paint);
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
