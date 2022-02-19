package com.bank.atm.domain.service.user.command.transfer;

import java.util.ArrayList;

import com.bank.atm.domain.common.Guard;
import com.bank.atm.domain.common.handler.Handler;
import com.bank.atm.domain.data.AccountRepository;
import com.bank.atm.domain.data.Owe;
import com.bank.atm.domain.service.AbstractServiceCommand;
import com.bank.atm.domain.service.ServiceException;
import com.bank.atm.domain.service.user.command.owe.OweRequest;

public class TransferCommand extends AbstractServiceCommand<TransferRequest, TransferResult> {
    private final Handler<OweRequest, Integer> oweCmd;

    public TransferCommand(AccountRepository accRepo, Handler<OweRequest, Integer> oweCmd) {
        super(accRepo);
        Guard.validateArgNotNull(oweCmd, "oweCmd");
        this.oweCmd = oweCmd;
    }

    @Override
    public TransferResult execute(TransferRequest request) throws ServiceException {
        Guard.validateArgNotNullOrEmpty(request.getAccountName(), "accountName");
        Guard.validateArgNotNullOrEmpty(request.getRecipient(), "recipient");

        if (request.getAmount() < 1)
            throw TransferException.amountMustGreaterThanOrEqualsTo(request.getAccountName(), request.getRecipient(),
                    request.getAmount());

        if (request.getRecipient().equals(request.getAccountName()))
            throw TransferException.cannotTransferToSameAccount(request.getAccountName(), request.getRecipient(),
                    request.getAmount());

        var acc = getAccount(request.getAccountName());
        var recAcc = getAccount(request.getRecipient());
        var oweList = new ArrayList<Owe>();

        var amount = oweCmd
                .execute(new OweRequest(acc, recAcc, request.getAmount(), oweList));
        if (amount != 0) {
            acc.setBalance(acc.getBalance() - amount);
            recAcc.setBalance(recAcc.getBalance() + amount);
            updateAccounts(acc, recAcc);
        }
        var result = new TransferResult(request.getAccountName(), request.getRecipient(), oweList);
        result.setAmount(amount);
        result.setBalance(acc.getBalance());
        return result;
    }

}
