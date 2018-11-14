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

import com.baidu.aip.nlp.AipNlp;
import com.my.service.NplService;

public class SqliteUtil {

	public static void main(String[] args) throws Exception {
		test();
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
		Connection conn = DriverManager.getConnection("jdbc:sqlite:G:\\studycode\\pythoncode\\baozhi\\synonym.db");
		Statement stat = conn.createStatement();
		ResultSet rs = stat.executeQuery("select * from syno;");
		AipNlp client = NplService.getClient();
		while (rs.next()) {
			try {
				String one =  rs.getString("one");
				String two =  rs.getString("two");
				double score = NplService.sim(client,one,two,1);
				String content = one + "|" +two + "=" + score ;
				System.out.println(content);
				FileWriteTxtUtil.appendFile2("G:\\studycode\\1.txt", content);
			} catch (Throwable e) {
				System.err.println(e.getMessage());
			}
		}
		rs.close();
		conn.close();
	}

}
