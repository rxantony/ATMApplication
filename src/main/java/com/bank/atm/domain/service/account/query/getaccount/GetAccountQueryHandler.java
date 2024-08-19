package com.bank.atm.domain.service.account.query.getaccount;

import javax.validation.constraints.NotNull;

import com.bank.atm.domain.common.handler.AbstractRequestHandler;
import com.bank.atm.domain.data.repository.AccountRepository;
import com.bank.atm.domain.service.dto.AccountDto;
import com.bank.atm.domain.service.exception.AccountNotExistsException;
import com.bank.atm.domain.service.mapper.AccountMapper;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GetAccountQueryHandler 
	extends AbstractRequestHandler<GetAccountQuery, AccountDto> {

	@NotNull
	private final AccountRepository repo;

	@NotNull
	private final AccountMapper mapper;

	@Override
	public AccountDto handle(GetAccountQuery request) throws Exception {
		return repo.get(request.getAccountName())
				.map(mapper::toDto)
				.orElseThrow(() -> AccountNotExistsException.create(request.getAccountName()));
	}
}