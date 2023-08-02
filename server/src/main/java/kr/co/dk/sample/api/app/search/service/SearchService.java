package kr.co.dk.sample.api.app.search.service;

import kr.co.dk.sample.api.app.search.dao.SearchDAO;
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
public class SearchService {

    private static final Logger log = LogManager.getLogger(SearchService.class);

    @Autowired
    SearchDAO searchDAO;
    @Autowired
    Environment env;
    @Qualifier("prop")
    private Properties properties;

    public List<Map> search(Map<String, Object> map) throws Exception {
        String category = map.get("category").toString();
        switch (category) {
            case "site":
                return searchDAO.searchBySite(map);
            case "company":
                return searchDAO.searchByCompany(map);
            case "work":
                return searchDAO.searchByWork(map);
            case "equip":
                return searchDAO.searchByEquip(map);
            default:
                return null;
        }
    }
}
