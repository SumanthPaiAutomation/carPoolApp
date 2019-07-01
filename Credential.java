package com.ample.util;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="Credential")
public class Credential {
	private String userName;
	private String passWord;
	private boolean loginSuccess;
	public boolean isLoginSuccess() {
		return loginSuccess;
	}
	public void setLoginSuccess(boolean loginSuccess) {
		this.loginSuccess = loginSuccess;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassWord() {
		return passWord;
	}
	public void setPassWord(String passWord) {
		this.passWord = passWord;
	}
	
}
