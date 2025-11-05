package com.neuralbank.application.mapper;

import com.neuralbank.domain.entity.Customer;
import com.neuralbank.domain.enums.CustomerType;
import com.neuralbank.infrastructure.rest.dto.request.CreateCustomerRequest;
import com.neuralbank.infrastructure.rest.dto.request.UpdateCustomerRequest;
import com.neuralbank.infrastructure.rest.dto.response.CustomerResponse;
import jakarta.enterprise.context.ApplicationScoped;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

@ApplicationScoped
public class CustomerMapper {
    
    public Customer toEntity(CreateCustomerRequest request) {
        Customer customer = new Customer();
        
        customer.identificacion = request.identificacion;
        customer.tipoIdentificacion = request.tipoIdentificacion;
        customer.nombre = request.nombre;
        customer.apellido = request.apellido;
        customer.fechaNacimiento = request.fechaNacimiento;
        customer.email = request.email;
        customer.telefono = request.telefono;
        customer.telefonoAlternativo = request.telefonoAlternativo;
        customer.direccion = request.direccion;
        customer.ciudad = request.ciudad;
        customer.estadoProvincia = request.estadoProvincia;
        customer.codigoPostal = request.codigoPostal;
        customer.paisId = request.paisId;
        customer.tipoCliente = request.tipoCliente != null ? request.tipoCliente : CustomerType.PERSONAL;
        customer.scoreCrediticio = request.scoreCrediticio;
        customer.nivelRiesgo = request.nivelRiesgo;
        customer.sucursalId = request.sucursalId;
        customer.ejecutivoId = request.ejecutivoId;
        customer.metadata = request.metadata;
        customer.activo = true;
        customer.fechaRegistro = LocalDate.now();
        
        return customer;
    }
    
    public CustomerResponse toResponse(Customer customer) {
        CustomerResponse response = new CustomerResponse();
        
        response.id = customer.id;
        response.identificacion = customer.identificacion;
        response.tipoIdentificacion = customer.tipoIdentificacion;
        response.nombre = customer.nombre;
        response.apellido = customer.apellido;
        response.nombreCompleto = customer.nombreCompleto;
        response.fechaNacimiento = customer.fechaNacimiento;
        response.email = customer.email;
        response.telefono = customer.telefono;
        response.telefonoAlternativo = customer.telefonoAlternativo;
        response.direccion = customer.direccion;
        response.ciudad = customer.ciudad;
        response.estadoProvincia = customer.estadoProvincia;
        response.codigoPostal = customer.codigoPostal;
        response.paisId = customer.paisId;
        response.tipoCliente = customer.tipoCliente;
        response.scoreCrediticio = customer.scoreCrediticio;
        response.nivelRiesgo = customer.nivelRiesgo;
        response.fechaRegistro = customer.fechaRegistro;
        response.activo = customer.activo;
        response.sucursalId = customer.sucursalId;
        response.ejecutivoId = customer.ejecutivoId;
        response.metadata = customer.metadata;
        response.createdAt = customer.createdAt;
        response.updatedAt = customer.updatedAt;
        
        return response;
    }
    
    public void updateEntityFromRequest(Customer customer, UpdateCustomerRequest request) {
        if (request.nombre != null) {
            customer.nombre = request.nombre;
        }
        
        if (request.apellido != null) {
            customer.apellido = request.apellido;
        }
        
        if (request.fechaNacimiento != null) {
            customer.fechaNacimiento = request.fechaNacimiento;
        }
        
        if (request.email != null) {
            customer.email = request.email;
        }
        
        if (request.telefono != null) {
            customer.telefono = request.telefono;
        }
        
        if (request.telefonoAlternativo != null) {
            customer.telefonoAlternativo = request.telefonoAlternativo;
        }
        
        if (request.direccion != null) {
            customer.direccion = request.direccion;
        }
        
        if (request.ciudad != null) {
            customer.ciudad = request.ciudad;
        }
        
        if (request.estadoProvincia != null) {
            customer.estadoProvincia = request.estadoProvincia;
        }
        
        if (request.codigoPostal != null) {
            customer.codigoPostal = request.codigoPostal;
        }
        
        if (request.paisId != null) {
            customer.paisId = request.paisId;
        }
        
        if (request.tipoCliente != null) {
            customer.tipoCliente = request.tipoCliente;
        }
        
        if (request.metadata != null) {
            customer.metadata = request.metadata;
        }
    }
    
    public void applyPatch(Customer customer, Map<String, Object> updates) {
        updates.forEach((key, value) -> {
            switch (key) {
                case "nombre":
                    customer.nombre = (String) value;
                    break;
                case "apellido":
                    customer.apellido = (String) value;
                    break;
                case "email":
                    customer.email = (String) value;
                    break;
                case "telefono":
                    customer.telefono = (String) value;
                    break;
                case "telefonoAlternativo":
                    customer.telefonoAlternativo = (String) value;
                    break;
                case "direccion":
                    customer.direccion = (String) value;
                    break;
                case "ciudad":
                    customer.ciudad = (String) value;
                    break;
                case "estadoProvincia":
                    customer.estadoProvincia = (String) value;
                    break;
                case "codigoPostal":
                    customer.codigoPostal = (String) value;
                    break;
                case "paisId":
                    customer.paisId = ((Number) value).longValue();
                    break;
                case "sucursalId":
                    customer.sucursalId = value != null ? ((Number) value).longValue() : null;
                    break;
                case "ejecutivoId":
                    customer.ejecutivoId = value != null ? ((Number) value).longValue() : null;
                    break;
                case "scoreCrediticio":
                    if (value instanceof Number) {
                        customer.scoreCrediticio = BigDecimal.valueOf(((Number) value).doubleValue());
                    }
                    break;
                case "nivelRiesgo":
                    customer.nivelRiesgo = (String) value;
                    break;
                case "activo":
                    customer.activo = (Boolean) value;
                    break;
            }
        });
    }
}