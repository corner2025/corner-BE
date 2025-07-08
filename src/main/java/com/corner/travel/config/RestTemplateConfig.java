package com.corner.travel.touristSpot.api.config;

import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactory;
import org.apache.hc.client5.http.ssl.TrustSelfSignedStrategy;
import org.apache.hc.core5.ssl.SSLContextBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate() {
        try {
            // ✅ SSL 설정
            SSLContext sslContext = SSLContextBuilder
                    .create()
                    .loadTrustMaterial(new TrustSelfSignedStrategy())
                    .build();

            SSLConnectionSocketFactory sslConnectionSocketFactory =
                    new SSLConnectionSocketFactory(sslContext);

            // ✅ HttpClient
            CloseableHttpClient httpClient = HttpClients.custom()
                    .setUserAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36")
                    .build();

            // ✅ Factory
            HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
            factory.setHttpClient(httpClient);
            factory.setConnectTimeout(15000);
            factory.setConnectionRequestTimeout(15000);

            // ✅ RestTemplate 생성
            RestTemplate restTemplate = new RestTemplate(factory);

            // ✅ 메시지 컨버터 추가 (기존 유지 + text/html 처리 가능하게 추가)
            List<HttpMessageConverter<?>> messageConverters = new ArrayList<>();
            messageConverters.add(new StringHttpMessageConverter(StandardCharsets.UTF_8)); // text/html 처리용
            messageConverters.add(new MappingJackson2HttpMessageConverter()); // JSON 처리용
            restTemplate.setMessageConverters(messageConverters);

            return restTemplate;

        } catch (Exception e) {
            System.err.println("SSL 설정 실패, 기본 RestTemplate 사용: " + e.getMessage());

            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
            factory.setHttpClient(httpClient);
            factory.setConnectTimeout(15000);
            factory.setConnectionRequestTimeout(15000);

            RestTemplate restTemplate = new RestTemplate(factory);

            List<HttpMessageConverter<?>> messageConverters = new ArrayList<>();
            messageConverters.add(new StringHttpMessageConverter(StandardCharsets.UTF_8));
            messageConverters.add(new MappingJackson2HttpMessageConverter());
            restTemplate.setMessageConverters(messageConverters);

            return restTemplate;
        }
    }
}
