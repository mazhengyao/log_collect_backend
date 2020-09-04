package com.example.logcollect.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;

// 后台接口 提取redis中的数据
@RestController
@RequestMapping("/log")
public class LogHandler {
    @GetMapping("/queryLog")
    public String queryLog(){
        // redis连接
        Jedis jedis = new Jedis("localhost");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("debug", jedis.hget("logDemo","debug"));
        jsonObject.put("info", jedis.hget("logDemo","info"));
        jsonObject.put("warn", jedis.hget("logDemo","warn"));
        jsonObject.put("error", jedis.hget("logDemo","error"));
        jsonObject.put("fatal", jedis.hget("logDemo","fatal"));

        return JSON.toJSONString(jsonObject);
    }
}