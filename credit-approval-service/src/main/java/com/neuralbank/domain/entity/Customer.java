package com.neuralbank.domain.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Entidad mínima de Customer para acceso desde el servicio de aprobación.
 * Solo contiene los campos necesarios para la lógica de aprobación.
 */
@Entity
@Table(name = "cliente", schema = "core")
public class Customer extends PanacheEntityBase {
    
    @Id
    @Column(name = "cliente_id")
    public Long id;
    
    @Column(name = "score_crediticio", precision = 5, scale = 2)
    public BigDecimal scoreCrediticio;
    
    @Column(name = "nivel_riesgo", length = 20)
    public String nivelRiesgo;
    
    @Column(name = "fecha_registro")
    public LocalDate fechaRegistro;
    
    @Column(nullable = false)
    public Boolean activo = true;
}

