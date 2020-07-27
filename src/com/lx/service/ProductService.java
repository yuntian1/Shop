package com.lx.service;

import com.lx.dao.ProductDao;
import com.lx.doman.*;
import com.lx.utils.DataSourceUtils;
import com.mysql.cj.x.protobuf.MysqlxExpr;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class ProductService {

    //获得最热商品
    public List<Product> findHotProductList() {

        ProductDao dao = new ProductDao();
        List<Product> hotProductList = null;
        try {
            hotProductList = dao.findHotProductList();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return hotProductList;

    }

    //获得最新商品
    public List<Product> findNewProductList() {
        ProductDao dao = new ProductDao();
        List<Product> newProductList = null;
        try {
            newProductList = dao.findHotProductList();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return newProductList;

    }

    //    获得商品分类列表
    public List<Category> findCategoryList() {
        ProductDao dao = new ProductDao();
        List<Category> categoryList = null;
        try {
            categoryList = dao.findCategoryList();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return categoryList;

    }

    public PageBean findProductListByCid(String cid, int currentPage, int currentCount) {

        ProductDao dao = new ProductDao();
        //封装一个PageBean返回给web层
        PageBean pageBean = new PageBean();

//      封装当前页
        pageBean.setCurrentPage(currentPage);
        pageBean.setCurrentCount(currentCount);
//      封装总条数
        int totalCount = 0;
        try {
            totalCount = dao.getCount(cid);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        pageBean.setTotalCount(totalCount);
//        封装总页数
        int totalPage = (int) Math.ceil(1.0 * totalCount / currentCount);
        pageBean.setTotalPage(totalPage);
//        当前页显示的数据
//        当前页与索引index的关系
        int index = (currentPage - 1) * currentCount;
        List<Product> list = null;
        try {
            list = dao.findProductByPage(cid, index, currentCount);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        pageBean.setList(list);

        return pageBean;
    }

    public Product findProductByPid(String pid) {
        ProductDao dao = new ProductDao();
        Product product = null;
        try {
            product = dao.findProductByPid(pid);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return product;
    }

    public Category findCategory(String cid) {
        ProductDao dao = new ProductDao();
        Category category = null;
        try {
            category = dao.findCategory(cid);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return category;
    }

    //提交订单将数据存到数据库
    public void submitOrder(Order order) {
        ProductDao dao = new ProductDao();
        try {
            //开启事务
            DataSourceUtils.startTransaction();
            //调用dao存储Order表的方法
            dao.addOrder(order);
            //调用dao存储OrderItem表的方法
            dao.addOrderItem(order);
        } catch (SQLException e) {
            try {
                DataSourceUtils.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            try {
                DataSourceUtils.commitAndRelease();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void updateOrderAddr(Order order) {
        ProductDao dao = new ProductDao();
        try {
            dao.updateOrderAddr(order);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void updateOrderState(String r6_order) {
        ProductDao dao = new ProductDao();
        try {
            dao.updateOrderState(r6_order);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Order> findAllOrders(String uid) {
        ProductDao dao = new ProductDao();
        List<Order> orderList = null;
        try {
            orderList = dao.findAllOrders(uid);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orderList;
    }

    public List<Map<String, Object>> findAllOrderItemByOid(String oid) {
        ProductDao dao = new ProductDao();
        List<Map<String, Object>> mapList = null;
        try {
            mapList = dao.findAllOrderItemByOid(oid);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return mapList;
    }

    //根据关键字查询商品
    public List<Object> findProductByWord(String word) {
    ProductDao dao = new ProductDao();
        List<Object> productList= null;
        try {
            productList = dao.findProductByWord(word);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return  productList;
    }

    public Product findProductByPname(String pname) {
        ProductDao dao = new ProductDao();
        Product product = null;
        try {
            product = dao.findProductByPname(pname);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return product;
    }
}


