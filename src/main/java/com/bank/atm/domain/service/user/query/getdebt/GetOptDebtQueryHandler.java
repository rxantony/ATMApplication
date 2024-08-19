package com.bank.atm.domain.service.user.query.getdebt;

import java.util.Optional;

import javax.validation.constraints.NotNull;

import com.bank.atm.domain.common.handler.AbstractRequestHandler;
import com.bank.atm.domain.data.repository.DebtRepository;
import com.bank.atm.domain.service.dto.DebtDto;
import com.bank.atm.domain.service.mapper.DebtMapper;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GetOptDebtQueryHandler  
  extends AbstractRequestHandler<GetOptDebtQuery, Optional<DebtDto>> {

  @NotNull
  private final DebtRepository repo;

  @NotNull
  private final DebtMapper mapper;

  @Override
  public Optional<DebtDto> handle(GetOptDebtQuery request) throws Exception {
    if(request.isDebtor()){
      return repo.getDebtTo(request.getAccountName1(), request.getAccountName2())
      .map(mapper::toDto);
    }
    return repo.getDebtFrom(request.getAccountName1(), request.getAccountName2())
      .map(mapper::toDto);
  }
}
