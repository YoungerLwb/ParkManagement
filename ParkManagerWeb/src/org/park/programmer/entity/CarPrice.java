package org.park.programmer.entity;

import java.util.Date;

/**
 * 
 * @author Administrator
 * 停车价格设置类
 */
public class CarPrice {
	private int pid;
	private String price;
	private Date paddtime;
	public int getPid() {
		return pid;
	}
	public void setPid(int pid) {
		this.pid = pid;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public Date getPaddtime() {
		return paddtime;
	}
	public void setPaddtime(Date paddtime) {
		this.paddtime = paddtime;
	}

}
