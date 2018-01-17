package com.wx.dao;

import com.wx.entity.WxOrderTele;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface WxOrderTeleDao extends CrudRepository<WxOrderTele ,Long> {
    List<WxOrderTele> findAllByTele(String tele);
}
