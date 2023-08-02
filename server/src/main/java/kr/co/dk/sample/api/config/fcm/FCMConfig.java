package kr.co.dk.sample.api.config.fcm;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.io.InputStream;

@Configuration
public class FCMConfig {
    private static final Logger log = LogManager.getLogger(FCMConfig.class);

    @Value("${firebase.database.url}")
    private String FIREBASE_DATABASE_URL;

    @PostConstruct
    public void initFCM() {
        InputStream serviceAccount;
        try {
            //해당 json은 firebase-> 프로젝트개요 옆 설정 -> 서비스계정 -> 자바 새비공개키 생성으로 얻을수 있을지도?
//            serviceAccount = this.getClass().getResourceAsStream("/goodch-firebase-adminsdk.json");
//
//            FirebaseOptions options = new FirebaseOptions.Builder()
//                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
//                    .setDatabaseUrl(FIREBASE_DATABASE_URL)
//                    .build();
//            FirebaseApp.initializeApp(options);
        } catch (Exception e) {
            log.error("FCMConfig config Exception : {}", e);
        }
    }
}
