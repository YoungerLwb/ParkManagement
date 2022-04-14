package org.park.programmer.entity;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 
 * @author Administrator
 * 车辆驶入表
 */
public class CarDriveIn {
	private int did;
	private String dplatenum;
	private String dname;
	private Date driveintime = null;
	private String dparkid;
	public int getDid() {
		return did;
	}
	public void setDid(int did) {
		this.did = did;
	}
	public String getDplatenum() {
		return dplatenum;
	}
	public void setDplatenum(String dplatenum) {
		this.dplatenum = dplatenum;
	}
	public String getDname() {
		return dname;
	}
	public void setDname(String dname) {
		this.dname = dname;
	}
	public Date getDriveintime() {
		return driveintime;
	}
	public void setDriveintime(Date driveintime) {
		this.driveintime = driveintime;
	}
	public String getDparkid() {
		return dparkid;
	}
	public void setDparkid(String dparkid) {
		this.dparkid = dparkid;
	}
	

}
