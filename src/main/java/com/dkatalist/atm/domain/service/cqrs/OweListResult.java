package com.dkatalist.atm.domain.service.cqrs;

import java.util.List;

import com.dkatalist.atm.domain.data.Owe;

public interface OweListResult {
    String getAccountName();

    List<Owe> getOweList();
}
