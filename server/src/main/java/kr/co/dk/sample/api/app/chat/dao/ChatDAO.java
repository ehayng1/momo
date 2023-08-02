package kr.co.dk.sample.api.app.chat.dao;

import kr.co.dk.sample.api.app.chat.dto.ChatMessageDTO;
import org.apache.ibatis.session.SqlSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class ChatDAO {

    static final String NAMESAPCE = "kr.co.dk.sample.api.app.chat.mapper.ChatDAO.";
    private static final Logger log = LogManager.getLogger(ChatDAO.class);
    @Autowired
    Environment env;
    //	@Autowired
    private final SqlSession sqlSession;
    //	@Qualifier("readOnlySqlSession")
    private final SqlSessionTemplate readOnlySqlSession;

    public ChatDAO(SqlSession sqlSession, SqlSessionTemplate readOnlySqlSession) {
        this.sqlSession = sqlSession;
        this.readOnlySqlSession = readOnlySqlSession;
    }

    public Map<String, Object> getRecvUserInfo(Map<String, Object> map) throws Exception {
        log.debug("+++++++++++++++++++++++++++++++++++++ getRecvUserInfo:::");
        Map<String, Object> result = readOnlySqlSession.selectOne(NAMESAPCE + "getRecvUserInfo", map);

        return result;
    }

    public List<Map> getJoinedRoomList(Map<String, Object> map) throws Exception {
        log.debug("+++++++++++++++++++++++++++++++++++++ getJoinedRoomList:::");
        List<Map> result = readOnlySqlSession.selectList(NAMESAPCE + "getJoinedRoomList", map);

        return result;
    }

    public Map<String, Object> selectRoomBeforeInsert(Map<String, Object> map) throws Exception {
        log.debug("+++++++++++++++++++++++++++++++++++++ selectRoomBeforeInsert:::");
        Map<String, Object> result = readOnlySqlSession.selectOne(NAMESAPCE + "selectRoomBeforeInsert", map);

        return result;
    }

    public int insertRoom(Map<String, Object> map) throws Exception {
        log.debug("+++++++++++++++++++++++++++++++++++++ insertRoom:::");
        int result = readOnlySqlSession.insert(NAMESAPCE + "insertRoom", map);

        return result;
    }

    public List<Map> selectRoomIdMessage(Map<String, Object> map) throws Exception {
        log.debug("+++++++++++++++++++++++++++++++++++++ selectRoomIdMessage:::");
        List<Map> result = readOnlySqlSession.selectList(NAMESAPCE + "selectRoomIdMessage", map);

        return result;
    }

    public int insertMessageInChat(Map<String, Object> map) throws Exception {
        log.debug("+++++++++++++++++++++++++++++++++++++ insertMessageInChat:::");
        int result = readOnlySqlSession.insert(NAMESAPCE + "insertMessageInChat", map);

        return result;
    }

    public int insertMessage(ChatMessageDTO chatMessageDTO) throws Exception {
        log.debug("+++++++++++++++++++++++++++++++++++++ insertMessage:::");
        int result = readOnlySqlSession.insert(NAMESAPCE + "insertMessage", chatMessageDTO);

        return result;
    }

    public List<Map> reciveMessageReturn(Map<String, Object> map) throws Exception {
        log.debug("+++++++++++++++++++++++++++++++++++++ insertRoom:::");
        List<Map> result = readOnlySqlSession.selectList(NAMESAPCE + "insertRoom", map);

        return result;
    }

    public Map<String, Object> selectUserInfo(Map<String, Object> map) throws Exception {
        log.debug("+++++++++++++++++++++++++++++++++++++ selectUserInfo:::");
        Map<String, Object> result = readOnlySqlSession.selectOne(NAMESAPCE + "selectUserInfo", map);
        return result;
    }

    public List<Map> rooms() throws Exception {
        log.debug("+++++++++++++++++++++++++++++++++++++ rooms:::");
        List<Map> result = readOnlySqlSession.selectList(NAMESAPCE + "rooms");

        return result;
    }

}
