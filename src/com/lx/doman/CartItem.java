package com.lx.doman;

public class CartItem {
//    商品对象
    private Product product;
//    商品数量
    private int buyNum;
//    商品小计
    private double subtotal;

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getBuyNum() {
        return buyNum;
    }

    public void setBuyNum(int buyNum) {
        this.buyNum = buyNum;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtoatl(double subtoatl) {
        this.subtotal = subtoatl;
    }
}
