package com.qrcb.common.core.data.handler;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import java.sql.*;
import java.time.LocalTime;
import java.util.Objects;

/**
 * @Author Anson
 * @Create 2023-07-17
 * @Description 实现LocalTime类型与Jdbc的TIME类型转换<br />
 */

@MappedTypes(LocalTime.class)
@MappedJdbcTypes(value = JdbcType.TIME, includeNullJdbcType = true)
public class LocalTimeTypeHandler extends org.apache.ibatis.type.LocalTimeTypeHandler {
	@Override
	public void setNonNullParameter(PreparedStatement ps, int i, LocalTime parameter, JdbcType jdbcType)
		throws SQLException {
		ps.setObject(i, Time.valueOf(parameter));
	}

	@Override
	public LocalTime getNullableResult(ResultSet rs, String columnName) throws SQLException {
		Object object = rs.getObject(columnName);
		if (Objects.isNull(object)) {
			return null;
		}
		//将Time转换成LocalTime类型
		if (object instanceof Time) {
			return ((Time) object).toLocalTime();
		}
		return rs.getObject(columnName, LocalTime.class);
	}

	@Override
	public LocalTime getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
		Object object = rs.getObject(columnIndex);
		if (Objects.isNull(object)) {
			return null;
		}
		if (object instanceof Time) {
			return ((Time) object).toLocalTime();
		}

		return rs.getObject(columnIndex, LocalTime.class);
	}

	@Override
	public LocalTime getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
		Object object = cs.getObject(columnIndex);
		if (Objects.isNull(object)) {
			return null;
		}
		if (object instanceof Time) {
			return ((Time) object).toLocalTime();
		}
		return cs.getObject(columnIndex, LocalTime.class);
	}
}
