package org.park.programmer.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.park.programmer.entity.CarPrice;
import org.park.programmer.entity.Page;
import org.park.programmer.entity.Park;
import org.park.programmer.util.StringUtil;

public class CarPriceDao extends BaseDao{

	public  boolean addCarPrice(CarPrice carPrice) {
		String sql = "insert into s_carprice(price,paddtime) values(?,?)";
		Object[] params = {carPrice.getPrice(),carPrice.getPaddtime()};
			
		return update(sql,params);
		
	}

	public  List<CarPrice> getCarPriceList(CarPrice carPrice, Page page) {
		List<CarPrice> ret = new ArrayList<CarPrice>();
		String sql = "select * from s_carprice ";
		if(!StringUtil.isEmpty(carPrice.getPrice())) {
			sql+="where price like '%"+carPrice.getPrice()+"%'";
		}
		sql += "limit "+page.getStart()+","+page.getPageSize();
		ResultSet resultSet = query(sql);
		try {
			while(resultSet.next())
			{
				CarPrice carprice = new CarPrice();
				carprice.setPid(resultSet.getInt("pid"));
				carprice.setPrice(resultSet.getString("price"));
				carprice.setPaddtime(resultSet.getTimestamp("paddtime"));
				ret.add(carprice);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ret;
	}

	public int getCarPriceListTotal(CarPrice carPrice) {
		int total = 0;
		String sql = "select count(*) from s_carprice ";
		if(!StringUtil.isEmpty(carPrice.getPrice())) {
			sql+="where price like '%"+carPrice.getPrice()+"%'";
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

	public boolean delCarPrice(Integer pid) {
		String sql = "delete from s_carprice where pid=?";
		Object[] params = {pid};
			
		return update(sql,params);
	}

	public boolean editCarPrice(CarPrice carPrice) {
		String sql = "update s_carprice set price=? where pid=?";
		
		Object[] params = {carPrice.getPrice(),carPrice.getPid()};
		return update(sql,params);
	}

}
