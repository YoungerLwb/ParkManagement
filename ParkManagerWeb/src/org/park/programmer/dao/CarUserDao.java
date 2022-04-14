package org.park.programmer.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.park.programmer.entity.Admin;
import org.park.programmer.entity.CarUser;
import org.park.programmer.entity.Page;
import org.park.programmer.util.StringUtil;

public class CarUserDao extends BaseDao {
	
	public CarUser login(String name,String password) {
		String sql = "select * from s_caruser where username= '"+ name +"' and upwd= '"+password+"'";
		ResultSet resultSet = query(sql);
		try {
			if(resultSet.next()) {
				CarUser carUser = new CarUser();
				carUser.setUid(resultSet.getInt("uid"));
				carUser.setUsername(resultSet.getString("username"));
				carUser.setUpwd(resultSet.getString("upwd"));
				carUser.setUbalance(resultSet.getString("ubalance"));
				carUser.setUicd(resultSet.getString("uicd"));
				carUser.setUname(resultSet.getString("uname"));
				carUser.setUtel(resultSet.getString("utel"));
				carUser.setUsex(resultSet.getString("usex"));
				carUser.setUphoto(resultSet.getBinaryStream("uphoto"));
				return carUser;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}
		return null;
		
	}
	
	public boolean editCarUser(CarUser carUser) {
		// TODO Auto-generated method stub
		String sql = "update s_caruser set username=?,upwd=?,uicd=?,uname=?,usex=?,utel=? where uid=?";
		Object[] params = {carUser.getUsername(),carUser.getUpwd(),carUser.getUicd(),carUser.getUname(),carUser.getUsex(),carUser.getUtel(),carUser.getUid()};
		return update(sql,params);
	}
	public boolean addBalanceCarUser(CarUser carUser) {
		// TODO Auto-generated method stub
		String sql = "update s_caruser set ubalance=? where uid=?";
		Object[] params = {carUser.getUbalance(),carUser.getUid()};
		return update(sql,params);
	}
	
	public boolean delCarUser(String uids) {
		// TODO Auto-generated method stub
		String sql = "delete from s_caruser where uid in ("+uids+")";
		
		return update(sql,null);
	}
	
	public boolean setCarUserPhoto(CarUser carUser) {
		// TODO Auto-generated method stub
		String sql = "update s_caruser set uphoto=? where uid=?";
//		Connection connection = getConnection();
//		try {
//			PreparedStatement pstmt = connection.prepareStatement(sql);
//			pstmt.setBinaryStream(1, carUser.getUphoto());
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		Object[] params = {carUser.getUphoto(),carUser.getUid()};
		return update(sql,params);
	}
	
	public boolean addCarUser(CarUser carUser){
		
		String sql = "insert into s_caruser(username,upwd,uicd,uname,usex,utel,ubalance,uphoto) values(?,?,?,?,?,?,?,?)";
		Object[] params = {carUser.getUsername(),carUser.getUpwd(),carUser.getUicd(),carUser.getUname(),carUser.getUsex(),carUser.getUtel(),carUser.getUbalance(),carUser.getUphoto()};					
			return update(sql,params);
	}
	// getCarUserPhoto
	public CarUser getCarUser(int uid) {
		String sql = "select * from s_caruser where uid = "+uid;
		CarUser carUser = null;
		ResultSet res = query(sql);
		try {
			if(res.next()) {
				carUser = new CarUser();
				carUser.setUid(res.getInt("uid"));
				carUser.setUsername(res.getString("username"));
				carUser.setUpwd(res.getString("upwd"));
				carUser.setUicd(res.getString("uicd"));
				carUser.setUname(res.getString("uname"));
				carUser.setUsex(res.getString("usex"));
				carUser.setUtel(res.getString("utel"));
				carUser.setUbalance(res.getString("ubalance"));
				carUser.setUphoto(res.getBinaryStream("uphoto"));
				return carUser;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return carUser;
	}
	public List<CarUser> getCarUserList(CarUser carUser,Page page){
		List<CarUser> ret = new ArrayList<CarUser>();
		String sql = "select * from s_caruser ";
		if(!StringUtil.isEmpty(carUser.getUsername())) {
			sql+="and username like '%"+carUser.getUsername()+"%'";
		}
		if(carUser.getUid() != 0) {
			sql+=" and uid="+carUser.getUid()+" ";
		}
		sql += "limit "+page.getStart()+","+page.getPageSize();
		ResultSet res = query(sql.replaceFirst("and", "where"));
		try {
			while(res.next())
			{
				CarUser caruser = new CarUser();
				caruser.setUid(res.getInt("uid"));
				caruser.setUsername(res.getString("username"));
				caruser.setUpwd(res.getString("upwd"));
				caruser.setUicd(res.getString("uicd"));
				caruser.setUname(res.getString("uname"));
				caruser.setUsex(res.getString("usex"));
				caruser.setUtel(res.getString("utel"));
				caruser.setUbalance(res.getString("ubalance"));
				caruser.setUphoto(res.getBinaryStream("uphoto"));
				ret.add(caruser);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ret;
	}
	public int getCarUserListTotal(CarUser carUser){
		int total = 0;
		String sql = "select count(*) as total from s_caruser ";
		if(!StringUtil.isEmpty(carUser.getUsername())) {
			sql+="and username like '%"+carUser.getUsername()+"%'";
		}
		if(carUser.getUid() != 0) {
			sql+=" and uid="+carUser.getUid();
		}
			ResultSet resultSet = query(sql.replaceFirst("and", "where"));
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
	public String getBalance(CarUser carUser) {
		// TODO Auto-generated method stub
		String sql = "select ubalance from s_caruser where uname='"+carUser.getUname()+"'";
		ResultSet resultSet = query(sql);
		String balance = null;
		try {
			if(resultSet.next()) {
				balance = resultSet.getString("ubalance");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return balance;
	}

	public boolean editPassword(CarUser carUser, String new_password) {
		String sql = "update s_caruser set upwd=? where uid=?";
		
		Object[] params = {new_password,carUser.getUid()};
		return update(sql,params);
	}
}
