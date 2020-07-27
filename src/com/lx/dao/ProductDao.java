package com.lx.dao;

import com.lx.doman.Category;
import com.lx.doman.Order;
import com.lx.doman.OrderItem;
import com.lx.doman.Product;
import com.lx.utils.DataSourceUtils;
import org.apache.catalina.tribes.group.interceptors.OrderInterceptor;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class ProductDao {


    public List<Product> findHotProductList() throws SQLException {

        QueryRunner runner = new QueryRunner(DataSourceUtils.getDataSource());
        String sql = "select * from product where is_hot=? limit ?,?";
        List<Product> hotProductList = runner.query(sql, new BeanListHandler<Product>(Product.class), 1, 0, 9);
        return hotProductList;
    }


    public List<Product> findNewProductList() throws SQLException {
        QueryRunner runner = new QueryRunner(DataSourceUtils.getDataSource());
        String sql = "select * from product order by pdate desc limit ?,?";
        List<Product> newProductList = runner.query(sql, new BeanListHandler<Product>(Product.class), 0, 9);
        return newProductList;
    }

    public List<Category> findCategoryList() throws SQLException {
        QueryRunner runner = new QueryRunner(DataSourceUtils.getDataSource());
        String sql = "select * from category";
        List<Category> categoryList = runner.query(sql, new BeanListHandler<Category>(Category.class));
        return categoryList;
    }

    public int getCount(String cid) throws SQLException {
        QueryRunner runner = new QueryRunner(DataSourceUtils.getDataSource());
        String sql = "select count(*) from product where cid=?";
        Long query = (Long) runner.query(sql, new ScalarHandler(), cid);
        return query.intValue();
    }

    public List<Product> findProductByPage(String cid, int index, int currentCount) throws SQLException {
        QueryRunner runner = new QueryRunner(DataSourceUtils.getDataSource());
        String sql = "select * from product where cid=? limit ?,?";
        List<Product> list = runner.query(sql, new BeanListHandler<Product>(Product.class), cid, index, currentCount);
        return list;
    }

    public Product findProductByPid(String pid) throws SQLException {
        QueryRunner runner = new QueryRunner(DataSourceUtils.getDataSource());
        String sql = "select * from product where pid=?";
        Product product = runner.query(sql, new BeanHandler<Product>(Product.class), pid);
        return product;
    }

    public Category findCategory(String cid) throws SQLException {
        QueryRunner runner = new QueryRunner(DataSourceUtils.getDataSource());
        String sql = "select * from category where cid=?";
        Category category = (Category) runner.query(sql, new BeanHandler<Category>(Category.class), cid);
        return category;
    }

    //向order表插入数据
    public void addOrder(Order order) throws SQLException {
        QueryRunner runner = new QueryRunner();
        String sql = "insert into orders values(?,?,?,?,?,?,?,?)";
        Connection con = DataSourceUtils.getConnection();
        runner.update(con, sql, order.getOid(), order.getOrdertime(), order.getTotal(),
                order.getState(), order.getAddress(), order.getName(), order.getTelephone(), order.getUser().getUid());
    }

    //向orderItem表插入数据
    public void addOrderItem(Order order) throws SQLException {
        QueryRunner runner = new QueryRunner();
        String sql = "insert into orderitem values(?,?,?,?,?)";
        Connection con = DataSourceUtils.getConnection();
        List<OrderItem> orderItems = order.getOrderItems();
        for (OrderItem item : orderItems) {
            runner.update(con, sql, item.getItemid(), item.getCount(), item.getSubtotal(), item.getProduct().getPid(),
                    item.getOrder().getOid());
        }
    }

    //更新订单地址信息
    public void updateOrderAddr(Order order) throws SQLException {
        QueryRunner runner = new QueryRunner(DataSourceUtils.getDataSource());
        String sql = "update orders set address=?,name=?,telephone=? where oid=?";
        runner.update(sql, order.getAddress(), order.getName(), order.getTelephone(), order.getOid());
    }

    //更新支负状态
    public void updateOrderState(String r6_order) throws SQLException {
        QueryRunner runner = new QueryRunner(DataSourceUtils.getDataSource());
        String sql = "update orders set state=? where oid=?";
        runner.update(sql, 1, r6_order);

    }

    public List<Order> findAllOrders(String uid) throws SQLException {
        QueryRunner runner = new QueryRunner(DataSourceUtils.getDataSource());
        String sql = "select * from orders where uid=?";
        List<Order> orderList = runner.query(sql,new BeanListHandler<Order>(Order.class),uid);
        return orderList;
    }

    public List<Map<String, Object>> findAllOrderItemByOid(String oid) throws SQLException {
        QueryRunner runner = new QueryRunner(DataSourceUtils.getDataSource());
        String sql = "select i.count,i.subtotal,p.pimage,p.pname,p.shop_price from orderItem i,product p where i.pid=p.pid and oid=?";
        List<Map<String, Object>> mapList = runner.query(sql, new MapListHandler(), oid);
        return mapList;
    }

    public List<Object> findProductByWord(String word) throws SQLException {
        QueryRunner runner = new QueryRunner(DataSourceUtils.getDataSource());
        String sql = "select * from product where pname like ? limit 0,8";
        List<Object> productList= (List<Object>) runner.query(sql, new ColumnListHandler("pname"),"%"+word+"%");
        return productList;
    }

    public Product findProductByPname(String pname) throws SQLException {
        QueryRunner runner = new QueryRunner(DataSourceUtils.getDataSource());
        String sql = "select * from product where pname=?";
        Product product= runner.query(sql, new BeanHandler<Product>(Product.class), pname);
        return product;
    }
}

