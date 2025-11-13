package com.neuralbank.domain.enums;

public enum RiskLevel {
    BAJO("Bajo"),
    MEDIO("Medio"),
    ALTO("Alto"),
    MUY_ALTO("Muy Alto");
    
    private final String value;
    
    RiskLevel(String value) {
        this.value = value;
    }
    
    public String getValue() {
        return value;
    }
    
    public static RiskLevel fromValue(String value) {
        for (RiskLevel level : RiskLevel.values()) {
            if (level.value.equalsIgnoreCase(value)) {
                return level;
            }
        }
        throw new IllegalArgumentException("Invalid RiskLevel: " + value);
    }
}