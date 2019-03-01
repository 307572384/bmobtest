package com.beta.bmobtest;

import cn.bmob.v3.BmobObject;

/**
 * Created by Kevein on 2019/3/1.7:49
 */

public class Person extends BmobObject {

	private String name;
	private String password;

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
}
