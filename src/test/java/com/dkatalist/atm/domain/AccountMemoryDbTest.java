package com.dkatalist.atm.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.util.Optional;

import com.dkatalist.atm.domain.data.Account;
import com.dkatalist.atm.domain.data.AccountDb;

import org.junit.jupiter.api.Test;

class AccountMemoryDbTest {

    @Test
    void getAddUpdate() {
        AccountDb db = new AccountDb();
        Optional<Account> acc = db.get("account1");
        boolean update1 = db.update(new Account("account2", 200));

        db.add(new Account("account1", 100));
        Optional<Account> acc2 = db.get("account1");
        boolean update2 = db.update(new Account("account1", 200));
        Optional<Account> acc3 = db.get("account1");

        assertFalse(acc.isPresent());
        assertFalse(update1);
        assertTrue(acc2.isPresent());
        assertTrue(update2);
        assertTrue(acc3.isPresent());
        assertEquals(200, acc3.get().getSaving());
    }
}
