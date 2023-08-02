package kr.co.dk.sample.api.app.auth.controller;

import com.amazonaws.services.sns.model.PublishResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.dk.sample.api.app.auth.dto.AuthEquipDTO;
import kr.co.dk.sample.api.app.auth.dto.AuthPhotoDTO;
import kr.co.dk.sample.api.app.auth.service.AuthService;
import kr.co.dk.sample.api.common.model.ApiResponse;
import kr.co.dk.sample.api.common.model.ErrorCode;
import kr.co.dk.sample.api.common.util.S3Util;
import kr.co.dk.sample.api.config.security.JwtTokenProvider;
import kr.co.dk.sample.api.util.ModelUtil;
import kr.co.dk.sample.api.util.RedisUtil;
import net.sf.json.JSONObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Tag(name = "인증")
@RestController
public class AuthController {

    private static final Logger log = LogManager.getLogger(AuthController.class);
    @Autowired
    S3Util s3Util;
    @Resource(name = "redisUtil")
    RedisUtil redisUtil;
    @Autowired
    private AuthService authService;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Value("${kakao.restapi.key}")
    private String kakaoRestKey;

    @Value("${naver.restapi.key}")
    private String naverRestKey;

    public void healthCheck() throws Exception {
//        log.info("ok");
    }

    public AuthController() {

    }

    @Operation(summary = "버전체크")
    @GetMapping("/api/v1/auth/open/version")
    public ResponseEntity<?> versionCheck(HttpServletRequest request) throws Exception {
        try {
            Map<String, Object> rtnMap = new HashMap<>();

            Map<String, Object> result = authService.getVersion();

            rtnMap.put("result", result);
            // 토큰 DB 검증 안하기 때문에 주석처리함
//            authService.insertJwtToken(resultMap, request);
            return ResponseEntity.ok(new ApiResponse(true, rtnMap, "success", 200));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_9999));
        }
    }

//    @Operation(summary = "버전 수정")
//    @PostMapping("/api/v1/auth/open/version/update")
//    public ResponseEntity<?> versionUpdate(
//            @Schema(
//                    description = "version",
//                    type = "array",
//                    example = " {\"version\" :\"1.1\"}")
//            @RequestBody Map<String, Object> paramMap, HttpServletRequest request) throws Exception {
//        log.info("=============================START========================================== in " + request.getRequestURL());
//
//        try {
//            if (paramMap == null) {
//                return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_9002));
//            }
//
//            Map<String, Object> rtnMap = new HashMap<>();
//
//            int result = authService.updateVersion(paramMap);
//
//            if(result <= 0) {
//                return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_9991));
//            }
//
//            rtnMap.put("result", result);
//            // 토큰 DB 검증 안하기 때문에 주석처리함
////            authService.insertJwtToken(resultMap, request);
//            return ResponseEntity.ok(new ApiResponse(true, rtnMap, "success", 200));
//        } catch (Exception e) {
//            e.printStackTrace();
//            return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_9999));
//        }
//    }

    //기존 소스에서 들고온 로직
//    @Operation(summary = "로그인 - SMS요청", description = "")
//    @PostMapping("/api/v1/auth/requestSms")
//    public ResponseEntity<?> requestSMS(HttpServletRequest request){
//        log.debug("=============================START= in " + request.getRequestURL());
//
//        //JWT토큰 가져오는 로직 ... 현재는 필요없음.
////        Map<String, Object> jwtMap = jwtTokenProvider.getJwtInfo(request);
////        if(null == jwtMap) {
////            return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_1119));
////        }
//
//        //인증요청시 디비 조회후 유저 정보가 없으면, 유저 컬럼 생성후 인증번호 발급하고 처리한다.
//        //인증요청시 디비 조회후 유저 정보가 있으면, 인증번호 발급 후 처리한다.
//        //로그아웃 로직인데, 수정필요
//        try {
//            Map<String, Object> paramMap = new HashMap<>();
////            paramMap.put("mb_idx", jwtMap.get("mb_idx"));
////            authService.updateJwtToken(paramMap);
//            return ResponseEntity.ok(new ApiResponse(true));
//        } catch(Exception e) {
//            return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_500));
//        }
//    }
//
//    //sms인증 완료되면, 유저정보 있다면 메인으로, 아닐경우 회원가입
//    @Operation(summary = "로그인 - SMS검증", description = "")
//    @PostMapping("/api/v1/auth/certSms")
//    public ResponseEntity<?> certSMS(HttpServletRequest request){
//        log.debug("=============================START= in " + request.getRequestURL());
//
//        Map<String, Object> jwtMap = jwtTokenProvider.getJwtInfo(request);
//        if(null == jwtMap) {
//            return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_1119));
//        }
//
//        //검증이후 정보 없는 유저라면 회원가입으로 진행
//        //로그아웃 로직인데, 수정필요
//        try {
//            Map<String, Object> paramMap = new HashMap<>();
//            paramMap.put("mb_idx", jwtMap.get("mb_idx"));
////            authService.updateJwtToken(paramMap);
//            return ResponseEntity.ok(new ApiResponse(true));
//        } catch(Exception e) {
//            return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_500));
//        }
//    }

    //sms인증 완료되면, 유저정보 있다면 메인으로, 아닐경우 회원가입
//    @Operation(summary = "로그인 -sms 인증 ",
//            responses = {@ApiResponse(responseCode = "200"
//                    , content = @Content(schema =@Schema(implementation = JwtAuthenticationResponse.class)))
//            })
//    @PostMapping("/api/v1/auth/certSms")
//    public ResponseEntity<?> authenticate(@Valid @RequestBody LoginDTO loginParams){
//        try {
//
//            JwtAuthenticationResponse jwtAuthenticationResponse = authService.signin(loginParams);
//            return ResponseEntity.ok(jwtAuthenticationResponse);
//        } catch (CustomException ce) {
//            log.error("Custom Exception : {}", ce);
////            return errorMessage(ce.getCode());
//            return ResponseEntity.status(401).body(ce.getCode());
//        } catch (Exception e) {
//            log.error("Exception : {}", e);
//            return errorMessage(ErrorCode.CODE_500);
//        }
//
//    }

    //apple login 필요할 경우 안전결제 참조하세요.
