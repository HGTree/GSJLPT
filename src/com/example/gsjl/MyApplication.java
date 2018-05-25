package com.example.gsjl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import android.app.Application;
import cn.bmob.newim.BmobIM;
import cn.bmob.newim.listener.BmobIMMessageHandler;
import cn.bmob.v3.Bmob;

/**
 * @author hugeng E-mail:958996499@qq.com
 * @version:2018年5月11日下午12:58:32
 *
 */
public class MyApplication extends Application {

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		// 初始化Bmob SDK
		if(getApplicationInfo().packageName.equals(getMyProcessName())){
			Bmob.initialize(this, "2ab0e841d7533bad91345fb6ccee44b4");
			BmobIM.init(this);
			BmobIM.registerDefaultMessageHandler(new MyImMessageHandler());
		}
		
	}

	/**
	 * 获取当前运行的进程名
	 * 
	 * @return
	 */
	public static String getMyProcessName() {
		try {
			File file = new File("/proc/" + android.os.Process.myPid() + "/" + "cmdline");
			BufferedReader mBufferedReader = new BufferedReader(new FileReader(file));
			String processName = mBufferedReader.readLine().trim();
			mBufferedReader.close();
			return processName;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
