package org.park.programmer.entity;

import java.io.InputStream;

/**
 * 
 * @author Administrator
 *	车辆信息实体类
 */
public class CarInfo {
	private int cid;
	private String cplatenum;
	private String cname;
	private InputStream cphoto;
	private String cbrand;
	public int getCid() {
		return cid;
	}
	public void setCid(int cid) {
		this.cid = cid;
	}
	public String getCplatenum() {
		return cplatenum;
	}
	public void setCplatenum(String cplatenum) {
		this.cplatenum = cplatenum;
	}
	public String getCname() {
		return cname;
	}
	public void setCname(String cname) {
		this.cname = cname;
	}
	public InputStream getCphoto() {
		return cphoto;
	}
	public void setCphoto(InputStream cphoto) {
		this.cphoto = cphoto;
	}
	public String getCbrand() {
		return cbrand;
	}
	public void setCbrand(String cbrand) {
		this.cbrand = cbrand;
	}
}
