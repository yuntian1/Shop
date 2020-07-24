package com.lx.dao;

import com.lx.doman.Category;
import com.lx.doman.Order;
import com.lx.doman.Product;
import com.lx.utils.DataSourceUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class AdminDao {


    public List<Category> findAllCategory() throws SQLException {
        QueryRunner runner =new QueryRunner(DataSourceUtils.getDataSource());
        String sql = "select * from category";
        List<Category> categoryList = runner.query(sql, new BeanListHandler<Category>(Category.class));
        return categoryList;
    }

    public void  addProduct(Product product) throws SQLException {
        QueryRunner runner =new QueryRunner(DataSourceUtils.getDataSource());
        String sql = "insert into product values(?,?,?,?,?,?,?,?,?,?)";
        runner.update(sql,product.getPid(),product.getPname(),product.getMarket_price(),
                product.getShop_price(),product.getPimage(),product.getPdate(),product.getIs_hot(),
                product.getPdesc(),product.getPflag(),product.getCategory().getCid());
    }

    public List<Order> findAllOrders() throws SQLException {
        QueryRunner runner =new QueryRunner(DataSourceUtils.getDataSource());
        String sql = "select * from orders";
        List<Order> orderList = runner.query(sql, new BeanListHandler<Order>(Order.class));
        return orderList;
    }

    public List<Map<String, Object>> findOrderInfoByOid(String oid) throws SQLException {
        QueryRunner runner =new QueryRunner(DataSourceUtils.getDataSource());
        String sql = "select p.pimage,p.pname,p.shop_price,i.count,i.subtotal " +
                "from product p,orderItem i " +
                "where  p.pid=i.pid and i.oid=?";
        List<Map<String, Object>> mapList = runner.query(sql, new MapListHandler(),oid);
        return mapList;

    }

    public List<Product> findProductList(int index, int currentCount) throws SQLException {
        QueryRunner runner =new QueryRunner(DataSourceUtils.getDataSource());
        String sql="select * from product limit ?,?";
        List<Product> productList = runner.query(sql, new BeanListHandler<Product>(Product.class), index, currentCount);
        return productList;
    }

    public int findProductCount() throws SQLException {
        QueryRunner runner =new QueryRunner(DataSourceUtils.getDataSource());
        String sql="select count(*) from product";
        Long totalCount = (Long) runner.query(sql, new ScalarHandler());
        return totalCount.intValue();

    }


    public void delProduct(String pid) throws SQLException {
        QueryRunner runner =new QueryRunner(DataSourceUtils.getDataSource());
        String sql="delete from product where pid=?";
        runner.update(sql, pid);
    }
}


