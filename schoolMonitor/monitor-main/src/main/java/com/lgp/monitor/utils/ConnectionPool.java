package com.lgp.monitor.utils;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.h2.jdbcx.JdbcConnectionPool;

public class ConnectionPool {
	private JdbcConnectionPool jdbcCP = null;

	private String h2DBPath;

	public ConnectionPool() {
		super();
	}

	public ConnectionPool(String h2DBPath) {
		super();
		this.h2DBPath = h2DBPath;
	}

	public void init() {
		h2DBPath = System.getProperty("publish.db.h2.path");
		if (h2DBPath == null){
			h2DBPath = System.getProperty("user.dir");
			if(h2DBPath.indexOf(":")>0){
				//有盘符会报错
				h2DBPath = "./publish-db/agent_db";
			}
			}
		jdbcCP = JdbcConnectionPool.create("jdbc:h2:" + h2DBPath
				+ ";RETENTION_TIME=5000", "sa", "");
		jdbcCP.setMaxConnections(50);
	}

	public Connection getConnection() throws SQLException {
		if (jdbcCP == null) {
			init();
		}
		return jdbcCP.getConnection();
	}

	public static void main(String[] args) throws SQLException {
		ConnectionPool cp = new ConnectionPool();
		cp.h2DBPath = "d:\\h2db\\h2db";
		try (Connection conn = cp.getConnection()) {
			DatabaseMetaData meta = conn.getMetaData();
			try (ResultSet rs = meta.getTables(null, null, "TBLDUPLICATE",
					new String[] { "TABLE" })) {
				if (!rs.next()) {
					try (PreparedStatement stmt = conn
							.prepareStatement("CREATE TABLE TBLDUPLICATE("
									+ "MOID VARCHAR(255)," + "EXTINFO OTHER)")) {
						stmt.execute();
					}
				}
				try (PreparedStatement ps = conn
						.prepareStatement("UPDATE TBLDUPLICATE SET MOID='test' where MOID = 'test'")) {
					ps.executeUpdate();
					ps.executeUpdate();

				}

			}
		}
	}
}
