package com.neuralbank.shared.constants;

public class ErrorMessages {
    
    // Customer errors
    public static final String CUSTOMER_NOT_FOUND = "Cliente no encontrado";
    public static final String CUSTOMER_ALREADY_EXISTS = "Cliente ya existe";
    public static final String INVALID_CUSTOMER_DATA = "Datos de cliente inválidos";
    
    // Validation errors
    public static final String IDENTIFICATION_REQUIRED = "Identificación es requerida";
    public static final String IDENTIFICATION_TYPE_REQUIRED = "Tipo de identificación es requerido";
    public static final String NAME_REQUIRED = "Nombre es requerido";
    public static final String COUNTRY_REQUIRED = "País es requerido";
    public static final String EMAIL_INVALID = "Email inválido";
    public static final String IDENTIFICATION_FORMAT_INVALID = "Formato de identificación inválido";
    
    // Business errors
    public static final String CUSTOMER_INACTIVE = "Cliente inactivo";
    public static final String CUSTOMER_BLOCKED = "Cliente bloqueado";
    public static final String DUPLICATE_EMAIL = "Email ya está en uso";
    public static final String DUPLICATE_IDENTIFICATION = "Identificación ya está registrada";
    
    // Internal errors
    public static final String INTERNAL_SERVER_ERROR = "Error interno del servidor";
    public static final String DATABASE_ERROR = "Error al acceder a la base de datos";
    
    private ErrorMessages() {
        // Clase de constantes - no instanciable
    }
}