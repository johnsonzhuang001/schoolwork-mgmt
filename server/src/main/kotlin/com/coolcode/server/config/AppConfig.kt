package com.coolcode.server.config

import org.apache.hc.client5.http.impl.classic.HttpClients
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactory;
import org.apache.hc.core5.ssl.SSLContexts
import org.springframework.boot.ssl.SslBundles
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory
import org.springframework.web.client.RestClient
import org.springframework.web.client.RestTemplate
import java.security.cert.X509Certificate

@Configuration
class AppConfig {
    // Secure client to api.crazy-collectors.com
    @Bean
    fun crazyCollectorsClient(restTemplateBuilder: RestTemplateBuilder, sslBundles: SslBundles): RestClient {
        return RestClient.create(restTemplateBuilder.setSslBundle(sslBundles.getBundle("crazy-collectors")).build())
    }

    // Secure client to herokuapp.com
    @Bean
    fun herokuClient(restTemplateBuilder: RestTemplateBuilder, sslBundles: SslBundles): RestClient {
        return RestClient.create(restTemplateBuilder.setSslBundle(sslBundles.getBundle("heroku")).build())
    }

    // Insecure client to trust any downstream system
    @Bean
    fun restClient(): RestClient {
        val acceptingTrustStrategy: (Array<X509Certificate>, String) -> Boolean = { _, _ -> true }
        val sslContext = SSLContexts.custom()
            .loadTrustMaterial(null, acceptingTrustStrategy)
            .build()
        val socketFactory = SSLConnectionSocketFactory(sslContext)
        val connectionManager = PoolingHttpClientConnectionManagerBuilder.create()
            .setSSLSocketFactory(socketFactory)
            .build()
        val httpClient = HttpClients.custom()
            .setConnectionManager(connectionManager)
            .build()
        val requestFactory = HttpComponentsClientHttpRequestFactory(httpClient)
        requestFactory.setHttpClient(httpClient)
        val restTemplate = RestTemplate(requestFactory)
        return RestClient.create(restTemplate)
    }
}