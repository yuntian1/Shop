package com.lx.doman;

import java.util.Date;

public class Product {
    //商品id
    private String pid;
    //商品名
    private String pname;
    //市场价
    private Double market_price;
    //店铺价
    private Double shop_price;
    //    商品照片
    private String pimage;
    //上架日期
    private Date pdate;
//    是否热门
    private int is_hot;
//    商品详情
    private String pdesc;
//    商品标记
    private int pflag;
//    商品种类
    private Category category;

    public String getPimage() {
        return pimage;
    }

    public void setPimage(String pimage) {
        this.pimage = pimage;
    }


    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public Double getMarket_price() {
        return market_price;
    }

    public void setMarket_price(Double market_price) {
        this.market_price = market_price;
    }

    public Double getShop_price() {
        return shop_price;
    }

    public void setShop_price(Double shop_price) {
        this.shop_price = shop_price;
    }

    public Date getPdate() {
        return pdate;
    }

    public void setPdate(Date pdate) {
        this.pdate = pdate;
    }

    public int getIs_hot() {
        return is_hot;
    }

    public void setIs_hot(int is_hot) {
        this.is_hot = is_hot;
    }

    public String getPdesc() {
        return pdesc;
    }

    public void setPdesc(String desc) {
        this.pdesc = desc;
    }

    public int getPflag() {
        return pflag;
    }

    public void setPflag(int pflag) {
        this.pflag = pflag;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}
