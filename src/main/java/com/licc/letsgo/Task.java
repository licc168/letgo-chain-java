package com.licc.letsgo;

import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.licc.letsgo.enums.PetsSortingEnum;
import com.licc.letsgo.model.Rule;
import com.licc.letsgo.model.User;
import com.licc.letsgo.res.petdata.PetDataRes;
import com.licc.letsgo.res.petdata.PetsOnSale;
import com.licc.letsgo.res.petdetail.PetDetailRes;
import com.licc.letsgo.rule.BuyRule;
import com.licc.letsgo.services.LetsgoService;
import com.licc.letsgo.util.RedisUtils;
import com.licc.letsgo.util.ResponseVo;

/**
 *
 * @author lichangchao
 * @version 1.0.0
 * @date 2018/3/19 18:27
 * @see
 */
@Service
@Async
public class Task {
    final Logger  logger = LoggerFactory.getLogger(this.getClass());
    @Resource
    RedisUtils    redisUtils;
    @Resource
    LetsgoService letsgoService;

    public void run(User user) {
        HashMap<String, Integer> cache = new HashMap<>(1000);
        String enable = "true";
        redisUtils.set(user.getName() + Const.REDIS_LETGO_ENABLE, "true");
        int count = 0;
        while ("true".equals(enable)) {
            count++;
            if (count % 10 == 0) {
                cache.clear();
            }
            String proxyIp = letsgoService.getProxyIp(Const.REDIS_PROXY_IP);
            ResponseVo<PetDataRes> petDataRes = letsgoService.queryPetData(proxyIp, PetsSortingEnum.CREATETIME_DESC, 0, 10);
            if (!petDataRes.isSuccess()) {
                logger.error("代理IP:" + proxyIp + ":" + petDataRes.getDesc());
                redisUtils.delete(Const.REDIS_PROXY_IP);
                continue;
            }
            PetDataRes dataRes = petDataRes.getData();
            if (dataRes.getData().getHasData()) {
                List<PetsOnSale> list = dataRes.getData().getPetsOnSale();

                Rule rule = new Rule();
                for (PetsOnSale petsOnSale : list) {
                    String cacheKey = petsOnSale.getId() + user.getName();
                    if (cache.get(cacheKey) != null)
                        continue;
                    cache.put(cacheKey, 1);
                    rule.setPetsOnSale(petsOnSale);
                    String petId = petsOnSale.getPetId();
                    BuyRule.execute(rule, user);
                    ResponseVo<PetDetailRes> detail = letsgoService.queryPetDeatil(proxyIp, petId);
                    if (detail.isSuccess()) {
                        rule.setPetDetailData(detail.getData().getData());
                        BuyRule.executeDetail(rule, user);
                    }
                }
            }
            Object en = redisUtils.get(user.getName() + Const.REDIS_LETGO_ENABLE);
            enable = en == null ? "false" : (String) en;
            if ("false".equals(enable)) {
                logger.info("账号：【" + user.getName() + "】关闭抓狗，别轻易错过哟");

            }
        }
    }

    public void stop(String name) {
        redisUtils.set(name + Const.REDIS_LETGO_ENABLE, "false");

    }

}
