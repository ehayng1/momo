package kr.co.dk.sample.api.app.report.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.dk.sample.api.app.report.service.ReportService;
import kr.co.dk.sample.api.common.model.ApiResponse;
import kr.co.dk.sample.api.common.model.ErrorCode;
import kr.co.dk.sample.api.config.security.JwtTokenProvider;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Tag(name="신고")
@RestController
public class ReportController {
    @Autowired
    ReportService reportService;
    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @Operation(summary = "게시글 / 댓글 신고")
    @PostMapping("/api/v1/report/insert")
    public ResponseEntity<?> insertReport(
            HttpServletRequest request,
            @Schema(
                    description = "게시글 신고 파라미터",
                    type = "array",
                    example = "{" +
                            "\"reportedIdx\": \"1\"," +
                            "\"category\": \"board, comment, chat\"" +
                            "}")
            @RequestBody
            Map<String, Object> paramMap) throws Exception {
        try {
            Map<String, Object> jwtMap = jwtTokenProvider.getJwtInfo(request);
            if (null == jwtMap) {
                return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_1104));
            }
            if (paramMap == null) {
                return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_9002));
            }

            paramMap.put("mb_idx", jwtMap.get("mb_idx").toString());
            int result = reportService.insertReport(paramMap);

            if (result <= 0) {
                return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_2199));
            }

            return ResponseEntity.ok(new ApiResponse(true, "success", 200));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_500));
        }
    }


    @Operation(summary = "채팅 사용자 신고")
    @PostMapping("/api/v1/report/insert/chat")
    public ResponseEntity<?> insertChatReport(
            HttpServletRequest request,
            @Schema(
                    description = "게시글 신고 파라미터",
                    type = "array",
                    example = "{" +
                            "\"name\": \"string\"," +
                            "\"type\": \"nickname / name\"" +
                            "}")
            @RequestBody
            Map<String, Object> paramMap) throws Exception {
        try {
            Map<String, Object> jwtMap = jwtTokenProvider.getJwtInfo(request);
            if (null == jwtMap) {
                return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_1104));
            }
            if (paramMap == null) {
                return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_9002));
            }

            paramMap.put("mb_idx", jwtMap.get("mb_idx").toString());
            Map<String, Object> idxMap = reportService.getUserIdx(paramMap);
            if (idxMap == null) {
                return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_2199));
            }
            paramMap.put("reportedIdx", idxMap.get("reportedidx").toString());
            paramMap.put("category", "chat");
            int result = reportService.insertReport(paramMap);

            if (result <= 0) {
                return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_2199));
            }

            return ResponseEntity.ok(new ApiResponse(true, "success", 200));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_500));
        }
    }

}
