package com.neuralbank.domain.entity;

import com.neuralbank.domain.enums.CreditStatus;
import com.neuralbank.domain.enums.CreditType;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name = "credito", schema = "core")
public class Credit extends PanacheEntityBase {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "credito_id")
    public Long id;
    
    @Column(name = "cliente_id", nullable = false)
    public Long clienteId;
    
    @Column(name = "cuenta_desembolso_id")
    public Long cuentaDesembolsoId;
    
    @Column(name = "numero_credito", unique = true, nullable = false, length = 50)
    public String numeroCredito;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_credito", nullable = false)
    public CreditType tipoCredito;
    
    @Column(name = "moneda_id", nullable = false)
    public Long monedaId;
    
    @Column(name = "monto_solicitado", nullable = false, precision = 18, scale = 2)
    public BigDecimal montoSolicitado;
    
    @Column(name = "monto_aprobado", precision = 18, scale = 2)
    public BigDecimal montoAprobado;
    
    @Column(name = "tasa_interes_anual", nullable = false, precision = 5, scale = 2)
    public BigDecimal tasaInteresAnual;
    
    @Column(name = "plazo_meses", nullable = false)
    public Integer plazoMeses;
    
    @Column(name = "fecha_solicitud", nullable = false)
    public LocalDate fechaSolicitud;
    
    @Column(name = "fecha_aprobacion")
    public LocalDate fechaAprobacion;
    
    @Column(name = "fecha_desembolso")
    public LocalDate fechaDesembolso;
    
    @Column(name = "fecha_primer_vencimiento")
    public LocalDate fechaPrimerVencimiento;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "estado")
    public CreditStatus estado = CreditStatus.Solicitado;
    
    @Column(name = "destino_credito", length = 300)
    public String destinoCredito;
    
    @Column(name = "score_aprobacion", precision = 5, scale = 2)
    public BigDecimal scoreAprobacion;
    
    @Column(name = "ejecutivo_id")
    public Long ejecutivoId;
    
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "metadata", columnDefinition = "jsonb")
    public Map<String, Object> metadata = new HashMap<>();
    
    @Column(name = "created_at", updatable = false)
    public LocalDateTime createdAt = LocalDateTime.now();
    
    @Column(name = "updated_at")
    public LocalDateTime updatedAt = LocalDateTime.now();
    
    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}

