package com.neuralbank.infrastructure.rest;

import com.neuralbank.application.service.CustomerService;
import com.neuralbank.infrastructure.rest.dto.request.CreateCustomerRequest;
import com.neuralbank.infrastructure.rest.dto.request.UpdateCustomerRequest;
import com.neuralbank.infrastructure.rest.dto.request.CustomerSearchRequest;
import com.neuralbank.infrastructure.rest.dto.response.CustomerResponse;
import com.neuralbank.infrastructure.rest.dto.response.CustomerSummaryResponse;
import com.neuralbank.infrastructure.rest.dto.response.CreditScoreResponse;
import com.neuralbank.infrastructure.rest.dto.response.PageResponse;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.util.Map;

@Path("/api/v1/customers")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Customer Service", description = "Gestión de clientes")
public class CustomerResource {
    
    @Inject
    CustomerService customerService;
    
    @POST
    @Operation(summary = "Crear nuevo cliente")
    @APIResponse(responseCode = "201", description = "Cliente creado exitosamente")
    @APIResponse(responseCode = "400", description = "Datos inválidos")
    @APIResponse(responseCode = "409", description = "Cliente duplicado")
    public Response createCustomer(CreateCustomerRequest request) {
        CustomerResponse response = customerService.createCustomer(request);
        return Response.status(Response.Status.CREATED).entity(response).build();
    }
    
    @GET
    @Path("/{customerId}")
    @Operation(summary = "Obtener cliente por ID")
    @APIResponse(responseCode = "200", description = "Cliente encontrado")
    @APIResponse(responseCode = "404", description = "Cliente no encontrado")
    public Response getCustomer(@PathParam("customerId") Long customerId) {
        CustomerResponse response = customerService.getCustomerById(customerId);
        return Response.ok(response).build();
    }
    
    @GET
    @Path("/identification/{identificacion}")
    @Operation(summary = "Buscar cliente por identificación")
    @APIResponse(responseCode = "200", description = "Cliente encontrado")
    @APIResponse(responseCode = "404", description = "Cliente no encontrado")
    public Response getCustomerByIdentificacion(@PathParam("identificacion") String identificacion) {
        CustomerResponse response = customerService.getCustomerByIdentificacion(identificacion);
        return Response.ok(response).build();
    }
    
    @PUT
    @Path("/{customerId}")
    @Operation(summary = "Actualizar cliente")
    @APIResponse(responseCode = "200", description = "Cliente actualizado")
    @APIResponse(responseCode = "404", description = "Cliente no encontrado")
    public Response updateCustomer(@PathParam("customerId") Long customerId, UpdateCustomerRequest request) {
        CustomerResponse response = customerService.updateCustomer(customerId, request);
        return Response.ok(response).build();
    }
    
    @PATCH
    @Path("/{customerId}")
    @Operation(summary = "Actualización parcial de cliente")
    @APIResponse(responseCode = "200", description = "Cliente actualizado")
    @APIResponse(responseCode = "404", description = "Cliente no encontrado")
    public Response patchCustomer(@PathParam("customerId") Long customerId, Map<String, Object> updates) {
        CustomerResponse response = customerService.patchCustomer(customerId, updates);
        return Response.ok(response).build();
    }
    
    @DELETE
    @Path("/{customerId}")
    @Operation(summary = "Eliminar cliente (soft delete)")
    @APIResponse(responseCode = "204", description = "Cliente eliminado")
    @APIResponse(responseCode = "404", description = "Cliente no encontrado")
    public Response deleteCustomer(@PathParam("customerId") Long customerId) {
        customerService.deleteCustomer(customerId);
        return Response.noContent().build();
    }
    
    @GET
    @Operation(summary = "Buscar clientes con filtros")
    @APIResponse(responseCode = "200", description = "Lista de clientes")
    public Response searchCustomers(
            @BeanParam CustomerSearchRequest searchRequest,
            @QueryParam("page") @DefaultValue("0") int page,
            @QueryParam("size") @DefaultValue("20") int size) {
        
        PageResponse<CustomerResponse> response = customerService.searchCustomers(searchRequest, page, size);
        return Response.ok(response).build();
    }
    
    @GET
    @Path("/{customerId}/credit-score")
    @Operation(summary = "Obtener score crediticio")
    @APIResponse(responseCode = "200", description = "Score obtenido")
    @APIResponse(responseCode = "404", description = "Cliente no encontrado")
    public Response getCreditScore(@PathParam("customerId") Long customerId) {
        CreditScoreResponse response = customerService.getCreditScore(customerId);
        return Response.ok(response).build();
    }
    
    @POST
    @Path("/{customerId}/credit-score/calculate")
    @Operation(summary = "Recalcular score crediticio")
    @APIResponse(responseCode = "200", description = "Score calculado")
    @APIResponse(responseCode = "404", description = "Cliente no encontrado")
    public Response calculateCreditScore(@PathParam("customerId") Long customerId) {
        CreditScoreResponse response = customerService.calculateCreditScore(customerId);
        return Response.ok(response).build();
    }
    
    @PUT
    @Path("/{customerId}/risk-level")
    @Operation(summary = "Actualizar nivel de riesgo")
    @APIResponse(responseCode = "204", description = "Nivel actualizado")
    @APIResponse(responseCode = "404", description = "Cliente no encontrado")
    public Response updateRiskLevel(
            @PathParam("customerId") Long customerId,
            Map<String, String> request) {
        
        String nivelRiesgo = request.get("nivelRiesgo");
        String justificacion = request.get("justificacion");
        customerService.updateRiskLevel(customerId, nivelRiesgo, justificacion);
        return Response.noContent().build();
    }
    
