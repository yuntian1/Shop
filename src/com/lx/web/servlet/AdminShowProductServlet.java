package com.lx.web.servlet;

import com.lx.doman.PageBean;
import com.lx.service.AdminService;
import com.lx.utils.BeanFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/adminShowProduct")
public class AdminShowProductServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        AdminService service = (AdminService) BeanFactory.getBean("adminService");
        String currentPageStr = request.getParameter("currentPage");
        if(currentPageStr==null){
            currentPageStr="1";
        }
        int currentPage = Integer.parseInt(currentPageStr);
        int currentCount = 6;
        PageBean pageBean = service.findProduct(currentPage,currentCount);
        request.setAttribute("currentPage",currentPage);
        request.setAttribute("pageBean",pageBean);
        request.getRequestDispatcher("admin/product/list.jsp").forward(request, response);

    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        doGet(request, response);


    }
}
