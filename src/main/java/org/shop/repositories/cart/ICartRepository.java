package org.shop.repositories.cart;

import org.shop.models.cart.Cart;
import org.shop.models.product.Product;

import java.util.List;
import java.util.Optional;

public interface ICartRepository {
    Optional<Cart> findCartById(String cartId);
    Optional<Cart> findCartByUserId(String userId);
    void modifyCart(String cartId, List<String> productIds);
    String createCart(String userId);
    void deleteCart(String userId);
}

