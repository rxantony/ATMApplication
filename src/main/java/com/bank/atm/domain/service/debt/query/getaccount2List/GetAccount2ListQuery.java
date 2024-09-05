package com.bank.atm.domain.service.debt.query.getaccount2List;

import java.util.Collection;

import javax.validation.constraints.NotBlank;

import com.bank.atm.domain.common.handler.Request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetAccount2ListQuery implements Request<Collection<String>> {
  @NotBlank
  private String accountName;
}
