package com.lx.service;

import com.lx.dao.AdminDao;
import com.lx.doman.Category;
import com.lx.doman.Order;
import com.lx.doman.PageBean;
import com.lx.doman.Product;

import java.util.List;
import java.util.Map;

public interface AdminService {


    public List<Category> findAllCategory();


    public void addProduct(Product product);

    public List<Order> findAllOrders();

    public List<Map<String, Object>> findOrderInfoByOid(String oid);

    public PageBean findProduct(int currentPage, int currentCount);

    public void delProduct(String pid);
}
