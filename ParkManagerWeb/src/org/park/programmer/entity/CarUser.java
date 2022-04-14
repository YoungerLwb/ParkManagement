package org.park.programmer.entity;

import java.io.InputStream;

/**
 * 
 * @author Administrator
 *用户实体类
 */
public class CarUser {
	private int uid;
	private String username;//用户名
	private String upwd;//用户名密码
	private String uicd;//ic卡号
	private String uname;//姓名
	private String usex;//性别
	private String utel;//电话
	private String ubalance;//卡内余额
	private InputStream uphoto;//照片
	public int getUid() {
		return uid;
	}
	public void setUid(int uid) {
		this.uid = uid;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getUpwd() {
		return upwd;
	}
	public void setUpwd(String upwd) {
		this.upwd = upwd;
	}
	public String getUicd() {
		return uicd;
	}
	public void setUicd(String uicd) {
		this.uicd = uicd;
	}
	public String getUname() {
		return uname;
	}
	public void setUname(String uname) {
		this.uname = uname;
	}
	public String getUsex() {
		return usex;
	}
	public void setUsex(String usex) {
		this.usex = usex;
	}
	public String getUtel() {
		return utel;
	}
	public void setUtel(String utel) {
		this.utel = utel;
	}
	public String getUbalance() {
		return ubalance;
	}
	public void setUbalance(String ubalance) {
		this.ubalance = ubalance;
	}
	public InputStream getUphoto() {
		return uphoto;
	}
	public void setUphoto(InputStream uphoto) {
		this.uphoto = uphoto;
	}

}
