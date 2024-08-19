package com.bank.atm.domain.data.model;

import java.util.Date;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
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
public class Debt {
	@NotBlank
	private String accountName1;

	@NotBlank
	private String accountName2;

	@NotNull
	private Date createdAt;

	@Min(0)
	private int amount;

	public Debt(Debt o) {
		accountName1 = o.getAccountName1();
		accountName2 = o.getAccountName2();
		createdAt = o.getCreatedAt();
		amount = o.getAmount();
	}
}
