package kr.co.dk.sample.api.app.board.dao;

import org.apache.ibatis.session.SqlSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class BoardDAO {

    static final String NAMESAPCE = "kr.co.dk.sample.api.app.board.mapper.BoardDAO.";
    private static final Logger log = LogManager.getLogger(BoardDAO.class);
    @Autowired
    Environment env;
    //	@Autowired
    private final SqlSession sqlSession;
    //	@Qualifier("readOnlySqlSession")
    private final SqlSessionTemplate readOnlySqlSession;

    public BoardDAO(SqlSession sqlSession, SqlSessionTemplate readOnlySqlSession) {
        this.sqlSession = sqlSession;
        this.readOnlySqlSession = readOnlySqlSession;
    }

    public Map<String, Object> selectBoardCnt(Map<String, Object> map) throws Exception {
        log.debug("+++++++++++++++++++++++++++++++++++++ selectBoardCnt:::");
        Map<String, Object> result = readOnlySqlSession.selectOne(NAMESAPCE + "selectBoardCnt", map);

        return result;
    }

    public List<Map> selectBoardByBest(Map<String, Object> map) throws Exception {
        log.debug("+++++++++++++++++++++++++++++++++++++ selectBoardByBest:::");
        List<Map> board = readOnlySqlSession.selectList(NAMESAPCE + "selectBoardByBest", map);

        return board;
    }

    public List<Map> selectBoardByNew(Map<String, Object> map) throws Exception {
        log.debug("+++++++++++++++++++++++++++++++++++++ selectBoardByNew:::");
        List<Map> board = readOnlySqlSession.selectList(NAMESAPCE + "selectBoardByNew", map);

        return board;
    }

    public List<Map> selectBoardByReply(Map<String, Object> map) throws Exception {
        log.debug("+++++++++++++++++++++++++++++++++++++ selectBoardByReply:::");
        List<Map> board = readOnlySqlSession.selectList(NAMESAPCE + "selectBoardByReply", map);

        return board;
    }

    public List<Map> selectBoardByView(Map<String, Object> map) throws Exception {
        log.debug("+++++++++++++++++++++++++++++++++++++ selectBoardByView:::");
        List<Map> board = readOnlySqlSession.selectList(NAMESAPCE + "selectBoardByView", map);

        return board;
    }

    public int updateBoard(Map<String, Object> map) throws Exception {
        log.debug("+++++++++++++++++++++++++++++++++++++ updateBoard:::");
        int result = readOnlySqlSession.update(NAMESAPCE + "updateBoard", map);

        return result;
    }

    public int increaseViewCount(Map<String, Object> map) throws Exception {
        log.debug("+++++++++++++++++++++++++++++++++++++ increaseViewCount:::");
        int result = readOnlySqlSession.update(NAMESAPCE + "increaseViewCount", map);

        return result;
    }

    public Map<String, Object> selectBoardDetail(Map<String, Object> map) throws Exception {
        log.debug("+++++++++++++++++++++++++++++++++++++ selectBoardDetail:::");
        Map<String, Object> result = readOnlySqlSession.selectOne(NAMESAPCE + "selectBoardDetail", map);

        return result;
    }

    public List<Map> selectReply(Map<String, Object> map) throws Exception {
        log.debug("+++++++++++++++++++++++++++++++++++++ selectReply:::");
        List<Map> result = readOnlySqlSession.selectList(NAMESAPCE + "selectReply", map);

        return result;
    }

    public List<Map> selectBoardFiles(Map<String, Object> map) throws Exception {
        log.debug("+++++++++++++++++++++++++++++++++++++ selectBoardFiles:::");
        List<Map> result = readOnlySqlSession.selectList(NAMESAPCE + "selectBoardFiles", map);

        return result;
    }

    public int insertBoard(Map<String, Object> map) throws Exception {
        log.debug("+++++++++++++++++++++++++++++++++++++ insertBoard:::");
        int result = readOnlySqlSession.insert(NAMESAPCE + "insertBoard", map);

        return result;
    }

    public int insertFilePaths(Map<String, Object> map) throws Exception {
        log.debug("+++++++++++++++++++++++++++++++++++++ insertFilePaths:::");
        int result = readOnlySqlSession.insert(NAMESAPCE + "insertFilePaths", map);

        return result;
    }

    public int insertReply(Map<String, Object> map) throws Exception {
        log.debug("+++++++++++++++++++++++++++++++++++++ insertReply:::");
        int result = readOnlySqlSession.insert(NAMESAPCE + "insertReply", map);

        return result;
    }

    public int deleteReply(Map<String, Object> map) throws Exception {
        log.debug("+++++++++++++++++++++++++++++++++++++ deleteReply:::");
        int result = readOnlySqlSession.update(NAMESAPCE + "deleteReply", map);

        return result;
    }

    public int updateReply(Map<String, Object> map) throws Exception {
        log.debug("+++++++++++++++++++++++++++++++++++++ updateReply:::");
        int result = readOnlySqlSession.update(NAMESAPCE + "updateReply", map);

        return result;
    }

}
