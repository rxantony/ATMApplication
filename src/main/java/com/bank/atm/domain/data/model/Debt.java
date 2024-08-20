package com.bank.atm.domain.data.model;

import java.util.Date;

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
	private String accountName1;
	private String accountName2;
	private Date createdAt;
	private int amount;

	public Debt(Debt o) {
		accountName1 = o.getAccountName1();
		accountName2 = o.getAccountName2();
		createdAt = o.getCreatedAt();
		amount = o.getAmount();
	}
}
