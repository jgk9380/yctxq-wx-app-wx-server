package com.wx.dao;

import com.wx.entity.WxRedPaper;
import org.springframework.data.repository.CrudRepository;

import java.util.Date;
import java.util.List;

public interface WxRedPaperDao  extends CrudRepository<WxRedPaper,Long>{
    WxRedPaper findById(Long Id);
    List<WxRedPaper> findByOpenId(String openId);
    List<WxRedPaper> findBySendDateBetweenAndSendResultEquals(Date d1,Date d2,String success);
}
