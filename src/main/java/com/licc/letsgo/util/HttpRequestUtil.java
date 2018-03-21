package com.licc.letsgo.util;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Proxy.Type;
import java.util.Date;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSONObject;

/**
 * Http请求工具类
 * @author snowfigure
 * @since 2014-8-24 13:30:56
 * @version v1.0.1
 */
public class HttpRequestUtil {

     
    public static void main(String[] args) {
//        //demo:代理访问
       while(true) {

           RestTemplate restTemplate = new RestTemplate();
//           String ip = restTemplate.getForObject("http://47.94.196.111:5010/get/", String.class);
//           String[] ips = ip.split(":");
//           SimpleClientHttpRequestFactory reqfac = new SimpleClientHttpRequestFactory();
//           reqfac.setProxy(
//               new Proxy(Type.HTTP, new InetSocketAddress(ips[0], Integer.parseInt(ips[1]))));
//           reqfac.setConnectTimeout(60);
//           restTemplate.setRequestFactory(reqfac);

           JSONObject request = new JSONObject();
           request.put("appId", 1);
           request.put("lastAmount", "");
           request.put("lastRareDegree", "");
           request.put("petIds",new String[]{});
           request.put("pageNo", 1);
           request.put("pageSize", 10);
           request.put("querySortType", "CREATETIME_DESC");
           request.put("tpl", "");
           request.put("requestId", new Date().getTime());

// set headers
           HttpHeaders headers = new HttpHeaders();
           headers.setContentType(MediaType.APPLICATION_JSON);
           HttpEntity<String> entity = new HttpEntity<String>(request.toString(), headers);
           try {
               ResponseEntity<String> loginResponse = restTemplate
                   .exchange("https://pet-chain.baidu.com/data/market/queryPetsOnSale",
                       HttpMethod.POST,
                       entity, String.class);
               System.out.println(loginResponse);
           }catch (Exception e){
               System.out.println("超时");
           }
           System.out.println(111);
       }
    }
     
}