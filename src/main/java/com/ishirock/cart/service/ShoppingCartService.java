package com.ishirock.cart.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import com.ishirock.cart.model.Product;
import com.ishirock.cart.model.ShoppingCart;
import com.ishirock.cart.model.ShoppingCartItem;

import org.eclipse.microprofile.rest.client.inject.RestClient;



@ApplicationScoped
public class ShoppingCartService {

    @Inject
    @RestClient
    private CatalogService catalogService;

    @Inject
    private PriceCalculationService priceCalculationService;

    private final Map<String, ShoppingCart> cartDB = new HashMap<>();

    public ShoppingCart calculateCartPrice(final ShoppingCart sc) {
        priceCalculationService.priceShoppingCart(sc);
        cartDB.put(sc.getId(), sc);
        return sc;
    }

    public ShoppingCart getShoppingCart(final String cartId) {
        ShoppingCart sc = cartDB.get(cartId);
        if (sc == null) {
            sc = new ShoppingCart();
            sc.setId(cartId);
            cartDB.put(cartId, sc);
        }
        return sc;
    }

    public ShoppingCart addToCart(final String cartId, final String itemId, final int quantity) {
        final ShoppingCart sc = getShoppingCart(cartId);
        if (quantity <= 0) {
            return sc;
        }
        Product product;
        product = (Product) getProduct(itemId);
        if (product == null) {
            return sc;
        }
        final Optional<ShoppingCartItem> cartItem = sc.getShoppingCartItemList().stream()
                .filter(sci -> sci.getProduct().getId().equals(itemId)).findFirst();
        if (cartItem.isPresent()) {
            cartItem.get().setQuantity(cartItem.get().getQuantity() + quantity);
        } else {
            final ShoppingCartItem newCartItem = new ShoppingCartItem();
            newCartItem.setProduct(product);
            newCartItem.setQuantity(quantity);
            newCartItem.setPrice(product.getPrice());
            sc.addShoppingCartItem(newCartItem);
        }
        calculateCartPrice(sc);
        cartDB.put(sc.getId(), sc);
        return sc;
    }

    public ShoppingCart removeFromCart(final String cartId, final String itemId, final int quantity) {
        final ShoppingCart sc = getShoppingCart(cartId);
        if (quantity <= 0) {
            return sc;
        }
        final Optional<ShoppingCartItem> cartItem = sc.getShoppingCartItemList().stream()
                .filter(sci -> sci.getProduct().getId().equals(itemId)).findFirst();
        if (cartItem.isPresent()) {
            if (cartItem.get().getQuantity() <= quantity) {
                sc.removeShoppingCartItem(cartItem.get());
            } else {
                cartItem.get().setQuantity(cartItem.get().getQuantity() - quantity);
            }
        }
        calculateCartPrice(sc);
        cartDB.put(sc.getId(), sc);
        return sc;
    }

    public ShoppingCart checkoutShoppingCart(final String cartId) {
        final ShoppingCart sc = getShoppingCart(cartId);
        return sc;
    }

    private Product getProduct(String itemId) {        
        return catalogService.getProduct(itemId).get(0);
    }
}