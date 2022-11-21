package com.demo.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@RestController
public class SelectController {

    @Autowired
    private RedisTemplate redisTemplate;

    @Value("#{'${mental}'.split(' ')}")
    private ArrayList<String> mental;

    @PostMapping("/select")
    public void select(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Map info = new ObjectMapper().readValue(request.getInputStream(), Map.class);
        String qq = info.get("QQ").toString();
        String value = (String)redisTemplate.opsForValue().get("SelectMental:QQ:" + qq);
        if (!ObjectUtils.isEmpty(value)){
            response.getWriter().println(value);
        }else{
            Random random = new Random();
            int rand = random.nextInt(mental.size());
            redisTemplate.opsForValue().set("SelectMental:QQ:" + qq,mental.get(rand),1L, TimeUnit.HOURS);
            response.getWriter().println(mental.get(rand));
        }
    }
}
