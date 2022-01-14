package com.dkatalist.atm.domain.service;

import com.dkatalist.atm.domain.data.Owe;

public interface OweListResult {
    String getAccountName();

    Iterable<Owe> getOwes();
}
