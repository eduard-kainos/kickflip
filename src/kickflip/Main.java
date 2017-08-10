package kickflip;

import java.math.*;
import java.sql.*;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		System.out.println("Test");
	
		System.out.println("Please select an option: \n1: Add an employee. \n2: Exit");
		
		Scanner sc = new Scanner(System.in);
		//Database db = new Database();
		
		int option = sc.nextInt();
		switch (option) {
		case 1: 
			insertEmployee();
			break;
		case 2:
			System.out.println("Goodbye!");
			System.exit(0);
			break;
		default:
			break;
		}
		sc.close();
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


}
