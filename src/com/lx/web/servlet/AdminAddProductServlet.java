package com.lx.web.servlet;

import com.lx.doman.Category;
import com.lx.doman.Product;
import com.lx.service.AdminService;
import com.lx.utils.BeanFactory;
import com.lx.utils.CommonsUtils;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;

@WebServlet("/adminAddProduct")
public class AdminAddProductServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        Map<String,Object> map = new HashMap<String,Object>();
        Product product = new Product();
        //创建磁盘文件项工厂
        DiskFileItemFactory factory = new DiskFileItemFactory();
        //创建文件上核心对象
        ServletFileUpload upload = new ServletFileUpload(factory);
        //获得文件项集合并解析
        try {
            List<FileItem> fileItems = upload.parseRequest(request);
            //遍历文件项
            for(FileItem item:fileItems){
                //判断是否是普通表单项
                boolean formField = item.isFormField();
                if(formField){
                    //是普通表单项
                    String fieldName = item.getFieldName();
                    String fieldValue = item.getString("UTF-8");
                    //将普通表单提交的数据放到map中
                    map.put(fieldName,fieldValue);
                }else{
                    //是文件上传项
                    String fileName = item.getName();
                    InputStream in = item.getInputStream();
                    String path = this.getServletContext().getRealPath("upload");
                    OutputStream out = new FileOutputStream(path+"/"+fileName);
                    IOUtils.copy(in, out);
                    item.delete();
                    map.put("pimage","/upload"+fileName);
                }

            }
            BeanUtils.populate(product,map);
            //判断是否封装完全
            //private String pid;
            product.setPid(CommonsUtils.getUUID());
            //private Date pdate;
            product.setPdate(new Date());
            //private int pflag;
            product.setPflag(0);
            //cid
            Category category = new Category();
            category.setCid(map.get("cid").toString());
            product.setCategory(category);
            AdminService service = (AdminService) BeanFactory.getBean("adminService");
            service.addProduct(product);

            response.sendRedirect(request.getContextPath()+"admin/product/list.jsp");

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        doGet(request, response);


    }
}
