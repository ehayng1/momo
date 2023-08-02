package kr.co.dk.sample.api.app.user.dao;

import org.apache.ibatis.session.SqlSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public class UserDAO {

    static final String NAMESAPCE = "kr.co.dk.sample.api.app.user.dao.";
    private static final Logger log = LogManager.getLogger(UserDAO.class);
    @Autowired
    Environment env;
    //	@Autowired
    private final SqlSession sqlSession;
    //	@Qualifier("readOnlySqlSession")
    private final SqlSessionTemplate readOnlySqlSession;

    public UserDAO(SqlSession sqlSession, SqlSessionTemplate readOnlySqlSession) {
        this.sqlSession = sqlSession;
        this.readOnlySqlSession = readOnlySqlSession;
    }

    public Map<String, Object> selectAllUser() throws Exception {
        log.debug("+++++++++++++++++++++++++++++++++++++ selectMemberInfo:::");
        Map<String, Object> user = readOnlySqlSession.selectOne(NAMESAPCE + "selectAllUser");

        return user;
    }

    public Map<String, Object> selectUserInfo(Map<String, Object> map) throws Exception {
        log.debug("+++++++++++++++++++++++++++++++++++++ selectMemberInfo:::");
        Map<String, Object> user = readOnlySqlSession.selectOne(NAMESAPCE + "selectUserInfo", map);

        return user;
    }


}
