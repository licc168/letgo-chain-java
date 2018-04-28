package com.licc.letsgo.interceptor;

import java.io.IOException;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

public class TokenInterceptor implements ClientHttpRequestInterceptor

{
   static String  UserAgent[] = {
        "Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:59.0) Gecko/20100101 Firefox/59.0",
        "Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/64.0.3282.186 Mobile Safari/537.36",
    };
    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException
    {
        String checkTokenUrl = request.getURI().getPath();
        String methodName = request.getMethod().name();
        String requestBody = new String(body);
        int ramdon = (int)(Math.random()*2);
        request.getHeaders().add("User-Agent",UserAgent[ramdon]);

        return execution.execute(request, body);
    }




}

