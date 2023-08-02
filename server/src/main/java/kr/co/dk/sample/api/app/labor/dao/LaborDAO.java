package kr.co.dk.sample.api.app.labor.dao;

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
public class LaborDAO {

    static final String NAMESAPCE = "kr.co.dk.sample.api.app.labor.mapper.LaborDAO.";
    private static final Logger log = LogManager.getLogger(LaborDAO.class);
    @Autowired
    Environment env;
    //	@Autowired
    private final SqlSession sqlSession;
    //	@Qualifier("readOnlySqlSession")
    private final SqlSessionTemplate readOnlySqlSession;

    public LaborDAO(SqlSession sqlSession, SqlSessionTemplate readOnlySqlSession) {
        this.sqlSession = sqlSession;
        this.readOnlySqlSession = readOnlySqlSession;
    }

    public Map<String, Object> selectLaborCnt(Map<String, Object> map) throws Exception {
        log.debug("+++++++++++++++++++++++++++++++++++++ selectLaborCnt:::");
        Map<String, Object> result = readOnlySqlSession.selectOne(NAMESAPCE + "selectLaborCnt", map);

        return result;
    }

    public List<Map> getTypeList() throws Exception {
        log.debug("+++++++++++++++++++++++++++++++++++++ getTypeList:::");
        List<Map> result = readOnlySqlSession.selectList(NAMESAPCE + "getTypeList");
        return result;
    }

    public List<Map> selectLaborList(Map<String, Object> map) throws Exception {
        log.debug("+++++++++++++++++++++++++++++++++++++ selectLaborList:::");
        List<Map> laborList = readOnlySqlSession.selectList(NAMESAPCE + "selectLaborList", map);
        return laborList;
    }

    public int insertLaborInfo(Map<String, Object> map) throws Exception {
        log.debug("+++++++++++++++++++++++++++++++++++++ insertEquipLease:::");
        int result = readOnlySqlSession.insert(NAMESAPCE + "insertLaborInfo", map);

        return result;
    }

    public Map<String, Object> selectLaborDetail(Map<String, Object> map) throws Exception {
        log.debug("+++++++++++++++++++++++++++++++++++++ selectLaborDetail:::");
        Map<String, Object> result = readOnlySqlSession.selectOne(NAMESAPCE + "selectLaborDetail", map);

        return result;
    }
}
