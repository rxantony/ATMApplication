package com.bank.atm.domain.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.bank.atm.domain.data.dto.AccountDto;
import com.bank.atm.domain.data.model.Account;

@Mapper
public interface AccountMapper {
  AccountMapper INSTANCE = Mappers.getMapper( AccountMapper.class);
  
  AccountDto toDto(Account model);
}
