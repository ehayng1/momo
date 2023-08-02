package kr.co.dk.sample.api.app.company.dao;

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
public class CompanyDAO {

    static final String NAMESAPCE = "kr.co.dk.sample.api.app.company.mapper.CompanyDAO.";
    private static final Logger log = LogManager.getLogger(CompanyDAO.class);
    @Autowired
    Environment env;
    //	@Autowired
    private final SqlSession sqlSession;
    //	@Qualifier("readOnlySqlSession")
    private final SqlSessionTemplate readOnlySqlSession;
    //아래 메소드를 사용하기위해서

    //mybatis를 사용하기위해서  쿼리문을 사용하기위해서
    public CompanyDAO(SqlSession sqlSession, SqlSessionTemplate readOnlySqlSession) {
        this.sqlSession = sqlSession;
        this.readOnlySqlSession = readOnlySqlSession;
    }

    public List<Map> selectAllCompany() throws Exception {
        log.debug("+++++++++++++++++++++++++++++++++++++ selectAllCompany:::");
        List<Map> company = readOnlySqlSession.selectList(NAMESAPCE + "selectAllCompany");
//디비에서 company목록을 선택한리스트를 list자료구조를 사용함
        return company;
    }

    public Map<String, Object> selectCompanyInfo(Map<String, Object> map) throws Exception {
        log.debug("+++++++++++++++++++++++++++++++++++++ selectCompanyInfo:::");
        Map<String, Object> company = readOnlySqlSession.selectOne(NAMESAPCE + "selectCompanyInfo", map);

        return company;
    }

    public List<Map> selectCompanySiteList(Map<String, Object> map) throws Exception {
        log.debug("+++++++++++++++++++++++++++++++++++++ selectCompanySiteList:::");
        List<Map> companySiteList = readOnlySqlSession.selectList(NAMESAPCE + "selectCompanySiteList", map);
        return companySiteList;
    }

    public Map<String, Object> selectCompanyEvaluate(Map<String, Object> map) throws Exception {
        log.debug("+++++++++++++++++++++++++++++++++++++ selectCompanyEvaluate:::");
        Map<String, Object> evaluate = readOnlySqlSession.selectOne(NAMESAPCE + "selectCompanyEvaluate", map);
        return evaluate;
    }

    public List<Map> selectCompanyRatingList(Map<String, Object> map) throws Exception {
        log.debug("+++++++++++++++++++++++++++++++++++++ selectCompanyRatingList:::");
        List<Map> companyRatingList = readOnlySqlSession.selectList(NAMESAPCE + "selectCompanyRatingList", map);
        return companyRatingList;
    }

    public Map<String, Object> selectSiteInfo(Map<String, Object> map) throws Exception {
        log.debug("+++++++++++++++++++++++++++++++++++++ selectSiteInfo:::");
        Map<String, Object> detail = readOnlySqlSession.selectOne(NAMESAPCE + "selectSiteInfo", map);
        return detail;
    }

    public List<Map> selectSiteRatingList(Map<String, Object> map) throws Exception {
        log.debug("+++++++++++++++++++++++++++++++++++++ selectCompanyRatingList:::");
        List<Map> siteRatingList = readOnlySqlSession.selectList(NAMESAPCE + "selectSiteRatingList", map);
        return siteRatingList;
    }

    public Map<String, Object> selectCompanyIdx(Map<String, Object> map) throws Exception {
        log.debug("+++++++++++++++++++++++++++++++++++++ selectCompanyIdx:::");
        Map<String, Object> result = readOnlySqlSession.selectOne(NAMESAPCE + "selectCompanyIdx", map);
        return result;
    }

    public int insertSiteRating(Map<String, Object> map) throws Exception {
        log.debug("+++++++++++++++++++++++++++++++++++++ insertSiteRating:::");
        int result = readOnlySqlSession.insert(NAMESAPCE + "insertSiteRating", map);
        return result;
    }

    public int updateSiteRating(Map<String, Object> map) throws Exception {
        log.debug("+++++++++++++++++++++++++++++++++++++ updateSiteRating:::");
        int result = readOnlySqlSession.update(NAMESAPCE + "updateSiteRating", map);
        return result;
    }

    public int updateCompanyRating(Map<String, Object> map) throws Exception {
        log.debug("+++++++++++++++++++++++++++++++++++++ updateCompanyRating:::");
        int result = readOnlySqlSession.update(NAMESAPCE + "updateCompanyRating", map);
        return result;
    }

    public List<Map> selectCompanyStatisticsTotal() throws Exception {
        log.debug("+++++++++++++++++++++++++++++++++++++ selectCompanyStatisticsTotal:::");
        List<Map> statistics = readOnlySqlSession.selectList(NAMESAPCE + "selectCompanyStatisticsTotal");
        return statistics;
    }

    public List<Map> selectCompanyStatisticsSafe() throws Exception {
        log.debug("+++++++++++++++++++++++++++++++++++++ selectCompanyStatisticsSafe:::");
        List<Map> statistics = readOnlySqlSession.selectList(NAMESAPCE + "selectCompanyStatisticsSafe");
        return statistics;
    }

    public List<Map> selectCompanyStatisticsWelfare() throws Exception {
        log.debug("+++++++++++++++++++++++++++++++++++++ selectCompanyStatisticsWelfare:::");
        List<Map> statistics = readOnlySqlSession.selectList(NAMESAPCE + "selectCompanyStatisticsWelfare");
        return statistics;
    }

    public List<Map> selectCompanyStatisticsHard() throws Exception {
        log.debug("+++++++++++++++++++++++++++++++++++++ selectCompanyStatisticsHard:::");
        List<Map> statistics = readOnlySqlSession.selectList(NAMESAPCE + "selectCompanyStatisticsHard");
        return statistics;
    }

    public List<Map> selectCompanyStatisticsPayment() throws Exception {
        log.debug("+++++++++++++++++++++++++++++++++++++ selectCompanyStatisticsPayment:::");
        List<Map> statistics = readOnlySqlSession.selectList(NAMESAPCE + "selectCompanyStatisticsPayment");
        return statistics;
    }

    public List<Map> selectCompanyStatisticsMeal() throws Exception {
        log.debug("+++++++++++++++++++++++++++++++++++++ selectCompanyStatisticsMeal:::");
        List<Map> statistics = readOnlySqlSession.selectList(NAMESAPCE + "selectCompanyStatisticsMeal");
        return statistics;
    }

    public List<Map> selectCompanyStatisticsRest() throws Exception {
        log.debug("+++++++++++++++++++++++++++++++++++++ selectCompanyStatisticsRest:::");
        List<Map> statistics = readOnlySqlSession.selectList(NAMESAPCE + "selectCompanyStatisticsRest");
        return statistics;
    }

}
