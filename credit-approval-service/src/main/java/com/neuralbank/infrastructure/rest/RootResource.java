package com.neuralbank.infrastructure.rest;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.Map;

@Path("")
@Produces(MediaType.APPLICATION_JSON)
public class RootResource {

    @GET
    public Response root() {
        Map<String, Object> body = Map.of(
            "name", "NeuralBank Credit Approval Service",
            "status", "OK",
            "version", "1.0.0",
            "endpoints", Map.of(
                "health", "/q/health",
                "openapi", "/q/openapi",
                "swagger", "/swagger-ui",
                "credit-approvals", "/api/v1/credit-approvals"
            )
        );
        return Response.ok(body).build();
    }
}

