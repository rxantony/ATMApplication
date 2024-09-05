package com.bank.atm.domain.service.debt.query.getaccount2List;

import java.util.Collection;
import javax.validation.constraints.NotNull;

import com.bank.atm.domain.common.handler.AbstractRequestHandler;
import com.bank.atm.domain.data.repository.DebtRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GetAccount2ListQueryHandler 
  extends AbstractRequestHandler<GetAccount2ListQuery, Collection<String>> {

  @NotNull
  private final DebtRepository repo;

  @Override
  public Collection<String> handle(GetAccount2ListQuery request) throws Exception {
    return repo.getAccount2List(request.getAccountName());
  }
  
}
