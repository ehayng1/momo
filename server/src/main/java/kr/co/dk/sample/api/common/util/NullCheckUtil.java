package kr.co.dk.sample.api.common.util;

import java.util.Objects;

public class NullCheckUtil {
    private NullCheckUtil() {
    }

    public static boolean isNull(Object... objects) {
        for (Object object : objects) {
            if (Objects.isNull(object)) return true;
        }
        return false;
    }
}
