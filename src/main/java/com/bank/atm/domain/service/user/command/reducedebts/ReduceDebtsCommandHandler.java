package com.bank.atm.domain.service.user.command.reducedebts;

import java.util.stream.Collectors;

import javax.validation.constraints.NotNull;

import com.bank.atm.domain.common.handler.AbstractRequestHandler;
import com.bank.atm.domain.common.handler.HandlerExtensions;
import com.bank.atm.domain.common.handler.RequestHandlerManager;
import com.bank.atm.domain.data.repository.DebtRepository;
import com.bank.atm.domain.mapper.DebtMapper;
import com.bank.atm.domain.service.user.command.reducedebt.ReduceDebtCommand;
import com.bank.atm.domain.service.user.command.reducedebt.ReduceDebtResult;
import lombok.RequiredArgsConstructor;
import lombok.experimental.ExtensionMethod;

@RequiredArgsConstructor
@ExtensionMethod(HandlerExtensions.class)
public class ReduceDebtsCommandHandler 
	extends AbstractRequestHandler<ReduceDebtsCommand, ReduceDebtsResult> {

	@NotNull
	private final RequestHandlerManager manager;

	@NotNull
	private final DebtRepository repo;

	@NotNull
	private final DebtMapper mapper;

  @Override
  public ReduceDebtsResult handle(ReduceDebtsCommand request) throws Exception {
    var debts = repo.getAccountList(request.getAccountName()).stream()
			.map(a -> reduceDebt(request.getAccountName(), a, request))
			.collect(Collectors.toList());

		return ReduceDebtsResult.builder()
			.accountName(request.getAccountName())
			.requestAmount(request.getAmount())
			.reduceDebts(debts)
			.build();
  }

	private ReduceDebtResult reduceDebt(String accountName1, String accountName2, ReduceDebtsCommand request){
		return manager.execute(ReduceDebtCommand.builder()
			.accountName1(accountName1)
			.accountName2(accountName2)
			.amount(request.getAmount())
			.build())
		.map(r -> {
			request.setAmount(r.getRemainder());
			return r;
		})
		.orElse(null);
	}
}