package kr.co.dk.sample.api.app.job.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.dk.sample.api.app.board.controller.BoardController;
import kr.co.dk.sample.api.app.job.dto.OfferDTO;
import kr.co.dk.sample.api.app.job.dto.SearchDTO;
import kr.co.dk.sample.api.app.job.service.JobService;
import kr.co.dk.sample.api.common.model.ApiResponse;
import kr.co.dk.sample.api.common.model.ErrorCode;
import kr.co.dk.sample.api.common.model.Paging;
import kr.co.dk.sample.api.common.push.PushService;
import kr.co.dk.sample.api.common.util.S3Util;
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

@Tag(name = "인력(구인/구직)")
@RestController
public class JobController {

    private static final Logger log = LogManager.getLogger(BoardController.class);

    @Autowired
    JobService jobService;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @Autowired
    S3Util s3Util;

    @Autowired
    PushService pushService;

    @Operation(summary = "구인/구직 목록 조회")
    @GetMapping("/api/v1/job/open/{category}/{subCategory}/{type}/{pageNo}")
    public ResponseEntity<?> getJobList(HttpServletRequest request,
                                        @Parameter(name = "category", description = "지역", in = ParameterIn.PATH)
                                        @PathVariable String category,
                                        @Parameter(name = "subCategory", description = "기술 구분", in = ParameterIn.PATH)
                                        @PathVariable String subCategory,
                                        @Parameter(name = "type", description = "구인/구직 분류(0, 1)", in = ParameterIn.PATH)
                                        @PathVariable String type,
                                        @Parameter(name = "pageNo", description = "페이지 번호", in = ParameterIn.PATH)
                                        @PathVariable String pageNo) throws Exception {
        Map<String, Object> rtnMap = new HashMap<String, Object>();
        try {

            if (category == null || subCategory == null || type == null || pageNo == null) {
                return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_9002));
            }

            Map<String, Object> paramMap = new HashMap<>();
            Map<String, Object> jwtMap = jwtTokenProvider.getJwtInfo(request); // mb_idx 추출 -> 찜하기 목록 조회용
            if (null == jwtMap) {
                paramMap.put("mb_idx", "0"); // 비회원일경우 찜하기가 안된 목록 조회되야함 -> 빈회원값인 0번 기입
            } else {
                paramMap.put("mb_idx", jwtMap.get("mb_idx"));
            }
            /*
                카테고리
                all = 전체
                worker = 근로자
                watchman = 왓치맨
                safetyCenter = 안전시설
                signal = 신호수
                fire = 화기감시자
                safetyMan = 안전관리자
                medical = 보건관리자
                ------
                수정됨, 아마 사용 안할듯 합니다
             */
//            switch(category.toString()) {
//                case "worker": paramMap.put("category", "근로자"); break;
//                case "watchman": paramMap.put("category", "왓치맨"); break;
//                case "safetyCenter": paramMap.put("category", "안전시설"); break;
//                case "signal": paramMap.put("category", "신호수"); break;
//                case "fire": paramMap.put("category", "화기감시자"); break;
//                case "safetyMan": paramMap.put("category", "안전관리자"); break;
//                case "medical": paramMap.put("category", "보건관리자"); break;
//                default: paramMap.put("category", category.toString()); break;
//            }
            paramMap.put("category", category);
            paramMap.put("subCategory", subCategory);
            paramMap.put("type", type);
            paramMap.put("pageNo", pageNo);
            Paging pagingBoard = jobService.setJobPaging(paramMap);
            List<Map> jobList = jobService.selectJobList(paramMap);

            if (jobList == null || jobList.isEmpty()) {
                return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_2121));
            }

            rtnMap.put("result", jobList);
            rtnMap.put("paging", pagingBoard);
            return ResponseEntity.ok(new ApiResponse(true, rtnMap, "success", 200));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_500));
        }
    }

    @Operation(summary = "공유하기 work 상세")
    @GetMapping("/api/v1/job/open/share/{workIdx}")
    public ResponseEntity<?> getWorkShare(HttpServletRequest request,
                                          @Parameter(name = "workIdx", description = "idx", in = ParameterIn.PATH)
                                          @PathVariable String workIdx
    ) throws Exception {
        Map<String, Object> rtnMap = new HashMap<String, Object>();
        try {
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("workIdx", workIdx);
            List<Map> shareList = jobService.getWorkShare(paramMap);

            if (shareList == null || shareList.isEmpty()) {
                return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_2127));
            }
            rtnMap.put("result", shareList);
            return ResponseEntity.ok(new ApiResponse(true, rtnMap, "success", 200));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_500));
        }
    }

    @Operation(summary = "구인 등록")
    @PostMapping("/api/v1/job/add/offer")
    public ResponseEntity<?> insertJobOffer(HttpServletRequest request,
                                            @Parameter(required = true) OfferDTO offerDTO) throws Exception {
        try {
            Map<String, Object> jwtMap = jwtTokenProvider.getJwtInfo(request);
            if (null == jwtMap) {
                return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_1104));
            }
