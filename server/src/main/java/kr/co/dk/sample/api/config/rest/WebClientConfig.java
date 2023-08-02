package kr.co.dk.sample.api.config.rest;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient kakaoWebClient() {
        return WebClient.builder().baseUrl("https://kapi.kakao.com").filter(ExchangeFilterFunction.ofResponseProcessor(clientResponse -> {
            if (clientResponse.statusCode().isError()) {
                return clientResponse.bodyToMono(KakaoExceptionDetails.class)
                        .flatMap(errorDetails -> Mono.error(new CustomClientException(clientResponse.statusCode(), errorDetails)));
            }
            return Mono.just(clientResponse);
        })).build();
    }

    @Bean
    public WebClient naverWebClient(){
        return WebClient.builder().baseUrl("https://openapi.naver.com").filter(ExchangeFilterFunction.ofResponseProcessor(clientResponse -> {
            if (clientResponse.statusCode().isError()) {
                return clientResponse.bodyToMono(KakaoExceptionDetails.class)
                        .flatMap(errorDetails -> Mono.error(new CustomClientException(clientResponse.statusCode(), errorDetails)));
            }
            return Mono.just(clientResponse);
        })).build();
    }

    @Bean
    public WebClient dynamicLinkWebClient() {
        return WebClient.builder().baseUrl("https://firebasedynamiclinks.googleapis.com").filter(ExchangeFilterFunction.ofResponseProcessor(clientResponse -> {
            if (clientResponse.statusCode().isError()) {
                return clientResponse.bodyToMono(KakaoExceptionDetails.class)
                        .flatMap(errorDetails -> Mono.error(new CustomClientException(clientResponse.statusCode(), errorDetails)));
            }
            return Mono.just(clientResponse);
        })).build();
    }

}
