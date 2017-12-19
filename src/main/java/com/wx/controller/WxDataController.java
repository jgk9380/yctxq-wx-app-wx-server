package com.wx.controller;

import com.wx.dao.WxLogDao;
import com.wx.entity.WxLog;
import com.wx.mid.util.WxUtils;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/public")
public class WxDataController {
    @Autowired
    WxUtils wxUtils;
    @Autowired
    @Qualifier("primaryJdbcTemplate")
    JdbcTemplate jdbcTemplate;
    @Autowired
    WxLogDao wxLogDao;

    @RequestMapping(path = "/data/queryBySql", method = RequestMethod.POST)
    List<Map<String, Object>> getDataBySql(@RequestBody(required = true) Map<String, Object> map) {
        String sql = (String) map.get("sql");
        List<Map<String, Object>> l = jdbcTemplate.queryForList(sql);
        return l;
    }

    @RequestMapping(path = "/log", method = RequestMethod.POST)
    JSONObject log(@RequestBody(required = true) Map<String, Object> map) {
        String keyword = (String) map.get("keyword");
        String logContent = (String) map.get("logContent");
        WxLog wxLog = new WxLog();
        wxLog.setId(wxUtils.getSeqencesValue().intValue());
        wxLog.setKeyword(keyword);
        wxLog.setLogContent(logContent);
        wxLogDao.save(wxLog);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("errorCode", "0");
        return jsonObject;
    }

    @RequestMapping(value = "/MP_verify_LNKwjvrx0iNDE9om.txt", method = {RequestMethod.GET})
    @ResponseBody
    public String oauth(@PathVariable("code") String code) {
        return "LNKwjvrx0iNDE9om";
    }

    @RequestMapping(value = "/", method = {RequestMethod.GET})
    public String transactionTest11() {
        return "";
    }

}
