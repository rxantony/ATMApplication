package com.bank.atm.domain.service.account.command.createaccount;

import java.util.Optional;

import javax.validation.constraints.NotNull;

import com.bank.atm.domain.common.handler.AbstractRequestHandler;
import com.bank.atm.domain.data.dto.AccountDto;
import com.bank.atm.domain.data.repository.AccountRepository;
import com.bank.atm.domain.service.mapper.AccountMapper;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CreateAccountCommandHandler 
	extends AbstractRequestHandler<CreateAccountCommand, AccountDto> {

	@NotNull
	private final AccountRepository repo;

	@NotNull
	private final AccountMapper mapper;

	@Override
	public AccountDto handle(CreateAccountCommand request) {
		return repo.get(request.getName())
				.filter(a -> a != null)
				.map(mapper::toDto)
				.orElseGet(() -> Optional.of(request)
						.map(mapper::toModel)
						.map(repo::add)
						.map(mapper::toDto)
						.get()
				);
	}
}
