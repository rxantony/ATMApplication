package com.dkatalist.atm.domain.service.oweCallculation;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import com.dkatalist.atm.domain.data.Account;
import com.dkatalist.atm.domain.data.Owe;
import com.dkatalist.atm.domain.data.OweRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

class ReduceOweFromServiceTest extends AbstractReduceOweServiceTest {

    @Test
    void reduceOweFromWithNoOweToAndNoNextProccReturnSameValueAsAmountArg(){
        calculate(null, 10, (r, n)-> new ReduceOweFromService(r, n));
    }

    @Test
    void reduceOweFromWithNoOweToAndWithNextProccReturnSameValueAsNextProccReturn(){
        var next = mock(OweCalculationService.class);
        when(next.calculate(any(), any(), anyInt(), anyList())).thenReturn(20);
        calculate(next, 20, (r, n)-> new ReduceOweFromService(r, n));
    }  

    static Stream<OweCalculationService> nextProcces() {
        return Stream.of(null, mock(OweCalculationService.class));
    }

    @ParameterizedTest
    @MethodSource("nextProcces")
    void reduceCurrentOweFromWithAmountLtCurrentOweFrom(OweCalculationService next){
        var bobAcc = new Account(bob, 180);
        var aliceAcc =   new Account(alice, 0);
        var oweList = new ArrayList<Owe>();
        var result = reduceCurrentOweFrom(bobAcc, aliceAcc, oweList, 30, next);

        assertEquals(180, bobAcc.getBalance());
        assertEquals(0, aliceAcc.getBalance());
        assertEquals(0, result);
        
        assertEquals(bob, oweList.get(1).getAccount1());
        assertEquals(alice, oweList.get(1).getAccount2());
        assertEquals(90, oweList.get(1).getAmount());

        assertEquals(alice, oweList.get(0).getAccount1());
        assertEquals(bob, oweList.get(0).getAccount2());
        assertEquals(-90, oweList.get(0).getAmount());
    }

    @ParameterizedTest
    @MethodSource("nextProcces")
    void reduceCurrentOweFromWithAmountGteCurrentOweFrom(OweCalculationService next){
        var bobAcc = new Account(bob, 180);
        var aliceAcc =   new Account(alice, 0);
        var oweList = new ArrayList<Owe>();
        var result = reduceCurrentOweFrom(bobAcc, aliceAcc, oweList, 130, next);

        assertEquals(180, bobAcc.getBalance());
        assertEquals(0, aliceAcc.getBalance());
        assertEquals(10, result);
        
        assertEquals(bob, oweList.get(1).getAccount1());
        assertEquals(alice, oweList.get(1).getAccount2());
        assertEquals(0, oweList.get(1).getAmount());

        assertEquals(alice, oweList.get(0).getAccount1());
        assertEquals(bob, oweList.get(0).getAccount2());
        assertEquals(0, oweList.get(0).getAmount());
    }

    int reduceCurrentOweFrom(Account bobAcc, Account aliceAcc, List<Owe> oweList, int amount,  OweCalculationService next){
        var currentBobOweFromAlice = Optional.of(new Owe(bob, alice, 120));
        var currentAliceOweToBob = Optional.of(new Owe(alice, bob, -120));

        var oweRepo = mock(OweRepository.class);
        when(oweRepo.getOweTo(anyString(), anyString())).thenReturn(currentAliceOweToBob);
        when(oweRepo.getOweFrom(anyString(), anyString())).thenReturn(currentBobOweFromAlice);

        var service = new ReduceOweFromService(oweRepo, next);
        return service.calculate(bobAcc, aliceAcc, amount, oweList);
    }
}
