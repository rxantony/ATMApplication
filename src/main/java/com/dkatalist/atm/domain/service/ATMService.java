package com.dkatalist.atm.domain.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.dkatalist.atm.domain.common.Guard;
import com.dkatalist.atm.domain.data.Owe;

public interface ATMService {

    DepositResult deposit(String accountName, int amount) throws ServiceException;

    TransactionResult withdraw(String accountName, int amount) throws ServiceException;

    TransferResult transfer(String accountName, String rescipient, int amount) throws ServiceException;

    List<Owe> getOweList(String accountName);

    public static class TransactionResult {
        private final String accountName;
        private int amount;
        private int balace;

        public TransactionResult(String accountName) {
            Guard.validateArgNotNullOrEmpty(accountName, "accountName");
            this.accountName = accountName;
        }

        public TransactionResult(String accountName, int amount, int balace) {
            this(accountName);
            setAmount(amount);
            setBalance(balace);
        }

        public String getAccountName(){
            return accountName;
        }

        public int getAmount() {
            return amount;
        }

        public void setAmount(int amount) {
            Guard.validateArgMustBeGreaterThan(amount, -1, "amount");
            this.amount = amount;
        }

        public int getBalance() {
            return balace;
        }

        public void setBalance(int amount) {
            Guard.validateArgMustBeGreaterThan(amount, -1, "amount");
            this.balace = amount;
        }
    }

    public static class DepositResult extends TransactionResult implements OweListResult {
        private final List<TransferResult> transferList;

        public DepositResult(String accountName) {
            this(accountName, 0, 0);
        }

        public DepositResult(String accountName, int amount, int saving) {
            super(accountName, amount, saving);
            this.transferList = new ArrayList<>();
        }

        public DepositResult(String accountName, int amount, int saving, List<TransferResult> transferList) {
            super(accountName, amount, saving);
            Guard.validateArgNotNull(transferList, "transferList");
            this.transferList = transferList;
        }

        public List<TransferResult> getTransferList(){
            return transferList;
        }

        @Override
        public List<Owe> getOweList() {
            return transferList.stream().map(TransferResult::getOweList).flatMap(List<Owe>::stream).collect(Collectors.toList());
        }
    }

    public static class TransferResult extends TransactionResult implements OweListResult {
        private final String recipient;
        private  final List<Owe> oweList;

        public TransferResult(String accountName, String recipient) {
            this(accountName, recipient, 0, 0);
        }

        public TransferResult(String accountName, String recipient, int amount, int saving) {
            super(accountName, amount, saving);
            Guard.validateArgNotNullOrEmpty(recipient, "recipient");
            this.recipient = recipient;
            this.oweList = new ArrayList<>();
        }

        public TransferResult(String accountName, String recipient, int amount, int saving, List<Owe> oweList) {
            super(accountName, amount, saving);
            Guard.validateArgNotNullOrEmpty(recipient, "recipient");
            Guard.validateArgNotNull(oweList, "oweList");
            this.recipient = recipient;
            this.oweList = oweList;
        }

        public String getRecipient(){
            return recipient;
        }

        public List<Owe> getOweList(){
            return oweList;
        }
    }

    public static interface OweListResult {
        String getAccountName();
        List<Owe> getOweList();
    }
}
