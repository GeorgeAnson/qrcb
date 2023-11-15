package com.qrcb.common.core.data.handler;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import java.sql.*;
import java.time.LocalDate;
import java.util.Objects;

/**
 * @Author Anson
 * @Create 2023-07-17
 * @Description 实现LocalDate类型与Jdbc的Date类型转换<br />
 */

@MappedTypes(LocalDate.class)
@MappedJdbcTypes(value = JdbcType.DATE, includeNullJdbcType = true)
public class LocalDateTypeHandler extends org.apache.ibatis.type.LocalDateTypeHandler {

	@Override
	public void setNonNullParameter(PreparedStatement ps, int i, LocalDate parameter, JdbcType jdbcType)
		throws SQLException {
		ps.setObject(i, Date.valueOf(parameter));
	}

	@Override
	public LocalDate getNullableResult(ResultSet rs, String columnName) throws SQLException {
		Object object = rs.getObject(columnName);
		if (Objects.isNull(object)) {
			return null;
		}

		//将Date类型转换成LocalDateTime类型
		if (object instanceof Date) {
			return ((Date) object).toLocalDate();
		}

		return rs.getObject(columnName, LocalDate.class);
	}

	@Override
	public LocalDate getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
		Object object = rs.getObject(columnIndex);
		if (Objects.isNull(object)) {
			return null;
		}

		//将Date类型转换成LocalDateTime类型
		if (object instanceof Date) {
			return ((Date) object).toLocalDate();
		}

		return rs.getObject(columnIndex, LocalDate.class);
	}

	@Override
	public LocalDate getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
		Object object = cs.getObject(columnIndex);
		if (Objects.isNull(object)) {
			return null;
		}

		//将Date类型转换成LocalDateTime类型
		if (object instanceof Date) {
			return ((Date) object).toLocalDate();
		}

		return cs.getObject(columnIndex, LocalDate.class);
	}
}
