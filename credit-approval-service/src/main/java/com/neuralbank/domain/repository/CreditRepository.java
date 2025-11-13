package com.neuralbank.domain.repository;

import com.neuralbank.domain.entity.Credit;
import com.neuralbank.domain.enums.CreditStatus;
import com.neuralbank.domain.enums.CreditType;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.quarkus.panache.common.Parameters;
import jakarta.enterprise.context.ApplicationScoped;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class CreditRepository implements PanacheRepositoryBase<Credit, Long> {
    
    public Optional<Credit> findByNumeroCredito(String numeroCredito) {
        return find("numeroCredito", numeroCredito).firstResultOptional();
    }
    
    public List<Credit> findByClienteId(Long clienteId) {
        return find("clienteId", clienteId).list();
    }
    
    public List<Credit> findByEstado(CreditStatus estado) {
        return find("estado", estado).list();
    }
    
    public List<Credit> findByTipoCredito(CreditType tipoCredito) {
        return find("tipoCredito", tipoCredito).list();
    }
    
    public List<Credit> findByClienteIdAndEstado(Long clienteId, CreditStatus estado) {
        return find("clienteId = ?1 and estado = ?2", clienteId, estado).list();
    }
    
    public List<Credit> findByFechaSolicitudBetween(LocalDate fechaInicio, LocalDate fechaFin) {
        return find("fechaSolicitud >= ?1 and fechaSolicitud <= ?2", fechaInicio, fechaFin).list();
    }
    
    public List<Credit> findByMontoSolicitadoRange(BigDecimal montoMin, BigDecimal montoMax) {
        return find("montoSolicitado >= ?1 and montoSolicitado <= ?2", montoMin, montoMax).list();
    }
}