//    @Operation(summary = "소셜 로그인 ",
//            responses = {@ApiResponse(responseCode = "200"
//                    , content = @Content(schema =@Schema(implementation = JwtAuthenticationResponse.class)))
//            })
//    @PostMapping("/api/v1/auth/o-auth")
//    public ResponseEntity<?> authenticate(HttpServletRequest httpServletRequest, @Valid @RequestBody LoginRequestDTO loginRequestDTO){
//        try {
//            String platformToken =getTokenFromRequest(httpServletRequest);
//            loginRequestDTO.setAccessToken(platformToken);
//
//            if(NullCheckUtil.isNull(loginRequestDTO, loginRequestDTO.getAccessToken(), loginRequestDTO.getAuthPlatform(), loginRequestDTO.getFcmToken())){
//                log.error("null parameter : {}",loginRequestDTO.toString());
//                return errorMessage(ErrorCode.CODE_9002);
//            }
//            JwtAuthenticationResponse jwtAuthenticationResponse = authService.signinWithOAuth(loginRequestDTO);
//            return ResponseEntity.ok(jwtAuthenticationResponse);
//        } catch (CustomException ce) {
//            log.error("Custom Exception : {}", ce);
//            return errorMessage(ce.getCode());
//        } catch (Exception e) {
//            log.error("Exception : {}", e);
//            return errorMessage(ErrorCode.CODE_500);
//        }
//
//    }

//    @Operation(summary = "액세스 토큰 발급", description = "리프레시 토큰을 헤더에 넣어 요청해야함",
//            responses = {@ApiResponse(responseCode = "200"
//                    , content = @Content(schema =@Schema(implementation = JwtAuthenticationResponse.class)))
//            })
//    @GetMapping("/api/v1/auth/user/accessToken")
//    public ResponseEntity<?> authenticate(HttpServletRequest request){
//        try {
//            JwtAuthenticationResponse jwtAuthenticationResponse = authService.getNewAccessToken(request);
//            return ResponseEntity.ok(jwtAuthenticationResponse);
//        } catch (CustomException ce) {
//            log.error("Custom Exception : {}", ce);
//            return errorMessage(ce.getCode());
//        } catch (Exception e) {
//            log.error("Exception : {}", e);
//            return errorMessage(ErrorCode.CODE_500);
//        }
//    }

    //    @Operation(summary = "2-1-2. SMS 인증번호 발송 로그인유저용", description = " JWT토큰의 member_idx를 확인하여 해당 폰번호에 인증번호 발송한다." +
