package kr.co.dk.sample.api.app.board.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.dk.sample.api.app.board.dto.BoardDTO;
import kr.co.dk.sample.api.app.board.dto.ReplyDTO;
import kr.co.dk.sample.api.app.board.service.BoardService;
import kr.co.dk.sample.api.common.model.ApiResponse;
import kr.co.dk.sample.api.common.model.ErrorCode;
import kr.co.dk.sample.api.common.model.Paging;
import kr.co.dk.sample.api.common.util.S3Util;
import kr.co.dk.sample.api.config.security.JwtTokenProvider;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Tag(name = "이슈 (게시글)")
@RestController
public class BoardController {

    private static final Logger log = LogManager.getLogger(BoardController.class);

    @Autowired
    BoardService boardService;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @Autowired
    S3Util s3Util;

    @Operation(summary = "이슈 목록", description = "")
    @GetMapping("/api/v1/board/open/board/{category}/{pageNo}")
    public ResponseEntity<?> selectAllBoard(HttpServletRequest request,
                                            @Parameter(name = "category", description = "이슈 분류", in = ParameterIn.PATH)
                                            @PathVariable String category,
                                            @Parameter(name = "pageNo", description = "페이지 번호", in = ParameterIn.PATH)
                                            @PathVariable String pageNo) throws Exception {
        Map<String, Object> rtnMap = new HashMap<String, Object>();
        try {

            if (category == null || pageNo == null) {
                return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_9002));
            }

            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("category", category);
            paramMap.put("pageNo", pageNo);
            /*
            카테고리
            best = 메인페이지 종합 베스트 글
            new = 최신순
            reply = 댓글순
            view = 조회순
             */
            Paging pagingBoard = boardService.setBoardPaging(paramMap);
            List<Map> boardList = boardService.selectAllBoard(paramMap);

