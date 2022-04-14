package org.park.programmer.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.park.programmer.entity.Admin;
import org.park.programmer.entity.Page;
import org.park.programmer.entity.Park;
import org.park.programmer.util.StringUtil;

/**
 * 
 * @author Administrator
 *管理员数据库操作封装
 */
public class AdminDao extends BaseDao{
	public Admin login(String name,String password) {
		String sql = "select * from s_admin where username= '"+ name +"' and password= '"+password+"'";
		ResultSet resultSet = query(sql);
		try {
			if(resultSet.next()) {
				Admin admin = new Admin();
				admin.setId(resultSet.getInt("id"));
				admin.setUsername(resultSet.getString("username"));
				admin.setPassword(resultSet.getString("password"));
				admin.setStatus(resultSet.getInt("status"));
				return admin;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}
		return null;
		
	}
	public boolean editPassword(Admin admin,String newPassword) {
		// TODO Auto-generated method stub
		String sql = "update s_admin set password=? where id=?";
		
		Object[] params = {newPassword,admin.getId()};
		return update(sql,params);
	}
	public boolean addAdmin(Admin admin) {
		String sql = "insert into s_admin(username,password,status) values(?,?,?)";
		Object[] params = {admin.getUsername(),admin.getPassword(),admin.getStatus()};
			
				return update(sql,params);
		
	}
	public List<Admin> getAdminList(Admin admin, Page page) {
		List<Admin> ret = new ArrayList<Admin>();
		String sql = "select * from s_admin ";
		if(!StringUtil.isEmpty(admin.getUsername())) {
			sql+="where username like '%"+admin.getUsername()+"%'";
		}
		sql += "limit "+page.getStart()+","+page.getPageSize();
		ResultSet resultSet = query(sql);
		try {
			while(resultSet.next())
			{
				Admin ad = new Admin();
				ad.setId(resultSet.getInt("id"));
				ad.setStatus(resultSet.getInt("status"));
				ad.setUsername(resultSet.getString("username"));
				
				ret.add(ad);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ret;
	}
	public int getAdminListTotal(Admin admin) {
		int total = 0;
		String sql = "select count(*) as total from s_admin ";
		if(!StringUtil.isEmpty(admin.getUsername())) {
			sql+="where username like '%"+admin.getUsername()+"%'";
		}
			ResultSet resultSet = query(sql);
		try {
			while(resultSet.next())
			{
				total = resultSet.getInt(1);
			}	
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return total;
	}

}
