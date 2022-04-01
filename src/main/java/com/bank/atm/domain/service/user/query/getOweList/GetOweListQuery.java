package com.bank.atm.domain.service.user.query.getOweList;

import java.util.List;

import com.bank.atm.domain.common.Guard;
import com.bank.atm.domain.common.handler.AbstractHandler;
import com.bank.atm.domain.data.Owe;
import com.bank.atm.domain.data.OweRepository;

public class GetOweListQuery extends AbstractHandler<GetOweListRequest, List<Owe>> {
    private final OweRepository oweRepo;

    public GetOweListQuery(OweRepository oweRepo) {
        super(GetOweListRequest.class);
        Guard.validateArgNotNull(oweRepo, "oweRepo");
        this.oweRepo = oweRepo;
    }

    @Override
    public List<Owe> execute(GetOweListRequest request) {
        return oweRepo.getList(request.getAccountName());
    }
}
