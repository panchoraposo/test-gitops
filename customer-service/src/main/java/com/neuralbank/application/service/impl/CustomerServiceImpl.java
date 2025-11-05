package com.neuralbank.application.service.impl;

import com.neuralbank.application.service.CustomerService;
import com.neuralbank.application.mapper.CustomerMapper;
import com.neuralbank.application.validation.CustomerValidator;
import com.neuralbank.domain.entity.Customer;
import com.neuralbank.domain.repository.CustomerRepository;
import com.neuralbank.infrastructure.exception.CustomerNotFoundException;
import com.neuralbank.infrastructure.exception.DuplicateCustomerException;
import com.neuralbank.infrastructure.rest.dto.request.CreateCustomerRequest;
import com.neuralbank.infrastructure.rest.dto.request.UpdateCustomerRequest;
import com.neuralbank.infrastructure.rest.dto.request.CustomerSearchRequest;
import com.neuralbank.infrastructure.rest.dto.response.CustomerResponse;
import com.neuralbank.infrastructure.rest.dto.response.CustomerSummaryResponse;
import com.neuralbank.infrastructure.rest.dto.response.CreditScoreResponse;
import com.neuralbank.infrastructure.rest.dto.response.PageResponse;
import com.neuralbank.shared.util.ScoreCalculator;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ApplicationScoped
public class CustomerServiceImpl implements CustomerService {
    
    @Inject
    CustomerRepository customerRepository;
    
    @Inject
    CustomerMapper customerMapper;
    
    @Inject
    CustomerValidator customerValidator;
    
    @Inject
    ScoreCalculator scoreCalculator;
    
    @Override
    @Transactional
    public CustomerResponse createCustomer(CreateCustomerRequest request) {
        customerValidator.validateCreateRequest(request);
        
        if (customerRepository.findByIdentificacion(request.identificacion).isPresent()) {
            throw new DuplicateCustomerException("Cliente con identificación " + request.identificacion + " ya existe");
        }
        
        if (request.email != null && customerRepository.findByEmail(request.email).isPresent()) {
            throw new DuplicateCustomerException("Cliente con email " + request.email + " ya existe");
        }
        
        Customer customer = customerMapper.toEntity(request);
        customerRepository.persist(customer);
        
        return customerMapper.toResponse(customer);
    }
    
    @Override
    public CustomerResponse getCustomerById(Long id) {
        Customer customer = customerRepository.findByIdOptional(id)
                .orElseThrow(() -> new CustomerNotFoundException("Cliente con ID " + id + " no encontrado"));
        return customerMapper.toResponse(customer);
    }
    
    @Override
    public CustomerResponse getCustomerByIdentificacion(String identificacion) {
        Customer customer = customerRepository.findByIdentificacion(identificacion)
                .orElseThrow(() -> new CustomerNotFoundException("Cliente con identificación " + identificacion + " no encontrado"));
        return customerMapper.toResponse(customer);
    }
    
    @Override
    @Transactional
    public CustomerResponse updateCustomer(Long id, UpdateCustomerRequest request) {
        Customer customer = customerRepository.findByIdOptional(id)
                .orElseThrow(() -> new CustomerNotFoundException("Cliente con ID " + id + " no encontrado"));
        
        customerValidator.validateUpdateRequest(request);
        customerMapper.updateEntityFromRequest(customer, request);
        
        return customerMapper.toResponse(customer);
    }
    
    @Override
    @Transactional
    public CustomerResponse patchCustomer(Long id, Map<String, Object> updates) {
        Customer customer = customerRepository.findByIdOptional(id)
                .orElseThrow(() -> new CustomerNotFoundException("Cliente con ID " + id + " no encontrado"));
        
        customerMapper.applyPatch(customer, updates);
        
        return customerMapper.toResponse(customer);
    }
    
    @Override
    @Transactional
    public void deleteCustomer(Long id) {
        Customer customer = customerRepository.findByIdOptional(id)
                .orElseThrow(() -> new CustomerNotFoundException("Cliente con ID " + id + " no encontrado"));
        
        customer.activo = false;
    }
    
