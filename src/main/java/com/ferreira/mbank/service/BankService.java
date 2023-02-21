package com.ferreira.mbank.service;

import com.ferreira.mbank.data.Movement;
import com.ferreira.mbank.exception.DepositException;
import com.ferreira.mbank.exception.WithdrawException;

import java.util.List;

public interface BankService {

    Movement deposit(Long customerId, Long amount) throws DepositException;

    Movement withdraw(Long customerId, Long amount) throws WithdrawException;

    List<Movement> findMovements(Long customerId);

}
