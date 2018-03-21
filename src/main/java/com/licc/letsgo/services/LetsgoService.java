package com.licc.letsgo.services;

import com.licc.letsgo.res.BaseRes;
import com.licc.letsgo.res.petdata.PetsOnSale;
import com.licc.letsgo.util.PetUtil;
import java.io.IOException;
import java.net.URI;

import java.util.Collections;
import java.util.Set;
import javax.annotation.Resource;

import org.assertj.core.util.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CachePut;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.licc.letsgo.Const;
import com.licc.letsgo.LiccProps;
import com.licc.letsgo.enums.PetsSortingEnum;
import com.licc.letsgo.model.Captcha;
import com.licc.letsgo.model.User;
import com.licc.letsgo.req.BuyReq;
import com.licc.letsgo.req.PetDataRequest;
import com.licc.letsgo.req.PetDetailReq;
import com.licc.letsgo.res.petdata.PetDataRes;
import com.licc.letsgo.res.petdetail.PetDetailRes;
import com.licc.letsgo.util.RedisUtils;
import com.licc.letsgo.util.ResponseVo;
import com.licc.letsgo.util.ResponseVoUtil;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

/**
 *
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
    LiccProps  liccProps;
    @Resource
    RedisUtils redisUtils;

    /**
     * <strong>查询莱茨狗数据</strong>
     * 
     * @param proxyIp 代理IP
     * @param sortingEnum 排序
     * @param pageNum
     * @param pageSize
     * @return
     */

    public ResponseVo<PetDataRes> queryPetData(String proxyIp, PetsSortingEnum sortingEnum, int pageNum, int pageSize) {
        try {
            PetDataRequest request = new PetDataRequest();
            request.setAppId(4);
            request.setPageNo(pageNum > 0 ? pageNum : 1);
            request.setPageSize(pageSize);
            request.setQuerySortType(sortingEnum.name());
            request.setRequestId(System.currentTimeMillis());
            request.setTpl("wallet");
            request.setPetIds(Lists.<String> newArrayList());
            RequestEntity<PetDataRequest> requestEntity = RequestEntity.post(new URI(liccProps.getPetUrl() + "data/market/queryPetsOnSale"))
                    .contentType(MediaType.APPLICATION_JSON).body(request);
            ResponseEntity<PetDataRes> res = getRestTemplate(proxyIp).exchange(requestEntity, PetDataRes.class);
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
            ResponseEntity<PetDetailRes> res = getRestTemplate(proxyIp).exchange(requestEntity, PetDetailRes.class);
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

    public void buy(PetsOnSale sale, User user) {
        try {
            String buyUrl = liccProps.getPetUrl()+"chain/detail?channel=market&petId="+sale.getPetId()+"&validCode="+sale.getValidCode();
            BuyReq req = new BuyReq();
            req.setAmount(sale.getAmount());
            req.setValidCode(sale.getValidCode());
            req.setPetId(sale.getPetId());
            Captcha captcha = getCaptcha(user);
            if(captcha ==null){
                String msg = user.getName()+"购买异常-验证码为空:" + PetUtil.transRareDegree(sale.getRareDegree())+" "+sale.getAmount()+" "+sale.getId()+"\n"+buyUrl;
                logger.error(msg);
                redisUtils.lpush(Const.REDIS_BUY_LETGO_FAIL,msg);
                return;
            }

            req.setSeed(captcha.getSeed());
            req.setCaptcha(captcha.getCode());
            RequestEntity<BuyReq> requestEntity = RequestEntity.post(new URI(liccProps.getPetUrl() + "data/txn/create"))
                    .header("Cookie", user.getCookie()).contentType(MediaType.APPLICATION_JSON).body(req);
            ResponseEntity<BaseRes> res = getRestTemplate(null).exchange(requestEntity, BaseRes.class);
            BaseRes baseRes = res.getBody();
            String errorNo = baseRes.getErrorNo();
            if ("00".equals(errorNo)) {
                String msg = user.getName()+"购买成功:"+PetUtil.transRareDegree(sale.getRareDegree())+" "+sale.getAmount()+" "+sale.getId()+"\n"+buyUrl;
                logger.info(msg);
                redisUtils.lpush(Const.REDIS_BUY_LETGO_SUCCESS,msg);

            }else {
                String msg = user.getName()+"购买异常:" + PetUtil.transRareDegree(sale.getRareDegree())+" "+sale.getAmount()+" "+sale.getId()+" "+baseRes.getErrorMsg()+"\n"+buyUrl;
                redisUtils.lpush(Const.REDIS_BUY_LETGO_FAIL,msg);

                logger.error(msg);
            }

        } catch (Exception e) {
            logger.error(user.getName()+"购买异常接口异常"+e.getMessage());
        }

    }

    public Captcha getCaptcha(User user) throws IOException {
        Set<String> codes = redisUtils.hkeys(user.getName() + Const.REDIS_CAPTCHAS);
        if(CollectionUtils.isEmpty(codes)){
            return null;
        }
        String seed = (String) codes.toArray()[0];
        String code = (String) redisUtils.hget(user.getName() + Const.REDIS_CAPTCHAS,seed);
        redisUtils.hdelete(user.getName() + Const.REDIS_CAPTCHAS,seed);
        return new Captcha(seed,code);

    }




}
