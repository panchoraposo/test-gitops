package com.neuralbank.infrastructure.rest;

import com.neuralbank.application.service.CreditApprovalService;
import com.neuralbank.infrastructure.rest.dto.request.CreditApprovalRequest;
import com.neuralbank.infrastructure.rest.dto.request.CreateCreditRequest;
import com.neuralbank.infrastructure.rest.dto.response.CreditApprovalResponse;
import com.neuralbank.infrastructure.rest.dto.response.CreditResponse;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.util.List;
import java.util.Map;

@Path("/api/v1/credit-approvals")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Credit Approval Service", description = "Gestión de aprobación de créditos")
public class CreditApprovalResource {
    
    @Inject
    CreditApprovalService creditApprovalService;
    
    @POST
    @Path("/evaluate")
    @Operation(summary = "Evaluar aprobación de crédito", 
               description = "Evalúa una solicitud de crédito aplicando las reglas de capacidad y willingness to pay")
    @APIResponse(responseCode = "200", description = "Evaluación completada")
    @APIResponse(responseCode = "404", description = "Crédito o cliente no encontrado")
    public Response evaluateApproval(CreditApprovalRequest request) {
        CreditApprovalResponse response = creditApprovalService.evaluateCreditApproval(request);
        return Response.ok(response).build();
    }
    
    @POST
    @Path("/{creditId}/approve")
    @Operation(summary = "Aprobar crédito")
    @APIResponse(responseCode = "200", description = "Crédito aprobado")
    @APIResponse(responseCode = "404", description = "Crédito no encontrado")
    @APIResponse(responseCode = "400", description = "El crédito no cumple con las reglas de aprobación")
    public Response approveCredit(@PathParam("creditId") Long creditId) {
        CreditApprovalResponse response = creditApprovalService.approveCredit(creditId);
        return Response.ok(response).build();
    }
    
    @POST
    @Path("/{creditId}/reject")
    @Operation(summary = "Rechazar crédito")
    @APIResponse(responseCode = "200", description = "Crédito rechazado")
    @APIResponse(responseCode = "404", description = "Crédito no encontrado")
    public Response rejectCredit(@PathParam("creditId") Long creditId, Map<String, String> request) {
        String reason = request.get("reason");
        CreditApprovalResponse response = creditApprovalService.rejectCredit(creditId, reason);
        return Response.ok(response).build();
    }
    
    @POST
    @Path("/credits")
    @Operation(summary = "Crear nueva solicitud de crédito")
    @APIResponse(responseCode = "201", description = "Solicitud de crédito creada")
    @APIResponse(responseCode = "400", description = "Datos inválidos")
    public Response createCreditRequest(CreateCreditRequest request) {
        CreditResponse response = creditApprovalService.createCreditRequest(request);
        return Response.status(Response.Status.CREATED).entity(response).build();
    }
    
    @GET
    @Path("/credits/{creditId}")
    @Operation(summary = "Obtener crédito por ID")
    @APIResponse(responseCode = "200", description = "Crédito encontrado")
    @APIResponse(responseCode = "404", description = "Crédito no encontrado")
    public Response getCredit(@PathParam("creditId") Long creditId) {
        CreditResponse response = creditApprovalService.getCreditById(creditId);
        return Response.ok(response).build();
    }
    
    @GET
    @Path("/credits/cliente/{clienteId}")
    @Operation(summary = "Obtener créditos de un cliente")
    @APIResponse(responseCode = "200", description = "Lista de créditos")
    public Response getCreditsByCliente(@PathParam("clienteId") Long clienteId) {
        List<CreditResponse> response = creditApprovalService.getCreditsByClienteId(clienteId);
        return Response.ok(response).build();
    }
    
    @GET
    @Path("/credits/status/{status}")
    @Operation(summary = "Obtener créditos por estado")
    @APIResponse(responseCode = "200", description = "Lista de créditos")
    public Response getCreditsByStatus(@PathParam("status") String status) {
        List<CreditResponse> response = creditApprovalService.getCreditsByStatus(status);
        return Response.ok(response).build();
    }
    
    @POST
    @Path("/{creditId}/recalculate")
    @Operation(summary = "Recalcular aprobación de crédito")
    @APIResponse(responseCode = "200", description = "Aprobación recalculada")
    @APIResponse(responseCode = "404", description = "Crédito no encontrado")
    public Response recalculateApproval(@PathParam("creditId") Long creditId) {
        CreditApprovalResponse response = creditApprovalService.recalculateApproval(creditId);
        return Response.ok(response).build();
    }
}

