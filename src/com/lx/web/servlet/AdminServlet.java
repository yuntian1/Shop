package com.lx.web.servlet;

import com.google.gson.Gson;
import com.lx.doman.Category;
import com.lx.doman.Order;
import com.lx.doman.PageBean;
import com.lx.doman.Product;
import com.lx.service.AdminService;
import com.lx.utils.BeanFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet("/admin")
public class AdminServlet extends BaseServlet {

    //public void adminDelProduct(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    //
    //    String pid = request.getParameter("pid");
    //    AdminService service = (AdminService) BeanFactory.getBean("AdminService");
    //
    //    service.delProduct(pid);
    //    response.sendRedirect(request.getContextPath()+"/admin/method=findAllProduct");
    //}
    //public void findAllProduct(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    //    AdminService service = (AdminService) BeanFactory.getBean("adminService");
    //    String currentPageStr = request.getParameter("currentPage");
    //    if(currentPageStr==null){
    //        currentPageStr="1";
    //    }
    //    int currentPage = Integer.parseInt(currentPageStr);
    //    int currentCount = 6;
    //    PageBean pageBean = service.findProduct(currentPage,currentCount);
    //    request.setAttribute("currentPage",currentPage);
    //    request.setAttribute("pageBean",pageBean);
    //    request.getRequestDispatcher("admin/product/list.jsp").forward(request, response);
    //}
    //后台订单详情
    public void findOrderInfoByOid(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=utf-8");
        String oid = request.getParameter("oid");
        AdminService service = (AdminService) BeanFactory.getBean("adminService");
        //用解耦合的方式进行编码 --姐web层与service的耦合

        List<Map<String,Object>> orderInfolist = service.findOrderInfoByOid(oid);
        Gson gson = new Gson();
        String json = gson.toJson(orderInfolist);
        response.getWriter().write(json);

    }


    //查询所有的订单
    public void findAllOrders(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        AdminService service = (AdminService) BeanFactory.getBean("adminService");
        List<Order> orderList  = service.findAllOrders();
        request.setAttribute("orderList",orderList);
        request.getRequestDispatcher("admin/order/list.jsp").forward(request,response);
    }
    //查询分类，ajax动态显示下拉列表
    public void findAllCategory(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");
        AdminService service = (AdminService) BeanFactory.getBean("adminService");
        List<Category> categoryList = service.findAllCategory();
        Gson gson = new Gson();
        String json = gson.toJson(categoryList);
        response.getWriter().write(json);
    }


}
