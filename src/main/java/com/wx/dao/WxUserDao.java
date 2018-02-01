package com.wx.dao;

import java.math.BigDecimal;

import java.util.List;


import com.wx.entity.WxUser;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
//WxUserEvent

public interface WxUserDao extends CrudRepository<WxUser, Long> {
    WxUser findById(long id);

    @Query(value = "select o from WxUser o where o.openId=:openId and o.wxApp.id=:appId")
    WxUser findByAppIdAndOpenId(@Param("appId") String appId, @Param("openId") String openId);


    @Query(value = "select o from WxUser o where (o.nickname like :searchValue or o.tele like :searchValue) and  o.wxApp.id='wx7dcc6b2e03a47c0b'  ")
    List<WxUser> findLikeByTeleOrNickname(@Param("searchValue") String sv);


    @Query(value = "select o from WxUser o where o.openId=:openId and o.wxApp.id='wx7dcc6b2e03a47c0b'")
    WxUser findYctxqWxUserByOpenId(@Param("openId") String openId);

//    @Query(value = "select to_number(a.sender) " +
//            " from (select  sender,max(send_date) send_d from wx_manual_message where type='up'and nvl(readed,0)=0 group by sender)  a," +
//            " (select  receiver,max(send_date)  send_d from wx_manual_message where type='down' group by  receiver) b " +
//            " where a.sender=b.receiver(+) and (a.send_d>b.send_d or b.send_d is null)",nativeQuery=true)
    @Query(value = " select  to_number(sender)  send_d from wx_manual_message where type='up' and nvl(readed,0)=0 group by sender",nativeQuery=true)
//    select  sender  send_d from wx_manual_message where type='up' and nvl(readed,0)=0 group by sender
    List<BigDecimal> findToRebackUser();

    @Query(value = "select o from WxUser o where o.tele=:tele and o.wxApp.id=:appId")
    WxUser findByTeleAndAppId(@Param("tele") String tele, @Param("appId") String appId);

    @Query(value = "select o from WxUser o where o.tele=:tele and o.wxApp.id='wx7dcc6b2e03a47c0b'")
    WxUser findYctxqWxUserByTele(@Param("tele") String tele);
    @Query(value = "select count(o) from WxUser o where o.referee=?1 and o.subscribeStatus=1")
    int getRefereeCount(WxUser wxUser);
    @Query(value = "select count(o) from WxUser o where o.referee=?1 and o.subscribeStatus=-1")
    int getRefereeCacelCount(WxUser wxUser);
    @Query(value = "select o from WxUser o where o.nickname is null and o.subscribeStatus=1 and o.wxApp.id=?1")
    List<WxUser> findNullNickUsers(String appId);
    @Query(value = "select unique referee_id from wx_user where  referee_id is not null",nativeQuery=true)
    List<BigDecimal> findRefereeList();
    @Query(value = "select nickname from wx_user where  referee_id=?1 and (referee_id,subscribe_date) in (select referee_id,max(subscribe_date)    from wx_user group by referee_id)",nativeQuery=true)
    String findLastRefereeName(long id);    
    @Query(value = "select id from Wx_User o where o.subscribe_Status =0 and o.App_id=?1", nativeQuery=true)
    List<BigDecimal> findNoSubUsers(String appId);
    @Query(value="select  id from wx_user where id in (select user_id from wx_user_msg where create_time>(sysdate-2) )",nativeQuery=true)
    List<BigDecimal> find48Users();

//    select id from wx_user a where sysdate-subscribe_date<2/24   and    not exists (select nvl(receiver,0) from wx_manual_message b where type='down' and a.id=b.receiver)
    //新关注未回复用户
    @Query(value = "select id from wx_user a where sysdate-subscribe_date<8/24  \n" +
            "and  subscribe_status=1 and \n" +
            "not exists (select nvl(receiver,0) from wx_manual_message b where type='down' and a.id=b.receiver)",nativeQuery=true)
    List<BigDecimal> findNewUserNoReplay();
    // select * from            ((select to_number(sender),max(send_date) send_d from wx_manual_message  where type='up' group by sender)  union         (select receiver,max(send_date) send_d from wx_manual_message  where type='down'and receiver is not null group by receiver having (sysdate-max(send_date)<24/12 )) )  order by send_d desc
    //历史回复用户+12小时内单向用户
    @Query(value = " select  id from \n" +
            " (\n" +
            "   (select to_number(sender) id,max(send_date) send_d from wx_manual_message  where type='up' group by sender having (sysdate-max(send_date)<2 ) ) \n" +
            "    union     \n" +
            "   (select nvl(receiver,0) id,max(send_date) send_d from wx_manual_message  where type='down'and receiver is not null \n" +
            "   and receiver in (select id from wx_user a where sysdate-subscribe_date<12/24 ) group by receiver having (sysdate-max(send_date)<12/24 )) \n" +
            "  ) \n" +
            " order by send_d desc",nativeQuery=true)
    List<BigDecimal> findHistoryReplayWxUserList();

}
