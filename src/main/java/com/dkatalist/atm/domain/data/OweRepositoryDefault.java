package com.dkatalist.atm.domain.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class OweRepositoryDefault implements OweRepository {
    private final List<Owe> db = new ArrayList<>();

    @Override
    public void add(Owe... owes) {
        for (var owe : owes) {
            if (owe == null)
                throw new IllegalArgumentException("one or some of owes is null");
            db.add(new Owe(owe));
        }
    }

    @Override
    public void update(Owe... owes) {
        for (var owe : owes) {
            var oowe = getOwe(owe.getAccount1(), owe.getAccount2());
            if (!oowe.isPresent())
                throw new IllegalArgumentException("one or some of owes is null");
            var iowe = oowe.get();
            iowe.setAmount(owe.getAmount());
        }
    }

    @Override
    public Optional<Owe> get(String accountName1, String accountName2) {
        var oowed = getOwe(accountName1, accountName2);
        if (!oowed.isPresent())
            return oowed;

        return Optional.of(new Owe(oowed.get()));
    }

    @Override
    public Optional<Owe> getOweTo(String accountName1, String accountName2) {
        return db.stream().filter(
                o -> o.getAccount1().equals(accountName1) && o.getAccount2().equals(accountName2) && o.getAmount() < 0)
                .findFirst();
    }

    @Override
    public Optional<Owe> getOweFrom(String accountName1, String accountName2) {
        return db.stream().filter(
                o -> o.getAccount1().equals(accountName1) && o.getAccount2().equals(accountName2) && o.getAmount() > 0)
                .findFirst();
    }

    @Override
    public List<Owe> getList(String accountName) {
        return db.stream().filter(o -> o.getAccount1().equals(accountName)).map(Owe::new).collect(Collectors.toList());
    }

    @Override
    public List<Owe> getOweToList(String accountName) {
        return db.stream().filter(o -> o.getAccount1().equals(accountName) && o.getAmount() < 0)
                .collect(Collectors.toList());
    }

    private Optional<Owe> getOwe(String account1, String accoun2) {
        return db.stream().filter(o -> o.getAccount1().equals(account1) && o.getAccount2().equals(accoun2)).findFirst();
    }
}
