package com.bank.atm.domain.service.user.command.adddebts;

import java.util.Optional;

import javax.validation.constraints.NotNull;

import com.bank.atm.domain.common.handler.AbstractVoidRequestHandler;
import com.bank.atm.domain.data.repository.DebtRepository;
import com.bank.atm.domain.service.mapper.DebtMapper;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AddDebtsCommandHandler extends AbstractVoidRequestHandler<AddDebtsCommand> {
  @NotNull
  private final DebtRepository repo;

  @NotNull
	private final DebtMapper mapper;

  @Override
  protected void handleInternal(AddDebtsCommand request) throws Exception {
    Optional.of(request.getDebts())
      .map(mapper::toModels)
      .ifPresent(repo::add);
  }
}
