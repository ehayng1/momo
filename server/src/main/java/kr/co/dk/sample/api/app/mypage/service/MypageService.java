package kr.co.dk.sample.api.app.mypage.service;

import kr.co.dk.sample.api.app.mypage.dao.MypageDAO;
import kr.co.dk.sample.api.common.model.Paging;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

@Service
public class MypageService {

    private static final Logger log = LogManager.getLogger(MypageService.class);

    @Autowired
    MypageDAO mypageDAO;
    @Autowired
    Environment env;
    @Qualifier("prop")
    private Properties properties;

    public Paging setWorkPaging(Map<String, Object> paramMap) throws Exception {
        Map<String, Object> map = mypageDAO.selectWorkCnt(paramMap);
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

    public Paging setMyRequestPaging(Map<String, Object> paramMap) throws Exception {
        Map<String, Object> map = new HashMap<>();
        if (paramMap.get("type").toString() == "2") {
            // 장비-임대만
            map = mypageDAO.selectEquipCnt(paramMap);
        } else {
            map = mypageDAO.selectWorkHistoryCnt(paramMap);
        }
        int totalCount = Integer.parseInt(String.valueOf(map.get("cnt")));
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

    public Paging setLikePaging(Map<String, Object> paramMap) throws Exception {
        Map<String, Object> map = new HashMap<>();
        if (paramMap.get("type").toString() == "2") {
            // 장비-임대만
            map = mypageDAO.selectEquipLikeCnt(paramMap);
        } else {
            map = mypageDAO.selectWorkLikeCnt(paramMap);
        }
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

    public Paging setIssuePaging(Map<String, Object> paramMap) throws Exception {
        Map<String, Object> map = mypageDAO.selectIssueCnt(paramMap);
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

    public Paging setAcceptancePaging(Map<String, Object> paramMap) throws Exception {
        Map<String, Object> map = mypageDAO.selectAcceptanceCnt(paramMap);
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

    public int changeInsertPhoto(Map<String, Object> map) throws Exception {
        return mypageDAO.changeInsertPhoto(map);
    }

    public Map<String, Object> getInfo(Map<String, Object> map) throws Exception {
        Object path = map.get("user_image");
        log.info(path);
        System.out.println(path);

        return mypageDAO.getInfo(map);
    }

    public List<Map> workHistory(Map<String, Object> map) throws Exception {
        return mypageDAO.workHistory(map);
    }

    public List<Map> myRequest(Map<String, Object> map) throws Exception {
        if (map.get("type").toString().equals("2")) {
            return mypageDAO.myRequestEquip(map);
        } else {
            return mypageDAO.myRequestWork(map);
        }
    }

    public List<Map> getFiles(Map<String, Object> map) throws Exception {
        return mypageDAO.getFiles(map);
    }

    public List<Map> likeHistory(Map<String, Object> map) throws Exception {
        if (map.get("type").toString().equals("2")) {
            return mypageDAO.likeEquipHistory(map);
        } else {
            return mypageDAO.likeWorkHistory(map);
        }
    }

    public int myRequestUpdate(Map<String, Object> map) throws Exception {
        if (map.get("type").toString().equals("2")) {
            return mypageDAO.myRequestUpdateEquip(map);
        } else {
            return mypageDAO.myRequestUpdateWork(map);
        }
    }

    public List<Map> issueHistory(Map<String, Object> map) throws Exception {
        return mypageDAO.issueHistory(map);
    }

    public int myRequestReload(Map<String, Object> map) throws Exception {
        if (map.get("type").toString().equals("2")) {
            int result1 =  mypageDAO.myRequestReloadEquip(map);
            int result2 = mypageDAO.myRequestReloadFiles(map);
            return result1 * result2;
        } else {
            return mypageDAO.myRequestReloadWork(map);
        }
    }

    public List<Map> selectAcceptanceList(Map<String, Object> map) throws Exception {
        return mypageDAO.selectAcceptanceList(map);
    }

}
