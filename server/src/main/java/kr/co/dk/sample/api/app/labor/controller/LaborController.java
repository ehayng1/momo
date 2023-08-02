package kr.co.dk.sample.api.app.labor.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.dk.sample.api.app.labor.dto.LaborDTO;
import kr.co.dk.sample.api.app.labor.service.LaborService;
import kr.co.dk.sample.api.common.model.ApiResponse;
import kr.co.dk.sample.api.common.model.ErrorCode;
import kr.co.dk.sample.api.common.model.Paging;
import kr.co.dk.sample.api.common.util.S3Util;
import kr.co.dk.sample.api.config.security.JwtTokenProvider;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static kr.co.dk.sample.api.common.util.CommonUtil.makeForeach;

@Tag(name = "노무")
@RestController
public class LaborController {

    private static final Logger log = LogManager.getLogger(LaborController.class);

    @Autowired
    LaborService laborService;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @Autowired
    S3Util s3Util;

    @Operation(summary = "타입 전체목록")
    @GetMapping(path = "/api/v1/labor/open/typeList")
    public ResponseEntity<?> getTypeList(HttpServletRequest request) throws Exception {
        Map<String, Object> rtnMap = new HashMap<String, Object>();
        try {

            List<Map> result = laborService.getTypeList();

            rtnMap.put("result", result);
            return ResponseEntity.ok(new ApiResponse(true, rtnMap, "success", 200));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_500));
        }
    }

    @Operation(summary = "노무 목록")
    @GetMapping(path = "/api/v1/labor/open/list/{type}/{pageNo}")
    public ResponseEntity<?> selectLaborList(HttpServletRequest request,
                                             @Parameter(name = "type", description = "노무사 타입 (','로 구분)", in = ParameterIn.PATH)
                                             @PathVariable String type,
                                             @Parameter(name = "pageNo", description = "페이지 번호", in = ParameterIn.PATH)
                                             @PathVariable String pageNo) throws Exception {
        Map<String, Object> rtnMap = new HashMap<String, Object>();
        try {
//            Map<String, Object> jwtMap = jwtTokenProvider.getJwtInfo(request);
//            if (null == jwtMap) {
//                return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_1104));
//            }
            Map<String, Object> paramMap = new HashMap<>();
//            paramMap.put("category", category.toString());
            if (null == pageNo || null == type) {
                return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_9002));
            }
            paramMap.put("pageNo", pageNo);
            if (!type.equals("all")) {
                paramMap.put("list", makeForeach(type));
            }

            Paging paging = laborService.setLaborPaging(paramMap);
            List<Map> laborList = laborService.selectLaborList(paramMap);

            rtnMap.put("laborList", laborList);
            rtnMap.put("paging", paging);
            return ResponseEntity.ok(new ApiResponse(true, rtnMap, "success", 200));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_500));
        }
    }

    @Operation(summary = "노무사 등록")
    @PostMapping("/api/v1/labor/open/regi")
    public ResponseEntity<?> insertLaborInfo(HttpServletRequest request, @Schema(
            description = "nickname",
            type = "array",
            example = " {\"labor_name\" :\"\" ,"
                    + "\"labor_simple_explain\": \"\","
                    + "\"labor_phone\": \"\","
                    + "\"labor_career\": \"\","
                    + "\"labor_description\": \"\","
                    + "\"labor_address\": \"\","
                    + "\"labor_image\": \"\","
    )
    @Parameter(required = true) LaborDTO laborDTO) throws Exception {
        Map<String, Object> rtnMap = new HashMap<String, Object>();
        try {
            rtnMap.put("labor_name", laborDTO.getLabor_name());
            rtnMap.put("labor_simple_explain", laborDTO.getLabor_simple_explain());
            rtnMap.put("labor_description", laborDTO.getLabor_description());

            rtnMap.put("labor_phone", laborDTO.getLabor_phone());
            String path = "";
            if (laborDTO.getLabor_image() != null) {
                path = s3Util.uploadSingleFile(laborDTO.getLabor_image(), "labor/");
//                log.info(path + "1");
            }
            rtnMap.put("labor_address", laborDTO.getLabor_address());
            rtnMap.put("labor_image", path);
            rtnMap.put("labor_career", laborDTO.getLabor_career());

            int result = laborService.insertLaborInfo(rtnMap);
            if (result <= 0) {
                return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_9989));
            }

            return ResponseEntity.ok(new ApiResponse(true, "success", 200));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_500));
        }
    }

    @Operation(summary = "노무사 상세정보")
    @GetMapping(path = "/api/v1/labor/open/detail/{idx}")
    public ResponseEntity<?> selectLaborDetail(HttpServletRequest request,
                                               @Parameter(name = "idx", description = "노무사 번호", in = ParameterIn.PATH)
                                               @PathVariable String idx) throws Exception {
        Map<String, Object> rtnMap = new HashMap<String, Object>();
        try {
//            Map<String, Object> jwtMap = jwtTokenProvider.getJwtInfo(request);
//            if (null == jwtMap) {
//                return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_1104));
//            }
            Map<String, Object> paramMap = new HashMap<>();
//            paramMap.put("category", category.toString());
            if (null == idx) {
                return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_9002));
            }
            paramMap.put("idx", idx);

            Map<String, Object> result = laborService.selectLaborDetail(paramMap);

            rtnMap.put("result", result);
            return ResponseEntity.ok(new ApiResponse(true, rtnMap, "success", 200));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_500));
        }
    }
}
