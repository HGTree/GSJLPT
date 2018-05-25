package com.example.gsjl.model;

import cn.bmob.v3.BmobObject;

/**
*@author hugeng E-mail:958996499@qq.com
*@version:2018年4月29日下午4:57:26
*
*/
public class MessageFroum extends BmobObject{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String MFcontent;
	private User Uid;
	private String isHeading;
	private String MFid;

	public String getIsHeading() {
		return isHeading;
	}

	public void setIsHeading(String isHeading) {
		this.isHeading = isHeading;
	}

	public String getMFcontent() {
		return MFcontent;
	}

	public void setMFcontent(String mFcontent) {
		MFcontent = mFcontent;
	}


	public User getUid() {
		return Uid;
	}

	public void setUid(User uid) {
		Uid = uid;
	}

	public String getMFid() {
		return MFid;
	}

	public void setMFid(String mFid) {
		MFid = mFid;
	}

	
}
