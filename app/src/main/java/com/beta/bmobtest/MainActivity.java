package com.beta.bmobtest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

public class MainActivity extends AppCompatActivity {
	private Person p2;
	private TextView lgUser;
	private TextView lgPassword;
	private Button btn_ok;
	private Button btn_rg;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Bmob.initialize(this, "16e74751f4ede2e59f1fcae1e508cc3b");


		addControl();
		addLogin();

	}
	private void addLogin() {
		btn_rg.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(MainActivity.this,RegisterActivity.class);
				startActivity(intent);
			}
		});

		btn_ok.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
					String lgU=lgUser.getText().toString().trim();
					String lgp=lgPassword.getText().toString().trim();
					final BmobUser bu2= new BmobUser();
				bu2.setUsername(lgU);
				bu2.setPassword(lgp);
					bu2.login(new SaveListener<BmobUser>() {
						@Override
						public void done(BmobUser bmobUser, BmobException e) {
							if(e==null)
							{
								Intent in_success = new Intent(MainActivity.this,Success.class);
								startActivity(in_success);

							}
							else
							{
								Toast.makeText(MainActivity.this, "账户名或密码不正确", Toast.LENGTH_SHORT).show();
								//loge(e);
							}

						}
					});



	}



});
	}
	private void addControl() {

		lgUser = (TextView) findViewById(R.id.id_username);
		lgPassword = (TextView) findViewById(R.id.id_userpassword);
		btn_ok = (Button) findViewById(R.id.id_ok);
		btn_rg = (Button) findViewById(R.id.id_register);
	}
	}
