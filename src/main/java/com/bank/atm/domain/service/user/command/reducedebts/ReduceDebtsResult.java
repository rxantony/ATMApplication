package com.bank.atm.domain.service.user.command.reducedebts;

import java.util.Optional;

import com.bank.atm.domain.service.user.command.reducedebt.ReduceDebtResult;

import static com.bank.atm.domain.common.Utils.streamFrom;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Singular;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReduceDebtsResult {

  private String accountName;

  private int requestAmount;

  @Singular
  private Iterable<ReduceDebtResult> reduceDebts; 

  public int getTotalAmount(){
    return Optional.ofNullable(reduceDebts) 
    .flatMap(c -> streamFrom(c)
      .map(d -> Math.abs(d.getAmount()))
      .reduce((x,y)-> x+y))
    .orElse(0);
  }
}
