package com.dkatalist.atm.domain.service.oweCallculation;

import java.util.List;

import com.dkatalist.atm.domain.common.Guard;
import com.dkatalist.atm.domain.data.Account;
import com.dkatalist.atm.domain.data.Owe;
import com.dkatalist.atm.domain.data.OweRepository;

public class RequestOweToService implements OweCalculationService {
    private OweRepository repo;
    private OweCalculationService next;

    public RequestOweToService(OweRepository repo, OweCalculationService next) {
        Guard.validateArgNotNull(repo, "repo");
        this.repo = repo;
        this.next = next;
    }

    @Override
    public int calculate(Account account, Account recipient, int amount, List<Owe> oweList) {
        if (amount <= account.getBalance()) {
            if (next != null)
                return next.calculate(account, recipient, amount, oweList);
            return amount;
        }

        Owe oweTo = null;
        Owe oweFrom = null;
        var oweAmount = amount - account.getBalance();
        var ooweTo = repo.getOweTo(account.getName(), recipient.getName()); // search our owe to recipient // took place before
        if (ooweTo.isPresent()) {
            oweTo = ooweTo.get();
            oweFrom = repo.getOweFrom(recipient.getName(),account.getName()).get();
            oweTo.setAmount(oweTo.getAmount() - oweAmount);
            oweFrom.setAmount(oweFrom.getAmount() + oweAmount);
            repo.update(oweTo, oweFrom);
        } else {
            oweTo = new Owe(account.getName(), recipient.getName(), -oweAmount);
            oweFrom = new Owe(recipient.getName(), account.getName(), oweAmount);
            repo.add(oweTo, oweFrom);
        }

        oweList.add(oweTo);
        oweList.add(oweFrom);
        return account.getBalance();
    }

}
