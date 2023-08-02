package kr.co.dk.sample.api.app.labor.service;

import kr.co.dk.sample.api.app.labor.dao.LaborDAO;
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
public class LaborService {

    private static final Logger log = LogManager.getLogger(LaborService.class);

    @Autowired
    LaborDAO laborDAO;
    @Autowired
    Environment env;
    @Qualifier("prop")
    private Properties properties;

    public Paging setLaborPaging(Map<String, Object> paramMap) throws Exception {
        Map<String, Object> map = laborDAO.selectLaborCnt(paramMap);
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

    public List<Map> getTypeList() throws Exception {
        return laborDAO.getTypeList();
    }

    public List<Map> selectLaborList(Map<String, Object> map) throws Exception {
        return laborDAO.selectLaborList(map);
    }

    public int insertLaborInfo(Map<String, Object> map) throws Exception {
        return laborDAO.insertLaborInfo(map);
    }

    public Map<String, Object> selectLaborDetail(Map<String, Object> map) throws Exception {
        return laborDAO.selectLaborDetail(map);
    }
}
