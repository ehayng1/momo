package kr.co.dk.sample.api.app.chat.service;

import kr.co.dk.sample.api.app.chat.dao.ChatDAO;
import kr.co.dk.sample.api.app.chat.dto.ChatMessageDTO;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Properties;


@RequiredArgsConstructor
@Service
public class ChatService {

    private static final Logger log = LogManager.getLogger(ChatService.class);
    @Autowired
    ChatDAO chatDAO;
    @Autowired
    Environment env;
    @Qualifier("prop")
    private Properties properties;

    public String getRecvUserInfo(Map<String, Object> map) throws Exception {
        String senderIdx = map.get("mb_idx").toString();
        Map<String, Object> result = chatDAO.getRecvUserInfo(map);

        if (!result.get("chatroom_user_idx_1").toString().equals(senderIdx)) {
            return result.get("chatroom_user_idx_1").toString();
        } else {
            return result.get("chatroom_user_idx_2").toString();
        }
    }


    public List<Map> getJoinedRoomList(Map<String, Object> map) throws Exception {
        List<Map> list = chatDAO.getJoinedRoomList(map);
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).get("user_idx_1") == null) {
                list.get(i).put("chat_title", list.get(i).get("name_2"));
            } else {
                list.get(i).put("chat_title", list.get(i).get("name_1"));
            }
        }
        return list;
    }

    public int insertRoom(Map<String, Object> map) throws Exception {
        // 채팅방 생성 전에 이미 생성되있는지 확인
        Map<String, Object> room = chatDAO.selectRoomBeforeInsert(map);
        if (room != null) {
            return Integer.parseInt(room.get("chatroom_idx").toString());
        }
        log.info(room + "map");
//    이것     if (room != null) {
//            return (List<Map>) room.get(Integer.parseInt(("chatroom_idx")));
//        }else{
//            List<Map> result =chatDAO.insertRoom(map);
//            return (List<Map>) result.get(Integer.parseInt(("chatroom_idx")));
//
//        }

        Map<String, Object> resultData = chatDAO.selectUserInfo(map);
        map.put("resultData", resultData);
        int result = chatDAO.insertRoom(map);
//        map.put("chatroom_idx",result);

//        resultData.put("result2",result);
        if (result > 0) {
            return Integer.parseInt(map.put("chatroom_idx", result).toString()
//                    Integer.parseInt(map.get("test").toString())
            );
        } else {
            return 0;
        }

//        return resultData.get("test").toString();


    }

    public Map<String, Object> selectUserInfo(Map<String, Object> blockchain) throws Exception {
//        List<Map>resultData= chatDAO.selectUserInfo(map);
        log.info("========start result data");
//        System.out.println(resultData + "resultDatawqerweqrweqrqwerwqer");

        return chatDAO.selectUserInfo(blockchain);

    }

    public List<Map> selectRoomIdMessage(Map<String, Object> map) throws Exception {
        return chatDAO.selectRoomIdMessage(map);
        // 채팅방 메세지  불러오기
//        List<String, Object> ListMessage = chatDAO.selectRoomIdMessage(map);
//        if (room != null) {
//            return Integer.parseInt(room.get("chatroom_idx").toString());
//        }

//        return chatDAO.insertRoom(map);
    }

    ///채팅 메세지
    public int insertMessageInChat(Map<String, Object> map) throws Exception {
        System.out.println(map.toString());
//        template.convertAndSend("/sub/"+map);
        return chatDAO.insertMessageInChat(map);
        // 채팅방 메세지  불러오기
//        List<String, Object> ListMessage = chatDAO.selectRoomIdMessage(map);
//        if (room != null) {
//            return Integer.parseInt(room.get("chatroom_idx").toString());
//        }

//        return chatDAO.insertRoom(map);
    }

    public int insertMessage(ChatMessageDTO chatMessageDTO) throws Exception {
        return chatDAO.insertMessage(chatMessageDTO);
    }

    public List<Map> reciveMessageReturn(Map<String, Object> map) throws Exception {
        System.out.println(map.toString());
//        template.convertAndSend("/sub/"+map);
        return chatDAO.reciveMessageReturn(map);

    }

    // 메인스크린 채팅방 목록
    public List<Map> rooms() throws Exception {
        return chatDAO.rooms();
    }

}
