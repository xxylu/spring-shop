package org.shop.services.cart;

import org.shop.models.cart.Cart;
import org.shop.models.user.Role;
import org.shop.models.user.User;
import org.shop.repositories.cart.CartRepository;
import org.shop.repositories.cart.ICartRepository;
import org.shop.repositories.order.IOrderRepository;
import org.shop.repositories.order.OrderRepository;
import org.shop.repositories.product.IProductRepository;
import org.shop.repositories.product.ProductRepository;
import org.shop.repositories.user.IUserRepository;
import org.shop.repositories.user.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CartService implements ICartService {
    IUserRepository userRepository = new UserRepository();
    IOrderRepository orderRepository = new OrderRepository();
    ICartRepository cartRepository = new CartRepository();
    IProductRepository productRepository = new ProductRepository();

    @Override
    public String createCart(String userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent() && user.get().getRoles() != Role.NOTACTIVE && !cartRepository.findCartByUserId(userId).isPresent()) {
            System.out.println("Utworzono koszyk");
            return cartRepository.createCart(userId);
        }
        System.out.println("User nie istnieje lub jest nieaktywny, lub koszyk  został utworzony");
        return null;
    }

    @Override
    public void deleteCart(String userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            cartRepository.deleteCart(userId);
            System.out.println("Usunięto koszyk użytkownika");
        } else {
            System.out.println("Nie znaleziono użytkownika, koszyk nie został usunięty");
        }
    }

    @Override
    public void addToCart(String userId, String cartId, String productId) {
        Optional<Cart> cartOpt = cartRepository.findCartById(cartId);

        if (cartOpt.isPresent() && cartOpt.get().getUserId().equals(userId)) {
            Cart cart = cartOpt.get();

            // Pobierz aktualną listę productId
            List<String> productIds = cart.convertFromJson();
            productIds.add(productId);

            // Zapisz nową listę
            cartRepository.modifyCart(cartId, productIds);
            System.out.println("Dodano produkt do koszyka");
        } else {
            System.out.println("Nie znaleziono koszyka lub koszyk nie należy do użytkownika");
        }
    }

    @Override
    public void removeFromCart(String userId, String cartId, String productId) {
        Optional<Cart> cartOpt = cartRepository.findCartById(cartId);

        if (cartOpt.isPresent() && cartOpt.get().getUserId().equals(userId)) {
            Cart cart = cartOpt.get();

            List<String> productIds = cart.convertFromJson();
            boolean removed = productIds.remove(productId);

            if (removed) {
                cartRepository.modifyCart(cartId, productIds);
                System.out.println("Usunięto produkt z koszyka");
            } else {
                System.out.println("Produkt nie był w koszyku");
            }
        } else {
            System.out.println("Nie znaleziono koszyka lub koszyk nie należy do użytkownika");
        }
    }

    @Override
    public void clearCart(String cartId) {
        Optional<Cart> cartOpt = cartRepository.findCartById(cartId);

        if (cartOpt.isPresent()) {
            // Ustawiamy pustą listę
            cartRepository.modifyCart(cartId, List.of());
            System.out.println("Wyczyszczono koszyk");
        } else {
            System.out.println("Nie znaleziono koszyka");
        }
    }

    public String getCartId(String userId) {
        Optional<Cart> cartOpt = cartRepository.findCartByUserId(userId);
        return cartOpt.map(Cart::getCartid).orElse(null);
    }

    @Override
    public Optional<Cart> getCart(String userId) {
        return cartRepository.findCartByUserId(userId);
    }
}
