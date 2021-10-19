package com.dkatalist.atm.domain.service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.dkatalist.atm.domain.common.Guard;
import com.dkatalist.atm.domain.data.Account;
import com.dkatalist.atm.domain.data.Db;
import com.dkatalist.atm.domain.data.Owe;
import com.dkatalist.atm.domain.data.OweDb;
import com.dkatalist.atm.domain.data.OweDb.Key;

public class ATMServiceDefault implements ATMService {
    private Db<String, Account> accDb;
    private Db<OweDb.Key, Owe> oweDb;

    public ATMServiceDefault(Db<String, Account> accDb, Db<OweDb.Key, Owe> oweDb) {
        Guard.validateArgNotNull(accDb, "accDb");
        Guard.validateArgNotNull(oweDb, "oweDb");

        this.accDb = accDb;
        this.oweDb = oweDb;
    }

    @Override
    public TransactionResult withdraw(String accountName, int amount) throws ServiceException {
        Guard.validateArgNotNullOrEmpty(accountName, "accountName");
        Guard.validateArgMustBeGreaterThan(amount, 0, "amount");

        Account acc = getAccount(accountName);
        TransactionResult result = new TransactionResult(accountName);
        if (amount > acc.getSaving())
            throw TransactionException.notEnoughAmount(accountName, "withdraw", amount);

        acc.setSaving(acc.getSaving() - amount);
        accDb.update(acc);

        result.setAmount(amount);
        result.setSaving(acc.getSaving());
        return result;
    }

    @Override
    public DepositResult deposit(String accountName, int amount) throws ServiceException {
        Guard.validateArgMustBeGreaterThan(amount, 0, "amount");

        Account acc = getAccount(accountName);
        acc.setSaving(acc.getSaving()+amount);
        accDb.update(acc);

        List<Owe> oweToList = oweDb.where(o -> o.getAccount1().equals(acc.getName()) && o.getAmount() < 0).stream()
                .sorted(Comparator.comparing(Owe::createdAt).reversed()).collect(Collectors.toList());

        if(oweToList.isEmpty()){
            DepositResult result = new DepositResult(accountName);
            result.setAmount(amount);
            result.setSaving(acc.getSaving());
            return result;
        }

        TransferResult transResult = null;
        DepositResult result = new DepositResult(accountName);
        for (Owe oweTo : oweToList) {
            int transAmount = amount;
            if (amount > -oweTo.getAmount()) {
                transAmount = -oweTo.getAmount();
                amount += transAmount;
            }
            transResult = transfer(accountName, oweTo.getAccount2(), transAmount);
            result.transferList.add(transResult);
        }
        result.setAmount(amount);
        result.setSaving(transResult.getSaving());
        return result;
    }

    @Override
    public TransferResult transfer(String accountName, String recipient, int amount) throws ServiceException {
        Guard.validateArgNotNullOrEmpty(accountName, "accountName");
        Guard.validateArgNotNullOrEmpty(recipient, "recipient");
        Guard.validateArgMustBeGreaterThan(amount, 0, "amount");

        if (recipient.equals(accountName))
            throw TransferException.cannotTransferToSameAccount(accountName, recipient, amount);

        Account acc = getAccount(accountName);
        Account recAcc = getAccount(recipient);
        TransferResult result = new TransferResult(accountName, recipient);

        // will transfer callculation result amount
        amount = reduceOweFrom(accountName, recipient, amount, result);
        if (result.oweList.isEmpty()) {
            //will transfer amount of param not callculation result amount
            reduceOweTo(accountName, recipient, amount, result);
            if(result.oweList.isEmpty())
                //will transfer calculation result amount
                amount = requestOweTo(acc, recipient, amount, result);
        }

        //transfer here
        if (amount != 0) {
            acc.setSaving(acc.getSaving() - amount);
            recAcc.setSaving(recAcc.getSaving() + amount);
            accDb.update(acc);
            accDb.update(recAcc);
        }
        result.setAmount(amount);
        result.setSaving(acc.getSaving());
        return result;
    }

