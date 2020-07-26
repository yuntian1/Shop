package com.lx.web.servlet;

import com.lx.doman.Category;
import com.lx.service.AdminService;
import com.lx.utils.BeanFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/adminEditCategoryUi")
public class AdminEditCategoryUiServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        AdminService service = (AdminService) BeanFactory.getBean("adminService");
        String cid = request.getParameter("cid");
        Category category = service.findCategoryByCid(cid);
        request.setAttribute("category", category);
        request.getRequestDispatcher("admin/category/edit.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        doGet(request, response);


    }
}
