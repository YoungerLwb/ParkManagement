package org.park.programmer.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadBase;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.park.programmer.dao.CarUserDao;
import org.park.programmer.entity.CarUser;
import org.park.programmer.entity.Page;
import org.park.programmer.entity.Park;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 
 * @author Administrator 车主信息管理功能实现servlet
 */
public class CarUserServlet extends HttpServlet {
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String method = request.getParameter("method");
		if ("toCarUserListView".equals(method)) {
			CarUserList(request, response);
		} else if ("AddCarUser".equals(method)) {
			addCarUser(request, response);
		}else if ("getCarUserList".equals(method)) {
			getCarUserList(request, response);
		}else if("EditCarUser".equals(method)) {
			editCarUserList(request, response);
		}else if("DeleteCarUser".equals(method)) {
			delCarUserList(request,response);
		}else if("addBalance".equals(method)) {
			addBalanceList(request,response);
		}else if("addBalanceCarUser".equals(method)) {
			addBalanceCarUser(request,response);
		}else if("getBalance".equals(method)) {
			getBalance(request,response);
		}
	}

	private void getBalance(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		System.out.println(request.getParameter("oname")); 
		String oname = request.getParameter("oname");
		
		Integer currentPage = request.getParameter("page") == null?1 : Integer.parseInt(request.getParameter("page"));
		Integer pageSize = request.getParameter("rows") == null?999 : Integer.parseInt(request.getParameter("rows"));
		CarUser carUser = new CarUser();
		carUser.setUname(oname);
		CarUserDao carUserDao = new CarUserDao();
		String balance= carUserDao.getBalance(carUser);
		
		carUserDao.closeCon();
		response.setCharacterEncoding("UTF-8");
		//JsonConfig jsonConfig = new JsonConfig();
		//String ParkListString = JSONArray.fromObject(parkList, jsonConfig).toString();
		List<String> list = new ArrayList<String>(); 
		list.add(balance);
		try {
			response.getWriter().write(JSONArray.fromObject(list).toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void addBalanceCarUser(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		Integer uid =Integer.parseInt(request.getParameter("euid"));
		float aubalance = Float.parseFloat(request.getParameter("eubalance"));
		float eban = Float.parseFloat(request.getParameter("eban"));
		float total = aubalance+eban;
		CarUser carUser = new CarUser();
		carUser.setUid(uid);
		String totalBalance = Float.toString(total);
		carUser.setUbalance(totalBalance);
		CarUserDao carUserDao = new CarUserDao();
		if(carUserDao.addBalanceCarUser(carUser)) {
			try {
				response.getWriter().write("success");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private void addBalanceList(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		try {
			request.getRequestDispatcher("view/addBalance.jsp").forward(request, response);
		} catch (ServletException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void delCarUserList(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		String[] uids = request.getParameterValues("uids[]");
		String[] uicds = request.getParameterValues("uicds[]");
		String idStr = "";
		for(String uid:uids) {
			idStr += uid+",";
		}
		idStr = idStr.substring(0, idStr.length()-1);
		CarUserDao carUserDao = new CarUserDao();
		if(carUserDao.delCarUser(idStr)) {
			try {
				response.getWriter().write("success");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally {
				carUserDao.closeCon();
			}
		}
	}

	private void editCarUserList(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		
		Integer uid =Integer.parseInt(request.getParameter("euid"));
		int userType = Integer.parseInt(request.getSession().getAttribute("userType").toString());
		String upwd = null;
		if(userType == 2) {
			CarUser currentCarUser = (CarUser)request.getSession().getAttribute("user");
			upwd = currentCarUser.getUpwd();
		}else {
			 upwd = request.getParameter("eupwd");
		}
		String username = request.getParameter("eusername");
		String uicd = request.getParameter("euicd");
		String uname = request.getParameter("euname");
		String usex = request.getParameter("eusex");
		String utel = request.getParameter("eutel");
	
		CarUser carUser = new CarUser();
		carUser.setUid(uid);
		carUser.setUsername(username);
		carUser.setUicd(uicd);
		carUser.setUpwd(upwd);
		carUser.setUname(uname);
		carUser.setUsex(usex);
		carUser.setUtel(utel);
		CarUserDao carUserDao = new CarUserDao();
		if(carUserDao.editCarUser(carUser)) {
			try {
				response.getWriter().write("success");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally {
				carUserDao.closeCon();
			}
		}
	}

	private void getCarUserList(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		String carUsername = request.getParameter("carUserName");
		Integer currentPage = request.getParameter("page") == null?1 : Integer.parseInt(request.getParameter("page"));
		Integer pageSize = request.getParameter("rows") == null?999 : Integer.parseInt(request.getParameter("rows"));
		
		int userType = Integer.parseInt(request.getSession().getAttribute("userType").toString());
		CarUser carUser = new CarUser();
		carUser.setUsername(carUsername);
		if(userType == 2) {
			//如果是车主用户，只能查看自己的信息
			CarUser currentCarUser = (CarUser)request.getSession().getAttribute("user");
			carUser.setUid(currentCarUser.getUid());
		}
		CarUserDao carUserDao = new CarUserDao();
		List<CarUser> carUserList = carUserDao.getCarUserList(carUser, new Page(currentPage,pageSize));
		int total = carUserDao.getCarUserListTotal(carUser);
		
		carUserDao.closeCon();
		response.setCharacterEncoding("UTF-8");
		//JsonConfig jsonConfig = new JsonConfig();
		//String ParkListString = JSONArray.fromObject(parkList, jsonConfig).toString();
		Map<String,Object> ret = new HashMap<String,Object>();
		ret.put("total",total);
		ret.put("rows",carUserList);
		
		try {
			
			String fromString = request.getParameter("from");
			if("combox".equals(fromString)) {
				response.getWriter().write(JSONArray.fromObject(carUserList).toString());
			}else {
				response.getWriter().write(JSONObject.fromObject(ret).toString());			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void addCarUser(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		try {
			request.setCharacterEncoding("utf-8");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html; charset=UTF-8");
		//上传
		boolean isMultipart = ServletFileUpload.isMultipartContent(request);
		if(isMultipart) {//判断前台form是否有multipart属性
			//FileItemFactory factory = new DiskFileItemFactory();//FileItemFactory的实现类，FileItemFactory是一个接口
			DiskFileItemFactory factory = new DiskFileItemFactory();
			ServletFileUpload upload = new ServletFileUpload(factory);
			CarUserDao carUserDao = new CarUserDao();
			try {		
				//控制上传单个文件的大小 20KB ServletFileUpload						
				upload.setSizeMax(20480);//单位是字节B
				//通过parseRequest(解析form中的所有请求字段，并保存到items集合中，（即前台传递的sno sname spiture此时就保存在了item中）
				List<FileItem> items = upload.parseRequest(request);
				//遍历items中的数据（sno，sname，spicture）
				Iterator<FileItem> iter= items.iterator();//迭代器
				String username = null;
				String upwd = null;
				String uicd = null;
				String uname = null;
				String usex = null;
				String utel = null;
				String ubalance = null;
				InputStream file = null;
				while(iter.hasNext()) {
					FileItem item = iter.next();
					String itemName = item.getFieldName();
					//判断前端字段是普通form表单字段(sno,sname)还是文件字段
					
					if(item.isFormField()) {
						if(itemName.equals("username")) {//根据name属性判断item是sno sname 还是spiture
							username = item.getString("utf-8");
						}else if(itemName.equals("upwd")) {
							upwd= item.getString("utf-8");
						}else if(itemName.equals("uicd")){
							uicd = item.getString("utf-8");
						}else if(itemName.equals("uname")){
							uname = item.getString("utf-8");
						}else if(itemName.equals("usex")){
							usex = item.getString("utf-8");
						}else if(itemName.equals("utel")){
							utel = item.getString("utf-8");
						}else if(itemName.equals("ubalance")){
							ubalance = item.getString("utf-8");
						}else{
							System.out.println("文件字段");
						}
					}else {//spitre   文件上传处理
						 file = item.getInputStream();//将文件转为输入流
						}						
			}
				
				CarUser carUser = new CarUser();
				carUser.setUsername(username);
				carUser.setUicd(uicd);
				carUser.setUpwd(upwd);
				carUser.setUname(uname);
				carUser.setUsex(usex);
				carUser.setUtel(utel);
				carUser.setUbalance(ubalance);
				carUser.setUphoto(file);
				
			   if(carUserDao.addCarUser(carUser)) {
					try {
						response.getWriter().write("success");
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}catch(FileUploadBase.FileSizeLimitExceededException e){
				System.out.println("文件超出大小");
			}catch (FileUploadException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally {
				carUserDao.closeCon();
			}
		}	
		
	}

	private void CarUserList(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		// TODO Auto-generated method stub
		request.getRequestDispatcher("view/CarUserList.jsp").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
}
