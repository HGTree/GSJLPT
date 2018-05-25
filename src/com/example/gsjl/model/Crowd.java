package com.example.gsjl.model;

import cn.bmob.v3.BmobObject;

/**
*@author hugeng E-mail:958996499@qq.com
*@version:2018年4月29日下午4:47:27
*
*/
public class Crowd extends BmobObject{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Number Cimage;
	private String Cname;
	private String Cexplain;
	private User Uid;
	


	public Number getCimage() {
		return Cimage;
	}

	public void setCimage(Number cimage) {
		Cimage = cimage;
	}

	public User getUid() {
		return Uid;
	}

	public void setUid(User uid) {
		Uid = uid;
	}

	public String getCname() {
		return Cname;
	}

	public void setCname(String cname) {
		Cname = cname;
	}

	public String getCexplain() {
		return Cexplain;
	}

	public void setCexplain(String cexplain) {
		Cexplain = cexplain;
	}
	
}
