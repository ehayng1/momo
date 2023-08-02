package kr.co.dk.sample.api.app.like.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.dk.sample.api.app.like.service.LikeService;
import kr.co.dk.sample.api.common.model.ApiResponse;
import kr.co.dk.sample.api.common.model.ErrorCode;
import kr.co.dk.sample.api.config.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Tag(name = "찜하기")
@RestController
public class LikeController {
    @Autowired
    LikeService likeService;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @Operation(summary = "찜하기 추가", description = "type : 0 = 일자리, 1 = 장비, boardNum : 일자리/장비 idx")
    @PostMapping(path = "/api/v1/like/insert")
    public ResponseEntity<?> addLike(
            @Schema(
                    description = "type, boardNum",
                    type = "array",
                    example = " {\"type\" :\"0, 1\","
                            + "\"boardNum\": \"1\"}")
            @RequestBody Map<String, Object> paramMap, HttpServletRequest request) throws Exception {

        Map<String, Object> rtnMap = new HashMap<String, Object>();
        try {
            Map<String, Object> jwtMap = jwtTokenProvider.getJwtInfo(request);
            if (null == jwtMap) {
                return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_1104));
            }

            paramMap.put("mb_idx", jwtMap.get("mb_idx"));
            int result = likeService.addLike(paramMap);

            rtnMap.put("result", result);
            return ResponseEntity.ok(new ApiResponse(true, rtnMap, "success", 200));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_500));
        }
    }

    @Operation(summary = "찜하기 토글", description = "type : 0 = 일자리, 1 = 장비, boardNum : 일자리/장비 idx")
    @PostMapping(path = "/api/v1/like/update")
    public ResponseEntity<?> toggleLike(
            @Schema(
                    description = "type, boardNum",
                    type = "array",
                    example = " {\"type\" :\"0, 1\","
                            + "\"boardNum\": \"1\"}")
            @RequestBody Map<String, Object> paramMap, HttpServletRequest request) throws Exception {

        Map<String, Object> rtnMap = new HashMap<String, Object>();
        try {
            Map<String, Object> jwtMap = jwtTokenProvider.getJwtInfo(request);
            if (null == jwtMap) {
                return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_1104));
            }
            paramMap.put("mb_idx", jwtMap.get("mb_idx"));
            int result = likeService.toggleLike(paramMap);

            rtnMap.put("result", result);
            return ResponseEntity.ok(new ApiResponse(true, rtnMap, "success", 200));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_500));
        }
    }
}
