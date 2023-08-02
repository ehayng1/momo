package kr.co.dk.sample.api.app.like.dao;

import kr.co.dk.sample.api.app.job.dao.JobDAO;
import org.apache.ibatis.session.SqlSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Repository;
import java.util.Map;

@Repository
public class LikeDAO {

    static final String NAMESAPCE = "kr.co.dk.sample.api.app.like.mapper.LikeDAO.";
    private static final Logger log = LogManager.getLogger(LikeDAO.class);
    @Autowired
    Environment env;
    //	@Autowired
    private final SqlSession sqlSession;
    //	@Qualifier("readOnlySqlSession")
    private final SqlSessionTemplate readOnlySqlSession;

    public LikeDAO(SqlSession sqlSession, SqlSessionTemplate readOnlySqlSession) {
        this.sqlSession = sqlSession;
        this.readOnlySqlSession = readOnlySqlSession;
    }

    public int addLike(Map<String, Object> map) throws Exception {
        log.debug("+++++++++++++++++++++++++++++++++++++ addLike:::");
        int result = readOnlySqlSession.insert(NAMESAPCE + "addLike", map);

        return result;
    }

    public int toggleLike(Map<String, Object> map) throws Exception {
        log.debug("+++++++++++++++++++++++++++++++++++++ toggleLike:::");
        int result = readOnlySqlSession.update(NAMESAPCE + "toggleLike", map);

        return result;
    }
}
