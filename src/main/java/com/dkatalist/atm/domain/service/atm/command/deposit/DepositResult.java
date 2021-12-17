package com.dkatalist.atm.domain.service.atm.command.deposit;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.dkatalist.atm.domain.common.Guard;
import com.dkatalist.atm.domain.data.Owe;
import com.dkatalist.atm.domain.service.OweListResult;
import com.dkatalist.atm.domain.service.TransactionResult;
import com.dkatalist.atm.domain.service.atm.command.transfer.TransferResult;

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

    public List<TransferResult> getTransferList() {
        return transferList;
    }

    @Override
    public List<Owe> getOweList() {
        return transferList.stream().map(TransferResult::getOweList).flatMap(List<Owe>::stream)
                .collect(Collectors.toList());
    }
}
