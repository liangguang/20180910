package com.lgp.utils.db;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class H2DaoExample {

	public static void crateTable() throws SQLException {
		Connection conn = null;
		Statement stmt = null;
		try {
			conn = H2ConnectionPoolUtil.getInstance().getConnection();
			DatabaseMetaData meta = conn.getMetaData();

			ResultSet rsTables = meta.getTables(null, null, "FILESTATUS",
					new String[] { "TABLE" });
			if (!rsTables.next()) {
				stmt = conn.createStatement();
				stmt.execute("CREATE TABLE FILESTATUS(FILEPATH VARCHAR(1024),LASTMODIFYTIME VARCHAR(1024),STATUS VARCHAR(1024),PRIMARY KEY(FILEPATH,LASTMODIFYTIME))");
			}
			rsTables.close();
		} finally {
			releaseConnection(conn, stmt, null);
		}
	}

	public static void addFile(String filePath, long lastModifyTime,
			String status) throws SQLException {
		Connection conn = null;
		PreparedStatement stmt = null;
		try {
			conn = H2ConnectionPoolUtil.getInstance().getConnection();

			stmt = conn
					.prepareStatement("INSERT INTO FILESTATUS VALUES(?,?,?)");
			stmt.setString(1, filePath);
			stmt.setString(2, String.valueOf(lastModifyTime));
			stmt.setString(3, status);
			stmt.execute();

		} finally {
			releaseConnection(conn, stmt, null);
		}
	}

	public static boolean isFileExits(String filePath, long lastModifyTime)
			throws SQLException {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			conn = H2ConnectionPoolUtil.getInstance().getConnection();
			stmt = conn
					.prepareStatement("SELECT FILEPATH FROM FILESTATUS WHERE FILEPATH=? AND LASTMODIFYTIME=?");
			stmt.setString(1, filePath);
			stmt.setString(2, String.valueOf(lastModifyTime));
			rs = stmt.executeQuery();
			return rs.next();
		} finally {
			releaseConnection(conn, stmt, rs);
		}
	}

	private static void releaseConnection(Connection conn, Statement stmt,
			ResultSet rs) throws SQLException {
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

}