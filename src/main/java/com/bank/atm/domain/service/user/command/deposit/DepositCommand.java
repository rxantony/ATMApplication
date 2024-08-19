package com.bank.atm.domain.service.user.command.deposit;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

import com.bank.atm.domain.common.handler.Request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class DepositCommand implements Request<DepositResult> {
    @NotBlank
    private String accountName;

    @Min(1)
    private int amount;
}
