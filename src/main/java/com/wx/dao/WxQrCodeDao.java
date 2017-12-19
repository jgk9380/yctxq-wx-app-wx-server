package com.wx.dao;

import com.wx.entity.WxQrCode;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface WxQrCodeDao extends CrudRepository<WxQrCode,Long> {
    WxQrCode findByWxUserId(Long id);
    List<WxQrCode> findByOwnerIdIsNull();
}
