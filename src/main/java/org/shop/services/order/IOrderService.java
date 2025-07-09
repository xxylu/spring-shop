package org.shop.services.order;

import org.shop.models.order.Order;
import org.shop.models.product.Product;

public interface IOrderService {
    void updateOrder(Order Order, String userId);
    void deleteOrder(String orderId, String userId);
    void addProductToOrder(String orderId, String userId, Product product);
    Boolean deleteProductFromOrder(String orderId, String userId, Product product);
    Boolean isBelongsToUser(String orderId, String userId);
}
