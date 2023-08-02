package kr.co.dk.sample.api.app.job.service;

import kr.co.dk.sample.api.app.job.dao.JobDAO;
import kr.co.dk.sample.api.common.model.Paging;
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
public class JobService {

    private static final Logger log = LogManager.getLogger(JobService.class);

    @Autowired
    JobDAO jobDAO;
    @Autowired
    Environment env;
    @Qualifier("prop")
    private Properties properties;

    public Paging setJobPaging(Map<String, Object> paramMap) throws Exception {
        Map<String, Object> map = jobDAO.selectJobCnt(paramMap);
        int totalCount = ((Long) map.get("cnt")).intValue();
        int pageNo = 1;
        int pageSize = 10;

        Paging paging = new Paging();

        try {

            if (paramMap.get("page_size") != null) {
                String sPageSize = (String) paramMap.get("page_size");
                try {
                    pageSize = Integer.parseInt(sPageSize);
                } catch (Exception e) {
                }
            }

            if (paramMap.get("pageNo") != null) {
                String sPageNo = (String) paramMap.get("pageNo");
                try {
                    pageNo = Integer.parseInt(sPageNo);
                } catch (Exception e) {
                }
            }

            paging.setPageNo(pageNo);
            paging.setPageSize(pageSize);
            paging.setTotalCount(totalCount);
        } catch (Exception e) {
            throw e;
        }

        paramMap.put("start_idx", paging.getStartIndex());
        paramMap.put("page_size", paging.getPageSize());

        return paging;
    }

    public List<Map> selectJobList(Map<String, Object> map) throws Exception {
        return jobDAO.selectJobList(map);
    }

    public List<Map> getWorkShare(Map<String, Object> map) throws Exception {
        return jobDAO.getWorkShare(map);
    }

    public int insertJobOffer(Map<String, Object> map) throws Exception {
        return jobDAO.insertJobOffer(map);
    }

    public int insertJobSearch(Map<String, Object> map) throws Exception {
        return jobDAO.insertJobSearch(map);
    }

    public int insertWorkHistory(Map<String, Object> map) throws Exception {
        return jobDAO.insertWorkHistory(map);
    }

    public int increaseWorkCount(Map<String, Object> map) throws Exception {
        return jobDAO.increaseWorkCount(map);
    }

    public int inertWork(Map<String, Object> map) throws Exception {
        return jobDAO.inertWork(map);
    }

    public Map<String, Object> checkChatroom(Map<String, Object> map) throws Exception {
        return jobDAO.checkChatroom(map);
    }

    public Map<String, Object> getChatroom(Map<String, Object> map) throws Exception {
        return jobDAO.getChatroom(map);
    }

    public int insertChatroom(Map<String, Object> map) throws Exception {
        return jobDAO.insertChatroom(map);
    }

    public List<Map> activeJobList(Map<String, Object> map) throws Exception {
        return jobDAO.activeJobList(map);
    }
}
