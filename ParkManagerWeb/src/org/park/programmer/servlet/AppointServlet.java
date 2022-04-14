package org.park.programmer.servlet;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.park.programmer.dao.AppointDao;
import org.park.programmer.dao.CarDriveInDao;
import org.park.programmer.dao.CarInfoDao;
import org.park.programmer.dao.ParkDao;
import org.park.programmer.entity.CarAppointment;
import org.park.programmer.entity.CarDriveIn;
import org.park.programmer.entity.CarUser;
import org.park.programmer.entity.Page;
import org.park.programmer.entity.Park;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class AppointServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String method = request.getParameter("method");
		if ("toAppointListView".equals(method)) {
			AppointList(request, response);
		} else if ("AddAppoint".equals(method)) {
			AddAppoint(request, response);
		}else if ("EditAppoint".equals(method)) {
			EditAppoint(request, response);
		}else if("getAppointList".equals(method)) {
			getAppointList(request, response);
		}else if("CheckAppoint".equals(method)) {
			checkAppoint(request,response);
		}else if("DeleteAppoint".equals(method)){
			DeleteAppoint(request,response);
		}
}
	private void DeleteAppoint(HttpServletRequest request, HttpServletResponse response) {
		String[] aids = request.getParameterValues("aids[]");
		String idStr = "";
		for(String aid:aids) {
			idStr += aid+",";
		}
		idStr = idStr.substring(0, idStr.length()-1);
		AppointDao appointDao = new AppointDao();
		
		if(appointDao.delAppoint(idStr)) {
			try {
				response.getWriter().write("success");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally {
				appointDao.closeCon();
			}
		}
		
	}
	private void checkAppoint(HttpServletRequest request, HttpServletResponse response) {
		Integer aid = Integer.parseInt(request.getParameter("aid"));
		Integer astatus = Integer.parseInt(request.getParameter("status"));

		CarAppointment carAppointment = new CarAppointment();	
		carAppointment.setAid(aid);
		carAppointment.setAstatus(astatus);
		AppointDao appointDao = new AppointDao();	

		if(appointDao.checkAppoint(carAppointment)) {
			try {
				response.getWriter().write("success");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally {
				appointDao.closeCon();
			}
		}
		
	}
	private void EditAppoint(HttpServletRequest request, HttpServletResponse response) {
		
		Integer aid = Integer.parseInt(request.getParameter("eaid"));
		String ausername = request.getParameter("ausername");
		String aname = request.getParameter("aname");
		String atel = request.getParameter("atel");
		String appointtime = request.getParameter("appointtime");
		Date apptime = null;
		
		try {
			apptime = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").parse(appointtime);
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		CarAppointment carAppointment = new CarAppointment();	
		carAppointment.setAid(aid);
		carAppointment.setAusername(ausername);
		carAppointment.setAname(aname);
		carAppointment.setAtel(atel);
		carAppointment.setAppointtime(apptime);
		AppointDao appointDao = new AppointDao();	

		if(appointDao.editAppoint(carAppointment)) {
			try {
				response.getWriter().write("success");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally {
				appointDao.closeCon();
			}
		}
	}
	private void getAppointList(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		String a_username = request.getParameter("a_username");
		String caraname = request.getParameter("caraname");		
		Integer pageSize = request.getParameter("rows") == null ? 99 : Integer.parseInt(request.getParameter("rows"));
		Integer currentPage = request.getParameter("page")==null ? 1 : Integer.parseInt(request.getParameter("page"));
		int userType = Integer.parseInt(request.getSession().getAttribute("userType").toString());
		
		CarAppointment carAppointment = new CarAppointment();
		carAppointment.setAusername(a_username);
		carAppointment.setAname(caraname);
		if(userType == 2) {
			CarUser currentCarUser = (CarUser)request.getSession().getAttribute("user");
			String a = currentCarUser.getUsername();
			carAppointment.setAusername(a);
		}
		AppointDao appointDao = new AppointDao();
		List<CarAppointment> appointList = appointDao.getAppointList(carAppointment, new Page(currentPage,pageSize));
		int total = appointDao.getAppointListTotal(carAppointment);
		
		appointDao.closeCon();
		response.setCharacterEncoding("UTF-8");
		//JsonConfig jsonConfig = new JsonConfig();
		//String ParkListString = JSONArray.fromObject(parkList, jsonConfig).toString();
		Map<String,Object> ret = new HashMap<String,Object>();
		ret.put("total",total);
		ret.put("rows",appointList);
		
		try {

			response.getWriter().write(JSONObject.fromObject(ret).toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private void AddAppoint(HttpServletRequest request, HttpServletResponse response) {
		String ausername = request.getParameter("ausername");
		String aname = request.getParameter("aname");
		String atel = request.getParameter("atel");
		String appointtime = request.getParameter("appointtime");
		String addtime = request.getParameter("addtime");
		Date apptime = null;
		Date atime = null;
		try {
			apptime = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").parse(appointtime);
			atime = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").parse(addtime);
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		CarAppointment carAppointment = new CarAppointment();
		
		carAppointment.setAusername(ausername);
		carAppointment.setAname(aname);
		carAppointment.setAtel(atel);
		carAppointment.setAppointtime(apptime);
		carAppointment.setAddtime(atime);
		AppointDao appointDao = new AppointDao();
		
		if(appointDao.addAppoint(carAppointment)) {
			try {
				response.getWriter().write("success");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally {
				appointDao.closeCon();
			}
		}
		
	}
	private void AppointList(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		try {
			request.getRequestDispatcher("view/Appoint.jsp").forward(request, response);
		} catch (ServletException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		doGet(request, response);
}
}
