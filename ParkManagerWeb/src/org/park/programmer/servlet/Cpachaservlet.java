package org.park.programmer.servlet;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.park.programmer.util.CpachaUtil;
/**
 * 
 * @author Administrator
 *验证码Servlet
 */

public class Cpachaservlet extends HttpServlet {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String method = request.getParameter("method");
		if("loginCpacha".equals(method)) {
			generateLoginCpacha(request, response);
			return ;
		}
		response.getWriter().write("error method");
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		doGet(request, response);
	}
	private void generateLoginCpacha(HttpServletRequest request, HttpServletResponse response) throws IOException {
		CpachaUtil cpachautil = new CpachaUtil();
		String generatorVCode = cpachautil.generatorVCode();
		//将验证码保存在session域中，以便判断验证码是否正确
		request.getSession().setAttribute("loginCpacha", generatorVCode);//session.attribute
		//生成验证码图片
		BufferedImage generatorRotateVCodeImage = cpachautil.generatorRotateVCodeImage(generatorVCode, true);
		ImageIO.write(generatorRotateVCodeImage,"gif",response.getOutputStream());
	
	}

}
