package kr.co.dk.sample.api.app.company.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.dk.sample.api.app.company.dto.RatingDTO;
import kr.co.dk.sample.api.app.company.service.CompanyService;
import kr.co.dk.sample.api.common.model.ApiResponse;
import kr.co.dk.sample.api.common.model.ErrorCode;
import kr.co.dk.sample.api.config.security.JwtTokenProvider;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


//정훈
@Tag(name = "건설사")       // 스웨거 api 큰 틀 제목
@RestController          //여기 구문 은(파일은)restcontroller 이다라는걸 표현
public class CompanyController {

    private static final Logger log = LogManager.getLogger(CompanyController.class);  //company controller안에서 로그를찍어줄수있게해주는 것

    @Autowired                      ///의존성주입 생성자 autowired쓰면서 컴포넌트 느낌  companyservice 에있는 메소드 가져올수있다
    CompanyService companyService;

    @Autowired                  //// 의존성 주입 jwttoken을 언제든 재사용하기위해서
    JwtTokenProvider jwtTokenProvider;

    @Operation(summary = "건설사 목록", description = "") // swagger에있는 설명
    @GetMapping("/api/v1/company/open/selectAllCompany") // api 주소
    public ResponseEntity<?> selectAllCompany(HttpServletRequest request) throws Exception {  // throwException try부분에 exception객체를 생성해서 throw로 강제로 예외처리해주는부분
        //로그 시작 알림
        Map<String, Object> rtnMap = new HashMap<String, Object>();  //Map 타입은 string이며 object로 하겠다
        //맵은 entry객체를 저장하는구조를 갖고있고 key,value로 저장한다 값은 중복될수있지만 key는 중복될수없다 .
        try {
//            Map<String, Object> jwtMap = jwtTokenProvider.getJwtInfo(request);
//            if (null == jwtMap) {
//                return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_1104));
//            }
            List<Map> companyList = companyService.selectAllCompany();

            //리스트형태인 컴패니리스트는 서비스에 selectAllcompany에 있는구문을 사용한다
//만약에 없으면 에러코드
            if (companyList == null) {
                return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_2101));
            }
//앞서선언한 Map안에  Map안에 try구문에서 서비스구문 메소드를 가져온것을 rtnMap을 넣어서
            //get요청시 보낸다
            rtnMap.put("company", companyList);
            return ResponseEntity.ok(new ApiResponse(true, rtnMap, "success", 200));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_500));
        }
    }

    //정훈
    @Operation(summary = "건설사 상세 정보", description = "")
    @GetMapping(path = "/api/v1/company/open/selectCompanyInfo/{idx}")
    //디테일관련해서 해당 idx를 넣었을때 프론트에서 그부분만 보여주기위해서
    public ResponseEntity<?> selectCompanyInfo(HttpServletRequest request, @PathVariable(name = "idx") String idx) throws Exception {
        //리스폰스엔티티는 httpEntity를 상속받고있어서 httpHeader와 body를 가질수있다 .
        //httpServletRequest servlet은 클라이언트의 요청을 처리하고 겱과를 ㄱ응답해준다
        //@pathVariable 은 url 경로에 변수를 넣어주기위해서 뒤에 String idx는 타입을명시
        Map<String, Object> rtnMap = new HashMap<String, Object>();
        //결과값을 보내기 위해서 try구문 밖에서 선언
//        Object obj = new Object();
        try {
//            Map<String, Object> jwtMap = jwtTokenProvider.getJwtInfo(request);
//            if (null == jwtMap) {
//                return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_1104));
//            }
            if (idx == null) {
                //idx가 null일경우 예외처리
                return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_9002));
            }
            Map<String, Object> paramMap = new HashMap<>();

//            paramMap.put("cpn_idx", jwtMap.get("cpn_idx"));
            paramMap.put("cpn_idx", idx);
            //paramMap 에 idx를 넣음


            // 건설사 기본정보, 종합순위는 추후 추가해야함.
            Map<String, Object> companyInfo = companyService.selectCompanyInfo(paramMap);

            if (companyInfo == null || companyInfo.isEmpty()) {
                return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_2101));
            }

            rtnMap.put("companyInfo", companyInfo);

            // 현장리스트
            List<Map> siteList = companyService.selectCompanySiteList(paramMap);
            //리스트로 한뭉텅이로 저장되어잇는걸
            //맵은 키밸류형식으로 하니깐 rtnMap에 현장리스트인 list뽑아놓은걸 넣어놓음
            //아래구문 도 이와같다 .,
            rtnMap.put("siteList", siteList);

            // 세부가평가
            Map<String, Object> evaluate = companyService.selectCompanyEvaluate(paramMap);

            rtnMap.put("evaluate", evaluate);

            // 현장평가 리스트
            List<Map> ratingList = companyService.selectCompanyRatingList(paramMap);

            rtnMap.put("ratingList", ratingList);

            return ResponseEntity.ok(new ApiResponse(true, rtnMap, "success", 200));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_500));
        }
    }

    @Operation(summary = "현장 리스트")
    @GetMapping(path = "/api/v1/company/open/getSiteList/{companyIdx}")
    public ResponseEntity<?> getSiteList(HttpServletRequest request, @PathVariable(name = "companyIdx") String companyIdx) throws Exception {
        Map<String, Object> rtnMap = new HashMap<String, Object>();
        try {
//            Map<String, Object> jwtMap = jwtTokenProvider.getJwtInfo(request);
//            if (null == jwtMap) {
//                return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_1104));
//            }
            if (companyIdx == null) {
                return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_9002));
            }
            Map<String, Object> paramMap = new HashMap<>();
