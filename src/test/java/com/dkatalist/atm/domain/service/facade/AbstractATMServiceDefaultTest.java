package com.dkatalist.atm.domain.service.facade;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.dkatalist.atm.domain.data.Account;
import com.dkatalist.atm.domain.data.AccountRepository;
import com.dkatalist.atm.domain.data.Owe;
import com.dkatalist.atm.domain.data.OweRepository;
import com.dkatalist.atm.domain.service.oweCallculation.OweCalculationService;

public abstract class AbstractATMServiceDefaultTest {
    protected final static String bob = "bob";
    protected final static String alice = "alice";
    protected final Optional<Owe> noOweFrom = Optional.empty();
    protected final List<Owe> emptyOweList = new ArrayList<Owe>();

    protected AbstractATMServiceDefaultTest() {
    }

    protected ATMServiceDefault crerateEmptyATMServive() {
        var account = Optional.of(new Account(bob, 500));
        var accRepo = mock(AccountRepository.class);
        when(accRepo.get(bob)).thenReturn(account);
            
        var oweService = mock(OweCalculationService.class);
        var oweRepo = mock(OweRepository.class);
        return new ATMServiceDefault(accRepo, oweRepo, oweService);
    }
}
