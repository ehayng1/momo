package kr.co.dk.sample.api.app.terms.service;

import kr.co.dk.sample.api.app.terms.dao.TermsDAO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Properties;

@Service
public class TermsService {

    private static final Logger log = LogManager.getLogger(TermsService.class);

    @Autowired
    TermsDAO termsDAO;
    @Autowired
    Environment env;
    @Qualifier("prop")
    private Properties properties;

    public Map<String, Object> getTermsDetail(Map<String, Object> map) throws Exception {
        return termsDAO.getTermsDetail(map);
    }
}
