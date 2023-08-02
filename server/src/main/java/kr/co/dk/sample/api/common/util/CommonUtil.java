package kr.co.dk.sample.api.common.util;

//import jdk.nashorn.internal.parser.DateParser;

//import static jdk.nashorn.internal.parser.DateParser.Name.TIME_SEPARATOR;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CommonUtil {
    private static final Logger log = LogManager.getLogger(CommonUtil.class);

    private CommonUtil() {
    }

//    public static Sort.Order getSortOrder(Class entity,String sortColumn, SortType sortType) throws CustomException {
//        if(!(Arrays.stream(entity.getDeclaredFields()).anyMatch(x-> x.getName().equals(sortColumn)) ||
//                Arrays.stream(DateAudit.class.getDeclaredFields()).anyMatch(x-> x.getName().equals(sortColumn)))){
//            throw new CustomException(ErrorCode.CODE_9001);
//        }
//
//        if(sortType == SortType.ASC){
//            return Sort.Order.asc(sortColumn);
//        }else{
//            return Sort.Order.desc(sortColumn);
//        }
//    }

    /**
     * mysql datetime 타입으로 반환한다.
     *
     * @param gubun
     * @return
     */
    public static String getDate(String gubun) {
        String formatDateTime = null;
        if (gubun.equals("mysql")) {
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

            formatDateTime = now.format(formatter);
        } else if (gubun.equals("day")) {
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd");

            formatDateTime = now.format(formatter);
        } else if (gubun.equals("file_date")) {
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");

            formatDateTime = now.format(formatter);
        } else {
            formatDateTime = getDate();     //일반적인 형식으로 Return
        }

        return formatDateTime;
    }

    /**
     * 오늘날짜
     *
     * @return
     */
    public static String getDate() {
        Calendar now = Calendar.getInstance();
        String y = "", m = "", d = "", hh = "", mm = "", ss = "", ms = "";
        int year = now.get(Calendar.YEAR);
        int month = (now.get(Calendar.MONTH)) + 1;
        int day = now.get(Calendar.DAY_OF_MONTH);
        int hour = now.get(Calendar.HOUR_OF_DAY);
        int min = now.get(Calendar.MINUTE);
        int sec = now.get(Calendar.SECOND);
        long msec = now.get(Calendar.MILLISECOND);

        y = year + "";
        if (month < 10) {
            m = "0" + month;
        } else if (month >= 10) {
            m = "" + month;
        }
        if (day < 10) {
            d = "0" + day;
        } else if (day >= 10) {
            d = "" + day;
        }
        if (hour < 10) {
            hh = "0" + hour;
        } else if (hour >= 10) {
            hh = "" + hour;
        }
        if (min == 0) {
            mm = "00";
        } else if (min < 10) {
            mm = "0" + min;
        } else if (min >= 10) {
            mm = "" + min;
        }
        if (sec == 0) {
            ss = "00";
        } else if (sec < 10) {
            ss = "0" + sec;
        } else if (sec >= 10) {
            ss = "" + sec;
        }
        if (msec == 0) {
            ms = "000";
        } else if (msec < 100 && msec >= 10) {
            ms = "0" + msec;
        } else if (msec <= 10) {
            ms = "00" + msec;
        } else if (msec >= 100) {
            ms = "" + msec;
        }

        return y + m + d + hh + mm + ss + ms;
    }

    /**
     * 현재시간
     *
     * @return
     */
    public static String getTime() {
        Calendar now = Calendar.getInstance();
        String hh = "", mm = "", ss = "", ms = "";
        int hour = now.get(Calendar.HOUR_OF_DAY);
        int min = now.get(Calendar.MINUTE);
        int sec = now.get(Calendar.SECOND);
        long msec = now.get(Calendar.MILLISECOND);

        if (hour < 10) {
            hh = "0" + hour;
        } else if (hour >= 10) {
            hh = "" + hour;
        }
        if (min == 0) {
            mm = "00";
        } else if (min < 10) {
            mm = "0" + min;
        } else if (min >= 10) {
            mm = "" + min;
        }
        if (sec == 0) {
            ss = "00";
        } else if (sec < 10) {
            ss = "0" + sec;
        } else if (sec >= 10) {
            ss = "" + sec;
        }
        if (msec == 0) {
            ms = "000";
        } else if (msec < 100 && msec >= 10) {
            ms = "0" + msec;
        } else if (msec <= 10) {
            ms = "00" + msec;
        } else if (msec >= 100) {
            ms = "" + msec;
        }

        return hh + mm + ss + ms;
    }

    /**
     * mybatis IN 처리용, ','를 List로 나눠줌
     *
     * @param code
     * @return
     */
    public static List<String> makeForeach(String code) {
        String gb = ",";
        return makeForeach(code, gb);
    }

    public static List<String> makeForeach(String code, String gb) {

        if (code == null || "".equals(code)) {
            return null;
        }

        List<String> codeList = new ArrayList<String>();

        String[] aCode = code.split(gb);
        for (int i = 0; i < aCode.length; i++) {

            codeList.add(aCode[i]);
        }

        return codeList;
    }

}
