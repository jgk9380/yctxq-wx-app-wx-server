package com.wx.dao;

import com.wx.entity.WxFeeOrder;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface WxFeeOrderDao extends CrudRepository<WxFeeOrder,String> {
    List<WxFeeOrder> findAllByStatus(int status);
}
