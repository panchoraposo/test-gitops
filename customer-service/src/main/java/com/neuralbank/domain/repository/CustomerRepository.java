package com.neuralbank.domain.repository;

import com.neuralbank.domain.entity.Customer;
import com.neuralbank.domain.enums.CustomerType;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Parameters;
import jakarta.enterprise.context.ApplicationScoped;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class CustomerRepository implements PanacheRepositoryBase<Customer, Long> {
    
    public Optional<Customer> findByIdentificacion(String identificacion) {
        return find("identificacion", identificacion).firstResultOptional();
    }
    
    public Optional<Customer> findByEmail(String email) {
        return find("email", email).firstResultOptional();
    }
    
    public List<Customer> findByTipoCliente(CustomerType tipoCliente) {
        return find("tipoCliente", tipoCliente).list();
    }
    
    public List<Customer> findByPaisId(Long paisId) {
        return find("paisId", paisId).list();
    }
    
    public List<Customer> findBySucursalId(Long sucursalId) {
        return find("sucursalId", sucursalId).list();
    }
    
    public List<Customer> findByEjecutivoId(Long ejecutivoId) {
        return find("ejecutivoId", ejecutivoId).list();
    }
    
    public List<Customer> findActiveCustomers() {
        return find("activo", true).list();
    }
    
    public List<Customer> findByScoreRange(BigDecimal minScore, BigDecimal maxScore) {
        return find("scoreCrediticio >= ?1 and scoreCrediticio <= ?2", minScore, maxScore).list();
    }
    
    public List<Customer> findByNivelRiesgo(String nivelRiesgo) {
        return find("nivelRiesgo", nivelRiesgo).list();
    }
    
    public List<Customer> searchByName(String searchTerm) {
        return find("LOWER(nombreCompleto) LIKE LOWER(?1)", "%" + searchTerm + "%").list();
    }
    
    public List<Customer> findWithFilters(
            CustomerType tipoCliente,
            Long paisId,
            String ciudad,
            BigDecimal scoreMin,
            BigDecimal scoreMax,
            String nivelRiesgo,
            Long sucursalId,
            Long ejecutivoId,
            Boolean activo,
            int page,
            int size) {
        
        StringBuilder query = new StringBuilder("1=1");
        Parameters params = new Parameters();
        
        if (tipoCliente != null) {
            query.append(" and tipoCliente = :tipoCliente");
            params.and("tipoCliente", tipoCliente);
        }
        
        if (paisId != null) {
            query.append(" and paisId = :paisId");
            params.and("paisId", paisId);
        }
        
        if (ciudad != null && !ciudad.trim().isEmpty()) {
            query.append(" and LOWER(ciudad) = LOWER(:ciudad)");
            params.and("ciudad", ciudad.trim());
        }
        
        if (scoreMin != null) {
            query.append(" and scoreCrediticio >= :scoreMin");
            params.and("scoreMin", scoreMin);
        }
        
        if (scoreMax != null) {
            query.append(" and scoreCrediticio <= :scoreMax");
            params.and("scoreMax", scoreMax);
        }
        
        if (nivelRiesgo != null && !nivelRiesgo.trim().isEmpty()) {
            query.append(" and nivelRiesgo = :nivelRiesgo");
            params.and("nivelRiesgo", nivelRiesgo.trim());
        }
        
        if (sucursalId != null) {
            query.append(" and sucursalId = :sucursalId");
            params.and("sucursalId", sucursalId);
        }
        
        if (ejecutivoId != null) {
            query.append(" and ejecutivoId = :ejecutivoId");
            params.and("ejecutivoId", ejecutivoId);
        }
        
        if (activo != null) {
            query.append(" and activo = :activo");
            params.and("activo", activo);
        }
        
        return find(query.toString(), params)
                .page(Page.of(page, size))
                .list();
    }
    
    public long countWithFilters(
            CustomerType tipoCliente,
            Long paisId,
            String ciudad,
            BigDecimal scoreMin,
            BigDecimal scoreMax,
            String nivelRiesgo,
            Long sucursalId,
            Long ejecutivoId,
            Boolean activo) {
        
        StringBuilder query = new StringBuilder("1=1");
        Parameters params = new Parameters();
        
        if (tipoCliente != null) {
            query.append(" and tipoCliente = :tipoCliente");
            params.and("tipoCliente", tipoCliente);
        }
        
        if (paisId != null) {
            query.append(" and paisId = :paisId");
            params.and("paisId", paisId);
        }
        
        if (ciudad != null && !ciudad.trim().isEmpty()) {
            query.append(" and LOWER(ciudad) = LOWER(:ciudad)");
            params.and("ciudad", ciudad.trim());
        }
        
        if (scoreMin != null) {
            query.append(" and scoreCrediticio >= :scoreMin");
            params.and("scoreMin", scoreMin);
        }
        
        if (scoreMax != null) {
            query.append(" and scoreCrediticio <= :scoreMax");
            params.and("scoreMax", scoreMax);
        }
        
        if (nivelRiesgo != null && !nivelRiesgo.trim().isEmpty()) {
            query.append(" and nivelRiesgo = :nivelRiesgo");
            params.and("nivelRiesgo", nivelRiesgo.trim());
        }
        
        if (sucursalId != null) {
            query.append(" and sucursalId = :sucursalId");
            params.and("sucursalId", sucursalId);
        }
        
        if (ejecutivoId != null) {
            query.append(" and ejecutivoId = :ejecutivoId");
            params.and("ejecutivoId", ejecutivoId);
        }
        
        if (activo != null) {
            query.append(" and activo = :activo");
            params.and("activo", activo);
        }
        
        return count(query.toString(), params);
    }
}