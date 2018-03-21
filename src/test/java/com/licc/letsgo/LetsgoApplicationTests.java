package com.licc.letsgo;

import com.licc.letsgo.enums.PetsSortingEnum;
import com.licc.letsgo.res.petdata.PetDataRes;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Proxy.Type;
import java.net.URI;

import javax.annotation.Resource;

import org.assertj.core.util.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import com.licc.letsgo.req.PetDataRequest;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LetsgoApplicationTests {
    @Resource
    RedisTemplate redisTemplate;
    @Resource
    LiccProps liccProps;
    @Test
    public void contextLoads() {
        RestTemplate restTemplate = new RestTemplate();
        SimpleClientHttpRequestFactory reqfac = new SimpleClientHttpRequestFactory();
        String proxyIp = (String) redisTemplate.opsForList().leftPop("proxy_ip");
        String[] ips = proxyIp.split(":");
        reqfac.setProxy(new Proxy(Type.HTTP, new InetSocketAddress(ips[0], Integer.parseInt(ips[1]))));
        restTemplate.setRequestFactory(reqfac);
        try {
            PetDataRequest request = new PetDataRequest();
            request.setAppId(4);
            request.setPageNo(1);
            request.setPageSize(10);
            request.setQuerySortType(PetsSortingEnum.CREATETIME_DESC.name());
            request.setRequestId(System.currentTimeMillis());
            request.setTpl("wallet");
            request.setPetIds(Lists.<String>newArrayList());
            RequestEntity<PetDataRequest> requestEntity = RequestEntity
                    .post(new URI(liccProps.getPetUrl()+"data/market/queryPetsOnSale")).contentType(MediaType.APPLICATION_JSON)
                    .body(request);
            ResponseEntity<PetDataRes> res = restTemplate.exchange(requestEntity, PetDataRes.class);
            System.out.println(res);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
