package com.licc.letsgo.services;

import com.licc.letsgo.req.ConfirmBuyReq;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.Resource;

import com.licc.letsgo.execute.LetgoBuyPetSaleThread;
import com.licc.letsgo.util.*;
import org.assertj.core.util.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.licc.letsgo.Const;
import com.licc.letsgo.LiccProps;
import com.licc.letsgo.enums.PetsSortingEnum;
import com.licc.letsgo.model.Captcha;
import com.licc.letsgo.model.User;
import com.licc.letsgo.req.BuyReq;
import com.licc.letsgo.req.CaptchaReq;
import com.licc.letsgo.req.PetDataRequest;
import com.licc.letsgo.req.PetDetailReq;
import com.licc.letsgo.res.BaseRes;
import com.licc.letsgo.res.captcha.CaptchaData;
import com.licc.letsgo.res.captcha.CaptchaRes;
import com.licc.letsgo.res.petdata.PetDataRes;
import com.licc.letsgo.res.petdata.PetsOnSale;
import com.licc.letsgo.res.petdetail.PetDetailRes;

/**
 * @author lichangchao
 * @version 1.0.0
 * @date 2018/3/2 15:17
 * @see
 */
@Service
@SuppressWarnings("unchecked")
public class LetsgoService extends BaseService {
    final Logger logger = LoggerFactory.getLogger(LetsgoService.class);
    @Resource
    LiccProps    liccProps;
    @Resource
    RedisUtils redisUtils;

    /**
     * <strong>查询莱茨狗数据</strong>
     *
     * @param proxyIp 代理IP
     * @return
     */

    public ResponseVo<PetDataRes> queryPetData(String proxyIp, PetDataRequest request) {
        try {
            request.setAppId(4);
            request.setRequestId(System.currentTimeMillis());
            request.setTpl("wallet");
            request.setPetIds(Lists.<String> newArrayList());
            RequestEntity<PetDataRequest> requestEntity = RequestEntity.post(new URI(liccProps.getPetUrl() + "data/market/queryPetsOnSale"))
                    .contentType(MediaType.APPLICATION_JSON).body(request);
            ResponseEntity<PetDataRes> res = getRestTemplate(proxyIp, 1500, 1500).exchange(requestEntity, PetDataRes.class);
            PetDataRes petDataRes = res.getBody();
            String errorNo = petDataRes.getErrorNo();
            if ("00".equals(errorNo)) {
                return ResponseVoUtil.successResult(proxyIp, petDataRes);
            }
            return ResponseVoUtil.failResult("查询接口返回异常：" + petDataRes.getErrorMsg());
        } catch (Exception e) {
            return ResponseVoUtil.failResult("查询接口异常,检查代理设置");
        }

    }

    /**
     * <strong>查询莱茨狗详情</strong>
     *
     * @param proxyIp 代理IP
     * @param petId 排序
     * @return
     */

    public ResponseVo<PetDetailRes> queryPetDeatil(String proxyIp, String petId) {
        try {
            PetDetailReq request = new PetDetailReq();
            request.setAppId(1);
            request.setRequestId(System.currentTimeMillis());
            request.setPetId(petId);

            RequestEntity<PetDetailReq> requestEntity = RequestEntity.post(new URI(liccProps.getPetUrl() + "data/pet/queryPetById"))
                    .contentType(MediaType.APPLICATION_JSON).body(request);
            ResponseEntity<PetDetailRes> res = getRestTemplate(proxyIp, 500, 500).exchange(requestEntity, PetDetailRes.class);
            PetDetailRes petDetailRes = res.getBody();
            String errorNo = petDetailRes.getErrorNo();
            if ("00".equals(errorNo)) {
                return ResponseVoUtil.successData(petDetailRes);
            }
            return ResponseVoUtil.failResult("查询莱茨狗详情接口返回异常：");
        } catch (Exception e) {

            return ResponseVoUtil.failResult("查询莱茨狗详情异常,检查代理设置");
        }

    }

    public void buyAll(String ruleName, PetsOnSale sale, User user1) {
        ExecutorService serviceBuy = Executors.newFixedThreadPool(10);
        //
        // serviceBuy.execute(new LetgoBuyPetSaleThread(ruleName, sale, this,
        // user));
        // serviceBuy.execute(new LetgoBuyPetSaleThread(ruleName, sale, this,
        // user));
        //
        // serviceBuy.shutdown();
        List<User> userList = liccProps.getUsers();
        for (User user : userList) {
            serviceBuy.execute(new LetgoBuyPetSaleThread(ruleName, sale, this, user));
        }

    }