//            "phoneNo : 01011111111 ")
//    @PostMapping("/sendSmsCertifyNumIsLogin")
//    public ResponseEntity<?> sendSmsCertifyNumIsLogin(
//            @Schema(
//                    description = "",
//                    type = "array",
//                    example = " {\"phoneNo\" :\"01011111111\" }"
//            )
//            @RequestParam Map<String, Object> paramMap, HttpServletRequest request) throws Throwable {
//        log.debug("=============================START========================================== in " + request.getRequestURL());
//        Map<String, Object> rtnMap = new HashMap<String, Object>();
//        Map<String, Object> rtnMap2 = new HashMap<String, Object>();
//        try {
//            log.info(paramMap.toString());
//            String language = request.getAttribute("language") + "";
//            paramMap.put("language" , language);
//            Map<String, Object> jwtMap = jwtTokenProvider.getJwtInfo(request);
//            if (null == jwtMap) {
//                return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_9997));
//            }
//            paramMap.put("mb_idx", jwtMap.get("mb_idx"));
//            paramMap.put("mb_id", jwtMap.get("mb_id"));
//
//
//            //SMS 체크
//            Map<String, Object> mbMap = infoService.selectDetail(paramMap);
//            String mb_certify_phone = mbMap.get("mb_certify_phone") + "";
//            if (mb_certify_phone.equals("Y")) {
//                //폰번호 없으면 리턴
//                if (paramMap.get("phoneNo").equals("") || paramMap.get("phoneNo") == null) {
//                    return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_9999));
//                }
//
//                // 1. 전화번호 : 세션 or DB
//                String phoneNum = "";
//
//                //paramMap.put("mb_gubun", "1"); // 정상 회원
//                int phoneCnt = infoService.checkPhoneInfo(paramMap);
//                if (phoneCnt == 0) {
//                    return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_2008));
//                }
////				전화번호 정보가 여러개일때 추가 코드 - 정책이 정해지면 그때 반영
////				else if(phoneCnt > 1) {
////					mv.addObject(KEY_CODE, FAIL_CODE_DUP_PHONENO);
////					mv.addObject(KEY_MSG, FAIL_MSG_DUP_PHONENO);
////					return mv;
////
////				}
//
//                Map<String, Object> returnMap = infoService.selectCheckMbPhoneNo(paramMap);
//
//                if (returnMap == null) {
//                    return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_2014));
//                }
//
//                phoneNum = paramMap.get("phoneNo").toString().replaceAll("-", "").trim();
//
//                // 2. 6자리 숫자 렌덤
//                String randomCodeNumber = RandomCodeUtill.randomCodeNumber(6);
//
//                // 3. SMS 유효시간
//                int expireTime = 3; // 디폴트 3분하자
//
//                Map<String, Object> map = redisUtil.getRedisDataMap(RD_EXPIRE_TIME);
//                if (map != null && map.containsKey("SMS_EXPIRE_TIME")) {
//                    expireTime = Integer.parseInt(map.get("SMS_EXPIRE_TIME") + "");
//                }
//                log.debug("map :: " + map);
//
//                // 4. 문자전송
//                paramMap.put("alarm_code", ALARM_CODE_CERT_NO);
//                paramMap.put("phone_no", phoneNum);
//                paramMap.put("cert_no", randomCodeNumber);
//                //필수값 세팅
//                paramMap.put("mb_alarm_etc", mbMap.get("mb_alarm_etc"));
//                paramMap.put("mb_alarm_trade", mbMap.get("mb_alarm_trade"));
//                paramMap.put("lan",language);
////			tempBlockAlarm
//                String rtnMsg = infoAlarmService.sendAlarm(paramMap);
//                log.debug("문자전송 rtnMsg :: " + rtnMsg);
//
//                // 5. DB insert (mb_certification_code)
//                // 오늘날짜 출력
//                Date date = DateUtil.addTimeByMinute(new Date(), expireTime);    // 유효시간 (3분)
//                paramMap.put("mb_id", "-");                                    // 이메일 인증시에만 들어간다
//                paramMap.put("reg_date", DateUtil.getStringNowDateTime());        // 요청일
//                paramMap.put("expire_date", DateUtil.getStringFromDate(date, "yyyy-MM-dd HH:mm"));    //유효시간
//                paramMap.put("code", randomCodeNumber);                            // 인증코드
//                int ret = infoService.insertUserCertification(paramMap);
//                rtnMap2.put("certify_idx", paramMap.get("idx"));
//                rtnMap.put("result", rtnMap2);
//                log.debug("저장 rtnMap2 :: " + rtnMap2);
//            } else if(mb_certify_phone.equals("N")){
//
//                String email = jwtMap.get("mb_id") + "";
//                // 1. 랜덤코드생성
//                String randomCodeNumber = RandomCodeUtill.randomCodeNumber(6);
//                // 2. SMS 유효시간
//                int expireTime = 3; // 디폴트 3분하자
//
//                Map<String, Object> map = redisUtil.getRedisDataMap(RD_EXPIRE_TIME);
//                if (map != null && map.containsKey("SMS_EXPIRE_TIME")) {
//                    expireTime = Integer.parseInt(map.get("SMS_EXPIRE_TIME") + "");
//                }
//                log.debug("map :: " + map);
//
//                //3. 메일발송
//                Map<String, Object> emailSendMap = new HashMap<String, Object>();
//                emailSendMap.put("alarm_code", ALARM_CODE_CERT_NO_EMAIL);// 이메일 인증
//                emailSendMap.put("mb_id", email);
//                emailSendMap.put("cert_no", randomCodeNumber);
//                emailSendMap.put("mb_alarm_trade", "Y");
//                emailSendMap.put("mb_alarm_etc", "Y");
//                emailSendMap.put("lan",language);
//                String mailResult = infoAlarmService.sendAlarm(emailSendMap);
//
//                // 5. DB insert (mb_certification_code)
//                // 오늘날짜 출력
//                Date date = DateUtil.addTimeByMinute(new Date(), expireTime);    // 유효시간 (3분)
//                paramMap.put("mb_id", email);                                    // 이메일 인증시에만 들어간다
//                paramMap.put("reg_date", DateUtil.getStringNowDateTime());        // 요청일
//                paramMap.put("expire_date", DateUtil.getStringFromDate(date, "yyyy-MM-dd HH:mm"));    //유효시간
//                paramMap.put("code", randomCodeNumber);                            // 인증코드
//                int ret = infoService.insertUserCertification(paramMap);
//                rtnMap2.put("certify_idx", paramMap.get("idx"));
//
//                rtnMap.put("result", rtnMap2);
//                log.debug("저장 rtnMap2 :: " + rtnMap2);
//
//            }
//            return ResponseEntity.ok(new ApiResponse(true, rtnMap, ErrorCode.CODE_0000));
//        }
//        catch (Exception e) {
//            e.printStackTrace();
//            return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_9999));
//        }
//    }
//
//    @Operation(summary = "2-1-3. SMS 인증번호 확인 로그인유저용", description = "certify_idx(seq) = 3, certify_num(code) = 772211" +
//            "mb_certification_code 테이블 참조")
//    @PostMapping("/confirmSmsCertifyNumIsLogin")
//    public ResponseEntity<?> confirmSmsCertifyNumIsLogin(
//            @Schema(
//                    description = "",
//                    type = "array",
//                    example = " {\"certify_idx\" :\"19\" ,"
//                            + "\"certify_num\": \"805410\"}"
//            )
//            @RequestParam Map<String, Object> paramMap, HttpServletRequest request) throws Throwable {
//        log.debug("=============================START========================================== in " + request.getRequestURL());
//        Map<String, Object> rtnMap = new HashMap<String, Object>();
//        Map<String, Object> rtnMap2 = new HashMap<String, Object>();
//        try {
//            log.info(paramMap.toString());
//            Map<String, Object> jwtMap = jwtTokenProvider.getJwtInfo(request);
//            if (null == jwtMap) {
//                return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_9997));
//            }
//            paramMap.put("mb_idx", jwtMap.get("mb_idx"));
//
//            log.debug("paramMap :: " + paramMap);
//            log.info("paramMap :: " + paramMap);
//
//            // 1. 인증데이타 가져오기
//            Map<String, Object> mapCertifi = infoService.selectUserCertification(paramMap);
//            // 2. 인증번호 발송 여부
//            if (mapCertifi == null) {
//                // [5200] 인증번호를 확인하시기 바랍니다.
//                return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_5200));
//            }
//
//            // 3. 유효시간 체크
//            String expireDate = mapCertifi.get("expire_date") + ""; // yyyy-MM-dd HH:mm
//            String nowDate = DateUtil.getToday("yyyy-MM-dd HH:mm");
//            log.debug("expireDate :: " + expireDate);
//            log.debug("nowDate :: " + nowDate);
//            log.debug("DiffMinute :: " + DateUtil.getDiffMinuteCount(nowDate, expireDate));
//            if (DateUtil.getDiffMinuteCount(nowDate, expireDate) < 0) {
//                // [5210] 인증시간이 초과 하였습니다.
//                return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_5210));
//            }
//
//            // 4. 인증번호 확인 유무
//            if ("Y".equals(mapCertifi.get("confirm"))) {
//                // [5220] 이미 인증번호를 확인 하였습니다.
//                return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_5220));
//            }
//
//            // 5. 인증번호 테이블 업데이트 (mb_certification_code)
//            infoService.updateUserCertification(paramMap);
//
//            return ResponseEntity.ok(new ApiResponse(true, ErrorCode.CODE_0000));
//        } catch (Exception e) {
//            e.printStackTrace();
//            return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_9999));
//        }
//    }
//
    @Operation(summary = "JWT AccessToken 재발행", description = "")
    @GetMapping("/getAccessToken")
    public ResponseEntity<?> refreshToken(HttpServletRequest request) {
        try {
            ApiResponse apiResponse = authService.getNewAccessToken(request);
            if (!apiResponse.getSuccess()) {
                return ResponseEntity.ok(apiResponse);
            }
            Map<String, Object> resultMap = ModelUtil.toMap(apiResponse.getData());

            Map<String, Object> accessJwt = jwtTokenProvider.getJwtInfo(
                    resultMap.get("accessToken") + "");

            resultMap.put("mb_idx", accessJwt.get("mb_idx"));
            // 토큰 DB 검증 안하기 때문에 주석처리함
//            authService.insertJwtToken(resultMap, request);
            return ResponseEntity.ok(apiResponse);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_9999));
        }

    }

    @Operation(summary = "1-2-3 회원여부 체크", description = "phone: 휴대폰 번호")
    @PostMapping("/api/v1/auth/open/checkUserExist")
    public ResponseEntity<?> checkUserExist(
            @Schema(
                    description = "phone",
                    type = "array",
                    example = " {\"phone\" :\"010-1234-1234\"}")
            @RequestBody Map<String, Object> paramMap, HttpServletRequest request) {
        try {
            if (paramMap.get("phone") == null || paramMap.get("phone").toString() == "") {
                return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_9998));
            }
            Map<String, Object> rtnMap = authService.checkUserExist(paramMap);
            if (rtnMap == null) {
                return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_9002));
            } else if (Integer.parseInt(rtnMap.get("cnt").toString()) > 0) {
                rtnMap.put("exist", true);
            } else if (Integer.parseInt(rtnMap.get("cnt").toString()) == 0) {
                rtnMap.put("exist", false);
            }

            int code = (int) (Math.random() * 1000000); // 000000 ~ 999999 중 랜덤
            log.info(code);
            String codeStr = String.format("%06d",
                    code); // 000xxx같은 숫자는 xxx만 들어감, 앞자리 0으로 채우기
            paramMap.put("code", codeStr);
            paramMap.put("msg", "건설모모 인증코드는 [" + codeStr + "] 입니다.");
            PublishResult smsResult = authService.sendSMSCode(paramMap);

            if (smsResult == null) {
                return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_9992));
            }

            int result = authService.insertPhoneAuthentication(paramMap);

            if (result > 0) {
                return ResponseEntity.ok(new ApiResponse(true, rtnMap, "success", 200));
            } else {
                return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_1110));
            }

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_9999));
        }
    }

    @Operation(summary = "1-2-4 회원 로그인", description = "phone : 로그인비밀번호, code : 인증번호, exist : 회원정보 여부")
    @PostMapping("/api/v1/auth/open/loginUser")
    public ResponseEntity<?> loginUser(
            @Schema(
                    description = "phone, code",
                    type = "array",
                    example = " {\"phone\" :\"010-1234-1234\" ,"
                            + "\"code\": \"123456\","
                            + "\"exist\": \"true\","

                            + "\"fcm_token\": \"string\"}")
            @RequestBody Map<String, Object> paramMap, HttpServletRequest request) {

        Map<String, Object> rtnMap = new HashMap<>();
        try {
            if (paramMap.get("phone") == null || paramMap.get("code") == null) {
                return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_9998));
            }

            /*
                1. phone_authentication의 코드 체크 -> 제일마지막꺼, 회수는 5회까지
                --- 가입된 사용자
                ----- user_status에 따른 처리
                    0   user_idx를 mb_idx로 넣고 jwt토큰으로 변환 후 반환
                    1   비활성화된 상태
                    3   탈퇴처리된 유저
                --- 미가입 사용자
                회원가입 진행. 데이터 반환할 필요 없음
                --- 코드가 일치하지 않을 때
                테이블의 count를 1 올리고 실패처리
                --- count가 5 이상일 때
                횟수 초과처리

             */

            // 로그인처리
            /*
                result.get("msg")
                - count 횟수초과
                - notEquals 불일치
                - increaseError 불일치 & 증감실패
                - enroll 가입해야함
                - success 유저정보 있고 성공함, status 판별 필요
                - error 서버에러
             */
            Map<String, Object> result = authService.selectMember(paramMap);
            if (result == null) {
                return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_9999));
            }
            if (result.get("msg").toString().equals("error")) {
                return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_9999));
            } else if (result.get("msg").toString().equals("increaseError")) {
                return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_9999));
            } else if (result.get("msg").toString().equals("notEquals")) {
                rtnMap.put("msg", "인증코드가 틀립니다.");
                rtnMap.put("code", "1");
            } else if (result.get("msg").toString().equals("count")) {
                rtnMap.put("msg", "횟수가 초과하였습니다.");
                rtnMap.put("code", "2");
            } else if (result.get("msg").toString().equals("enroll")) {
                rtnMap.put("msg", "회원가입 후 로그인이 가능합니다.");
                rtnMap.put("code", "0");
            } else if (result.get("msg").toString().equals("success")) {
                rtnMap = result; // 로그인정보를 가지고있음
                rtnMap.put("mb_idx", rtnMap.get("user_idx").toString());
//                ApiResponse apiResponse = authService.getLoginToken(request, rtnMap);
                return ResponseEntity.ok(
                        new ApiResponse(true, rtnMap.get("token").toString(), "success", 200));
            } else {
                return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_9999));
            }

            return ResponseEntity.ok(new ApiResponse(true, rtnMap, "success", 200));


        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_9999));
        }
    }

    @Operation(summary = "1-2-5 회원가입", description = "phone : 전화번호, location : 선택 지역, check : 마케팅 수신여부(Y, N로 구분)")
    @PostMapping("/api/v1/auth/open/enrollUser")
    public ResponseEntity<?> enrollUser(HttpServletRequest request,
//          @Schema(
//              description = "phone, location, check",
//              type = "array",
//              example = " {\"phone\" :\"010-1234-1234\" ,"
//                  + "\"location\": \"서울:경기:부산\","
//                  + "\"check\": \"Y\","
//                  + "\"user_type\": \"\","
//
//                  + "\"fcm_token\": \"string\"}")
                                        @Parameter(required = true) AuthEquipDTO authEquipDTO) {
        Map<String, Object> rtnMap = new HashMap<>();
        Map<String, Object> paramsMap = new HashMap<>();
        try {

            if (authEquipDTO.getPhone() == null || authEquipDTO.getLocation() == null
                    || authEquipDTO.getCheck() == null) {
                return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_9998));
            }

            paramsMap.put("phone", authEquipDTO.getPhone());
            paramsMap.put("location", authEquipDTO.getLocation());
            paramsMap.put("check", authEquipDTO.getCheck());
            paramsMap.put("fcm_token", authEquipDTO.getToken());
            paramsMap.put("user_type", authEquipDTO.getUser_type());
            paramsMap.put("name", authEquipDTO.getName());
            paramsMap.put("nickname", authEquipDTO.getNickname());
            int result = authService.enrollUser(paramsMap);
            log.info(result);

            if (result <= 0) {
                return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_9999));
            } else {
                paramsMap.put("mb_idx", paramsMap.get("user_idx").toString());
//                if (paramsMap.get("user_type").toString().equals("장비주") || paramsMap.get(
//                        "user_type").toString().equals("노무사")) {
//                    rtnMap.put("fcm_token", authEquipDTO.getToken());
//                    rtnMap.put("user_type", authEquipDTO.getUser_type());
//                    rtnMap.put("mb_idx", paramsMap.get("user_idx").toString());
//                    rtnMap.put("owner_name", authEquipDTO.getOwner_name());
//                    rtnMap.put("equipname", authEquipDTO.getEquipname());
//                    rtnMap.put("owner_phone", authEquipDTO.getOwner_phone());
//                    rtnMap.put("owner_category", authEquipDTO.getOwner_category());
//                    rtnMap.put("owner_address", authEquipDTO.getOwner_address());
//                    String path = "";
//                    if (authEquipDTO.getOwner_image() != null) {
//                        path = s3Util.uploadSingleFile(authEquipDTO.getOwner_image(),
//                                "equipRegi/");
//                        if (rtnMap.get("user_type").equals("노무사")) {
//                            path = s3Util.uploadSingleFile(
//                                    authEquipDTO.getOwner_image(),
//                                    "labor//");
//                        }
//                        log.info(path + "1");
//                    }
//                    //update delete는 행의 개수를 리턴한다 select는 table의정보니까 배열이타입으로
//                    rtnMap.put("owner_image", path);
//                    rtnMap.put("owner_career", authEquipDTO.getOwner_career());
//                    rtnMap.put("equip_spec", authEquipDTO.getEquip_spec());
//                    int commonResult = authService.addSignUp(rtnMap); //장비주
//                    if (commonResult <= 0) {
//                        return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_9999));
//                    }
//
//                }

                String token = authService.generateUserAccessToken(paramsMap);
                paramsMap.put("token", token);
                rtnMap.put("token", token);
                String blockchain = "";

                try {
                    blockchain = authService.createWallet(authEquipDTO.getPhone());
                } catch (Exception e) {
                    e.printStackTrace();
                    log.error("wallet create error :::: ", e.getMessage());
                }
                paramsMap.put("blockchain", blockchain);
                rtnMap.put("blockchain", blockchain);
                int result2 = authService.updateMemberWithBlockchain(paramsMap);

                log.info(result2);
                if (result2 <= 0) {
                    return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_9999));
                } else {
                    return ResponseEntity.ok(
                            new ApiResponse(true, rtnMap, "success", 200));
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_9999));
        }
    }

    @Operation(summary = "닉네임 중복확인")
    @GetMapping("/api/v1/auth/open/nickname/{nickname}")
    public ResponseEntity<?> getNicknameCount(
            HttpServletRequest request,
            @Parameter(name = "nickname", description = "닉네임", in = ParameterIn.PATH)
            @PathVariable String nickname) throws Exception {
        Map<String, Object> rtnMap = new HashMap<String, Object>();
        try {
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("nickname", nickname);
            rtnMap = authService.getNicknameCount(paramMap);

            if (rtnMap == null || rtnMap.isEmpty()) {
                return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_2127));
            }
            return ResponseEntity.ok(new ApiResponse(true, rtnMap, "success", 200));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_500));
        }
    }

    @Operation(summary = "닉네임 설정", description = "닉네임:김모모")
    @PostMapping("/api/v1/auth/nickname")
    public ResponseEntity<?> PostNickName(
            @Schema(
                    description = "nickname",
                    type = "array",
                    example =
                            "{\"nickname\": \"김모모\"}")
            @RequestBody Map<String, Object> paramMap, HttpServletRequest request) {
        Map<String, Object> rtnMap = new HashMap<>();
        try {

            Map<String, Object> jwtMap = jwtTokenProvider.getJwtInfo(request);
            if (null == jwtMap) {
                return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_1104));
            }

