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
	private static String username, password;
	private static Boolean loggedin = false;

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		int repeatInt = 0;

		while (repeatInt == 0) {
			if (loggedin) {
				printMenu();
				String text = sc.nextLine();
				int option = 0;
				try {
					option = Integer.parseInt(text);
				} catch (Exception e) {
					System.out.println("Wrong input.");
				}

				switch (option) {
				case 1:
					insertEmployee();
					break;
				case 2:
					select();
					break;
				case 3:
					System.out.println("Goodbye!");
					repeatInt++;
					System.exit(0);
					break;
				case 4:
					insertSalesEmployee();
				default:
					break;
				}
			}else {
				loggedin = loginScreen(sc);
			}
		}

		sc.close();
	}

	public static boolean loginScreen(Scanner scan) {
		System.out.println("Please login to system: \n");
		System.out.println("Please enter your username: ");
		username = scan.nextLine();
		System.out.println("Please enter your pasword: ");
		password = scan.nextLine();

		try {
			conn = Database.getConnection(username, password);
			Database.closeConnection(conn);
			System.out.println("Successful connection!");
		} catch (Exception e) {
			System.out.println("Connection refused.");
			return false;
		}

		return true;
	}

	public static void printMenu() {
		System.out.println("Please select an option:" + " \n1: Add an employee." + " \n2: Show employee table."
				+ " \n3: Exit." + " \n4: Add sales employee.");
	}

	public static void select() {
		conn = null;
		statement = null;
		rs = null;
		query = "SELECT e.name, d.departmentName FROM employee e JOIN department d ON e.departmentID = d.departmentID ORDER BY d.departmentName;";

		try {
			conn = Database.getConnection(username, password);
			statement = conn.prepareStatement(query);
			rs = statement.executeQuery();

			String department = "", name, newDPT;
			while (rs.next()) {
				name = rs.getString("name");
				newDPT = rs.getString("departmentName");
				if (!newDPT.equals(department)) {
					department = newDPT;
					System.out.println("\n ***** " + department + " ***** ");
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

		System.out.println("Please enter the employees bank account number (8 digits)");
		String bankAccount = sc.nextLine();

		System.out.println("Please enter the employees sort code number (6 digits)");
		String sortCode = sc.nextLine();

		System.out.println(
				"Please enter the employees department ID \n1 for Evolve \n2 for Enterprise \n3 for Government");
		int departID = sc.nextInt();
		sc.nextLine();

		try {
			conn = Database.getConnection(username, password);
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
			System.out.println(name + " has been successfully added.");
		}

	}

	public static void insertSalesEmployee() {
		conn = null;
		statement = null;
		preparedStmt = null;

		Scanner sc = new Scanner(System.in);

		String query = "INSERT INTO "
				+ "employee(name, address, initialSalary, nin, bankAccountNo, sortCode, departmentID)"
				+ "VALUES (?, ?, ?, ?, ?, ?, ?);";

		System.out.println("Please enter the sales employees name");
		String name = sc.nextLine();

		System.out.println("Please enter the sales employees address");
		String address = sc.nextLine();

		System.out.println("Please enter the sales employee starting salary");
		BigDecimal initialSalary = sc.nextBigDecimal();
		sc.nextLine();

		System.out.println("Please enter the sales employees national insurance number (9 characters)");
		String nin = sc.nextLine();

		System.out.println("Please enter the sales employees bank account number (8 digits)");
		String bankAccount = sc.nextLine();

		System.out.println("Please enter the sales employees sort code number (6 digits)");
		String sortCode = sc.nextLine();

		System.out.println(
				"Please enter the sales employees department ID \n1 for Evolve \n2 for Enterprise \n3 for Government");
		int departID = sc.nextInt();
		sc.nextLine();

		System.out.println("Please enter the sales employee commission rate");
		BigDecimal commission = sc.nextBigDecimal();
		sc.nextLine();

		System.out.println("Please enter the sales employee sales total");
		BigDecimal salesTotal = sc.nextBigDecimal();
		sc.nextLine();

		String query2 = "INSERT INTO " + "salesEmployee(employeeNumber, commissionRate, salesTotal)"
				+ "VALUES (?, ?, ?);";

		try {
			conn = Database.getConnection(username, password);
			statement = conn.prepareStatement(query);

			// create the mysql insert preparedstatement
			preparedStmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			preparedStmt.setString(1, name);
			preparedStmt.setString(2, address);
			preparedStmt.setBigDecimal(3, initialSalary);
			preparedStmt.setString(4, nin);
			preparedStmt.setString(5, bankAccount);
			preparedStmt.setString(6, sortCode);
			preparedStmt.setInt(7, departID);

			// execute the preparedstatement
			preparedStmt.execute();

			ResultSet resultSet = preparedStmt.getGeneratedKeys();
			int empNumCurrent = 0;

			while (resultSet.next()) {
				empNumCurrent = resultSet.getInt(1);
			}

			statement = conn.prepareStatement(query2);
			preparedStmt = conn.prepareStatement(query2);
			preparedStmt.setInt(1, empNumCurrent);
			preparedStmt.setBigDecimal(2, commission);
			preparedStmt.setBigDecimal(3, salesTotal);

			preparedStmt.execute();

		} catch (SQLException e1) {
			e1.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			Database.closeConnection(conn);
			System.out.println(name + " has been successfully added.");
		}

	}
}
