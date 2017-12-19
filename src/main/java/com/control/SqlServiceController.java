package com.control;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Created by jianggk on 2017/2/10.
 */
@RestController
@RequestMapping("/sql")
public class SqlServiceController {
    //提供经营分析端的sql查询及更新服务。
    @Autowired
    @Qualifier("primaryJdbcTemplate")
    JdbcTemplate jdbcTemplate;

    @RequestMapping(value = "/query/{sql}", method = RequestMethod.GET)
    List<Map<String, Object>> getQueryResult(@PathVariable("sql") String sql) {
            //       try {
            List<Map<String, Object>> res = jdbcTemplate.queryForList(sql);
            return res;
            //        //例外应该交给客户端处理
            //                }catch (Exception e){
            //                      new ReponseResult(-1,e.getMessage());
            //                }
    }

    @RequestMapping(value = "/update/{sql}", method = RequestMethod.GET)
    int update(@PathVariable("sql") String sql) {
        return jdbcTemplate.update(sql);
    }
}
