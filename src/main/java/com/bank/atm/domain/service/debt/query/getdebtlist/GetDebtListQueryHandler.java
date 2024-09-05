package com.bank.atm.domain.service.debt.query.getdebtList;

import java.util.Collection;
import java.util.stream.Collectors;

import javax.validation.constraints.NotNull;

import com.bank.atm.domain.common.handler.AbstractRequestHandler;
import com.bank.atm.domain.data.dto.DebtDto;
import com.bank.atm.domain.data.repository.DebtRepository;
import com.bank.atm.domain.mapper.DebtMapper;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GetDebtListQueryHandler 
	extends AbstractRequestHandler<GetDebtListQuery, Collection<DebtDto>> {
	
	@NotNull
	private final DebtRepository repo;

	@NotNull
  private final DebtMapper mapper;
	
	@Override
	public Collection<DebtDto> handle(GetDebtListQuery request) {
		if(request.isAll()){
		return repo.getAllList(request.getAccountName()).stream()
			.map(mapper::toDto)
			.collect(Collectors.toList());
		}
		return repo.getList(request.getAccountName()).stream()
			.map(mapper::toDto)
			.collect(Collectors.toList());
	}
}
