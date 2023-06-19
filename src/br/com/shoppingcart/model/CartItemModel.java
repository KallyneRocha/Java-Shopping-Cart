package br.com.shoppingcart.model;

public class CartItemModel {
    private int id;
    private ProductModel product;
    private int quantity;

    public CartItemModel(ProductModel product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public ProductModel getProduct() {
            return product;
    }
    public void setProduct(ProductModel product) {
        this.product = product;
    }
    public int getQuantity() {
        return quantity;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
