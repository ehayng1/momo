package kr.co.dk.sample.api.app.auth.dao;


import kr.co.dk.sample.api.common.model.ErrorCode;
import kr.co.dk.sample.api.common.model.exception.CustomException;
import kr.co.dk.sample.api.config.security.dto.Authority;
import kr.co.dk.sample.api.config.security.dto.User;
import kr.co.dk.sample.api.config.security.role.UserPrincipal;
import kr.co.dk.sample.api.util.ModelUtil;
import org.apache.ibatis.session.SqlSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class AuthDAO implements UserDetailsService {

    static final String NAMESAPCE = "kr.co.dk.sample.api.app.auth.mapper.AuthDAO.";
    private static final Logger log = LogManager.getLogger(AuthDAO.class);
    //	@Autowired
    private final SqlSession sqlSession;

    //	@Qualifier("readOnlySqlSession")
    private final SqlSessionTemplate readOnlySqlSession;

    public AuthDAO(SqlSession sqlSession, SqlSessionTemplate readOnlySqlSession) {
        this.sqlSession = sqlSession;
        this.readOnlySqlSession = readOnlySqlSession;
    }

    public Map<String, Object> getVersion() throws Exception {
        log.debug("+++++++++++++++++++++++++++++++++++++ getVersion:::");
        Map<String, Object> result = readOnlySqlSession.selectOne(NAMESAPCE + "getVersion");

        return result;
    }

    public int updateVersion(Map<String, Object> map) throws Exception {
        log.debug("+++++++++++++++++++++++++++++++++++++ updateVersion:::");
        int result = readOnlySqlSession.update(NAMESAPCE + "updateVersion", map);

        return result;
    }

    public Map<String, Object> checkUserExist(Map<String, Object> map) throws Exception {
        log.debug("+++++++++++++++++++++++++++++++++++++ checkUserExist:::");
        Map<String, Object> result = readOnlySqlSession.selectOne(NAMESAPCE + "checkUserExist", map);

        return result;
    }

    public Map<String, Object> getInfoByPhone(Map<String, Object> map) throws Exception {
        log.debug("+++++++++++++++++++++++++++++++++++++ getInfoByPhone:::");
        Map<String, Object> result = readOnlySqlSession.selectOne(NAMESAPCE + "getInfoByPhone", map);

        return result;
    }

    public int insertPhoneAuthentication(Map<String, Object> map) throws Exception {
        log.debug("+++++++++++++++++++++++++++++++++++++ insertPhoneAuthentication:::");
        int result = readOnlySqlSession.insert(NAMESAPCE + "insertPhoneAuthentication", map);

        return result;
    }

    public Map<String, Object> compareCode(Map<String, Object> map) throws Exception {
        log.debug("+++++++++++++++++++++++++++++++++++++ compareCode:::");
        Map<String, Object> result = readOnlySqlSession.selectOne(NAMESAPCE + "compareCode", map);

        return result;
    }

    public int increaseAuthCount(Map<String, Object> map) throws Exception {
        log.debug("+++++++++++++++++++++++++++++++++++++ increaseAuthCount:::");
        int result = readOnlySqlSession.update(NAMESAPCE + "increaseAuthCount", map);

        return result;
    }

    public Map<String, Object> selectMember(Map<String, Object> map) throws Exception {
        log.debug("+++++++++++++++++++++++++++++++++++++ selectMember:::");
        Map<String, Object> result = readOnlySqlSession.selectOne(NAMESAPCE + "selectMember", map);

        return result;
    }

    public int updateMemberWithBlockchain(Map<String, Object> map) throws Exception {
        log.debug("+++++++++++++++++++++++++++++++++++++ updateMemberWithBlockchain:::");
        int result = readOnlySqlSession.update(NAMESAPCE + "updateMemberWithBlockchain", map);

        return result;
    }

    public int updateMember(Map<String, Object> map) throws Exception {
        log.debug("+++++++++++++++++++++++++++++++++++++ updateMember:::");
        int result = readOnlySqlSession.update(NAMESAPCE + "updateMember", map);

        return result;
    }

    public int insertWallet(Map<String, Object> map) throws Exception {
        log.debug("+++++++++++++++++++++++++++++++++++++ insertWallet:::");
        int result = readOnlySqlSession.update(NAMESAPCE + "insertWallet", map);

        return result;
    }

    public int enrollUser(Map<String, Object> map) throws Exception {
        log.debug("+++++++++++++++++++++++++++++++++++++ enrollUser:::");
        int result = readOnlySqlSession.update(NAMESAPCE + "enrollUser", map);

        return result;
    }

    public Map<String, Object> getNicknameCount(Map<String, Object> map) throws Exception {
        log.debug("+++++++++++++++++++++++++++++++++++++ getNicknameCount:::");
        Map<String, Object> result = readOnlySqlSession.selectOne(NAMESAPCE + "getNicknameCount", map);

        return result;
    }

    public int insertNickName(Map<String, Object> map) throws Exception {
        log.debug("+++++++++++++++++++++++++++++++++++++ enrollUser:::");
        int result = readOnlySqlSession.update(NAMESAPCE + "insertNickName", map);

        return result;
    }

    public int insertLabor(Map<String, Object> map) throws Exception {
        log.debug("+++++++++++++++++++++++++++++++++++++ addSignUp:::");
        int result = readOnlySqlSession.insert(NAMESAPCE + "insertLabor", map);

        return result;
    }

    public int addSignUp(Map<String, Object> map) throws Exception {
        log.debug("+++++++++++++++++++++++++++++++++++++ addSignUp:::");
        int result = readOnlySqlSession.insert(NAMESAPCE + "addSignUp", map);

        return result;
    }

    public int insertPhoto(Map<String, Object> map) throws Exception {
        log.debug("+++++++++++++++++++++++++++++++++++++ enrollUser:::");
        int result = readOnlySqlSession.update(NAMESAPCE + "insertPhoto", map);

        return result;
    }

    public Map<String, Object> selectValidateNickName(Map<String, Object> map) throws Exception {
        log.debug("+++++++++++++++++++++++++++++++++++++ enrollUser:::");
        Map<String, Object> result = readOnlySqlSession.selectOne(NAMESAPCE + "selectValidateNickName", map);

        return result;
    }

    //    public Map<String, Object> selectMember(Map<String, Object> map) throws Exception{
//        log.debug("+++++++++++++++ selectMember  :::: ");
//
//        log.debug("map ::: " + map);
//
//        String mb_id= SecurityUtil.encryptKSign("member_", "mb_id", (String)map.get("mb_id"));
//        map.put("mb_id", mb_id);
////		log.debug("map ::: "+map);
//        Map<String,Object> member = (Map<String, Object>) readOnlySqlSession.selectOne(NAMESAPCE + "selectMember", map);
////		log.debug("map ::: "+map);
//        if(member!=null) {
//            log.debug("2. 확인");
//            mb_id= SecurityUtil.decryptKSign("member_", "mb_id", (String)member.get("mb_id"));
//            member.put("mb_id", mb_id);
//        }
//
//        return member;
//    }
//
//    public Map<String, Object> selectSignin(Map<String, Object> map) throws Exception{
//        log.debug("+++++++++++++++ selectSignin  :::: ");
//        log.debug("selectSignin  :::: "+map);
//
//        Map<String, Object> dataMap = new HashMap<>();
//        dataMap.putAll(map);
//        dataMap.put("login_id",SecurityUtil.encryptKSign("member_", "mb_id", (String) map.get("login_id")));
//
//        String login_password=(String)map.get("login_password");
//        String password_salt=(String)map.get("password_salt");
//        dataMap.put("login_password", SecurityUtil.makePasswordHash(login_password, password_salt));
//
//        log.debug("확인 login_password  :::: "+login_password);
//        log.debug("확인 map:::"+map);
//        Map<String,Object> member = readOnlySqlSession.selectOne(NAMESAPCE + "selectSignin", dataMap);
//        log.debug("확인 member:::"+member);
//
//        if(member!=null) {
//            member.put("mb_id",SecurityUtil.decryptKSign("member_","mb_id", member.get("mb_id").toString()));
//        }
//        return member;
//    }
//
//    public Map<String, Object> selectfromIP(Map<String, Object> map) throws Exception{
//        return readOnlySqlSession.selectOne(NAMESAPCE + "selectfromIP", map);
//    }
//
//    public int updateMemberActive(Map<String, Object> map) {
//        return sqlSession.update(NAMESAPCE + "updateMemberActive", map);
//    }
//
//    public int insertMemberDetailHistory(Map<String, Object> map)  throws Exception {
//        map.put("memo",SecurityUtil.encryptKSign("member_detail", "memo", (String) map.get("memo")));
//        return sqlSession.insert(NAMESAPCE + "insertMemberDetailHistory", map);
//    }
//
//    public Object insertActivity(Map<String, Object> map) throws Exception{
//        return sqlSession.insert(NAMESAPCE + "insertActivity", map);
//    }
//
//    public Object updateLoginDate(Map<String, Object> map) throws Exception{
//        return sqlSession.update(NAMESAPCE + "updateLoginDate", map);
//    }
//
//    public Map<String, Object> selectMemberSessionCnt(Map<String, Object> map) {
//        return readOnlySqlSession.selectOne(NAMESAPCE + "selectMemberSessionCnt", map);
//    }
//
//    public int insertMemberSession(Map<String, Object> map) {
//        return sqlSession.insert(NAMESAPCE + "insertMemberSession", map);
//    }
//
//    public Object deleteMemberSession(Map<String, Object> map) throws Exception{
//        return sqlSession.delete(NAMESAPCE + "deleteMemberSession", map);
//    }
//
//    public int selectDeviceAgentTokenCount(Map<String, Object> map){
//        return readOnlySqlSession.selectOne(NAMESAPCE + "selectDeviceAgentTokenCount",map);
//    }
//
//    public int deleteAgentToken(Map<String, Object> map) throws Exception{
//        return sqlSession.delete(NAMESAPCE + "deleteAgentToken", map);
//    }
//
//    public int selectTokenCount(Map<String, Object> map){
//        return readOnlySqlSession.selectOne(NAMESAPCE + "selectTokenCount",map);
//    }
//
//    public int updateToken(Map<String, Object> map) throws Exception{
//        return sqlSession.update(NAMESAPCE + "updateToken", map);
//    }
//    public int insertToken(Map<String, Object> map) throws Exception{
//        return sqlSession.insert(NAMESAPCE + "insertToken", map);
//    }
//
//    public int updateJwtToken(Map<String, Object> map) throws Exception{
//        return sqlSession.update(NAMESAPCE + "updateJwtToken", map);
//    }
//    public int insertJwtToken(Map<String, Object> map) throws Exception{
//        return sqlSession.insert(NAMESAPCE + "insertJwtToken", map);
//    }
//
    @Override
    public UserDetails loadUserByUsername(String userId) {
        UserDetails userDetails = null;
        try {

            Map<String, Object> map = new HashMap<>();
            map.put("mb_idx", userId);

            Map<String, Object> userMap = readOnlySqlSession.selectOne(NAMESAPCE + "selectMember", map);
//            User user = ModelUtil.fromMap(userMap, User.class);
//            userDetails = UserPrincipal.create(user);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userDetails;
    }

    public UserDetails loadUserByIdx(Map<String, Object> param) throws CustomException {
        UserDetails userDetails = null;
        try {
            Map<String, Object> userMap = readOnlySqlSession.selectOne(NAMESAPCE + "checkMember", param);
            User user = ModelUtil.fromMap(userMap, User.class);
            if (user == null) {
                throw new CustomException(ErrorCode.CODE_1108);
            }
            Authority authority = new Authority(user.getUser_idx(), "USER", user);
            user.addAuthority(authority);
            user.getAuthorities();
            userDetails = UserPrincipal.create(user);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return userDetails;
    }

    //    public Map<String, Object> certificationType(Map<String, Object> map) throws Exception {
//        Map<String,Object> certificationType = readOnlySqlSession.selectOne(NAMESAPCE + "certificationType", map);
//        if(certificationType.get("mb_google_otp_key") != null)
//            certificationType.put("mb_google_otp_key", SecurityUtil.decryptKSign("member_", "mb_google_otp_key", (String) certificationType.get("mb_google_otp_key")));
//        else
//            certificationType.put("mb_google_otp_key", null);
//        return certificationType;
//    }
//
//    public Map<String,Object> selectMemberTrdPasswordInfo(Map<String, Object> map) throws Exception {
//        //map.put("prev_passwd", SecurityUtil.encryptSHA256(new String(SEED_KISA.DECRYPT((String)map.get("prev_passwd"))).trim()));
//
//        String prev_passwd=(String)map.get("prev_passwd");
//        map.put("prev_passwd", SecurityUtil.encryptSHA256(new String(SEED_KISA.ENCRYPT(prev_passwd.getBytes() ))));
//
//        Map<String,Object> mypage = readOnlySqlSession.selectOne(NAMESAPCE + "selectMemberTrdPasswordInfo", map);
//        return mypage;
//
//    }
//
//    public Map<String, Object> getEmailTokenByMbIdx(Map<String, Object> map) {
//        return readOnlySqlSession.selectOne(NAMESAPCE + "getEmailTokenByMbIdx", map);
//    }
//
//    public Map<String, Object> selectMemberDetailHistory(Map<String, Object> paramMap){
//        return readOnlySqlSession.selectOne(NAMESAPCE + "selectMemberDetailHistory", paramMap);
//    }
//
//    public Map<String, Object> selectDetail(Map<String, Object> map) throws Exception {
//
//        Map<String, Object> member = readOnlySqlSession.selectOne(NAMESAPCE + "selectDetail", map);
//
//        member.put("mb_id", SecurityUtil.decryptKSign("member_", "mb_id", (String) member.get("mb_id")));
//        if (member.containsKey("mb_full_name") && member.get("mb_full_name") != null) {
//            member.put("mb_full_name", SecurityUtil.decryptKSign("member_detail", "mb_full_name", (String) member.get("mb_full_name")));
//        }
//        if (member.containsKey("mb_phone_no") && member.get("mb_phone_no") != null) {
//            member.put("mb_phone_no", SecurityUtil.decryptKSign("member_detail", "mb_phone_no", (String) member.get("mb_phone_no")));
//        }
//        if (member.containsKey("mb_birthday") && member.get("mb_birthday") != null) {
//            member.put("mb_birthday", SecurityUtil.decryptKSign("member_detail", "mb_birthday", (String) member.get("mb_birthday")));
//        }
//        //member.put("mb_bankname", SecurityUtil.decryptKSign("member_detail", "mb_bankname", (String)member.get("mb_bankname")));
//        //member.put("mb_bankcode", SecurityUtil.decryptKSign("member_detail", "mb_bankcode", (String)member.get("mb_bankcode")));
//        //member.put("mb_account_no", SecurityUtil.decryptKSign("member_detail", "mb_account_no", (String)member.get("mb_account_no")));
//
//        return member;
//    }
//
//    public void updateMbLeaveReqest(Map<String, Object> map) throws Exception{
//        sqlSession.update(NAMESAPCE + "updateMbLeaveReqest", map);
//    }
//
//    public void updateMemberCf(Map<String, Object> map) throws Exception{
//        sqlSession.update(NAMESAPCE + "updateMemberCf", map);
//    }
//
//    public Map<String, Object> selectParentCf(Map<String, Object> map) throws Exception{
//        return readOnlySqlSession.selectOne(NAMESAPCE + "selectParentCf", map);
//    }
//
//    public Map<String, Object> selectEmailCheck(Map<String, Object> map) throws Exception{
//        map.put("mb_id",SecurityUtil.encryptKSign("member_","mb_id", (String) map.get("email")));
//        return readOnlySqlSession.selectOne(NAMESAPCE + "selectEmailCheck", map);
//    }
//
//    public int selectJoinCheck(Map<String, Object> map) throws Exception {
//        map.put("join_phone_no",SecurityUtil.encryptKSign("mb_phone_no", "cellNo", (String) map.get("join_phone_no")));
//        return (int) readOnlySqlSession.selectOne(NAMESAPCE + "selectJoinCheck", map);
//    }
//
//    public int selectCfCodeCnt(Map<String, Object> map) throws Exception{
//        return (int) readOnlySqlSession.selectOne(NAMESAPCE + "selectCfCodeCnt",map);
//    }
//
//    public List<Map<String, Object>> selectServiceCoin()throws Exception{
//        return readOnlySqlSession.selectList(NAMESAPCE + "selectServiceCoin");
//    }
//
//    public Object insert(Map<String, Object> map) throws Exception{
//
//        map.put("join_id",SecurityUtil.encryptKSign("member_", "mb_id", (String) map.get("join_id")));
//        String join_password=(String)map.get("join_password");
//        String password_salt=SecurityUtil.generateSalt();
//        map.put("join_password", SecurityUtil.makePasswordHash(join_password, password_salt));
//        map.put("join_password_salt", password_salt);
//        return sqlSession.insert(NAMESAPCE + "insertAddUser", map);
//    }
//
//    public Object insertDetail(Map<String, Object> map) throws Exception{
////		map.put("join_name",SecurityUtil.encryptKSign("member_detail", "mb_full_name", (String) map.get("join_name")));
////		map.put("join_birthday",SecurityUtil.encryptKSign("member_detail", "join_birthday", (String) map.get("join_birthday")));
////		map.put("join_phone_no",SecurityUtil.encryptKSign("member_detail", "mb_phone_no", (String) map.get("join_phone_no")));
//        String mb_trade_password=(String)map.get("mb_trade_password");
//        map.put("mb_trade_password", SecurityUtil.encryptSHA256(new String(SEED_KISA.ENCRYPT(mb_trade_password.getBytes() ))));
//        return sqlSession.insert(NAMESAPCE + "insertDetail", map);
//    }
//
//    public Map<String, Object> checkCfCode(Map<String, Object> map) throws Exception{
//        return readOnlySqlSession.selectOne(NAMESAPCE + "checkCfCode",map);
//    }
//
//    public Object insert_cf(Map<String, Object> map) throws Exception{
//        return sqlSession.insert(NAMESAPCE + "insert_cf", map);
//    }
//
//    public int insertMbAuthorizationLimit(Map<String, Object> map) {
//        return sqlSession.insert(NAMESAPCE + "insertMbAuthorizationLimit", map);
//    }
//
//    public int updateUserDetailInfo(Map<String, Object> map) throws Exception {
////		if(map.get("mb_trade_password") != null) {
//
//        //map.put("mb_trade_password", SecurityUtil.encryptSHA256(new String(SEED_KISA.DECRYPT((String)map.get("mb_trade_password"))).trim()));
////			String mb_trade_password=(String)map.get("mb_trade_password");
////			map.put("mb_trade_password", SecurityUtil.encryptSHA256(new String(SEED_KISA.ENCRYPT(mb_trade_password.getBytes() ))));
////		}
//        if(map.get("mb_account_no") != null) {
//            map.put("mb_account_no",SecurityUtil.encryptKSign("member_detail", "mb_account_no", (String) map.get("mb_account_no")));
//        }
//        if(map.get("mb_bankname") != null) {
//            map.put("mb_bankname",SecurityUtil.encryptKSign("member_detail", "mb_bankname", (String) map.get("mb_bankname")));
//        }
//        if(map.get("mb_bankcode") != null) {
//            map.put("mb_bankcode",SecurityUtil.encryptKSign("member_detail", "mb_bankcode", (String) map.get("mb_bankcode")));
//        }
//        return sqlSession.update(NAMESAPCE + "updateUserDetailInfo", map);
//
//    }
//
//    public Map<String, Object> selectMemberCertifyLevel(Map<String, Object> map) throws Exception{
//        return readOnlySqlSession.selectOne(NAMESAPCE + "selectMemberCertifyLevel", map);
//    }
//
//    public int updateCertifyLevel(Map<String, Object> paramMap) {
//        return sqlSession.update(NAMESAPCE + "updateCertifyLevel", paramMap);
//    }
//
//    public int updateDetailCfCnt(Map<String, Object> paramMap) {
//        return sqlSession.update(NAMESAPCE + "updateDetailCfCnt", paramMap);
//    }
//
//    public Object insertKrwAccount(Map<String, Object> map) throws Exception{
//        return sqlSession.insert(NAMESAPCE + "insertKrwAccount", map);
//    }
//
//    public int insertKrwAccountHistory(Map<String, Object> paramMap) {
//        return sqlSession.insert(NAMESAPCE + "insertKrwAccountHistory", paramMap);
//    }
//
//    public  Map<String, Object> selectCoinAddress(Map<String, Object> map) throws Exception{
//        return readOnlySqlSession.selectOne(NAMESAPCE + "selectCoinAddress", map);
//    }
//
//    public Object insertAccount(Map<String, Object> map) throws Exception{
//        return sqlSession.insert(NAMESAPCE + "insertAccount", map);
//    }
//
//    public Object insertAccountHistory(Map<String, Object> map) throws Exception{
//        return sqlSession.insert(NAMESAPCE + "insertAccountHistory", map);
//    }
//
    public Map<String, Object> dbValidateToken(Map<String, Object> map) throws Exception {
        return readOnlySqlSession.selectOne(NAMESAPCE + "dbValidateToken", map);
    }
//
//    public  Map<String, Object> selectKRWAccount(Map<String, Object> map) throws Exception{
//        return readOnlySqlSession.selectOne(NAMESAPCE + "selectKRWAccount", map);
//    }
//
//    public List<Map<String, Object>> selectAllCoinBalance(Map<String, Object> map) throws Exception{
//        return readOnlySqlSession.selectList(NAMESAPCE + "selectAllCoinBalance", map);
//    }
//
//    public List<Map<String, Object>> selectKrwCoinTrade(Map<String, Object> map) throws Exception{
//        return readOnlySqlSession.selectList(NAMESAPCE + "selectKrwCoinTrade", map);
//    }
//
//    public List<Map<String, Object>> isExistCoinMarket(Map<String, Object> map) throws Exception{
//        return readOnlySqlSession.selectList(NAMESAPCE + "isExistCoinMarket", map);
//    }
//
//    public List<Map<String, Object>> selectCoinTradeMarket(Map<String, Object> map) throws Exception{
//        return readOnlySqlSession.selectList(NAMESAPCE + "selectCoinTradeMarket", map);
//    }
//
//    public List<Map<String, Object>> selectExchangeIngList(Map<String, Object> map) throws Exception{
//        return readOnlySqlSession.selectList(NAMESAPCE + "selectExchangeIngList", map);
//    }
//
//    public void updateWithdrawalApiKey(Map<String, Object> map) throws Exception{
//        sqlSession.update(NAMESAPCE + "updateWithdrawalApiKey", map);
//    }
//    public  Map<String, Object> selectApiKey(Map<String, Object> map) throws Exception{
//        return readOnlySqlSession.selectOne(NAMESAPCE + "selectApiKey", map);
//    }
}
