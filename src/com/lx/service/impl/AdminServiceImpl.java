package com.lx.service.impl;

import com.lx.dao.AdminDao;
import com.lx.doman.*;
import com.lx.service.AdminService;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class AdminServiceImpl implements AdminService {

    public List<Category> findAllCategory() {

        AdminDao dao = new AdminDao();
        List<Category> categoryList = null;
        try {
            categoryList = dao.findAllCategory();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return categoryList;
    }

    public void addProduct(Product product) {
        AdminDao dao = new AdminDao();
        try {
            dao.addProduct(product);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Order> findAllOrders() {
        AdminDao dao = new AdminDao();
        List<Order> orderList = null;
        try {
            orderList = dao.findAllOrders();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orderList;
    }

    public List<Map<String, Object>> findOrderInfoByOid(String oid) {
        AdminDao dao = new AdminDao();
        List<Map<String, Object>> orderInfoList = null;
        try {
            orderInfoList = dao.findOrderInfoByOid(oid);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orderInfoList;
    }

    public PageBean findProduct(int currentPage, int currentCount) {
        AdminDao dao = new AdminDao();
        PageBean pagebean = new PageBean();
        pagebean.setCurrentPage(currentPage);
        pagebean.setCurrentCount(currentCount);
        //查总条数
        int totalCount = 0;
        try {
            totalCount = dao.findProductCount();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //封装总条数
        pagebean.setTotalCount(totalCount);

        //封装总页数
        int totalPage = (int) Math.ceil(1.0 * totalCount / currentCount);
        pagebean.setTotalPage(totalPage);
        //查询记录
        //索引
        int index = (currentPage - 1) * currentCount;
        List<Product> productList = null;
        try {
            productList = dao.findProductList(index, currentCount);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //封装查询productList
        pagebean.setList(productList);
        //pagebean封装完毕，返回
        return pagebean;
    }

    @Override
    public ProductVo findProductByPid(String pid) {
        AdminDao dao = new AdminDao();
        ProductVo product = null;
        try {
            product = dao.findProductByPid(pid);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return product;

    }

    @Override
    public void updateProduct(ProductVo product) {
        AdminDao dao = new AdminDao();
        try {
            dao.updateProduct(product);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delCategory(String cid) {
        AdminDao dao = new AdminDao();
        try {
            dao.delCategory(cid);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void saveCategory(String cid, String cname) {
        AdminDao dao =new AdminDao();
        try {
            dao.saveCategory(cid,cname);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Category findCategoryByCid(String cid) {
        AdminDao dao = new AdminDao();
        Category category = null;
        try {
            category = dao.findCategoryBycid(cid);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return category;
    }

    @Override
    public void addCategory(Category category) {
        AdminDao dao = new AdminDao();
        try {
            dao.addCategory(category);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void delProduct(String pid) {
        AdminDao dao = new AdminDao();
        try {
            dao.delProduct(pid);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}


