package kickflip;

import java.math.*;
import java.sql.*;

public class Main {

	public static void main(String[] args) {
		System.out.println("Test");
		
		
		
		try {
			Connection conn = DriverManager.getConnection("jdbc:mysql://192.168.1.117/kickflip", "developer", "p@ssword1");
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery("SELECT employeeNumber, name FROM employee LIMIT 5");
			
			// the mysql insert statement
			String query = 	"INSERT INTO "	+ 
						   	"employee(name, address, initialSalary, nin, bankAccountNo, sortCode, departmentID)"	+
							"VALUES (?, ?, ?, ?, ?, ?, ?);"	;
		     
		      // create the mysql insert preparedstatement
		      PreparedStatement preparedStmt = conn.prepareStatement(query);
		      preparedStmt.setString 	(1, "JavaTEST");
		      preparedStmt.setString		(2, "Address test");
		      preparedStmt.setBigDecimal (3, new BigDecimal(12345.02));
		      preparedStmt.setString		(4, "SS123456D");
		      preparedStmt.setString    	(5, "12345678");
		      preparedStmt.setString    	(6, "123456");
		      preparedStmt.setInt   		(7, 1);
		      
		      // execute the preparedstatement
		      preparedStmt.execute();
		      
			
			while (rs.next()) {
				String out = String.format("%s has the following employee number: %s.", rs.getString("name"),
						rs.getString("employeeNumber"));
				System.out.println(out);
			}
			conn.close();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		
		
		
	}


}