            if (boardList == null || boardList.isEmpty()) {
                return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_2121));
            }

            rtnMap.put("result", boardList);
            rtnMap.put("paging", pagingBoard);
            return ResponseEntity.ok(new ApiResponse(true, rtnMap, "success", 200));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_500));
        }
    }

    @Operation(summary = "게시글 상태전환 (삭제처리)")
    @PutMapping("/api/v1/board/board/{issueIdx}")
    public ResponseEntity<?> updateBoard(HttpServletRequest request,
                                         @Parameter(required = true, description = "게시글 번호", in = ParameterIn.PATH, name = "issueIdx") @PathVariable String issueIdx) throws Exception {
        try {
            Map<String, Object> jwtMap = jwtTokenProvider.getJwtInfo(request);
            if (null == jwtMap) {
                return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_1104));
            }
            if (issueIdx == null) {
                return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_9002));
            }
            jwtMap.put("issue_idx", issueIdx);

            // 통신
            int result = boardService.updateBoard(jwtMap);

            if (result <= 0) {
                return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_9990));
            }

            return ResponseEntity.ok(new ApiResponse(true, "success", 200));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_500));
        }
    }

    @Operation(summary = "게시글 상세 조회")
    @GetMapping("/api/v1/board/open/board/detail/{idx}")
    public ResponseEntity<?> selectBoardDetail(HttpServletRequest request,
                                               @Parameter(name = "idx", description = "게시글 번호", in = ParameterIn.PATH)
                                               @PathVariable String idx) throws Exception {
        Map<String, Object> rtnMap = new HashMap<String, Object>();
        try {
            Map<String, Object> jwtMap = jwtTokenProvider.getJwtInfo(request);
            String mbIdx = "";
            if (null != jwtMap) {
                mbIdx = jwtMap.get("mb_idx").toString();
            }
            if (idx == null) {
                return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_9002));
            }

            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("issue_idx", idx);

            int increaseCount = boardService.increaseViewCount(paramMap);
            Map<String, Object> detail = boardService.selectBoardDetail(paramMap);

            if (detail == null || detail.isEmpty()) {
                return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_2121));
            }

            String modify = detail.get("issue_user_idx").toString().equals(mbIdx) ? "true" : "false";
            detail.put("modify", modify);
            rtnMap.put("detail", detail);

            List<Map> reply = boardService.selectReply(paramMap);

            for(int i = 0; i < reply.size(); i++) {
                if (reply.get(i).get("reply_user_idx").toString().equals(mbIdx)) {
                    reply.get(i).put("modify", true);
                }
            }

            List<Map> files = boardService.selectBoardFiles(paramMap);

            rtnMap.put("reply", reply);
            rtnMap.put("files", files);
            return ResponseEntity.ok(new ApiResponse(true, rtnMap, "success", 200));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_500));
        }
    }

    @Operation(summary = "게시글 작성")
    @PostMapping("/api/v1/board/write")
    public ResponseEntity<?> insertBoard(HttpServletRequest request,
                                         @Parameter(required = true) BoardDTO boardDTO) throws Exception {
        try {
            Map<String, Object> jwtMap = jwtTokenProvider.getJwtInfo(request);
            if (null == jwtMap) {
                return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_1104));
            }

            jwtMap.put("issue_title", boardDTO.getIssueTitle());
            jwtMap.put("issue_content", boardDTO.getIssueContent());
            String path = "";
            int checkVideo = 1;
            int checkImage = 1;
            String[] images = {
                    ".bmp",
                    ".rle",
                    ".dib",
                    ".jpg",
                    ".jpeg",
                    ".gif",
                    ".png",
                    ".tif",
                    ".tiff",
                    ".raw",
                    ".svg",
                    ".webp"
            };
            String[] videos = {
                    ".mp4",
                    ".m4v",
                    ".avi",
                    ".wmv",
                    ".mpg",
                    ".mpeg",
                    ".mkv",
                    ".mov",
                    ".3gp",
                    ".3g2",
                    ".webm"
            };

            List<Map> fileList = new ArrayList<Map>();

            if (boardDTO.getIssueImage() != null) {
//                path = s3Util.uploadMultiFile(boardDTO.getIssueImage(), "board/");
                List<String> paths = s3Util.uploadMultiFile(boardDTO.getIssueImage(), "board/");
//                String origin = boardDTO.getIssueImage().getOriginalFilename().toLowerCase();
                for (int j = 0; j < boardDTO.getIssueImage().size(); j++) {
                    String origin = boardDTO.getIssueImage().get(j).getOriginalFilename().toLowerCase();
                    for (int i = 0; i < images.length; i++) {
                        if (origin.contains(images[i])) {
                            checkImage = 0;
                            break;
                        }
                    }
                    for (int i = 0; i < videos.length; i++) {
                        if (origin.contains(videos[i])) {
                            checkVideo = 0;
                            break;
                        }
                    }
                }

                for (int i = 0; i < boardDTO.getIssueImage().size(); i++) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("path", paths.get(i).toString());
                    map.put("fileType", boardDTO.getFileExt().get(i).toString());
                    fileList.add(map);
                }

                log.info(path + "1");
                jwtMap.put("postType", "0"); // 파일 첨부 시 필요한 데이터, 게시글 - 0, 장비 - 1
                jwtMap.put("fileList", fileList);
            }
            jwtMap.put("issue_image", path);
            jwtMap.put("issue_additional", boardDTO.getAdditional());

            if (boardDTO.getAdditional() != null) {
                String additional = boardDTO.getAdditional().toLowerCase();
                for (int i = 0; i < images.length; i++) {
                    if (additional.contains(images[i])) {
                        checkImage = 0;
                        break;
                    }
                }
                for (int i = 0; i < videos.length; i++) {
                    if (additional.contains(videos[i])) {
                        checkVideo = 0;
                        break;
                    }
                }
            }

            log.info(path + "2");

            jwtMap.put("checkImage", checkImage);
            jwtMap.put("checkVideo", checkVideo);

            int result = boardService.insertBoard(jwtMap);

            if (boardDTO.getIssueImage() != null) {
                jwtMap.put("boardIdx", jwtMap.get("issue_idx").toString());
                int result2 = boardService.insertFilePaths(jwtMap);
                if (result * result2 <= 0) {
                    return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_9989));
                }
            } else {
                if (result <= 0) {
                    return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_9989));
                }
            }

            return ResponseEntity.ok(new ApiResponse(true, "success", 200));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_500));
        }
    }

    @Operation(summary = "댓글 작성")
    @PostMapping("/api/v1/board/reply/write")
    public ResponseEntity<?> insertReply(HttpServletRequest request,
                                         @Parameter(required = true) @RequestBody ReplyDTO replyDTO) throws Exception {
        try {
            Map<String, Object> jwtMap = jwtTokenProvider.getJwtInfo(request);
            if (null == jwtMap) {
                return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_1104));
            }

            jwtMap.put("issue_idx", replyDTO.getIssueIdx());
            jwtMap.put("reply_content", replyDTO.getReplyContent());

            int result = boardService.insertReply(jwtMap);

            if (result <= 0) {
                return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_9989));
            }

            return ResponseEntity.ok(new ApiResponse(true, "success", 200));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_500));
        }
    }

    @Operation(summary = "댓글 삭제")
    @PostMapping("/api/v1/board/reply/delete")
    public ResponseEntity<?> deleteReply(
            @Schema(
                description = "reply_idx, issue_idx",
                type = "array",
                example = " {\"reply_idx\" :\"1\" ,"
                        + "\"issue_idx\": \"1\"}")
            @RequestBody Map<String, Object> paramMap, HttpServletRequest request) throws Exception {
        try {
            Map<String, Object> jwtMap = jwtTokenProvider.getJwtInfo(request);
            if (null == jwtMap) {
                return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_1104));
            }

            paramMap.put("mb_idx", jwtMap.get("mb_idx").toString());

            int result = boardService.deleteReply(paramMap);

            if (result <= 0) {
                return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_9989));
            }

            return ResponseEntity.ok(new ApiResponse(true, "success", 200));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_500));
        }
    }

    @Operation(summary = "댓글 수정")
    @PostMapping("/api/v1/board/reply/update")
    public ResponseEntity<?> updateReply(
            @Schema(
                    description = "idx, content",
                    type = "array",
                    example = " {\"idx\" :\"1\" ,"
                            + "\"content\": \"\"}")
            @RequestBody Map<String, Object> paramMap, HttpServletRequest request) throws Exception {
        try {
            Map<String, Object> jwtMap = jwtTokenProvider.getJwtInfo(request);
            if (null == jwtMap) {
                return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_1104));
            }

            paramMap.put("mb_idx", jwtMap.get("mb_idx").toString());

            int result = boardService.updateReply(paramMap);

            if (result <= 0) {
                return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_9989));
            }

            return ResponseEntity.ok(new ApiResponse(true, "success", 200));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_500));
        }
    }

    @Operation(summary = "파일 업로드")
    @RequestMapping(
            path = "/api/v1/board/upload",
            method = RequestMethod.POST,
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadFile(HttpServletRequest request,
                                        @RequestPart MultipartFile multipartFile, @RequestParam String idx) throws Exception {
        log.info(multipartFile);
        try {
////            Map<String, Object> jwtMap = jwtTokenProvider.getJwtInfo(request);
////            if (null == jwtMap) {
////                return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_1104));
////            }
////            if (idx == null) {
////                return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_9002));
////            }
//            Map<String, Object> paramMap = new HashMap<>();
////            paramMap.put("user_idx", jwtMap.get("cpn_idx"));
//            paramMap.put("user_idx", 1);
//            paramMap.put("issue_idx", replyDTO.getIssueIdx());
//            paramMap.put("reply_content", replyDTO.getReplyContent());


//            int result = boardService.insertReply(paramMap);

            String path = s3Util.uploadSingleFile(multipartFile, "board/");

            log.info(path);

//            if(result <= 0) {
            return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_2124));
//            }

//            return ResponseEntity.ok(new ApiResponse(true, "success", 200));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_500));
        }
    }

    @Operation(summary = "댓글 리스트")
    @GetMapping("/api/v1/board/open/board/detail/reply/{issueIdx}")
    public ResponseEntity<?> getReplyList(HttpServletRequest request,
                                          @Parameter(required = true, description = "게시글 번호", in = ParameterIn.PATH, name = "issueIdx")
                                          @PathVariable String issueIdx) throws Exception {
        Map<String, Object> rtnMap = new HashMap<String, Object>();
        try {
            Map<String, Object> jwtMap = jwtTokenProvider.getJwtInfo(request);
            String mbIdx = "";
            if (null != jwtMap) {
                mbIdx = jwtMap.get("mb_idx").toString();
            }

            if (issueIdx == null) {
                return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_9002));
            }

            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("issue_idx", issueIdx);

            List<Map> result = boardService.selectReply(paramMap);

            if (result == null || result.isEmpty()) {
                return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_2121));
            }

            for(int i = 0; i < result.size(); i++) {
                if (result.get(i).get("reply_user_idx").toString().equals(mbIdx)) {
                    result.get(i).put("modify", true);
                }
            }

            rtnMap.put("result", result);

            return ResponseEntity.ok(new ApiResponse(true, rtnMap, "success", 200));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_500));
        }
    }

}
