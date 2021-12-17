package com.dkatalist.atm;

import com.dkatalist.atm.domain.application.MediaInput;
import com.dkatalist.atm.domain.application.MediaOutput;
import com.dkatalist.atm.domain.application.facade.ATMMachine;
import com.dkatalist.atm.domain.application.facade.SessionDefault;
import com.dkatalist.atm.domain.application.facade.SessionInputHandlerDefault;
import com.dkatalist.atm.domain.application.facade.SessionManagerDefault;
import com.dkatalist.atm.domain.application.facade.SessionManagerInputHandlerDefault;
import com.dkatalist.atm.domain.data.AccountRepositoryDefault;
import com.dkatalist.atm.domain.data.OweRepositoryDefault;
import com.dkatalist.atm.domain.service.facade.ATMServiceDefault;
import com.dkatalist.atm.domain.service.facade.AccountServiceDefault;
import com.dkatalist.atm.domain.service.oweCallculation.ReduceOweFromService;
import com.dkatalist.atm.domain.service.oweCallculation.ReduceOweToService;
import com.dkatalist.atm.domain.service.oweCallculation.RequestOweToService;

public class FacadeATMMachineRunner implements ATMMachineRunner{
    @Override
    public void run(MediaInput inputReader, MediaOutput inputWriter) {
        // 1. repositories
        var accRepo = new AccountRepositoryDefault();
        var oweRepo = new OweRepositoryDefault();

        // 2. create services
        var callculationSrv = new ReduceOweFromService(oweRepo
                , new ReduceOweToService(oweRepo
                    , new RequestOweToService(oweRepo, null)));
        var accService = new AccountServiceDefault(accRepo);
        var atmService = new ATMServiceDefault(accRepo, oweRepo, callculationSrv);

        // 3. create session manager
        var sessionMgr = new SessionManagerDefault(accService,
                arg -> new SessionDefault(arg.accountName, atmService, accService, arg.eventLogout,
                        session -> new SessionInputHandlerDefault(session, inputWriter)),
                mgr -> new SessionManagerInputHandlerDefault(mgr, inputWriter));

        // 4. create atm machine
        var machine = new ATMMachine(sessionMgr, inputReader);

        // 5. run atm machine
        machine.run();
    } 
}
