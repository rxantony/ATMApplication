package com.bank.atm.domain.service.debt.command.adddebts;

import java.util.Collection;
import java.util.Optional;

import javax.validation.constraints.NotNull;

import com.bank.atm.domain.common.handler.AbstractRequestHandler;
import com.bank.atm.domain.data.dto.DebtDto;
import com.bank.atm.domain.data.repository.DebtRepository;
import com.bank.atm.domain.service.mapper.DebtMapper;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AddDebtsCommandHandler
		extends AbstractRequestHandler<AddDebtsCommand, Collection<DebtDto>> {

	@NotNull
	private final DebtRepository repo;

	@NotNull
	private final DebtMapper mapper;

	@Override
	public Collection<DebtDto> handle(AddDebtsCommand request) throws Exception {
		return Optional.of(request.getDebts())
				.map(mapper::toModels)
				.map(repo::add)
				.map(r -> mapper.toDtos(r))
				.orElse(null);
	}
}
