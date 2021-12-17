package com.dkatalist.atm.domain.service.atm.query.getOweList;

import java.util.List;

import com.dkatalist.atm.domain.common.Guard;
import com.dkatalist.atm.domain.common.handler.Handler;
import com.dkatalist.atm.domain.data.Owe;
import com.dkatalist.atm.domain.data.OweRepository;

public class GetOweListQuery implements Handler<GetOweListRequest, List<Owe>> {
    private final OweRepository oweRepo;

    public GetOweListQuery(OweRepository oweRepo) {
        Guard.validateArgNotNull(oweRepo, "oweRepo");
        this.oweRepo = oweRepo;
    }

    @Override
    public List<Owe> execute(GetOweListRequest request) {
        return oweRepo.getList(request.getAccountName());
    }
}
