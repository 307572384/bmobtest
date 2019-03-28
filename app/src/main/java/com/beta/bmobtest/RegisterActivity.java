package com.beta.bmobtest;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import me.weyye.hipermission.HiPermission;
import me.weyye.hipermission.PermissionCallback;
import me.weyye.hipermission.PermissionItem;

/**
 * Created by Kevein on 2019/3/1.8:03
 */

public class RegisterActivity extends Activity implements View.OnClickListener {
	public static final String TAG = "RegisterActivity";
	//倒计时广播
	private final BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			final String action = intent.getAction();
			switch (action) {
				case RegisterCodeTimerService.IN_RUNNING:
					if (register_countdown.isEnabled())
						register_countdown.setEnabled(false);
					//正在倒计时
					register_countdown.setText("获取验证码(" + intent.getStringExtra("time") + ")");
					Log.e(TAG, "倒计时中(" + intent.getStringExtra("time") + ")");
					break;
				case RegisterCodeTimerService.END_RUNNING:
					//完成倒计时
					register_countdown.setEnabled(true);
					register_countdown.setText("获取验证码");
					break;
			}
		}
	};
	private Intent   mIntent;
	private Context  mContext;
	private EditText register_phone;
	private EditText register_password;
	private Button   register_ok, register_countdown;
	private EditText register_ValiDation;//输入验证码

	// 注册广播
	private static IntentFilter updateIntentFilter() {
		final IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(RegisterCodeTimerService.IN_RUNNING);
		intentFilter.addAction(RegisterCodeTimerService.END_RUNNING);
		return intentFilter;
	}

	private void showToast(String text) {//简化Toast
		Toast.makeText(mContext, text, Toast.LENGTH_SHORT).show();
	}

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register_layout);
		mContext = this;
		addControl();//加载控件
		Bmob.initialize(this, "你自己的appid");
	}

	@Override
	protected void onResume() {
		super.onResume();
		// 注册广播
		registerReceiver(mBroadcastReceiver, updateIntentFilter());
	}

	@Override
	protected void onPause() {
		super.onPause();
		// 移除注册
		unregisterReceiver(mBroadcastReceiver);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.e(TAG, "onDestroy 方法调用");
	}

	private boolean isPhoneNumber(String phoneStr) {
		//定义电话格式的正则表达式
		String regex = "^((13[0-9])|(14[5,7])|(15[0-3,5-9])|(17[0,3,5-8])|(18[0-9])|166|198|199|(147))\\d{8}$";
		//设定查看模式
		Pattern p = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
		//判断Str是否匹配，返回匹配结果
		Matcher m = p.matcher(phoneStr);
		return m.matches();
	}

	private boolean PasswordJudge(String pwStr) {

		//密码判断以字母开头，长度在6~18之间，只能包含字母、数字和下划线
		String pattern = "^[a-zA-Z]\\w{5,17}$";

		Pattern r = Pattern.compile(pattern);
		Matcher m = r.matcher(pwStr);
		return m.find();
	}

	private void addControl() {

		register_ok = (Button) findViewById(R.id.id_register_ok);
		register_phone = (EditText) findViewById(R.id.register_phone);
		register_password = (EditText) findViewById(R.id.register_password);
		register_countdown = (Button) findViewById(R.id.btn_countdown);
		register_ValiDation = (EditText) findViewById(R.id.et_validation);
		register_countdown.setOnClickListener(this);
		register_ok.setOnClickListener(this);

	}


	//动态获取手机状态和发送短信权限
	private void Permissionx() {
		List<PermissionItem> permissions = new ArrayList<PermissionItem>();
		permissions.add(new PermissionItem(Manifest.permission.SEND_SMS, getString(R.string.permission_send_sms_phone), R.drawable.permission_ic_sms));
		permissions.add(new PermissionItem(Manifest.permission.READ_PHONE_STATE, getString(R.string.permission_read_phone_state), R.drawable.permission_ic_phone));
		permissions.add(new PermissionItem(Manifest.permission.CAMERA,"允许打开相机",R.drawable.permission_ic_camera));
		HiPermission.create(mContext)
				.title(getString(R.string.permission_cus_title))
				.permissions(permissions)
				.msg(getString(R.string.permission_cus_msg))
				.animStyle(R.style.PermissionAnimModal)
				.style(R.style.PermissionDefaultGreenStyle)
				.checkMutiPermission(new PermissionCallback() {
					@Override
					public void onClose() {
						Log.i(TAG, "onClose");
						showToast(getString(R.string.permission_on_close));
					}

					@Override
					public void onFinish() {
						showToast(getString(R.string.permission_completed));
					}

					@Override
					public void onDeny(String permission, int position) {
						Log.i(TAG, "onDeny");
					}

					@Override
					public void onGuarantee(String permission, int position) {
						Log.i(TAG, "onGuarantee");
					}
				});


	}

	@Override
	public void onClick(View view) {
		String mobile = register_phone.getText().toString().trim();
		//		String password = register_password.getText().toString().trim();
		switch (view.getId()) {

			case R.id.btn_countdown: {//发送短信验证
				mIntent = new Intent(mContext, RegisterCodeTimerService.class);//接收倒计时广播
				Permissionx();//安卓6.0动态获取权限
				// 将按钮设置为不可用状态
				register_countdown.setEnabled(false);
				// 启动倒计时的服务
				startService(mIntent);
				//				 通过requestSMSCode方式给绑定手机号的该用户发送指定短信模板的短信验证码
				BmobSMS.requestSMSCode(mobile, "1", new QueryListener<Integer>() {
					@Override
					public void done(Integer smsId, BmobException e) {
						if (e == null) {
							showToast(getString(R.string.smssuccess) + smsId);
						} else {
							showToast(getString(R.string.failed) + smsId);
						}
					}
				});
				break;
			}
			case R.id.id_register_ok:
				String password = register_password.getText().toString().trim();//获取密码
				String valiation = register_ValiDation.getText().toString();//获取验证码
				boolean phone = isPhoneNumber(mobile);
				//判断密码
				boolean pwd = PasswordJudge(password);

				if (mobile.isEmpty() || password.isEmpty()) {
					showToast(getString(R.string.user_or_password_empty));
					return;
				} else if (!phone) {
					showToast(getString(R.string.phone_failed));
					return;
				} else if (!pwd) {
					showToast(getString(R.string.userpassword_failed));
					return;
				} else if (TextUtils.isEmpty(valiation)) {
					showToast(getString(R.string.valiation_empty));
					return;

				} else {
					//Bmob注册验证方法
					User p2 = new User();
					p2.setMobilePhoneNumber(mobile);
					p2.setPassword(password);
					p2.signOrLogin(valiation, new SaveListener<BmobUser>() {//valiation关于获取到验证码
						@Override
						public void done(BmobUser bmobUser, BmobException e) {
							if (e == null) {
								//判断是否注册成功成功则跳转到登陆的页面
								Intent intent_register = new Intent(RegisterActivity.this, MainActivity.class);
								startActivity(intent_register);
								showToast(getString(R.string.user_success));
							} else {
								showToast(getString(R.string.user_failed) + e.getErrorCode() + e.getMessage());
							}
						}
					});
					break;
				}

		}
	}
}