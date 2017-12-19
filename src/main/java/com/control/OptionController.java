package com.control;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//import javax.faces.model.SelectItem;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Created by jianggk on 2017/2/13.
 */

//@RestController
//@RequestMapping("/option")
public class OptionController {
//    @Autowired
//    @Qualifier("primaryJdbcTemplate")
//    JdbcTemplate jdbcTemplate;
//
//    @RequestMapping("/depart")//部门
//    List<SelectItem> getDepartSelectItems(){
//        return null;
//    }
//    @RequestMapping("/city")//业务单元
//    List<SelectItem> getcitySelectItems(){
//        return null;
//    }
//    @RequestMapping("/grid")//网格
//    List<SelectItem> getGridSelectItems(){
//        return null;
//    }
//    @RequestMapping("/channel")//渠道
//    List<SelectItem> getChannelSelectItems(){
//        return null;
//    }
//    @RequestMapping("/position")//岗位
//    List<SelectItem> getPositionSelectItems(){
//        return null;
//    }
//    @RequestMapping("/cityManager")//业务单元负责人
//    List<SelectItem> getCityManagerSelectItems(){
//        return null;
//    }
//    @RequestMapping("/cityStockManager")//业务单元存量推进人
//    List<SelectItem> getCityStockManagerSelectItems(){
//        return null;
//    }
//    @RequestMapping("/gridManager")//网格负责人
//    List<SelectItem> getGridManagerSelectItems(){
//        return null;
//    }
//    @RequestMapping("/channelManager/{channelId}")//渠道负责人,根据ID，自建选店长，共建选渠道经理。
//    List<SelectItem> getChannelManagerSelectItems(){
//        return null;
//    }
//    @RequestMapping("/bySql/{sql}")//渠道负责人,根据ID，自建选店长，共建选渠道经理。
//
//    List<SelectItem> getOpenBySqlSelectItems(@PathVariable("sql") String sql){
//        //sql需要中含有 label 和 value 字段。
//            List<Map<String,Object>> data=jdbcTemplate.queryForList(sql);
//        List<SelectItem> res=new ArrayList<>();
//        for(Map<String,Object> m:data){
//            SelectItem selectItem=new SelectItem();
//            selectItem.setLabel(m.get("label").toString());
//            selectItem.setValue( m.get("value").toString());
//            res.add(selectItem);
//        }
//        return res;
//    }

}
