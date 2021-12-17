package com.dkatalist.atm.domain.service.oweCallculation;

import java.util.List;

import com.dkatalist.atm.domain.common.Guard;
import com.dkatalist.atm.domain.data.Account;
import com.dkatalist.atm.domain.data.Owe;
import com.dkatalist.atm.domain.data.OweRepository;

public class ReduceOweToService implements OweCalculationService {
    private OweRepository repo;
    private OweCalculationService next;

    public ReduceOweToService(OweRepository repo, OweCalculationService next) {
        Guard.validateArgNotNull(repo, "repo");
        this.repo = repo;
        this.next = next;
    }

    @Override
    public int calculate(Account account, Account recipient, int amount, List<Owe> oweList) {
        var ooweTo = repo.getOweTo(account.getName(), recipient.getName());
        if (!ooweTo.isPresent()) {
            if (next != null)
                return next.calculate(account, recipient, amount, oweList);
            return amount;
        }

        var oweTo = ooweTo.get();
        var oweFrom = repo.getOweFrom(oweTo.getAccount2(), oweTo.getAccount1()).get();
        if (amount < oweFrom.getAmount()) {//
            oweTo.setAmount(oweTo.getAmount() + amount);
            oweFrom.setAmount(oweFrom.getAmount() - amount);
        } else {
            oweTo.setAmount(0);
            oweFrom.setAmount(0);
        }

        repo.update(oweTo, oweFrom);

        oweList.add(oweTo);
        oweList.add(oweFrom);

        return amount;
    }

}
