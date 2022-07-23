package com.bank.atm.domain.data;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class OweRepositoryDefaultTest {
    static final String bob = "bob";
    static final String alice = "alice";

    @Test
    void addNullOweAndThrowIllegalArumentException(){
        var repo = new DefaultOweRepository();
        var owe = new Owe(bob, alice, 0);
        assertThrows(IllegalArgumentException.class, () ->  repo.add(owe, null));
        assertThrows(IllegalArgumentException.class, () ->  repo.add(null, owe));
    }

    @Test
    void addNewOwesAndGet(){
        var bobOweToAlice = new Owe(bob, alice, -10);
        var aliceOweFromBob = new Owe(alice, bob, 10);
        var repo = new DefaultOweRepository();

        var getBobOweToAlice =  repo.get(bob, alice);
        var getALiceToBob =  repo.get(alice, bob);

        repo.add(bobOweToAlice, aliceOweFromBob);

        var getBobOweToAlice2 =  repo.get(bob, alice);
        var getALiceToBob2 =  repo.get(alice, bob);

        assertTrue(getBobOweToAlice.isEmpty());
        assertTrue(getALiceToBob.isEmpty());

        assertNotNull(getBobOweToAlice2);
        assertNotNull(getALiceToBob2);

        assertEquals(bobOweToAlice.getAccount1(), getBobOweToAlice2.get().getAccount1());
        assertEquals(bobOweToAlice.getAmount(), getBobOweToAlice2.get().getAmount());
        assertEquals(bobOweToAlice.getCreatedAt(), getBobOweToAlice2.get().getCreatedAt());
    }

    @Test
    void addNewOwesGetAndChangeAmountValueDoesNotEffectTheOtherOne(){
        var bobOweToAlice = new Owe(bob, alice, -10);
        var repo = new DefaultOweRepository();

        repo.add(bobOweToAlice);
        var getBobOweToAlice =  repo.get(bob, alice);
        getBobOweToAlice.get().setAmount(100);

        assertEquals(-10, bobOweToAlice.getAmount());
        assertEquals(100, getBobOweToAlice.get().getAmount());
        assertNotEquals(bobOweToAlice.getAmount(), getBobOweToAlice.get().getAmount());
    }

    @Test
    void updateWithNotExistsOweThanThowIllegalArgumentException(){
        var repo = new DefaultOweRepository();
        var owe1 = new Owe(bob, alice, 100);
        var owe2 = new Owe("dylan", alice, 200);
        var owe3 = new Owe(alice, bob, 300);
        repo.add(owe1, owe3);
        assertThrows(IllegalArgumentException.class, () ->  repo.update(owe1, owe2, owe3));
    }

    @Test
    void updateExixtingOwe(){
        var repo = new DefaultOweRepository();
        var bobOweFromAlice = new Owe(bob, alice, 100);
        var dylanOweFromAlice = new Owe("dylan", alice, 200);
        var aliceOweFromBob = new Owe(alice, bob, 300);

        repo.add(bobOweFromAlice, dylanOweFromAlice, aliceOweFromBob);

        bobOweFromAlice.setAmount(600);
        dylanOweFromAlice.setAmount(700);
        aliceOweFromBob.setAmount(800);

        repo.update(bobOweFromAlice, dylanOweFromAlice, aliceOweFromBob);

        var getBobOweFromAlice = repo.get(bob, alice);
        var getDylanOweFromAlice = repo.get("dylan", alice);
        var getAliceOweFromBob = repo.get(alice, bob);

        assertEquals(600, getBobOweFromAlice.get().getAmount());
        assertEquals(700, getDylanOweFromAlice.get().getAmount());
        assertEquals(800, getAliceOweFromBob.get().getAmount());
    }

    @Test
    void getOweFrom(){
        var bobOweFromAlice = new Owe(bob, alice, 10);
        var repo = new DefaultOweRepository();
        repo.add(bobOweFromAlice);
        var getBobOweFromAlice = repo.getOweFrom(bob, alice);

        assertNotNull(getBobOweFromAlice);
    }

    @Test
    void getOweTo(){
        var bobOweToAlice = new Owe(bob, alice, -10);
        var repo = new DefaultOweRepository();
        repo.add(bobOweToAlice);
        var getBobOweToAlice = repo.getOweTo(bob, alice);

        assertNotNull(getBobOweToAlice);
    }

    @Test
    void getOweToListAndCompareItValues(){
        var bobOweToAlice = new Owe(bob, alice, -10);
        var bobOweToAndy = new Owe(bob, "andy", -710);
        var bobOweToDylan = new Owe(bob, "dylan", -410);
        var bobOweFromJosh = new Owe(bob, "josh", 910);
        var bobOweFromJenny = new Owe(bob, "jenny", 0);

        var repo = new DefaultOweRepository();
        repo.add(bobOweToAlice, bobOweToAndy, bobOweFromJosh, bobOweFromJenny, bobOweToDylan);
        var getBobOweToList= repo.getOweToList(bob);

        assertNotNull(getBobOweToList);
        assertEquals(3, getBobOweToList.size());
    }

    @Test
    void getListAndCompareItValues(){
        var bobOweToAlice = new Owe(bob, alice, -10);
        var bobOweFromAndy = new Owe(bob, "andy", 710);
        var bobOweToDylan = new Owe(bob, "dylan", -410);
        var bobOweFromJosh = new Owe(bob, "josh", 910);
        var bobOweFromJenny = new Owe(bob, "jenny", 0);

        var repo = new DefaultOweRepository();
        repo.add(bobOweToAlice, bobOweFromJosh, bobOweFromAndy, bobOweFromJenny, bobOweToDylan);
        var getBobOweToList= repo.getList(bob);
        var bobOweToCount = getBobOweToList.stream().filter(o-> o.getAmount() < 0).count();
        var bobOweFromCount = getBobOweToList.stream().filter(o-> o.getAmount() > 0).count();
        var bobZeroOwe =  getBobOweToList.stream().filter(o-> o.getAmount() == 0).count();
        
        assertNotNull(getBobOweToList);
        assertEquals(5, getBobOweToList.size());
        assertEquals(2, bobOweToCount);
        assertEquals(2, bobOweFromCount);
        assertEquals(1, bobZeroOwe);
    }
}
