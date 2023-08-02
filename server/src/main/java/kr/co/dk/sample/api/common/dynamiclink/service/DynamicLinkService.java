package kr.co.dk.sample.api.common.dynamiclink.service;

import kr.co.dk.sample.api.common.dynamiclink.dto.*;
import kr.co.dk.sample.api.common.model.enums.type.LinkType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class DynamicLinkService {
    private static final Logger log = LogManager.getLogger(DynamicLinkService.class);
    private final String domainUriPrefix;
    private final IosInfo iosInfo;
    private final AndroidInfo androidInfo;
    private final WebClient dynamicLinkWebClient;
    private final String apiKey;
    @Value("${dynamic.link.url}")
    private String dynamicLink;

    public DynamicLinkService(WebClient dynamicLinkWebClient, @Value("${dynamic.link.domainUriPrefix}") String domainUriPrefix
            , @Value("${dynamic.link.androidPackageName}") String androidPackageName, @Value("${dynamic.link.iosBundleId}") String iosBundleId
            , @Value("${dynamic.link.apikey}") String apiKey) {
        this.apiKey = apiKey;
        this.domainUriPrefix = domainUriPrefix;
        this.dynamicLinkWebClient = dynamicLinkWebClient;
        this.iosInfo = new IosInfo();
        this.androidInfo = new AndroidInfo();
        iosInfo.setIosBundleId(iosBundleId);
        androidInfo.setAndroidPackageName(androidPackageName);
    }

    public DynamicLinkResponse getDynamicLink(String targetIdx, LinkType type) {

        DynamicLinkResponse dynamicLinkResponse = dynamicLinkWebClient.post()
                .uri(uriBuilder -> uriBuilder.path("/v1/shortLinks").queryParam("key", "{apiKey}").build(apiKey))
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(getDynamicLinkRequest(targetIdx, type))
                .retrieve()
                .bodyToMono(DynamicLinkResponse.class).block();
        return dynamicLinkResponse;
    }

    private DynamicLinkRequest getDynamicLinkRequest(String idx, LinkType type) {
        DynamicLinkRequest dynamicLinkRequest = new DynamicLinkRequest();
        DynamicLinkInfo dynamicLinkInfo = new DynamicLinkInfo();
        dynamicLinkInfo.setDomainUriPrefix(domainUriPrefix);
        dynamicLinkInfo.setLink(dynamicLink + "?idx=" + idx + "&type=" + type.name());
        dynamicLinkInfo.setAndroidInfo(androidInfo);
        dynamicLinkInfo.setIosInfo(iosInfo);
        dynamicLinkRequest.setDynamicLinkInfo(dynamicLinkInfo);
        return dynamicLinkRequest;
    }

}
