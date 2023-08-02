package kr.co.dk.sample.api.app.chat.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.dk.sample.api.app.chat.dto.ChatDTO;
import kr.co.dk.sample.api.app.chat.service.ChatService;
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

@Tag(name = "채팅")
@RestController
public class ChatController {

    private static final Logger log = LogManager.getLogger(ChatController.class);
    private final int PAGE = 10;
    @Autowired
    JwtTokenProvider jwtTokenProvider;
    @Autowired
    ChatService chatService;

//    @MessageMapping("/receive")
//
//    @SendTo("/send")
//
//
//    // SocketHandler는 1) /receive에서 메시지를 받고, /send로 메시지를 보내줍니다.
//    // 정의한 SocketVO를 1) 인자값, 2) 반환값으로 사용합니다.
//    public ResponseEntity<?> SocketVO(ChatVO chatVO) {
//
//        Map<String, Object> paramMap = new HashMap<String, Object>();
//        Map<String, Object> rtn
//        Map = new HashMap<String, Object>();
//        // vo에서 getter로 userName을 가져옵니다.
//        String userName = chatVO.getUserName();
//        // vo에서 setter로 content를 가져옵니다.
//        String content = chatVO.getContent();
//        String token = chatVO.getToken();
//
//        // 생성자로 반환값을 생성합니다.
//        paramMap.put("userName", userName);
//        paramMap.put("content", content);
//        paramMap.put("token", token);
//
//        rtnMap.put("userName", userName);
//        rtnMap.put("content", content);
//
//        // 반환
//        try {
////            Map<String, Object> jwtMap = jwtTokenProvider.getJwtInfo(request);
//            String mbIdx = jwtTokenProvider.getUserIdFromJWTString(token);
//            if (mbIdx == null) {
//                return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_1104));
//            }
//
//            return ResponseEntity.ok(new ApiResponse(true, rtnMap, "success", 200));
//        } catch (Exception e) {
//            e.printStackTrace();
//            return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_500));
//        }
//    }

    @GetMapping("/api/v1/chat/info/{idx}")
    @Operation(summary = "채팅방의 상대방 정보 조회")
    public ResponseEntity<?> getRecvUserInfo(
            HttpServletRequest request,
            @Parameter(name = "idx", description = "방 번호", in = ParameterIn.PATH)
            @PathVariable String idx) {
        Map<String, Object> rtnMap = new HashMap<String, Object>();
        try {
            Map<String, Object> jwtMap = jwtTokenProvider.getJwtInfo(request);
            if (null == jwtMap) {
                return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_1104));
            }

            if (null == idx || "" == idx) {
                return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_9002));
            }

            jwtMap.put("idx", idx);
            String result = chatService.getRecvUserInfo(jwtMap);

            if (result == null || result.isEmpty()) {
                return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_2125));
            }

            rtnMap.put("recv", result);
            return ResponseEntity.ok(new ApiResponse(true, rtnMap, "success", 200));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_500));
        }
    }

    @GetMapping("/api/v1/chat/chatroom")
    @Operation(summary = "사용자의 채팅방 목록 가져오기")
    public ResponseEntity<?> getJoinedRoomListByUserId(HttpServletRequest request) {
        Map<String, Object> rtnMap = new HashMap<String, Object>();
        try {
            Map<String, Object> jwtMap = jwtTokenProvider.getJwtInfo(request);
            if (null == jwtMap) {
                return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_1104));
            }

//            Paging pagingBoard = boardService.setBoardPaging(jwtMap);
            List<Map> chatRoomList = chatService.getJoinedRoomList(jwtMap);

            if (chatRoomList == null || chatRoomList.isEmpty()) {
                return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_2125));
            }

            rtnMap.put("result", chatRoomList);
//            rtnMap.put("paging", pagingBoard);
            return ResponseEntity.ok(new ApiResponse(true, rtnMap, "success", 200));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_500));
        }
    }

    @PostMapping("/api/v1/chat/chatroom")
    @Operation(summary = "채팅방 생성")
    public ResponseEntity<?> insertRoom(
            @Schema(
                    description = "userIdx",
                    type = "array",
                    example = " {\"userIdx\" :\"1\"}")
            @RequestBody Map<String, Object> paramMap, HttpServletRequest request) throws Exception {
        Map<String, Object> rtnMap = new HashMap<String, Object>();
        try {
            Map<String, Object> jwtMap = jwtTokenProvider.getJwtInfo(request);
            if (null == jwtMap) {
                return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_1104));
            }
            if (null == paramMap) {
                return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_9002));
            }
            if (jwtMap.get("mb_idx").toString().equals(paramMap.get("userIdx").toString())) {
                return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_2128));
            }
