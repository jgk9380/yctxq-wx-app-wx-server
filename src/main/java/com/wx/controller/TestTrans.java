package com.wx.controller;

import com.wx.dao.WxUserDao;
import com.wx.entity.WxUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Component

public class TestTrans {
    @Autowired
    WxUserDao wxUserDao;
    //分析：默认spring事务只在发生未被捕获的 RuntimeException 时才回滚。
    //@Transaction和底层sql的实现有无关系？
    @Transactional
    public void ddd(){
        WxUser wxUser=wxUserDao.findById(8346102);
        try {
            System.out.println("begin lock;"+new Date());
            Thread.sleep(90*1000);
            System.out.println("end lock;"+new Date());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        wxUser.setLongName("1:"+new Date());
        wxUserDao.save(wxUser);
        wxUser.setTele("15651554444");
        wxUserDao.save(wxUser);
    }
}


//springboot的transaction不锁定表


//分析：默认spring事务只在发生未被捕获的 RuntimeException 时才回滚。
//        spring aop  异常捕获原理：被拦截的方法需显式抛出异常，并不能经任何处理，这样aop代理才能捕获到方法的异常，才能进行回滚，
// 默认情况下aop只捕获 RuntimeException 的异常，但可以通过配置来捕获特定的异常并回滚
//        换句话说在service的方法中不使用try catch 或者在catch中最后加上throw new runtimeexcetpion（），这样程序异常时才能被aop捕获进而回滚
//        解决办法：
//        1.首先确认数据库支持事务。即为InnoDB。
//        方案一：手动回滚。给注解加上参数如：@Transactional(rollbackFor=Exception.class)
//        方案二：如上述分析。MyException改为继承RuntimeException的异常。并且在service上层要继续捕获这个异常并处理
//        方案三：在service层方法的catch语句中增加：TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();语句，手动回滚，这样上层就无需去处理异常

//
//    锁表查询SQL:
//            SELECT object_name, machine, s.sid, s.serial#
//        FROM gv$locked_object l, dba_objects o, gv$session s
//        WHERE l.object_id　= o.object_id
//        AND l.session_id = s.sid;
//
//
//        找到被锁定的表，解锁
//
//        复制代码 代码如下:
//
//
//        --释放SESSION SQL:
//        --alter system kill session 'sid, serial#';
//        ALTER system kill session '23, 1647';