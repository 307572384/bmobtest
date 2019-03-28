package com.beta.bmobtest;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.beta.bmobtest.bean.UploadCtEntity;
import com.bumptech.glide.Glide;

import java.io.File;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

/**
 * Created by Kevein on 2019/3/1.8:39
 * Luban压缩后上传图片后Glide显示
 */

public class Success extends Activity {
	private Button bt1, bt2;//1添加图片，2上传图片
	private ImageView img;
	private TextView  tw1;//本地路径以及上传返回路径
	private String    picturePath;//图片路径
	private File      mFile;//用于存储鲁班压缩完返回的图片地址

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.success);
		Bmob.initialize(this, "");//这里替换成你的
		bt1 = (Button) findViewById(R.id.mainButton1);
		bt2 = (Button) findViewById(R.id.mainButton2);
		img = (ImageView) findViewById(R.id.mainImageView1);
		tw1 = (TextView) findViewById(R.id.mainTextView1);
		bt1.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View p1) {
				Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				startActivityForResult(i, 1);

				// TODO: Implement this method
			}
		});
		bt2.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View p1) {
				UploadCtEntity uploadCtEntity = new UploadCtEntity();

				uploadCtEntity.setZqda("测试");
				uploadCtEntity.save(new SaveListener<String>() {


					@Override
					public void done(String p1, BmobException p2) {
						// TODO: Implement this method
						if (p2 == null) {
							uploadTp(mFile, p1);//上传图片
						}
					}
				});


				// TODO: Implement this method
			}
		});


	}

	private void uploadTp(File filePath, final String objectId) {
		//创建文件对象并传入图片路径
		final BmobFile bmobFile = new BmobFile(new File(String.valueOf(filePath)));
		bmobFile.uploadblock(new UploadFileListener() {

			@Override
			public void done(BmobException p1) {
				// TODO: Implement this method
				if (p1 == null) {
					//上传成功
					String url = bmobFile.getFileUrl();//获取图片地址
					UploadCtEntity uploadCtEntity = new UploadCtEntity();
					uploadCtEntity.setPicture(bmobFile);//设置图片对象
					uploadCtEntity.setPictureUrl(url);//设置图片地址
					uploadCtEntity.update(objectId, new UpdateListener() {

						@Override
						public void done(BmobException p1) {
							// TODO: Implement this method
							if (p1 == null) {
								Toast.makeText(Success.this, "上传成功", Toast.LENGTH_SHORT).show();
								tw1.setText(bmobFile.getFileUrl());
								Glide.with(Success.this).load(bmobFile.getFileUrl()).transform(new BitmapTran(Success.this, 16)).into(img);

							} else {
								Toast.makeText(Success.this, p1.toString(), Toast.LENGTH_SHORT).show();
							}
						}
					});
				}
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 1 && resultCode == RESULT_OK && null != data) {
			Uri selectedImage = data.getData();
			String[] filePathColumn = {MediaStore.Images.Media.DATA};
			Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
			cursor.moveToFirst();
			int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
			//图片路径
			picturePath = cursor.getString(columnIndex);
			cursor.close();
			File filex = new File(picturePath);
			InitLuBan(filex);//将获取到的图片地址传到鲁班过去压缩
			//将图片显示到ImageView
			//本地路径

//			img.setImageBitmap(BitmapFactory.decodeFile(picturePath));

		}
	}

	//存放鲁班压缩完后的图片你可以不写但是为了看效果所以就先这么写着
	private String getPath() {
		String path = Environment.getExternalStorageDirectory() + "/Luban/image/";
		File file = new File(path);
		if (file.mkdirs()) {
			return path;
		}
		return path;
	}

	private void InitLuBan(File file) {
		Luban.with(this)
				.load(file)//加载图片地址
				.ignoreBy(100)//默认100KB以下文件不压缩
				.setTargetDir(getPath())//设置保存的本地文件地址
				.setCompressListener(new OnCompressListener() {
					@Override
					public void onStart() {

					}

					@Override
					public void onSuccess(File file) {
						tw1.setText(picturePath);
						Glide.with(Success.this).//Glide加载获取成功后压缩的图片
								load(file)
								.into(img);
						mFile = file;//将压缩后的图片地址转换成公共类

					}

					@Override
					public void onError(Throwable e) {
					}
				}).launch();//启动鲁班
	}
}
