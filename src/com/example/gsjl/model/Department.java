package com.example.gsjl.model;

import cn.bmob.v3.BmobObject;

/**
*@author hugeng E-mail:958996499@qq.com
*@version:2018年4月29日下午4:55:02
*
*/
public class Department extends BmobObject{
	/**
	 * 部门信息模型类
	 * 部门名称
	 * 部门简介
	 */
	private static final long serialVersionUID = 1L;
	private String Dname;
	private String Ddetail;
	public String getDname() {
		return Dname;
	}
	public void setDname(String dname) {
		Dname = dname;
	}
	public String getDdetail() {
		return Ddetail;
	}
	public void setDdetail(String ddetail) {
		Ddetail = ddetail;
	}

}
