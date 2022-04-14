package org.park.programmer.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 
 * @author Administrator
 *数据库连接util
 */
public class DbUtil {
	private static String url = "jdbc:mysql://localhost:3306/db_park_manager_web?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=UTC";
	private static String user = "root";
	private static String password = "123456";
	private static String jdbcName = "com.mysql.cj.jdbc.Driver";
	public static Connection connection = null;
	public static PreparedStatement pstmt = null;
	public static ResultSet rs = null;
	
	public static Connection getConnection() {
		try {
			Class.forName(jdbcName);
			connection = DriverManager.getConnection(url, user, password);
			System.out.println("数据库连接成功");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("数据库连接失败");

		}catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("数据库连接失败");

		}
		return  connection;
	}
	public static void closeCon() {
		try {
			if(connection!=null)connection.close();
			System.out.println("数据库连接已关闭");
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		DbUtil dbUtil = new DbUtil();
		dbUtil.getConnection();
	}
}
