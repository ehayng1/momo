package kr.co.dk.sample.api.app.chat.controller;

import kr.co.dk.sample.api.app.chat.dao.ChatRoomRepository;
import kr.co.dk.sample.api.app.chat.service.ChatService;
import kr.co.dk.sample.api.common.model.ApiResponse;
import kr.co.dk.sample.api.common.model.ErrorCode;
import kr.co.dk.sample.api.config.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/chat")
public class RoomController {

    private static final Logger log = LogManager.getLogger(RoomController.class);
    private final ChatRoomRepository repository;
    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @Autowired
    ChatService chatService;

    //채팅방 목록 조회
    @GetMapping(value = "/open/rooms")
    public ResponseEntity<?> rooms(HttpServletRequest request) {
        Map<String, Object> rtnMap = new HashMap<String, Object>();
        try {

            List<Map> chatRoomList = chatService.rooms();

            if (chatRoomList == null || chatRoomList.isEmpty()) {
                return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_2121));
            }

            rtnMap.put("result", chatRoomList);
            return ResponseEntity.ok(new ApiResponse(true, rtnMap, "success", 200));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_500));
        }
    }

    //채팅방 개설
//    @PostMapping(value = "/room")
//    public String create(@RequestParam String name, RedirectAttributes rttr){
//
//        log.info("# Create Chat Room , name: " + name);
//        rttr.addFlashAttribute("roomName", repository.createChatRoomDTO(name));
//        return "redirect:/chat/rooms";
//    }

    //채팅방 조회
    @GetMapping("/room")
    public void getRoom(String roomId, Model model) {
        model.addAttribute("room", repository.findRoomById(roomId));
    }
}