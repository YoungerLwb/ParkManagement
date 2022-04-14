package org.park.programmer.servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.park.programmer.dao.AdminDao;
import org.park.programmer.dao.CarUserDao;
import org.park.programmer.dao.ParkDao;
import org.park.programmer.entity.Admin;
import org.park.programmer.entity.CarUser;
import org.park.programmer.entity.Page;
import org.park.programmer.entity.Park;
import org.park.programmer.util.StringUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
/**
 * 
 * @author Administrator
 *	系统登录后主界面
 */
public class SystemServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String method = request.getParameter("method");
		if("toPersonalView".equals(method)) {
			personalView(request,response);
			return;
		}else if("EditPassword".equals(method)) {
			editPassword(request,response);
			return;
		}else if("toAdminiList".equals(method)) {
			toAdminiList(request,response);
			return;
		}else if("AddAdmini".equals(method)){
			addAdmini(request,response);
			return;
		}else if("getAdminiList".equals(method)) {
			getAdminiList(request,response);
			return;
		}
		request.getRequestDispatcher("view/system.jsp").forward(request, response);

		}
	private void getAdminiList(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		String exname = request.getParameter("exname");
		
		Integer pageSize = request.getParameter("rows") == null ? 99 : Integer.parseInt(request.getParameter("rows"));
		Integer currentPage = request.getParameter("page")==null ? 1 : Integer.parseInt(request.getParameter("page"));
		Admin admin = new Admin();
		admin.setUsername(exname);
		AdminDao adminDao = new AdminDao();
		List<Admin> adminList = adminDao.getAdminList(admin, new Page(currentPage,pageSize));
		int total = adminDao.getAdminListTotal(admin);
		
		adminDao.closeCon();
		response.setCharacterEncoding("UTF-8");
		//JsonConfig jsonConfig = new JsonConfig();
		//String ParkListString = JSONArray.fromObject(parkList, jsonConfig).toString();
		Map<String,Object> ret = new HashMap<String,Object>();
		ret.put("total",total);
		ret.put("rows",adminList);		
		try {			
				response.getWriter().write(JSONArray.fromObject(adminList).toString());			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private void addAdmini(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		int status = Integer.parseInt(request.getParameter("status"));
		
		Admin admin = new Admin();
		admin.setUsername(username);
		admin.setPassword(password);
		admin.setStatus(status);
		AdminDao adminDao = new AdminDao();
		
		if(adminDao.addAdmin(admin)) {
			try {
				response.getWriter().write("success");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally {
				adminDao.closeCon();
			}
		}
	}
	private void toAdminiList(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		request.getRequestDispatcher("view/AdminiList.jsp").forward(request, response);
	}
	private void editPassword(HttpServletRequest request, HttpServletResponse response) {
		String password = request.getParameter("password");
		String new_password = request.getParameter("new_password");
		response.setCharacterEncoding("UTF-8");
		int userType = Integer.parseInt(request.getSession().getAttribute("userType").toString());
		if(userType == 1) {
			//管理员
			Admin admin = (Admin)request.getSession().getAttribute("user");
			if(!admin.getPassword().equals(password)) {
				try {
					response.getWriter().write("原密码错误");
					return;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else {
				AdminDao adminDao = new AdminDao();
				if(adminDao.editPassword(admin, new_password)) {
					try {
						response.getWriter().write("success");
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}finally {
						adminDao.closeCon();
					}
				}else {
					try {
						response.getWriter().write("数据库修改错误");
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}finally {
						adminDao.closeCon();
					}
				}
			}
		}else {
			//车主
			CarUser carUser = (CarUser)request.getSession().getAttribute("user");
			if(!carUser.getUpwd().equals(password)) {
				try {
					response.getWriter().write("原密码错误");
					return;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else {
				CarUserDao carUserDao = new CarUserDao();
				if(carUserDao.editPassword(carUser, new_password)) {
					try {
						response.getWriter().write("success");
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}finally {
						carUserDao.closeCon();
					}
				}else {
					try {
						response.getWriter().write("数据库修改错误");
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}finally {
						carUserDao.closeCon();
					}
				}
		}
		}	
		//request.getSession().getAttribute("user");

	}
	private void personalView(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		try {
			request.getRequestDispatcher("view/personalView.jsp").forward(request, response);
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
