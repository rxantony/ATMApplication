package com.bank.atm.domain.mapper;

import java.util.Collection;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.bank.atm.domain.data.dto.DebtDto;
import com.bank.atm.domain.data.model.Debt;

@Mapper
public interface DebtMapper {
  DebtMapper INSTANCE = Mappers.getMapper( DebtMapper.class);

  DebtDto toDto(Debt model);
  Collection<DebtDto> toDtos(Collection<Debt> models);
  
  Debt toModel(Debt dto);
  Debt toModel(DebtDto dto);
  Collection<Debt> toModels(Collection<DebtDto> dtos);
}