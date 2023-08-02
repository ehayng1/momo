package kr.co.dk.sample.api.app.search.dao;

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
public class SearchDAO {

    static final String NAMESAPCE = "kr.co.dk.sample.api.app.search.mapper.SearchDAO.";
    private static final Logger log = LogManager.getLogger(SearchDAO.class);
    @Autowired
    Environment env;
    //	@Autowired
    private final SqlSession sqlSession;
    //	@Qualifier("readOnlySqlSession")
    private final SqlSessionTemplate readOnlySqlSession;

    public SearchDAO(SqlSession sqlSession, SqlSessionTemplate readOnlySqlSession) {
        this.sqlSession = sqlSession;
        this.readOnlySqlSession = readOnlySqlSession;
    }

    public List<Map> searchBySite(Map<String, Object> map) {
        log.debug("+++++++++++++++++++++++++++++++++++++ searchBySite:::");
        return readOnlySqlSession.selectList(NAMESAPCE + "searchBySite", map);
    }

    public List<Map> searchByCompany(Map<String, Object> map) {
        log.debug("+++++++++++++++++++++++++++++++++++++ searchByCompany:::");
        return readOnlySqlSession.selectList(NAMESAPCE + "searchByCompany", map);
    }

    public List<Map> searchByWork(Map<String, Object> map) {
        log.debug("+++++++++++++++++++++++++++++++++++++ searchByWork:::");
        return readOnlySqlSession.selectList(NAMESAPCE + "searchByWork", map);
    }

    public List<Map> searchByEquip(Map<String, Object> map) {
        log.debug("+++++++++++++++++++++++++++++++++++++ searchByEquip:::");
        return readOnlySqlSession.selectList(NAMESAPCE + "searchByEquip", map);
    }

}
