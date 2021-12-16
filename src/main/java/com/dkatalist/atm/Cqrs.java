package com.dkatalist.atm;

import com.dkatalist.atm.domain.application.MediaInput;
import com.dkatalist.atm.domain.application.MediaOutput;
import com.dkatalist.atm.domain.application.cqrs.SessionDefault;
import com.dkatalist.atm.domain.application.cqrs.SessionInputHandlerDefault;
import com.dkatalist.atm.domain.application.cqrs.SessionManagerDefault;
import com.dkatalist.atm.domain.application.cqrs.SessionManagerInputHandlerDefault;
import com.dkatalist.atm.domain.application.cqrs.ATMMachine;
import com.dkatalist.atm.domain.common.cqrs.handler.HandlerManagerDefault;
import com.dkatalist.atm.domain.data.AccountRepositoryDefault;
import com.dkatalist.atm.domain.data.OweRepositoryDefault;
import com.dkatalist.atm.domain.service.ATMServiceDefault;
import com.dkatalist.atm.domain.service.AccountServiceDefault;
import com.dkatalist.atm.domain.service.ReduceOweFromService;
import com.dkatalist.atm.domain.service.ReduceOweToService;
import com.dkatalist.atm.domain.service.RequestOweToService;
import com.dkatalist.atm.domain.service.cqrs.account.command.createAccount.CreateAccountCommand;
import com.dkatalist.atm.domain.service.cqrs.account.query.getAccount.GetAccountQuery;
import com.dkatalist.atm.domain.service.cqrs.atm.command.deposit.DepositCommand;
import com.dkatalist.atm.domain.service.cqrs.atm.command.transfer.TransferCommand;
import com.dkatalist.atm.domain.service.cqrs.atm.command.withdraw.WithdrawCommand;
import com.dkatalist.atm.domain.service.cqrs.atm.query.getOweList.GetOweListQuery;

public class Cqrs implements ATMMachineRunner{
    @Override
    public void runATMMachine(MediaInput inputReader, MediaOutput inputWriter) {
        var accRepo = new AccountRepositoryDefault();
        var oweRepo = new OweRepositoryDefault();

        var callculationServ = new ReduceOweFromService(oweRepo
        , new ReduceOweToService(oweRepo
            , new RequestOweToService(oweRepo, null)));

        var transferCmd = new TransferCommand(accRepo, callculationServ);
        var handlerMgr = new HandlerManagerDefault();
        handlerMgr.registerHandler(new GetAccountQuery(accRepo))
            .registerHandler(new GetOweListQuery(oweRepo))
            .registerHandler(new CreateAccountCommand(accRepo))
            .registerHandler(transferCmd)
            .registerHandler(new WithdrawCommand(accRepo))
            .registerHandler(new DepositCommand(accRepo, oweRepo, transferCmd));

        var sessionMgr = new SessionManagerDefault(handlerMgr, 
            arg -> new SessionDefault(arg.accountName, handlerMgr, arg.eventLogout,
                session -> new SessionInputHandlerDefault(session, inputWriter)),
            mgr -> new SessionManagerInputHandlerDefault(mgr, inputWriter));

        var machine = new ATMMachine(sessionMgr, inputReader);

            // 5. run atm machine
        machine.run();
    } 
}
