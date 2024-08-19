package com.bank.atm.domain.service.account.command.createaccount;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.bank.atm.domain.data.dto.AccountDto;
import com.bank.atm.domain.data.model.Account;
import com.bank.atm.domain.mapper.AccountMapper;

@Mapper
public interface CreateAccountMapper {
  AccountMapper ACCOUNT_MAPPER_INSTANCE = Mappers.getMapper( AccountMapper.class);
  CreateAccountMapper INSTANCE = Mappers.getMapper( CreateAccountMapper.class);
  
  AccountDto toDto(CreateAccountCommand request);
  Account toModel(CreateAccountCommand request);
}
