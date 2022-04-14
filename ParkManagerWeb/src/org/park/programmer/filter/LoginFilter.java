package org.park.programmer.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginFilter implements Filter {

	@Override
	public void destroy(){
		

	}
	@Override
	public void doFilter(ServletRequest req, ServletResponse rep, FilterChain chain)
			throws IOException, ServletException {
		// TODO Auto-generated method stub
		HttpServletRequest request = (HttpServletRequest)req;
		HttpServletResponse response= (HttpServletResponse)rep;
		Object user = request.getSession().getAttribute("user");//LoginServlet会将用户信息保存到session
		if(user == null) {
			//未登录
			response.sendRedirect("index.jsp");
			return;
		
		}else {
			chain.doFilter(request, response);
		}
	}
	@Override
	public void init(FilterConfig filterConfig)
	{
		
	}

}
