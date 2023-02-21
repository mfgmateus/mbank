package com.ferreira.mbank.service.impl;

import com.ferreira.mbank.data.Account;
import com.ferreira.mbank.data.Movement;
import com.ferreira.mbank.data.repository.AccountRepository;
import com.ferreira.mbank.data.repository.MovementRepository;
import com.ferreira.mbank.exception.DepositException;
import com.ferreira.mbank.exception.WithdrawException;
import com.ferreira.mbank.service.BankService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BankServiceImpl implements BankService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final AccountRepository accountRepository;
    private final MovementRepository movementRepository;

    @Autowired
    public BankServiceImpl(AccountRepository accountRepository,
                           MovementRepository movementRepository) {
        this.accountRepository = accountRepository;
        this.movementRepository = movementRepository;
    }


    @Override
    public Movement deposit(Long customerId, Long amount) throws DepositException {

        var actualAmount = Math.abs(amount);
        var movement = createMovement(customerId, actualAmount);
        logger.info("New deposit request received for customer {} of {}", customerId, actualAmount);

        validateDeposit(actualAmount);

        var account = findAccount(customerId);

        account.setBalance(account.getBalance() + actualAmount);
        account.setUpdated(LocalDateTime.now());

        movement.setBalance(account.getBalance());

        accountRepository.save(account);
        movementRepository.save(movement);

        logger.info("Balance for customer {} updated to {}", customerId, actualAmount);

        return movement;
    }

    @Override
    public Movement withdraw(Long customerId, Long amount) throws WithdrawException {

        var actualAmount = Math.abs(amount);
        var movement = createMovement(customerId, -actualAmount);

        var account = findAccount(customerId);

        logger.info("New withdraw request received for customer {} of {}", customerId, actualAmount);

        validateWithdraw(actualAmount, account);

        account.setBalance(account.getBalance() - actualAmount);
        account.setUpdated(LocalDateTime.now());

        movement.setBalance(account.getBalance());

        accountRepository.save(account);
        movementRepository.save(movement);

        logger.info("Balance for customer {} updated to {}", customerId, account.getBalance());

        return movement;
    }

    @Override
    public List<Movement> findMovements(Long customerId) {
        logger.info("Find movements request for customer {}", customerId);
        return movementRepository.findAllByCustomerId(customerId);
    }

    private Account buildAccount(Long customerId) {
        var account = new Account();
        account.setCustomerId(customerId);
        account.setBalance(0L);
        return account;
    }

    private Account findAccount(Long customerId) {
        return accountRepository.findById(customerId)
            .orElse(buildAccount(customerId));
    }

    private static Movement createMovement(Long customerId, Long amount) {
        var movement = new Movement();
        movement.setAmount(amount);
        movement.setCustomerId(customerId);
        movement.setDateTime(LocalDateTime.now());
        return movement;
    }

    private static void validateWithdraw(long actualAmount, Account account) throws WithdrawException {

        if (account.getBalance() < actualAmount) {
            throw new WithdrawException("Account has insufficient funds for withdraw operation");
        }

        if (actualAmount == 0) {
            throw new WithdrawException("Withdraw value cannot be zero");
        }
    }

    private static void validateDeposit(long actualAmount) throws DepositException {

        if (actualAmount == 0) {
            throw new DepositException("Deposit value cannot be zero");
        }
    }
}
