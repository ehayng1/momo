package kr.co.dk.sample.api.common.util;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;

public class LowerKeyMap extends HashMap {

    public Object put(Object key, Object value) {
        return super.put(StringUtils.lowerCase((String) key), value);
    }

}
