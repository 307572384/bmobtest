package com.beta.bmobtest;

import android.app.Application;

import com.antfortune.freeline.FreelineCore;

/**
 * Created by Kevein on 2019/3/28.21:00
 */

public class application  extends Application {
	public void onCreate() {

		super.onCreate();

		FreelineCore.init(this);
	}
}
