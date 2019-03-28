package com.beta.bmobtest;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class MainActivity extends AppCompatActivity {
	private Person                   p2;
	private TextView                 lgUser;
	private TextView                 lgPassword;
	private Button                   btn_ok;
	private Button                   btn_rg;
	private CheckBox                 rememberPass;
	private SharedPreferences        pref;
	private SharedPreferences.Editor editor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Bmob.initialize(this, "");//替换成你自己的


		addControl();
		addLogin();

	}

	private void addLogin() {
		btn_rg.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
				startActivity(intent);
			}
		});

		btn_ok.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				String lgU = lgUser.getText().toString().trim();
				String lgp = lgPassword.getText().toString().trim();
				editor = pref.edit();
				if (rememberPass.isChecked()) {
					editor.putBoolean("remember_password", true);
					editor.putString("account", lgU);
					editor.putString("password", lgp);
				} else {
					editor.clear();
				}
				editor.apply();
				final BmobUser bu2 = new BmobUser();
				bu2.setUsername(lgU);
				bu2.setPassword(lgp);
				bu2.login(new SaveListener<BmobUser>() {
					@Override
					public void done(BmobUser bmobUser, BmobException e) {
						if (e == null) {
							Intent in_success = new Intent(MainActivity.this, Success.class);
							startActivity(in_success);

						} else {
							Toast.makeText(MainActivity.this, "账户名或密码不正确", Toast.LENGTH_SHORT).show();
							//loge(e);
						}

					}
				});


			}


		});

	}

	private void addControl() {
		pref = PreferenceManager.getDefaultSharedPreferences(this);
		rememberPass = (CheckBox) findViewById(R.id.remember);
		lgUser = (TextView) findViewById(R.id.id_username);
		lgPassword = (TextView) findViewById(R.id.id_userpassword);
		btn_ok = (Button) findViewById(R.id.id_ok);
		btn_rg = (Button) findViewById(R.id.id_register);
		boolean isRemenber = pref.getBoolean("remember_password", false);
		if (isRemenber) {
			//将账号和密码都设置到文本中
			String account = pref.getString("account", "");
			String password = pref.getString("password", "");
			lgUser.setText(account);
			lgPassword.setText(password);
			rememberPass.setChecked(true);

		}
	}
}
