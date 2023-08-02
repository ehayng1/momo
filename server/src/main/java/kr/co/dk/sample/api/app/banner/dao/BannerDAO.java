package kr.co.dk.sample.api.app.banner.dao;

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
public class BannerDAO {

    static final String NAMESAPCE = "kr.co.dk.sample.api.app.banner.mapper.BannerDAO.";
    private static final Logger log = LogManager.getLogger(BannerDAO.class);
    @Autowired
    Environment env;
    //	@Autowired
    private final SqlSession sqlSession;
    //	@Qualifier("readOnlySqlSession")
    private final SqlSessionTemplate readOnlySqlSession;

    public BannerDAO(SqlSession sqlSession, SqlSessionTemplate readOnlySqlSession) {
        this.sqlSession = sqlSession;
        this.readOnlySqlSession = readOnlySqlSession;
    }

    public List<Map> getBannerList() throws Exception {
        log.debug("+++++++++++++++++++++++++++++++++++++ getBannerList:::");
        List<Map> result = readOnlySqlSession.selectList(NAMESAPCE + "getBannerList");

        return result;
    }

}
