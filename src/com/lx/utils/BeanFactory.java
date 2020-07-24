package com.lx.utils;

import org.dom4j.*;
import org.dom4j.io.SAXReader;

public class BeanFactory {
    public static Object getBean(String id){
    //生产对象---根据清单生产----配置文件--将每一个bean生产的细节配置到文件中
    //    使用dom4j的xml解析技术

        try {
            SAXReader reader = new SAXReader();
            String path = BeanFactory.class.getClassLoader().getResource("bean.xml").getPath();
            Document doc = reader.read(path);
            //获得元素，参数是xpath规则
            Element element = (Element) doc.selectSingleNode("//bean[@id='" + id + "']");
            String className = element.attributeValue("class");
            Class clazz = Class.forName(className);
            Object object = clazz.newInstance();
            return object;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
