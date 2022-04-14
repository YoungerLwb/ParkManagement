package org.park.programmer.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.park.programmer.dao.AdminDao;
import org.park.programmer.dao.CarUserDao;
import org.park.programmer.entity.Admin;
import org.park.programmer.entity.CarUser;
import org.park.programmer.util.StringUtil;
/**
 * 
 * @author Administrator
 *登录验证Servlet
 */
public class LoginServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String method = request.getParameter("method");
		if("LoginOut".equals(method)) {
			loginOut(request,response);
			return;
		}
		String vcode = request.getParameter("vcode");
		String name = request.getParameter("account");
		String password = request.getParameter("password");
		int type = Integer.parseInt(request.getParameter("type"));
		String loginCpacha = request.getSession().getAttribute("loginCpacha").toString();
		if(StringUtil.isEmpty(vcode)) {
			response.getWriter().write("vcodeError");
			return;
		}
		if(!vcode.toUpperCase().equals(loginCpacha.toUpperCase())) {
			response.getWriter().write("vcodeError");
			return;
		}

		//执行完以上操作还未return，说明用户名密码正确
		//验证码验证通过，对比用户名密码是否正确
		String loginStatus = "loginFaild";
		switch (type) {
		case 1:{
			AdminDao adminDao = new AdminDao();
			Admin admin = adminDao.login(name, password);
			adminDao.closeCon();
			if(admin == null) {
				response.getWriter().write("loginError");
				return;
			}
			HttpSession session = request.getSession();
			session.setAttribute("user", admin);
			session.setAttribute("userType", type);
			loginStatus = "adminLoginSuccess";
			
			break;
		}
		case 2:{
			CarUserDao carUserDao = new CarUserDao();
			CarUser carUser = carUserDao.login(name, password);
			carUserDao.closeCon();
			if(carUser == null) {
				response.getWriter().write("loginError");
				return;
			}
			HttpSession session = request.getSession();
			session.setAttribute("user", carUser);
			session.setAttribute("userType", type);
			loginStatus = "adminLoginSuccess";
			
			break;
		}
		default:
			break;
		}
		
		response.getWriter().write("adminLoginSuccess");		
	}
	private void loginOut(HttpServletRequest request, HttpServletResponse response) throws IOException {
		request.getSession().removeAttribute("user");
		request.getSession().removeAttribute("userType");
		response.sendRedirect("index.jsp");
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		doGet(request, response);
	}

}
