package kr.co.dk.sample.api.config.security.service;

//import com.upts.backend.common.model.ErrorCode;
//import com.upts.backend.common.model.exception.CustomException;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class UserDetailsServiceImp implements UserDetailsService {

    static final String NAMESAPCE = "com.upts.backend.rest.asset.dao.Auth.";

    @Autowired
    private SqlSession sqlSession;

    public Map<String, Object> selectMember(Map<String, Object> param) {
        return sqlSession.selectOne(NAMESAPCE + "selectMember", param);
    }

    @Override
    public UserDetails loadUserByUsername(String userId) {
        UserDetails userDetails = null;
        try {

//            Map<String, Object> map = new HashMap<>();
//            map.put("mb_idx", userId);
//
//            Map<String, Object> userMap = sqlSession.selectOne(NAMESAPCE + "selectMember", map);
//            User user = ModelUtil.fromMap(userMap, User.class);
//            userDetails = UserPrincipal.create(user);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userDetails;
    }

//    public UserDetails loadUserByIdx(Map<String, Object> param) throws CustomException {
//        UserDetails userDetails = null;
//        try {
//            Map<String, Object> userMap = sqlSession.selectOne(NAMESAPCE + "selectMember", param);
//            User user = ModelUtil.fromMap(userMap, User.class);
//            if(user == null){
//                throw new CustomException(ErrorCode.CODE_1108);
//            }
//            user.getAuthorities();
//            userDetails= UserPrincipal.create(user);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return userDetails;
//    }
}
