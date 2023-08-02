package kr.co.dk.sample.api.app.mypage.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.dk.sample.api.app.mypage.dto.MypageDTO;
import kr.co.dk.sample.api.app.mypage.service.MypageService;
import kr.co.dk.sample.api.common.model.ApiResponse;
import kr.co.dk.sample.api.common.model.ErrorCode;
import kr.co.dk.sample.api.common.model.Paging;
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

@Tag(name = "마이페이지")
@RestController
public class MypageController {
    private static final Logger log = LogManager.getLogger(MypageController.class);

    @Autowired
    MypageService mypageService;
    @Autowired
    S3Util s3Util;
    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @Operation(summary = "내정보 가져오기")
    @GetMapping("/api/v1/mypage/info")
    public ResponseEntity<?> getInfo(HttpServletRequest request) throws Exception {
        Map<String, Object> rtnMap = new HashMap<String, Object>();
        try {
            Map<String, Object> jwtMap = jwtTokenProvider.getJwtInfo(request);
            if (null == jwtMap) {
                return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_1104));
            }
            Map<String, Object> result = mypageService.getInfo(jwtMap);

            if (result == null) {
                return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_2107));
            }
            Object path = result.get("user_image");
            String fileUrl = s3Util.fileUrl((String) path);

            result.put("user_image", fileUrl);
            rtnMap.put("result", result);
