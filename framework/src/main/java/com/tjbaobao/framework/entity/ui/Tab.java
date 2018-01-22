package com.tjbaobao.framework.entity.ui;

/**
 * 标签组件实体类
 * @author TJbaobao
 * @time 2017年3月3日
 */

public class Tab {
	int imgOnId =-1 ,imgOffId =-1;
	String text ="";
	int textOnColor=-1,textOffColor=-1 ;
	String tag ;
	int position = 0;
	public Tab() {
		
	}
	public Tab(int imgOnId, int imgOffId, String text, int textOnColor, int textOffColor) {
		this.imgOnId = imgOnId;
		this.imgOffId = imgOffId;
		this.text = text;
		this.textOnColor = textOnColor;
		this.textOffColor = textOffColor;
	}
	public Tab(int imgOnId, int imgOffId, String text, int textOnColor, int textOffColor, String tag) {
		this.imgOnId = imgOnId;
		this.imgOffId = imgOffId;
		this.text = text;
		this.textOnColor = textOnColor;
		this.textOffColor = textOffColor;
		this.tag = tag ;
	}
	public int getImgOnId() {
		return imgOnId;
	}
	public void setImgOnId(int imgOnId) {
		this.imgOnId = imgOnId;
	}
	public int getImgOffId() {
		return imgOffId;
	}
	public void setImgOffId(int imgOffId) {
		this.imgOffId = imgOffId;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public int getTextOnColor() {
		return textOnColor;
	}
	public void setTextOnColor(int textOnColor) {
		this.textOnColor = textOnColor;
	}
	public int getTextOffColor() {
		return textOffColor;
	}
	public void setTextOffColor(int textOffColor) {
		this.textOffColor = textOffColor;
	}
	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}
	public int getPosition() {
		return position;
	}
	public void setPosition(int position) {
		this.position = position;
	}
}
