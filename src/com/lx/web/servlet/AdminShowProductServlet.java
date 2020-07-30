package com.lx.web.servlet;

import com.lx.doman.Category;
import com.lx.doman.PageBean;
import com.lx.doman.Product;
import com.lx.doman.ProductVo;
import com.lx.service.AdminService;
import com.lx.service.ProductService;
import com.lx.utils.BeanFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/adminShowProduct")
public class AdminShowProductServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        ProductService service = new ProductService();
        List<ProductVo> productList = service.findAllProduct();
        List<Category> categoryList = service.findCategoryList();
        request.setAttribute("productList",productList);
        request.setAttribute("categoryList",categoryList);
        request.getRequestDispatcher("admin/product/list.jsp").forward(request, response);

    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        doGet(request, response);


    }
}