    /**
     * 下单接口
     *
     * @param ruleName
     * @param sale
     * @param user
     */
    @Async
    public void buy(String ruleName, PetsOnSale sale, User user) {
        try {
            String buyUrl = liccProps.getPetUrl() + "chain/detail?channel=market&petId=" + sale.getPetId() + "&validCode="
                    + sale.getValidCode();
            BuyReq req = new BuyReq();
            req.setAmount(sale.getAmount());
            req.setValidCode(sale.getValidCode());
            req.setPetId(sale.getPetId());
            String proxyIp = this.getProxyIp();

            // 从redis取

            Captcha captcha = getCaptchaRedis(user);
            if (captcha == null) {

                captcha = getCaptcha(proxyIp, user);
                if (captcha == null) {
                    String msg = ruleName + ">>>>>" + user.getName() + "  时间：" + DateUtil.getNowDate() + "购买异常-验证码为空:"
                            + PetUtil.transRareDegree(sale.getRareDegree()) + " " + sale.getAmount() + " " + sale.getId() + "\n" + buyUrl;
                    logger.error(msg);
                    redisUtils.lpush(Const.REDIS_BUY_LETGO_FAIL, msg);
                    return;
                }
            }

            req.setSeed(captcha.getSeed());
            req.setCaptcha(captcha.getCode());
            RequestEntity<BuyReq> requestEntity = RequestEntity.post(new URI(liccProps.getPetUrl() + "data/txn/sale/create"))
                    .header("Cookie", user.getCookie()).contentType(MediaType.APPLICATION_JSON).body(req);
            ResponseEntity<BaseRes> res = getRestTemplate(proxyIp, 10000, 10000).exchange(requestEntity, BaseRes.class);

            BaseRes baseRes = res.getBody();
            String errorNo = baseRes.getErrorNo();
            if ("00".equals(errorNo)) {
//                // 确认交易密码
//                ConfirmBuyReq confirmBuyReq = new ConfirmBuyReq();
//                confirmBuyReq.setS(user.getS());
//                confirmBuyReq.setAppId(1);
//                confirmBuyReq.setConfirmType(2);
//                confirmBuyReq.setRequestId(System.currentTimeMillis());
//                RequestEntity<ConfirmBuyReq>  buyReqRequestEntity = RequestEntity.post(new URI(liccProps.getPetUrl() + "data/order/confirm")).header("Cookie", user.getCookie())
//                        .contentType(MediaType.APPLICATION_JSON).body(confirmBuyReq);
//                res = getRestTemplate(proxyIp, 10000, 10000).exchange(buyReqRequestEntity, BaseRes.class);
//                baseRes = res.getBody();
//                errorNo = baseRes.getErrorNo();
//                if ("00".equals(errorNo)) {
//                    String msg = ruleName + ">>>>>" + user.getName() + "  时间：" + DateUtil.getNowDate() + "购买成功:"
//                            + PetUtil.transRareDegree(sale.getRareDegree()) + " " + sale.getAmount() + " " + sale.getId() + "\n" + buyUrl;
//                    logger.info(msg);
//                    redisUtils.lpush(Const.REDIS_BUY_LETGO_SUCCESS, msg);
//
//                }else{
                String msg = ruleName + ">>>>>" + user.getName() + "  时间：" + DateUtil.getNowDate() + "购买成功:"
                    + PetUtil.transRareDegree(sale.getRareDegree()) + " " + sale.getAmount() + " " + sale.getId() + "\n" + buyUrl;
                logger.info(msg);
                redisUtils.lpush(Const.REDIS_BUY_LETGO_SUCCESS, msg);


            }else{
                    String msg = ruleName + ">>>>>" + user.getName() + " 时间：" + DateUtil.getNowDate() + "购买异常:"
                        + PetUtil.transRareDegree(sale.getRareDegree()) + " " + sale.getAmount() + " " + sale.getId() + " " + errorNo
                        + " " + baseRes.getErrorMsg() + "\n" + buyUrl;
                    logger.error(msg);
                    // 验证码错误 重新购买
                    if ("100".equals(errorNo)) {
                        buy(ruleName, sale, user);
                    }
                    if ("101".equals(errorNo)) {
                        CacheManager.clearOnly(user.getName()+"_captcha");
                        buy(ruleName, sale, user);

                    }
                    redisUtils.lpush(Const.REDIS_BUY_LETGO_FAIL, msg);



            }

        } catch (Exception e) {
            logger.error(user.getName() + "购买异常接口异常" + e.getMessage());
        }

    }

