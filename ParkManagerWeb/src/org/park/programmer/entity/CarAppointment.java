package org.park.programmer.entity;

import java.util.Date;

/**
 * 预约表实体类
 * @author Administrator
 *
 */
public class CarAppointment {
	public static int APPOINT_STATUS_WAIT = 0;//待审核
	public static int APPOINT_STATUS_AGREE = 1;//同意
	public static int APPOINT_STATUS_DISAGREE = -1;//不同意
	private int aid;
	private String ausername;
	private String aname;
	private String atel;
	private Date appointtime = null;
	private int astatus = APPOINT_STATUS_WAIT;
	private Date addtime;
	
	
	public int getAid() {
		return aid;
	}
	public void setAid(int aid) {
		this.aid = aid;
	}
	public String getAusername() {
		return ausername;
	}
	public void setAusername(String ausername) {
		this.ausername = ausername;
	}
	public String getAname() {
		return aname;
	}
	public void setAname(String aname) {
		this.aname = aname;
	}
	public String getAtel() {
		return atel;
	}
	public void setAtel(String atel) {
		this.atel = atel;
	}
	public Date getAppointtime() {
		return appointtime;
	}
	public void setAppointtime(Date appointtime) {
		this.appointtime = appointtime;
	}
	public int getAstatus() {
		return astatus;
	}
	public void setAstatus(int status) {
		this.astatus = status;
	}
	public Date getAddtime() {
		return addtime;
	}
	public void setAddtime(Date addtime) {
		this.addtime = addtime;
	}
	
}
