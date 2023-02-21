package com.ferreira.mbank.data.repository;

import com.ferreira.mbank.data.Account;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends CrudRepository<Account, Long> {


}
