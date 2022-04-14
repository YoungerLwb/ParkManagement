package org.park.programmer.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.park.programmer.entity.Park;
import org.park.programmer.util.DbUtil;

/**
 * 
 * @author Administrator
 * 基础Dao，封装基本操作
 */
public class BaseDao {
	private DbUtil dbUtil = new DbUtil();
	
	/**
	 * 关闭数据库连接，释放资源
	 */
	public void closeCon() {
		dbUtil.closeCon();
	}
	/**
	 * 基础查询,多条查询,全部
	 */
	public ResultSet query(String sql){
		try {
			PreparedStatement prepareStatement = dbUtil.getConnection().prepareStatement(sql);
			return prepareStatement.executeQuery();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
	}
	/**
	 * 插入查询，判断是否已经存在
	 */
	public boolean addQuery(String sql,Object[] params)
	{
		PreparedStatement pstmt;
		try {
			pstmt = dbUtil.getConnection().prepareStatement(sql);
			if(params!=null){
				for(int i=0;i<params.length;i++) {
					pstmt.setObject(i+1, params[i]);
				}
			}
			if(pstmt.executeQuery().next()) {
				return true;
			}else {
				return false;
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		
	
	}
	/**
	 * 更新操作
	 */
	public boolean update(String sql,Object[] params) {
		try {
			
			PreparedStatement pstmt = dbUtil.getConnection().prepareStatement(sql);
			if(params!=null){
				for(int i=0;i<params.length;i++) {
					pstmt.setObject(i+1, params[i]);
				}
			}
			int count = pstmt.executeUpdate();
			if(count>0)
				return true;
			else
				return false;
				
					
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return false;
	}
	
	public Connection getConnection() {
		return dbUtil.getConnection();
	}
	
}
