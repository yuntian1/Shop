package com.lx.web.servlet;

import com.google.gson.Gson;
import com.lx.doman.*;
import com.lx.service.ProductService;
import com.lx.utils.CommonsUtils;
import com.lx.utils.JedisPoolUtils;
import com.lx.utils.PaymentUtil;
import org.apache.commons.beanutils.BeanUtils;
import redis.clients.jedis.Jedis;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

@WebServlet("/product")
public class ProductServlet extends BaseServlet {
//    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        response.setContentType("text/html;charset=UTF-8");
//        String method = request.getParameter("method");
//        if ("productList".equals(method)) {
//            productList(request, response);
//        } else if ("categoryList".equals(method)) {
//            categoryList(request, response);
//        } else if ("index".equals(method)) {
//            index(request, response);
//        } else if ("productInfo".equals(method)) {
//            productInfo(request, response);
//        }
//    }
//
//    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        request.setCharacterEncoding("UTF-8");
//        doGet(request, response);
//
//
//    }

    //前台搜索
    public void productSearch(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //获得商品名称
        String pname = req.getParameter("searchname");
        ProductService service = new ProductService();
        //查询名为pname的商品
        Product product = service.findProductByPname(pname);
        req.setAttribute("product", product);
        req.getRequestDispatcher("/product_info.jsp").forward(req, resp);
    }

public void myOrders(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    //判断用户是否登陆
    HttpSession session = req.getSession();
    User user = (User) session.getAttribute("user");
    //if(user==null){
    //    resp.sendRedirect(req.getContextPath()+"/login.jsp");
    //    return;
    //}
    ProductService service = new ProductService();
    //查询该用户中的所有的订单信息（单表查询user表）
    //集合中的每一个order对象的数据是不完整的，缺少List<OrderItem> orderItems数据
    List<Order> orderList = service.findAllOrders(user.getUid());
    //循环所有的订单为每个订单填充订单项集合信息
    if(orderList!=null){
        for(Order order:orderList){
            //获得每一个订单的oid
            String oid = order.getOid();
            //查询该订单所有的订单项--mapList封装的是多个订单项和该订单项中的商品的信息
            List<Map<String, Object>> mapList = service.findAllOrderItemByOid(oid);
            //将mapList转换成List<OrderItem> orderItems
            for(Map<String,Object> map:mapList){

                try {
                    //从map中取出count subtotal 封装到OrderItem中
                    OrderItem item = new OrderItem();
                    BeanUtils.populate(item,map);
                    //从 map中取出 pimage pname shop_price封到Product中
                    Product product = new Product();
                    BeanUtils.populate(product,map);
                    //将product封装到OrderItem中
                    item.setProduct(product);
                    //将orderItem封装到order中的orderItemList中
                    order.getOrderItems().add(item);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }


            }

        }


    }
    //orderList已经封装完毕
    req.setAttribute("orderList",orderList);
    req.getRequestDispatcher("/order_list.jsp").forward(req,resp);

}


