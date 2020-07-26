package com.lx.web.servlet;

import com.lx.service.AdminService;
import com.lx.utils.BeanFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/adminEditCategory")
public class AdminEditCategoryServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String cid = request.getParameter("cid");
        String cname = request.getParameter("cname");
        AdminService service = (AdminService) BeanFactory.getBean("adminService");
        service.saveCategory(cid,cname);
        response.sendRedirect(request.getContextPath()+"/adminCategoryList");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        doGet(request, response);


    }
}