    //tidak ada transfer, deduce owefrom
    private int reduceOweFrom(String accountName,  String recipient, int amount, TransferResult result){
        Optional<Owe> ooweFrom = oweDb.first(
            o -> o.getAccount1().equals(accountName) && o.getAccount2().equals(recipient) && o.getAmount() > 0);
            
        if(!ooweFrom.isPresent())
            return amount;

        Owe oweFrom = ooweFrom.get();
        Owe oweTo = oweDb.get(new Key(oweFrom.getAccount2(), oweFrom.getAccount1())).get();
        if (amount <= oweFrom.getAmount()) {
            oweFrom.setAmount(oweFrom.getAmount() - amount);
            oweTo.setAmount(oweTo.getAmount() + amount);
            amount = 0;
        } else {
            amount -= oweFrom.getAmount();
            oweFrom.setAmount(0);
            oweTo.setAmount(0);
        }
        
        oweDb.update(oweFrom);
        oweDb.update(oweTo);

        result.oweList.add(oweFrom);
        result.oweList.add(oweTo);
        return amount;
    }

    //lakukan transfer sejumlah amount dan deduce oweTo
    private int reduceOweTo(String accountName,  String recipient, int amount, TransferResult result){
        Optional<Owe> ooweTo = oweDb.first(
            o -> o.getAccount1().equals(accountName) && o.getAccount2().equals(recipient) && o.getAmount() < 0);    
        
        if(!ooweTo.isPresent())
            return amount;
        
        Owe oweTo = ooweTo.get();
        Owe oweFrom = oweDb.get(new Key(oweTo.getAccount2(), oweTo.getAccount1())).get();
        if (amount <= oweFrom.getAmount()) {
            oweTo.setAmount(oweTo.getAmount() + amount);
            oweFrom.setAmount(oweFrom.getAmount() - amount);
            amount = 0;
        } else {
            amount -= oweFrom.getAmount();
            oweTo.setAmount(0);
            oweFrom.setAmount(0);
        }
        
        oweDb.update(oweTo);
        oweDb.update(oweFrom);

        result.oweList.add(oweTo);
        result.oweList.add(oweFrom);
        return amount;
    }

    //transfer amount dan increase oweto
    private int requestOweTo(Account acc, String recipient, int amount, TransferResult result){
        if (amount > acc.getSaving()) {
            Owe oweTo = null;
            Owe oweFrom = null;
            int oweAmount = amount - acc.getSaving();
            amount = acc.getSaving();
            Optional<Owe> ooweTo = oweDb
                    .first(o -> o.getAccount1().equals(acc.getName()) && o.getAccount2().equals(recipient)); //search our owe to  recipient took place before
            if (ooweTo.isPresent()) {
                oweTo = ooweTo.get();
                oweFrom = oweDb.first(o -> o.getAccount1().equals(recipient) && o.getAccount2().equals(acc.getName()))
                        .get();
                oweTo.setAmount(oweTo.getAmount() + oweAmount);
                oweFrom.setAmount(oweFrom.getAmount() - oweAmount);
                oweDb.update(oweTo);
                oweDb.update(oweFrom);
            } else {
                oweTo = new Owe(acc.getName(), recipient, -oweAmount);
                oweFrom = new Owe(recipient, acc.getName(), oweAmount);
                oweDb.add(oweTo);
                oweDb.add(oweFrom);
            }
            result.oweList.add(oweFrom);
            result.oweList.add(oweTo);
        }
        return amount;
    }

    @Override
    public List<Owe> getOweList(String accountName) {
        return oweDb.where(o -> o.getAccount1().equals(accountName));
    }

    private Account getAccount(String accountName) throws AccountNotExistsException {
        Optional<Account> oacc = accDb.get(accountName);
        if (!oacc.isPresent())
            throw AccountNotExistsException.create(accountName);
        return oacc.get();
    }
}
