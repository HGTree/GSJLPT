package com.example.gsjl.model;

import cn.bmob.v3.BmobObject;

/**
*@author hugeng E-mail:958996499@qq.com
*@version:2018年4月29日下午5:10:37
*
*/
public class MessageTalk extends BmobObject{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String MTtype;
	private String MTcontent;
	private User Uidsend;
	private User Uidaccept;
	private String MTstate;
	private Crowd Cidaccept;
	public String getMTtype() {
		return MTtype;
	}
	public void setMTtype(String mTtype) {
		MTtype = mTtype;
	}
	public String getMTcontent() {
		return MTcontent;
	}
	public void setMTcontent(String mTcontent) {
		MTcontent = mTcontent;
	}
	public User getUidsend() {
		return Uidsend;
	}
	public void setUidsend(User uidsend) {
		Uidsend = uidsend;
	}
	public User getUidaccept() {
		return Uidaccept;
	}
	public void setUidaccept(User uidaccept) {
		Uidaccept = uidaccept;
	}
	public String getMTstate() {
		return MTstate;
	}
	public void setMTstate(String mTstate) {
		MTstate = mTstate;
	}
	public Crowd getCidaccept() {
		return Cidaccept;
	}
	public void setCidaccept(Crowd cidaccept) {
		Cidaccept = cidaccept;
	}
	

}
