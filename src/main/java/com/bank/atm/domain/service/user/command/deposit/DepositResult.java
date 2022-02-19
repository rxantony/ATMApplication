package com.bank.atm.domain.service.user.command.deposit;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import com.bank.atm.domain.common.Guard;
import com.bank.atm.domain.data.Owe;
import com.bank.atm.domain.service.OweListResult;
import com.bank.atm.domain.service.TransactionResult;
import com.bank.atm.domain.service.user.command.transfer.TransferResult;

public class DepositResult extends TransactionResult implements OweListResult {
    private final List<TransferResult> transferList;

    public DepositResult(String accountName) {
        this(accountName, 0, 0);
    }

    public DepositResult(String accountName, int amount, int balance) {
        this(accountName, amount, balance, new ArrayList<>());
    }

    public DepositResult(String accountName, int amount, int balance, List<TransferResult> transferList) {
        super(accountName, amount, balance);
        Guard.validateArgNotNull(transferList, "transferList");
        this.transferList = transferList;
    }

    public DepositResult addTransferResult(TransferResult result){
        transferList.add(result);
        return this;
    }

    public Iterable<TransferResult> getTransfers() {
        return transferList;
    }

    @Override
    public Iterable<Owe> getOwes() {
        return transferList.stream()
                .map(TransferResult::getOwes)
                .flatMap(it->StreamSupport.stream(it.spliterator(), false).sequential()).collect(Collectors.toList());
    }
}
