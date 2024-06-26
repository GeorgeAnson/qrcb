package com.qrcb.common.extension.sequence.range.impl.db;

import com.qrcb.common.extension.sequence.exception.SeqException;

import javax.sql.DataSource;
import java.sql.*;

/**
 * @Author Anson
 * @Create 2023-10-24
 * @Description 操作 DB 帮助类 <br/>
 */

public class BaseDbHelper {


    private static final long DELTA = 100000000L;

    private final static String SQL_CREATE_TABLE = "BEGIN"+
            "DECLARE CONTINUE HANDLER FOR SQLSTATE '42710' BEGIN END;"+
            "EXECUTE IMMEDIATE '"+
            "CREATE TABLE #tableName"
            +"("
                + "id bigint NOT NULL GENERATED ALWAYS AS IDENTITY CONSTRAINT #tableName_PK PRIMARY KEY,"
                + "value bigint NOT NULL,"
                + "name varchar(32) NOT NULL CONSTRAINT #tableName_UNIQUE_IDX_NAME UNIQUE,"
                + "gmt_create TIMESTAMP NOT NULL,"
                + "gmt_modified TIMESTAMP NOT NULL"
            +")';"
            +"END;";

    private final static String SQL_INSERT_RANGE = "MERGE INTO #tableName AS t1"+
            "USING (VALUES(?,?,?,?)) AS t2(name,value,gmt_create,gmt_modified)"+
            "ON t1.name=t2.name"+
            "WHEN NOT MATCHED THEN INSERT (name,value,gmt_create,gmt_modified)"
            + " VALUES(t2.name,t2.value,t2.gmt_create,t2.gmt_modified)";

    private final static String SQL_UPDATE_RANGE = "UPDATE #tableName SET value=?,gmt_modified=? WHERE name=? AND "
            + "value=?";

    private final static String SQL_SELECT_RANGE = "SELECT value FROM #tableName WHERE name=?";

    /**
     * 创建表
     *
     * @param dataSource DB来源
     * @param tableName  表名
     */
    static void createTable(DataSource dataSource, String tableName) {

        Connection conn = null;
        Statement stmt = null;

        try {
            conn = dataSource.getConnection();
            stmt = conn.createStatement();
            stmt.executeUpdate(SQL_CREATE_TABLE.replaceAll("#tableName", tableName));
        } catch (SQLException e) {
            throw new SeqException(e);
        } finally {
            closeQuietly(stmt);
            closeQuietly(conn);
        }
    }

    /**
     * 插入数据区间
     *
     * @param dataSource DB来源
     * @param tableName  表名
     * @param name       区间名称
     * @param stepStart  初始位置
     */
    private static void insertRange(DataSource dataSource, String tableName, String name, long stepStart) {

        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = dataSource.getConnection();
            stmt = conn.prepareStatement(SQL_INSERT_RANGE.replace("#tableName", tableName));
            stmt.setString(1, name);
            stmt.setLong(2, stepStart);
            stmt.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
            stmt.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new SeqException(e);
        } finally {
            closeQuietly(stmt);
            closeQuietly(conn);
        }
    }

    /**
     * 更新区间，乐观策略
     *
     * @param dataSource DB来源
     * @param tableName  表名
     * @param newValue   更新新数据
     * @param oldValue   更新旧数据
     * @param name       区间名称
     * @return 成功/失败
     */
    static boolean updateRange(DataSource dataSource, String tableName, Long newValue, Long oldValue, String name) {

        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = dataSource.getConnection();
            stmt = conn.prepareStatement(SQL_UPDATE_RANGE.replace("#tableName", tableName));
            stmt.setLong(1, newValue);
            stmt.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
            stmt.setString(3, name);
            stmt.setLong(4, oldValue);
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            throw new SeqException(e);
        } finally {
            closeQuietly(stmt);
            closeQuietly(conn);
        }
    }

    /**
     * 查询区间，如果区间不存在，会新增一个区间，并返回null，由上层重新执行
     *
     * @param dataSource DB来源
     * @param tableName  来源
     * @param name       区间名称
     * @param stepStart  初始位置
     * @return 区间值
     */
    static Long selectRange(DataSource dataSource, String tableName, String name, long stepStart) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        long oldValue;

        try {
            conn = dataSource.getConnection();
            stmt = conn.prepareStatement(SQL_SELECT_RANGE.replace("#tableName", tableName));
            stmt.setString(1, name);

            rs = stmt.executeQuery();
            if (!rs.next()) {
                // 没有此类型数据，需要初始化
                insertRange(dataSource, tableName, name, stepStart);
                return null;
            }
            oldValue = rs.getLong(1);

            if (oldValue < 0) {
                String msg = "Sequence value cannot be less than zero, value = " + oldValue
                        + ", please check table sequence" + tableName;
                throw new SeqException(msg);
            }

            if (oldValue > Long.MAX_VALUE - DELTA) {
                String msg = "Sequence value overflow, value = " + oldValue + ", please check table sequence"
                        + tableName;
                throw new SeqException(msg);
            }

            return oldValue;
        } catch (SQLException e) {
            throw new SeqException(e);
        } finally {
            closeQuietly(rs);
            closeQuietly(stmt);
            closeQuietly(conn);
        }
    }

    private static void closeQuietly(AutoCloseable closeable) {
        if (null != closeable) {
            try {
                closeable.close();
            } catch (Throwable e) {
                // Ignore
            }
        }
    }

}
