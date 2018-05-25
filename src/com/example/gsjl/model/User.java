package com.example.gsjl.model;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobUser;

/**
*@author hugeng E-mail:958996499@qq.com
*@version:2018年4月29日下午4:35:29
*
*/
public class User extends BmobObject{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String Uname;
	private String Uemail;
	private String Upassword;
	private String Uphone;
	private Number Uimage;
	private String Usex;
	private String Urole;
	private Department Did;
	public String getUname() {
		return Uname;
	}
	public void setUname(String uname) {
		Uname = uname;
	}
	public String getUemail() {
		return Uemail;
	}
	public void setUemail(String uemail) {
		Uemail = uemail;
	}

	public String getUpassword() {
		return Upassword;
	}
	public void setUpassword(String upassword) {
		Upassword = upassword;
	}
	public String getUphone() {
		return Uphone;
	}
	public void setUphone(String uphone) {
		Uphone = uphone;
	}


	public Number getUimage() {
		return Uimage;
	}
	public void setUimage(Number uimage) {
		Uimage = uimage;
	}

	public String getUsex() {
		return Usex;
	}
	public void setUsex(String usex) {
		Usex = usex;
	}
	public String getUrole() {
		return Urole;
	}
	public void setUrole(String urole) {
		Urole = urole;
	}
	public Department getDid() {
		return Did;
	}
	public void setDid(Department did) {
		Did = did;
	}



}
