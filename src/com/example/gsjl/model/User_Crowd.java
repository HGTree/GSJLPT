package com.example.gsjl.model;

import cn.bmob.v3.BmobObject;

/**
*@author hugeng E-mail:958996499@qq.com
*@version:2018年4月29日下午4:45:24
*
*/
public class User_Crowd extends BmobObject{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private User Uid;
	private Crowd Cid;
	public User getUid() {
		return Uid;
	}
	public void setUid(User uid) {
		Uid = uid;
	}
	public Crowd getCid() {
		return Cid;
	}
	public void setCid(Crowd cid) {
		Cid = cid;
	}
	
}
