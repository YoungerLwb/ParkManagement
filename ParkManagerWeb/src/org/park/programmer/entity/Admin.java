package org.park.programmer.entity;
/**
 * 
 * @author Administrator
 *管理员实体类
 */
public class Admin {
	private int id;
	private String username;
	private String password;
	private int status = 1;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	
	

	
}
