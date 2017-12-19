package com.wx.dao;

import com.wx.entity.WxManualMessage;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface WxManualMessageDao extends CrudRepository<WxManualMessage,Integer> {
    WxManualMessage findById(int id);
    //List<WxManualMessage> findByWxUserIdOrderByReceivedDate(int wxUserId);
//    @Query("select o.wxUserId,count(o.content) from WxManualMessage o where o.replyFlag=0")
//    List<Map<String,Integer>> findToReplayUsers();
//查找某个人的某个时间前上行未读信息
   @Query("select o from WxManualMessage o where o.sender=:sender and o.readed=0 and o.sendDate<:now")
   List<WxManualMessage> findBySenderAndReadedAndSendDateBefore(@Param("sender") String sender, @Param("now") Date date);
}
