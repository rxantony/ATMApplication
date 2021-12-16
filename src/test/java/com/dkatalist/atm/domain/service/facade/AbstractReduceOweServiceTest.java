package com.dkatalist.atm.domain.service.facade;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Optional;
import java.util.function.BiFunction;

import com.dkatalist.atm.domain.data.Account;
import com.dkatalist.atm.domain.data.Owe;
import com.dkatalist.atm.domain.data.OweRepository;
import com.dkatalist.atm.domain.service.OweCalculationService;

abstract class AbstractReduceOweServiceTest {

    protected static final String bob = "bob";
    protected static final String alice = "alice";
    protected static final Optional<Owe> emptyOwe = Optional.empty();
    
    void calculate(OweCalculationService next, int expected, BiFunction<OweRepository, OweCalculationService, OweCalculationService> factory){
        var bobAcc = mock(Account.class);
        var aliceAcc =  mock(Account.class);

        var oweRepo = mock(OweRepository.class);
        when(oweRepo.getOweTo(anyString(), anyString())).thenReturn(emptyOwe);

        var oweList = new ArrayList<Owe>(0);
        var service = factory.apply(oweRepo, next);
        var result = service.calculate(bobAcc, aliceAcc, expected, oweList);

        assertEquals(expected, result);
    }
}
