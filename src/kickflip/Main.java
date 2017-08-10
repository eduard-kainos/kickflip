package kickflip;

import java.math.*;
import java.sql.*;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		System.out.println("Test");
	
		System.out.println("Please select an option:"
				+ " \n1: Add an employee."
				+ " \n2: Show employee table."
				+ " \n3: Exit");
		
		Scanner sc = new Scanner(System.in);
		
		int option = sc.nextInt();
		switch (option) {
		case 1: 
			insertEmployee();
			break;
		case 2:
			select();
			break;
		case 3:
			System.out.println("Goodbye!");
			System.exit(0);
			break;
		default:
			break;
		}
		sc.close();
	}
	
	public static void select() {
		Connection conn = null;
        PreparedStatement statement = null;
        ResultSet rs = null;
        String query = "SELECT employeeNumber, name FROM employee";

        try {
        		conn = Database.getConnection();
            statement = conn.prepareStatement(query);
            rs = statement.executeQuery();
            
			while (rs.next()) {
				String out = String.format("%s has the following employee number: %s.", rs.getString("name"),
						rs.getString("employeeNumber"));
				System.out.println(out);
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			Database.closeConnection(conn);
        }
	}
	
	public static void insertEmployee() {
		Scanner sc = new Scanner(System.in);
		try {
			Connection conn = DriverManager.getConnection("jdbc:mysql://192.168.1.117/kickflip", "developer", "p@ssword1");
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery("SELECT employeeNumber, name FROM employee");
			
			// the mysql insert statement
			String query = 	"INSERT INTO "	+ 
						   	"employee(name, address, initialSalary, nin, bankAccountNo, sortCode, departmentID)"	+
							"VALUES (?, ?, ?, ?, ?, ?, ?);"	;
			
			System.out.println("Please enter the employees name");
			String name = sc.nextLine();
			
			System.out.println("Please enter the employees address");
			String address = sc.nextLine();
			
			System.out.println("Please enter the starting salary");
			BigDecimal initialSalary = sc.nextBigDecimal();
			sc.nextLine();
			
			System.out.println("Please enter the employees national insurance number (9 characters)");
			String nin = sc.nextLine();
			
			System.out.println("Please enter the employees bank account number");
			String bankAccount = sc.nextLine();
			
			System.out.println("Please enter the employees sort code number");
			String sortCode = sc.nextLine();
			
			System.out.println("Please enter the employees department ID");
			int departID = sc.nextInt();
			sc.nextLine();
			
		      // create the mysql insert preparedstatement
		      PreparedStatement preparedStmt = conn.prepareStatement(query);
		      preparedStmt.setString 	(1, name);
		      preparedStmt.setString		(2, address);
		      preparedStmt.setBigDecimal (3, initialSalary);
		      preparedStmt.setString		(4, nin);
		      preparedStmt.setString    	(5, bankAccount);
		      preparedStmt.setString    	(6, sortCode);
		      preparedStmt.setInt   		(7, departID);
		      
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
	
	
	
	/*
	public void insertQuery(String name) {

		if (!error) {
			try {
				// the mysql insert statement
				String query = "INSERT INTO "
						+ "employee(name, address, initialSalary, nin, bankAccountNo, sortCode, departmentID)"
						+ "VALUES (?, ?, ?, ?, ?, ?, ?);";

				// create the mysql insert preparedstatement
				PreparedStatement preparedStmt = this.conn.prepareStatement(query);
				preparedStmt.setString(1, name);
				preparedStmt.setString(2, "Address test");
				preparedStmt.setBigDecimal(3, new BigDecimal(12345.02));
				preparedStmt.setString(4, "SS123456D");
				preparedStmt.setString(5, "12345678");
				preparedStmt.setString(6, "123456");
				preparedStmt.setInt(7, 1);

				// execute the preparedstatement
				preparedStmt.execute();
				closeDB();
			} catch (SQLException e1) {
				error = true;
				e1.printStackTrace();
			}
		} else {
			System.out.println("Some error occured");
		}

	}
	*/


}