//            Map<String, Object> blockchain = new HashMap<>();

//            blockchain =chatService.selectUserInfo(paramMap.get("mb_idx").toString());
            Map<String, Object> select = chatService.selectUserInfo(paramMap);

            jwtMap.put("userIdx", paramMap.get("userIdx").toString());
            jwtMap.put("test", select);
            int result = chatService.insertRoom(jwtMap);
            jwtMap.put("chatroomId", result);
//            return ResponseEntity.ok(new ApiResponse(true, jwtMap , "success", 200));

            //이것     jwtMap.put("result",select);
//            int result = chatService.insertRoom(jwtMap);
            if (result > 0) {
                return ResponseEntity.ok(new ApiResponse(true, jwtMap, "success", 200));
            } else {
                return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_9989));
            }

            //이것    rtnMap.put("realReuslt",jwtMap);
//          List<Map>result2= chatService.insertRoom(rtnMap);


//                return ResponseEntity.ok(new ApiResponse(true, result2, "success", 200));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_500));
        }
    }

    //채팅방 메세지 불러오기
    @GetMapping("/api/v1/chat/chatroom/{chatroomId}")
    @Operation(summary = "채팅방의 메세지 보기 ")
    public ResponseEntity<?> getMessageByChatroomId(HttpServletRequest request,
                                                    @Parameter(name = "chatroomId", description = "방 번호", in = ParameterIn.PATH)
                                                    @PathVariable String chatroomId) {
        Map<String, Object> rtnMap = new HashMap<String, Object>();
        try {
//            Map<String, Object> jwtMap = jwtTokenProvider.getJwtInfo(request);
//            if (null == jwtMap) {
//                return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_1104));
//            }

//            Paging pagingBoard = boardService.setBoardPaging(jwtMap);
//            Map<Map> paramMam = chatService.getJoinedRoomList(jwtMap);

            if (chatroomId == null) {
                return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_9989));
            }
            Map<String, Object> paramMap = new HashMap<>();

            paramMap.put("chatRoom_idx", chatroomId);
            List<Map> result = chatService.selectRoomIdMessage(paramMap);
            rtnMap.put("result", result);

            return ResponseEntity.ok(new ApiResponse(true, rtnMap, "success", 200));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_500));
        }
//        return chatService.getMessageByChatroomId(chatroomId, page);
    }

    // 메세지보내기
    @PostMapping("/api/v1/chat/send")
    @Operation(summary = "채팅 메세지 보내기")
    public ResponseEntity<?> insertMessageInChat(
            @Parameter(required = true) HttpServletRequest request, @RequestBody ChatDTO chatDTO) throws Exception {
        Map<String, Object> rtnMap = new HashMap<String, Object>();
        try {
            Map<String, Object> jwtMap = jwtTokenProvider.getJwtInfo(request);
            if (null == jwtMap) {
                return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_1104));
            }
            rtnMap.put("chatroom_idx", chatDTO.getChatroom_idx());
            rtnMap.put("send_user_idx", jwtMap.get("mb_idx").toString());
            rtnMap.put("recv_user_idx", chatDTO.getRecvName());

            rtnMap.put("content", chatDTO.getContent());


            int result = chatService.insertMessageInChat(rtnMap);
            log.info("result", result);
            return ResponseEntity.ok(new ApiResponse(true, "success", 200));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_500));
        }
    }
//
//    @PatchMapping("/api/v1/chat/chatroom/message")
//    @Operation(summary = "채팅방 읽음 처리")
//    public ResponseEntity<?> updateReadtime(@RequestBody ChatroomRequest chatroom) {
//        log.info("ChatroomController : updateReadtime => {}", chatroom.getChatroomId());
//
//        return chatService.updateReadTime(chatroom);
//    }
//
//    @MessageMapping("/api/v1/chat/message")
//    public ResponseEntity<BasicResponse> sendMessage(@Payload Message message) {
//        log.info("ChatroomController : sendMessage " +  "전달 메세지 : " + message);
//
//        return chatService.sendMessage(message);
//    }

    @PostMapping("/api/v1/chat/chatroom/delete")
//    @MessageMapping("/message")
//    @SendTo("api/v1/chat/send")
    @Operation(summary = "채팅방 나가기")
    public ResponseEntity<?> outMessage(
            @Parameter(required = true) HttpServletRequest request, @RequestBody ChatDTO chatDTO) throws Exception {
        Map<String, Object> rtnMap = new HashMap<String, Object>();
        try {
//
            return ResponseEntity.ok(new ApiResponse(true, "success", 200));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_500));
        }
    }


}


