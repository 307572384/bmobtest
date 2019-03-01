package com.beta.bmobtest;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by Kevein on 2019/3/1.8:03
 */

public class RegisterActivity extends Activity {
	private TextView register_user;
	private TextView register_password;
	private Button   register_ok;
	private Person   p2;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register_layout);
		addControl();//加载控件
		addRegisterShow();//注册方法
		Bmob.initialize(this, "16e74751f4ede2e59f1fcae1e508cc3b");
	}
	private void addRegisterShow() {
		register_ok.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				final BmobUser p2 = new BmobUser();
				p2.setUsername(register_user.getText().toString());
				p2.setPassword(register_password.getText().toString());
				//插入方法
				p2.save(new SaveListener<BmobUser>() {
					@Override
					public void done(BmobUser bmobUser, BmobException e) {
						if (e == null) {
							Toast.makeText(RegisterActivity.this, "添加数据成功，返回objectId为：" + p2.getObjectId(), Toast.LENGTH_SHORT).show();
						} else {
							Toast.makeText(RegisterActivity.this, "用户名或者密码不能为空", Toast.LENGTH_SHORT).show();
						}
					}
				});

			}
		});
	}
	private void addControl() {
		register_user = (TextView) findViewById(R.id.id_register_username);
		register_password = (TextView) findViewById(R.id.id_register_userpassword);
		register_ok = (Button) findViewById(R.id.id_register_ok);


	}
}