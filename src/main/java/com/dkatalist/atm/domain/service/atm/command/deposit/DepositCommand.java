package com.dkatalist.atm.domain.service.atm.command.deposit;

import java.util.Comparator;
import java.util.stream.Collectors;

import com.dkatalist.atm.domain.common.Guard;
import com.dkatalist.atm.domain.common.handler.HandlerWithException;
import com.dkatalist.atm.domain.data.AccountRepository;
import com.dkatalist.atm.domain.data.Owe;
import com.dkatalist.atm.domain.data.OweRepository;
import com.dkatalist.atm.domain.service.ServiceException;
import com.dkatalist.atm.domain.service.atm.command.AbstractATMCommand;
import com.dkatalist.atm.domain.service.atm.command.transfer.TransferRequest;
import com.dkatalist.atm.domain.service.atm.command.transfer.TransferResult;

public class DepositCommand extends AbstractATMCommand<DepositRequest, DepositResult> {

    private final OweRepository oweRepo;
    private final HandlerWithException<TransferRequest, TransferResult, ServiceException> transferCommand;

    public DepositCommand(AccountRepository accRepo, OweRepository oweRepo,
            HandlerWithException<TransferRequest, TransferResult, ServiceException> transferCommand) {
        super(accRepo);
        Guard.validateArgNotNull(transferCommand, "transferCommand");
        Guard.validateArgNotNull(oweRepo, "oweRepo");
        this.transferCommand = transferCommand;
        this.oweRepo = oweRepo;
    }

    @Override
    public DepositResult execute(DepositRequest request) throws ServiceException {
        Guard.validateArgNotNullOrEmpty(request.getAccountName(), "accountName");

        if (request.getAmount() < 1)
            throw DepositException.amountMustGreaterThanOrEqualsTo(request.getAccountName(), request.getAmount());

        var acc = getAccount(request.getAccountName());
        acc.setBalance(acc.getBalance() + request.getAmount());
        updateAccounts(acc);

        var oweToList = oweRepo.getOweToList(acc.getName()).stream()
                .sorted(Comparator.comparing(Owe::getCreatedAt).reversed()).collect(Collectors.toList());

        if (oweToList.isEmpty()) {
            DepositResult result = new DepositResult(request.getAccountName());
            result.setAmount(request.getAmount());
            result.setBalance(acc.getBalance());
            return result;
        }

        TransferResult transferResult = null;
        var result = new DepositResult(request.getAccountName());
        var amount = request.getAmount();
        for (var oweTo : oweToList) {
            var transAmount = amount;
            if (amount > -oweTo.getAmount()) {
                transAmount = -oweTo.getAmount();
            }
            var transferRequest = new TransferRequest(request.getAccountName(), oweTo.getAccount2(), transAmount);
            transferResult = transferCommand.execute(transferRequest);
            result.addTransferResult(transferResult);
            amount -= transAmount;
        }
        result.setAmount(amount);
        result.setBalance(transferResult.getBalance());
        return result;
    }

}
