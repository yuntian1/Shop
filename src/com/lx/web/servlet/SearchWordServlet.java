package com.lx.web.servlet;

import com.google.gson.Gson;
import com.lx.doman.Product;
import com.lx.service.ProductService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/searchWord")
public class SearchWordServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String word = request.getParameter("word");
        //查询该关键字的所有商品
        ProductService service = new ProductService();
        List<Object> productList = service.findProductByWord(word);
        Gson gson = new Gson();
        String json = gson.toJson(productList);
        //System.out.println(json);
        response.getWriter().write(json);

    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        doGet(request, response);


    }
}
