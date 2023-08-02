package kr.co.dk.sample.api.app.equip.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.dk.sample.api.app.equip.dto.EquipRegiDTO;
import kr.co.dk.sample.api.app.equip.dto.LeaseDTO;
import kr.co.dk.sample.api.app.equip.dto.RentalDTO;
import kr.co.dk.sample.api.app.equip.service.EquipService;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Tag(name = "장비")
@RestController
public class EquipController {

    private static final Logger log = LogManager.getLogger(EquipController.class);

    @Autowired
    EquipService equipService;

    @Autowired
    PushService pushService;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @Autowired
    S3Util s3Util;

    @Operation(summary = "임대/임차 목록 조회")
    @GetMapping("/api/v1/equip/open/{category}/{subCategory}/{pageNo}")
    public ResponseEntity<?> getEquipList(HttpServletRequest request,
                                          @Parameter(name = "category", description = "장비분류", in = ParameterIn.PATH)
                                          @PathVariable String category,
                                          @Parameter(name = "subCategory", description = "지역", in = ParameterIn.PATH)
                                          @PathVariable String subCategory,
                                          @Parameter(name = "pageNo", description = "페이지 번호", in = ParameterIn.PATH)
                                          @PathVariable String pageNo) throws Exception {
        Map<String, Object> rtnMap = new HashMap<String, Object>();
        try {

            if (category == null) {
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
                tool = 공구
                gear = 장비
             */
            /*
                서브카테고리
                all = 전체
                time = 등록일 최신순
                new = 연식 4년이하
                old = 연식 5년이상
             */
            paramMap.put("category", category);
            paramMap.put("subCategory", subCategory);
            paramMap.put("pageNo", pageNo);
            Paging pagingBoard = equipService.setEquipPaging(paramMap);
            List<Map> equipList = equipService.selectEquipList(paramMap);

            if (equipList == null || equipList.isEmpty()) {
                return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_2121));
            }

            rtnMap.put("result", equipList);
            rtnMap.put("paging", pagingBoard);
            return ResponseEntity.ok(new ApiResponse(true, rtnMap, "success", 200));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_500));
        }
    }

    @Operation(summary = "카테고리 항목별 개수 가져오기")
    @GetMapping("/api/v1/equip/open/count/{type}")
    public ResponseEntity<?> getEquipCount(HttpServletRequest request,
                                          @Parameter(name = "type", description = "종류/지역 구분", in = ParameterIn.PATH)
                                          @PathVariable String type) throws Exception {
        Map<String, Object> rtnMap = new HashMap<String, Object>();
        try {

            // 0 = 장비, 1 = 지역
            if (type == null) {
                return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_9002));
            }

            Map<String, Object> paramMap = new HashMap<>();

            paramMap.put("type", type);
            List<Map> cnt = equipService.getEquipCount(paramMap);

            if (cnt == null || cnt.isEmpty()) {
                return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_2121));
            }

            rtnMap.put("result", cnt);
            return ResponseEntity.ok(new ApiResponse(true, rtnMap, "success", 200));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_500));
        }
    }


    @Operation(summary = "장비 상세 조회")
    @GetMapping("/api/v1/equip/open/equipImages/{idx}")
    public ResponseEntity<?> getEquipList(
            HttpServletRequest request,
            @Parameter(name = "idx", description = "장비번호", in = ParameterIn.PATH)
            @PathVariable String idx) throws Exception {
        Map<String, Object> rtnMap = new HashMap<String, Object>();
        try {

            if (idx == null) {
                return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_9002));
            }

            Map<String, Object> paramMap = new HashMap<>();

            paramMap.put("idx", idx);
            List<Map> pathList = equipService.selectEquipFilePaths(paramMap);

            Map<String, Object> equip = equipService.selectEquipDetail(paramMap);

            rtnMap.put("result", pathList);
            rtnMap.put("equip", equip);
            return ResponseEntity.ok(new ApiResponse(true, rtnMap, "success", 200));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_500));
        }
    }

    @Operation(summary = "임대 등록")
    @PostMapping("/api/v1/equip/add/rental")
    public ResponseEntity<?> insertEquipRental(HttpServletRequest request, @Parameter(required = true) RentalDTO rentalDTO) throws Exception {
        try {
            Map<String, Object> jwtMap = jwtTokenProvider.getJwtInfo(request);
            if (null == jwtMap) {
                return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_1104));
            }
