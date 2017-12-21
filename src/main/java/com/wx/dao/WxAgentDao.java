package com.wx.dao;

import com.wx.entity.WxAgent;
import org.springframework.data.repository.CrudRepository;

public interface WxAgentDao extends CrudRepository<WxAgent,Long> {
    WxAgent findById(Long id);
}
