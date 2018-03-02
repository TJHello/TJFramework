package com.tjbaobao.framework.utils;

import android.content.ContentResolver;
import android.content.ContentUris;
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
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
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
	

	public static Bitmap getBitmap(String path)
	{
		Bitmap bitmap = null;
		InputStream is = null ;
		try {
			if(FileUtil.exists(path))
			{
				is = new FileInputStream(new File(path));
			}
			else
			{
				is = Tools.getAssetsInputSteam(path);
			}
			if(is!=null)
			{
				bitmap = BitmapFactory.decodeStream(is);

			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}catch (Exception e){
			e.printStackTrace();
		}finally {
			if(is!=null)
			{
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return bitmap;
	}

	public static Bitmap getBitmap(String path,int width,int height)
	{
		return compressImage(path,width,height);
	}

	public static BitmapConfig getBitmapConfig(String path)
	{

		InputStream is = null ;
		try {
			if(FileUtil.exists(path))
			{
				is = new FileInputStream(new File(path));
			}
			else
			{
				is = Tools.getAssetsInputSteam(path);
			}
			if(is!=null)
			{
				BitmapFactory.Options bmpFactoryOptions = new BitmapFactory.Options();
				bmpFactoryOptions.inJustDecodeBounds = false;
				BitmapFactory.decodeStream(is,null,bmpFactoryOptions);
				BitmapConfig bitmapConfig = new BitmapConfig();
				bitmapConfig.setWidth(bmpFactoryOptions.outWidth);
				bitmapConfig.setHeight(bmpFactoryOptions.outHeight);
				if(bitmapConfig.getWidth()<=0||bitmapConfig.getHeight()<=0)
				{
					return null;
				}
				return bitmapConfig;
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}catch (Exception e){
			e.printStackTrace();
		}finally {
			if(is!=null)
			{
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

	public static BitmapConfig getBitmapConfig(Bitmap bitmap)
	{
		if(bitmap==null)
		{
			return null;
		}
		BitmapConfig bitmapConfig = new BitmapConfig();
		bitmapConfig.setWidth(bitmap.getWidth());
		bitmapConfig.setHeight(bitmap.getHeight());
		bitmapConfig.setSize(bitmap.getByteCount());
		return bitmapConfig;
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
		return compressImage(path,showWidth,showHeight,Config.ARGB_8888);
	}

	public static Bitmap compressImageRGB(String path,int showWidth,int showHeight)
	{
		return compressImage(path,showWidth,showHeight,Config.RGB_565);
	}

	public static Bitmap compressImage(String path,int showWidth,int showHeight,Config config)
	{
		if(path==null)
		{
			return null;
		}
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
		int bmpPotionWidth = bmpFactoryOptions.outWidth;
		int bmpPotionHeight = bmpFactoryOptions.outHeight;
		bmpFactoryOptions.inPreferredConfig =config;
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
		return bitmap;
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
		String path = ConstantUtil.getImageCachePath()+UUID.randomUUID().toString()+"."+prefix;
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
		if (inBitmap == null||inBitmap.isRecycled()) {
			return null;
		}
		Bitmap outBitmap = Bitmap.createBitmap (inBitmap.getWidth(), inBitmap.getHeight() , inBitmap.getConfig());
		Canvas canvas = new Canvas(outBitmap);
		canvas.setDrawFilter( new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
		Paint paint = new Paint();
		paint.setColor(tintColor );
		paint.setColorFilter( new PorterDuffColorFilter(tintColor, PorterDuff.Mode.SRC_IN)) ;
		canvas.drawBitmap(inBitmap , 0, 0, paint) ;
		inBitmap.recycle();
		return outBitmap ;
	}

	public static final Bitmap createRGBImage(Bitmap bitmap,int color)
	{
		int bitmapWidth=bitmap.getWidth();
		int bitmapHeight=bitmap.getHeight();
		int[] arrayColor=new int[bitmapWidth*bitmapHeight];
		int count=0;
		for(int i=0;i<bitmapHeight;i++){
			for(int j=0;j<bitmapWidth;j++){
				int color1=bitmap.getPixel(j,i);
				if(color1!=0){
					color1=color;
				}
				arrayColor[count]=color1;
				count++;
			}
		}
		bitmap = Bitmap.createBitmap( arrayColor, bitmapWidth, bitmapHeight, Config.ARGB_8888 );
		return bitmap;
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
		return matrixBitmap(bitmap,toWidth,toHeight,Config.ARGB_8888);
	}

	public static Bitmap matrixBitmapRGB(Bitmap bitmap,float toWidth,float toHeight)
	{
		return matrixBitmap(bitmap,toWidth,toHeight,Config.RGB_565);
	}

	public static Bitmap matrixBitmap(Bitmap bitmap,float toWidth,float toHeight,Config config)
	{
		if(bitmap==null||toWidth<=0||toHeight<=0)
		{
			return  null;
		}
		Bitmap bitmapTarget = null;
		if(bitmap.getWidth()>0&&bitmap.getHeight()>0)
		{
			float scale_y = toWidth / (float)bitmap.getWidth();
			float scale_x = toHeight / (float)bitmap.getHeight();
			Matrix matrix = new Matrix();
			matrix.postScale(scale_x, scale_y);
			bitmapTarget = Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),matrix,true);
		}
		bitmap.recycle();
		return bitmapTarget;
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
		Bitmap bitmapTarget = Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),matrix,true);
		bitmap.recycle();
		return bitmapTarget;
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

	public static void recycled(Bitmap bitmap)
	{
		if(bitmap!=null&&!bitmap.isRecycled())
		{
			bitmap.recycle();
		}
	}

	public static boolean isOk(String path)
	{
		if(getBitmapConfig(path)!=null)
		{
			return true;
		}
		return false;
	}

	public static boolean isOk(Bitmap bitmap)
	{
		if(bitmap==null||bitmap.isRecycled()||bitmap.getWidth()==0||bitmap.getHeight()==0)
		{
			return false;
		}
		return true;
	}

	/**
	 * 从图片uri获取path
	 *
	 * @param context 上下文
	 * @param uri     图片uri
	 */
	public static String getPathFromUri(Context context, Uri uri) {
		String outPath = "";
		Cursor cursor = context.getContentResolver()
				.query(uri, null, null, null, null);
		if (cursor == null) {
			// miui 2.3 有可能为null
			return uri.getPath();
		} else {
			if (uri.toString().contains("content://com.android.providers.media.documents/document/image")) { // htc 某些手机
				// 获取图片地址
				String _id = null;
				String uridecode = uri.decode(uri.toString());
				int id_index = uridecode.lastIndexOf(":");
				_id = uridecode.substring(id_index + 1);
				Cursor mcursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, " _id = " + _id,
						null, null);
				mcursor.moveToFirst();
				int column_index = mcursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
				outPath = mcursor.getString(column_index);
				if (!mcursor.isClosed()) {
					mcursor.close();
				}
				if (!cursor.isClosed()) {
					cursor.close();
				}
				return outPath;
			} else {
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
					if (DocumentsContract.isDocumentUri(context, uri)) {
						String docId = DocumentsContract.getDocumentId(uri);
						if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
							//Log.d(TAG, uri.toString());
							String id = docId.split(":")[1];
							String selection = MediaStore.Images.Media._ID + "=" + id;
							outPath = getImagePath(context, MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
						} else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
							//Log.d(TAG, uri.toString());
							Uri contentUri = ContentUris.withAppendedId(
									Uri.parse("content://downloads/public_downloads"),
									Long.valueOf(docId));
							outPath = getImagePath(context, contentUri, null);
						}
						return outPath;
					}
				}
				if ("content".equalsIgnoreCase(uri.getScheme())) {
					String auth = uri.getAuthority();
					if (auth.equals("media")) {
						outPath = getImagePath(context, uri, null);
					} else if (auth.equals("com.pcb.mall.fileprovider")) {
						//参看file_paths_public配置
						outPath = Environment.getExternalStorageDirectory() + "/Pictures/" + uri.getLastPathSegment();
					}
					return outPath;
				}
			}
			return outPath;
		}

	}


	/**
	 * 从uri中取查询path路径
	 *
	 * @param context   上下文
	 * @param uri
	 * @param selection
	 */
	private static String getImagePath(Context context, Uri uri, String selection) {
		String path = null;
		Cursor cursor = context.getContentResolver().query(uri, null, selection, null, null);
		if (cursor != null) {
			if (cursor.moveToFirst()) {
				path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
			}
			cursor.close();
		}
		return path;
	}
}
