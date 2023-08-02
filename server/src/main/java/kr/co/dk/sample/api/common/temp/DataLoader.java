package kr.co.dk.sample.api.common.temp;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements ApplicationRunner {
    private static final Logger log = LogManager.getLogger(DataLoader.class);

    @Value("${profile.activate}")
    private String profileActivated;

//    @Autowired
//    private UserMapper userRepository;
//
//    @Autowired
//    private AuthorityRepository authorityRepository;


    @Value("${goodch.file.resource.server}")
    private String resourceServer;

    @Override
    public void run(ApplicationArguments args) throws Exception {

        if (isLocal()) {
//            userDataInit();
        } else {
            //슬랙메세지 입니다. 각 슬랙방에 맞춰 주세요
//            WebClient.builder().build().post().uri("https://hooks.slack.com/services/슬랙/비밀/키").contentType(MediaType.APPLICATION_JSON).bodyValue(new SlackPayload("배포 완료했습니당~~")).retrieve().bodyToMono(String.class).block();
        }


//        log.info("데이터 init 끝났습니다.");
//        log.info("열심히 일하세요");
    }

//    private List<User> userDataInit(){
//        List<User> users = new ArrayList<>();
//        users.add(createUser("1440894793","김동균", "보라","000-00000-00000","동균@dk.kr","12345","미남역","근처",10000));
//        users.add(createUser("123123","김근형","홀","123-4567-1234","근형@upchian.kr","11111","회사앞","일ㅋㅋㅋ",5000));
//
//        for(int i=3; i<21; ++i){
//            users.add(createUser("1231231"+i,"김근형"+i,"홀","123-1234-12"+i,i+"근형@upchian.kr","11111"+i,"회사앞"+i,"일마레"+i,i*300));
//        }
//
//        return users;
//    }
//
//    private User createUser(String authIdx,String name, String nickName,String phoneNumber,String email
//                        ,String zipCode, String location, String addressDetail, int point){
//        User user = new User();
////        user.setAuthIdx(authIdx);
////        user.setAuthPlatform("kakao");
//        user.setName(name);
////        user.setNickName(nickName);
//        user.setStatus(UserStatus.DEFAULT);
////        user.setProfileImageUrl("");
////        user.setThumbImageUrl("");
////        user.setPhoneNumber(phoneNumber);
////        user.setEmail(email);
////        user.setZipCode(zipCode);
////        user.setAddressLocation(location);
////        user.setAddressDetail(addressDetail);
////        user.setPoint(point);
////        userRepository.save(user);
//        Authority authority = new Authority();
//        authority.setUser(user);
////        authorityRepository.save(authority);
//        Authority sellerAuthority = new Authority();
////        sellerAuthority.setUser(user);
////        sellerAuthority.setRole(UserType.ROLE_SELLER);
////        authorityRepository.save(sellerAuthority);
//        Authority sellerAuthority2 = new Authority();
//        sellerAuthority2.setUser(user);
//        sellerAuthority2.setRole(UserType.ROLE_ADMIN);
////        authorityRepository.save(sellerAuthority2);
//        return user;
//    }

    private boolean isLocal() {
        return profileActivated.equalsIgnoreCase("local");
    }
}
