package org.park.programmer.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadBase;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.park.programmer.dao.CarDriveInDao;
import org.park.programmer.dao.CarDriveOutDao;
import org.park.programmer.dao.CarInfoDao;
import org.park.programmer.dao.DuojiDao;
import org.park.programmer.entity.CarDriveIn;
import org.park.programmer.entity.CarInfo;
import org.park.programmer.entity.CarUser;
import org.park.programmer.entity.DuoJi;
import org.park.programmer.entity.Page;
import org.park.programmer.entity.Park;

import net.sf.json.JSONObject;
import org.park.programmer.entity.CarDriveOut;
public class CarDriveInAOutServlet extends HttpServlet {
	 
		private static final long serialVersionUID = 1L;
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String method = request.getParameter("method");
		if ("toCarDriveIn".equals(method)) {
			CarDriveIn(request, response);
		} else if ("AddCarDriveIn".equals(method)) {
			addCarDriveIn(request, response);
		}else if ("toCarDriveInListView".equals(method)) {
			carDriveInListView(request, response);
		}else if("getCarDriveInList".equals(method)) {
			getCarDriveInList(request, response);
		}else if("toCarDriveOut".equals(method)) {
			CarDriveOut(request,response);
		}else if("AddCarDriveOut".equals(method)){
			addCarDriveOut(request,response);
		}else if("toCarDriveOutListView".equals(method)) {
			CarDriveOutListView(request,response);
		}else if("getCarDriveOutList".equals(method)) {
			getCarDriveOutList(request,response);
		}else if("ExportCarDriveOutList".equals(method)) {
			exportCarDriveOutList(request,response);
		}
}
		private void exportCarDriveOutList(HttpServletRequest request, HttpServletResponse response) {
			String cplatenum = request.getParameter("carPlatenum");
			String carcname = request.getParameter("carcname");
			int userType = Integer.parseInt(request.getSession().getAttribute("userType").toString());

			CarDriveOut carDriveOut = new CarDriveOut();
			carDriveOut.setOplatenum(cplatenum);
			carDriveOut.setOname(carcname);
			if(userType == 2) {
				//如果是车主用户，只能查看自己的信息
				CarUser currentCarUser = (CarUser)request.getSession().getAttribute("user");
				carDriveOut.setOname(currentCarUser.getUname());
			}
			try {
				response.setHeader("Content-Disposition", "attachment;filename="+URLEncoder.encode("CarDriveOutList.xls", "UTF-8"));
				response.setHeader("Connection", "close");
				response.setHeader("Content-Type", "application/octet-stream");
			ServletOutputStream outputStream = response.getOutputStream(); 
			//实现将数据装入到Excel
			CarDriveOutDao carDriveOutDao = new CarDriveOutDao();
			List<CarDriveOut> carDriveOutList = carDriveOutDao.getCarDriveOutList1(carDriveOut);
			carDriveOutDao.closeCon();
			HSSFWorkbook hssfWorkbook = new HSSFWorkbook();
			HSSFSheet createSheet = hssfWorkbook.createSheet("停车记录表");
			HSSFRow createRow = createSheet.createRow(0);
			createRow.createCell(0).setCellValue("停车列表序号");
			createRow.createCell(1).setCellValue("车牌号");
			createRow.createCell(2).setCellValue("车主名");
			createRow.createCell(3).setCellValue("车位号");
			createRow.createCell(4).setCellValue("驶入时间");
			createRow.createCell(5).setCellValue("驶出时间");
			createRow.createCell(6).setCellValue("停车时长");
			createRow.createCell(7).setCellValue("停车费用");
			createRow.createCell(8).setCellValue("停车单价");
			//outputStream.write("hello world".getBytes());
			int row = 1;
			for(CarDriveOut entry:carDriveOutList) {
				createRow = createSheet.createRow(row++);
				createRow.createCell(0).setCellValue(entry.getOid());
				createRow.createCell(1).setCellValue(entry.getOplatenum());
				createRow.createCell(2).setCellValue(entry.getOname());
				createRow.createCell(3).setCellValue(entry.getOparkid());
				createRow.createCell(4).setCellValue(entry.getOdriveintime().toString());
				createRow.createCell(5).setCellValue(entry.getOdriveouttime().toString());
				createRow.createCell(6).setCellValue(entry.getOparktime());
				createRow.createCell(7).setCellValue(entry.getOcharge());
				createRow.createCell(8).setCellValue(entry.getOprice());
			}
			hssfWorkbook.write(outputStream);
			outputStream.flush();
			outputStream.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	
	}
		private void getCarDriveOutList(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
			String cplatenum = request.getParameter("carPlatenum");
			String carcname = request.getParameter("carcname");
			Integer currentPage = request.getParameter("page") == null?1 : Integer.parseInt(request.getParameter("page"));
			Integer pageSize = request.getParameter("rows") == null?999 : Integer.parseInt(request.getParameter("rows"));
			int userType = Integer.parseInt(request.getSession().getAttribute("userType").toString());

			CarDriveOut carDriveOut = new CarDriveOut();
			carDriveOut.setOname(carcname);
			carDriveOut.setOplatenum(cplatenum);
			if(userType == 2) {
				CarUser currentCarUser = (CarUser)request.getSession().getAttribute("user");
				carDriveOut.setOname(currentCarUser.getUname());
			}
			CarDriveOutDao carDriveOutDao = new CarDriveOutDao();
			List<CarDriveOut> carDriveOutList = carDriveOutDao.getCarDriveOutList(carDriveOut, new Page(currentPage,pageSize));
			int total = carDriveOutDao.getCarDriveOutListTotal(carDriveOut);
			carDriveOutDao.closeCon();
			response.setCharacterEncoding("UTF-8");
			//JsonConfig jsonConfig = new JsonConfig();
			//String ParkListString = JSONArray.fromObject(parkList, jsonConfig).toString();
			Map<String,Object> ret = new HashMap<String,Object>();
			ret.put("total",total);
			ret.put("rows",carDriveOutList);
			
			try {
				response.getWriter().write(JSONObject.fromObject(ret).toString());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
		private void CarDriveOutListView(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
			try {
				request.getRequestDispatcher("view/CarDriveOutList.jsp").forward(request, response);
			} catch (ServletException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
	}
		private void addCarDriveOut(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
			String odrivein = request.getParameter("odriveintime");
			String odriveout = request.getParameter("odriveouttime");
			Date odriveintime = null;
			Date odriveouttime = null;
			try {
				odriveintime = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").parse(odrivein);
				odriveouttime = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").parse(odriveout);
			} catch (ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			//int obalance = 0;
			float obalance = 0;
			if(request.getParameter("obalance")!=null) {
				 obalance = Float.parseFloat(request.getParameter("obalance"));
			}
			
			String oparkid = request.getParameter("oparkid");
			String oname = request.getParameter("oname");
			String oplatenum = request.getParameter("oplatenum");
			String oparktime= request.getParameter("oparktime");
			String charge = request.getParameter("ocharge");
			String oprice = request.getParameter("oprice");
			//int ocharge = Integer.parseInt(charge);
			float ocharge = Float.parseFloat(charge);
			if(obalance < ocharge) {
				try {
					response.getWriter().write("nomoney");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return ;
			}
			
			
			
			//String oprice = request.getParameter("oprice");
			CarDriveOut carDriveOut = new CarDriveOut();
			carDriveOut.setOcharge(Float.toString(ocharge));
			carDriveOut.setOdriveintime(odriveintime);
			carDriveOut.setOdriveouttime(odriveouttime);
			carDriveOut.setOname(oname);
			carDriveOut.setOparkid(oparkid);
			carDriveOut.setOparktime(oparktime);
			carDriveOut.setOplatenum(oplatenum);
			carDriveOut.setOprice(oprice);
			CarDriveOutDao carDriveOutDao = new CarDriveOutDao();
			
			Park park = new Park();
			String status = "空闲";
			park.setParkid(oparkid);
			park.setStatus(status);
			carDriveOutDao.editPark(park);
			
			float new_balance = obalance-ocharge;
			CarUser carUser = new CarUser();
			String n_balance = Float.toString(new_balance);
			carUser.setUname(oname);
			carUser.setUbalance(n_balance);
			carDriveOutDao.editCarUser(carUser);
			
			CarDriveIn carDriveIn = new CarDriveIn();
			carDriveIn.setDplatenum(oplatenum);
			carDriveOutDao.delDriveIn(carDriveIn);
			
			DuoJi duoji = new DuoJi();
			DuojiDao duojiDao = new DuojiDao();
			
			if(carDriveOutDao.addDriveOut(carDriveOut)) {
				try {
					String sduoji = "aa";
					duoji.setSduoji(sduoji);
					duojiDao.editDuoji(duoji);
					
					response.getWriter().write("success");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}finally {
					carDriveOutDao.closeCon();
				}
			}
	}
	
		private void CarDriveOut(HttpServletRequest request, HttpServletResponse response) {
			try {
				request.getRequestDispatcher("view/CarDriveOut.jsp").forward(request, response);
			} catch (ServletException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

	}
		private void getCarDriveInList(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
			String cplatenum = request.getParameter("carPlatenum");
			String carcname = request.getParameter("carcname");
			Integer currentPage = request.getParameter("page") == null?1 : Integer.parseInt(request.getParameter("page"));
			Integer pageSize = request.getParameter("rows") == null?999 : Integer.parseInt(request.getParameter("rows"));
			CarDriveIn carDriveIn = new CarDriveIn();
			carDriveIn.setDname(carcname);
			carDriveIn.setDplatenum(cplatenum);
			CarDriveInDao carDriveInDao = new CarDriveInDao();
			List<CarDriveIn> carDriveInList = carDriveInDao.getCarDriveInList(carDriveIn, new Page(currentPage,pageSize));
			int total = carDriveInDao.getCarDriveInListTotal(carDriveIn);
			carDriveInDao.closeCon();
			response.setCharacterEncoding("UTF-8");
			//JsonConfig jsonConfig = new JsonConfig();
			//String ParkListString = JSONArray.fromObject(parkList, jsonConfig).toString();
			Map<String,Object> ret = new HashMap<String,Object>();
			ret.put("total",total);
			ret.put("rows",carDriveInList);
			
			try {
				response.getWriter().write(JSONObject.fromObject(ret).toString());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
		private void carDriveInListView(HttpServletRequest request, HttpServletResponse response) {
			try {
				request.getRequestDispatcher("view/CarDriveInList.jsp").forward(request, response);
			} catch (ServletException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
	}
		
		private void addCarDriveIn(HttpServletRequest request, HttpServletResponse response) {
			String drivein = request.getParameter("driveintime");
			Date driveintime = null;
			try {
				driveintime = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").parse(drivein);
			} catch (ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			String dparkid = request.getParameter("dparkid");
			String dname = request.getParameter("dname");
			String dplatenum = request.getParameter("dplatenum");
			CarDriveIn carDriveIn = new CarDriveIn();
			carDriveIn.setDname(dname);
			carDriveIn.setDparkid(dparkid);
			carDriveIn.setDplatenum(dplatenum);
			carDriveIn.setDriveintime(driveintime);
			CarDriveInDao carDriveInDao = new CarDriveInDao();
			Park park = new Park();
			DuoJi duoji = new DuoJi();
			DuojiDao duojiDao = new DuojiDao();
			if(carDriveInDao.addDriveIn(carDriveIn)) {
				try {
					
					String sduoji = "aa";
					duoji.setSduoji(sduoji);
					duojiDao.editDuoji(duoji);
					
					String status = "(停放中)"+dplatenum;
					park.setParkid(dparkid);
					park.setStatus(status);
					carDriveInDao.editPark(park);
					response.getWriter().write("success");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}finally {
					carDriveInDao.closeCon();
				}
			}
					
	}
		private void CarDriveIn(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
			try {
				request.getRequestDispatcher("view/CarDriveIn.jsp").forward(request, response);
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
