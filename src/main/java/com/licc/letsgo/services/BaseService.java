package com.licc.letsgo.services;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.SocketAddress;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import com.licc.letsgo.Const;

/**
 * Function:(这里用一句话描述这个类的作用)
 *
 * @author Administrator
 * @version 1.0.0
 * @date 2018/3/19 17:57
 * @see
 */
public class BaseService {
    @Resource
    RedisTemplate redisTemplate;
    final Logger logger = LoggerFactory.getLogger(LetsgoService.class);



    RestTemplate getRestTemplate(String proxyIp) {
        RestTemplate restTemplate;
        if (!StringUtils.isEmpty(proxyIp)) {
            SimpleClientHttpRequestFactory httpRequestFactory = new SimpleClientHttpRequestFactory();
            httpRequestFactory.setReadTimeout(200);
            httpRequestFactory.setConnectTimeout(200);
            String[] ips = proxyIp.split(":");
            SocketAddress address = new InetSocketAddress(ips[0], Integer.parseInt(ips[1]));
            Proxy proxy = new Proxy(Proxy.Type.HTTP, address);
            httpRequestFactory.setProxy(proxy);
            restTemplate = new RestTemplate(httpRequestFactory);
        } else {
            restTemplate = new RestTemplate();

        }
        return restTemplate;
    }

    public  String getProxyIp(String key) {
        try {
            switch (key) {
                case Const.REDIS_PROXY_IP:
                    return (String) redisTemplate.opsForValue().get(key);

                case Const.REDIS_PROXY_LIST:
                    return (String) redisTemplate.opsForList().leftPop(key);

                default:
                    return null;

            }
        }catch (Exception e){
            logger.error("获取代理数据异常");
            return null;
        }
    }

}
