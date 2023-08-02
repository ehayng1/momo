package kr.co.dk.sample.api.app.alarm.dao;

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
public class AlarmDAO {
    static final String NAMESAPCE = "kr.co.dk.sample.api.app.alarm.mapper.AlarmDAO.";
    private static final Logger log = LogManager.getLogger(AlarmDAO.class);
    @Autowired
    Environment env;
    //	@Autowired
    private final SqlSession sqlSession;
    //	@Qualifier("readOnlySqlSession")
    private final SqlSessionTemplate readOnlySqlSession;

    public AlarmDAO(SqlSession sqlSession, SqlSessionTemplate readOnlySqlSession) {
        this.sqlSession = sqlSession;
        this.readOnlySqlSession = readOnlySqlSession;
    }

    public List<Map> selectAlarmList(Map<String, Object> map) throws Exception {
        log.debug("+++++++++++++++++++++++++++++++++++++ selectAlarmList:::");
        List<Map> alarmList = readOnlySqlSession.selectList(NAMESAPCE + "selectAlarmList", map);
        return alarmList;
    }

    public int insertPush(Map<String, Object> map) throws Exception {
        log.debug("+++++++++++++++++++++++++++++++++++++ insertPush:::");
        int result = readOnlySqlSession.insert(NAMESAPCE + "insertPush", map);
        return result;
    }

    public List<Map> getFcmList(Map<String, Object> map) throws Exception {
        log.debug("+++++++++++++++++++++++++++++++++++++ getFcmList:::");
        List<Map> result = readOnlySqlSession.selectList(NAMESAPCE + "getFcmList", map);
        return result;
    }
}
