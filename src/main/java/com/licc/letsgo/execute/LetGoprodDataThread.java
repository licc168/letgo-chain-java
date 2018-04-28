package com.licc.letsgo.execute;

import com.licc.letsgo.Const;
import com.licc.letsgo.req.PetDataRequest;
import com.licc.letsgo.res.petdata.PetDataRes;
import com.licc.letsgo.res.petdata.PetsOnSale;
import com.licc.letsgo.services.LetsgoService;
import com.licc.letsgo.util.DateUtil;
import com.licc.letsgo.util.RedisUtils;
import com.licc.letsgo.util.ResponseVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class LetGoprodDataThread implements Runnable {
    final Logger   logger = LoggerFactory.getLogger(this.getClass());

    LetsgoService  letsgoService;
    RedisUtils     redisUtils;
    PetDataRequest petDataRequest = new PetDataRequest();

    public LetGoprodDataThread(LetsgoService letsgoService, RedisUtils redisUtils, PetDataRequest petDataRequest) {
        this.letsgoService = letsgoService;
        this.redisUtils = redisUtils;

        this.petDataRequest = petDataRequest;
    }

    @Override
    public void run() {
        int count=0;
        try {
            while (true) {

                if (Const.saleQueue.size() > 10000) {
                    Const.saleQueue.clear();
                }

                String proxyIp = letsgoService.getProxyIp();
                if(proxyIp==null){
                    Thread.sleep(550);
                }
                ResponseVo<PetDataRes> petDataRes = letsgoService.queryPetData(proxyIp, this.petDataRequest);
                if (!petDataRes.isSuccess()) {
                    logger.error("代理IP:" + proxyIp + ":" + petDataRes.getDesc());
                    Const.proxyCache.clear();
                    if(!Const.proxyQueue.isEmpty()){
                        String poxyIp =  Const.proxyQueue.element();
                        Const.proxyQueue.remove(poxyIp);
                        Const.proxyCache.put("proxyIp",poxyIp);
                    }else{
                        if(!Const.proxyQueueBak.isEmpty()){
                            String poxyIp =  Const.proxyQueueBak.element();
                            Const.proxyQueueBak.remove(poxyIp);
                            Const.proxyCache.put("proxyIp",poxyIp);
                        }else{
                            if(!Const.proxyQueueBakBak.isEmpty()) {
                                String poxyIp = Const.proxyQueueBakBak.element();
                                Const.proxyQueueBakBak.remove(poxyIp);
                                Const.proxyCache.put("proxyIp", poxyIp);
                            }
                        }

                    }

                    continue;
                }
                PetDataRes dataRes = petDataRes.getData();
                if (dataRes.getData().getHasData()) {
                    List<PetsOnSale> list = dataRes.getData().getPetsOnSale();
                    for (PetsOnSale petsOnSale : list) {
//                        // 只抓取0代狗
//                        if (petsOnSale.getGeneration() > 0)
//                            continue;
//                        // 只抓取未繁殖过的
//                        if (petsOnSale.getIsCooling() || !"0分钟".equals(petsOnSale.getCoolingInterval()))
//                            continue;
                        String cacheKey = petsOnSale.getPetId();

                        if (Const.cache.get(cacheKey)!=null)
                            continue;
                        Const.cache.put(cacheKey,true);

                        Const.saleQueue.put(petsOnSale);
                        logger.debug(petsOnSale.toString());
                        count++;
                    }
                   // logger.debug(user.getName()+"第"+count+"次查询  "+"队列大小:"+Const.saleQueue.size()+" "+DateUtil.getNowDate());
                    count++;
                }
                if (Const.cache.size() > 10000) {
                    logger.info("清理狗数据：" + DateUtil.getNowDate());
                    Const.cache.clear();
                }
            }
        } catch (Exception e) {
            logger.error("生产狗数据异常" + e.getMessage());
        }
    }


//    @Override
//    public void run() {
//        HashMap<String, Integer> cache = new HashMap<>(1000);
//
//        try {
//            while (true) {
//                if (Const.saleQueue.size() == 20) {
//                    logger.debug("清理狗数据：" + DateUtil.getNowDate());
//                    Const.saleQueue.clear();
//                }
//
//                    List<Object> list = redisUtils.lgetAll("letgo-data");
//                    for (Object obj : list) {
//                        PetsOnSale petsOnSale = JacksonUtil.readValue((String) obj,PetsOnSale.class);
//                        // 只抓取0代狗
//                        if (petsOnSale.getGeneration() > 0)
//                            continue;
//                        // 只抓取未繁殖过的
//                        if (petsOnSale.getIsCooling() || !"0分钟".equals(petsOnSale.getCoolingInterval()))
//                            continue;
//                        String cacheKey = petsOnSale.getId() + user.getName();
//                        if (cache.get(cacheKey) != null)
//                            continue;
//                        cache.put(cacheKey, 1);
//                        Const.saleQueue.put(petsOnSale);
//                        logger.debug("获取到狗数据【" + petsOnSale.getId() + "】：" + DateUtil.getNowDate());
//
//
//                }
//            }
//        } catch (Exception e) {
//            logger.error("生产狗数据异常" + e.getMessage());
//        }
//    }
}
