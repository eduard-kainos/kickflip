package kickflip;

import java.math.*;
import java.sql.*;
import java.util.Scanner;

public class Main {

	private static Connection conn;
	private static PreparedStatement statement;
	private static ResultSet rs;
	private static String query;
	private static PreparedStatement preparedStmt;

	public static void main(String[] args) {
		System.out.println("Test");

		System.out.println(
				"Please select an option:" + " \n1: Add an employee." + " \n2: Show employee table." + " \n3: Exit");

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
		conn = null;
		statement = null;
		rs = null;
		query = "SELECT e.name, d.departmentName FROM employee e JOIN department d ON e.departmentID = d.departmentID ORDER BY d.departmentName;";

		try {
			conn = Database.getConnection();
			statement = conn.prepareStatement(query);
			rs = statement.executeQuery();

			String department = "" , name, newDPT;
			while (rs.next()) {
				name = rs.getString("name");
				newDPT = rs.getString("departmentName");
				if( !newDPT.equals(department)) {
					department = newDPT;
					System.out.println("\n ***** " + department + " ***** " );
				}
				System.out.println(" - " + name);
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			Database.closeConnection(conn);
		}
	}

	public static void insertEmployee() {
		conn = null;
		statement = null;
		preparedStmt = null;

		Scanner sc = new Scanner(System.in);

		String query = "INSERT INTO "
				+ "employee(name, address, initialSalary, nin, bankAccountNo, sortCode, departmentID)"
				+ "VALUES (?, ?, ?, ?, ?, ?, ?);";

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

		try {
			conn = Database.getConnection();
			statement = conn.prepareStatement(query);

			// create the mysql insert preparedstatement
			preparedStmt = conn.prepareStatement(query);
			preparedStmt.setString(1, name);
			preparedStmt.setString(2, address);
			preparedStmt.setBigDecimal(3, initialSalary);
			preparedStmt.setString(4, nin);
			preparedStmt.setString(5, bankAccount);
			preparedStmt.setString(6, sortCode);
			preparedStmt.setInt(7, departID);

			// execute the preparedstatement
			preparedStmt.execute();
		} catch (SQLException e1) {
			e1.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			Database.closeConnection(conn);
			System.out.println( name + " has been successfully added.");
		}
		
	}
}
