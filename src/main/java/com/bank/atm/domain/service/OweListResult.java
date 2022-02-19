package com.bank.atm.domain.service;

import com.bank.atm.domain.data.Owe;

public interface OweListResult {
    String getAccountName();

    Iterable<Owe> getOwes();
}
