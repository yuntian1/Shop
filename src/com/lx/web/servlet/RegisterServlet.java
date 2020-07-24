package com.lx.web.servlet;

import com.lx.doman.User;
import com.lx.service.UserService;
import com.lx.utils.CommonsUtils;
import com.lx.utils.MailUtils;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.Converter;

import javax.mail.MessagingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        //获得表单数据
        Map<String, String[]> properties = request.getParameterMap();
        User user = new User();
        try {
            //自己制定一个类型装换器（将String转换成Date）
            ConvertUtils.register(new Converter() {
                @Override
                public Object convert(Class clazz, Object value) {
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                    Date parse = null;
                    try {
                        parse = format.parse(value.toString());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    return parse;
                }
            }, Date.class);
            //映射封装
            BeanUtils.populate(user, properties);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        //private String uid;
        user.setUid(CommonsUtils.getUUID());
        //private String telephone;
        user.setTelephone(null);
        //private int state;//是否激活
        user.setState(0);
        //private String code;//激活码
        String activeCode = CommonsUtils.getUUID();
        user.setCode(activeCode);

        //将user传递给service层
        UserService service = new UserService();
        boolean isRegisterSuccess = service.regist(user);

        //是否注册成功
        if (isRegisterSuccess) {
            //发送激活邮件
            String emailMsg = "恭喜您注册成功，请点击下面的链接进行激活账户+" +
                    "<a href='http://localhost:8080/LxShop/active?activeCode="+activeCode+"' >"
                    +"http://localhost:8080/LxShop/active?activeCode="+activeCode+"</a>";
            try {
                MailUtils.sendMail(user.getEmail(),emailMsg);
            } catch (MessagingException e) {
                e.printStackTrace();
            }
            //跳转到注册成功界面
            response.sendRedirect(request.getContextPath() + "/registerSuccess.jsp");
        } else {
            //跳转到失败页面
            response.sendRedirect(request.getContextPath() + "/registerFail.jsp");

        }
    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }
}
