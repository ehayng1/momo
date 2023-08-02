package kr.co.dk.sample.api.app.alarm.service;

import kr.co.dk.sample.api.app.alarm.dao.AlarmDAO;
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
public class AlarmService {
    private static final Logger log = LogManager.getLogger(AlarmService.class);

    @Autowired
    AlarmDAO alarmDAO;
    @Autowired
    Environment env;
    @Qualifier("prop")
    private Properties properties;

    public List<Map> selectAlarmList(Map<String, Object> map) throws Exception {
        return alarmDAO.selectAlarmList(map);
    }
}
