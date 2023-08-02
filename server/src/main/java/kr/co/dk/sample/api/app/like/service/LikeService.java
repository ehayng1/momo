package kr.co.dk.sample.api.app.like.service;

import kr.co.dk.sample.api.app.job.dao.JobDAO;
import kr.co.dk.sample.api.app.job.service.JobService;
import kr.co.dk.sample.api.app.like.dao.LikeDAO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Properties;

@Service
public class LikeService {

    private static final Logger log = LogManager.getLogger(LikeService.class);

    @Autowired
    LikeDAO likeDAO;
    @Autowired

    Environment env;

    @Qualifier("prop")
    private Properties properties;

    public int addLike(Map<String, Object> map) throws Exception {
        return likeDAO.addLike(map);
    }

    public int toggleLike(Map<String, Object> map) throws Exception {
        return likeDAO.toggleLike(map);
    }
}
