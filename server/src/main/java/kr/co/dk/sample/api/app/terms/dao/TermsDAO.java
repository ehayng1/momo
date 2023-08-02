package kr.co.dk.sample.api.app.terms.dao;

import org.apache.ibatis.session.SqlSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public class TermsDAO {

    static final String NAMESAPCE = "kr.co.dk.sample.api.app.terms.mapper.TermsDAO.";
    private static final Logger log = LogManager.getLogger(TermsDAO.class);
    @Autowired
    Environment env;
    //	@Autowired
    private final SqlSession sqlSession;
    //	@Qualifier("readOnlySqlSession")
    private final SqlSessionTemplate readOnlySqlSession;

    public TermsDAO(SqlSession sqlSession, SqlSessionTemplate readOnlySqlSession) {
        this.sqlSession = sqlSession;
        this.readOnlySqlSession = readOnlySqlSession;
    }

    public Map<String, Object> getTermsDetail(Map<String, Object> map) throws Exception {
        log.debug("+++++++++++++++++++++++++++++++++++++ getTermsDetail:::");
        Map<String, Object> result = readOnlySqlSession.selectOne(NAMESAPCE + "getTermsDetail", map);

        return result;
    }

}
