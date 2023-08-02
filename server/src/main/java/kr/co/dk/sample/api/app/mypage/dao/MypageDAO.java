package kr.co.dk.sample.api.app.mypage.dao;

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
public class MypageDAO {

    static final String NAMESAPCE = "kr.co.dk.sample.api.app.mypage.mapper.MypageDAO.";
    private static final Logger log = LogManager.getLogger(MypageDAO.class);
    @Autowired
    Environment env;
    //	@Autowired
    private final SqlSession sqlSession;
    //	@Qualifier("readOnlySqlSession")
    private final SqlSessionTemplate readOnlySqlSession;

    public MypageDAO(SqlSession sqlSession, SqlSessionTemplate readOnlySqlSession) {
        this.sqlSession = sqlSession;
        this.readOnlySqlSession = readOnlySqlSession;
    }

    public Map<String, Object> selectWorkCnt(Map<String, Object> map) throws Exception {
        log.debug("+++++++++++++++++++++++++++++++++++++ selectWorkCnt:::");
        Map<String, Object> result = readOnlySqlSession.selectOne(NAMESAPCE + "selectWorkCnt", map);

        return result;
    }

    public Map<String, Object> selectEquipCnt(Map<String, Object> map) throws Exception {
        log.debug("+++++++++++++++++++++++++++++++++++++ selectEquipCnt:::");
        Map<String, Object> result = readOnlySqlSession.selectOne(NAMESAPCE + "selectEquipCnt", map);

        return result;
    }

    public Map<String, Object> selectWorkLikeCnt(Map<String, Object> map) throws Exception {
        log.debug("+++++++++++++++++++++++++++++++++++++ selectWorkLikeCnt:::");
        Map<String, Object> result = readOnlySqlSession.selectOne(NAMESAPCE + "selectWorkLikeCnt", map);

        return result;
    }

    public Map<String, Object> selectEquipLikeCnt(Map<String, Object> map) throws Exception {
        log.debug("+++++++++++++++++++++++++++++++++++++ selectEquipLikeCnt:::");
        Map<String, Object> result = readOnlySqlSession.selectOne(NAMESAPCE + "selectEquipLikeCnt", map);

        return result;
    }

    public Map<String, Object> selectIssueCnt(Map<String, Object> map) throws Exception {
        log.debug("+++++++++++++++++++++++++++++++++++++ selectIssueCnt:::");
        Map<String, Object> result = readOnlySqlSession.selectOne(NAMESAPCE + "selectIssueCnt", map);

        return result;
    }

    public Map<String, Object> selectAcceptanceCnt(Map<String, Object> map) throws Exception {
        log.debug("+++++++++++++++++++++++++++++++++++++ selectAcceptanceCnt:::");
        Map<String, Object> result = readOnlySqlSession.selectOne(NAMESAPCE + "selectAcceptanceCnt", map);

        return result;
    }

    public int changeInsertPhoto(Map<String, Object> map) throws Exception {
        log.debug("+++++++++++++++++++++++++++++++++++++ enrollUser:::");
        int result = readOnlySqlSession.update(NAMESAPCE + "changeInsertPhoto", map);

        return result;
    }

    public Map<String, Object> selectWorkHistoryCnt(Map<String, Object> map) throws Exception {
        log.debug("+++++++++++++++++++++++++++++++++++++ selectWorkHistoryCnt:::");
        Map<String, Object> result = readOnlySqlSession.selectOne(NAMESAPCE + "selectWorkHistoryCnt", map);

        return result;
    }

    public Map<String, Object> getInfo(Map<String, Object> map) throws Exception {
        log.debug("+++++++++++++++++++++++++++++++++++++ getInfo:::");
        Map<String, Object> result = readOnlySqlSession.selectOne(NAMESAPCE + "getInfo", map);

        return result;
    }

    public List<Map> workHistory(Map<String, Object> map) throws Exception {
        log.debug("+++++++++++++++++++++++++++++++++++++ workHistory:::");
        List<Map> result = readOnlySqlSession.selectList(NAMESAPCE + "workHistory", map);

        return result;
    }

    public List<Map> myRequestEquip(Map<String, Object> map) throws Exception {
        log.debug("+++++++++++++++++++++++++++++++++++++ myRequestEquip:::");
        List<Map> result = readOnlySqlSession.selectList(NAMESAPCE + "myRequestEquip", map);

        return result;
    }

    public List<Map> myRequestWork(Map<String, Object> map) throws Exception {
        log.debug("+++++++++++++++++++++++++++++++++++++ myRequestWork:::");
        List<Map> result = readOnlySqlSession.selectList(NAMESAPCE + "myRequestWork", map);

        return result;
    }

    public List<Map> getFiles(Map<String, Object> map) throws Exception {
        log.debug("+++++++++++++++++++++++++++++++++++++ getFiles:::");
        List<Map> result = readOnlySqlSession.selectList(NAMESAPCE + "getFiles", map);

        return result;
    }

    public List<Map> likeEquipHistory(Map<String, Object> map) throws Exception {
        log.debug("+++++++++++++++++++++++++++++++++++++ likeEquipHistory:::");
        List<Map> result = readOnlySqlSession.selectList(NAMESAPCE + "likeEquipHistory", map);

        return result;
    }

    public List<Map> likeWorkHistory(Map<String, Object> map) throws Exception {
        log.debug("+++++++++++++++++++++++++++++++++++++ likeWorkHistory:::");
        List<Map> result = readOnlySqlSession.selectList(NAMESAPCE + "likeWorkHistory", map);

        return result;
    }

    public int myRequestUpdateEquip(Map<String, Object> map) throws Exception {
        log.debug("+++++++++++++++++++++++++++++++++++++ myRequestUpdateEquip:::");
        int result = readOnlySqlSession.update(NAMESAPCE + "myRequestUpdateEquip", map);

        return result;
    }

    public int myRequestUpdateWork(Map<String, Object> map) throws Exception {
        log.debug("+++++++++++++++++++++++++++++++++++++ myRequestUpdateWork:::");
        int result = readOnlySqlSession.update(NAMESAPCE + "myRequestUpdateWork", map);

        return result;
    }

    public List<Map> issueHistory(Map<String, Object> map) throws Exception {
        log.debug("+++++++++++++++++++++++++++++++++++++ issueHistory:::");
        List<Map> result = readOnlySqlSession.selectList(NAMESAPCE + "issueHistory", map);

        return result;
    }

    public int myRequestReloadEquip(Map<String, Object> map) throws Exception {
        log.debug("+++++++++++++++++++++++++++++++++++++ myRequestReloadEquip:::");
        int result = readOnlySqlSession.insert(NAMESAPCE + "myRequestReloadEquip", map);

        return result;
    }

    public int myRequestReloadWork(Map<String, Object> map) throws Exception {
        log.debug("+++++++++++++++++++++++++++++++++++++ myRequestReloadWork:::");
        int result = readOnlySqlSession.insert(NAMESAPCE + "myRequestReloadWork", map);

        return result;
    }

    public int myRequestReloadFiles(Map<String, Object> map) throws Exception {
        log.debug("+++++++++++++++++++++++++++++++++++++ myRequestReloadFiles:::");
        int result = readOnlySqlSession.insert(NAMESAPCE + "myRequestReloadFiles", map);

        return result;
    }

    public List<Map> selectAcceptanceList(Map<String, Object> map) throws Exception {
        log.debug("+++++++++++++++++++++++++++++++++++++ selectAcceptanceList:::");
        List<Map> result = readOnlySqlSession.selectList(NAMESAPCE + "selectAcceptanceList", map);

        return result;
    }

}
