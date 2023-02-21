package com.ferreira.mbank.data.repository;

import com.ferreira.mbank.data.Movement;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovementRepository extends CrudRepository<Movement, Long> {

    List<Movement> findAllByCustomerId(Long customerId);

}
