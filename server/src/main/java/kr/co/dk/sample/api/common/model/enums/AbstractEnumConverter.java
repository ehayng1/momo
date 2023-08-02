package kr.co.dk.sample.api.common.model.enums;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeException;
import org.apache.ibatis.type.TypeHandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * https://www.holaxprogramming.com/2015/11/12/spring-boot-mybatis-typehandler/ 참조
 *
 * @param <E>
 */
public abstract class AbstractEnumConverter<E extends Enum<E>> implements TypeHandler<CodeEnum> {
    private final Class<E> enumType;

    public AbstractEnumConverter(Class<E> enumType) {
        this.enumType = enumType;
    }


    @Override
    public void setParameter(PreparedStatement ps, int i, CodeEnum parameter, JdbcType jdbcType) throws SQLException {
        ps.setObject(i, parameter.getValue());
    }

    @Override
    public CodeEnum getResult(ResultSet rs, String columnName) throws SQLException {
        int code = rs.getInt(columnName);
        return getCodeEnum(code);
    }

    @Override
    public CodeEnum getResult(ResultSet rs, int columnIndex) throws SQLException {
        int code = rs.getInt(columnIndex);
        return getCodeEnum(code);
    }

    @Override
    public CodeEnum getResult(CallableStatement cs, int columnIndex) throws SQLException {
        int code = cs.getInt(columnIndex);
        return getCodeEnum(code);
    }

    private CodeEnum getCodeEnum(int code) {
        try {
            CodeEnum[] enumConstants = (CodeEnum[]) enumType.getEnumConstants();
            for (CodeEnum codeNum : enumConstants) {
                if (codeNum.getValue().equals(code)) {
                    return codeNum;
                }
            }
            return null;
        } catch (Exception e) {
            throw new TypeException("Can't make enum object '" + enumType + "'", e);
        }
    }
}
