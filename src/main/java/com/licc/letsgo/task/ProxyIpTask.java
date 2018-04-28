package com.licc.letsgo.task;

import com.licc.letsgo.req.PetDataRequest;
import java.util.HashMap;
import java.util.Set;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

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
    HashMap<String,Boolean> cache = new HashMap<>(100);
    /**
     * 设置有效代理放在代理池中
     */
    @Scheduled(fixedRate = 2000)
    @Async
    public void proxyList() {
        if (Const.proxyFlag) {
            Const.proxyFlag = false;
            try {
//                if (Const.proxyQueue.isEmpty()||Const.proxyQueue.size()<50) {
                    if (cache.size() > 2000) {
                        cache.clear();
                    }
                    Set<String> proxys = redisUtils.hkeys(Const.REDIS_USEFUL_PROXY);
                    for (String proxyIp : proxys) {
                        if (cache.get(proxyIp) != null) continue;
                        //Thread.sleep(500);
                        PetDataRequest request = new PetDataRequest();
                        request.setPageNo(1);
                        request.setPageSize(10);
                        request.setQuerySortType(PetsSortingEnum.CREATETIME_DESC.name());
                        ResponseVo<PetDataRes> res = letsgoService.queryPetData(proxyIp, request);
                        cache.put(proxyIp, true);
                        if (res.isSuccess()) {
                            logger.debug("有效代理IP：" + proxyIp + "  " + DateUtil.getNowDate());
//                        redisUtils.sadd(Const.REDIS_PROXY_LIST, proxyIp);
//                        Object ip =  redisUtils.get(Const.REDIS_PROXY_IP);
//                        if (ip == null) {
//                            redisUtils.set(Const.REDIS_PROXY_IP, proxyIp);
//                        }
                            //将有效代理加入队列
                            if (!Const.proxyQueue.isEmpty() && Const.proxyQueue.contains(proxyIp)) continue;
                            Const.proxyQueue.add(proxyIp);
                            Const.proxyQueueBak.add(proxyIp);
                            Const.proxyQueueBakBak.add(proxyIp);
                            String useIp = Const.proxyCache.get("proxyIp");
                            if (StringUtils.isEmpty(useIp)) {
                                Const.proxyCache.put("proxyIp", proxyIp);
                            }

                        }
                    }
//                }

            } catch (Exception e) {

                logger.error("有效代理放在代理池-异常" + e.getMessage());
            }
            Const.proxyFlag = true;
        }
    }

    // /**
    // * 获取有效代理放在缓存中保持30秒钟
    // */
    // @Scheduled(fixedRate = 2000)
    // @Async
    // public void proxyIp() {
    // try {
    // String poxyIp = (String) redisUtils.get(Const.REDIS_PROXY_IP);
    // if (poxyIp == null) {
    // poxyIp = (String) redisUtils.lpop(Const.REDIS_PROXY_LIST);
    // if (poxyIp != null) {
    // logger.debug(Const.REDIS_PROXY_IP + ":" + poxyIp + " " +
    // DateUtil.getNowDate());
    // redisUtils.set(Const.REDIS_PROXY_IP, poxyIp);
    // redisUtils.expire(Const.REDIS_PROXY_IP, 3, TimeUnit.SECONDS);
    // }
    // }
    // } catch (Exception e) {
    // logger.error("获取有效代理放在缓存中-异常");
    // }
    // }

}
