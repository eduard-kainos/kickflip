package kickflip;

import java.sql.*;

public class Main {

	public static void main(String[] args) {
		System.out.println("Test");
		
		try {
			Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/kickflip", "root", "p@ssword");
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery("SELECT employeeNumber, name FROM employee LIMIT 5");
			while (rs.next()) {
				String out = String.format("%s has the following employee number: %s.", rs.getString("name"),
						rs.getString("employeeNumber"));
				System.out.println(out);
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}

}
