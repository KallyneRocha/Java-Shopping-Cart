package br.com.shoppingcart.model;

import br.com.shoppingcart.dao.ProductDAO;
import br.com.shoppingcart.dao.ShoppingCartDAO;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ShoppingCartModel {
    private List<CartItemModel> cartItems;

    public ShoppingCartModel() {
        cartItems = new ArrayList<>();
    }

    public void addProduct(ProductModel product, int quantity) {
        CartItemModel cartItem = new CartItemModel(product, quantity);
        cartItems.add(cartItem);
    }

    public void removeProduct(ProductModel product) {
        CartItemModel cartItemToRemove = null;
        for (CartItemModel cartItem : cartItems) {
            if (cartItem.getProduct().equals(product)) {
                cartItemToRemove = cartItem;
                break;
            }
        }
        if (cartItemToRemove != null) {
            cartItems.remove(cartItemToRemove);
        }
    }

    public double calculatePrice(ShoppingCartDAO cartDAO, ProductDAO productDAO) throws SQLException {
        double totalPrice = 0;
        List<CartItemModel> cartItems = cartDAO.getAll();

        for (CartItemModel cartItem : cartItems) {
            int productId = cartItem.getProduct().getId();
            int quantity = cartItem.getQuantity();

            ProductModel product = productDAO.getProductById(productId);
            if (product != null) {
                double itemPrice = product.getPrice();
                double totalItemPrice = itemPrice * quantity;
                totalPrice += totalItemPrice;
            }
        }

        return totalPrice;
    }


    public void clearCart() {
        cartItems.clear();
    }

}

