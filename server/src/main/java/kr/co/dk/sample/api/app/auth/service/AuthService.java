package kr.co.dk.sample.api.app.auth.service;


//import kr.co.dk.sample.api.app.auth.dto.LoginDTO;
//import kr.co.dk.sample.api.app.auth.dto.LoginRequestDTO;
//import kr.co.dk.sample.api.app.auth.entity.Authority;
//import kr.co.dk.sample.api.app.auth.repository.AuthorityRepository;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import com.amazonaws.services.sns.model.MessageAttributeValue;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;
import kr.co.dk.sample.api.app.auth.dao.AuthDAO;
import kr.co.dk.sample.api.common.model.ApiResponse;
import kr.co.dk.sample.api.common.model.ErrorCode;
import kr.co.dk.sample.api.config.security.JwtTokenProvider;
import kr.co.dk.sample.api.config.security.dto.JwtAuthenticationResponse;
import kr.co.dk.sample.api.config.security.dto.JwtTokenValidation;
import kr.co.dk.sample.api.util.SEED_KISA;
import kr.co.dk.sample.api.util.StringUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.client.WebClient;
import org.web3j.crypto.Bip39Wallet;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.WalletUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

@Service
public class AuthService {

    private static final Logger log = LogManager.getLogger(AuthService.class);

    @Value("${jwt.user.refreshToken.expirationInMinute}")
    private int userRefreshTokenExpiration;

    @Value("${cloud.aws.credentials.accessKey}")
    private String accKey;

    @Value("${cloud.aws.credentials.secretKey}")
    private String secKey;

    @Value("${kakao.restapi.key}")
    private String kakaoRestKey;

    @Value("${naver.restapi.key}")
    private String naverRestKey;

//    private final WebClient kakaoWebClient;
//    private final WebClient naverWebClient;

    @Autowired
    private AuthDAO authDAO;
    //
    private final JwtTokenProvider jwtTokenProvider;

