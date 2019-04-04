package com.beta.bmobtest;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.beta.bmobtest.adapter.ImageAdapter;
import com.beta.bmobtest.bean.UploadCtEntity;
import com.donkingliang.imageselector.utils.ImageSelector;
import com.donkingliang.imageselector.utils.ImageSelectorUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobBatch;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BatchResult;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListListener;
import cn.bmob.v3.listener.UploadBatchListener;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

/**
 * Created by Kevein on 2019/3/1.8:39
 * Luban压缩后上传图片后Glide显示
 */

public class Success extends Activity implements View.OnClickListener {
	private static final int REQUEST_CODE = 0x00000011;
	private Button bt1, bt2;//1添加图片，2上传图片
	private String    picturePath;//图片路径
	private File      mFile;//用于存储鲁班压缩完返回的图片地址
	private Button    mainButton1;
	private Button    mainButton2;
	private Context mContext = this;
	private ImageAdapter mAdapter;
	private RecyclerView rvImage;
	Set<String> tsx = new HashSet<String>();
	private List           up_urlx   =new ArrayList<>();//外部存储图片url
	private List<BmobFile> up_image = new ArrayList<>();//外部存储图片
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.success);
		initView();
		Bmob.initialize(this, "");//这里替换成你的
		bt1 = (Button) findViewById(R.id.mainButton1);
		bt2 = (Button) findViewById(R.id.mainButton2);
		mAdapter = new ImageAdapter(this);
		rvImage = findViewById(R.id.rv_image);
		rvImage.setLayoutManager(new GridLayoutManager(this, 3));
		rvImage.setAdapter(mAdapter);
	}

	private void showToast(String text) {//简化Toast
		Toast.makeText(mContext, text, Toast.LENGTH_SHORT).show();
	}


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REQUEST_CODE && data != null) {
			//获取选择器返回的数据
			ArrayList<String> images = data.getStringArrayListExtra(
					ImageSelectorUtils.SELECT_RESULT);

			/**
			 * 是否是来自于相机拍照的图片，
			 * 只有本次调用相机拍出来的照片，返回时才为true。
			 * 当为true时，图片返回的结果有且只有一张图片。
			 */
//			boolean isCameraImage = data.getBooleanExtra(ImageSelector.IS_CAMERA_IMAGE, false);

			y_Luban(images);
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

	private void y_Luban(final ArrayList<String> images) {

		final ArrayList<String> newList = new ArrayList<>();//压缩后的图片路径
		Luban.with(mContext)
				.load(images)
				.setTargetDir(getPath())
				.ignoreBy(100)
				.setCompressListener(new OnCompressListener() {
					@Override
					public void onStart() {
					}

					@Override
					public void onSuccess(final File file) {
//						mAdapter.refresh(images);
						final ArrayList<BmobFile> bmobFiles = new ArrayList<BmobFile>();
//						newList.add(file.getPath());//将获取到的压缩图片地址放入newList中
//						final String[] arrString = newList.toArray(new String[newList.size()]);
//						List list = new ArrayList();
						Set<String> set = new HashSet<>();
						final List<String> list1 = new ArrayList<>();
						for(int i=0;i<9;i++)
						{
							set.add(file.getPath());//将压缩好的九张图片封装进set

						}
						Iterator<String> it = set.iterator();
						while(it.hasNext())
						{
							list1.add(it.next());//遍历如果有重复就删掉没有重复就将图片封装进List
						}
//						UploadCtEntity uploadCtEntity = new UploadCtEntity();

//						uploadCtEntity.setZqda("测试");
						final String[] arrString = list1.toArray(new String[list1.size()]);
						BmobFile.uploadBatch(arrString, new UploadBatchListener() {
							@Override
							public void onSuccess(List<BmobFile> files, List<String> urls) {
								if(urls.size()==arrString.length){//如果数量相等，则代表文件全部上传完成
									//do something
									final List<String>           up_url   = new ArrayList<>(urls);//外部存储图片url
									final List<BmobFile> up_image =  new ArrayList<>(files);//外部存储图片
									up_url.addAll(urls);
									up_image.addAll(files);
//									System.out.println(up_url);
									 Set<String> ts = new HashSet<String>();//使用set方法防止List中有多个数组
									ts.addAll(up_url);
									tsx = ts;
									List<String> list2 = new ArrayList<String> ();
									list2.addAll(tsx);
									up_urlx = list2;
								}

//									for (String listf:urls)
//									{
//										//增强型遍历返回的urls将onSuccess获取到的数据进行回调，覆盖然后存放在up_url中
//										up_url.add(listf);
//									}
//									//迭代器将onSuccess获取到的数据进行回调，覆盖存放到up_image中
//
//									Iterator it = files.iterator();
//									while(it.hasNext())
//									{
//											up_image.add((BmobFile) urls);
//									}
//
////									for(BmobFile up_imagex :files){
////										//由于BmobFile中没有add的方法所以用
////										BmobFile upimage = new BmobFile();
////
////										//方法体
////									}
									//do something
								}


							@Override
							public void onProgress(int i, int i1, int i2, int i3) {

							}

							@Override
							public void onError(int i, String s) {

							}
						});
						//								persons.add(uploadCtEntity);
//						uploadCtEntity.save(new SaveListener<String>() {
//							@Override
//							public void done(String s, BmobException e) {
//
//								BmobFile.uploadBatch(arrString, new UploadBatchListener() {
//									@Override
//									public void onSuccess(List<BmobFile> files, List<String> urls) {
////										List<BmobObject> persons = new ArrayList<>((Collection<? extends BmobObject>) new File(String.valueOf(files)));
//										up_url = urls;
//										up_image = files;
//									/*	Set<String> set = new HashSet<>();
//										final List<String> list1 = new ArrayList<>();
//										for(int i=0;i<9;i++)
//										{
//											set.add(file.getPath());//将压缩好的九张图片封装进set
//											set.add(urls.get(i));
//
//										}
//										Iterator<String> it = set.iterator();
//										while(it.hasNext())
//										{
//											list1.add(it.next());//遍历如果有重复就删掉没有重复就将图片封装进List
//										}*/
////										List<BmobFile> up_image = files;
//										//声明该Collection模板类的"类型变量"可以是所有Object的子类
//										/*UploadCtEntity uploadCtEntity = new UploadCtEntity("测试",files.get(0));
//										uploadCtEntity.setPicturl(up_url);//图片地址
//										uploadCtEntity.setPicture((BmobFile) up_image);//图片对象
//										new BmobBatch().insertBatch(persons).doBatch(new QueryListListener<BatchResult>() {
//											@Override
//											public void done(List<BatchResult> list, BmobException e) {
//												if (e == null)
//												{
//													for(int i = 0 ;i<list.size();i++)
//													{
//														BatchResult result = list.get(i);
//														BmobException ex =result.getError();
//														if(ex==null){
//															showToast("数据更新成功");
//														}else{
//															showToast(i+"个更新失败"+ex.getMessage()+","+ex.getErrorCode());
//														}
//													}
//												}
//												else{
//													showToast("数据更新失败");
//												}
//											}
//										});
//*/
//									}
//
//									@Override
//									public void onProgress(int i, int i1, int total, int i3) {
//
//										showToast("total" + total);//提示上传个数
//
//									}
//
//									@Override
//									public void onError(int i, String s) {
//
//									}
//								});
//							}
//						});


						//将Arraylist转化为String[]
						//						BmobFile.uploadBatch(arrString, new UploadBatchListener() {//上传压缩后的图片
						//							@Override
						//							public void onSuccess(List<BmobFile> files, List<String> urls) {
						//								List<BmobObject> up_images = new ArrayList<BmobObject>();
						//								UploadCtEntity uploadCtEntity = new UploadCtEntity(); //将图片存储到表中
						//								uploadCtEntity.setZqda("测试");
						//								uploadCtEntity.setPictureUrl(String.valueOf(urls));
						//								uploadCtEntity.setUp_picture((BmobFile) files);
						//								up_images.add(uploadCtEntity);
						//								new BmobBatch().insertBatch(up_images).doBatch(new QueryListListener<BatchResult>() {
						//									@Override
						//									public void done(List<BatchResult> o, BmobException e) {
						//										if (e == null) {
						//											for (int i = 0; i < o.size(); i++) {
						//												BatchResult result = o.get(i);
						//												BmobException ex = result.getError();
						//												if (ex == null) {
						//													showToast("上传图片成功" + result + "张"  );
						//												} else {
						//													showToast("上传图片失败第" + i + ex.getMessage() + ex.getErrorCode());
						//												}
						//											}
						//										} else {
						//											Log.i("bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());
						//										}
						//									}
						//								});
						//							}
						//
						//							@Override
						//							public void onProgress(int i, int i1, int i2, int i3) {
						//
						//							}
						//
						//							@Override
						//							public void onError(int i, String s) {
						//
						//							}
						//						});

					}

					@Override
					public void onError(Throwable e) {

					}
				}).launch();
	}


	private void initView() {
		mainButton1 = (Button) findViewById(R.id.mainButton1);
		mainButton2 = (Button) findViewById(R.id.mainButton2);

		mainButton1.setOnClickListener(this);
		mainButton2.setOnClickListener(this);
	}
	/** 批量插入操作
	 * insertBatch
	 * @return void
	 * @throws
	 */
	public void BmobInsert ()
	{

		List<BmobObject> list = new ArrayList<BmobObject>();
		//需要研究一下List集合，暂时只能发送一条数据类型批量不行

			UploadCtEntity uploadCtEntity = new UploadCtEntity();
			uploadCtEntity.setZqda("测试");
			uploadCtEntity.setImage_url(up_urlx);
			list.add( uploadCtEntity);

		new BmobBatch().insertBatch(list).doBatch(new QueryListListener<BatchResult>() {
			@Override
			public void done(List<BatchResult> list, BmobException e) {
				if(e==null)
				{System.out.println(list);
					showToast("成功插入表"+list.size());
				}
			}
		});

	}

	/*public void insertBatch(List<BmobObject> files){
		new BmobObject().insertBatch(MainActivity.this, files, new SaveListener() {

			@Override
			public void onSuccess() {
				// TODO Auto-generated method stub
				ShowToast("---->批量更新成功");
			}

			@Override
			public void onFailure(int arg0, String arg1) {
				// TODO Auto-generated method stub
				ShowToast("---->批量更新失败"+arg0);

			}
		});
	}*/
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.mainButton1:
				ImageSelector.builder()
						.useCamera(true) // 设置是否使用拍照
						.setSingle(false)  //设置是否单选
						.setMaxSelectCount(1) // 图片的最大选择数量，小于等于0时，不限数量。
						.start(this, REQUEST_CODE); // 打开相册
				break;
			case R.id.mainButton2:
				BmobInsert();
				//								List<BmobObject> bmobFilesx= new ArrayList<BmobObject>();
				//								UploadCtEntity uploadCtEntity = new UploadCtEntity();
				//								uploadCtEntity.setZqda("测试");
				//				//				bmobFilesx.add(uploadCtEntity);//添加uploadCtEntity内的数据进集合里面（详情根据Bmob开发文档进行编写）
				//				//				new BmobBatch().
				//								uploadCtEntity.save(new SaveListener<String>() {
				//									@Override
				//									public void done(String p1, BmobException p2) {
				//										// TODO: Implement this method
				//										if (p2 == null) {
				//											uploadTp(mFile, String.valueOf(p2));
				//										}
				//									}
				//								});


				break;
		}
	}
}
