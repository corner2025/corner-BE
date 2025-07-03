package com.corner.travel.touristSpot.api.config;

import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactory;
import org.apache.hc.client5.http.ssl.TrustSelfSignedStrategy;
import org.apache.hc.core5.ssl.SSLContextBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;

@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate() {
        try {
            // ✅ SSL 컨텍스트 설정 - 공공데이터포털 SSL 이슈 해결
            SSLContext sslContext = SSLContextBuilder
                    .create()
                    .loadTrustMaterial(new TrustSelfSignedStrategy())
                    .build();

            SSLConnectionSocketFactory sslConnectionSocketFactory =
                    new SSLConnectionSocketFactory(sslContext);

            // ✅ HttpClient 생성
            CloseableHttpClient httpClient = HttpClients.custom()
                    .setUserAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36")
                    .build();

            // ✅ RequestFactory 설정
            HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
            factory.setHttpClient(httpClient);
            factory.setConnectTimeout(15000); // 15초로 증가
            factory.setConnectionRequestTimeout(15000); // 15초로 증가

            return new RestTemplate(factory);

        } catch (Exception e) {
            // SSL 설정 실패 시 기본 RestTemplate 반환
            System.err.println("SSL 설정 실패, 기본 RestTemplate 사용: " + e.getMessage());

            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
            factory.setHttpClient(httpClient);
            factory.setConnectTimeout(15000);
            factory.setConnectionRequestTimeout(15000);

            return new RestTemplate(factory);
        }
    }
}