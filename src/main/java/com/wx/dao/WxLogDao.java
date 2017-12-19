package com.wx.dao;

import com.wx.entity.WxLog;
import org.springframework.data.repository.CrudRepository;

public interface WxLogDao extends CrudRepository<WxLog,Integer> {
}
