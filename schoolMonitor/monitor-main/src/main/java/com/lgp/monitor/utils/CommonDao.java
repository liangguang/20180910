package com.lgp.monitor.utils;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CommonDao {

	private final static Logger logger = LoggerFactory.getLogger(CommonDao.class);

	public static void CreateTable(String tbName) {

		Statement statement = null;

		Connection connection = null;

		ResultSet rs = null;
		try {

			connection = new ConnectionPool().getConnection();

			DatabaseMetaData metaData = connection.getMetaData();

			rs = metaData.getTables(null, null, tbName, new String[] { "TABLE" });
			if (rs.next()) {
				System.out.println(rs.getString("TABLE_NAME"));
			} else {
				statement = connection.createStatement();
				statement.execute(
						"Create Table " + tbName + " (clueid varchar(40),updatetime varchar(20), PRIMARY KEY(clueid))");
			}
		} catch (Throwable e) {
			logger.error("查询H2数据库表信息失败," + e.getMessage(), e);
		} finally {
			try {
				releaseConnection(connection, statement, rs);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

	}

	public static void addInfo(String tbName, String c1, String c2) {
		Statement statement = null;
		Connection connection = null;
		ResultSet rs = null;
		try {
			connection = new ConnectionPool().getConnection();
			statement = connection.createStatement();
			System.out.println("insert into " + tbName + " values ('" + c1 + "','" + c2 + "')");
			boolean flag = statement.execute("insert into " + tbName + " values ('" + c1 + "','" + c2 + "')");
			logger.debug("执行插入数据结构：" + flag);
		} catch (Throwable e) {
			logger.error("插入H2数据库表数据失败," + e.getMessage(), e);
		} finally {
			try {
				releaseConnection(connection, statement, rs);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

	}

	public static void updateTime(String c1, String c2) {

	}

	private static void releaseConnection(Connection conn, Statement stmt, ResultSet rs) throws SQLException {
		if (rs != null) {
			rs.close();
		}
		if (stmt != null) {
			stmt.close();
		}
		if (conn != null) {
			conn.close();
		}
	}

	public static boolean getTableData(String tbName, String clueid) {
		PreparedStatement statement = null;

		boolean flag = false;

		Connection connection = null;

		ResultSet rs = null;
		try {
			connection = new ConnectionPool().getConnection();
			statement = connection.prepareStatement("select clueid,updatetime from " + tbName + " where clueid = ?");
			statement.setString(1, clueid);
			rs = statement.executeQuery();
			if (rs.next()) {
				System.out.println(rs.getString("clueid") + ":" + rs.getString("updatetime"));
				flag = true;
			}
		} catch (Throwable e) {
			logger.error("查询数据时有错误：id=" + clueid, e);
		} finally {
			try {
				releaseConnection(connection, statement, rs);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return flag;
	}

	public static void CreateTranscodeTable(String tbName) {

		Statement statement = null;

		Connection connection = null;

		ResultSet rs = null;
		try {

			connection = new ConnectionPool().getConnection();

			DatabaseMetaData metaData = connection.getMetaData();

			rs = metaData.getTables(null, null, tbName, new String[] { "TABLE" });
			if (rs.next()) {
				System.out.println(rs.getString("TABLE_NAME"));
			} else {
				try (PreparedStatement stmt = connection
						.prepareStatement("CREATE TABLE " + tbName + "(" + "ARCTASKID VARCHAR(255) PRIMARY KEY,"
								+ "ACTIVITYINSTANCEID VARCHAR(255)," + "WORKITEMID VARCHAR(255),"
								+ "STATUS VARCHAR(255), PROGRESS INT," + "MOID VARCHAR(255)," + "DEVICEID VARCHAR(255),"
								+ "ROOTPATH VARCHAR(255)," + "TASKNAME VARCHAR(255)," + "EXTINFO OTHER)")) {
					stmt.execute();
				}
			}
		} catch (Throwable e) {
			logger.error("创建H2转码表信息失败," + e.getMessage(), e);
		} finally {
			try {
				releaseConnection(connection, statement, rs);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

	}

	public static void main(String[] args) {

		CommonDao.CreateTable("ceshitable");
		// CommonDao.addInfo("1231", "aasd");

	}

}