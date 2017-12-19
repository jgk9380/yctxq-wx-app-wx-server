package com.wx.dao;

import com.wx.entity.WxResource;
import org.springframework.data.repository.CrudRepository;

public interface WxResourceDao extends CrudRepository<WxResource,Long> {
    WxResource findById(Long id);
}
