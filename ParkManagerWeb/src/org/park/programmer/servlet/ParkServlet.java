package org.park.programmer.servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 班级信息管理servlet
 */
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.park.programmer.dao.ParkDao;
import org.park.programmer.entity.Page;
import org.park.programmer.entity.Park;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

public class ParkServlet extends HttpServlet {
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String method = request.getParameter("method");
		if("toParkListView".equals(method)) {//system.jsp
			parkList(request,response);
		}else if("getParkList".equals(method)) {//parkList.jsp
			getParkList(request, response);
		}else if("AddPark".equals(method)) {//parkList.jsp
			addPark(request, response);
		}else if("DeletePark".equals(method)) {
			delPark(request, response);
		}else if("EditPark".equals(method)) {
			editPark(request, response);
		}
	}


	private void editPark(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		Integer id = Integer.parseInt(request.getParameter("id"));
		String parkid = request.getParameter("parkid");
		String area = request.getParameter("area");
		String status = request.getParameter("status");
		Park park = new Park();
		park.setParkid(parkid);
		park.setArea(area);
		park.setStatus(status);
		park.setId(id);
		ParkDao parkDao = new ParkDao();
		if(parkDao.editPark(park)) {
			try {
				response.getWriter().write("success");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally {
				parkDao.closeCon();
			}
		}
	}


	private void delPark(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		Integer id = Integer.parseInt(request.getParameter("parkid"));
		ParkDao parkDao = new ParkDao();
		if(parkDao.delPark(id)) {
			try {
				response.getWriter().write("success");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally {
				parkDao.closeCon();
			}
		}
	}


	private void addPark(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		String parkid = request.getParameter("parkid");
		String area = request.getParameter("area");
		String status = request.getParameter("status");
		Park park = new Park();
		park.setParkid(parkid);
		park.setArea(area);
		park.setStatus(status);
		ParkDao parkDao = new ParkDao();
		
		if(parkDao.addPark(park)) {
			try {
				response.getWriter().write("success");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally {
				parkDao.closeCon();
			}
		}
		
	}


	private void parkList(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		try {
			request.getRequestDispatcher("view/parkList.jsp").forward(request, response);
		} catch (ServletException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private void getParkList(HttpServletRequest request, HttpServletResponse response) {
		String parkid = request.getParameter("parkId");
	
		Integer pageSize = request.getParameter("rows") == null ? 99 : Integer.parseInt(request.getParameter("rows"));
		Integer currentPage = request.getParameter("page")==null ? 1 : Integer.parseInt(request.getParameter("page"));
		Park park = new Park();
		park.setParkid(parkid);
		ParkDao parkDao = new ParkDao();
		List<Park> parkList = parkDao.getParkList(park, new Page(currentPage,pageSize));
		int total = parkDao.getParkListTotal(park);
		
		parkDao.closeCon();
		response.setCharacterEncoding("UTF-8");
		//JsonConfig jsonConfig = new JsonConfig();
		//String ParkListString = JSONArray.fromObject(parkList, jsonConfig).toString();
		Map<String,Object> ret = new HashMap<String,Object>();
		ret.put("total",total);
		ret.put("rows",parkList);
		
		try {
			String fromString = request.getParameter("from");
			if("combox".equals(fromString)) {
				response.getWriter().write(JSONArray.fromObject(parkList).toString());
			}else {
			response.getWriter().write(JSONObject.fromObject(ret).toString());
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
}
