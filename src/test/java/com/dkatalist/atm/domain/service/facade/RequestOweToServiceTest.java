package com.dkatalist.atm.domain.service.facade;

import static org.junit.Assert.assertTrue;
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
import com.dkatalist.atm.domain.service.OweCalculationService;
import com.dkatalist.atm.domain.service.RequestOweToService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

class RequestOweToServiceTest {

    static final String bob = "bob";
    static final String alice = "alice";
    static final Optional<Owe> emptyOwe = Optional.empty();
    static final List<Owe> emptyOweList = new ArrayList<Owe>(0);

    @Test
    void requestForOweWithAmountLtBalanceAndWithNoNextProcc(){
        var bobAcc = new Account(bob, 40);
        var aliceAcc =   new Account(alice, 80);
        var oweList = calculate(null, bobAcc, aliceAcc, 10, 10);
        
        assertEquals(40, bobAcc.getBalance());
        assertEquals(80, aliceAcc.getBalance());
        assertTrue(oweList.isEmpty());
    }

    @Test
    void requestForOweWithAmountLtBalanceAndWithNextProcc(){
        var bobAcc = new Account(bob, 700);
        var aliceAcc =   new Account(alice, 500);

        var next = mock(OweCalculationService.class);
        when(next.calculate(any(), any(), anyInt(), anyList())).thenReturn(70);

        var oweList = calculate(next, bobAcc, aliceAcc, 20, 70);
        assertEquals(700, bobAcc.getBalance());
        assertEquals(500, aliceAcc.getBalance());
        assertTrue(oweList.isEmpty());
    }

    List<Owe> calculate(OweCalculationService next, Account bobAcc, Account aliceAcc, int amount, int expected){
        var oweRepo = mock(OweRepository.class);
        var oweList = new ArrayList<Owe>();
        var service = new RequestOweToService(oweRepo, next);
        var result = service.calculate(bobAcc, aliceAcc, amount, oweList);

        assertEquals(expected, result);
        return oweList;
    }


    static Stream<OweCalculationService> nextProcces() {
        return Stream.of(null, mock(OweCalculationService.class));
    }

    @ParameterizedTest
    @MethodSource("nextProcces")
    void requestForOweWithAmountGteBalanceAndHaveNoPreviosOweTo(OweCalculationService next){
        var bobAcc = new Account(bob, 100);
        var aliceAcc =   new Account(alice, 180);

        var oweRepo = mock(OweRepository.class);
        when(oweRepo.getOweTo(anyString(), anyString())).thenReturn(emptyOwe);

        var oweList = new ArrayList<Owe>();
        var service = new RequestOweToService(oweRepo, next);
        var result = service.calculate(bobAcc, aliceAcc, 120, oweList);

        assertEquals(100, bobAcc.getBalance());
        assertEquals(180, aliceAcc.getBalance());
        assertEquals(bobAcc.getBalance(), result);
        
        assertEquals(bob, oweList.get(0).getAccount1());
        assertEquals(alice, oweList.get(0).getAccount2());
        assertEquals(-20, oweList.get(0).getAmount());

        assertEquals(alice, oweList.get(1).getAccount1());
        assertEquals(bob, oweList.get(1).getAccount2());
        assertEquals(20, oweList.get(1).getAmount());
    }

    @ParameterizedTest
    @MethodSource("nextProcces")
    void requestForOweWithAmountGteBalanceAndHavePreviosOweTo(OweCalculationService next){
        var bobAcc = new Account(bob, 0);
        var aliceAcc =   new Account(alice, 180);
        var currentBobOweToAlice = Optional.of(new Owe(bob, alice, -30));
        var currentAliceOweToBob = Optional.of(new Owe(alice, bob, 30));

        var oweRepo = mock(OweRepository.class);
        when(oweRepo.getOweTo(anyString(), anyString())).thenReturn(currentBobOweToAlice);
        when(oweRepo.getOweFrom(anyString(), anyString())).thenReturn(currentAliceOweToBob);

        var oweList = new ArrayList<Owe>();
        var service = new RequestOweToService(oweRepo, next);
        var result = service.calculate(bobAcc, aliceAcc, 120, oweList);

        assertEquals(0, bobAcc.getBalance());
        assertEquals(180, aliceAcc.getBalance());
        assertEquals(bobAcc.getBalance(), result);
        
        assertEquals(bob, oweList.get(0).getAccount1());
        assertEquals(alice, oweList.get(0).getAccount2());
        assertEquals(-150, oweList.get(0).getAmount());

        assertEquals(alice, oweList.get(1).getAccount1());
        assertEquals(bob, oweList.get(1).getAccount2());
        assertEquals(150, oweList.get(1).getAmount());
    }
}
