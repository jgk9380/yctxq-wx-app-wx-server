package com.entity.p;

import com.dao.p.BtiAgentDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BtiAgentInitor {
    @Autowired
    BtiAgentDao btiAgentDao;

    public void init() {
        List<BtiAgent> l = btiAgentDao.findAll();
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        for (BtiAgent ba : l) {
            if(null==ba.getSecretId()) {
                String secretId = passwordEncoder.encode("" + ba.getId() + ba.name);
                ba.setSecretId(secretId);
                System.out.println("sec="+secretId+" len="+secretId.length());
                btiAgentDao.save(ba);
            }
        }
    }
}
