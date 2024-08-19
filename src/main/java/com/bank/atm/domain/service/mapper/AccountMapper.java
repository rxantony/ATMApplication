package com.bank.atm.domain.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.bank.atm.domain.data.model.Account;
import com.bank.atm.domain.service.account.command.createaccount.CreateAccountCommand;
import com.bank.atm.domain.service.dto.AccountDto;

@Mapper
public interface AccountMapper {
  AccountMapper INSTANCE = Mappers.getMapper( AccountMapper.class);
  
  AccountDto toDto(Account model);
  AccountDto toDto(CreateAccountCommand request);
  Account toModel(CreateAccountCommand request);
}
