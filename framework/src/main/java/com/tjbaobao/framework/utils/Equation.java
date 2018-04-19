package com.tjbaobao.framework.utils;


import com.tjbaobao.framework.entity.PointF;

/**
 * 数学公式工具类
 */
public class Equation {

	/**
	 * 通过圆的标准方程求出x坐标
	 * @param y y坐标
	 * @param a 圆心横坐标
	 * @param b 圆心纵坐标
	 * @param r  圆的半径
	 * @return 当前y对应的x
	 */
	public static float getXForCircle(float y,float a,float b,float r)
	{
		float circleY = b-y  ;
		float circleX = (float) Math.sqrt(r*r-circleY*circleY);
		return a-circleX;
	}

	public static float getYForCircle(float x,float a,float b,float r)
	{
		float circleX = a-x ;
		float circleY =  (float) Math.sqrt(r*r-circleX*circleX);
		return b-circleY;
	}
	/**
	 * 通过角度获取圆轨迹的横坐标
	 * @param angle 角度0-360度
	 * @param radius 半径
	 * @return
	 */
	public static float getXForRadian(float angle,float radius)
	{
		
		if(angle<=90)
		{
			float radian = (float) (angle*Math.PI/180f);
			return  (float) (Math.sin(radian) * radius);
		}
		else if(angle<=180)
		{
			angle -=90;
			float radian = (float) (angle*Math.PI/180f);
			return (float) (Math.cos(radian)*radius);
		}
		else if(angle<=270)
		{
			angle -=180;
			float radian = (float) (angle*Math.PI/180f);
			return  (float) -(Math.sin(radian) * radius);
		}
		else
		{
			angle -=270;
			float radian = (float) (angle*Math.PI/180f);
			return  (float)-(Math.cos(radian)*radius);
		}
	}
	/**
	 * 通过弧度获取圆轨迹纵坐标
	 * @param angle 角度0-360度
	 * @param radius 半径
	 * @return
	 */
	public static float getYForRadian(float angle,float radius)
	{
		
		if(angle<=90)
		{
			float radian = (float) (angle*Math.PI/180f);
			return (float)-(Math.cos(radian)*radius);
		}
		else if(angle<=180)
		{
			angle -=90;
			float radian = (float) (angle*Math.PI/180f);
			return (float)(Math.sin(radian)*radius);
		}
		else if(angle<=270)
		{
			angle -=180;
			float radian = (float) (angle*Math.PI/180f);
			return (float)(Math.cos(radian)*radius);
		}
		else
		{
			angle -=270;
			float radian = (float) (angle*Math.PI/180f);
			return (float) -(Math.sin(radian) * radius);
		}
	}

	/**
	 *通过两点获得两点之间的距离
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @return
	 */
	public static double getDistanceBy2Dot(float x1,float y1,float x2,float y2)
	{
		return Math.sqrt (Math.pow(x1-x2,2)+Math.pow(y1-y2,2));//两点之间的距离
	}

	/**
	 * 通过两点获得两点之间的角度
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @return
	 */
	public static double getAngleBy2Dot(float x1,float y1,float x2,float y2)
	{
		double a =  getDistanceBy2Dot(x1,y1,x2,y2);
		double b = Math.abs(y1-y2);
		return Math.asin(b/a)/Math.PI*180d;
	}

	/**
	 * 判断两矩形是否相交
	 * @param pointF1 矩形1左上角坐标
	 * @param pointF2 矩形2左上角坐标
	 * @param widthTop1 矩形1长
	 * @param widthTop2 矩形2长
	 * @param widthLeft1 矩形1宽
	 * @param widthLeft2 矩形2宽
	 * @return 相交矩形的中点
	 */
	public static PointF isRectangleCross(PointF pointF1, PointF pointF2, float widthTop1, float widthTop2, float widthLeft1, float widthLeft2)
	{
		float minX = Math.max(pointF1.x,pointF2.x);
		float minY = Math.max(pointF1.y,pointF2.y);
		PointF pointFMax1 = new PointF(pointF1.x+widthTop1,pointF1.y+widthLeft1);
		PointF pointFMax2 = new PointF(pointF2.x+widthTop2,pointF2.y+widthLeft2);
		float maxX = Math.min(pointFMax1.x,pointFMax2.x);
		float maxY = Math.min(pointFMax1.y,pointFMax2.y);
		if(minX   < maxX   &&  minY   <   maxY)
		{
			float centerX = (minX+maxX)/2f;
			float centerY = (minY+maxY)/2f;
			return new PointF(centerX,centerY);
		}
		return null;
	}
}
