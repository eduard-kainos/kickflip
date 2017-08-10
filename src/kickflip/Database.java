package kickflip;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Database {

	private Connection conn;
	private Statement st;

	static Connection getConnection() throws Exception {
        String url = "jdbc:mysql://192.168.1.117/";
        String dbName = "kickflip";
        String driver = "com.mysql.jdbc.Driver";
        String userName = "developer";
        String password = "p@ssword1";

        Class.forName(driver).newInstance();
        Connection conn = DriverManager.getConnection(url + dbName, userName,password);

        return conn;
    }
	
	static Connection getConnection(String userName, String password) throws Exception {
		String url = "jdbc:mysql://192.168.1.117/";
		String dbName = "kickflip";
		String driver = "com.mysql.jdbc.Driver";
		String useSSL = "?useSSL=false";

		Class.forName(driver).newInstance();
		Connection conn = DriverManager.getConnection(url + dbName + useSSL, userName, password);

		return conn;
	}
	
	public static void closeConnection(Connection conn) {

        try {
            conn.close();
        } catch (SQLException e) {
        		System.out.println("Close DB error!");
        }

    }
	    
}
