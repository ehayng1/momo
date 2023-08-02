package kr.co.dk.sample.api.common.model.enums.status;

import kr.co.dk.sample.api.common.model.enums.AbstractEnumConverter;
import kr.co.dk.sample.api.common.model.enums.CodeEnum;
import lombok.Getter;
import org.apache.ibatis.type.MappedTypes;

@Getter
public enum UserStatus implements CodeEnum<Integer> {
    DEFAULT(1, "가입", true), BLOCKED(2, "차단", false), LEAVE_REQUEST(3, "탈퇴 요청", false), LEAVE(4, "탈퇴", false);

    private final Integer value;
    private final String description;
    /**
     * 로그인 할수 있는지
     */
    private final boolean canLogin;


    UserStatus(Integer value, String description, boolean canLogin) {
        this.value = value;
        this.description = description;
        this.canLogin = canLogin;
    }

    @MappedTypes(UserStatus.class)
    public static class TypeHandler extends AbstractEnumConverter<UserStatus> {
        public TypeHandler() {
            super(UserStatus.class);
        }
    }


}
