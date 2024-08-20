package com.bank.atm.domain.service.user.command.requestdebt;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.bank.atm.domain.common.handler.Request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestDebtCommand implements Request<RequestDebtResult> {
    @NotNull
    private String accountName1;

    @NotNull
    private String accountName2;
    
    @Min(1)
    private int amount;
}
