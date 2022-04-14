package org.park.programmer.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.park.programmer.entity.Page;
import org.park.programmer.entity.Park;
import org.park.programmer.util.StringUtil;

/**
 * 
 * @author Administrator
 *车位信息数据库操作
 */
public class ParkDao extends BaseDao{
	public List<Park> getParkList(Park park,Page page){
		List<Park> ret = new ArrayList<Park>();
		String sql = "select * from s_park ";
		if(!StringUtil.isEmpty(park.getParkid())) {
			sql+="where parkid like '%"+park.getParkid()+"%'";
		}
		sql += "limit "+page.getStart()+","+page.getPageSize();
		ResultSet resultSet = query(sql);
		try {
			while(resultSet.next())
			{
				Park pa = new Park();
				pa.setId(resultSet.getInt("id"));
				pa.setParkid(resultSet.getString("parkid"));
				pa.setArea(resultSet.getString("area"));
				pa.setStatus(resultSet.getString("status"));
				ret.add(pa);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ret;
	}
	public int getParkListTotal(Park park){
		int total = 0;
		String sql = "select count(*) as total from s_park ";
		if(!StringUtil.isEmpty(park.getParkid())) {
			sql+="where parkid like '%"+park.getParkid()+"%'";
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
	public boolean addPark(Park park){
	
		String sql = "insert into s_park(parkid,area,status) values(?,?,?)";
		String parkid = park.getArea().subSequence(0, 1)+park.getParkid();
		Object[] params = {parkid,park.getArea(),park.getStatus()};
			if(AddQuery(parkid)) {
				return false;
			}else {
				return update(sql,params);
		}
	}
	private boolean AddQuery(String parkid) {
		// TODO Auto-generated method stub
		String sql = "select * from s_park where parkid=?";
		Object[] params = {parkid};
		return addQuery(sql,params);
		
	}
	public boolean delPark(int id){
		
		String sql = "delete from s_park where id=?";
		Object[] params = {id};
			
		return update(sql,params);
	}
	public boolean editPark(Park park) {
		// TODO Auto-generated method stub
		String sql = "update s_park set parkid=?,area=?,status=? where id=?";
		String parkid = park.getArea().subSequence(0, 1)+park.getParkid();
		Object[] params = {parkid,park.getArea(),park.getStatus(),park.getId()};
		return update(sql,params);
	}
}
