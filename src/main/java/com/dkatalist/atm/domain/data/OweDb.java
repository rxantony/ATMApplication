package com.dkatalist.atm.domain.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.dkatalist.atm.domain.common.Guard;

public class OweDb implements Db<OweDb.Key, Owe> {
    private List<Owe> owes = new ArrayList<>();

    @Override
    public Optional<Owe> get(Key key) {
        Optional<Owe> oowed = getTansaction(key.account1, key.account2);
        if (!oowed.isPresent())
            return oowed;

        return Optional.of(new Owe(oowed.get()));
    }

    @Override
    public void add(Owe owe) {
        owes.add(new Owe(owe));
    }

    @Override
    public boolean update(Owe owe) {
        Optional<Owe> oowe = getTansaction(owe.getAccount1(), owe.getAccount2());
        if (!oowe.isPresent())
            return false;

        Owe iowe = oowe.get();
        iowe.setAmount(owe.getAmount());
        return true;
    }

    @Override
    public boolean delete(Key key) {
        Optional<Owe> oowe = getTansaction(key.account1, key.account2);
        if (oowe.isPresent())
            return false;

        owes.remove(oowe.get());
        return true;
    }

    @Override
    public List<Owe> where(Predicate<Owe> filter) {
        return owes.stream().filter(filter).map(Owe::new).collect(Collectors.toList());
    }

    @Override
    public Optional<Owe> first(Predicate<Owe> filter) {
        Optional<Owe> oowe =  owes.stream().filter(filter).findFirst();
        if(!oowe.isPresent())
            return oowe;
        return Optional.of(new Owe(oowe.get()));
    }
    
    @Override
    public boolean exists(Predicate<Owe> filter) {
        return owes.stream().filter(filter).count() > 0;
    }

    private Optional<Owe> getTansaction(String account1, String accoun2){
        return owes.stream()
            .filter(o -> o.getAccount1().equals(account1) && o.getAccount2().equals(accoun2)).findFirst();
    }

    public static class Key {
        public final String account1;
        public final String account2;

        public Key(String account1, String account2) {
            Guard.validateArgNotNullOrEmpty(account1, "account1");
            Guard.validateArgNotNullOrEmpty(account2, "account2");

            this.account1 = account1;
            this.account2 = account2;
        }
    }
}