//            if (idx == null) {
//                return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_9002));
//            }
            Map<String, Object> paramMap = new HashMap<>();
//            offerDTO.getPayment()
//            paramMap.put("user_idx", jwtMap.get("cpn_idx"));
            paramMap.put("mb_idx", jwtMap.get("mb_idx").toString());

            paramMap.put("offerDTO", offerDTO);
            String path = "";
            if (offerDTO.getJobImage() != null) {
                path = s3Util.uploadSingleFile(offerDTO.getJobImage(), "job/");
                log.info(path + "1");
            }
            paramMap.put("jobImage", path);

            log.info(path + "2");

            int result = jobService.insertJobOffer(paramMap);

            if (result <= 0) {
                return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_9989));
            }

            return ResponseEntity.ok(new ApiResponse(true, "success", 200));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_500));
        }
    }

    @Operation(summary = "구직 등록")
    @PostMapping("/api/v1/job/add/search")
    public ResponseEntity<?> insertJobSearch(HttpServletRequest request,
                                             @Parameter(required = true) @RequestBody SearchDTO searchDTO) throws Exception {
        try {
            Map<String, Object> jwtMap = jwtTokenProvider.getJwtInfo(request);
            if (null == jwtMap) {
                return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_1104));
            }

            Map<String, Object> paramMap = new HashMap<>();
//            paramMap.put("user_idx", jwtMap.get("cpn_idx"));
            paramMap.put("mb_idx", jwtMap.get("mb_idx").toString());
            paramMap.put("searchDTO", searchDTO);

            int result = jobService.insertJobSearch(paramMap);

            if (result <= 0) {
                return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_9989));
            }

            return ResponseEntity.ok(new ApiResponse(true, "success", 200));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_500));
        }
    }

    @Operation(summary = "내조건으로찾기 가입했던 지역과 업종으로 찾기")
    @GetMapping("/api/v1/job/all/selectList")
    public ResponseEntity<?> getSelectMyLocationService(HttpServletRequest request,
                                                        @Parameter(required = true) @RequestBody SearchDTO searchDTO) throws Exception {
        try {
            Map<String, Object> jwtMap = jwtTokenProvider.getJwtInfo(request);
            if (null == jwtMap) {
                return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_1104));
            }

            Map<String, Object> paramMap = new HashMap<>();
//            paramMap.put("user_idx", jwtMap.get("cpn_idx"));
            paramMap.put("mb_idx", jwtMap.get("mb_idx").toString());
            paramMap.put("searchDTO", searchDTO);

            int result = jobService.insertJobSearch(paramMap);

            if (result <= 0) {
                return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_9989));
            }

            return ResponseEntity.ok(new ApiResponse(true, "success", 200));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_500));
        }
    }

    @Operation(summary = "구인/구직 신청")
    @PostMapping("/api/v1/job/request")
    public ResponseEntity<?> insertWorkHistory(
            @Schema(
                description = "workIdx",
                type = "array",
                example = "{" +
                                "\"workIdx\": \"1\"," +
                                "\"userIdx\": \"1\"" +
                          "}")
            @RequestBody Map<String, Object> paramMap, HttpServletRequest request
    ) throws Exception {
        try {
            Map<String, Object> jwtMap = jwtTokenProvider.getJwtInfo(request);
            if (null == jwtMap) {
                return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_1104));
            }
            if (paramMap == null) {
                return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_9002));
            }
            int idx = Integer.parseInt(String.valueOf(jwtMap.get("mb_idx")));
            int idx2 = Integer.parseInt(String.valueOf(paramMap.get("userIdx")));

            if (idx == idx2) {
                return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_1513));
            }
            paramMap.put("mb_idx", jwtMap.get("mb_idx").toString());