    //
    public AuthService(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public Map<String, Object> getVersion() throws Exception {
        return authDAO.getVersion();
    }

    public int updateVersion(Map<String, Object> map) throws Exception {
        return authDAO.updateVersion(map);
    }

    public ApiResponse getLoginToken(HttpServletRequest request, Map<String, Object> jwtMap) {
        String AccessToken = jwtTokenProvider.createUserLoginAccessToken(jwtMap);
        return new ApiResponse(true, new JwtAuthenticationResponse(AccessToken), ErrorCode.CODE_0000);
    }

    public ApiResponse getConfirmdToken(HttpServletRequest request, Map<String, Object> jwtMap) {
        String AccessToken = jwtTokenProvider.createUserConfirmdAccessToken(jwtMap);
        String refreshToken = jwtTokenProvider.createUserConfirmdRefreshToken(jwtMap);
        return new ApiResponse(true, new JwtAuthenticationResponse(AccessToken, refreshToken), ErrorCode.CODE_0000);
    }

    public String generateUserAccessToken(Map<String, Object> jwtMap) {
        return jwtTokenProvider.createUserAccessToken(jwtMap);
    }

    public String generateUserRefreshToken(Map<String, Object> jwtMap) {
        return jwtTokenProvider.createUserConfirmdRefreshToken(jwtMap);
    }

    public ApiResponse getNewAccessToken(HttpServletRequest request) throws Exception {
        String jwt = getJwtFromRequest(request);
        String jwt2 = getJwtFromRequest(request);

        jwt = new String(SEED_KISA.DECRYPT(jwt));
        jwt = jwt.trim();

        JwtTokenValidation jwtTokenValidation = jwtTokenProvider.validateToken(jwt);
        if (jwtTokenValidation.isSuccess()) {
            if (jwtTokenValidation.isRefreshToken()) {
                Map<String, Object> tmpJwtMap = jwtTokenProvider.getJwtInfo(request);

                Map<String, Object> map = new HashMap<>();
                String agent = request.getHeader("User-Agent");
                map.put("agent_type", StringUtil.getAgentType(agent));

                map.put("mb_idx", tmpJwtMap.get("mb_idx"));
                map.put("token_type", "REFRESH");
                map.put("expiration_in_minute", userRefreshTokenExpiration);
                map.put("token", jwt2);
                Map<String, Object> dbToken = authDAO.dbValidateToken(map);
                if (dbToken == null) {
                    return new ApiResponse(ErrorCode.CODE_1101);
                }

                Map<String, Object> jwtMap = jwtTokenProvider.getJwtInfoRefresh(jwt);

                String generatedJwt = jwtTokenProvider.createUserAccessToken(jwtMap);
                return new ApiResponse(true, new JwtAuthenticationResponse(generatedJwt), ErrorCode.CODE_0000);
            } else {
                return new ApiResponse(ErrorCode.CODE_1105);
            }
        } else {
            return new ApiResponse(jwtTokenValidation.getCode());
        }
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public Map<String, Object> checkUserExist(Map<String, Object> map) throws Exception {
        return authDAO.checkUserExist(map);
    }

    public Map<String, Object> getInfoByPhone(Map<String, Object> map) throws Exception {
        Map<String, Object> rtnMap = authDAO.getInfoByPhone(map);
        if (rtnMap == null) {
            rtnMap = new HashMap<>();
            rtnMap.put("exist", "false");
            return rtnMap;
        } else {
            rtnMap.put("exist", "true");
            map.put("mb_idx", rtnMap.get("user_idx").toString());
            String token = generateUserAccessToken(map);
            rtnMap.put("token", token);
//            rtnMap.put("fcm_token", map.get("fcm_token").toString());
            String refresh = generateUserRefreshToken(map);
            rtnMap.put("refresh", refresh);
            int result = authDAO.updateMember(rtnMap);
            if (result > 0) {
                log.info("update success");
            } else {
                return null;
            }
        }

        return rtnMap;
    }

    public PublishResult sendSMSCode(Map<String, Object> map) throws Exception {
        // 1. SMS전송부터
        PublishResult result = null;
        try {
            BasicAWSCredentials awsCreds = new BasicAWSCredentials(accKey, secKey);

            AmazonSNSClientBuilder builder = AmazonSNSClientBuilder.standard();

            AmazonSNS sns = builder.withRegion(Regions.AP_NORTHEAST_1)
                    .withCredentials(new AWSStaticCredentialsProvider(awsCreds)).build();

            Map<String, MessageAttributeValue> smsAttributes = new HashMap<>();

            // 발신자 설정
//            smsAttributes.put("AWS.SNS.SMS.SenderID",
//                    new MessageAttributeValue().withStringValue("").withDataType("String"));

            // 최대가격 설정
            smsAttributes.put("AWS.SNS.SMS.MaxPrice", new MessageAttributeValue()
                    .withStringValue("0.5").withDataType("String"));

            // 메세지 타입
            smsAttributes.put("AWS.SNS.SMS.SMSType", new MessageAttributeValue()
                    .withStringValue("Transactional").withDataType("String"));

            // 수신번호 가공
            String phone = map.get("phone").toString().replace("-", "");
            if (phone.startsWith("0")) {
                phone = "+82" + phone;
            } else {
                // 번호 형식 오류
                return result;
            }

            result = this.sendSMSMessage(sns,
                    map.get("msg").toString(),
                    phone,
                    smsAttributes);
        } catch (Exception ex) {

            log.error("The sms was not sent.");
            log.error("Error message: " + ex.getMessage());
            throw new AmazonClientException(ex.getMessage(), ex);
        }
        return result;
    }

    public PublishResult sendSMSMessage(AmazonSNS sns, String message, String phoneNumber,
                                        Map<String, MessageAttributeValue> smsAttributes) {

        PublishResult result = sns.publish(new PublishRequest()
                .withMessage(message)
                .withPhoneNumber(phoneNumber)
                .withMessageAttributes(smsAttributes));

        return result;
    }

    public int insertPhoneAuthentication(Map<String, Object> map) throws Exception {
        return authDAO.insertPhoneAuthentication(map);
    }


    public Map<String, Object> selectMember(Map<String, Object> map) throws Exception {
        // 1. phone_authentication으로 인증코드 비교
        Map<String, Object> rtnMap = new HashMap<>();
        try {
            Map<String, Object> codeMap = authDAO.compareCode(map);
            if (codeMap == null) {
                // 못가져옴
                rtnMap.put("msg", "error");
            } else if (Integer.parseInt(codeMap.get("count").toString()) >= 5) {
                rtnMap.put("msg", "count"); // 시도횟수 초과
            } else if (!map.get("code").toString().equals(codeMap.get("number").toString())) {
                int result = authDAO.increaseAuthCount(map);
                if (result > 0) {
                    rtnMap.put("msg", "notEquals"); // 불일치
                } else {
                    rtnMap.put("msg", "increaseError"); // 서버에러
                }
            } else {
                if (map.get("exist").toString().equals("true")) {
                    rtnMap = authDAO.selectMember(map);
                    if (rtnMap == null) {
                        return rtnMap;
                    } else {
                        map.put("mb_idx", rtnMap.get("user_idx").toString());
                        String token = generateUserAccessToken(map);
                        rtnMap.put("token", token);
                        rtnMap.put("fcm_token", map.get("fcm_token").toString());
                        String refresh = generateUserRefreshToken(map);
                        rtnMap.put("refresh", refresh);
                        int result = authDAO.updateMember(rtnMap);
                        if (result > 0) {
                            log.info("update success");
                        } else {
                            return null;
                        }
                    }
                    rtnMap.put("msg", "success"); // 성공시
                } else {
                    rtnMap.put("msg", "enroll"); // 가입해야함
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            rtnMap.put("msg", "error");
        }
        return rtnMap;
    }

    public int enrollUser(Map<String, Object> map) throws Exception {
        return authDAO.enrollUser(map);
    }

    public Map<String, Object> getNicknameCount(Map<String, Object> map) throws Exception {
        return authDAO.getNicknameCount(map);
    }

    public int insertNickName(Map<String, Object> map) throws Exception {
        return authDAO.insertNickName(map);
    }

    public int insertPhoto(Map<String, Object> map) throws Exception {
        return authDAO.insertPhoto(map);
    }

    public Map<String, Object> selectValidateNickName(Map<String, Object> map) throws Exception {
        return authDAO.selectValidateNickName(map);
    }

    public int addSignUp(Map<String, Object> map) throws Exception {
        if (map.get("user_type").equals("노무사")) {
            return authDAO.insertLabor(map);
        }

        return authDAO.addSignUp(map);
    }

    public int updateMemberWithBlockchain(Map<String, Object> map) throws Exception {
        // 지갑생성, 로컬에서 실행 시 파일 생성 안됨
//        try {
//            // 블락체인 코드 테스트용, 나중에 가입쪽으로 코드 옮겨야함
//            String blockchain = this.createWallet(map.get("phone").toString());
//            map.put("blockchain", blockchain);
//            int result2 = authDAO.insertWallet(map);
//            if (result2 <= 0) {
//                map.put("msg", "walletError");
//                log.error("wallet insert error");
////                return rtnMap;
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            map.put("blockchain", "");
//        }
        int result1 = authDAO.insertWallet(map);
        int result2 = authDAO.updateMemberWithBlockchain(map);
        return result1 * result2;
    }

    public String createWallet(String phoneNumber) throws Exception {
        // ************ — — — — — — create account by seed phrase ******************
        System.out.println("Creating New Account");
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Enter New Password");
        String walletPassword = phoneNumber;
        /* Define Wallet File Location */
        File walletDirectory = new File("/chmod777");
        Bip39Wallet walletName = WalletUtils.generateBip39Wallet(walletPassword, walletDirectory);
        System.out.println("wallet location: " + walletDirectory + "/" + walletName);
        Credentials credentials = WalletUtils.loadBip39Credentials(walletPassword, walletName.getMnemonic());
        String accountAddress = credentials.getAddress();
        System.out.println("Account address: " + credentials.getAddress());
        ECKeyPair privateKey = credentials.getEcKeyPair();
        String seedPhrase = walletName.getMnemonic();
        System.out.println("Account Details:");
        System.out.println("Your New Account : " + credentials.getAddress()); // 지갑주소
        System.out.println("Mneminic Code: " + walletName.getMnemonic());
        System.out.println("Private Key: " + privateKey.getPrivateKey().toString(16));
        System.out.println("Public Key: " + privateKey.getPublicKey().toString(16));

        return credentials.getAddress();
    }

//    public Map<String, Object> selectSignin(Map<String, Object> map) throws Exception {
//        return authDAO.selectSignin(map);
//    }
//
//    public Map<String, Object> selectfromIP(Map<String, Object> map) throws Exception {
//        return authDAO.selectfromIP(map);
//    }
//
//    public int updateMemberActive(Map<String, Object> map) throws Exception {
//        return authDAO.updateMemberActive(map);
//    }
//
//    public int insertMemberDetailHistory(Map<String, Object> map) throws Exception {
//        return authDAO.insertMemberDetailHistory(map);
//    }
//
//    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class})
//    public Object insertActivityMem(Map<String, Object> map, HttpServletRequest request) throws Exception {
//        try {
//            authDAO.insertActivity(map);
//            authDAO.updateLoginDate(map);
//            log.debug("map ::" + map.get("mb_idx"));
//            if (0 == (int) map.get("mb_idx")) {
//                return false;
//            } else {
//                log.debug("zzzz map ::" + map.get("mb_idx"));
//            }
//
//            Map<String, Object> sessionCntMAP = authDAO.selectMemberSessionCnt(map);
//            int sessioncnt = ((Long) sessionCntMAP.get("cnt")).intValue();
//            //세션 session 테이블 넣기
//            if (map.get("mb_idx") != null) {
//                map.put("action_page", "login");
//                if (sessioncnt == 0) {
//                    authDAO.insertMemberSession(map);
//                } else {
//                    authDAO.deleteMemberSession(map);
//                    authDAO.insertMemberSession(map);
//                }
//            }
//            return true;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return false;
//        }
//    }
//
//    //회원 접속정보(회원 페이지 이용내역)
//    public void insertActivity(Map<String, Object> paramMap, HttpServletRequest request) throws Exception {
//        try {
//            //히스토리 등록
//            //IPAddress
//            String ip = NetworkUtil.getClientIP(request);
//            //IPv6 경우는 예외처리한다
//            if (ip.indexOf("0:0:0") > -1) {
//                ip = "127.0.0.1";
//            }
//            // ::1 대응
//            if ("::1".equals(ip)) {
//                ip = "127.0.0.1";
//            }
//
//            log.debug("======================== Login ip : " + ip);
//
//            Map<String, Object> locationMap = new HashMap<>();
//
//            if (!"".equals(ip) && null != ip) {
//                String[] orgAddrArray = ip.trim().split(",");
//
//                String[] firstAddrArray = orgAddrArray[0].trim().split("\\.");
//
//                long num = 0;
//                for (int i = 0; i < firstAddrArray.length; i++) {
//                    if (firstAddrArray[i].contains(":")) {
//                        firstAddrArray[i] = firstAddrArray[i].substring(0, firstAddrArray[i].indexOf(":"));
//                    }
//                    int power = 3 - i;
//                    num += ((Integer.parseInt(firstAddrArray[i]) % 256 * Math.pow(256, power)));
//                }
//
//                paramMap.put("strAddr", num);
//
//                locationMap = authDAO.selectfromIP(paramMap);
//            }
//
//            String agent = request.getHeader("User-Agent");
//            String brower = null;
//
//            if (agent != null) {
//                if (agent.indexOf("Trident") > -1) brower = "MSIE";
//                else if (agent.indexOf("Chrome") > -1) brower = "Chrome";
//                else if (agent.indexOf("Opera") > -1) brower = "Opera";
//                else if (agent.indexOf("iPhone") > -1 && agent.indexOf("Mobile") > -1) brower = "iPhone";
//                else if (agent.indexOf("Android") > -1 && agent.indexOf("Mobile") > -1) brower = "Android";
//            }
//
//            try {
////                Object mb_id = SecurityUtil.encryptKSign("member", "mb_id", (String) paramMap.get("mb_id"));
////                paramMap.put("mb_id", "-" + mb_id);
//                paramMap.put("mb_id", (String) request.getSession().getAttribute("mb_id") );
//            } catch (Exception e) {
//                paramMap.put("mb_id", "");
//            }
//
//            if (null != locationMap) {
//                paramMap.put("agent_location", String.valueOf(locationMap.get("CN")));
//            }
//
//            paramMap.put("result", "TRUE");
//            paramMap.put("agent_os", agent);
//            paramMap.put("agent_browser", brower);
//            paramMap.put("agent_ip", ip);
//            paramMap.put("agent_type", StringUtil.getAgentType(agent));
//            authDAO.insertActivity(paramMap);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class})
//    public int insertToken(Map<String, Object> map) throws Exception {
//
//        //1. 동일한 토큰이 등록 되었다면 삭제 (1개의 디바이스에 다른 회원 아디로 로그인 시에 기존 로그인한 디바이스 정보를 삭제한다.)
//        int result;
//        int deviceAgentTokenCount = authDAO.selectDeviceAgentTokenCount(map);
//        if (deviceAgentTokenCount > 0) {
//            authDAO.deleteAgentToken(map);
//        }
//        //2. 회원 IDX로 등록 된 정보가 있는지
//        int count = authDAO.selectTokenCount(map);
//        if (count > 0) {
//            //3-1. 있다면 토큰 업데이트
//            result = authDAO.updateToken(map);
//        } else {
//            //3-2. 없다면 토큰 입력
//            result = authDAO.insertToken(map);
//        }
//
//        return result;
//    }
//
//    @Transactional
//    public int insertJwtToken(Map<String, Object> map, HttpServletRequest request) throws Exception {
//        int result;
//        Map<String, Object> insertMap = new HashMap<>();
//        insertMap.put("mb_idx", map.get("mb_idx"));
//
//        String agent = request.getHeader("User-Agent");
//        insertMap.put("agent_type", StringUtil.getAgentType(agent));
//
//        // 2차, OTP 인증인경우
//        if (map.get("refreshToken") != null) {
//            //기존꺼  Access, refresh 모두 N처리
//            authDAO.updateJwtToken(insertMap);
//
//            //DB 인서트
//            insertMap.put("token_type", "ACCESS");
//            insertMap.put("token_value", map.get("accessToken"));
//            result = authDAO.insertJwtToken(insertMap);
//
//            insertMap.put("token_type", "REFRESH");
//            insertMap.put("token_value", map.get("refreshToken"));
//            result = authDAO.insertJwtToken(insertMap);
//
//        }
//        // 재발행인 경우
//        else {
//            insertMap.put("refresh", "Y");
//            //기존꺼 Access만 N처리
//            authDAO.updateJwtToken(insertMap);
//
//            //DB 인서트
//            insertMap.put("token_type", "ACCESS");
//            insertMap.put("token_value", map.get("accessToken"));
//            result = authDAO.insertJwtToken(insertMap);
//
//        }
//
//        return result;
//    }
//
//    public int updateJwtToken(Map<String, Object> map) throws Exception {
//        return authDAO.updateJwtToken(map);
//    }
//
//    public Map<String, Object> certificationType(Map<String, Object> map) throws Exception {
//        return authDAO.certificationType(map);
//    }
//
//    public Map<String, Object> selectMemberTrdPasswordInfo(Map<String, Object> map) throws Exception {
//        return authDAO.selectMemberTrdPasswordInfo(map);
//    }
//
//    char[] seed = {'a', '2', 'c', '4', 'e', '6', 'g', '8', 'i', '0', 'k', '2', 'm', '4', 'o', '6'};
//
//    public String getCommunityParam(HttpServletRequest request) {
//        String mbInfo = "";
//
//        //세션정보가없을 종료
//        String mb_idx = request.getSession().getAttribute("mb_idx") == null ? "" : String.valueOf((Integer) request.getSession().getAttribute("mb_idx"));
//        if (mb_idx == "") {
//            return mbInfo;
//        }
//        try {
//            if (mb_idx != null && !mb_idx.equals("")) {
//                // email 토큰 가져오는 파라미터 설정
//                Map<String, Object> param = new HashMap<String, Object>();
//                param.put("mb_idx", mb_idx);
//                // email 토큰 가져오기
//                Map<String, Object> result = authDAO.getEmailTokenByMbIdx(param);
//
//                String emailToken = (String) result.get("mb_email_token");
//                System.out.println(emailToken);
//                // aes256 시드 뽑기
//                char[] seedArr = seed.clone();
//                Random rnd = new Random();
//                for (int i = 0; i < seedArr.length; i++) {
//                    int idx = rnd.nextInt(16);
//                    char temp = seedArr[i];
//                    seedArr[i] = seedArr[idx];
//                    seedArr[idx] = temp;
//                }
//
//                String secretKey = new String(seedArr);
//
//                AESUtil aesUtil = new AESUtil(secretKey);
//
//                String base64Str = Base64Util.encode(emailToken.getBytes());
//                String aesEncStr = aesUtil.AES_Encode(base64Str);
//                String base32Str = Base32Util.encode(aesEncStr);
//
//                mbInfo = secretKey.substring(0, 8) + base32Str.substring(0, 2) + secretKey.substring(8, 16) + base32Str.substring(2);
//
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            mbInfo = "";
//        }
//
//        return mbInfo;
//    }
//
//    public String selectMemberDetailHistory(Map<String, Object> paramMap) {
//        String result = authDAO.selectMemberDetailHistory(paramMap) == null ? "" : authDAO.selectMemberDetailHistory(paramMap).get("reg_date").toString();
//        return result;
//    }
//
//    public int deleteMemberSession(Map<String, Object> map) throws Exception {
//        return (int) authDAO.deleteMemberSession(map);
//    }
//
//    public Map<String, Object> selectDetail(Map<String, Object> map) throws Exception {
//        return authDAO.selectDetail(map);
//    }
//
//    public Map<String, Object> selectEmailCheck(Map<String, Object> map) throws Exception {
//        return authDAO.selectEmailCheck(map);
//    }
//
//    public Map<String, Object> selectParentCf(Map<String, Object> map) throws Exception {
//        return authDAO.selectParentCf(map);
//    }
//
//    public int selectJoinCheck(Map<String, Object> map) throws Exception {
//        return (int) authDAO.selectJoinCheck(map);
//    }
//
//    public int selectCfCodeCnt(Map<String, Object> map) throws Exception {
//        return (int) authDAO.selectCfCodeCnt(map);
//    }
//
//    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class})
//    public Map<String, Object> insert(Map<String, Object> map) throws Exception {
//        Map<String, Object> rtnMap = new HashMap<>();
//        rtnMap.put("result" , "SUCCESS");
//        try {
//
//            Date nowtime = new Date();
//
//            SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//            String now = transFormat.format(nowtime);
//
//            Map<String, Object> checkMap = new HashMap<>();
//            Map<String, Object> cfInsertMap = new HashMap<>();
//
//            //맴버 추천인 코드 생성
//            String cf_code = "";
//            while (true) {
//                cf_code = randomCfCode();
//                checkMap.put("mb_cf_code", cf_code);
//                if (authDAO.checkCfCode(checkMap) == null) {
//                    cfInsertMap.put("mb_cf_code", cf_code);
//                    map.put("cf_code", cf_code);
//                    break;
//                }
//            }
//
//
//            //	추천인 코드 입력(MEMBER_CF)URL 가입
//            if (map.get("mb_cf_code") != null && !"".equals(map.get("mb_cf_code"))) {
//                //추천인 코드 가져오기
//                String mb_cf_code = map.get("mb_cf_code").toString();
//                checkMap.put("mb_cf_code", mb_cf_code);
//                //부모 추천인 코드 정보 찾기
//                Map<String, Object> parentCfMap = authDAO.checkCfCode(checkMap);
//                if (parentCfMap != null) {
//                    if (parentCfMap.get("second_up_cf_code") != null) {
//                        checkMap.put("mb_cf_code", parentCfMap.get("second_up_cf_code"));
//                        //조부모 추천인 코드 정보 찾기
//                        Map<String, Object> grandparentsCfMap = authDAO.checkCfCode(checkMap);
//                        if (grandparentsCfMap != null) {
//                            //맴버 first 등록
//                            cfInsertMap.put("first_up_mb_idx", grandparentsCfMap.get("mb_idx"));
//                            cfInsertMap.put("first_up_cf_code", grandparentsCfMap.get("mb_cf_code"));
//
//                            //조부모 카운트 +1
//                            int grandparentsCfCnt = Integer.parseInt(grandparentsCfMap.get("mb_cf_cnt") + "");
//                            grandparentsCfMap.put("mb_cf_cnt", grandparentsCfCnt + 1);
//                            grandparentsCfMap.put("cf_cnt", grandparentsCfCnt + 1);
//                            authDAO.updateMemberCf(grandparentsCfMap);
//                            authDAO.updateDetailCfCnt(grandparentsCfMap);
//                        }
//                    }
//                    // 부모 카운트 +1
//                    int parentCfCnt = Integer.parseInt(parentCfMap.get("mb_cf_cnt") + "");
//                    parentCfMap.put("mb_cf_cnt", parentCfCnt + 1);
//                    parentCfMap.put("cf_cnt", parentCfCnt + 1);
//                    authDAO.updateMemberCf(parentCfMap);
//                    authDAO.updateDetailCfCnt(parentCfMap);
//
//                    // 맴버 second등록
//                    cfInsertMap.put("second_up_mb_idx", parentCfMap.get("mb_idx"));
//                    cfInsertMap.put("second_up_cf_code", parentCfMap.get("mb_cf_code"));
//
//                } else {
//                    rtnMap.put("result","FAIL");
//                    rtnMap.put("error", ErrorCode.CODE_9003);
//                    rtnMap.put("error_type", "recommend");
//                    //추천인 정보가 없습니다.
//                    return null;
//                }
//            }
//            authDAO.insert(map);
//            authDAO.insertDetail(map);
//
//            cfInsertMap.put("mb_cf_cnt", 0);
//            cfInsertMap.put("mb_idx", map.get("mb_idx"));
//
//            authDAO.insert_cf(cfInsertMap);
//            String mb_idx = String.valueOf(map.get("mb_idx"));
//
//            /**
//             * history의 reg_date는 account의 mod_date와 같아야 해서 변수처리 함.
//             */
//            Map<String, Object> paramMap = new HashMap<>();
//            paramMap.put("mb_idx", mb_idx);
//            paramMap.put("mod_date", now);
//            paramMap.put("reg_date", now);
//
//            /* 회원에 대한 인증 제한 insert */
//            authDAO.insertMbAuthorizationLimit(map);
//
//            rtnMap.put("mb_idx", mb_idx);
//        } catch (Exception e) {
//            rtnMap.put("result","FAIL");
//            e.printStackTrace();
//            log.error(e.getMessage());
//            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
//        }
//        return rtnMap;
//    }
//
//    public List<Map<String, Object>> selectServiceCoin() throws Exception {
//        return authDAO.selectServiceCoin();
//    }
//
//    public void insertAccount(Map<String, Object> map) throws Exception {
//        log.debug("================================insertAccount");
//
//        // 원화계정
//        authDAO.insertKrwAccount(map);
//        // 원화계정 history
//        authDAO.insertKrwAccountHistory(map);
//
//        // 코인계정
//        List<Map<String, Object>> coinList = (List<Map<String, Object>>) map.get("coinList");
//        for (Map<String, Object> coinMap : coinList) {
//            Map<String, Object> insertMap = new HashMap<>(map);
//            insertMap.putAll(coinMap);
//            insertMap.put("address", "");
//
//            authDAO.insertAccount(insertMap);
//            authDAO.insertAccountHistory(insertMap);
//        }
//    }
//
//    public ApiResponse memberWithdrawalRequest(Map<String, Object> paramMap, HttpServletRequest request) throws Exception {
//        try{
//            // 1. 보유 KRW
//            Map<String, Object> krwMap = authDAO.selectKRWAccount(paramMap);
//            BigDecimal exchange_ing, trade_ing, total_ing, dambo, accident, balance;
//            try {
//                balance = new BigDecimal(krwMap.get("balance").toString());
//            } catch (Exception e) {
//                balance = BigDecimal.ZERO;
//            }
//            try {
//                exchange_ing = new BigDecimal(krwMap.get("exchange_ing").toString());
//            } catch (Exception e) {
//                exchange_ing = BigDecimal.ZERO;
//            }
//            try {
//                trade_ing = new BigDecimal(krwMap.get("trade_ing").toString());
//            } catch (Exception e) {
//                trade_ing = BigDecimal.ZERO;
//            }
//            try {
//                dambo = new BigDecimal(krwMap.get("dambo").toString());
//            } catch (Exception e) {
//                dambo = BigDecimal.ZERO;
//            }
//            try {
//                accident = new BigDecimal(krwMap.get("accident").toString());
//            } catch (Exception e) {
//                accident = BigDecimal.ZERO;
//            }
//            total_ing = exchange_ing.add(trade_ing).add(dambo).add(accident);
//
//            // 2. 보유 코인들 조회
//            setCoinListParam(paramMap);
//            List<Map<String, Object>> coinList = authDAO.selectAllCoinBalance(paramMap);
//
//            Map<String, Object> coinMap = new HashMap<>();
//            coinMap.put("mb_idx", paramMap.get("mb_idx"));
//            if(coinList != null && coinList.size() != 0){
//                // 출금 진행중, 출금 요청, 주문 들어가있는지 검사한다.
//                for (int i = 0 ; i < coinList.size(); i++){
//                    coinMap.put("coin_type", coinList.get(i).get("coin_type"));
//
//                    try{
//                        // krw_{coin}_trade 검사
//                        List<Map<String, Object>> krwCoinTradeMap = authDAO.selectKrwCoinTrade(coinMap);
//
//                        if(krwCoinTradeMap != null && krwCoinTradeMap.size() != 0 ){
//                            // "[2048] 마켓에 미체결 내역이 존재합니다."
//                            return new ApiResponse(ErrorCode.CODE_2048);
//                        }
//                    }catch (Exception e){
//                        // {coin}_trade 가 없는경우
//                        e.printStackTrace();
//                    }
//
//                    try{
//                        // 해당 코인의 마켓이 있는지 검사
//                        List<Map<String, Object>> coinMarket = authDAO.isExistCoinMarket(coinMap);
//
//                        // 있다면 해당 마켓 trade 검사
//                        if(coinMarket.size() != 0 && coinMarket != null){
//                            for (int j = 0; j < coinMarket.size(); j++){
//                                coinMap.put("trd_type", coinMarket.get(j).get("trd_type"));
//                                List<Map<String, Object>> coinMarketTradeMap = authDAO.selectCoinTradeMarket(coinMap);
//                                if(coinMarketTradeMap != null && coinMarketTradeMap.size() != 0 ){
//                                    // "[2048] 마켓에 미체결 내역이 존재합니다."
//                                    return new ApiResponse(ErrorCode.CODE_2048);
//                                }
//                            }
//                        }
//                    }catch (Exception e){
//                        // {coin}_trade 가 없는경우
//                        e.printStackTrace();
//                    }
//                    // coin_exchange 검사
//                    List<Map<String, Object>> exchangeList = authDAO.selectExchangeIngList(coinMap);
//                    if(exchangeList != null && exchangeList.size() != 0){
//                        // "[2049] 진행중인 입출금 내역이 존재합니다."
//                        return new ApiResponse(ErrorCode.CODE_2049);
//                    }
//                }
//            }
//
//            // 3. krw_exchage 검사
//            coinMap.put("coin_type", "krw");
//            List<Map<String, Object>> krwExchangeList = authDAO.selectExchangeIngList(coinMap);
//            if(krwExchangeList != null && krwExchangeList.size() != 0){
//                // "[2049] 진행중인 입출금 내역이 존재합니다."
//                return new ApiResponse(ErrorCode.CODE_2049);
//            }
//
//            // 4. 마지막으로 krw account 검사
//            if(exchange_ing.compareTo(BigDecimal.ZERO) != 0) {
//                // "[2049] 진행중인 입출금 내역이 존재합니다."
//                return new ApiResponse(ErrorCode.CODE_2049);
//            }
//
//            if(trade_ing.compareTo(BigDecimal.ZERO) != 0){
//                // "[2048] 마켓에 미체결 내역이 존재합니다."
//                return new ApiResponse(ErrorCode.CODE_2048);
//            }
//            log.info("=============== 탈퇴회원 계좌검증 완료 ===================");
//            // 맴버 gubun 업데이트
//            authDAO.updateMbLeaveReqest(paramMap);
//
//
//            // api 키가 있는지 조회
//            Map<String, Object> apiKeyMap = authDAO.selectApiKey(paramMap);
//            if(apiKeyMap != null && apiKeyMap.size() != 0){
//                // 맴버 API 키 업데이트
//                authDAO.updateWithdrawalApiKey(paramMap);
//            }
//            paramMap.put("mb_idx", paramMap.get("mb_idx").toString());
//            paramMap.put("activity_type", "17"); // 탈퇴 신청
//            insertActivity(paramMap, request);
//
//            return new ApiResponse(true, ErrorCode.CODE_0000);
//        }
//        catch (Exception e){
//            e.printStackTrace();
//            return new ApiResponse(ErrorCode.CODE_9999);
//        }
//    }
//
//    public void updateMbLeaveReqest(Map<String, Object> paramMap, HttpServletRequest request) throws Exception {
//        authDAO.updateMbLeaveReqest(paramMap);
//        paramMap.put("mb_idx", paramMap.get("mb_idx").toString());
//        paramMap.put("activity_type", "17"); // 탈퇴 신청
//        insertActivity(paramMap, request);
//    }
//
//    private void setCoinListParam(Map<String, Object> paramMap) throws Exception {
//
//        List<Map<String, Object>> coinList = redisUtil.getRedisDataList(RD_ServiceCoin);
//        if (coinList == null || coinList.size() == 0) {
//            coinList = authDAO.selectServiceCoin();
////            redisUtil.setRedisData(RD_ServiceCoin, jsonUtil.getJsonStringFromList(coinList));
//        }
//
//        for (Map<String, Object> coinMap : coinList) {
//            String simple_form = coinMap.get("simple_form")+"";
//            coinMap.put("lower_coin_name", simple_form.toLowerCase());
//            if (paramMap.get("mb_idx") != null) {
//                coinMap.put("mb_idx", paramMap.get("mb_idx"));
//            }
//        }
//        paramMap.put("coinList", coinList);
//    }

    // 소셜 로그인
//    public Map<String, Object> getAuthFromPlatform() {
//
//    }

}
