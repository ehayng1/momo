package kr.co.dk.sample.api.app.banner.service;

import kr.co.dk.sample.api.app.banner.dao.BannerDAO;
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
public class BannerService {

    private static final Logger log = LogManager.getLogger(BannerService.class);

    @Autowired
    BannerDAO bannerDAO;
    @Autowired
    Environment env;
    @Qualifier("prop")
    private Properties properties;

    public List<Map> getBannerList() throws Exception {
        return bannerDAO.getBannerList();
    }

}
