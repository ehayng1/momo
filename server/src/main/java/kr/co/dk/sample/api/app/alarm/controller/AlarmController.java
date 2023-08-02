package kr.co.dk.sample.api.app.alarm.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.dk.sample.api.app.alarm.service.AlarmService;
import kr.co.dk.sample.api.common.model.ApiResponse;
import kr.co.dk.sample.api.common.model.ErrorCode;
import kr.co.dk.sample.api.config.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Tag(name = "알림")
@RestController
public class AlarmController {
    @Autowired
    AlarmService alarmService;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @Operation(summary = "알림 목록")
    @GetMapping("/api/v1/alarm/list")
    public ResponseEntity<?> selectAlarmList(HttpServletRequest request) throws Exception {
        Map<String, Object> rtnMap = new HashMap<String, Object>();
        try {
            Map<String, Object> jwtMap = jwtTokenProvider.getJwtInfo(request);
            if (null == jwtMap) {
                return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_1104));
            }
            List<Map> companyList = alarmService.selectAlarmList(jwtMap);

            rtnMap.put("result", companyList);
            return ResponseEntity.ok(new ApiResponse(true, rtnMap, "success", 200));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_500));
        }
    }

}