//            paramMap.put("cpn_idx", jwtMap.get("cpn_idx"));
            paramMap.put("cpn_idx", companyIdx);
            // 현장 리스트
            List<Map> siteList = companyService.selectCompanySiteList(paramMap);

            if (siteList == null || siteList.isEmpty()) {
                return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_2102));
            }

            rtnMap.put("siteList", siteList);

            return ResponseEntity.ok(new ApiResponse(true, rtnMap, "success", 200));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_500));
        }
    }

    @Operation(summary = "현장 상세정보")
    @GetMapping(path = "/api/v1/company/open/selectSiteDetail/{siteIdx}")
    public ResponseEntity<?> selectSiteDetail(HttpServletRequest request, @PathVariable(name = "siteIdx") String siteIdx) throws Exception {
        Map<String, Object> rtnMap = new HashMap<String, Object>();
        try {
//            Map<String, Object> jwtMap = jwtTokenProvider.getJwtInfo(request);
//            if (null == jwtMap) {
//                return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_1104));
//            }
            if (siteIdx == null) {
                return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_9002));
            }
            Map<String, Object> paramMap = new HashMap<>();
//            paramMap.put("cpn_idx", jwtMap.get("cpn_idx"));
            paramMap.put("site_idx", siteIdx);
            // 현장 상세정보
            Map<String, Object> siteInfo = companyService.selectSiteInfo(paramMap);

            if (siteInfo == null || siteInfo.isEmpty()) {
                return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_2102));
            }

            rtnMap.put("siteInfo", siteInfo);

            // 현장 평가 리스트
            List<Map> ratingList = companyService.selectSiteRatingList(paramMap);

            rtnMap.put("ratingList", ratingList);

            return ResponseEntity.ok(new ApiResponse(true, rtnMap, "success", 200));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_500));
        }
    }

    @Operation(summary = "현장 평가 작성")

    @PostMapping(path = "/api/v1/company/site")
    public ResponseEntity<?> insertSiteRating(HttpServletRequest request, @Parameter(required = true) @RequestBody RatingDTO ratingDTO) throws Exception {
        try {
            Map<String, Object> jwtMap = jwtTokenProvider.getJwtInfo(request);
            //jwtMap에  post요청시 토큰값을가져온걸확인하기위해서 ?

            if (null == jwtMap) {
                return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_1104));
            }
            if (ratingDTO == null) {
                return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_9002));
            }
//바디 값을 모델링 한부분을 넣는다
            jwtMap.put("ratingDTO", ratingDTO);

            log.info("----- jwtMap -----" + jwtMap);

            // 건설사 idx 가져옴
            Map<String, Object> idxMap = companyService.selectCompanyIdx(jwtMap);
            //토큰값도 같이보냄
            log.info(idxMap);

            if (idxMap == null || idxMap.isEmpty()) {
                return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_2103));
            } else {
                jwtMap.put("company_idx", idxMap.get("company_idx").toString());
            }


            // 평가 기입
            int result = companyService.insertSiteRating(jwtMap);
//컴패니서비스에 insert update delete는  행의 개수를 반환하기때문에 int 를 넣음
            if (result <= 0) {
                return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_9989));
            }

            return ResponseEntity.ok(new ApiResponse(true, "success", 200));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_500));
        }

    }

    @Operation(summary = "실시간 평가 통계")
    @GetMapping(path = "/api/v1/company/open/statistics/{category}")
    public ResponseEntity<?> selectCompanyStatistics(HttpServletRequest request, @PathVariable(name = "category") String category) throws Exception {
        Map<String, Object> rtnMap = new HashMap<String, Object>();
        try {
//            Map<String, Object> jwtMap = jwtTokenProvider.getJwtInfo(request);
//            if (null == jwtMap) {
//                return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_1104));
//            }
            if (category == null) {
                return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_9002));
            }
            Map<String, Object> paramMap = new HashMap<>();
//            paramMap.put("cpn_idx", jwtMap.get("cpn_idx"));
            paramMap.put("category", category);
            // 현장 상세정보
            List<Map> companyList = companyService.selectCompanyStatistics(paramMap);

            if (companyList == null || companyList.isEmpty()) {
                return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_2103));
            }

            rtnMap.put("siteInfo", companyList);

            return ResponseEntity.ok(new ApiResponse(true, rtnMap, "success", 200));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_500));
        }
    }
}
