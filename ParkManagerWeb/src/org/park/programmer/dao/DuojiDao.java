package org.park.programmer.dao;

import org.park.programmer.entity.CarInfo;
import org.park.programmer.entity.DuoJi;

public class DuojiDao extends BaseDao{
	public boolean editDuoji(DuoJi Duoji) {
		// TODO Auto-generated method stub
		String sql = "update s_duoji set sduoji=? where sid=1";
		Object[] params = {Duoji.getSduoji()};
		return update(sql,params);
	}
}