    @GET
    @Path("/{customerId}/summary")
    @Operation(summary = "Resumen completo del cliente")
    @APIResponse(responseCode = "200", description = "Resumen obtenido")
    @APIResponse(responseCode = "404", description = "Cliente no encontrado")
    public Response getCustomerSummary(@PathParam("customerId") Long customerId) {
        CustomerSummaryResponse response = customerService.getCustomerSummary(customerId);
        return Response.ok(response).build();
    }
    
    @POST
    @Path("/{customerId}/activate")
    @Operation(summary = "Activar cliente")
    @APIResponse(responseCode = "204", description = "Cliente activado")
    @APIResponse(responseCode = "404", description = "Cliente no encontrado")
    public Response activateCustomer(@PathParam("customerId") Long customerId) {
        customerService.activateCustomer(customerId);
        return Response.noContent().build();
    }
    
    @POST
    @Path("/{customerId}/deactivate")
    @Operation(summary = "Desactivar cliente")
    @APIResponse(responseCode = "204", description = "Cliente desactivado")
    @APIResponse(responseCode = "404", description = "Cliente no encontrado")
    public Response deactivateCustomer(@PathParam("customerId") Long customerId, Map<String, String> request) {
        String motivo = request.get("motivo");
        customerService.deactivateCustomer(customerId, motivo);
        return Response.noContent().build();
    }
    
    @POST
    @Path("/{customerId}/block")
    @Operation(summary = "Bloquear cliente")
    @APIResponse(responseCode = "204", description = "Cliente bloqueado")
    @APIResponse(responseCode = "404", description = "Cliente no encontrado")
    public Response blockCustomer(@PathParam("customerId") Long customerId, Map<String, String> request) {
        String motivo = request.get("motivo");
        String comentarios = request.get("comentarios");
        customerService.blockCustomer(customerId, motivo, comentarios);
        return Response.noContent().build();
    }
    
    @POST
    @Path("/{customerId}/unblock")
    @Operation(summary = "Desbloquear cliente")
    @APIResponse(responseCode = "204", description = "Cliente desbloqueado")
    @APIResponse(responseCode = "404", description = "Cliente no encontrado")
    public Response unblockCustomer(@PathParam("customerId") Long customerId) {
        customerService.unblockCustomer(customerId);
        return Response.noContent().build();
    }
    
    @PUT
    @Path("/{customerId}/email")
    @Operation(summary = "Actualizar email")
    @APIResponse(responseCode = "204", description = "Email actualizado")
    @APIResponse(responseCode = "404", description = "Cliente no encontrado")
    public Response updateEmail(@PathParam("customerId") Long customerId, Map<String, String> request) {
        String email = request.get("email");
        customerService.updateEmail(customerId, email);
        return Response.noContent().build();
    }
    
    @PUT
    @Path("/{customerId}/phone")
    @Operation(summary = "Actualizar teléfono")
    @APIResponse(responseCode = "204", description = "Teléfono actualizado")
    @APIResponse(responseCode = "404", description = "Cliente no encontrado")
    public Response updatePhone(@PathParam("customerId") Long customerId, Map<String, String> request) {
        String telefono = request.get("telefono");
        customerService.updatePhone(customerId, telefono);
        return Response.noContent().build();
    }
    
    @PUT
    @Path("/{customerId}/address")
    @Operation(summary = "Actualizar dirección")
    @APIResponse(responseCode = "204", description = "Dirección actualizada")
    @APIResponse(responseCode = "404", description = "Cliente no encontrado")
    public Response updateAddress(@PathParam("customerId") Long customerId, Map<String, String> request) {
        String direccion = request.get("direccion");
        String ciudad = request.get("ciudad");
        String estadoProvincia = request.get("estadoProvincia");
        String codigoPostal = request.get("codigoPostal");
        customerService.updateAddress(customerId, direccion, ciudad, estadoProvincia, codigoPostal);
        return Response.noContent().build();
    }
    
    @PUT
    @Path("/{customerId}/executive/{executiveId}")
    @Operation(summary = "Asignar ejecutivo")
    @APIResponse(responseCode = "204", description = "Ejecutivo asignado")
    @APIResponse(responseCode = "404", description = "Cliente no encontrado")
    public Response assignExecutive(
            @PathParam("customerId") Long customerId,
            @PathParam("executiveId") Long executiveId) {
        
        customerService.assignExecutive(customerId, executiveId);
        return Response.noContent().build();
    }
    
    @GET
    @Path("/{customerId}/metadata")
    @Operation(summary = "Obtener metadata del cliente")
    @APIResponse(responseCode = "200", description = "Metadata obtenida")
    @APIResponse(responseCode = "404", description = "Cliente no encontrado")
    public Response getMetadata(@PathParam("customerId") Long customerId) {
        Map<String, Object> metadata = customerService.getMetadata(customerId);
        return Response.ok(metadata).build();
    }
    
    @PUT
    @Path("/{customerId}/metadata")
    @Operation(summary = "Actualizar metadata completa")
    @APIResponse(responseCode = "204", description = "Metadata actualizada")
    @APIResponse(responseCode = "404", description = "Cliente no encontrado")
    public Response updateMetadata(@PathParam("customerId") Long customerId, Map<String, Object> metadata) {
        customerService.updateMetadata(customerId, metadata);
        return Response.noContent().build();
    }
    
    @PATCH
    @Path("/{customerId}/metadata/{key}")
    @Operation(summary = "Actualizar campo específico de metadata")
    @APIResponse(responseCode = "204", description = "Campo actualizado")
    @APIResponse(responseCode = "404", description = "Cliente no encontrado")
    public Response updateMetadataField(
            @PathParam("customerId") Long customerId,
            @PathParam("key") String key,
            Object value) {
        
        customerService.updateMetadataField(customerId, key, value);
        return Response.noContent().build();
    }
}