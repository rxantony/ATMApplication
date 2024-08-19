package com.bank.atm.domain.service.mapper;

import java.util.Collection;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.bank.atm.domain.data.dto.DebtDto;
import com.bank.atm.domain.data.model.Debt;
import com.bank.atm.domain.service.debt.command.requestdebt.RequestDebtCommand;

@Mapper
public interface DebtMapper {
  DebtMapper INSTANCE = Mappers.getMapper( DebtMapper.class);

  DebtDto toDto(Debt model);
  Collection<DebtDto> toDtos(Collection<Debt> models);
  
  Debt toModel(DebtDto dto);
  Debt toModel(RequestDebtCommand request);
  Collection<Debt> toModels(Collection<DebtDto> models);
}