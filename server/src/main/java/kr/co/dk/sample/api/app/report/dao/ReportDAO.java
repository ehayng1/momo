package kr.co.dk.sample.api.app.report.dao;

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
public class ReportDAO {
    static final String NAMESAPCE = "kr.co.dk.sample.api.app.report.mapper.ReportDAO.";
    private static final Logger log = LogManager.getLogger(ReportDAO.class);
    @Autowired
    Environment env;
    //	@Autowired
    private final SqlSession sqlSession;
    //	@Qualifier("readOnlySqlSession")
    private final SqlSessionTemplate readOnlySqlSession;

    public ReportDAO(SqlSession sqlSession, SqlSessionTemplate readOnlySqlSession) {
        this.sqlSession = sqlSession;
        this.readOnlySqlSession = readOnlySqlSession;
    }

    public int insertReport(Map<String, Object> map) throws Exception {
        log.debug("+++++++++++++++++++++++++++++++++++++ insertReport:::");
        return readOnlySqlSession.insert(NAMESAPCE + "insertReport", map);
    }

    public Map<String, Object> getUserIdx(Map<String, Object> map) throws Exception {
        log.debug("+++++++++++++++++++++++++++++++++++++ getUserIdx:::");
        return readOnlySqlSession.selectOne(NAMESAPCE + "getUserIdx", map);
    }
}
