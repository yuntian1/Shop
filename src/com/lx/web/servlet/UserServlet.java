package com.lx.web.servlet;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.lx.doman.User;
import com.lx.service.UserService;
import com.lx.utils.MD5Utils;

@WebServlet("/user")
public class UserServlet extends BaseServlet {
//用户注销
	public void logout(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		HttpSession  session= request.getSession();
		session.removeAttribute("user");
		Cookie cookie_username = new Cookie("cookie_username","");
		cookie_username.setMaxAge(0);
		//创建存储密码的cookie
		Cookie cookie_password = new Cookie("cookie_password","");
		cookie_password.setMaxAge(0);

		response.addCookie(cookie_username);
		response.addCookie(cookie_password);

		response.sendRedirect(request.getContextPath()+"/login.jsp");
	}
	//用户登录
	public void login(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		request.setCharacterEncoding("UTF-8");
		//得到用户输入的验证码
		String checkcode_client = request.getParameter("checkcode_session");
		//得到验证码
		String checkcode_session = (String) request.getSession().getAttribute("checkcode_session");
		if(!checkcode_session.equals(checkcode_client)){
			request.setAttribute("loginInfo", "您的验证码不正确");
			request.getRequestDispatcher("/login.jsp").forward(request, response);
			return;
		}
		HttpSession session = request.getSession();

		//获得输入的用户名和密码
		String username = request.getParameter("username");
		String password = request.getParameter("password");

		//对密码进行加密
		//password = MD5Utils.md5(password);

		//将用户名和密码传递给service层
		UserService service = new UserService();
		User user = null;
		try {
			user = service.login(username,password);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		//判断用户是否登录成功 user是否是null
		if(user!=null){
			//登录成功
			//***************判断用户是否勾选了自动登录*****************
			String autoLogin = request.getParameter("autoLogin");
			if("true".equals(autoLogin)){
				//要自动登录
				//创建存储用户名的cookie
				Cookie cookie_username = new Cookie("cookie_username",user.getUsername());
				cookie_username.setMaxAge(10*60);
				//创建存储密码的cookie
				Cookie cookie_password = new Cookie("cookie_password",user.getPassword());
				cookie_password.setMaxAge(10*60);

				response.addCookie(cookie_username);
				response.addCookie(cookie_password);

			}

			//***************************************************
			//将user对象存到session中
			session.setAttribute("user", user);

			//重定向到首页
			response.sendRedirect(request.getContextPath()+"/product?method=index");
		}else{
			request.setAttribute("loginInfo", "用户名或密码错误");
			request.getRequestDispatcher("/login.jsp").forward(request, response);
		}
	}
}
