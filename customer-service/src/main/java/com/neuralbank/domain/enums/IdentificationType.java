package com.neuralbank.domain.enums;

public enum IdentificationType {
    DNI("DNI"),
    PASSPORT("Passport"),
    SSN("SSN"),
    RFC("RFC"),
    CURP("CURP"),
    CPF("CPF"),
    CUIT("CUIT"),
    RUT("RUT"),
    RUN("RUN"),
    NINO("NINO"),
    CIF("CIF"),
    EIN("EIN"),
    TAX_ID("Tax ID");
    
    private final String value;
    
    IdentificationType(String value) {
        this.value = value;
    }
    
    public String getValue() {
        return value;
    }
    
    public static IdentificationType fromValue(String value) {
        for (IdentificationType type : IdentificationType.values()) {
            if (type.value.equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid IdentificationType: " + value);
    }
}