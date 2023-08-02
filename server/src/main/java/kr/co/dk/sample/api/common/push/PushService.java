package kr.co.dk.sample.api.common.push;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import kr.co.dk.sample.api.app.alarm.dao.AlarmDAO;
import kr.co.dk.sample.api.common.model.ApiResponse;
import kr.co.dk.sample.api.common.model.ErrorCode;
import kr.co.dk.sample.api.common.util.HeaderRequestInterceptor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static kr.co.dk.sample.api.common.util.CommonUtil.makeForeach;

@Service
public class PushService {
    private static final Logger log = LogManager.getLogger(PushService.class);

    @Autowired
    AlarmDAO alarmDAO;

    @Value("${firebase.server.key}")
    private String serverKey;

    private static final String firebase_api_url="https://fcm.googleapis.com/fcm/send";

//    public boolean sendMessage(Push push){
//        HashMap<String ,String> data = new HashMap<String, String>();
//        data.put("type", push.getLinkType() == null? LinkType.ALARM.name():push.getLinkType().name());
//        data.put("idx", push.getTargetIdx() == null?"0":push.getTargetIdx());
//        return sendMessage(push.getTo().getFcmToken(), push.getTitle(), push.getBody(), data);
//    }

    public boolean sendMessage(String token, String title, String body, HashMap<String, String> data) {
        try {
            Notification notification = Notification.builder().setTitle(title).setBody(body).build();
            Message message = Message.builder().setToken(token).putAllData(data)
                    .setNotification(notification).build();
            String result = FirebaseMessaging.getInstance().send(message);
        } catch (FirebaseMessagingException e) {
            log.error("FirebaseMessagingException : {}", e);
        } catch (Exception e) {
            log.error("Exception : {}", e);
        }

        return true;
    }

    public void sendMessage(String idx, String title, String body) throws Exception {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("idx", idx);
        paramMap.put("title", title);
        paramMap.put("content", body);
        List<String> notifications = PeriodicNotificationJson(paramMap);

        if (notifications == null) {
//            return ResponseEntity.ok(new ApiResponse(ErrorCode.CODE_2107));
            log.error("유저 fcm 정보 없음");
            return;
        }

        for (int i = 0; i < notifications.size(); i++) {
            HttpEntity<String> rtnRequest = new HttpEntity<>(notifications.get(i).toString());

            CompletableFuture<String> pushNotification = send(rtnRequest);
            CompletableFuture.allOf(pushNotification).join();

            try{
                String firebaseResponse = pushNotification.get();
            }
            catch (InterruptedException e){
                log.debug("got interrupted!");
                throw new InterruptedException();
            }
            catch (ExecutionException e){
                log.debug("execution error!");
            }
        }


        int result = alarmDAO.insertPush(paramMap);

//        return ResponseEntity.ok(new ApiResponse(true, rtnMap, "success", 200));
    }

    public List<String> PeriodicNotificationJson(Map<String, Object> map) throws Exception {
        LocalDate localDate = LocalDate.now();

        String sampleData[] = {"device token value 1","device token value 2","device token value 3"};

        map.put("list", makeForeach(map.get("idx").toString()));

        List<Map> fcmList = alarmDAO.getFcmList(map);

        if (fcmList.isEmpty()) {
            return null;
        } else {
            map.put("idxList", fcmList);
        }

        JSONObject body = new JSONObject();

        List<String> rtnList = new ArrayList<String>();

//        for(int i=0; i<sampleData.length; i++){
//            tokenlist.add(sampleData[i]);
//        }

        JSONArray array = new JSONArray();

        if (fcmList.size() > 9) {
            for(int i=0; i<fcmList.size(); i++) {
                array.put(fcmList.get(i).get("token_fcm").toString());
                if (array.length() > 9) {
                    body = new JSONObject();

                    body.put("registration_ids", array);

                    JSONObject notification = new JSONObject();
                    notification.put("title", map.get("title").toString());
                    notification.put("body", map.get("content").toString());

                    body.put("notification", notification);

                    rtnList.add(body.toString());

                    array = new JSONArray();
                } else if (i == fcmList.size() - 1) {
                    body = new JSONObject();

                    body.put("registration_ids", array);

                    JSONObject notification = new JSONObject();
                    notification.put("title", map.get("title").toString());
                    notification.put("body", map.get("content").toString());

                    body.put("notification", notification);

                    rtnList.add(body.toString());

                    array = new JSONArray();
                }
            }
        } else {
            for(int i=0; i<fcmList.size(); i++) {
                array.put(fcmList.get(i).get("token_fcm").toString());
            }
            body.put("registration_ids", array);

            JSONObject notification = new JSONObject();
            notification.put("title", map.get("title").toString());
            notification.put("body", map.get("content").toString());

            body.put("notification", notification);

            rtnList.add(body.toString());
        }

        return rtnList;
    }

    @Async
    public CompletableFuture<String> send(HttpEntity<String> entity) {

        RestTemplate restTemplate = new RestTemplate();

        restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(Charset.forName("UTF-8")));

        ArrayList<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();

        interceptors.add(new HeaderRequestInterceptor("Authorization",  "key=" + serverKey));
        interceptors.add(new HeaderRequestInterceptor("Content-Type", "application/json; UTF-8 "));
        restTemplate.setInterceptors(interceptors);

        String firebaseResponse = restTemplate.postForObject(firebase_api_url, entity, String.class);

        return CompletableFuture.completedFuture(firebaseResponse);
    }
}
