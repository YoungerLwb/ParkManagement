package org.park.programmer.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.park.programmer.entity.CarInfo;
import org.park.programmer.entity.CarUser;
import org.park.programmer.entity.Page;
import org.park.programmer.util.StringUtil;

public class CarInfoDao extends BaseDao{
	public boolean addCarInfo(CarInfo carInfo){
		
		String sql = "insert into s_carinfo(cplatenum,cname,cphoto,cbrand) values(?,?,?,?)";
		Object[] params = {carInfo.getCplatenum(),carInfo.getCname(),carInfo.getCphoto(),carInfo.getCbrand()};					
			return update(sql,params);
	}

	public List<CarInfo> getCarInfoList(CarInfo carInfo, Page page) {
		// TODO Auto-generated method stub
		
		List<CarInfo> ret = new ArrayList<CarInfo>();
		String sql = "select * from s_carinfo ";
		if(!StringUtil.isEmpty(carInfo.getCplatenum())) {
			sql+="and cplatenum like '%"+carInfo.getCplatenum()+"%'";
		}
		if(!StringUtil.isEmpty(carInfo.getCname())) {
			sql+=" and cname like '%"+carInfo.getCname()+"%'";
		}
		sql += "limit "+page.getStart()+","+page.getPageSize();
		
		ResultSet res = query(sql.replaceFirst("and", "where"));
		
		try {
			while(res.next())
			{
				CarInfo carinfo = new CarInfo();
				carinfo.setCid(res.getInt("cid"));
				carinfo.setCplatenum(res.getString("cplatenum"));;
				carinfo.setCname(res.getString("cname"));
				carinfo.setCphoto(res.getBinaryStream("cphoto"));
				carinfo.setCbrand(res.getString("cbrand"));
				ret.add(carinfo);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ret;
	}

	public int getCarInfoListTotal(CarInfo carInfo) {
		// TODO Auto-generated method stub
		int total = 0;
		String sql = "select count(*) as total from s_carinfo ";
		
		if(!StringUtil.isEmpty(carInfo.getCplatenum())) {
			sql+="and cplatenum like '%"+carInfo.getCplatenum()+"%'";
		}
		if(!StringUtil.isEmpty(carInfo.getCname())) {
			sql+=" and cname like '%"+carInfo.getCname()+"%'";
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
	public CarInfo getCarInfoPhoto(int cid) {
		String sql = "select * from s_carinfo where cid="+cid;
		CarInfo carInfo = null;
		ResultSet res = query(sql);
		try {
			if(res.next()) {
				 carInfo= new CarInfo();
				 carInfo.setCid(res.getInt("cid"));
					carInfo.setCplatenum(res.getString("cplatenum"));;
					carInfo.setCname(res.getString("cname"));
					carInfo.setCphoto(res.getBinaryStream("cphoto"));
					carInfo.setCbrand(res.getString("cbrand"));
					return carInfo;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return carInfo;
	}

	public boolean editCarInfo(CarInfo carInfo) {
		// TODO Auto-generated method stub
		String sql = "update s_carinfo set cplatenum=?,cname=?,cbrand=? where cid=?";
		Object[] params = {carInfo.getCplatenum(),carInfo.getCname(),carInfo.getCbrand(),carInfo.getCid()};
		return update(sql,params);
	}

	public boolean setCarInfoPhoto(CarInfo carInfo) {
		// TODO Auto-generated method stub
		String sql = "update s_carinfo set cphoto=? where cid=?";
		Object[] params = {carInfo.getCphoto(),carInfo.getCid()};
		return update(sql,params);
		
	}



	public boolean delCarInfo(String cids) {
		String sql = "delete from s_carinfo where cid in ("+cids+")";
		
		return update(sql,null);
		
	}
}
