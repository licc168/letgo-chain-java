package com.licc.letsgo.interceptor;

import java.io.IOException;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

public class TokenInterceptor implements ClientHttpRequestInterceptor
{
    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException
    {
        String checkTokenUrl = request.getURI().getPath();
        String methodName = request.getMethod().name();
        String requestBody = new String(body);
        //request.getHeaders().add("X-Auth-Token","");

        return execution.execute(request, body);
    }
}

