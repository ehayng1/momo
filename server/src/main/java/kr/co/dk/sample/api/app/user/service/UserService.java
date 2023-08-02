package kr.co.dk.sample.api.app.user.service;

import kr.co.dk.sample.api.app.user.dao.UserDAO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Properties;


@Service
public class UserService {
    private static final Logger log = LogManager.getLogger(UserService.class);

//    public UserService(UserMapper userRepository){
//        this.userRepository = userRepository;
//
//    }
//
//    private UserMapper userRepository;

    @Autowired
    UserDAO userDAO;
    @Autowired
    Environment env;
    @Qualifier("prop")
    private Properties properties;

    public Map<String, Object> selectUserInfo(Map<String, Object> map) throws Exception {
        return userDAO.selectUserInfo(map);
    }


//    legacy logic
//
//    public UserDetail getUserDetail(long userIdx){
//        User user = userRepository.findByIdx(userIdx);
//        UserDetail userDetail = new UserDetail();
//        return userDetail;
//    }
//
//    @Transactional
//    public void updateUserDetail(UserDetail userDetail, long userIdx){
//        User user = userRepository.findByIdx(userIdx);
//        user.setName(userDetail.getName());
////        user.setPhoneNumber(userDetail.getPhoneNumber());
////        user.setEmail(userDetail.getEmail());
////        user.setZipCode(userDetail.getZipCode());
////        user.setAddressLocation(userDetail.getAddressLocation());
////        user.setAddressDetail(userDetail.getAddressDetail());
////        user.setBirthday(userDetail.getBirthday());
////        user.setBirthYear(userDetail.getBirthYear());
////        userRepository.save(user);
//    }
//
//    @Transactional
//    public void leaveRequest(long userIdx){
//        User user = userRepository.findByIdx(userIdx);
//        user.setStatus(UserStatus.LEAVE_REQUEST);
////        userRepository.save(user);
//    }

}
