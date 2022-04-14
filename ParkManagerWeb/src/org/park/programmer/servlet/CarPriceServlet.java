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

import org.park.programmer.dao.CarPriceDao;
import org.park.programmer.dao.ParkDao;
import org.park.programmer.entity.CarPrice;
import org.park.programmer.entity.Page;
import org.park.programmer.entity.Park;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class CarPriceServlet extends HttpServlet {
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String method = request.getParameter("method");
		if("toCarPriceListView".equals(method)) {//system.jsp
			CarPriceListView(request,response);
		}else if("getCarPriceList".equals(method)) {//parkList.jsp
			getCarPriceList(request, response);
		}else if("AddCarPrice".equals(method)) {//parkList.jsp
			addCarPrice(request, response);
		}else if("DeleteCarPrice".equals(method)) {
			delCarPrice(request, response);
		}else if("EditCarPrice".equals(method)) {
			editCarPrice(request, response);
		}
	}


	private void editCarPrice(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		Integer pid = Integer.parseInt(request.getParameter("pid"));
		String price = request.getParameter("price");

		CarPrice carPrice = new CarPrice();
		carPrice.setPrice(price);
		carPrice.setPid(pid);
		CarPriceDao carPriceDao = new CarPriceDao();
		if(carPriceDao.editCarPrice(carPrice)) {
			try {
				response.getWriter().write("success");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally {
				carPriceDao.closeCon();
			}
		}
	}


	private void delCarPrice(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		Integer pid = Integer.parseInt(request.getParameter("pid"));
		CarPriceDao carPriceDao = new CarPriceDao();
		if(carPriceDao.delCarPrice(pid)) {
			try {
				response.getWriter().write("success");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally {
				carPriceDao.closeCon();
			}
		}
	}


	private void addCarPrice(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		String addtime = request.getParameter("addtime");
		Date paddtime = null;
		try {
			paddtime = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").parse(addtime);
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String price = request.getParameter("price");
		
		CarPrice carPrice = new CarPrice();
		carPrice.setPaddtime(paddtime);
		carPrice.setPrice(price);

		CarPriceDao carPriceDao = new CarPriceDao();
		
		if(carPriceDao.addCarPrice(carPrice)) {
			try {
				response.getWriter().write("success");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally {
				carPriceDao.closeCon();
			}
		}
		
	}


	private void CarPriceListView(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		try {
			request.getRequestDispatcher("view/CarPriceList.jsp").forward(request, response);
		} catch (ServletException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private void getCarPriceList(HttpServletRequest request, HttpServletResponse response) {
		String price = request.getParameter("price");
	
		Integer pageSize = request.getParameter("rows") == null ? 99 : Integer.parseInt(request.getParameter("rows"));
		Integer currentPage = request.getParameter("page")==null ? 1 : Integer.parseInt(request.getParameter("page"));
		CarPrice carPrice = new CarPrice();
		carPrice.setPrice(price);
		
		CarPriceDao carPriceDao = new CarPriceDao();
		List<CarPrice> carPriceList = carPriceDao.getCarPriceList(carPrice, new Page(currentPage,pageSize));
		int total = carPriceDao.getCarPriceListTotal(carPrice);
		
		carPriceDao.closeCon();
		response.setCharacterEncoding("UTF-8");
		//JsonConfig jsonConfig = new JsonConfig();
		//String ParkListString = JSONArray.fromObject(parkList, jsonConfig).toString();
		Map<String,Object> ret = new HashMap<String,Object>();
		ret.put("total",total);
		ret.put("rows",carPriceList);
		
		try {
			String fromString = request.getParameter("from");
			if("combox".equals(fromString)) {
				response.getWriter().write(JSONArray.fromObject(carPriceList).toString());
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
