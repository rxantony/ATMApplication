package com.bank.atm.domain.service.user.command.updatedebts;

import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

import javax.validation.constraints.NotNull;

import com.bank.atm.domain.common.handler.AbstractRequestHandler;
import com.bank.atm.domain.data.repository.DebtRepository;
import com.bank.atm.domain.service.dto.DebtDto;
import com.bank.atm.domain.service.mapper.DebtMapper;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UpdateDebtsCommandHandler 
  extends AbstractRequestHandler<UpdateDebtsCommand, Collection<DebtDto>> {

  @NotNull
  private final DebtRepository repo;

  @NotNull
	private final DebtMapper mapper;

  @Override
  public Collection<DebtDto> handle(UpdateDebtsCommand request) throws Exception {
    return Optional.of(request.getDebts())
      .map(mapper::toModels)
      .map(repo::update)
      .map(mapper::toDtos)
      .orElseGet(()-> Arrays.asList());
  }
}
