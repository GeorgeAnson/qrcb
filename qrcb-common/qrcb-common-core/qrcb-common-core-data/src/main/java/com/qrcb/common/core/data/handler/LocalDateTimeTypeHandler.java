package com.qrcb.common.core.data.handler;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * @Author Anson
 * @Create 2023-07-17
 * @Description 实现LocalDateTime类型与Jdbc的TIMESTAMP类型转换<br/>
 * db2中，Timestamp类型直接只用mp类型转换器会报错。
 */

@MappedTypes(LocalDateTime.class)
@MappedJdbcTypes(value = JdbcType.TIMESTAMP, includeNullJdbcType = true)
public class LocalDateTimeTypeHandler extends org.apache.ibatis.type.LocalDateTimeTypeHandler {
	@Override
	public void setNonNullParameter(PreparedStatement ps, int i, LocalDateTime parameter, JdbcType jdbcType)
		throws SQLException {
		ps.setObject(i, Timestamp.valueOf(parameter));
	}

	@Override
	public LocalDateTime getNullableResult(ResultSet rs, String columnName) throws SQLException {
		Object object = rs.getObject(columnName);
		if (Objects.isNull(object)) {
			return null;
		}

		//将时间戳转换成LocalDateTime类型
		if (object instanceof Timestamp) {
			return ((Timestamp) object).toLocalDateTime();
		}

		return rs.getObject(columnName, LocalDateTime.class);
	}

	@Override
	public LocalDateTime getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
		Object object = rs.getObject(columnIndex);

		if (Objects.isNull(object)) {
			return null;
		}
		if (object instanceof Timestamp) {
			return ((Timestamp) object).toLocalDateTime();
		}

		return rs.getObject(columnIndex, LocalDateTime.class);
	}

	@Override
	public LocalDateTime getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
		Object object = cs.getObject(columnIndex);

		if (Objects.isNull(object)) {
			return null;
		}
		if (object instanceof Timestamp) {
			return ((Timestamp) object).toLocalDateTime();
		}

		return cs.getObject(columnIndex, LocalDateTime.class);
	}
}
