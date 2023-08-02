package kr.co.dk.sample.api.app.banner.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.dk.sample.api.app.banner.service.BannerService;
import kr.co.dk.sample.api.common.model.ApiResponse;
import kr.co.dk.sample.api.common.model.ErrorCode;
import kr.co.dk.sample.api.config.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Tag(name = "배너")
@RestController
public class BannerController {
    @Autowired
    BannerService bannerService;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @Operation(summary = "배너 전체목록")
    @GetMapping(path = "/api/v1/banner/open/list")
    public ResponseEntity<?> getBannerList() throws Exception {
        Map<String, Object> rtnMap = new HashMap<String, Object>();
        try {

            List<Map> result = bannerService.getBannerList();

            rtnMap.put("result", result);
            return ResponseEntity.ok(new ApiResponse(true, rtnMap, "success", 200));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_500));
        }
    }

}
