package com.dkatalist.atm.domain.service.facade;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import com.dkatalist.atm.domain.data.Account;
import com.dkatalist.atm.domain.data.Owe;
import com.dkatalist.atm.domain.data.OweRepository;
import com.dkatalist.atm.domain.service.OweCalculationService;
import com.dkatalist.atm.domain.service.ReduceOweToService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

class ReduceOweToServiceTest extends AbstractReduceOweServiceTest {


    @Test
    void reduceOweToWithNoOweToAndNoNextProccReturnSameValueAsAmountArg(){
        calculate(null, 10, (r, n)-> new ReduceOweToService(r, n));
    }

    @Test
    void reduceOweToWithNoOweToAndWithNextProccReturnSameValueAsNextProccReturn(){
        var next = mock(OweCalculationService.class);
        when(next.calculate(any(), any(), anyInt(), anyList())).thenReturn(20);
        calculate(next, 20, (r, n)-> new ReduceOweToService(r, n));
    }  

    static Stream<OweCalculationService> nextProcces() {
        return Stream.of(null, mock(OweCalculationService.class));
    }

    @ParameterizedTest
    @MethodSource("nextProcces")
    void reduceCurrentOweToWithAmountLtCurrentOweTo(OweCalculationService next){
        var bobAcc = new Account(bob, 0);
        var aliceAcc =   new Account(alice, 180);
        var oweList = new ArrayList<Owe>();
        var result = reduceCurrentOweTo(bobAcc, aliceAcc, oweList, 30, next);

        assertEquals(0, bobAcc.getBalance());
        assertEquals(180, aliceAcc.getBalance());
        assertEquals(30, result);
        
        assertEquals(bob, oweList.get(0).getAccount1());
        assertEquals(alice, oweList.get(0).getAccount2());
        assertEquals(-90, oweList.get(0).getAmount());

        assertEquals(alice, oweList.get(1).getAccount1());
        assertEquals(bob, oweList.get(1).getAccount2());
        assertEquals(90, oweList.get(1).getAmount());
    }

    @ParameterizedTest
    @MethodSource("nextProcces")
    void reduceCurrentOweToWithAmountGteCurrentOweTo(OweCalculationService next){
        var bobAcc = new Account(bob, 0);
        var aliceAcc =   new Account(alice, 180);
        var oweList = new ArrayList<Owe>();
        var result = reduceCurrentOweTo(bobAcc, aliceAcc, oweList, 200, next);

        assertEquals(0, bobAcc.getBalance());
        assertEquals(180, aliceAcc.getBalance());
        assertEquals(200, result);
        
        assertEquals(bob, oweList.get(0).getAccount1());
        assertEquals(alice, oweList.get(0).getAccount2());
        assertEquals(0, oweList.get(0).getAmount());

        assertEquals(alice, oweList.get(1).getAccount1());
        assertEquals(bob, oweList.get(1).getAccount2());
        assertEquals(0, oweList.get(1).getAmount());
    }

    int reduceCurrentOweTo(Account bobAcc, Account aliceAcc, List<Owe> oweList, int amount,  OweCalculationService next){
        var currentBobOweToAlice = Optional.of(new Owe(bob, alice, -120));
        var currentAliceOweFromBob = Optional.of(new Owe(alice, bob, 120));

        var oweRepo = mock(OweRepository.class);
        when(oweRepo.getOweTo(anyString(), anyString())).thenReturn(currentBobOweToAlice);
        when(oweRepo.getOweFrom(anyString(), anyString())).thenReturn(currentAliceOweFromBob);

        var service = new ReduceOweToService(oweRepo, next);
        return service.calculate(bobAcc, aliceAcc, amount, oweList);
    }
}
