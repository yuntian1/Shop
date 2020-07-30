package com.lx.web.servlet;

import com.lx.doman.Category;
import com.lx.doman.Condition;
import com.lx.doman.PageBean;
import com.lx.doman.ProductVo;
import com.lx.service.AdminService;
import com.lx.service.ProductService;
import com.lx.utils.BeanFactory;
import org.apache.commons.beanutils.BeanUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

@WebServlet("/adminSearchProductList")
public class AdminSearchProductListServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        //接收数据
        //分页

        Map<String, String[]> parameterMap = request.getParameterMap();
        //将散装的查询数据封装到一个vo实体中
        Condition con = new Condition();
        try {
            BeanUtils.populate(con, parameterMap);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        //数据库查询
        AdminService service = (AdminService) BeanFactory.getBean("adminService");
        List<ProductVo> productList=  service.findProductListByCondition(con);
        ProductService service1 = new ProductService();
        List<Category> categoryList = service1.findCategoryList();
        //查询商品分类列表
        request.setAttribute("con", con);
        request.setAttribute("productList", productList);
        request.setAttribute("categoryList", categoryList);
        request.getRequestDispatcher("admin/product/list.jsp").forward(request, response);

    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        doGet(request, response);


    }
}
