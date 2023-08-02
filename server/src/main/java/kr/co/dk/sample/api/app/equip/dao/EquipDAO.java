package kr.co.dk.sample.api.app.equip.dao;

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
public class EquipDAO {

    static final String NAMESAPCE = "kr.co.dk.sample.api.app.equip.mapper.EquipDAO.";
    private static final Logger log = LogManager.getLogger(EquipDAO.class);
    @Autowired
    Environment env;
    //	@Autowired
    private final SqlSession sqlSession;
    //	@Qualifier("readOnlySqlSession")
    private final SqlSessionTemplate readOnlySqlSession;

    public EquipDAO(SqlSession sqlSession, SqlSessionTemplate readOnlySqlSession) {
        this.sqlSession = sqlSession;
        this.readOnlySqlSession = readOnlySqlSession;
    }

    public Map<String, Object> selectEquipCnt(Map<String, Object> map) throws Exception {
        log.debug("+++++++++++++++++++++++++++++++++++++ selectEquipCnt:::");
        Map<String, Object> result = readOnlySqlSession.selectOne(NAMESAPCE + "selectEquipCnt", map);

        return result;
    }

    public List<Map> selectEquipList(Map<String, Object> map) throws Exception {
        log.debug("+++++++++++++++++++++++++++++++++++++ selectEquipList:::");
        List<Map> result = readOnlySqlSession.selectList(NAMESAPCE + "selectEquipList", map);

        return result;
    }

    public List<Map> getEquipCount(Map<String, Object> map) throws Exception {
        log.debug("+++++++++++++++++++++++++++++++++++++ getEquipCount:::");
        List<Map> result = readOnlySqlSession.selectList(NAMESAPCE + "getEquipCount", map);

        return result;
    }

    public List<Map> getAreaCount(Map<String, Object> map) throws Exception {
        log.debug("+++++++++++++++++++++++++++++++++++++ getAreaCount:::");
        List<Map> result = readOnlySqlSession.selectList(NAMESAPCE + "getAreaCount", map);

        return result;
    }

    public Map<String, Object> selectEquipDetail(Map<String, Object> map) throws Exception {
        log.debug("+++++++++++++++++++++++++++++++++++++ selectEquipDetail:::");
        Map<String, Object> result = readOnlySqlSession.selectOne(NAMESAPCE + "selectEquipDetail", map);

        return result;
    }


    public List<Map> selectEquipFilePaths(Map<String, Object> map) throws Exception {
        log.debug("+++++++++++++++++++++++++++++++++++++ selectEquipFilePaths:::");
        List<Map> result = readOnlySqlSession.selectList(NAMESAPCE + "selectEquipFilePaths", map);

        return result;
    }

    public int insertEquipHistory(Map<String, Object> map) throws Exception {
        log.debug("+++++++++++++++++++++++++++++++++++++ insertEquipHistory:::");
        int result = readOnlySqlSession.insert(NAMESAPCE + "insertEquipHistory", map);

        return result;
    }

    public int insertEquipRental(Map<String, Object> map) throws Exception {
        log.debug("+++++++++++++++++++++++++++++++++++++ insertEquipRental:::");
        int result = readOnlySqlSession.insert(NAMESAPCE + "insertEquipRental", map);

        return result;
    }

    public int insertFilePaths(Map<String, Object> map) throws Exception {
        log.debug("+++++++++++++++++++++++++++++++++++++ insertFilePaths:::");
        int result = readOnlySqlSession.insert(NAMESAPCE + "insertFilePaths", map);

        return result;
    }

    public int insertEquipLease(Map<String, Object> map) throws Exception {
        log.debug("+++++++++++++++++++++++++++++++++++++ insertEquipLease:::");
        int result = readOnlySqlSession.insert(NAMESAPCE + "insertEquipLease", map);

        return result;
    }

    public int insertEquipRegister(Map<String, Object> map) throws Exception {
        log.debug("+++++++++++++++++++++++++++++++++++++ insertEquipLease:::");
        int result = readOnlySqlSession.insert(NAMESAPCE + "insertEquipRegister", map);

        return result;
    }

    public Map<String, Object> selectEquipOwnerCnt(Map<String, Object> map) throws Exception {
        log.debug("+++++++++++++++++++++++++++++++++++++ selectEquipOwnerCnt:::");
        Map<String, Object> result = readOnlySqlSession.selectOne(NAMESAPCE + "selectEquipOwnerCnt", map);

        return result;
    }

    public Map<String, Object> selectEquipOwnerImage(Map<String, Object> map) throws Exception {
        log.debug("+++++++++++++++++++++++++++++++++++++ selectEquipOwnerImage:::");
        Map<String, Object> result = readOnlySqlSession.selectOne(NAMESAPCE + "selectEquipOwnerImage", map);

        return result;
    }

    public int inertEquip(Map<String, Object> map) throws Exception {
        log.debug("+++++++++++++++++++++++++++++++++++++ inertEquip:::");
        int result = readOnlySqlSession.update(NAMESAPCE + "inertEquip", map);

        return result;
    }

    public int insertEquipFiles(Map<String, Object> map) throws Exception {
        log.debug("+++++++++++++++++++++++++++++++++++++ insertEquipFiles:::");
        int result = readOnlySqlSession.update(NAMESAPCE + "insertEquipFiles", map);

        return result;
    }

    public Map<String, Object> checkChatroom(Map<String, Object> map) throws Exception {
        log.debug("+++++++++++++++++++++++++++++++++++++ checkChatroom:::");
        Map<String, Object> result = readOnlySqlSession.selectOne(NAMESAPCE + "checkChatroom", map);

        return result;
    }

    public Map<String, Object> getChatroom(Map<String, Object> map) throws Exception {
        log.debug("+++++++++++++++++++++++++++++++++++++ getChatroom:::");
        Map<String, Object> result = readOnlySqlSession.selectOne(NAMESAPCE + "getChatroom", map);

        return result;
    }

    public int insertChatroom(Map<String, Object> map) throws Exception {
        log.debug("+++++++++++++++++++++++++++++++++++++ insertChatroom:::");
        int result = readOnlySqlSession.update(NAMESAPCE + "insertChatroom", map);

        return result;
    }
}
