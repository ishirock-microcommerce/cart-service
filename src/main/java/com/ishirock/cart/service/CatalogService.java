package com.ishirock.cart.service;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import com.ishirock.cart.model.Product;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@Path("/catalog")
@RegisterRestClient
public interface CatalogService {

    @GET
    @Path("/{id}")
    @Produces("application/json")
    public List<Product> getProduct (@org.jboss.resteasy.annotations.jaxrs.PathParam String id);
    
}
