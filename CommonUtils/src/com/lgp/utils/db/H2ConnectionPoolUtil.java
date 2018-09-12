package com.lgp.utils.db;

import java.sql.Connection;
import java.sql.SQLException;

import org.h2.jdbcx.JdbcConnectionPool;

public class H2ConnectionPoolUtil {
	private static H2ConnectionPoolUtil cp = null;
	private JdbcConnectionPool jdbcCP = null;
	private String h2DBPath = "db.h2.path";

	private H2ConnectionPoolUtil() {
		String dbPath = System.getProperty(h2DBPath);
		jdbcCP = JdbcConnectionPool.create("jdbc:h2:" + dbPath, "sa", "");
		jdbcCP.setMaxConnections(50);
	}

	public static H2ConnectionPoolUtil getInstance() {
		if (cp == null) {
			cp = new H2ConnectionPoolUtil();
		}
		return cp;
	}

	public Connection getConnection() throws SQLException {
		return jdbcCP.getConnection();
	}
}
