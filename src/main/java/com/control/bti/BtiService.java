package com.control.bti;

import com.dao.p.BtiAgentDao;
import com.dao.p.BtiOrderDao;
import com.entity.p.BtiOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class BtiService {
    @Autowired
    private BtiOrderDao btiOrderDao;
    @Autowired
    private BtiAgentDao btiAgentDao;


    @Transactional
    public List<BtiOrder> getUncheckedOrder(String loingId) {
        List<BtiOrder> l2 = btiOrderDao.findCheckingOrder(loingId);
        if(l2.size()>=5)
            return l2;
        int size=5-l2.size();
        Pageable pageable = new PageRequest(0, size);
        List<BtiOrder> l1 = btiOrderDao.findUnCheckOrder(loingId, pageable).getContent();
        for (BtiOrder bo : l1) {
            if (null == bo.getCheckerLoginUserId()) {
                bo.setCheckerLoginUserId(loingId);
                btiOrderDao.save(bo);
            }
        }

        List result = new ArrayList();
        //l1.addAll(l2);//测试Transtraction用
        result.addAll(l2);
        result.addAll(l1);
        return result;
    }


}
