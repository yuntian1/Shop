package com.lx.service;

import com.lx.dao.AdminDao;
import com.lx.doman.*;

import java.util.List;
import java.util.Map;

public interface AdminService {


    public List<Category> findAllCategory();


    public void addProduct(Product product);

    public List<Order> findAllOrders();

    public List<Map<String, Object>> findOrderInfoByOid(String oid);

    public PageBean findProduct(int currentPage, int currentCount);

    public void delProduct(String pid);


    public ProductVo findProductByPid(String pid);

    public  void updateProduct(ProductVo product);

    public void addCategory(Category category);

    public void delCategory(String cid);

    public Category findCategoryByCid(String cid);

    public void saveCategory(String cid, String cname);

    public List<ProductVo> findProductListByCondition(Condition con);
}
