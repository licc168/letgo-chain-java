package com.licc.letsgo.task;

import com.licc.letsgo.util.Cache;
import com.licc.letsgo.util.CacheManager;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.licc.letsgo.Const;
import com.licc.letsgo.LiccProps;
import com.licc.letsgo.model.Captcha;
import com.licc.letsgo.model.User;
import com.licc.letsgo.services.LetsgoService;
import com.licc.letsgo.util.DateUtil;
import com.licc.letsgo.util.RedisUtils;

/**
 * 生成验证码
 *
 * @author lichangchao
 * @version 1.0.0
 * @date 2018/3/22 14:29
 * @see
 */

public class GenCodeTask {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    LetsgoService letsgoService;

    @Resource
    LiccProps liccProps;

    @Scheduled(fixedRate = 90000)

    public void genCode() {
        CacheManager.clearAll();
        List<User> users = liccProps.getUsers();
        String proxyIp = letsgoService.getProxyIp();
        try {

            for (User user : users) {
                Captcha captcha = letsgoService.getCaptcha(proxyIp, user);
                if (captcha != null) {
                    logger.info("用户【" + user.getName() + "】【" + DateUtil.getNowDate() + "】生成验证码成功");
                    CacheManager.putCache(user.getName() + "_captcha", new Cache(captcha.getSeed(),captcha.getCode()));
                } else {
                    CacheManager.clearAll();


                }
            }

        } catch (Exception e) {
            logger.error("生成验证码错误");
        }

    }
}
