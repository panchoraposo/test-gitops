package com.neuralbank.domain.repository;

import com.neuralbank.domain.entity.Account;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class AccountRepository implements PanacheRepositoryBase<Account, Long> {
    
    public List<Account> findByClienteId(Long clienteId) {
        return find("clienteId", clienteId).list();
    }
}

