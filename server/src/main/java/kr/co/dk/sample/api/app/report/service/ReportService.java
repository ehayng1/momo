package kr.co.dk.sample.api.app.report.service;

import kr.co.dk.sample.api.app.report.dao.ReportDAO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.core.env.Environment;

import java.util.Map;
import java.util.Properties;

@Service
public class ReportService {
    private static final Logger log = LogManager.getLogger(ReportService.class);

    @Autowired
    ReportDAO reportDAO;

    @Autowired
    Environment env;

    @Qualifier("prop")
    private Properties properties;

    public int insertReport(Map<String, Object> map) throws Exception {
        return reportDAO.insertReport(map);
    }

    public Map<String, Object> getUserIdx(Map<String, Object> map) throws Exception {
        return reportDAO.getUserIdx(map);
    }

}
