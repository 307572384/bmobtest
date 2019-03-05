package com.beta.bmobtest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register_layout);
		addControl();//加载控件
		addRegisterShow();//注册方法
		Bmob.initialize(this, "你自己的appid");
	}

	private void addRegisterShow() {
		register_ok.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String mobile = register_user.getText().toString().trim();
				String password = register_password.getText().toString().trim();

				//判断是否输入是否是电话号码
				boolean phone = isPhoneNumber(mobile);
				//判断密码
				boolean pwd = PasswordJudge(password);

				if(mobile.isEmpty() || password.isEmpty())
				{
					Toast.makeText(RegisterActivity.this,"用户名或密码不能为空",Toast.LENGTH_SHORT).show();
				}
				else if(!phone)
				{
					Toast.makeText(RegisterActivity.this,"请输入正确的手机号码",Toast.LENGTH_SHORT).show();

				}
				else if(!pwd)
				{
					Toast.makeText(RegisterActivity.this,"密码在以字母开头6-18位之前只能包含字母数字和下划线",Toast.LENGTH_SHORT).show();
				}
				else{
					Bmoblogin();
				}

			}
		});
	}
private boolean isPhoneNumber(String phoneStr)
{
	//定义电话格式的正则表达式
	String regex = "^((13[0-9])|(14[5,7])|(15[0-3,5-9])|(17[0,3,5-8])|(18[0-9])|166|198|199|(147))\\d{8}$";
	//设定查看模式
	Pattern p = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
	//判断Str是否匹配，返回匹配结果
	Matcher m = p.matcher(phoneStr);
	return m.matches();
}
private boolean PasswordJudge(String pwStr)
{

	//密码判断以字母开头，长度在6~18之间，只能包含字母、数字和下划线
	String pattern = "^[a-zA-Z]\\w{5,17}$";

	Pattern r = Pattern.compile(pattern);
	Matcher m = r.matcher(pwStr);
	return m.find();
}

	private void addControl() {
		register_user = (TextView) findViewById(R.id.id_register_username);
		register_password = (TextView) findViewById(R.id.id_register_userpassword);
		register_ok = (Button) findViewById(R.id.id_register_ok);


	}
	private void Bmoblogin()//Bmo注册写入方法
	{
		final BmobUser p2 = new BmobUser();
		p2.setUsername(register_user.getText().toString());
		p2.setPassword(register_password.getText().toString());
		p2.signUp(new SaveListener<BmobUser>() {
			@Override
			public void done(BmobUser bmobUser, BmobException e) {
				if (e == null) {
					//判断是否注册成功成功则跳转到登陆的页面
					Intent intent_register = new Intent(RegisterActivity.this,MainActivity.class);
					startActivity(intent_register);
					Toast.makeText(RegisterActivity.this, "添加数据成功，返回objectId为：" + p2.getObjectId(), Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(RegisterActivity.this, "用户名或者密码不能为空", Toast.LENGTH_SHORT).show();
				}
			}
		});
	}
}