package kr.co.dk.sample.api.app.job.dao;

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
public class JobDAO {

    static final String NAMESAPCE = "kr.co.dk.sample.api.app.job.mapper.JobDAO.";
    private static final Logger log = LogManager.getLogger(JobDAO.class);
    @Autowired
    Environment env;
    //	@Autowired
    private final SqlSession sqlSession;
    //	@Qualifier("readOnlySqlSession")
    private final SqlSessionTemplate readOnlySqlSession;

    public JobDAO(SqlSession sqlSession, SqlSessionTemplate readOnlySqlSession) {
        this.sqlSession = sqlSession;
        this.readOnlySqlSession = readOnlySqlSession;
    }

    public Map<String, Object> selectJobCnt(Map<String, Object> map) throws Exception {
        log.debug("+++++++++++++++++++++++++++++++++++++ selectJobCnt:::");
        Map<String, Object> result = readOnlySqlSession.selectOne(NAMESAPCE + "selectJobCnt", map);

        return result;
    }

    public List<Map> selectJobList(Map<String, Object> map) throws Exception {
        log.debug("+++++++++++++++++++++++++++++++++++++ selectJobList:::");
        List<Map> result = readOnlySqlSession.selectList(NAMESAPCE + "selectJobList", map);

        return result;
    }

    public List<Map> getWorkShare(Map<String, Object> map) throws Exception {
        log.debug("+++++++++++++++++++++++++++++++++++++ selectJobList:::");
        List<Map> result = readOnlySqlSession.selectList(NAMESAPCE + "getWorkShare", map);

        return result;
    }

    public int insertJobOffer(Map<String, Object> map) throws Exception {
        log.debug("+++++++++++++++++++++++++++++++++++++ insertJobOffer:::");
        int result = readOnlySqlSession.insert(NAMESAPCE + "insertJobOffer", map);

        return result;
    }

    public int insertJobSearch(Map<String, Object> map) throws Exception {
        log.debug("+++++++++++++++++++++++++++++++++++++ insertJobSearch:::");
        int result = readOnlySqlSession.insert(NAMESAPCE + "insertJobSearch", map);

        return result;
    }

    public int insertWorkHistory(Map<String, Object> map) throws Exception {
        log.debug("+++++++++++++++++++++++++++++++++++++ insertWorkHistory:::");
        int result = readOnlySqlSession.insert(NAMESAPCE + "insertWorkHistory", map);

        return result;
    }

    public int increaseWorkCount(Map<String, Object> map) throws Exception {
        log.debug("+++++++++++++++++++++++++++++++++++++ increaseWorkCount:::");
        int result = readOnlySqlSession.update(NAMESAPCE + "increaseWorkCount", map);

        return result;
    }

    public int inertWork(Map<String, Object> map) throws Exception {
        log.debug("+++++++++++++++++++++++++++++++++++++ inertWork:::");
        int result = readOnlySqlSession.update(NAMESAPCE + "inertWork", map);

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

    public List<Map> activeJobList(Map<String, Object> map) throws Exception {
        log.debug("+++++++++++++++++++++++++++++++++++++ activeJobList:::");
        List<Map> result = readOnlySqlSession.selectList(NAMESAPCE + "activeJobList", map);

        return result;
    }

}
