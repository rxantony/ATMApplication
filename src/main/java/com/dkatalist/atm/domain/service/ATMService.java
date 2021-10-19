package com.dkatalist.atm.domain.service;

import java.util.ArrayList;
import java.util.List;

import com.dkatalist.atm.domain.common.Guard;
import com.dkatalist.atm.domain.data.Owe;

public interface ATMService {

    DepositResult deposit(String accountName, int amount) throws ServiceException;

    TransactionResult withdraw(String accountName, int amount) throws ServiceException;

    TransferResult transfer(String accountName, String rescipient, int amount) throws ServiceException;

    List<Owe> getOweList(String accountName);

    public static class TransactionResult {
        public final String accountName;
        private int amount;
        private int saving;
        public TransactionResult(String accountName) {
            Guard.validateArgNotNullOrEmpty(accountName, "accountName");
            this.accountName = accountName;
        }

        public int getAmount() {
            return amount;
        }

        public void setAmount(int amount) {
            this.amount = amount;
        }

        public int getSaving() {
            return saving;
        }

        public void setSaving(int amount) {
            this.saving = amount;
        }
    }

    public static class DepositResult extends TransactionResult {
        public final List<TransferResult> transferList;
        public DepositResult(String accountName) {
            super(accountName);
            this.transferList = new ArrayList<>();
        }

    }

    public static class TransferResult extends TransactionResult {
        public final String recipient;
        public final List<Owe> oweList;
        public TransferResult(String accountName, String recipient) {
            super(accountName);
            Guard.validateArgNotNullOrEmpty(recipient, "recipient");
            this.recipient = recipient;
            this.oweList = new ArrayList<>();
        }

    }

    /*public static class TransferItem {
        public final String accountName;
        public final int amount;
        public TransferItem(String accountName, int amount) {
            Guard.validateArgNotNullOrEmpty(accountName, "accountName");
            this.accountName = accountName;
            this.amount = amount;
        }
    }*/
}
