package kr.co.dk.sample.api.app.terms.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.dk.sample.api.app.terms.service.TermsService;
import kr.co.dk.sample.api.common.model.ApiResponse;
import kr.co.dk.sample.api.common.model.ErrorCode;
import kr.co.dk.sample.api.config.security.JwtTokenProvider;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Tag(name = "약관")
@RestController
public class TermsController {

    private static final Logger log = LogManager.getLogger(TermsController.class);

    @Autowired
    TermsService termsService;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @Operation(summary = "약관 내용 가져오기", description = "")
    @GetMapping(path = "/api/v1/terms/open/detail/{idx}")
    public ResponseEntity<?> getTermsDetail(HttpServletRequest request, @PathVariable(name = "idx") String idx) throws Exception {
        Map<String, Object> rtnMap = new HashMap<String, Object>();
        try {

            if (idx == null) {
                return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_9002));
            }
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("idx", idx);

            Map<String, Object> result = termsService.getTermsDetail(paramMap);

            if (result == null || result.isEmpty()) {
                return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_9988));
            }

            rtnMap.put("result", result);

            return ResponseEntity.ok(new ApiResponse(true, rtnMap, "success", 200));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_500));
        }
    }
}
