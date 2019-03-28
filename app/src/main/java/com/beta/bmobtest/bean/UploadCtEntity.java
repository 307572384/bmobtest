package com.beta.bmobtest.bean;
import cn.bmob.v3.datatype.*;
import cn.bmob.v3.*;
//上传图片数据表
public class UploadCtEntity extends BmobObject
{
		private BmobFile picture;//图片
	private String pictureUrl;//图片地址
	private String zqda;

	
	
	public void setZqda(String zqda)
	{
		this.zqda = zqda;
	}

	public String getZqda()
	{
		return zqda;
	}
	
	

	public void setPicture(BmobFile picture) {
		this.picture = picture;
	}

	public BmobFile getPicture() {
		return picture;
	}

	public void setPictureUrl(String pictureUrl) {
		this.pictureUrl = pictureUrl;
	}

	public String getPictureUrl() {
		return pictureUrl;
	}
}
