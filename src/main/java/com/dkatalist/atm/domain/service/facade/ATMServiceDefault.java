package com.dkatalist.atm.domain.service.facade;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import com.dkatalist.atm.domain.common.Guard;
import com.dkatalist.atm.domain.data.Account;
import com.dkatalist.atm.domain.data.AccountRepository;
import com.dkatalist.atm.domain.data.Owe;
import com.dkatalist.atm.domain.data.OweRepository;
import com.dkatalist.atm.domain.service.AccountNotExistsException;
import com.dkatalist.atm.domain.service.ServiceException;
import com.dkatalist.atm.domain.service.oweCallculation.OweCalculationService;

public class ATMServiceDefault implements ATMService {
    
    private AccountRepository accRepo;
    private OweRepository oweRepo;
    private OweCalculationService oweService;

    public ATMServiceDefault(AccountRepository accRepo, OweRepository oweRepo, OweCalculationService oweService) {
        Guard.validateArgNotNull(accRepo, "accRepo");
        Guard.validateArgNotNull(oweRepo, "oweRepo");
        Guard.validateArgNotNull(oweService, "oweService");

        this.accRepo = accRepo;
        this.oweRepo = oweRepo;
        this.oweService = oweService;
    }

    @Override
    public TransactionResult withdraw(String accountName, int amount) throws ServiceException {
        Guard.validateArgNotNullOrEmpty(accountName, "accountName");

        if(amount < 1)
            throw TransactionException.amountMustGreaterThanOrEqualsTo(accountName, "withdraw", amount);

        var acc = getAccount(accountName);
        var result = new TransactionResult(accountName);
        if (amount > acc.getBalance())
            throw TransactionException.notEnoughAmount(accountName, "withdraw", amount);

        acc.setBalance(acc.getBalance() - amount);
        accRepo.update(acc);

        result.setAmount(amount);
        result.setBalance(acc.getBalance());
        return result;
    }

    @Override
    public DepositResult deposit(String accountName, int amount) throws ServiceException {
        Guard.validateArgNotNullOrEmpty(accountName, "accountName");

        if(amount < 1)
            throw TransactionException.amountMustGreaterThanOrEqualsTo(accountName, "deposit", amount);

        var acc = getAccount(accountName);
        acc.setBalance(acc.getBalance()+amount);
        accRepo.update(acc);

        var oweToList = oweRepo.getOweToList(acc.getName()).stream()
                .sorted(Comparator.comparing(Owe::getCreatedAt).reversed()).collect(Collectors.toList());

        if(oweToList.isEmpty()){
            DepositResult result = new DepositResult(accountName);
            result.setAmount(amount);
            result.setBalance(acc.getBalance());
            return result;
        }

        TransferResult transResult = null;
        var result = new DepositResult(accountName);
        for (var oweTo : oweToList) {
            var transAmount = amount;
            if (amount > -oweTo.getAmount()) {
                transAmount = -oweTo.getAmount();
            }
            transResult = transfer(accountName, oweTo.getAccount2(), transAmount);
            result.getTransferList().add(transResult);
            amount -= transAmount;
        }
        result.setAmount(amount);
        result.setBalance(transResult.getBalance());
        return result;
    }

    @Override
    public TransferResult transfer(String accountName, String recipient, int amount) throws ServiceException {
        Guard.validateArgNotNullOrEmpty(accountName, "accountName");
        Guard.validateArgNotNullOrEmpty(recipient, "recipient");

        if(amount < 1)
            throw TransactionException.amountMustGreaterThanOrEqualsTo(accountName, "transfer", amount);

        if (recipient.equals(accountName))
            throw TransferException.cannotTransferToSameAccount(accountName, recipient, amount);

        var acc = getAccount(accountName);
        var recAcc = getAccount(recipient);
        var result = new TransferResult(accountName, recipient);

        amount = oweService.calculate(acc, recAcc, amount, result.getOweList());
        if (amount != 0) {
            acc.setBalance(acc.getBalance() - amount);
            recAcc.setBalance(recAcc.getBalance() + amount);
            accRepo.update(acc, recAcc);
        }
        result.setAmount(amount);
        result.setBalance(acc.getBalance());
        return result;
    }
    
    @Override
    public List<Owe> getOweList(String accountName) {
        return oweRepo.getList(accountName);
    }

    private Account getAccount(String accountName) throws AccountNotExistsException {
        var oacc = accRepo.get(accountName);
        if (!oacc.isPresent())
            throw AccountNotExistsException.create(accountName);
        return oacc.get();
    }
}
