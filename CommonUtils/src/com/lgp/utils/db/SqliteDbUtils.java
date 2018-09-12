package com.lgp.utils.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SqliteDbUtils {

	private Statement statement;
	private Connection connection;
	private String querySql;

	public SqliteDbUtils(String dbPath) throws SQLException,ClassNotFoundException{
		Class.forName("org.sqlite.JDBC");
		this.connection = DriverManager.getConnection("jdbc:sqlite:"
				+ dbPath);
		statement = connection.createStatement();
		statement.setQueryTimeout(0);
	}
	
	public SqliteDbUtils(Connection connection) throws SQLException {
		this.connection = connection;
		statement = connection.createStatement();
		statement.setQueryTimeout(0);
	}

	public int executeSql(String sql) throws SQLException {
		// 执行sql语句
		querySql = sql;
		return statement.executeUpdate(sql);
	}

	public int insert(String table, HashMap<String, Object> hashMap) throws SQLException {
		StringBuilder builderKey = new StringBuilder();
		StringBuilder builderVal = new StringBuilder();
		for (Map.Entry<String, Object> entry : hashMap.entrySet()) {
			builderKey.append(entry.getKey() + ",");
			if (entry.getValue() instanceof String) {
				builderVal.append("'" + ((String) entry.getValue()).replace("'", "''") + "'" + ",");
			} else {
				builderVal.append(entry.getValue() + ",");
			}
		}
		String key = builderKey.toString().substring(0, builderKey.toString().length() - 1);
		String val = builderVal.toString().substring(0, builderVal.toString().length() - 1);
		return executeSql("insert into " + table + "(" + key + ") values(" + val + ")");
	}

	public int update(String table, HashMap<String, Object> hashMap, String where) throws SQLException {
		StringBuilder builderKey = new StringBuilder();
		StringBuilder builderVal = new StringBuilder();
		StringBuilder keyVal = new StringBuilder();
		for (Map.Entry<String, Object> entry : hashMap.entrySet()) {
			builderKey.append(entry.getKey());
			if (entry.getValue() instanceof String) {
				builderVal.append("'" + ((String) entry.getValue()).replace("'", "''") + "'");
			} else {
				builderVal.append(entry.getValue());
			}
			keyVal.append(builderKey.toString() + "=" + builderVal.toString() + ",");
			builderKey.delete(0, builderKey.length());
			builderVal.delete(0, builderVal.length());
		}
		String kv = keyVal.toString().substring(0, keyVal.toString().length() - 1);
		return executeSql("update " + table + " set " + kv + " where " + where);
	}

	public int delete(String table, String where) throws SQLException {
		return executeSql("delete from " + table + " where " + where);
	}

	public int getCount(String tab, String where) throws SQLException {
		int count = 0;
		where = where != null && !where.trim().equals("") ? " where " + where : "";
		querySql = "select count(*) as count from " + tab + where + " limit 1";
		ResultSet rSet = statement.executeQuery(querySql);
		while (rSet.next()) {
			count = rSet.getInt("count");
		}
		return count;
	}

	public int getCount(String tab) throws SQLException {
		return getCount(tab, null);
	}

	public ArrayList<HashMap<String, Object>> select(String tab, String[] columns, String where, String limit)
			throws SQLException {
		String column = "*";
		if (columns != null) {
			StringBuilder builder = new StringBuilder();
			for (int i = 0; i < columns.length; i++) {
				builder.append(columns[i] + ",");
			}
			column = builder.toString().substring(0, builder.toString().length() - 1);
		}
		where = where != null && !where.trim().equals("") ? " where " + where : "";
		limit = limit != null && !limit.trim().equals("") ? " limit " + limit : "";
		return query("select " + column + " from " + tab + where + limit);
	}

	public ArrayList<HashMap<String, Object>> select(String tab, String[] columns) throws SQLException {
		return select(tab, columns, null, null);

	}

	public ArrayList<HashMap<String, Object>> select(String tab) throws SQLException {
		return select(tab, null, null, null);

	}

	public ArrayList<HashMap<String, Object>> select(String tab, String where) throws SQLException {
		return select(tab, null, where, null);

	}

	public ArrayList<HashMap<String, Object>> query(String sql) throws SQLException {
		querySql = sql;
		ResultSet rs = statement.executeQuery(sql);
		ResultSetMetaData data = rs.getMetaData();
		ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
		HashMap<String, Object> hashMap;
		while (rs.next()) {
			hashMap = new HashMap<String, Object>();
			for (int i = 1; i < data.getColumnCount() + 1; i++) {
				hashMap.put(data.getColumnName(i), rs.getObject(data.getColumnName(i)));
			}
			list.add(hashMap);
		}
		return list;
	}

	public void close() {
		try {
			if (connection != null)
				connection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public String getQuery() {
		return querySql;
	}
}