    @Override
    public PageResponse<CustomerResponse> searchCustomers(CustomerSearchRequest request, int page, int size) {
        List<Customer> customers = customerRepository.findWithFilters(
                request.tipoCliente,
                request.paisId,
                request.ciudad,
                request.scoreMin,
                request.scoreMax,
                request.nivelRiesgo,
                request.sucursalId,
                request.ejecutivoId,
                request.activo,
                page,
                size
        );
        
        long totalElements = customerRepository.countWithFilters(
                request.tipoCliente,
                request.paisId,
                request.ciudad,
                request.scoreMin,
                request.scoreMax,
                request.nivelRiesgo,
                request.sucursalId,
                request.ejecutivoId,
                request.activo
        );
        
        List<CustomerResponse> content = customers.stream()
                .map(customerMapper::toResponse)
                .collect(Collectors.toList());
        
        return new PageResponse<>(content, page, size, totalElements);
    }
    
    @Override
    public CreditScoreResponse getCreditScore(Long customerId) {
        Customer customer = customerRepository.findByIdOptional(customerId)
                .orElseThrow(() -> new CustomerNotFoundException("Cliente con ID " + customerId + " no encontrado"));
        
        CreditScoreResponse response = new CreditScoreResponse();
        response.customerId = customerId;
        response.score = customer.scoreCrediticio;
        response.nivelRiesgo = customer.nivelRiesgo;
        
        return response;
    }
    
    @Override
    @Transactional
    public CreditScoreResponse calculateCreditScore(Long customerId) {
        Customer customer = customerRepository.findByIdOptional(customerId)
                .orElseThrow(() -> new CustomerNotFoundException("Cliente con ID " + customerId + " no encontrado"));
        
        customer.scoreCrediticio = scoreCalculator.calculate(customer);
        customer.nivelRiesgo = scoreCalculator.determineRiskLevel(customer.scoreCrediticio);
        
        CreditScoreResponse response = new CreditScoreResponse();
        response.customerId = customerId;
        response.score = customer.scoreCrediticio;
        response.nivelRiesgo = customer.nivelRiesgo;
        
        return response;
    }
    
    @Override
    @Transactional
    public void updateRiskLevel(Long customerId, String nivelRiesgo, String justificacion) {
        Customer customer = customerRepository.findByIdOptional(customerId)
                .orElseThrow(() -> new CustomerNotFoundException("Cliente con ID " + customerId + " no encontrado"));
        
        customer.nivelRiesgo = nivelRiesgo;
        
        if (customer.metadata == null) {
            customer.metadata = new HashMap<>();
        }
        customer.metadata.put("risk_justification", justificacion);
        customer.metadata.put("risk_update_date", java.time.LocalDateTime.now().toString());
    }
    
    @Override
    public CustomerSummaryResponse getCustomerSummary(Long customerId) {
        Customer customer = customerRepository.findByIdOptional(customerId)
                .orElseThrow(() -> new CustomerNotFoundException("Cliente con ID " + customerId + " no encontrado"));
        
        CustomerSummaryResponse summary = new CustomerSummaryResponse();
        summary.customer = customerMapper.toResponse(customer);
        // TODO: Obtener cuentas, créditos, etc. de otros servicios
        
        return summary;
    }
    
    @Override
    @Transactional
    public void activateCustomer(Long id) {
        Customer customer = customerRepository.findByIdOptional(id)
                .orElseThrow(() -> new CustomerNotFoundException("Cliente con ID " + id + " no encontrado"));
        
        customer.activo = true;
    }
    
    @Override
    @Transactional
    public void deactivateCustomer(Long id, String motivo) {
        Customer customer = customerRepository.findByIdOptional(id)
                .orElseThrow(() -> new CustomerNotFoundException("Cliente con ID " + id + " no encontrado"));
        
        customer.activo = false;
        
        if (customer.metadata == null) {
            customer.metadata = new HashMap<>();
        }
        customer.metadata.put("deactivation_reason", motivo);
        customer.metadata.put("deactivation_date", java.time.LocalDateTime.now().toString());
    }
    
