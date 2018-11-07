package com.my.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class SqliteUtil {

	public static void main(String[] args) throws Exception {
		add();
	}

	public static void add() throws ClassNotFoundException, SQLException {
		String dbpath = "G:\\studycode\\synonym.db";
		Class.forName("org.sqlite.JDBC");
		Connection conn = DriverManager.getConnection("jdbc:sqlite:"+dbpath);
		PreparedStatement prep = conn.prepareStatement("insert into syno values (?, ?);");
		
		List<String> list = getFileLine("G:\\studycode\\pythoncode\\最全同义词词库.txt");
		for (String line : list) {
			String[] cs = line.split(",|，");
			prep.setString(1, cs[0]);
			prep.setString(2, cs[1]);
			prep.addBatch();
		}
		list = getFileLine("G:\\studycode\\pythoncode\\同义词.txt");
		for (String line : list) {
			String[] cs = line.split("=");
			prep.setString(1, cs[0]);
			prep.setString(2, cs[1]);
			prep.addBatch();
		}
		conn.setAutoCommit(false);
		prep.executeBatch();
		conn.setAutoCommit(true);
		conn.close();
	}
	
	public static List<String> getFileLine(String filePath){
		List<String> list = new ArrayList<>();
		File file = new File(filePath);
		// BufferedReader:从字符输入流中读取文本，缓冲各个字符，从而实现字符、数组和行的高效读取。
		BufferedReader buf = null;
		try {
			// FileReader:用来读取字符文件的便捷类。
			buf = new BufferedReader(new FileReader(file));
			// buf = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
			String temp = null;
			while ((temp = buf.readLine()) != null) {
				list.add(temp);
			}
		} catch (Exception e) {
			e.getStackTrace();
		} finally {
			if (buf != null) {
				try {
					buf.close();
				} catch (IOException e) {
					e.getStackTrace();
				}
			}
		}
		return list;
	};
	
	
	public static void createTable() throws Exception {
		String dbpath = "G:\\studycode\\synonym.db";
		String querySql = "SELECT COUNT(*)  as CNT FROM sqlite_master where type='table' and name='syno'";
		Class.forName("org.sqlite.JDBC");
		Connection conn = DriverManager.getConnection("jdbc:sqlite:"+dbpath);
		Statement stat = conn.createStatement();
		ResultSet rs = stat.executeQuery(querySql);
		if(rs.next()) {
			System.out.println(rs.getInt(1));
		}
		stat.executeUpdate("drop table if exists syno;");
		stat.executeUpdate("create table syno (one, two);");
		rs = stat.executeQuery(querySql);
		if(rs.next()) {
			System.out.println(rs.getInt(1));
		}
	}

	public static void test() throws Exception {
		Class.forName("org.sqlite.JDBC");
		Connection conn = DriverManager.getConnection("jdbc:sqlite:test.db");
		Statement stat = conn.createStatement();
		stat.executeUpdate("drop table if exists people;");
		stat.executeUpdate("create table people (name, occupation);");
		PreparedStatement prep = conn.prepareStatement("insert into people values (?, ?);");

		prep.setString(1, "Gandhi");
		prep.setString(2, "politics");
		prep.addBatch();
		prep.setString(1, "Turing");
		prep.setString(2, "computers");
		prep.addBatch();
		prep.setString(1, "Wittgenstein");
		prep.setString(2, "smartypants");
		prep.addBatch();

		conn.setAutoCommit(false);
		prep.executeBatch();
		conn.setAutoCommit(true);

		ResultSet rs = stat.executeQuery("select * from people;");
		while (rs.next()) {
			System.out.println("name = " + rs.getString("name"));
			System.out.println("job = " + rs.getString("occupation"));
		}
		rs.close();
		conn.close();
	}

}
