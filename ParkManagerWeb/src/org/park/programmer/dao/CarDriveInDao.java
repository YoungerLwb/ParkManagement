package org.park.programmer.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.park.programmer.entity.CarDriveIn;
import org.park.programmer.entity.CarInfo;
import org.park.programmer.entity.Page;
import org.park.programmer.entity.Park;
import org.park.programmer.util.StringUtil;

public class CarDriveInDao extends BaseDao {
	public boolean addDriveIn(CarDriveIn carDriveIn){
		
		String sql = "insert into s_cardrivein(dplatenum,dname,driveintime,dparkid) values(?,?,?,?)";
		
		Object[] params = {carDriveIn.getDplatenum(),carDriveIn.getDname(),carDriveIn.getDriveintime(),carDriveIn.getDparkid()};
			
				return update(sql,params);
		
	}
	public boolean editPark(Park park) {
		// TODO Auto-generated method stub
		String sql = "update s_park set status=? where parkid=?";
		
		Object[] params = {park.getStatus(),park.getParkid()};
		return update(sql,params);
	}
	public List<CarDriveIn> getCarDriveInList(CarDriveIn carDriveIn, Page page) {
		// TODO Auto-generated method stub
		List<CarDriveIn> ret = new ArrayList<CarDriveIn>();
		String sql = "select * from s_cardrivein ";
		if(!StringUtil.isEmpty(carDriveIn.getDplatenum())) {
			sql+="and dplatenum like '%"+carDriveIn.getDplatenum()+"%'";
		}
		if(!StringUtil.isEmpty(carDriveIn.getDname())) {
			sql+=" and dname like '%"+carDriveIn.getDname()+"%'";
		}
		sql += "limit "+page.getStart()+","+page.getPageSize();
		
		ResultSet res = query(sql.replaceFirst("and", "where"));
		
		try {
			while(res.next())
			{
				CarDriveIn cardriveIn = new CarDriveIn();
				cardriveIn.setDid(res.getInt("did"));
				cardriveIn.setDname(res.getString("dname"));
				cardriveIn.setDparkid(res.getString("dparkid"));
				cardriveIn.setDplatenum(res.getString("dplatenum"));
				cardriveIn.setDriveintime(res.getTimestamp("driveintime"));	
				ret.add(cardriveIn);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ret;
		
	}
	
	public int getCarDriveInListTotal(CarDriveIn carDriveIn) {
		int total = 0;
		String sql = "select count(*) from s_cardrivein ";
		
		if(!StringUtil.isEmpty(carDriveIn.getDplatenum())) {
			sql+="and dplatenum like '%"+carDriveIn.getDplatenum()+"%'";
		}
		if(!StringUtil.isEmpty(carDriveIn.getDname())) {
			sql+=" and dname like '%"+carDriveIn.getDname()+"%'";
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
