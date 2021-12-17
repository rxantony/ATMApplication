package com.dkatalist.atm.domain.service.cqrs.atm.command.transfer;

import com.dkatalist.atm.domain.common.Guard;
import com.dkatalist.atm.domain.data.AccountRepository;
import com.dkatalist.atm.domain.service.ServiceException;
import com.dkatalist.atm.domain.service.cqrs.atm.command.AbstractATMCommand;
import com.dkatalist.atm.domain.service.facade.TransactionException;
import com.dkatalist.atm.domain.service.facade.TransferException;
import com.dkatalist.atm.domain.service.oweCallculation.OweCalculationService;

public class TransferCommand extends AbstractATMCommand<TransferRequest, TransferResult> {

    private final OweCalculationService calculationService;

    public TransferCommand(AccountRepository accRepo, OweCalculationService calculationService) {
        super(accRepo);
        Guard.validateArgNotNull(calculationService, "calculationService");
        this.calculationService = calculationService;
    }

    @Override
    public TransferResult execute(TransferRequest request) throws ServiceException {
        Guard.validateArgNotNullOrEmpty(request.getAccountName(), "accountName");
        Guard.validateArgNotNullOrEmpty(request.getRecipient(), "recipient");

        if (request.getAmount() < 1)
            throw TransactionException.amountMustGreaterThanOrEqualsTo(request.getAccountName(), request.getRecipient(),
                    request.getAmount());

        if (request.getRecipient().equals(request.getAccountName()))
            throw TransferException.cannotTransferToSameAccount(request.getAccountName(), request.getRecipient(),
                    request.getAmount());

        var acc = getAccount(request.getAccountName());
        var recAcc = getAccount(request.getRecipient());
        var result = new TransferResult(request.getAccountName(), request.getRecipient());

        var amount = this.calculationService.calculate(acc, recAcc, request.getAmount(), result.getOweList());
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
