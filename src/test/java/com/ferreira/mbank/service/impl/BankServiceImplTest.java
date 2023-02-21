package com.ferreira.mbank.service.impl;

import com.ferreira.mbank.data.Account;
import com.ferreira.mbank.data.repository.AccountRepository;
import com.ferreira.mbank.data.repository.MovementRepository;
import com.ferreira.mbank.exception.DepositException;
import com.ferreira.mbank.exception.WithdrawException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class BankServiceImplTest {

    @Mock
    private MovementRepository movementRepository;

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private BankServiceImpl bankService;

    @Captor
    private ArgumentCaptor<Account> accountArgumentCaptor;


    @Test
    void deposit_shouldDepositAmountAndUpdateBalanceForNewAccount() throws DepositException {
        //given
        Mockito.when(accountRepository.findById(1L)).thenReturn(Optional.empty());

        //when
        var movement = bankService.deposit(1L, 100L);
        Assertions.assertEquals(100L, movement.getAmount());
        Assertions.assertEquals(100L, movement.getBalance());
    }

    @Test
    void deposit_shouldHandleNegativeAmountOnDeposit() throws DepositException {
        //given
        Mockito.when(accountRepository.findById(1L)).thenReturn(Optional.empty());

        //when
        var movement = bankService.deposit(1L, -100L);
        Assertions.assertEquals(100L, movement.getAmount());
        Assertions.assertEquals(100L, movement.getBalance());
    }

    @Test
    void deposit_shouldIncreaseBalanceAfterNewDeposit() throws DepositException {
        //given
        var account = new Account();
        account.setBalance(50L);
        Mockito.when(accountRepository.findById(1L)).thenReturn(Optional.of(account));

        //when
        var movement = bankService.deposit(1L, -100L);
        Assertions.assertEquals(100L, movement.getAmount());
        Assertions.assertEquals(150L, movement.getBalance());
    }

    @Test
    void deposit_shouldStoreUpdatedBalance() throws DepositException {
        Mockito.when(accountRepository.findById(1L)).thenReturn(Optional.empty());
        Mockito.when(accountRepository.save(accountArgumentCaptor.capture())).thenReturn(new Account());

        bankService.deposit(1L, 100L);

        var savedBalance = accountArgumentCaptor.getAllValues().stream().findFirst();
        Assertions.assertTrue(savedBalance.isPresent());
        Assertions.assertEquals(100L, savedBalance.get().getBalance());
    }


    @Test
    void withdraw_shouldWithdrawAmount() throws WithdrawException {
        //given
        var account = new Account();
        account.setBalance(100L);
        Mockito.when(accountRepository.findById(1L)).thenReturn(Optional.of(account));

        //when
        var movement = bankService.withdraw(1L, 50L);
        Assertions.assertEquals(-50L, movement.getAmount());
        Assertions.assertEquals(50L, movement.getBalance());
    }

    @Test
    void withdraw_shouldHandleNegativeAmountOnWithdraw() throws WithdrawException {
        //given
        var account = new Account();
        account.setBalance(100L);
        Mockito.when(accountRepository.findById(1L)).thenReturn(Optional.of(account));

        //when
        var movement = bankService.withdraw(1L, -50L);
        Assertions.assertEquals(-50L, movement.getAmount());
        Assertions.assertEquals(50L, movement.getBalance());
    }


    @Test
    void withdraw_shouldStoreUpdatedBalance() throws WithdrawException {
        var account = new Account();
        account.setBalance(100L);
        Mockito.when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        Mockito.when(accountRepository.save(accountArgumentCaptor.capture())).thenReturn(new Account());

        bankService.withdraw(1L, 50L);

        var savedBalance = accountArgumentCaptor.getAllValues().stream().findFirst();
        Assertions.assertTrue(savedBalance.isPresent());
        Assertions.assertEquals(50L, savedBalance.get().getBalance());
    }

}
