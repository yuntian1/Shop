package com.lx.web.servlet;

import com.lx.doman.Category;
import com.lx.service.AdminService;
import com.lx.utils.BeanFactory;
import com.lx.utils.CommonsUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/adminAddCategory")
public class AdminAddCategoryServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        AdminService service = (AdminService) BeanFactory.getBean("adminService");
        String cname = request.getParameter("cname");
        String cid = CommonsUtils.getUUID();
        Category category = new Category();
        category.setCid(cid);
        category.setCname(cname);
        service.addCategory(category);
        response.sendRedirect(request.getContextPath()+"/adminCategoryList");

    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        doGet(request, response);


    }
}
