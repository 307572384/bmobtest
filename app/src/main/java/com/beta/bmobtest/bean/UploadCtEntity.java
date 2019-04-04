package com.beta.bmobtest.bean;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.*;
import cn.bmob.v3.datatype.BmobFile;

//上传图片数据表
public class UploadCtEntity extends BmobObject
{

	public List<BmobFile> getPicture() {
		return picture;
	}

	public void setPicture(List<BmobFile> picture) {
		this.picture = picture;
	}

	private List<BmobFile> picture;//图片
	private String         zqda;
	private List<String>   image_url;
	public List<String> getImage_url() {
		return image_url;
	}

	public void setImage_url(List<String> image_url) {
		this.image_url = image_url;
	}



	public ArrayList<BmobFile> getFile_picture() {
		return file_picture;
	}

	public void setFile_picture(ArrayList<BmobFile> file_picture) {
		this.file_picture = file_picture;
	}

	private ArrayList<BmobFile> file_picture;//图片集合存放多图



	public void setZqda(String zqda)
	{
		this.zqda = zqda;
	}

	public String getZqda()
	{
		return zqda;
	}







}