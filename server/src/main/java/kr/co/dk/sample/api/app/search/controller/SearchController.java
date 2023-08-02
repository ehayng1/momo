package kr.co.dk.sample.api.app.search.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.dk.sample.api.app.search.service.SearchService;
import kr.co.dk.sample.api.common.model.ApiResponse;
import kr.co.dk.sample.api.common.model.ErrorCode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Tag(name = "검색")
@RestController
public class SearchController {

    private static final Logger log = LogManager.getLogger(SearchController.class);

    @Autowired
    SearchService searchService;

    @Operation(summary = "검색")
    @CrossOrigin(origins = "*")
    @GetMapping("/api/v1/search/open/{category}/{value}")
    public ResponseEntity<?> search(
            HttpServletRequest request,
            @PathVariable(name = "category") String category,
            @PathVariable(name = "value") String value) throws Exception {
        Map<String, Object> rtnMap = new HashMap<String, Object>();
        try {

            if (category == null || value == null) {
                return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_9002));
            }

            Map<String, Object> paramMap = new HashMap<>();

            paramMap.put("category", category);
            paramMap.put("value", value);
            // 검색처리
            List<Map> result = searchService.search(paramMap);

            if (result == null || result.isEmpty()) {
                return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_3101));
            }

            rtnMap.put("result", result);

            return ResponseEntity.ok(new ApiResponse(true, rtnMap, "success", 200));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_500));
        }
    }
}
