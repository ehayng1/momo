package kr.co.dk.sample.api.common.dynamiclink.dto;

import lombok.Data;

@Data
public class DynamicLinkInfo {
    private String domainUriPrefix;
    private String link;
    private AndroidInfo androidInfo;
    private IosInfo iosInfo;
}