//            int result = jobService.insertWorkHistory(paramMap); // work_history 테이블 기입 -> 1:1채팅에서 제안하기로 이동
//            int result2 = jobService.inertWork(paramMap); // work_status 비활성으로 변경 -> 개인이 수정하게끔 변경됨
            int result3 = 1; // 채팅방이 있다는 가정

            // 채팅방 확인
            Map<String, Object> chatroom = jobService.checkChatroom(paramMap);
            String chatroomIdx = "";
            if (Integer.parseInt(String.valueOf(chatroom.get("cnt"))) < 1) {
                // 채팅방 생성
                result3 = jobService.insertChatroom(paramMap);
                if (result3 > 0) {
                    chatroomIdx = paramMap.get("chatroom_idx").toString();
                }
            } else {
                // 채팅방 idx 조회
                chatroom = jobService.getChatroom(paramMap);
                chatroomIdx = chatroom.get("chatroom_idx").toString();
            }

//            if (result * result2 * result3 <= 0) {
            if (result3 <= 0) {
                return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_9989));
            }
            // 푸시발송 추가할것
            pushService.sendMessage(idx + "," + idx2, "건설모모 일자리 신청", "채팅을 확인해주세요.");

            return ResponseEntity.ok(new ApiResponse(true, chatroomIdx, "success", 200));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_500));
        }
    }

    @Operation(summary = "구인/구직 제안")
    @PostMapping("/api/v1/job/suggest")
    public ResponseEntity<?> suggestWork(
            @Schema(
                    description = "workIdx",
                    type = "array",
                    example = "{" +
                            "\"workIdx\": \"1\"," +
                            "\"userIdx\": \"1\"" +
                            "}")
            @RequestBody Map<String, Object> paramMap, HttpServletRequest request
    ) throws Exception {
        try {
            Map<String, Object> jwtMap = jwtTokenProvider.getJwtInfo(request);
            if (null == jwtMap) {
                return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_1104));
            }
            if (paramMap == null) {
                return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_9002));
            }
            int idx = Integer.parseInt(String.valueOf(jwtMap.get("mb_idx")));
            int idx2 = Integer.parseInt(String.valueOf(paramMap.get("userIdx")));

            if (idx == idx2) {
                return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_1513));
            }
            paramMap.put("mb_idx", jwtMap.get("mb_idx").toString());

            int result = jobService.insertWorkHistory(paramMap);

            if (result <= 0) {
                return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_9989));
            }

            return ResponseEntity.ok(new ApiResponse(true, "success", 200));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_500));
        }
    }

    @Operation(summary = "일자리 조회수 증가")
    @PostMapping("/api/v1/job/open/increase")
    public ResponseEntity<?> increaseWorkCount(@Schema(
            description = "workIdx",
            type = "array",
            example = " {\"workIdx\" :\"1\"}")
                                               @RequestBody Map<String, Object> paramMap, HttpServletRequest request) throws Exception {
        try {
            if (paramMap == null) {
                return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_9002));
            }

            int result = jobService.increaseWorkCount(paramMap);

            if (result <= 0) {
                return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_9989));
            }

            return ResponseEntity.ok(new ApiResponse(true, "success", 200));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_500));
        }
    }

    @Operation(summary = "일자리 예약상태 변환")
    @PostMapping("/api/v1/job/inert")
    public ResponseEntity<?> inertWork(@Schema(
            description = "workIdx",
            type = "array",
            example = " {\"workIdx\" :\"1\"}")
                                       @RequestBody Map<String, Object> paramMap, HttpServletRequest request) throws Exception {
        try {
            Map<String, Object> jwtMap = jwtTokenProvider.getJwtInfo(request);
            if (null == jwtMap) {
                return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_1104));
            }
            if (paramMap == null) {
                return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_9002));
            }

            int result = jobService.inertWork(paramMap);

            if (result <= 0) {
                return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_9989));
            }

            return ResponseEntity.ok(new ApiResponse(true, "success", 200));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_500));
        }
    }

    @Operation(summary = "업무제안 목록 조회")
    @GetMapping("/api/v1/job/activeList")
    public ResponseEntity<?> activeJobList(HttpServletRequest request) throws Exception {
        try {
            Map<String, Object> jwtMap = jwtTokenProvider.getJwtInfo(request);
            if (null == jwtMap) {
                return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_1104));
            }

            List<Map> rtnMap = jobService.activeJobList(jwtMap);

            if (rtnMap == null) {
                return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_9988));
            }

            return ResponseEntity.ok(new ApiResponse(true, rtnMap, "success", 200));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_500));
        }
    }

}
