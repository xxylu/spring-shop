package org.shop.services.cart;

import org.shop.models.cart.Cart;
import org.shop.models.product.Product;

public interface ICartService {
    String createCart(String userId);
    void deleteCart(String userId);
    void addToCart(String userId, String cartId, String productId);
    void removeFromCart(String userId, String cartId, String productId);
    void clearCart(String cartId);
    String getCartId(String userId);
}
