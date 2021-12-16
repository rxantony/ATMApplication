package com.dkatalist.atm.domain.service;

import java.util.List;

import com.dkatalist.atm.domain.common.Guard;
import com.dkatalist.atm.domain.data.Account;
import com.dkatalist.atm.domain.data.Owe;
import com.dkatalist.atm.domain.data.OweRepository;

public class ReduceOweFromService implements OweCalculationService {
    private OweRepository repo;
    private OweCalculationService next;

    public ReduceOweFromService(OweRepository repo, OweCalculationService next) {
        Guard.validateArgNotNull(repo, "repo");
        this.repo = repo;
        this.next = next;
    }

    @Override
    public int calculate(Account account, Account recipient, int amount, List<Owe> oweList) {
        var ooweFrom = repo.getOweFrom(account.getName(), recipient.getName());
        if (!ooweFrom.isPresent()) {
            if (next != null)
                return next.calculate(account, recipient, amount, oweList);
            return amount;
        }

        var oweFrom = ooweFrom.get();
        var oweTo = repo.getOweTo(oweFrom.getAccount2(), oweFrom.getAccount1()).get();
        if (amount < oweFrom.getAmount()) {//
            oweFrom.setAmount(oweFrom.getAmount() - amount);
            oweTo.setAmount(oweTo.getAmount() + amount);
            amount = 0;
        } else {
            amount -= oweFrom.getAmount();
            oweFrom.setAmount(0);
            oweTo.setAmount(0);
        }

        repo.update(oweTo, oweFrom);

        oweList.add(oweTo);
        oweList.add(oweFrom);

        return amount;
    }

}
