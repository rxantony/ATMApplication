package com.bank.atm.domain.service.user.command.transfer;

import javax.validation.constraints.Min;
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
public class TransferCommand implements Request<TransferResult> {
	@NotBlank
	private String accountName;

	@NotBlank
	private String recipientName;

	@Min(1)
	private int amount;
}
