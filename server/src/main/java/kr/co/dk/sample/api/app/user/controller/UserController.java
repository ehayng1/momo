package kr.co.dk.sample.api.app.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import kr.co.dk.sample.api.app.user.service.UserService;
import kr.co.dk.sample.api.common.model.ApiResponse;
import kr.co.dk.sample.api.common.model.ErrorCode;
import kr.co.dk.sample.api.config.security.JwtTokenProvider;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
public class UserController {
    private static final Logger log = LogManager.getLogger(UserController.class);

    @Autowired
    UserService userService;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @Operation(summary = "1.유저 상세 정보 ", description = "")
    @GetMapping("/api/v1/user/selectUserInfo")
    public ResponseEntity<?> selectUserInfo(HttpServletRequest request) throws Throwable {
        Map<String, Object> rtnMap = new HashMap<String, Object>();
        try {
            Map<String, Object> jwtMap = jwtTokenProvider.getJwtInfo(request);
            if (null == jwtMap) {
                return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_1104));
            }
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("mb_idx", jwtMap.get("mb_idx"));
            Map<String, Object> userInfo = userService.selectUserInfo(paramMap);

            rtnMap.put("user", userInfo);
            return ResponseEntity.ok(new ApiResponse(true, rtnMap));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_500));
        }
    }

//    @Transactional
//    public UserDetails loadUserByIdx(long idx) throws CustomException {
//        UserDetails userDetails = null;
//        User user = userService.findByIdx(idx);
//        if(user == null){
//            throw new CustomException(ErrorCode.CODE_1108);
//        }
//        user.getAuthorities();
//        userDetails= UserPrincipal.create(user);
//        return userDetails;
//    }

}
