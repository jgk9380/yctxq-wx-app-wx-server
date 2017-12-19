package com.wx.dao;

import com.wx.entity.WxInterfaceMessage;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface WxInterfaceMessageDao extends CrudRepository<WxInterfaceMessage, Integer> {
    WxInterfaceMessage findById(int id);
    List<WxInterfaceMessage> findByDispDateIsNull();
}
