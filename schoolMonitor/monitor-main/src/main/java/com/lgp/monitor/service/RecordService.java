package com.lgp.monitor.service;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.h2.jdbcx.JdbcConnectionPool;

import com.lgp.monitor.model.FileRecord;

public class RecordService {

	private static boolean initialized = false;
	private static JdbcConnectionPool pool = null;
	private static String h2DBPath = "";

	public static boolean init(String dbPath) throws SQLException {
		if (h2DBPath == null || !h2DBPath.equals(dbPath)) {
			h2DBPath = dbPath;
			initialized = false;
			if (pool != null) {
				pool.dispose();
				pool = null;
			}
		}
		if (!initialized) {
			if (pool == null)
				pool = JdbcConnectionPool.create("jdbc:h2:" + h2DBPath + ";RETENTION_TIME=5000", "sa", "");
			try (Connection conn = pool.getConnection()) {
				DatabaseMetaData meta = conn.getMetaData();
				try (ResultSet rs = meta.getTables(null, null, "TBLFILESTATUS", new String[] { "TABLE" })) {
					initialized = rs.next();
					if (!initialized) {
						try (PreparedStatement stmt = conn.prepareStatement(
								"CREATE TABLE TBLFILESTATUS(" + "FILENAME VARCHAR(255)," + "EXT OTHER)")) {
							initialized = stmt.execute();
						}
					}
				}
			}
		}
		return initialized;
	}

	public static List<FileRecord> queryFileRecord() throws SQLException {
		init(h2DBPath);
		try (Connection conn = pool.getConnection()) {
			try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM TBLFILESTATUS")) {
				try (ResultSet rs = ps.executeQuery()) {
					List<FileRecord> result = new ArrayList<FileRecord>();
					while (rs.next()) {
						FileRecord rec = (FileRecord) rs.getObject("EXT");
						result.add(rec);
					}
					return result;
				}
			}
		}
	}

	public static boolean putFileRecord(FileRecord record) throws SQLException {
		init(h2DBPath);
		try (Connection conn = pool.getConnection()) {
			try (PreparedStatement ps = conn.prepareStatement("UPDATE TBLFILESTATUS SET EXT = ? WHERE FILENAME = ?")) {
				ps.setObject(1, record);
				ps.setString(2, record.getFileName());
				if (ps.executeUpdate() < 1) {
					try (PreparedStatement psInsert = conn.prepareStatement("")) {
						return ps.executeUpdate() == 1;
					}
				} else {
					return true;
				}
			}
		}
	}

	public static void deleteFileRecord(FileRecord record) throws SQLException {
		init(h2DBPath);
		try (Connection conn = pool.getConnection()) {
			try (PreparedStatement ps = conn.prepareStatement("DELETE FROM TBLFILESTATUS WHERE FILENAME = ?")) {
				ps.setString(1, record.getFileName());
				ps.executeUpdate();
			}
		}
	}

}