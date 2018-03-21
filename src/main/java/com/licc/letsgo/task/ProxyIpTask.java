package com.licc.letsgo.task;

import com.licc.letsgo.LiccProps;
import com.licc.letsgo.model.User;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.licc.letsgo.Const;
import com.licc.letsgo.enums.PetsSortingEnum;
import com.licc.letsgo.res.petdata.PetDataRes;
import com.licc.letsgo.services.LetsgoService;
import com.licc.letsgo.util.DateUtil;
import com.licc.letsgo.util.RedisUtils;
import com.licc.letsgo.util.ResponseVo;

/**
 * @author lichangchao
 */
@Component
public class ProxyIpTask {
    final Logger  logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    LetsgoService letsgoService;
    @Resource
    RedisUtils    redisUtils;

    /**
     * 设置有效代理放在代理池中
     */
    @Scheduled(fixedRate = 50)
    public void proxyList() {
        try {
            Set<String> proxys = redisUtils.hkeys(Const.REDIS_USEFUL_PROXY);
            for (String proxyIp : proxys) {
                ResponseVo<PetDataRes> res = letsgoService.queryPetData(proxyIp, PetsSortingEnum.CREATETIME_DESC, 1, 10);
                if (res.isSuccess()) {
                    logger.debug("有效代理IP：" + proxyIp + "  " + DateUtil.getNowDate());
                    redisUtils.lpush(Const.REDIS_PROXY_LIST, proxyIp);
                }

            }
        } catch (Exception e) {

            logger.error("有效代理放在代理池-异常");
        }
    }

    /**
     * 获取有效代理放在缓存中保持30秒钟
     */
    @Scheduled(fixedRate = 50)
    public void proxyIp() {
        try {
            String poxyIp = (String) redisUtils.get(Const.REDIS_PROXY_IP);
            if (poxyIp == null) {
                poxyIp = (String) redisUtils.lpop(Const.REDIS_PROXY_LIST);
                if (poxyIp != null) {
                    logger.debug(Const.REDIS_PROXY_IP + ":" + poxyIp + "  " + DateUtil.getNowDate());
                    redisUtils.set(Const.REDIS_PROXY_IP, poxyIp);
                    redisUtils.expire(Const.REDIS_PROXY_IP, 3, TimeUnit.SECONDS);
                }
            }
        } catch (Exception e) {
            logger.error("获取有效代理放在缓存中-异常");
        }
    }


}