//
            Map<String, Object> result1 = authService.selectValidateNickName(paramMap);
            int countNickName = Integer.parseInt(result1.get("nickname").toString());
            log.info(countNickName + "++test");
            if (countNickName > 0) {
                return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_1120));
            } else {

                //ㅇㅣ게 되는거
                rtnMap.put("mb_idx", jwtMap.get("mb_idx").toString());
                rtnMap.put("nickname", paramMap.get("nickname"));

                int result = authService.insertNickName(rtnMap);

                log.info(result);

//            log.info("result",result);
                return ResponseEntity.ok(new ApiResponse(true, "success", 200));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_500));
        }

    }

    @Operation(summary = "유저 닉네임 실명 사진등록", description = "유저 닉네임 실명 사진등록")
    @PostMapping("/api/v1/auth/open/userInfoPhoto")
    public ResponseEntity<?> PostInfoPhoto(
            @Schema(
                    description = "photo",
                    type = "array",
                    example = " {"
                                + "\"nickname\" :\"닉네임\" ,"
                                + "\"user_name\": \"실명\","
                                + "\"user_image\": \"프로필사진(필수X)\","
                                + "\"randNum\": \"아바타번호\""
                             + "}")
            @Parameter(required = true) AuthPhotoDTO authPhotoDTO, HttpServletRequest request) {
        Map<String, Object> rtnMap = new HashMap<>();
        try {

            Map<String, Object> jwtMap = jwtTokenProvider.getJwtInfo(request);
            if (null == jwtMap) {
                return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_1104));
            }

            rtnMap.put("mb_idx", jwtMap.get("mb_idx").toString());
            String path = "";
            if (authPhotoDTO.getUser_image() != null) {
                path = s3Util.uploadSingleFile(authPhotoDTO.getUser_image(), "userImage/");
                log.info(path + "1");
            } else {
                path = "file/userImage/" + authPhotoDTO.getRandNum() + ".png";
            }
            rtnMap.put("nickname", authPhotoDTO.getNickname());
            Map<String, Object> result1 = authService.selectValidateNickName(rtnMap);
            int countNickName = Integer.parseInt(result1.get("nickname").toString());
            if (countNickName > 0) {
                return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_1120));
            } else {
                rtnMap.put("nickname", authPhotoDTO.getNickname());

                rtnMap.put("user_name", authPhotoDTO.getUser_name());
                rtnMap.put("user_image", path);
                rtnMap.put("randNum", authPhotoDTO.getRandNum());

                int result = authService.insertPhoto(rtnMap);
                log.info(result);

                return ResponseEntity.ok(new ApiResponse(true, "success", 200));
            }

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_500));
        }
    }

    @Operation(summary = "카카오 연결")
    @GetMapping("/api/v1/auth/open/kakaoLogin")
    public ResponseEntity<?> kakaoConnect(HttpServletRequest request) throws Exception {
        StringBuffer url = new StringBuffer();

        url.append("https://kauth.kakao.com/oauth/authorize?");
        url.append("client_id=" + kakaoRestKey);
//        url.append("&redirect_uri=http://localhost:3015/account/kakao/1"); // 로컬
        url.append("&redirect_uri=http://web.buildmomo.com/account/kakao/1"); // 실서버
        url.append("&response_type=code");

        return ResponseEntity.ok(new ApiResponse(true, url.toString(), "success", 200));
    }

    @Operation(summary = "카카오 로그인")
