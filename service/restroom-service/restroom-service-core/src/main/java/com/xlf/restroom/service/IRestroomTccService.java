package com.xlf.restroom.service;

import com.xlf.restroom.api.IRestroomService;
import com.xlf.restroom.pojo.Toilet;
import io.seata.rm.tcc.api.BusinessActionContext;
import io.seata.rm.tcc.api.BusinessActionContextParameter;
import io.seata.rm.tcc.api.LocalTCC;
import io.seata.rm.tcc.api.TwoPhaseBusinessAction;

// 必须加在接口上
@LocalTCC
public interface IRestroomTccService extends IRestroomService {

    // Tcc关键注解
    @TwoPhaseBusinessAction(name = "releaseTcc", commitMethod = "releaseCommit", rollbackMethod = "releaseCancel")
    // 此注解将id放入上下文中，从actionContext直接获取
    public Toilet releaseTcc(@BusinessActionContextParameter(paramName = "id") Long id);

    public boolean releaseCommit(BusinessActionContext actionContext);

    public boolean releaseCancel(BusinessActionContext actionContext);

}
