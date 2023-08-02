package kr.co.dk.sample.api.app.chat.controller;


import kr.co.dk.sample.api.app.chat.vo.ChatVO;
import kr.co.dk.sample.api.app.user.controller.UserController;
import kr.co.dk.sample.api.config.security.JwtTokenProvider;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class SocketController {
    private static final Logger log = LogManager.getLogger(UserController.class);
    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @MessageMapping("/receive")

    // /send로 메시지를 반환합니다.
    @SendTo("/send")

    // SocketHandler는 1) /receive에서 메시지를 받고, /send로 메시지를 보내줍니다.
    // 정의한 SocketVO를 1) 인자값, 2) 반환값으로 사용합니다.
    public ChatVO SocketHandler(ChatVO chatVo) {
        // vo에서 getter로 userName을 가져옵니다.
        String userName = chatVo.getUserName();

        // vo에서 setter로 content를 가져옵니다.
        String content = chatVo.getContent();
        // 생성자로 반환값을 생성합니다.
//        String test = jwtTokenProvider.getUserIdFromJWTString(userName);
//      String test= jwtTokenProvider.getUserIdFromJWTString(userName);
        ChatVO result = new ChatVO(userName, content);

        log.info("===========test=======" + result);

        // 반환
//        Map<String, Object> jwtMap = jwtTokenProvider.getJwtInfo(userName);


        log.info(result + "===result====");
        return result;
    }


}
