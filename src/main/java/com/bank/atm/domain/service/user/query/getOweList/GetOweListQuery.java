package com.bank.atm.domain.service.user.query.getowelist;

import java.util.List;
import java.util.stream.Collectors;

import com.bank.atm.domain.common.Guard;
import com.bank.atm.domain.common.handler.AbstractHandler;
import com.bank.atm.domain.data.OweRepository;

public class GetOweListQuery extends AbstractHandler<GetOweListRequest, List<GetOweResult>> {
    private final OweRepository oweRepo;

    public GetOweListQuery(OweRepository oweRepo) {
        super(GetOweListRequest.class);
        Guard.validateArgNotNull(oweRepo, "oweRepo");
        this.oweRepo = oweRepo;
    }

    @Override
    public List<GetOweResult> handle(GetOweListRequest request) {
        var oweList = oweRepo.getList(request.getAccountName());
        return oweList.stream()
                .map(o -> new GetOweResult(o.getAccount1(), o.getAccount2(), o.getAmount(), o.getCreatedAt()))
                .collect(Collectors.toList());
    }
}