    //确认订单--更新收货人信息--在线支付
    public void confirmOrder(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //更新收货人信息
        Map<String, String[]> parameterMap = req.getParameterMap();
        Order order = new Order();
        try {
            BeanUtils.populate(order,parameterMap);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        ProductService service = new ProductService();
        service.updateOrderAddr(order);
        //在线支付
        //获得选择的银行
 /*       String pd_Frpid = req.getParameter("pd_Frpid");
        if(pd_Frpid.equals("ABC-NET-B2C")){
        //接入农行接口
        }else if(pd_Frpid.equals("ICBC-NET_B2C")){
            //接入工行入口

        }*/
        //.......

        //只接入一个接口，这个接口是第三方平台提供的
        //接入易宝支付
        // 获得 支付必须基本数据
        String orderid = req.getParameter("oid");
        String money = order.getTotal()+"";
        //String money = "0.1";
        // 银行
        String pd_FrpId = req.getParameter("pd_FrpId");
// 发给支付公司需要哪些数据
        String p0_Cmd = "Buy";
        String p1_MerId = ResourceBundle.getBundle("merchantInfo").getString("p1_MerId");
        String p2_Order = orderid;
        String p3_Amt = money;
        String p4_Cur = "CNY";
        String p5_Pid = "";
        String p6_Pcat = "";
        String p7_Pdesc = "";
        // 支付成功回调地址 ---- 第三方支付公司会访问、用户访问
        // 第三方支付可以访问网址
        String p8_Url = ResourceBundle.getBundle("merchantInfo").getString("callback");
        String p9_SAF = "";
        String pa_MP = "";
        String pr_NeedResponse = "1";
        // 加密hmac 需要密钥
        String keyValue = ResourceBundle.getBundle("merchantInfo").getString(
                "keyValue");
        String hmac = PaymentUtil.buildHmac(p0_Cmd, p1_MerId, p2_Order, p3_Amt,
                p4_Cur, p5_Pid, p6_Pcat, p7_Pdesc, p8_Url, p9_SAF, pa_MP,
                pd_FrpId, pr_NeedResponse, keyValue);


        String url = "https://www.yeepay.com/app-merchant-proxy/node?pd_FrpId="+pd_FrpId+
                "&p0_Cmd="+p0_Cmd+
                "&p1_MerId="+p1_MerId+
                "&p2_Order="+p2_Order+
                "&p3_Amt="+p3_Amt+
                "&p4_Cur="+p4_Cur+
                "&p5_Pid="+p5_Pid+
                "&p6_Pcat="+p6_Pcat+
                "&p7_Pdesc="+p7_Pdesc+
                "&p8_Url="+p8_Url+
                "&p9_SAF="+p9_SAF+
                "&pa_MP="+pa_MP+
                "&pr_NeedResponse="+pr_NeedResponse+
                "&hmac="+hmac;

        //重定向到第三方支付平台
        resp.sendRedirect(url);


    }
    //    提交订单
    public void submitOrder(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");
        //if (user == null) {
        //    resp.sendRedirect(req.getContextPath() + "/login.jsp");
        //    return;
        //}
//    判断用户是否已经登陆：未登录下面代码不执行

//    封装好一个Order对象传递给service层
        Order order = new Order();
//        订单号
        String oid = CommonsUtils.getUUID();
        order.setOid(oid);
//        下单时间
        order.setOrdertime(new Date());
//        获得购物车
        Cart cart = (Cart) session.getAttribute("cart");
        double total = cart.getTotal();
        order.setTotal(total);
//        订单支付状态
        order.setState(0);
//        收货地址
        order.setAddr(null);
//        收货人
        order.setName(null);
//        收货电话
        order.setTelephone(null);
//        用户
        order.setUser(user);
//        订单项
        Map<String, CartItem> cartItems = cart.getCartItems();
        for (Map.Entry<String, CartItem> entry : cartItems.entrySet()) {
            CartItem cartItem = entry.getValue();
            //创建新的订单项
            OrderItem orderItem = new OrderItem();
            //1. private String itemid;//订单项id
            orderItem.setItemid(CommonsUtils.getUUID());
            //2. private int count;//订单向内商品的购买数量
            orderItem.setCount(cartItem.getBuyNum());
            //3.private double subtotal;//订单小计
            orderItem.setSubtotal(cartItem.getSubtotal());
            //4.private Product product;//订单项内的商品
            orderItem.setProduct(cartItem.getProduct());
            //5.private Order order;//订单项属于哪个订单
            orderItem.setOrder(order);
            //将订单项添加到订单的订单集合中
            order.getOrderItems().add(orderItem);

        }
        //order对象封装完毕
        ProductService service =new ProductService();
        service.submitOrder(order);
        session.setAttribute("order",order);
        //页面跳转
        resp.sendRedirect(req.getContextPath()+"/order_info.jsp");

    }

    //    清空购物车
    public void clearCart(HttpServletRequest req, HttpServletResponse rsp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        session.removeAttribute("cart");
        rsp.sendRedirect(req.getContextPath() + "/cart.jsp");
    }


    //    删除购物车中单一商品
    public void delProFormCart(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        获得要删除的商品的pid
        String pid = req.getParameter("pid");
//    删除session中购物车的item
        HttpSession session = req.getSession();
        Cart cart = (Cart) session.getAttribute("cart");
        Map<String, CartItem> cartItems = cart.getCartItems();
        //        需要修改总价
        cart.setTotal(cart.getTotal() - cartItems.get(pid).getSubtotal());
        cartItems.remove(pid);
        cart.setCartItems(cartItems);


        session.setAttribute("cart", cart);
        resp.sendRedirect(req.getContextPath() + "/cart.jsp");
    }


    //    将商品添加到购物车
    public void addToCart(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        ProductService service = new ProductService();
//        获得要添加到购物车的商品的pid
        String pid = req.getParameter("pid");
//        获得购买该商品的数量
        int buyNum = Integer.parseInt(req.getParameter("buyNum"));
//        获得product对象
        Product product = service.findProductByPid(pid);
//        计算小计
        double subtotal = product.getShop_price() * buyNum;
        CartItem item = new CartItem();
        item.setBuyNum(buyNum);
        item.setProduct(product);
        item.setSubtoatl(subtotal);

//        获得购物车 //判断是否存在购物车
        Cart cart = (Cart) session.getAttribute("cart");
        if (cart == null) {
            cart = new Cart();
        }
//        将购物项放到购物车里- - key是pid
//        先判断购物车里包含不包含此购物项- - -判断key是否已经存在
//        如果购物车中已经存在该商品--将现在买的数量与原有的数量惊醒相加操作
        Map<String, CartItem> cartItems = cart.getCartItems();
        double newsubtotal = 0.0;
        if (cartItems.containsKey(pid)) {
//            取出原有商品的数量
            CartItem cartItem = cartItems.get(pid);
            int oldBuyNum = cartItem.getBuyNum();
            oldBuyNum += buyNum;
            cartItem.setBuyNum(oldBuyNum);

//            修改小计
//            原来商品的小计
            double oldsubtotal = cartItem.getSubtotal();
            newsubtotal = buyNum * product.getShop_price();
            cartItem.setSubtoatl(oldsubtotal + newsubtotal);
            cart.setCartItems(cartItems);
        } else {
//            如果车中没有该商品
            cart.getCartItems().put(product.getPid(), item);
            newsubtotal = buyNum * product.getShop_price();
        }

//        再次访问session
        //计算总计
        double total = cart.getTotal() + newsubtotal;
        cart.setTotal(total);
        session.setAttribute("cart", cart);

//        直接跳转到购物车页面
        resp.sendRedirect(req.getContextPath() + "/cart.jsp");

    }


    //显示商品分类功能
    public void categoryList(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        //先从缓存中查询categoryList 如果有直接使用 没有从数据库中查询
        ProductService service = new ProductService();
        Jedis jedis = JedisPoolUtils.getJedis();
        String categoryListJson = jedis.get("categoryListJson");
        if (categoryListJson == null) {

            System.out.println(" 缓存没有数据查询数据库");
            //从数据库读取数据
            List<Category> categoryList = service.findCategoryList();
//          使用Gson将数据转成json格式
            Gson gson = new Gson();
            categoryListJson = gson.toJson(categoryList);
            jedis.set("categoryListJson", categoryListJson);
        }
        //        设置字符编码
        response.setContentType("text/html;charset=UTF-8");
        response.getWriter().write(categoryListJson);

    }

    //显示首页
    public void index(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        ProductService service = new ProductService();
        //准备热门商品--List<product>
        List<Product> hotProductList = service.findHotProductList();
        //准备分类商品
        List<Category> categoryList = service.findCategoryList();
        //准备最新商品--List<product>
        List<Product> newProductList = service.findNewProductList();
        request.setAttribute("hotProductList", hotProductList);
        request.setAttribute("categoryList", categoryList);
        request.setAttribute("newProductList", newProductList);
        request.getRequestDispatcher("/index.jsp").forward(request, response);

    }

    //显示商品详细信息,实现历史记录功能
    public void productInfo(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        获得当前页
//        String currentPage1 = request.getParameter("currentPage");
//        int currentPage =Integer.parseInt(currentPage1);
        //        获得商品类别
        String cid = request.getParameter("cid");
//        获得当前页
        String currentPage = request.getParameter("currentPage");
//        获得要查询商品的pid
        String pid = request.getParameter("pid");
        ProductService service = new ProductService();
        Product product = service.findProductByPid(pid);
        Category category = service.findCategory(cid);
//
        request.setAttribute("currentPage", currentPage);
        request.setAttribute("cid", cid);
        request.setAttribute("product", product);
        request.setAttribute("category ", category);
//        获得客户端携带的cookie--获得名字是pids的cookie
        String pids = pid;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("pids".equals(cookie.getName())) {
                    pids = cookie.getValue();//1-3-2本次访问商品的pid是8-1-3-2
//                    将pids拆成一个数组
                    String[] split = pids.split("-");
                    List<String> asList = Arrays.asList(split);
                    LinkedList<String> list = new LinkedList(asList);
//                    判断集合中是否包含当前pid
                    if (list.contains(pid)) {
                        list.remove(pid);
                        list.addFirst(pid);
                    } else {
                        list.addFirst(pid);
                    }
                    StringBuffer sb = new StringBuffer();
                    for (int i = 0; i < list.size() && i < 7; i++) {
                        sb.append(list.get(i));
                        sb.append("-");
                    }
                    pids = sb.substring(0, sb.length() - 1);
                }

            }


        }

//        当在转发之前 创建cookie存储pid
        Cookie cookie_pids = new Cookie("pids", pids);
        response.addCookie(cookie_pids);

        request.getRequestDispatcher("/product_info.jsp").forward(request, response);

    }

    //根据商品类别获得商品功能
    public void productList(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //获得cid
        String cid = request.getParameter("cid");
        response.setContentType("text/html;charset=UTF-8");
        String currentPageStr = request.getParameter("currentPage");
        if (currentPageStr == null) currentPageStr = "1";
        int currentPage = Integer.parseInt(currentPageStr);
        int currentCount = 12;

        ProductService service = new ProductService();
        PageBean pageBean = service.findProductListByCid(cid, currentPage, currentCount);
        request.setAttribute("pageBean", pageBean);
        request.setAttribute("cid", cid);


//        定义一个记录历史商品的集合
        List<Product> historyProductList = new ArrayList<Product>();

//        获得客户端携带的名叫pids的cookie
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("pids".equals(cookie.getName())) {
                    String pids = cookie.getValue();
                    String[] split = pids.split("-");
                    for (String pid : split) {
                        Product pro = service.findProductByPid(pid);
                        historyProductList.add(pro);
                    }
                }
            }
        }

//        将历史记录中的集合放到域中
        request.setAttribute("historyProductList", historyProductList);

        request.getRequestDispatcher("/product_list.jsp").forward(request, response);
    }


}