//    @GetMapping("/api/v1/auth/open/kakao/callback")
    @PostMapping("/api/v1/auth/open/kakao/callback")
    public ResponseEntity<?> kakaoLogin(
            @Schema(
                    description="code", type="array",
                    example = " {\"code\" :\"\"}")
            @RequestBody Map<String, Object> map,
            HttpServletRequest request) throws Exception {
        try {
            if (map == null) {
                return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_500));
            }
            String token = getKakaoAccessToken(map.get("code").toString());
//            session.setAttribute("access_token", token);
            Map<String, Object> info = getKakaoUserInfo(token);
            // 카카오 비즈니스 등록되면 휴대폰번호 기준으로 회원조회 후 리턴!!!
            Map<String, Object> rtnMap = authService.getInfoByPhone(info);
            if (rtnMap.get("exist").toString() == "true") {
                return ResponseEntity.ok(new ApiResponse(true, rtnMap.get("token").toString(), "success", 200));
            } else {
                if (info.get("phone_number") != null) {
                    rtnMap.put("phone", info.get("phone_number").toString());
                }
                rtnMap.put("nickname", info.get("nickname").toString());
                return ResponseEntity.ok(new ApiResponse(true, rtnMap, "success", 200));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_500));
        }
    }

    private String getKakaoAccessToken(String code) throws Exception {
        // 카카오로 보낼 api
        WebClient webClient =
                WebClient.builder()
                        .baseUrl("https://kauth.kakao.com")
                        .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .build();

        // 카카오 서버에 요청보내기 & 응답
        JSONObject response = webClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path("/oauth/token")
                        .queryParam("grant_type", "authorization_code")
                        .queryParam("client_id", kakaoRestKey)
//                        .queryParam("redirect_uri", "http://localhost:3015/account/kakao/1") // 로컬
                        .queryParam("redirect_uri", "http://web.buildmomo.com/account/kakao/1") // 실서버
                        .queryParam("code", code)
                        .build())
                .retrieve().bodyToMono(JSONObject.class).block();
        return (String) response.get("access_token");
    }

    private Map<String, Object> getKakaoUserInfo(String token) throws Exception {
        try {
            WebClient webClient =
                    WebClient.builder()
                            .baseUrl("https://kapi.kakao.com")
                            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                            .build();

            JSONObject response = webClient.post()
                    .uri(uriBuilder -> uriBuilder.path("/v2/user/me").build())
                    .header("Authorization", "Bearer " + token)
                    .retrieve().bodyToMono(JSONObject.class).block();

            String id = String.valueOf(response.get("id"));
            Map<String, Object> map = (Map<String, Object>) response.get("kakao_account");
            Map<String, Object> profile = (Map<String, Object>) map.get("profile");
            String nickname = profile.get("nickname").toString();

            Map<String, Object> rtnMap = new HashMap<>();
            rtnMap.put("id", id);
            rtnMap.put("nickname", nickname);
            if (map.get("has_phone_number").toString().equals("true")) {
                String phone = map.get("phone_number").toString();
                phone = phone.replaceAll("[+]82 ", "0");
                rtnMap.put("phone_number", phone);
            }

            if (map.get("name") != null) {
                rtnMap.put("name", map.get("name").toString());
            }
            // 카카오 비즈니스 등록하면 휴대폰번호 가져와야함!! ... JY
//            rtnMap.put("phone", profile.get("phone").toString());

            return rtnMap;
        } catch (Exception e) {
            return null;
        }
    }

    @Operation(summary = "네이버 콜백")
    @PostMapping("/api/v1/auth/open/naver/callback")
    public ResponseEntity<?> naverLogin(
            @Schema(
                    description="phone", type="array",
                    example = " {\"phone_number\" :\"\"}")
            @RequestBody Map<String, Object> map,
            HttpServletRequest request) throws Exception {
        try {
            if (map == null) {
                return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_500));
            }
            Map<String, Object> rtnMap = authService.getInfoByPhone(map);
            if (rtnMap.get("exist").toString() == "true") {
                return ResponseEntity.ok(new ApiResponse(true, rtnMap.get("token").toString(), "success", 200));
            } else {
                return ResponseEntity.ok(new ApiResponse(true, rtnMap, "success", 200));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_500));
        }
    }
