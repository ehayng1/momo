package kr.co.dk.sample.api.app.company.service;

import kr.co.dk.sample.api.app.company.dao.CompanyDAO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Properties;

@Service
public class CompanyService {

    private static final Logger log = LogManager.getLogger(CompanyService.class);

    @Autowired
    CompanyDAO companyDAO;
    @Autowired
    Environment env;
    @Qualifier("prop")
    //이걸 사용하는 이유는 뭘까
    private Properties properties;

    public List<Map> selectAllCompany() throws Exception {
        return companyDAO.selectAllCompany();
    }

    public Map<String, Object> selectCompanyInfo(Map<String, Object> map) throws Exception {
        return companyDAO.selectCompanyInfo(map);
    }

    public List<Map> selectCompanySiteList(Map<String, Object> map) throws Exception {
        return companyDAO.selectCompanySiteList(map);
    }

    public Map<String, Object> selectCompanyEvaluate(Map<String, Object> map) throws Exception {
        return companyDAO.selectCompanyEvaluate(map);
    }

    public List<Map> selectCompanyRatingList(Map<String, Object> map) throws Exception {
        return companyDAO.selectCompanyRatingList(map);
    }

    public Map<String, Object> selectCompanyIdx(Map<String, Object> map) throws Exception {
        return companyDAO.selectCompanyIdx(map);
    }

    public int insertSiteRating(Map<String, Object> map) throws Exception {
        int result1 = companyDAO.insertSiteRating(map);
        int result2 = companyDAO.updateSiteRating(map);
        int result3 = companyDAO.updateCompanyRating(map);

        return result1 * result2 * result3;
    }

    public Map<String, Object> selectSiteInfo(Map<String, Object> map) throws Exception {
        return companyDAO.selectSiteInfo(map);
    }

    public List<Map> selectSiteRatingList(Map<String, Object> map) throws Exception {
        return companyDAO.selectSiteRatingList(map);
    }

//    public int insertSiteRating(Map<String, Object> map) throws Exception {
//
//    }

    public List<Map> selectCompanyStatistics(Map<String, Object> map) throws Exception {
        String category = map.get("category").toString();

        switch (category) {
            case "safe":
                return companyDAO.selectCompanyStatisticsSafe();
            case "welfare":
                return companyDAO.selectCompanyStatisticsWelfare();
            case "hard":
                return companyDAO.selectCompanyStatisticsHard();
            case "payment":
                return companyDAO.selectCompanyStatisticsPayment();
            case "meal":
                return companyDAO.selectCompanyStatisticsMeal();
            case "rest":
                return companyDAO.selectCompanyStatisticsRest();
            case "total":
                return companyDAO.selectCompanyStatisticsTotal();
            default:
                return null;
        }
//        return companyDAO.selectCompanyStatistics();
    }
}
