package com.ferreira.mbank.controller;

import com.ferreira.mbank.controller.request.DepositRequest;
import com.ferreira.mbank.controller.request.WithdrawRequest;
import com.ferreira.mbank.data.Movement;
import com.ferreira.mbank.exception.DepositException;
import com.ferreira.mbank.exception.WithdrawException;
import com.ferreira.mbank.service.BankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class BankController {

    private final BankService bankService;

    @Autowired
    public BankController(BankService bankService) {
        this.bankService = bankService;
    }

    @PostMapping("/deposits/{customerId}")
    public Movement deposit(@PathVariable Long customerId,
                            @RequestBody DepositRequest request) throws DepositException {
        return bankService.deposit(customerId, request.getAmount());
    }

    @PostMapping("/withdraws/{customerId}")
    public Movement withdraw(@PathVariable Long customerId,
                             @RequestBody WithdrawRequest request) throws WithdrawException {
        return bankService.withdraw(customerId, request.getAmount());
    }

    @GetMapping("/movements/{customerId}")
    public List<Movement> findMovements(@PathVariable Long customerId) {
        return bankService.findMovements(customerId);
    }

    @GetMapping("/")
    public String home() {
        return "ok";
    }

}
