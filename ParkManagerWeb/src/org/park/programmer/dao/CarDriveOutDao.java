package org.park.programmer.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.park.programmer.entity.CarDriveIn;
import org.park.programmer.entity.CarDriveOut;
import org.park.programmer.entity.CarUser;
import org.park.programmer.entity.Page;
import org.park.programmer.entity.Park;
import org.park.programmer.util.StringUtil;

public class CarDriveOutDao extends BaseDao {
	public boolean editPark(Park park) {
		// TODO Auto-generated method stub
		String sql = "update s_park set status=? where parkid=?";
		
		Object[] params = {park.getStatus(),park.getParkid()};
		return update(sql,params);
	}

	public boolean addDriveOut(CarDriveOut carDriveOut) {
		String sql = "insert into s_cardriveout(oplatenum,oname,oparkid,odriveintime,odriveouttime,oparktime,ocharge,oprice) values(?,?,?,?,?,?,?,?)";
		
		Object[] params = {carDriveOut.getOplatenum(),carDriveOut.getOname(),carDriveOut.getOparkid(),carDriveOut.getOdriveintime(),carDriveOut.getOdriveouttime(),carDriveOut.getOparktime(),carDriveOut.getOcharge(),carDriveOut.getOprice()};
			
				return update(sql,params);
	}
	/*
	 *   更新车主卡剩余余额
	 */
	public boolean editCarUser(CarUser carUser) {
		
		String sql = "update s_caruser set ubalance=? where uname=?";
		
		Object[] params = {carUser.getUbalance(),carUser.getUname()};
		return update(sql,params);
	}
	/*
	 *  清除入场车辆
	 */
	public boolean delDriveIn(CarDriveIn carDriveIn) {
		// TODO Auto-generated method stub
		String sql = "delete from s_cardrivein where dplatenum=?";
		Object[] params = {carDriveIn.getDplatenum()};
		return update(sql,params);
	}
/**
 * 获取某一人的所有停车记录
 * @param carDriveOut
 * @return
 */
	public List<CarDriveOut> getCarDriveOutList1(CarDriveOut carDriveOut) {
		// TODO Auto-generated method stub
		List<CarDriveOut> ret = new ArrayList<CarDriveOut>();
		String sql = "select * from s_cardriveout ";
		if(!StringUtil.isEmpty(carDriveOut.getOplatenum())) {
			sql+="and oplatenum like '%"+carDriveOut.getOplatenum()+"%'";
		}
		if(!StringUtil.isEmpty(carDriveOut.getOname())) {
			sql+=" and oname like '%"+carDriveOut.getOname()+"%'";
		}
		
		ResultSet res = query(sql.replaceFirst("and", "where"));
		
		try {
			while(res.next())
			{
				CarDriveOut cardriveOut = new CarDriveOut();
				cardriveOut.setOid(res.getInt("oid"));
				cardriveOut.setOplatenum(res.getString("oplatenum"));
				cardriveOut.setOname(res.getString("oname"));
				cardriveOut.setOparkid(res.getString("oparkid"));
				cardriveOut.setOdriveintime(res.getTimestamp("odriveintime"));
				cardriveOut.setOdriveouttime(res.getTimestamp("odriveouttime"));
				cardriveOut.setOparktime(res.getString("oparktime"));
				cardriveOut.setOcharge(res.getString("ocharge"));
				cardriveOut.setOprice(res.getString("oprice"));	
				ret.add(cardriveOut);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ret;
		
	}
	
	
	public List<CarDriveOut> getCarDriveOutList(CarDriveOut carDriveOut, Page page) {
		// TODO Auto-generated method stub
		List<CarDriveOut> ret = new ArrayList<CarDriveOut>();
		String sql = "select * from s_cardriveout ";
		if(!StringUtil.isEmpty(carDriveOut.getOplatenum())) {
			sql+="and oplatenum like '%"+carDriveOut.getOplatenum()+"%'";
		}
		if(!StringUtil.isEmpty(carDriveOut.getOname())) {
			sql+=" and oname like '%"+carDriveOut.getOname()+"%'";
		}
		sql += "limit "+page.getStart()+","+page.getPageSize();
		
		ResultSet res = query(sql.replaceFirst("and", "where"));
		
		try {
			while(res.next())
			{
				CarDriveOut cardriveOut = new CarDriveOut();
				cardriveOut.setOid(res.getInt("oid"));
				cardriveOut.setOplatenum(res.getString("oplatenum"));
				cardriveOut.setOname(res.getString("oname"));
				cardriveOut.setOparkid(res.getString("oparkid"));
				cardriveOut.setOdriveintime(res.getTimestamp("odriveintime"));
				cardriveOut.setOdriveouttime(res.getTimestamp("odriveouttime"));
				cardriveOut.setOparktime(res.getString("oparktime"));
				cardriveOut.setOcharge(res.getString("ocharge"));
				cardriveOut.setOprice(res.getString("oprice"));	
				ret.add(cardriveOut);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ret;
		
	}

	public int getCarDriveOutListTotal(CarDriveOut carDriveOut) {
		int total = 0;
		String sql = "select count(*) from s_cardriveout ";
		
		if(!StringUtil.isEmpty(carDriveOut.getOplatenum())) {
			sql+="and oplatenum like '%"+carDriveOut.getOplatenum()+"%'";
		}
		if(!StringUtil.isEmpty(carDriveOut.getOname())) {
			sql+=" and oname like '%"+carDriveOut.getOname()+"%'";
		}
		
		ResultSet res = query(sql.replaceFirst("and", "where"));
		try {
			while(res.next())
			{
				total = res.getInt(1);
			}	
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return total;
	}
}
