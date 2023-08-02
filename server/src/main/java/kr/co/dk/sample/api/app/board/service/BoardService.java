package kr.co.dk.sample.api.app.board.service;

import kr.co.dk.sample.api.app.board.dao.BoardDAO;
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
public class BoardService {

    private static final Logger log = LogManager.getLogger(BoardService.class);

    @Autowired
    BoardDAO boardDAO;
    @Autowired
    Environment env;
    @Qualifier("prop")
    private Properties properties;

    public Paging setBoardPaging(Map<String, Object> paramMap) throws Exception {
        Map<String, Object> map = boardDAO.selectBoardCnt(paramMap);
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

    public List<Map> selectAllBoard(Map<String, Object> map) throws Exception {
        String category = map.get("category").toString();
        switch (category) {
            case "best":
                return boardDAO.selectBoardByBest(map);
            case "new":
                return boardDAO.selectBoardByNew(map);
            case "reply":
                return boardDAO.selectBoardByReply(map);
            case "view":
                return boardDAO.selectBoardByView(map);
            default:
                return null;
        }
    }

    public int updateBoard(Map<String, Object> map) throws Exception {
        return boardDAO.updateBoard(map);
    }

    public int increaseViewCount(Map<String, Object> map) throws Exception {
        return boardDAO.increaseViewCount(map);
    }

    public Map<String, Object> selectBoardDetail(Map<String, Object> map) throws Exception {
        return boardDAO.selectBoardDetail(map);
    }

    public List<Map> selectReply(Map<String, Object> map) throws Exception {
        return boardDAO.selectReply(map);
    }

    public List<Map> selectBoardFiles(Map<String, Object> map) throws Exception {
        return boardDAO.selectBoardFiles(map);
    }

    public int insertBoard(Map<String, Object> map) throws Exception {
        return boardDAO.insertBoard(map);
    }

    public int insertFilePaths(Map<String, Object> map) throws Exception {
        return boardDAO.insertFilePaths(map);
    }

    public int insertReply(Map<String, Object> map) throws Exception {
        return boardDAO.insertReply(map);
    }

    public int deleteReply(Map<String, Object> map) throws Exception {
        return boardDAO.deleteReply(map);
    }

    public int updateReply(Map<String, Object> map) throws Exception {
        return boardDAO.updateReply(map);
    }

}
