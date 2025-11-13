package com.neuralbank.domain.repository;

import com.neuralbank.domain.entity.Customer;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CustomerRepository implements PanacheRepositoryBase<Customer, Long> {
    
    // PanacheRepositoryBase ya proporciona findByIdOptional(id) que podemos usar directamente
}