//            rtnMap.put("result",fileUrl);
            return ResponseEntity.ok(new ApiResponse(true, rtnMap, "success", 200));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_500));
        }
    }

    @Operation(summary = "업무이력")
    @GetMapping("/api/v1/mypage/workHistory/{pageNo}")
    public ResponseEntity<?> workHistory(HttpServletRequest request,
                                         @Parameter(name = "pageNo", description = "페이지 번호", in = ParameterIn.PATH)
                                         @PathVariable String pageNo) throws Exception {
        Map<String, Object> rtnMap = new HashMap<String, Object>();
        try {
            Map<String, Object> jwtMap = jwtTokenProvider.getJwtInfo(request);
            if (null == jwtMap) {
                return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_1104));
            }
            if (pageNo == null) {
                return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_9002));
            }
            jwtMap.put("pageNo", pageNo);
            Paging paging = mypageService.setWorkPaging(jwtMap);
            List<Map> result = mypageService.workHistory(jwtMap);
            if (result == null) {
                return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_2107));
            }

            rtnMap.put("result", result);
            rtnMap.put("paging", paging);
            return ResponseEntity.ok(new ApiResponse(true, rtnMap, "success", 200));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_500));
        }
    }

    @Operation(summary = "나의 신청내역")
    @GetMapping("/api/v1/mypage/myRequest/{type}/{pageNo}")
    public ResponseEntity<?> myRequest(HttpServletRequest request,
                                       @Parameter(name = "type", description = "0 구인, 1 구직, 2 임대", in = ParameterIn.PATH)
                                       @PathVariable String type,
                                       @Parameter(name = "pageNo", description = "페이지 번호", in = ParameterIn.PATH)
                                       @PathVariable String pageNo) throws Exception {
        Map<String, Object> rtnMap = new HashMap<String, Object>();
        try {
            Map<String, Object> jwtMap = jwtTokenProvider.getJwtInfo(request);
            if (null == jwtMap) {
                return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_1104));
            }
            if (type == null || type.equals("") || pageNo == null) {
                return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_9002));
            }
            jwtMap.put("type", type);
            jwtMap.put("pageNo", pageNo);
            Paging paging = mypageService.setMyRequestPaging(jwtMap);
            List<Map> result = mypageService.myRequest(jwtMap);
            if (result == null) {
                return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_2107));
            }

            rtnMap.put("result", result);
            rtnMap.put("paging", paging);
            return ResponseEntity.ok(new ApiResponse(true, rtnMap, "success", 200));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_500));
        }
    }

    @Operation(summary = "다시 올리기 장비 사진목록 조회")
    @GetMapping("api/v1/mypage/getFiles/{idx}")
    public ResponseEntity<?> getFiles(HttpServletRequest request,
                                    @Parameter(name = "idx", description = "페이지 번호", in = ParameterIn.PATH)
                                    @PathVariable String idx) throws Exception {
        Map<String, Object> rtnMap = new HashMap<String, Object>();
        try {
            Map<String, Object> jwtMap = jwtTokenProvider.getJwtInfo(request);
            if (null == jwtMap) {
                return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_1104));
            }
            if (idx == null) {
                return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_9002));
            }
            jwtMap.put("idx", idx);
            List<Map> result = mypageService.getFiles(jwtMap);
            if (result == null) {
                return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_2107));
            }

            rtnMap.put("result", result);
            return ResponseEntity.ok(new ApiResponse(true, rtnMap, "success", 200));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_500));
        }
    }

    @Operation(summary = "찜한 목록 조회")
    @GetMapping("/api/v1/mypage/like/{type}/{pageNo}")
    public ResponseEntity<?> likeHistory(HttpServletRequest request,
                                       @Parameter(name = "type", description = "0 사람, 1 일자리, 2 장비", in = ParameterIn.PATH)
                                       @PathVariable String type,
                                       @Parameter(name = "pageNo", description = "페이지 번호", in = ParameterIn.PATH)
                                       @PathVariable String pageNo) throws Exception {
        Map<String, Object> rtnMap = new HashMap<String, Object>();
        try {
            Map<String, Object> jwtMap = jwtTokenProvider.getJwtInfo(request);
            if (null == jwtMap) {
                return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_1104));
            }
            if (type == null || type.equals("") || pageNo == null) {
                return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_9002));
            }
            jwtMap.put("type", type);
            jwtMap.put("pageNo", pageNo);
            Paging paging = mypageService.setLikePaging(jwtMap);
            List<Map> result = mypageService.likeHistory(jwtMap);
            if (result == null) {
                return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_2107));
            }

            rtnMap.put("result", result);
            rtnMap.put("paging", paging);
            return ResponseEntity.ok(new ApiResponse(true, rtnMap, "success", 200));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_500));
        }
    }

    @Operation(summary = "사진변경", description = "프로필사진변경")
    @PostMapping("/api/v1/mypage/myprofilephoto")
    public ResponseEntity<?> PostInfoPhoto(
            @Schema(
                    description = "photo",
                    type = "array",
                    example = "{\"user_image\": \"\"}"
            )
            @Parameter(required = true) MypageDTO mypageDTO, HttpServletRequest request) {
        Map<String, Object> rtnMap = new HashMap<>();
        try {

            Map<String, Object> jwtMap = jwtTokenProvider.getJwtInfo(request);
            if (null == jwtMap) {
                return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_1104));
            }

            rtnMap.put("mb_idx", jwtMap.get("mb_idx").toString());
            String path = "";
            if (mypageDTO.getUser_image() != null) {
                path = s3Util.uploadSingleFile(mypageDTO.getUser_image(), "userImage/");
                log.info(path + "1");
            }
            rtnMap.put("user_image", path);
            int result = mypageService.changeInsertPhoto(rtnMap);
            if (mypageDTO.getUser_image() == null) {
                return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_1121));

            }

            log.info("result", result);
            return ResponseEntity.ok(new ApiResponse(true, "success", 200));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_500));
        }
    }

    @Operation(summary = "신청내역 구인구직 활성/비활성/삭제", description = "type: 0 구인, 1 구직, 2 임대, action: 0 활성, 1 비활성, 3 삭제, idx: 인덱스  ")
    @PutMapping("/api/v1/mypage/myRequest")
    public ResponseEntity<?> myRequestUpdate(
            @Schema(
                    description = "type, action, idx",
                    type = "array",
                    example = " {\"type\" :\"0\" ,"
                            + "\"action\" :\"0\" ,"
                            + "\"idx\": \"0\"}")
            @RequestBody Map<String, Object> paramMap, HttpServletRequest request) throws Exception {
        Map<String, Object> rtnMap = new HashMap<String, Object>();
        try {
            Map<String, Object> jwtMap = jwtTokenProvider.getJwtInfo(request);
            if (null == jwtMap) {
                return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_1104));
            }
            if (paramMap.get("type") == null || paramMap.get("action") == null || paramMap.get("idx") == null) {
                return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_9998));
            }
            paramMap.put("mb_idx", jwtMap.get("mb_idx").toString());
            int result = mypageService.myRequestUpdate(paramMap);
            if (result > 0) {
                return ResponseEntity.ok(new ApiResponse(true, rtnMap, "success", 200));
            } else {
                return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_2110));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_500));
        }
    }

    @Operation(summary = "커뮤니티 작성글 내역")
    @GetMapping("/api/v1/mypage/issueHistory/{pageNo}")
    public ResponseEntity<?> issueHistory(HttpServletRequest request,
                                         @Parameter(name = "pageNo", description = "페이지 번호", in = ParameterIn.PATH)
                                         @PathVariable String pageNo) throws Exception {
        Map<String, Object> rtnMap = new HashMap<String, Object>();
        try {
            Map<String, Object> jwtMap = jwtTokenProvider.getJwtInfo(request);
            if (null == jwtMap) {
                return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_1104));
            }
            if (pageNo == null) {
                return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_9002));
            }
            jwtMap.put("pageNo", pageNo);
            Paging paging = mypageService.setIssuePaging(jwtMap);
            List<Map> result = mypageService.issueHistory(jwtMap);
            if (result == null) {
                return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_2107));
            }

            rtnMap.put("result", result);
            rtnMap.put("paging", paging);
            return ResponseEntity.ok(new ApiResponse(true, rtnMap, "success", 200));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_500));
        }
    }

    @Operation(summary = "신청내역 다시올리기", description = "type: 0 구인, 1 구직, 2 장비, idx: 인덱스  ")
    @PutMapping("/api/v1/mypage/myRequest/reload")
    public ResponseEntity<?> myRequestReload(
            @Schema(
                    description = "type, action, idx",
                    type = "array",
                    example = " {\"type\" :\"0\" ,"
                            + "\"idx\": \"0\"}")
            @RequestBody Map<String, Object> paramMap, HttpServletRequest request) throws Exception {
        Map<String, Object> rtnMap = new HashMap<String, Object>();
        try {
            Map<String, Object> jwtMap = jwtTokenProvider.getJwtInfo(request);
            if (null == jwtMap) {
                return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_1104));
            }
            if (paramMap.get("type") == null || paramMap.get("idx") == null) {
                return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_9998));
            }
            paramMap.put("mb_idx", jwtMap.get("mb_idx").toString());
            int result = mypageService.myRequestReload(paramMap);
            if (result > 0) {
                return ResponseEntity.ok(new ApiResponse(true, rtnMap, "success", 200));
            } else {
                return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_2110));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_500));
        }
    }


    @Operation(summary = "수락한 일자리 목록 조회")
    @GetMapping("/api/v1/mypage/acceptance/{pageNo}")
    public ResponseEntity<?> acceptanceList(
            HttpServletRequest request,
            @Parameter(name = "pageNo", description = "페이지 번호", in = ParameterIn.PATH)
            @PathVariable String pageNo) throws Exception {
        Map<String, Object> rtnMap = new HashMap<String, Object>();
        try {
            Map<String, Object> jwtMap = jwtTokenProvider.getJwtInfo(request);
            if (null == jwtMap) {
                return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_1104));
            }
            if (pageNo == null) {
                return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_9002));
            }
            jwtMap.put("pageNo", pageNo);

            Paging pagingBoard = mypageService.setAcceptancePaging(jwtMap);
            List<Map> jobList = mypageService.selectAcceptanceList(jwtMap);

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

}
