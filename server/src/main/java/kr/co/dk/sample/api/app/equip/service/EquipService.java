package kr.co.dk.sample.api.app.equip.service;

import kr.co.dk.sample.api.app.equip.dao.EquipDAO;
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
public class EquipService {
    private static final Logger log = LogManager.getLogger(EquipService.class);

    @Autowired
    EquipDAO equipDAO;
    @Autowired
    Environment env;
    @Qualifier("prop")
    private Properties properties;

    public Paging setEquipPaging(Map<String, Object> paramMap) throws Exception {
        Map<String, Object> map = equipDAO.selectEquipCnt(paramMap);
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

    public List<Map> selectEquipList(Map<String, Object> map) throws Exception {
        return equipDAO.selectEquipList(map);
    }

    public List<Map> getEquipCount(Map<String, Object> map) throws Exception {
        if (map.get("type").toString().equals("0")) {
            return equipDAO.getEquipCount(map);
        } else {
            return equipDAO.getAreaCount(map);
        }
    }

    public List<Map> selectEquipFilePaths(Map<String, Object> map) throws Exception {
        return equipDAO.selectEquipFilePaths(map);
    }

    public Map<String, Object> selectEquipDetail(Map<String, Object> map) throws Exception {
        return equipDAO.selectEquipDetail(map);
    }

    public int insertEquipHistory(Map<String, Object> map) throws Exception {
        return equipDAO.insertEquipHistory(map);
    }

    public int insertEquipRental(Map<String, Object> map) throws Exception {
        return equipDAO.insertEquipRental(map);
    }

    public int insertFilePaths(Map<String, Object> map) throws Exception {
        return equipDAO.insertFilePaths(map);
    }

    public int insertEquipLease(Map<String, Object> map) throws Exception {
        return equipDAO.insertEquipLease(map);
    }

    public int insertEquipRegister(Map<String, Object> map) throws Exception {
        return equipDAO.insertEquipRegister(map);
    }

    public Map<String, Object> selectEquipOwnerCnt(Map<String, Object> map) throws Exception {
        return equipDAO.selectEquipOwnerCnt(map);
    }

    public Map<String, Object> selectEquipOwnerImage(Map<String, Object> map) throws Exception {
        return equipDAO.selectEquipOwnerImage(map);
    }

    public int inertEquip(Map<String, Object> map) throws Exception {

        return equipDAO.inertEquip(map);
    }

    public int insertEquipFiles(Map<String, Object> map) throws Exception { // 다시올리기 했을 때 사용
        return equipDAO.insertEquipFiles(map);
    }

    public Map<String, Object> checkChatroom(Map<String, Object> map) throws Exception {
        return equipDAO.checkChatroom(map);
    }

    public Map<String, Object> getChatroom(Map<String, Object> map) throws Exception {
        return equipDAO.getChatroom(map);
    }

    public int insertChatroom(Map<String, Object> map) throws Exception {
        return equipDAO.insertChatroom(map);
    }
}
