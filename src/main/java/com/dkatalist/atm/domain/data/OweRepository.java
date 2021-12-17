package com.dkatalist.atm.domain.data;

import java.util.List;
import java.util.Optional;

public interface OweRepository {
    void add(Owe... owe);

    void update(Owe... owes);

    Optional<Owe> get(String accountName1, String accountName2);

    Optional<Owe> getOweTo(String accountName1, String accountName2);

    Optional<Owe> getOweFrom(String accountName1, String accountName2);

    List<Owe> getList(String accountName);

    List<Owe> getOweToList(String accountName);
}
