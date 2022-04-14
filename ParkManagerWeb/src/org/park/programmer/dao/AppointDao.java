package org.park.programmer.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.park.programmer.entity.CarAppointment;
import org.park.programmer.entity.CarInfo;
import org.park.programmer.entity.Page;
import org.park.programmer.util.StringUtil;

public class AppointDao extends BaseDao {

	public boolean addAppoint(CarAppointment carAppointment) {
		
		String sql = "insert into s_carappointment(ausername,aname,atel,appointtime,astatus,addtime) values(?,?,?,?,?,?)";
		
		Object[] params = {carAppointment.getAusername(),carAppointment.getAname(),carAppointment.getAtel(),carAppointment.getAppointtime(),carAppointment.APPOINT_STATUS_WAIT,carAppointment.getAddtime()};
			
				return update(sql,params);
	}

	public List<CarAppointment> getAppointList(CarAppointment carAppointment, Page page) {
		List<CarAppointment> ret = new ArrayList<CarAppointment>();
		String sql = "select * from s_carappointment ";
		if(!StringUtil.isEmpty(carAppointment.getAusername())) {
			sql+="and ausername like '%"+carAppointment.getAusername()+"%'";
		}
		if(!StringUtil.isEmpty(carAppointment.getAname())) {
			sql+=" and aname like '%"+carAppointment.getAname()+"%'";
		}
		sql += "limit "+page.getStart()+","+page.getPageSize();
		
		ResultSet res = query(sql.replaceFirst("and", "where"));
		
		try {
			while(res.next())
			{
				CarAppointment cam = new CarAppointment();
				cam.setAid(res.getInt("aid"));
				cam.setAusername(res.getString("ausername"));
				cam.setAname(res.getString("aname"));
				cam.setAtel(res.getString("atel"));
				cam.setAppointtime(res.getTimestamp("appointtime"));
				cam.setAstatus(res.getInt("astatus"));
				cam.setAddtime(res.getTimestamp("addtime"));
				ret.add(cam);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ret;
		
	}

	public int getAppointListTotal(CarAppointment carAppointment) {
		int total = 0;
		
		String sql = "select count(*) from s_carappointment ";
		if(!StringUtil.isEmpty(carAppointment.getAusername())) {
			sql+="and ausername like '%"+carAppointment.getAusername()+"%'";
		}
		if(!StringUtil.isEmpty(carAppointment.getAname())) {
			sql+=" and aname like '%"+carAppointment.getAname()+"%'";
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

	public boolean editAppoint(CarAppointment carAppointment) {
		String sql = "update s_carappointment set ausername=?,aname=?,atel=?,appointtime=?,astatus=? where aid=?";
		Object[] params = {carAppointment.getAusername(),carAppointment.getAname(),carAppointment.getAtel(),carAppointment.getAppointtime(),carAppointment.APPOINT_STATUS_WAIT,carAppointment.getAid()};
		return update(sql,params);
	}

	public boolean checkAppoint(CarAppointment carAppointment) {
		String sql = "update s_carappointment set astatus=? where aid=?";
		Object[] params = {carAppointment.getAstatus(),carAppointment.getAid()};
		return update(sql,params);
	}

	public boolean delAppoint(String aids) {
		String sql = "delete from s_carappointment where aid in ("+aids+")";
		
		return update(sql,null);
	}
	
}
