package com.dkatalist.atm.domain.service;

import java.util.List;

import com.dkatalist.atm.domain.data.Account;
import com.dkatalist.atm.domain.data.Owe;

public interface OweCalculationService {
    int calculate(Account account,  Account recipient, int amount, List<Owe> oweList);
}
