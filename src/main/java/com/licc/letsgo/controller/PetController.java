package com.licc.letsgo.controller;

import com.licc.letsgo.Const;
import com.licc.letsgo.LiccProps;
import com.licc.letsgo.model.User;
import com.licc.letsgo.services.LetsgoService;
import com.licc.letsgo.util.RedisUtils;

import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.Resource;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.licc.letsgo.Task;
import com.licc.letsgo.util.ResponseVo;
import com.licc.letsgo.util.ResponseVoUtil;

@Controller

public class PetController {
    @Resource
    Task task;
    @Resource
    LiccProps liccProps;
    @Resource
    RedisUtils redisUtils;
    @Resource
    LetsgoService letsgoService;


    @RequestMapping(value = "", method = RequestMethod.GET)
    public String index(Map<String, Object> model) {
        model.put("users", liccProps.getUsers());
        return "index1";
    }

    @RequestMapping(value = "/data/{type}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseVo start(@PathVariable Integer type) {
        task.data(type);
        return ResponseVoUtil.successMsg("执行成功");
    }

    @RequestMapping(value = "/buyFrist", method = RequestMethod.POST)
    @ResponseBody
    public ResponseVo stop(User user) {
        task.buyFrist(user);
        return ResponseVoUtil.successMsg("执行成功");
    }
    @RequestMapping(value = "/buyLast", method = RequestMethod.POST)
    @ResponseBody
    public ResponseVo buyLast(User user) {
        task.buyLast(user);
        return ResponseVoUtil.successMsg("执行成功");
    }

    @RequestMapping(value = "/buysuccess", method = RequestMethod.GET)

    public String buysuccess(Map<String, Object> model) {
        List<Object> list = redisUtils.lgetAll(Const.REDIS_BUY_LETGO_SUCCESS);
        model.put("list", list);
        return "buymsg";
    }

    @RequestMapping(value = "/buyfail", method = RequestMethod.GET)
    public String buyfail(Map<String, Object> model) {
        List<Object> list = redisUtils.lgetAll(Const.REDIS_BUY_LETGO_FAIL);
        model.put("list", list);
        return "buymsg";
    }
}
