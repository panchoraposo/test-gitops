package com.neuralbank.client;

import com.neuralbank.dto.request.CreditApprovalRequest;
import com.neuralbank.dto.request.CreateCreditRequest;
import com.neuralbank.dto.response.CreditApprovalResponse;
import com.neuralbank.dto.response.CreditResponse;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import java.util.List;
import java.util.Map;

@Path("/api/v1/credit-approvals")
@RegisterRestClient(configKey = "creditapprovalclient")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface CreditApprovalClient {

    // ============================================
    // CREDIT APPROVAL OPERATIONS
    // ============================================

    /**
     * Evaluate credit approval
     * POST /api/v1/credit-approvals/evaluate
     */
    @POST
    @Path("/evaluate")
    CreditApprovalResponse evaluateApproval(CreditApprovalRequest request);

    /**
     * Approve credit
     * POST /api/v1/credit-approvals/{creditId}/approve
     */
    @POST
    @Path("/{creditId}/approve")
    CreditApprovalResponse approveCredit(@PathParam("creditId") Long creditId);

    /**
     * Reject credit
     * POST /api/v1/credit-approvals/{creditId}/reject
     */
    @POST
    @Path("/{creditId}/reject")
    CreditApprovalResponse rejectCredit(
            @PathParam("creditId") Long creditId,
            Map<String, String> request
    );

    /**
     * Recalculate approval
     * POST /api/v1/credit-approvals/{creditId}/recalculate
     */
    @POST
    @Path("/{creditId}/recalculate")
    CreditApprovalResponse recalculateApproval(@PathParam("creditId") Long creditId);

    // ============================================
    // CREDIT CRUD OPERATIONS
    // ============================================

    /**
     * Create credit request
     * POST /api/v1/credit-approvals/credits
     */
    @POST
    @Path("/credits")
    CreditResponse createCreditRequest(CreateCreditRequest request);

    /**
     * Get credit by ID
     * GET /api/v1/credit-approvals/credits/{creditId}
     */
    @GET
    @Path("/credits/{creditId}")
    CreditResponse getCreditById(@PathParam("creditId") Long creditId);

    /**
     * Get credits by cliente ID
     * GET /api/v1/credit-approvals/credits/cliente/{clienteId}
     */
    @GET
    @Path("/credits/cliente/{clienteId}")
    List<CreditResponse> getCreditsByCliente(@PathParam("clienteId") Long clienteId);

    /**
     * Get credits by status
     * GET /api/v1/credit-approvals/credits/status/{status}
     */
    @GET
    @Path("/credits/status/{status}")
    List<CreditResponse> getCreditsByStatus(@PathParam("status") String status);
}