//    @Operation(summary = "1-2-4 회원 로그인", description = "phone : 로그인비밀번호, code : 인증번호")
//    @PostMapping("/api/v1/loginUser")
//    public ResponseEntity<?> loginUser(
//            @Schema(
//                    description = "phone, code",
//                    type = "array",
//                    example = " {\"phone\" :\"010-1234-1234\" ,"
//                            + "\"code\": \"123456\"}")
//            @RequestParam Map<String, Object> paramMap, HttpServletRequest request) {
//
//        log.debug("=============================START= in " + request.getRequestURL());
//        try {
//            if (paramMap.get("phone") == null || paramMap.get("code") == null) {
//                return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_9998));
//            }
//
//            /*
//                1. phone_authentication의 코드 체크 -> 제일마지막꺼, 회수는 5회까지
//                --- 가입된 사용자
//                ----- user_status에 따른 처리
//                    0   user_idx를 mb_idx로 넣고 jwt토큰으로 변환 후 반환
//                    1   비활성화된 상태
//                    3   탈퇴처리된 유저
//                --- 미가입 사용자
//                회원가입 진행. 데이터 반환할 필요 없음
//                --- 코드가 일치하지 않을 때
//                테이블의 count를 1 올리고 실패처리
//                --- count가 5 이상일 때
//                횟수 초과처리
//
//             */
//
//            //****** login_id 암호화 되기전 redis key 값 설정
//            String redis_key = "MB_" + paramMap.get("phone");
//
//            paramMap.put("phone", paramMap.get("phone"));
//            String code = paramMap.get("code").toString();
//            paramMap.put("code", code);
//            HashMap<String, Object> showParamMap = new HashMap<String, Object>();
//            showParamMap.putAll(paramMap);
////            showParamMap.remove("login_password");
//
//            log.debug("1. paramMap :: " + showParamMap); //
//
//            // 이메일 가입 정보가 없을 경우 멘트 추가
//            Map<String, Object> memberMapByEmail = authService.selectMember(paramMap);
//            if (memberMapByEmail == null) {
//                return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_2002));
//            }
//            paramMap.put("password_salt", memberMapByEmail.get("mb_password_salt"));
//            Map<String, Object> memberMap = authService.selectSignin(paramMap);
//            log.debug("2. memberMap :: " + memberMap);
//
//            //※ 접속정보 저장
////            String ip = NetworkUtil.getClientIP(request);
////            //IPv6 경우는 예외처리한다 ipv6 대응
////            if (ip.indexOf("0:0:0") > -1) {
////                ip = "127.0.0.1";
////            }
////
////            // ::1 대응
////            if ("::1".equals(ip)) {
////                ip = "127.0.0.1";
////            }
////
////            log.debug("======================== Login ip : " + ip);
////            paramMap.put("agent_ip", ip);
//
////            Map<String, Object> locationMap = new HashMap<>();
////            if (!"".equals(ip) && null != ip) {
////                String[] orgAddrArray = ip.trim().split(",");
////                String[] firstAddrArray = orgAddrArray[0].trim().split("\\.");
////
////                long num = 0;
////                for (int i = 0; i < firstAddrArray.length; i++) {
////                    if (firstAddrArray[i].contains(":")) {
////                        firstAddrArray[i] = firstAddrArray[i].substring(0, firstAddrArray[i].indexOf(":"));
////                    }
////                    int power = 3 - i;
////                    num += ((Integer.parseInt(firstAddrArray[i]) % 256 * Math.pow(256, power)));
////                }
////
////                log.debug("strAddr ::" + num);
////                paramMap.put("strAddr", num);
////                locationMap = authService.selectfromIP(paramMap);
////            }
//
//            String agent = request.getHeader("User-Agent");
//            String brower = "api";
//
//            //탈퇴요청 계정 로그인 막기
//            if (memberMapByEmail.get("mb_gubun").toString().equals("4")) {
//                return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_2031));
//            }
//
//            //로그인 금지 아이디 체크
//            if (memberMapByEmail.get("mb_active").toString().equals("3")) {
//
//                /************MB_ACTIVE
//                 0 : 정상        : 로그인0 거래0 출금0,
//                 1 : 출금금지    : 로그인0 거래0 출금x
//                 2 : 출금거래금지: 로그인0 거래x 출금x
//                 3 : 로그인금지  : 로그인x 거래x 출금x ******/
//                Map<String, Object> getRedisMap = redisUtil.getRedisDataMap(redis_key);
//                int count = (int) getRedisMap.get("count");
//                if (getRedisMap.size() != 0 && count >= 5) {
//                    return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_2025));
//                }
//
//                System.out.println("로그인 금지");
//                return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_2019));
//            }
//
//            // ------------ 로그인 횟수 체크 --------------
//            //redis key {"count":5}
//            Map<String, Object> redisInsertMap = new HashMap<>();
//            Map<String, Object> getRedisMap = redisUtil.getRedisDataMap(redis_key);
//
//            int count = 1;
//
//            if (memberMap == null) {
//                paramMap.put("mb_idx", 0);
//                paramMap.put("activity_type", 2); //로그인 실패
//
//                if (getRedisMap.size() == 0) {
//                    redisInsertMap.put("count", count);
//                } else {
//                    count = (int) getRedisMap.get("count");
//                    count += 1;
//                    redisInsertMap.put("count", count);
//                    if (count >= 5) {
//                        redisUtil.setRedisData(redis_key, jsonUtil.getJsonStringFromMap(redisInsertMap).toString());
//                        Map<String, Object> updateActiveMap = new HashMap<>();
//                        updateActiveMap.put("mb_active", 3); //로그인 금지
//                        updateActiveMap.put("act_type", 14);
//                        updateActiveMap.put("memo", "[로그인금지]로그인시도횟수초과");
//                        updateActiveMap.put("mb_id", paramMap.get("mb_id"));
//                        updateActiveMap.put("mb_idx", memberMapByEmail.get("mb_idx"));
//
//                        authService.updateMemberActive(updateActiveMap);
//                        authService.insertMemberDetailHistory(updateActiveMap);
//                        return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_2025));
//                    }
//                }
//            } else {
//                paramMap.put("mb_idx", new Integer(memberMap.get("mb_idx").toString()));
//                paramMap.put("activity_type", 1); //로그인 성공
//                count = 0;
//            }
//            redisInsertMap.put("count", count);
//            try {
//                redisUtil.setRedisData(redis_key, jsonUtil.getJsonStringFromMap(redisInsertMap).toString());
//            } catch (Exception e) {
//            }
//
//            //-----------------------------------------------------------------------------------------
//
//            paramMap.put("mb_id", paramMap.get("phone"));
//
//            if (null != locationMap) {
//                paramMap.put("agent_location", String.valueOf(locationMap.get("cn")));
//            } else {
//                paramMap.put("agent_location", "");
//            }
//            paramMap.put("agent_os", agent);
//            paramMap.put("agent_browser", brower);
//            paramMap.put("agent_ip", ip);
//            //paramMap.put("agent_type", StringUtil.getAgentType(agent));
//            paramMap.put("agent_type", NetworkUtil.getAgentDevice(request));
//            paramMap.put("result", "TRUE");
//
//            log.info("login agent"+agent);
//            log.info("login browser"+brower);
//            log.info("login ip"+ip);
//            log.info("login device"+NetworkUtil.getAgentDevice(request));
//
//            Object ActiveMap = authService.insertActivityMem(paramMap, request);
//            log.debug("ActiveMap ::" + ActiveMap);
//
//            //아이디 비밀번호 오입력체크
//            if (memberMap == null) {
//                return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_2007));
//            }
//
//            log.debug("memberMap :: " + memberMap);
//            int mb_idx = new Integer(memberMap.get("mb_idx").toString());
//            int mb_certify_level = new Integer(memberMap.get("mb_certify_level").toString());
//            int mb_trade_level = new Integer(memberMap.get("mb_trade_level").toString());
//
//            //접속 디바이스 정보확인 및 저장
//            if (null != paramMap.get("agent_os") && null != paramMap.get("agent_token")) {
//                log.debug("paramMap ::" + paramMap);
//                Object tokenMap = authService.insertToken(paramMap);
//                log.debug("tokenMap ::" + tokenMap);
//            }
//
//            Map<String, Object> jwtMap = new HashMap<>();
//            jwtMap.put("mb_idx", mb_idx);
//            jwtMap.put("mb_id", memberMap.get("mb_id"));
//            jwtMap.put("second_login", "N"); // 2차 로그인 유무
//
//
//            //로그인 푸시 유저 알람
//
//            if (memberMap.get("mb_alarm_login").toString().compareTo("Y") == 0) {
//                Map<String, Object> adminAlarmMap = new HashMap<>();
//                adminAlarmMap.put("alarm_code", ALARM_CODE_LOG_IN);
//                adminAlarmMap.put("mb_id", memberMap.get("mb_id"));
//                adminAlarmMap.put("ip", ip);
//                Date nowtime = new Date();
//                SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                String now = transFormat.format(nowtime);
//                adminAlarmMap.put("time", now);
//
//                //Y인것을 mbselect로 가져와서 확인해야 함. dkkim
//                adminAlarmMap.put("mb_alarm_etc", "Y");
//                adminAlarmMap.put("mb_alarm_trade", "Y");
//                adminAlarmMap.put("mb_idx", mb_idx);
//                adminAlarmMap.put("phone_no", "");
//
//
//                String alarmResult = alarmUtil.sendAlarm(adminAlarmMap);
//            }
//
//
//            //push_dfians_list 저장 - 로그인 알림
//            Map<String, Object> dfPushMap = new HashMap<>();
////				idx, category, reg_date, notify_type, notify_content, mb_idx
//            dfPushMap.put("category","login");
//            dfPushMap.put("notify_type","login");
//            dfPushMap.put("notify_content", "Login notification (Login IP:"+ip+")");
//            dfPushMap.put("mb_idx",mb_idx);
//
//            authService.insertDfPush(dfPushMap);
//
//            ApiResponse apiResponse = authService.getLoginToken(request, jwtMap);
//            return ResponseEntity.ok(apiResponse);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_9999));
//        }
//    }
//
//
//
//    private String getTokenFromRequest(HttpServletRequest request) {
//        String bearerToken = request.getHeader("authorization");
//
//        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
//            return bearerToken.substring(7, bearerToken.length());
//        }
//        return null;
//    }

}
