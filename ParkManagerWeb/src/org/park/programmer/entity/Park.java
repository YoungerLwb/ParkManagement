package org.park.programmer.entity;
/**
 * 
 * @author Administrator
 *车位实体表
 */
public class Park {
	private int id;
	private String parkid;
	private String area;
	private String status;
	public int getId() {
		return id;
	}
	public String getParkid() {
		return parkid;
	}
	public void setParkid(String parkid) {
		this.parkid = parkid;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
}