    /**
     * 验证码
     *
     * @param user
     */
    public ResponseVo<CaptchaData> getCaptchaApi(String proxyIp, User user) {
        try {
            CaptchaReq req = new CaptchaReq();
            req.setAppId(4);
            req.setRequestId(System.currentTimeMillis());
            RequestEntity<CaptchaReq> requestEntity = RequestEntity.post(new URI(liccProps.getPetUrl() + "data/captcha/gen"))
                    .header("Cookie", user.getCookie()).contentType(MediaType.APPLICATION_JSON).body(req);
            ResponseEntity<CaptchaRes> res = getRestTemplate(proxyIp, 10000, 10000).exchange(requestEntity, CaptchaRes.class);
            CaptchaRes baseRes = res.getBody();
            String errorNo = baseRes.getErrorNo();
            if ("00".equals(errorNo)) {
                return ResponseVoUtil.successData(baseRes.getData());
            }
            return ResponseVoUtil.failResult(user.getName() + "验证码接口返回异常：" + errorNo);

        } catch (Exception e) {
            logger.error(user.getName() + "验证码接口异常" + e.getMessage());
            return ResponseVoUtil.failResult(user.getName() + "验证码接口异常" + e.getMessage());
        }

    }

    /**
     * 通过接口生成验证码接口
     */
    public Captcha getCaptcha(String proxyIp, User user) {
        try {
            ResponseVo<CaptchaData> captchaDataResponseVo = getCaptchaApi(proxyIp, user);
            if (captchaDataResponseVo.isSuccess()) {
                String img = captchaDataResponseVo.getData().getImg();
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("imgbase64", img);
                RequestEntity<JSONObject> requestEntity = RequestEntity.post(new URI(liccProps.getCodeUrl()))
                        .contentType(MediaType.APPLICATION_JSON).body(jsonObject);
                ResponseEntity<String> res = getRestTemplate(null, 10000, 10000).exchange(requestEntity, String.class);
                String captcha = res.getBody();
                if (!StringUtils.isEmpty(captcha)) {
                    return new Captcha(captchaDataResponseVo.getData().getSeed(), captcha);
                }
            } else {
                logger.error(captchaDataResponseVo.getDesc());
            }

        } catch (URISyntaxException e) {
            logger.error("验证码接口-getCaptcha异常" + e.getMessage());
        }
        return null;
    }

    /**
     * 通过redis 获取验证码接口 预先生成好
     *
     * @param user
     * @return
     * @throws IOException
     */
    public Captcha getCaptchaRedis(User user) throws IOException {
        Cache cache = CacheManager.getCacheInfo(user.getName() + "_captcha");
        if (cache != null) {
            String seed = cache.getKey();
            String code = (String) cache.getValue();
            return new Captcha(seed, code);
        } else {
            return null;
        }

    }

    @Cacheable(value = "letgo-Enable", key = "#name")
    public String getLetgoEnable(String name) {
        Object ob = redisUtils.get(Const.REDIS_LETGO_ENABLE + name);
        return ob == null ? "false" : (String) ob;
    }

    @CachePut(value = "letgo-Enable", key = "#name")
    public String saveLetgoEnable(String name, String value) {
        redisUtils.set(Const.REDIS_LETGO_ENABLE + name, value);
        return value;
    }

    @CacheEvict(value = "letgo-Enable", key = "#name")
    public void deleteLetgoEnable(String name) {
    }

    public String getProxyIp() {
        try {
            String proxyIp = Const.proxyCache.get("proxyIp");
            return proxyIp;
        } catch (Exception e) {
            logger.error("获取代理IP异常");
            return null;
        }
    }

    public void stop(String name) {
        redisUtils.set(Const.REDIS_LETGO_ENABLE + name, "false");
        this.deleteLetgoEnable(name);
    }




}
