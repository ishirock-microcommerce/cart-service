package com.ishirock.cart;

import javax.inject.Inject;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.ishirock.cart.model.ShoppingCart;
import com.ishirock.cart.service.ShoppingCartService;

@Path("/cart")
public class CartResource {

    
    @Inject
    private ShoppingCartService shoppingCartService;


    @Path("/{id}")
    @Produces({MediaType.APPLICATION_JSON})
    @GET
    public ShoppingCart getCart(@PathParam("id") String cartId) {
        return shoppingCartService.getShoppingCart(cartId);
    }

    @Path("/{cartId}/{itemId}/{quantity}")
    @POST
    @Produces({MediaType.APPLICATION_JSON})
    public ShoppingCart add(@PathParam("cartId") String cartId, @PathParam("itemId") String itemId, @PathParam("quantity") int quantity) throws Exception {
        return shoppingCartService.addToCart(cartId, itemId, quantity);
    }

    @DELETE
    @Path("/{cartId}/{itemId}/{quantity}")
    @Produces({MediaType.APPLICATION_JSON})
    public ShoppingCart delete(@PathParam("cartId") String cartId, @PathParam("itemId") String itemId, @PathParam("quantity") int quantity) throws Exception {
        return shoppingCartService.removeFromCart(cartId, itemId, quantity);
    }
    
    @POST
    @Path("/checkout/{cartId}")
    @Produces({MediaType.APPLICATION_JSON})
    public ShoppingCart checkout(@PathParam("cartId") String cartId) {
        ShoppingCart cart = shoppingCartService.checkoutShoppingCart(cartId);
        return cart;
    }
}