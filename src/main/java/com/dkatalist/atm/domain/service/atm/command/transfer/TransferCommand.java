package com.dkatalist.atm.domain.service.atm.command.transfer;

import com.dkatalist.atm.domain.common.Guard;
import com.dkatalist.atm.domain.common.handler.Handler;
import com.dkatalist.atm.domain.data.AccountRepository;
import com.dkatalist.atm.domain.service.ServiceException;
import com.dkatalist.atm.domain.service.atm.command.AbstractATMCommand;
import com.dkatalist.atm.domain.service.atm.command.oweCalcullation.OweCallculationRequest;

public class TransferCommand extends AbstractATMCommand<TransferRequest, TransferResult> {

    private final Handler<OweCallculationRequest, Integer> calculationCmd;

    public TransferCommand(AccountRepository accRepo, Handler<OweCallculationRequest, Integer> calculationCmd) {
        super(accRepo);
        Guard.validateArgNotNull(calculationCmd, "calculationCmd");
        this.calculationCmd = calculationCmd;
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
        var result = new TransferResult(request.getAccountName(), request.getRecipient());

        var amount = calculationCmd
                .execute(new OweCallculationRequest(acc, recAcc, request.getAmount(), result.getOweList()));
        if (amount != 0) {
            acc.setBalance(acc.getBalance() - amount);
            recAcc.setBalance(recAcc.getBalance() + amount);
            updateAccounts(acc, recAcc);
        }
        result.setAmount(amount);
        result.setBalance(acc.getBalance());
        return result;
    }

}
