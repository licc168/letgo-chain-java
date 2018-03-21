package com.licc.letsgo.controller;

import com.licc.letsgo.Const;
import com.licc.letsgo.LiccProps;
import com.licc.letsgo.model.User;
import com.licc.letsgo.util.RedisUtils;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.Resource;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

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
    @RequestMapping(value = "",method = RequestMethod.GET)
    public String index(Map<String, Object> model) {
        model.put("users",liccProps.getUsers());
        return "index1";
    }

    @RequestMapping(value = "/start",method = RequestMethod.POST)
    @ResponseBody
    public ResponseVo start( User user) {
        task.run(user);
        return ResponseVoUtil.successMsg("执行成功");
    }

    @RequestMapping(value = "/stop",method = RequestMethod.POST)
    @ResponseBody
    public ResponseVo stop(String name) {
        task.stop(name);
        return ResponseVoUtil.successMsg("关闭成功");
    }


    @RequestMapping(value = "/buysuccess",method = RequestMethod.GET)

    public String buysuccess(Map<String, Object> model) {
        List<Object> list = redisUtils.lgetAll(Const.REDIS_BUY_LETGO_SUCCESS);
        model.put("list",list);
        return "buymsg";
    }

    @RequestMapping(value = "/buyfail",method = RequestMethod.GET)
    public String  buyfail(Map<String, Object> model) {
        List<Object> list = redisUtils.lgetAll(Const.REDIS_BUY_LETGO_FAIL);
        model.put("list",list);
        return "buymsg";
    }
}
