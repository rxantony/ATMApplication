package com.bank.atm.domain.service.dto;

import java.util.Date;

import javax.validation.constraints.NotNull;

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
public class DebtDto {

	private String accountName1;

	private String accountName2;

	private int amount;

	@NotNull
	private Date createdAt;

	public DebtDto(DebtDto o) {
		accountName1 = o.getAccountName1();
		accountName2 = o.getAccountName2();
		amount = o.getAmount();
		createdAt = o.getCreatedAt();
	}
}