//            if (idx == null) {
//                return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_9002));
//            }
            jwtMap.put("rentalDTO", rentalDTO);
            String path = "";
            List<Map> fileList = new ArrayList<Map>();
            if (rentalDTO.getConstructionImage() != null) {
//                path = s3Util.uploadSingleFile(rentalDTO.getConstructionImage(), "equip/");
                List<String> paths = s3Util.uploadMultiFile(rentalDTO.getConstructionImage(), "equip/");
                log.info(path + "1");
                for (int i = 0; i < rentalDTO.getConstructionImage().size(); i++) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("path", paths.get(i).toString());
                    map.put("fileType", rentalDTO.getFileExt().get(i).toString());
                    fileList.add(map);
                }

                log.info(path + "1");
                jwtMap.put("postType", "1"); // 파일 첨부 시 필요한 데이터, 게시글 - 0, 장비 - 1
                jwtMap.put("fileList", fileList);
            } else {
                Map<String, Object> cnt = equipService.selectEquipOwnerCnt(jwtMap);
                if (cnt != null) {
                    if (Integer.parseInt(cnt.get("cnt").toString()) > 0) {
                        Map<String, Object> imagePath = equipService.selectEquipOwnerImage(jwtMap);
                        if (imagePath != null) {
                            path = imagePath.get("owner_image").toString();
                        }
                    }
                }
            }
            jwtMap.put("constructionImage", path);

            log.info(path + "2");

            int result = equipService.insertEquipRental(jwtMap);

            if (rentalDTO.getConstructionImage() != null) {
                jwtMap.put("equipIdx", jwtMap.get("construction_idx").toString());
                int result2 = equipService.insertFilePaths(jwtMap);
                if (result * result2 <= 0) {
                    return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_9989));
                }
            } else {
                int result2 = 1;
                if (result > 0 && rentalDTO.getModify() == 1 && rentalDTO.getIdx() != 0) {
                    result2 = equipService.insertEquipFiles(jwtMap);
                }
                if (result * result2 <= 0) {
                    return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_9989));
                }
            }


            return ResponseEntity.ok(new ApiResponse(true, "success", 200));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_500));
        }
    }

    @Operation(summary = "임차 등록")
    @PostMapping("/api/v1/equip/add/lease")
    public ResponseEntity<?> insertEquipLease(HttpServletRequest request, @Parameter(required = true) @RequestBody LeaseDTO leaseDTO) throws Exception {
        try {
            Map<String, Object> jwtMap = jwtTokenProvider.getJwtInfo(request);
            if (null == jwtMap) {
                return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_1104));
            }
//            if (idx == null) {
//                return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_9002));
//            }
            jwtMap.put("leaseDTO", leaseDTO);

            int result = equipService.insertEquipLease(jwtMap);

            if (result <= 0) {
                return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_9989));
            }

            return ResponseEntity.ok(new ApiResponse(true, "success", 200));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_500));
        }
    }

    @Operation(summary = "장비주 등록")
    @PostMapping("/api/v1/equip/add/regi")
    public ResponseEntity<?> insertEquipRegister(@Schema(
            description = "nickname",
            type = "array",
            example = " {\"owner_name\" :\"\" ,"
                    + "\"owner_phone\": \"\","
                    + "\"equip_spec\": \"\","
                    + "\"owner_image\": \"\","
                    + "\"owner_category\": \"\","
    )
                                                 @Parameter(required = true) EquipRegiDTO equipRegiDTO) throws Exception {
        Map<String, Object> rtnMap = new HashMap<>();
        try {
            rtnMap.put("owner_name", equipRegiDTO.getOwner_name());
            rtnMap.put("owner_phone", equipRegiDTO.getOwner_phone());
            rtnMap.put("equip_spec", equipRegiDTO.getEquip_spec());
            String path = "";
            if (equipRegiDTO.getOwner_image() != null) {
                path = s3Util.uploadSingleFile(equipRegiDTO.getOwner_image(), "equipRegi/");
                log.info(path + "1");
            }
            rtnMap.put("owner_image", path);
            rtnMap.put("owner_category", equipRegiDTO.getOwner_category());

            int result = equipService.insertEquipRegister(rtnMap);

            if (result <= 0) {
                return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_9989));
            }

            return ResponseEntity.ok(new ApiResponse(true, "success", 200));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_500));
        }
    }

    @Operation(summary = "장비 예약상태 변환")
    @PostMapping("/api/v1/equip/inert")
    public ResponseEntity<?> inertEquip(@Schema(
            description = "equipIdx",
            type = "array",
            example = "{" +
                        "\"equipIdx\": \"1\"," +
                        "\"userIdx\": \"1\"" +
                    "}")
                                        @RequestBody Map<String, Object> paramMap, HttpServletRequest request) throws Exception {
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
//            if(jwtMap.get("mb_idx").())
//            jwtMap.get("mb_idx");
//            if (jwtMap.get("mb_idx") || paramsMap.get(
//                    "user_type").toString().equals("노무사"))

            paramMap.put("mb_idx", jwtMap.get("mb_idx").toString());

//            int result = equipService.inertEquip(paramMap); // construction_status 비활성으로 변경 -> 개인이 수정하게끔 변경됨
            int result2 = equipService.insertEquipHistory(paramMap);
            int result3 = 1; // 채팅방이 있다는 가정

            // 채팅방 확인
            Map<String, Object> chatroom = equipService.checkChatroom(paramMap);
            String chatroomIdx = "";
            if (Integer.parseInt(String.valueOf(chatroom.get("cnt"))) < 1) {
                // 채팅방 생성
                result3 = equipService.insertChatroom(paramMap);
                if (result3 > 0) {
                    chatroomIdx = paramMap.get("chatroom_idx").toString();
                }
            } else {
                // 채팅방 idx 조회
                chatroom = equipService.getChatroom(paramMap);
                chatroomIdx = chatroom.get("chatroom_idx").toString();
            }

            if (result2 * result3 <= 0) {
                return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_9989));
            }

            // 푸시발송 추가할것
            pushService.sendMessage(idx + "," + idx2, "건설모모 장비 신청", "채팅을 확인해주세요.");

            return ResponseEntity.ok(new ApiResponse(true, chatroomIdx,"success", 200));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_500));
        }
    }
}
