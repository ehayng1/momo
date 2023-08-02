package kr.co.dk.sample.api.common.temp;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.dk.sample.api.common.dynamiclink.dto.DynamicLinkResponse;
import kr.co.dk.sample.api.common.dynamiclink.service.DynamicLinkService;
import kr.co.dk.sample.api.common.file.service.FileService;
import kr.co.dk.sample.api.common.model.ErrorCode;
import kr.co.dk.sample.api.common.model.enums.type.LinkType;
import kr.co.dk.sample.api.common.push.PushService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Tag(name = "A-TestController", description = "테스트용 상용배포 하기전에 지울꺼임")
@RestController
public class TestController {
    private static final Logger log = LogManager.getLogger(TestController.class);
    private static final String JWT_TYPE = "jwtType";
    private static final String REFRESH_TYPE = "refresh";

//    @Autowired
//    private UserMapper userRepository;
//
//    @Autowired
//    private AuthorityRepository authorityRepository;
    private static final String ACCESS_TYPE = "access";
    private static final String USER_INDEX = "userIdx";
    @Value("${jwt.secret}")
    private String jwtSecret;
    @Autowired
    private FileService fileService;
    @Autowired
    private PushService pushService;
    @Autowired
    private DynamicLinkService dynamicLinkService;

    @Operation(summary = "다이 나믹 링크 생성하기")
    @GetMapping("/api/v1/test/dynamicLink")
    public ResponseEntity pushTest(String idx, LinkType type) {
        DynamicLinkResponse dynamicLinkResponse = dynamicLinkService.getDynamicLink(idx, type);
        return ResponseEntity.ok(dynamicLinkResponse);
    }

    @Operation(summary = "푸시 테스트")
    @GetMapping("/api/v1/test/push")
    public ResponseEntity pushTest(String token, String body, String title) {
        HashMap<String, String> data = new HashMap<>();
        data.put("teste", "zz");
        boolean success = pushService.sendMessage(token, title, body, data);
        return ResponseEntity.ok(success);
    }

    @Operation(description = "존재하는 회원 idx만 넣으세여", summary = "6시간 짜리 토큰 생성")
    @PostMapping("/api/v1/test/AccessToken")
    public ResponseEntity getAccessToken6hours(@RequestParam(defaultValue = "1") String userIdx) {

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + (6 * 1000 * 60 * 60));
        HashMap claims = new HashMap<>();
        claims.put(JWT_TYPE, ACCESS_TYPE);
        claims.put(USER_INDEX, userIdx);

        String accessToken = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
        return ResponseEntity.ok(accessToken);
    }

    @Operation(summary = "에러 코드 리스트", description = "전체 에러 코드")
    @GetMapping("/api/v1/test/error-code")
    public ResponseEntity getErrorCodes() {
        List<ErrorCode> errorCodes = new ArrayList<>();
        Arrays.stream(ErrorCode.values()).forEach(errorCode -> errorCodes.add(errorCode));
        return ResponseEntity.ok(errorCodes);
    }

    @Operation(summary = "에러 코드 검색", description = "에러 코드 입력")
    @GetMapping("/api/v1/test/error-code/{errorCode}")
    public ResponseEntity getErrorCode(@PathVariable int errorCode) {
        return ResponseEntity.ok(ErrorCode.getErrorCodeByCode(errorCode));
    }

//    @Operation(summary = "유저")
//    @GetMapping("/api/v1/test/user")
//    public ResponseEntity getUsers(){
//        return ResponseEntity.ok(userRepository.findAll());
//    }


}