    @Override
    @Transactional
    public void blockCustomer(Long id, String motivo, String comentarios) {
        Customer customer = customerRepository.findByIdOptional(id)
                .orElseThrow(() -> new CustomerNotFoundException("Cliente con ID " + id + " no encontrado"));
        
        customer.activo = false;
        
        if (customer.metadata == null) {
            customer.metadata = new HashMap<>();
        }
        customer.metadata.put("blocked", true);
        customer.metadata.put("block_reason", motivo);
        customer.metadata.put("block_comments", comentarios);
        customer.metadata.put("block_date", java.time.LocalDateTime.now().toString());
    }
    
    @Override
    @Transactional
    public void unblockCustomer(Long id) {
        Customer customer = customerRepository.findByIdOptional(id)
                .orElseThrow(() -> new CustomerNotFoundException("Cliente con ID " + id + " no encontrado"));
        
        customer.activo = true;
        
        if (customer.metadata == null) {
            customer.metadata = new HashMap<>();
        }
        customer.metadata.put("blocked", false);
        customer.metadata.put("unblock_date", java.time.LocalDateTime.now().toString());
    }
    
    @Override
    @Transactional
    public void updateEmail(Long id, String email) {
        Customer customer = customerRepository.findByIdOptional(id)
                .orElseThrow(() -> new CustomerNotFoundException("Cliente con ID " + id + " no encontrado"));
        
        if (customerRepository.findByEmail(email).isPresent()) {
            throw new DuplicateCustomerException("Email " + email + " ya está en uso");
        }
        
        customer.email = email;
    }
    
    @Override
    @Transactional
    public void updatePhone(Long id, String telefono) {
        Customer customer = customerRepository.findByIdOptional(id)
                .orElseThrow(() -> new CustomerNotFoundException("Cliente con ID " + id + " no encontrado"));
        
        customer.telefono = telefono;
    }
    
    @Override
    @Transactional
    public void updateAddress(Long id, String direccion, String ciudad, String estadoProvincia, String codigoPostal) {
        Customer customer = customerRepository.findByIdOptional(id)
                .orElseThrow(() -> new CustomerNotFoundException("Cliente con ID " + id + " no encontrado"));
        
        customer.direccion = direccion;
        customer.ciudad = ciudad;
        customer.estadoProvincia = estadoProvincia;
        customer.codigoPostal = codigoPostal;
    }
    
    @Override
    @Transactional
    public void assignExecutive(Long customerId, Long executiveId) {
        Customer customer = customerRepository.findByIdOptional(customerId)
                .orElseThrow(() -> new CustomerNotFoundException("Cliente con ID " + customerId + " no encontrado"));
        
        customer.ejecutivoId = executiveId;
    }
    
    @Override
    public Map<String, Object> getMetadata(Long customerId) {
        Customer customer = customerRepository.findByIdOptional(customerId)
                .orElseThrow(() -> new CustomerNotFoundException("Cliente con ID " + customerId + " no encontrado"));
        
        return customer.metadata != null ? customer.metadata : new HashMap<>();
    }
    
    @Override
    @Transactional
    public void updateMetadata(Long customerId, Map<String, Object> metadata) {
        Customer customer = customerRepository.findByIdOptional(customerId)
                .orElseThrow(() -> new CustomerNotFoundException("Cliente con ID " + customerId + " no encontrado"));
        
        customer.metadata = metadata;
    }
    
    @Override
    @Transactional
    public void updateMetadataField(Long customerId, String key, Object value) {
        Customer customer = customerRepository.findByIdOptional(customerId)
                .orElseThrow(() -> new CustomerNotFoundException("Cliente con ID " + customerId + " no encontrado"));
        
        if (customer.metadata == null) {
            customer.metadata = new HashMap<>();
        }
        customer.metadata.put(key, value);
    }
}