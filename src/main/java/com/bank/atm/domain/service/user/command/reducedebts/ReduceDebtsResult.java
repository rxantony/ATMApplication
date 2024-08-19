package com.bank.atm.domain.service.user.command.reducedebts;

import java.util.Optional;

import static com.bank.atm.domain.common.Utils.streamFrom;
import com.bank.atm.domain.service.user.command.reducedebt.ReduceDebtResult;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Singular;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReduceDebtsResult {

  @Singular
  private Iterable<ReduceDebtResult> reduceDebts; 

  public int getTotalPaidDebtAmount(){
    return Optional.ofNullable(reduceDebts) 
    .flatMap(c -> streamFrom(c)
      .map(d -> Math.abs(d.getAmount()))
      .reduce((x,y)-> x+y))
    .orElse(0);
  }
}
