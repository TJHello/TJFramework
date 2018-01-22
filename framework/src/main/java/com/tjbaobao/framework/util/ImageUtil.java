package com.tjbaobao.framework.util;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;

import com.tjbaobao.framework.entity.BitmapConfig;
import com.tjbaobao.framework.entity.FileType;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import static android.graphics.BitmapFactory.decodeStream;

public class ImageUtil {
	private static final int defWidth =Tools.getScreenWidth();
	private static final int defHeight= 480;
	
	//*********获取并且压缩图片
	@SuppressWarnings("deprecation")
	public static Drawable getDrawable(String path)
	{
		return new BitmapDrawable(compressImage(path));
	}
	public static Drawable getDrawable(String path,int width,int height)
	{
		return new BitmapDrawable(compressImage(path,width,height));
	}
	public static Bitmap getBitmap(String path)
	{
		return BitmapFactory.decodeFile(path);
	}
	public static BitmapConfig getBitmapConfig(String path)
	{
		File bmpFile = new File(path);
		if (!bmpFile.exists()) {
			return null;
		}
		BitmapFactory.Options bmpFactoryOptions = new BitmapFactory.Options();
		bmpFactoryOptions.inJustDecodeBounds = false;
		BitmapFactory.decodeFile(path,bmpFactoryOptions);
		BitmapConfig bitmapConfig = new BitmapConfig();
		bitmapConfig.setWidth(bmpFactoryOptions.outWidth);
		bitmapConfig.setHeight(bmpFactoryOptions.outHeight);
		return bitmapConfig;
	}
	public static Bitmap getBitmapByAssets(String path)
	{
		InputStream is = Tools.getAssetsInputSteam(path);
		if(is==null)
		{
			try {
				is = new FileInputStream(new File(path));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		return decodeStream(is);
	}
	public static Bitmap getBitmap(String path,int width,int height)
	{
		return compressImage(path,width,height);
	}

	public static BitmapConfig getBitmapConfigByAssets(String path) {
		InputStream is = Tools.getAssetsInputSteam(path);
		if (is == null) {
			try {
				is = new FileInputStream(new File(path));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		if (is != null) {
			BitmapFactory.Options bmpFactoryOptions = new BitmapFactory.Options();
			bmpFactoryOptions.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(is, null, bmpFactoryOptions);
			int bmpPotionWidth = bmpFactoryOptions.outWidth;
			int bmpPotionHeight = bmpFactoryOptions.outHeight;
			BitmapConfig config = new BitmapConfig();
			config.setWidth(bmpPotionWidth);
			config.setHeight(bmpPotionHeight);
			return config;
		}
		return null;
	}



	/**
	 * 压缩图片
	 * @param path
	 */
	public static Bitmap compressImage(String path)
	{
		return compressImage(path,defWidth,defHeight);
	}
	public static Bitmap compressImage(String path,int showWidth,int showHeight)
	{
		File bmpFile = new File(path);
		BitmapFactory.Options bmpFactoryOptions = new BitmapFactory.Options();
		bmpFactoryOptions.inJustDecodeBounds = true;
		if (!bmpFile.exists()) {
			InputStream inputStream = Tools.getAssetsInputSteam(path);
			if(inputStream!=null)
			{
				BitmapFactory.decodeStream(inputStream,null, bmpFactoryOptions);
			}
			else
			{
				return null;
			}
		}
		else
		{
			BitmapFactory.decodeFile(path, bmpFactoryOptions);
		}
		bmpFactoryOptions.inPreferredConfig = Config.ARGB_8888;
		int bmpPotionWidth = bmpFactoryOptions.outWidth;
		int bmpPotionHeight = bmpFactoryOptions.outHeight;
		float outWidthRatio = (float)bmpPotionWidth / (float)showWidth;
		float outHeightRatio = (float)bmpPotionHeight / (float)showHeight;
		float outRatio =0.5f+(outWidthRatio > outHeightRatio ? outWidthRatio : outHeightRatio);
		if (outRatio < 1) {
			outRatio = 1;
		}
		bmpFactoryOptions.inJustDecodeBounds = false;
		bmpFactoryOptions.inSampleSize = (int) outRatio;
		Bitmap bitmap ;
		if (!bmpFile.exists()) {
			InputStream inputStream = Tools.getAssetsInputSteam(path);
			if(inputStream!=null)
			{
				bitmap =BitmapFactory.decodeStream(inputStream,null, bmpFactoryOptions);
			}
			else
			{
				return null;
			}
		}
		else
		{
			bitmap =BitmapFactory.decodeFile(path, bmpFactoryOptions);
		}
		return compressImage(bitmap,showWidth,showHeight);
	}
	
	public static boolean compressAndSave(String path)
	{
		try {
			if(!new File(path).exists())
			{
				return false;
			}
			Bitmap bitmap = compressImage(path);
			FileOutputStream fileOut = new FileOutputStream(new File(path));
			bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOut);
			return true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return false; 
	}
	
	public static Bitmap compressImage(Bitmap image,int width,int height) {  
		if(image==null)
		{
			return null;
		}
		int maxQuality = width*height/10000;
		BitmapFactory.Options options = new BitmapFactory.Options();  
        options.inJustDecodeBounds = false ;
        options.inPreferredConfig = Config.ARGB_8888;
		int bmpPotionWidth = image.getWidth();
		int bmpPotionHeight = image.getHeight();
		float outWidthRatio = (float)bmpPotionWidth / (float)width;
		float outHeightRatio = (float)bmpPotionHeight / (float)height;
		float outRatio =0.5f+(outWidthRatio > outHeightRatio ? outWidthRatio : outHeightRatio);
		if (outRatio < 1) {
			outRatio = 1;
		}
		options.inSampleSize = (int) outRatio;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Bitmap bitmap = null;
        int quality = 80;
        do{
        	baos.reset();//清空baos
            image.compress(Bitmap.CompressFormat.JPEG, quality, baos);
            quality -= 10;
        }while( baos.toByteArray().length/1024>maxQuality&&quality>10);
        bitmap = BitmapFactory.decodeByteArray(baos.toByteArray(), 0, baos.toByteArray().length, options);
		image.recycle();
		image = null;
        return bitmap;
    }  
	
	public static Bitmap compressImage(Bitmap image)
	{
		return compressImage(image, defWidth, defHeight);
	}
	
	public static Bitmap compressImage(Drawable drawable,int width,int height)
	{
		BitmapDrawable bd = (BitmapDrawable) drawable;
		Bitmap bitmap = bd.getBitmap();
		bitmap = compressImage(bitmap,width,height);
		return bitmap;
	}

	public static Bitmap compressImage(Drawable drawable)
	{
		return compressImage(drawable, defWidth, defHeight);
	}
	
	public static String saveBitmap(String inPath)
	{
		Bitmap bitmap = getBitmap(inPath);
		String prefix = FileUtil.getPrefix(inPath);
		if(prefix==null)
		{
			return null;
		}
		String path = ConstantUtil.getFileCachePath()+UUID.randomUUID().toString()+"."+prefix;
		File file = new File(path);
		FileOutputStream out;
		try {
			out = new FileOutputStream(file);
			if(FileType.PNG.contains(prefix))
			{
				bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
			}
			else if(FileType.JPG.contains(prefix))
			{
				bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
			}
			else
			{
				bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
			}
			out.flush();
			out.close();
			return path;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static String saveBitmap(String inPath,String outPath)
	{
		Bitmap bitmap = getBitmap(inPath);
		String prefix = FileUtil.getPrefix(inPath);
		if(prefix==null)
		{
			return null;
		}
		File file = new File(outPath);
		FileOutputStream out;
		try {
			out = new FileOutputStream(file);
			if(FileType.PNG.contains(prefix))
			{
				bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
			}
			else if(FileType.JPG.contains(prefix))
			{
				bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
			}
			else
			{
				bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
			}
			out.flush();
			out.close();
			return outPath;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static String saveBitmap(Bitmap bitmap,String outPath)
	{
		if(bitmap==null)
		{
			return null;
		}
		String prefix = FileUtil.getPrefix(outPath);
		if(prefix==null)
		{
			return null;
		}
		File file = new File(outPath);
		FileOutputStream out;
		try {
			out = new FileOutputStream(file);
			if(FileType.PNG.contains(prefix))
			{
				bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
			}
			else if(FileType.JPG.contains(prefix))
			{
				bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
			}
			else
			{
				bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
			}
			out.flush();
			out.close();
			return outPath;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static String cupAndSaveBitmap(String inPath,String outPath,Rect rect)
	{
		File bmpFile = new File(inPath);
		if (!bmpFile.exists()) {
			return null;
		}
		try {
			FileInputStream inputStream = new FileInputStream(bmpFile);
			BitmapFactory.Options bmpFactoryOptions = new BitmapFactory.Options();
			bmpFactoryOptions.inJustDecodeBounds = false;
			Bitmap bitmapOut = decodeStream(inputStream,rect,bmpFactoryOptions);
			return saveBitmap(bitmapOut,outPath);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 填色
	 * @param inBitmap
	 * @param tintColor
	 * @return
	 */
	public static Bitmap tintBitmap(Bitmap inBitmap , int tintColor) {
		if (inBitmap == null) {
			return null;
		}
		Bitmap outBitmap = Bitmap.createBitmap (inBitmap.getWidth(), inBitmap.getHeight() , inBitmap.getConfig());
		Canvas canvas = new Canvas(outBitmap);
		canvas.setDrawFilter( new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
		Paint paint = new Paint();
		paint.setColor(tintColor );
		paint.setColorFilter( new PorterDuffColorFilter(tintColor, PorterDuff.Mode.SRC_IN)) ;
		canvas.drawBitmap(inBitmap , 0, 0, paint) ;
		return outBitmap ;
	}

	/**
	 * 缩放图片到指定大小
	 * @param bitmap
	 * @param toWidth
	 * @param toHeight
	 * @return
	 */
	public static Bitmap matrixBitmap(Bitmap bitmap,float toWidth,float toHeight)
	{
		if(bitmap==null)
		{
			return  null;
		}
		float scale_y = toWidth / bitmap.getWidth();
		float scale_x = toHeight / bitmap.getHeight();
		if(bitmap.getWidth()>0&&bitmap.getHeight()>0)
		{
			Matrix matrix = new Matrix();
			matrix.postScale(scale_x, scale_y);
			bitmap = Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),matrix,true);
		}
		final Paint paint = new Paint();
		paint.setAntiAlias(true);
		Bitmap target = Bitmap.createBitmap((int)toWidth,(int)toHeight, Config.ARGB_8888);
		Canvas canvas = new Canvas(target);
		canvas.setDrawFilter( new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
		canvas.drawBitmap(bitmap, 0, 0, paint);

		canvas = null;
		bitmap.recycle();
		bitmap = null;
		return target;
	}

	/**
	 * 旋转图片
	 * @param bitmap
	 * @param rotate
	 * @return
	 */
	public static Bitmap rotateBitmap(Bitmap bitmap,float rotate)
    {
        Matrix matrix = new Matrix();
        matrix.postRotate(rotate);
		bitmap = Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),matrix,true);
		return bitmap;
    }

	/**
	 * 读取照片旋转角度
	 *
	 * @param path 照片路径
	 * @return 角度
	 */
	public static int readPictureDegree(String path) {
		int degree = 0;
		try {
			ExifInterface exifInterface = new ExifInterface(path);
			int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
			switch (orientation) {
				case ExifInterface.ORIENTATION_ROTATE_90:
					degree = 90;
					break;
				case ExifInterface.ORIENTATION_ROTATE_180:
					degree = 180;
					break;
				case ExifInterface.ORIENTATION_ROTATE_270:
					degree = 270;
					break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return degree;
	}

	public static String getRealFilePath(final Context context, final Uri uri ) {
		if ( null == uri ) return null;
		final String scheme = uri.getScheme();
		String data = null;
		if ( scheme == null )
			data = uri.getPath();
		else if ( ContentResolver.SCHEME_FILE.equals( scheme ) ) {
			data = uri.getPath();
		} else if ( ContentResolver.SCHEME_CONTENT.equals( scheme ) ) {
			Cursor cursor = context.getContentResolver().query( uri, new String[] { MediaStore.Images.ImageColumns.DATA }, null, null, null );
			if ( null != cursor ) {
				if ( cursor.moveToFirst() ) {
					int index = cursor.getColumnIndex( MediaStore.Images.ImageColumns.DATA );
					if ( index > -1 ) {
						data = cursor.getString( index );
					}
				}
				cursor.close();
			}
		}
		return data;
	}

	public static Bitmap readBitmapByPosition(int left,int top,int width,int height)
	{
		InputStream is = Tools.getAssetsInputSteam("game_images/puzzle_assets_01.jpg");
		try {
			BitmapRegionDecoder mDecoder = BitmapRegionDecoder.newInstance(is, true);
			Rect mRect = new Rect();
			mRect.set(left,top,left+width,top+height);
			return mDecoder.decodeRegion(mRect, null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
