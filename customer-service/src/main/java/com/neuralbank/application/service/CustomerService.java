package com.neuralbank.application.service;

import com.neuralbank.infrastructure.rest.dto.request.CreateCustomerRequest;
import com.neuralbank.infrastructure.rest.dto.request.UpdateCustomerRequest;
import com.neuralbank.infrastructure.rest.dto.request.CustomerSearchRequest;
import com.neuralbank.infrastructure.rest.dto.response.CustomerResponse;
import com.neuralbank.infrastructure.rest.dto.response.CustomerSummaryResponse;
import com.neuralbank.infrastructure.rest.dto.response.CreditScoreResponse;
import com.neuralbank.infrastructure.rest.dto.response.PageResponse;

import java.util.Map;

public interface CustomerService {
    
    CustomerResponse createCustomer(CreateCustomerRequest request);
    
    CustomerResponse getCustomerById(Long id);
    
    CustomerResponse getCustomerByIdentificacion(String identificacion);
    
    CustomerResponse updateCustomer(Long id, UpdateCustomerRequest request);
    
    CustomerResponse patchCustomer(Long id, Map<String, Object> updates);
    
    void deleteCustomer(Long id);
    
    PageResponse<CustomerResponse> searchCustomers(CustomerSearchRequest request, int page, int size);
    
    CreditScoreResponse getCreditScore(Long customerId);
    
    CreditScoreResponse calculateCreditScore(Long customerId);
    
    void updateRiskLevel(Long customerId, String nivelRiesgo, String justificacion);
    
    CustomerSummaryResponse getCustomerSummary(Long customerId);
    
    void activateCustomer(Long id);
    
    void deactivateCustomer(Long id, String motivo);
    
    void blockCustomer(Long id, String motivo, String comentarios);
    
    void unblockCustomer(Long id);
    
    void updateEmail(Long id, String email);
    
    void updatePhone(Long id, String telefono);
    
    void updateAddress(Long id, String direccion, String ciudad, String estadoProvincia, String codigoPostal);
    
    void assignExecutive(Long customerId, Long executiveId);
    
    Map<String, Object> getMetadata(Long customerId);
    
    void updateMetadata(Long customerId, Map<String, Object> metadata);
    
    void updateMetadataField(Long customerId, String key, Object value);
}