package org.park.programmer.entity;

import java.util.Date;

public class CarDriveOut {
	private int oid;
	private String oplatenum;
	private String oname;
	private Date odriveintime = null;
	private Date odriveouttime = null;
	private String oparkid;
	private String oparktime;
	private String ocharge;
	private String oprice;
	public int getOid() {
		return oid;
	}
	public void setOid(int oid) {
		this.oid = oid;
	}
	public String getOplatenum() {
		return oplatenum;
	}
	public void setOplatenum(String oplatenum) {
		this.oplatenum = oplatenum;
	}
	public String getOname() {
		return oname;
	}
	public void setOname(String oname) {
		this.oname = oname;
	}
	public Date getOdriveintime() {
		return odriveintime;
	}
	public void setOdriveintime(Date odriveintime) {
		this.odriveintime = odriveintime;
	}
	public Date getOdriveouttime() {
		return odriveouttime;
	}
	public void setOdriveouttime(Date odriveouttime) {
		this.odriveouttime = odriveouttime;
	}
	public String getOparkid() {
		return oparkid;
	}
	public void setOparkid(String oparkid) {
		this.oparkid = oparkid;
	}
	public String getOparktime() {
		return oparktime;
	}
	public void setOparktime(String oparktime) {
		this.oparktime = oparktime;
	}
	public String getOcharge() {
		return ocharge;
	}
	public void setOcharge(String ocharge) {
		this.ocharge = ocharge;
	}
	public String getOprice() {
		return oprice;
	}
	public void setOprice(String oprice) {
		this.oprice = oprice;
	}
	
}
