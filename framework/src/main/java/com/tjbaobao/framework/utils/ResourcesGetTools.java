package com.tjbaobao.framework.utils;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.File;
import java.util.UUID;

public class ResourcesGetTools {
	public class RequestCode
	{
		public static final int IMG_CAMERA_GET = 101;//从相机获取图像
		public static final int IMG_GALLERY_GET = 102;//从图库获取图像
		public static final int IMG_FILE_GET = 103 ;//从文件管理器获取图像
		public static final int IMG_CAMERA_CUT = 104;//从相机获取图像时使用的图片裁减
		public static final int IMG_GALLERY_CUT = 105;//从图库获取图像时使用的图片裁减
		public static final int VIDEO_CAMERA_GET = 201;//从相机获取视频
		public static final int VIDEO_GALLERY_GET = 202 ;//从图库获取视频
		public static final int VIDEO_FILE_GET = 203 ;//从文件管理器获取视频
	}
	private String pathRes = "";
	private Activity activity ;
	private OnResourcesGetListener onResourcesGetListener;
	
	public ResourcesGetTools(Activity activity)
	{
		this.activity = activity ;
	}
	
	/**
	 * 启动相机获取图片
	 */
	public void getImgByCamera()
	{
		pathRes = ConstantUtil.getImageCachePath()+UUID.randomUUID().toString()+".jpg";
		File file =new File(pathRes);
	    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra("output", Uri.fromFile(file));
        intent.putExtra("outputFormat", "JPEG");
        intent.putExtra("return-data", false);
        activity.startActivityForResult(intent, RequestCode.IMG_CAMERA_GET);
	}
	/**
	 * 从图库获取图片
	 */
	public void getImgByGallery()
	{
		pathRes = ConstantUtil.getImageCachePath()+UUID.randomUUID().toString()+".jpg";
		File file =new File(pathRes);
		Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		intent.putExtra("output", Uri.fromFile(file));
		intent.setType("image/*");  
		intent.putExtra("outputFormat", "JPEG");
		intent.putExtra("return-data", true);
		activity.startActivityForResult(intent, RequestCode.IMG_GALLERY_GET);
	}

	/**
	 * 启动录像机获取视频
	 */
	public void getVideoByCamera()
	{
		pathRes = ConstantUtil.getVideoCachePath()+UUID.randomUUID().toString()+".mp4";
		File file = new File(pathRes);
		Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
		//intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);//清晰图，0最低清，1最高清，默认中间值
		activity.startActivityForResult(intent, RequestCode.VIDEO_CAMERA_GET);
	}

	public void getVideoByFile()
	{
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		intent.setType("video/*");
		intent.addCategory(Intent.CATEGORY_OPENABLE);
		activity.startActivityForResult(intent, RequestCode.VIDEO_FILE_GET);
	}
	/**
	 * 从相机启动剪切图片
	 */
	private void startCutFromCamera()
	{
		File file = new File(pathRes);
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(Uri.fromFile(file), "image/*");
		intent.putExtra("output", Uri.fromFile(file));
		intent.putExtra("outputFormat", "JPEG");
		intent.putExtra("crop", "true");
		intent.putExtra("return-data", false);
		activity.startActivityForResult(intent, RequestCode.IMG_CAMERA_CUT);
	}
	/**
	 * 从图库启动剪切图片
	 * @param data
	 */
	private void startCutFromGallery(Intent data)
	{
		File file = new File(pathRes);
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(data.getData(), "image/*");
		intent.putExtra("output", Uri.fromFile(file));
		intent.putExtra("outputFormat", "JPEG");
		intent.putExtra("crop", "true");
		intent.putExtra("return-data", false);
		activity.startActivityForResult(intent, RequestCode.IMG_GALLERY_CUT);
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if(resultCode==Activity.RESULT_OK)
		{
			switch(requestCode)
			{
			case RequestCode.IMG_CAMERA_GET:
				startCutFromCamera();
				break;
			case RequestCode.IMG_GALLERY_GET:
				startCutFromGallery(data);
				break;
			case RequestCode.IMG_FILE_GET:
				
				break;
			case RequestCode.IMG_CAMERA_CUT:
				if(onResourcesGetListener!=null)
				{
					onResourcesGetListener.onSuccess(requestCode, pathRes, data);
				}
				break;
			case RequestCode.IMG_GALLERY_CUT:
				if(onResourcesGetListener!=null)
				{
					onResourcesGetListener.onSuccess(requestCode, pathRes, data);
				}
				break;
			case RequestCode.VIDEO_CAMERA_GET:
				if(onResourcesGetListener!=null)
				{
					onResourcesGetListener.onSuccess(requestCode, pathRes, data);
				}
				break;
			case RequestCode.VIDEO_GALLERY_GET:
				
				break;
			case RequestCode.VIDEO_FILE_GET:
				if(onResourcesGetListener!=null)
				{
					onResourcesGetListener.onSuccess(requestCode, data.getData().getPath(), data);
				}
				break;
			}
		}
		else if(resultCode==Activity.RESULT_FIRST_USER)
		{
			if(onResourcesGetListener!=null)
			{
				onResourcesGetListener.onFail(requestCode,resultCode);
			}
		}
		else if(resultCode==Activity.RESULT_CANCELED)
		{
			if(onResourcesGetListener!=null)
			{
				onResourcesGetListener.onFail(requestCode,resultCode);
			}
		}
	}

	/**
	 * 获取资源返回监听器
	 * @author TJbaobao
	 * @time 2017年5月16日
	 */
	public interface OnResourcesGetListener
	{
		public void onSuccess(int requestCode, String path, Intent data);
		public void onFail(int requestCode, int resultCode);
	}

	public OnResourcesGetListener getOnResourcesGetListener() {
		return onResourcesGetListener;
	}

	public void setOnResourcesGetListener(OnResourcesGetListener onResourcesGetListener) {
		this.onResourcesGetListener = onResourcesGetListener;
	}
}
