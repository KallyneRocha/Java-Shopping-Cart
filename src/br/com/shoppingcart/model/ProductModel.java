package br.com.shoppingcart.model;

public class ProductModel {

    private int id;
    private String name;
    private double price;
    private int quantityAvailable;

    public ProductModel(String name, double price, int quantityAvailable) {
        // Validação das entradas
        if (name == null || price <= 0 || quantityAvailable < 0) {
            throw new IllegalArgumentException("Sorry, something went wrong. Please, check the product information and try again.");
        }

        this.name = name;
        this.price = price;
        this.quantityAvailable = quantityAvailable;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantityAvailable() {
        return quantityAvailable;
    }

    public void setQuantityAvailable(int quantityAvailable) {
        this.quantityAvailable = quantityAvailable;
    }
}
