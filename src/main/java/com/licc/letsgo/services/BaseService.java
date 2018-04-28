package com.licc.letsgo.services;

import com.licc.letsgo.interceptor.TokenInterceptor;
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



    RestTemplate getRestTemplate(String proxyIp,int readTime,int connectTime) {
        RestTemplate restTemplate;
        SimpleClientHttpRequestFactory httpRequestFactory = new SimpleClientHttpRequestFactory();

        if (!StringUtils.isEmpty(proxyIp)) {
            httpRequestFactory.setReadTimeout(readTime);
            httpRequestFactory.setConnectTimeout(connectTime);
            String[] ips = proxyIp.split(":");
            SocketAddress address = new InetSocketAddress(ips[0], Integer.parseInt(ips[1]));
            Proxy proxy = new Proxy(Proxy.Type.HTTP, address);
            httpRequestFactory.setProxy(proxy);
            restTemplate = new RestTemplate(httpRequestFactory);
        } else {
            httpRequestFactory.setReadTimeout(10000);
            httpRequestFactory.setConnectTimeout(10000);
            restTemplate = new RestTemplate(httpRequestFactory);

        }
        restTemplate.getInterceptors().add(new TokenInterceptor());
        return restTemplate;
    }


}
