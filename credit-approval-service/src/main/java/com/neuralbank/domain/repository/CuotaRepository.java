package com.neuralbank.domain.repository;

import com.neuralbank.domain.entity.Cuota;
import com.neuralbank.domain.enums.CuotaStatus;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class CuotaRepository implements PanacheRepositoryBase<Cuota, Long> {
    
    public List<Cuota> findByCreditoId(Long creditoId) {
        return find("creditoId", creditoId).list();
    }
    
    public List<Cuota> findByCreditoIdAndEstado(Long creditoId, CuotaStatus estado) {
        return find("creditoId = ?1 and estado = ?2", creditoId, estado).list();
    }
    
    public List<Cuota> findVencidasByCreditoId(Long creditoId) {
        return find("creditoId = ?1 and estado = ?2 and fechaVencimiento < ?3", 
                creditoId, CuotaStatus.Vencida, LocalDate.now()).list();
    }
    
    public Optional<Cuota> findByCreditoIdAndNumeroCuota(Long creditoId, Integer numeroCuota) {
        return find("creditoId = ?1 and numeroCuota = ?2", creditoId, numeroCuota).firstResultOptional();
    }
    
    public List<Cuota> findByCreditoIdOrderByNumeroCuota(Long creditoId) {
        return find("creditoId = ?1 order by numeroCuota", creditoId).list();
    }
}

