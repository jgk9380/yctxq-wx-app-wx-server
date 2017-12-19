package com.wx.dao;


import com.wx.entity.WxApp;
import org.springframework.data.repository.CrudRepository;
//import mid.springframework.stereotype.Repository;



public interface WxAppDao extends CrudRepository<WxApp, String> {
    
    WxApp findById(String id);

    WxApp findByAppName(String name);
    
//    WxApp findByUserName(String userName);
}
