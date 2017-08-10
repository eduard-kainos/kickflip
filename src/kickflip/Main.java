package kickflip;

import java.math.*;
import java.sql.*;
import java.util.HashMap;
import java.util.Scanner;

public class Main {

	private static Connection conn;
	private static PreparedStatement statement;
	private static ResultSet rs;
	private static String query;
	private static PreparedStatement preparedStmt;
	private static String username, password;
	private static Boolean loggedin = false;
	
	private static final String[] users = { "developer", "PeopleTeam", "FinanceTeam", "SalesTeam", "TalentManager" };
	private static final String[] options = { "Add an employee.", "Show employee table.", "Add sales employee.", "Employee's pay for this month.",
											"Employee with highest sales total.", "Create new project.", "Exit." };
	private static HashMap<String, int[]> hmap = new HashMap<String, int[]>();
	private static final int[] dev = { 0, 1, 2, 3, 4, 5, 6 };
	private static final int[] people = { 0, 1, 2, 6 };
	private static final int[] sales = { 4, 6 };
	private static final int[] finance = { 3, 6 };
	private static final int[] talent = { 5, 6 };

	public static void main(String[] args) {

		hmap.put("developer", dev);
		hmap.put("PeopleTeam", people);
		hmap.put("SalesTeam", sales);
		hmap.put("FinanceTeam", finance);
		hmap.put("TalentManager", talent);
		
		Scanner sc = new Scanner(System.in);
		int repeatInt = 0;

		while (repeatInt == 0) {
			if (loggedin) {
				printMenu(username);
				String text = sc.nextLine();
				int option = 0;
				try {
					option = Integer.parseInt(text);
				} catch (Exception e) {
					System.out.println("Wrong input.");
				}
				
				int[] arr = hmap.get(username);
				
				switch (arr[option-1] + 1) {
				case 1:
					insertEmployee();
					break;
				case 2:
					select();
					break;
				case 3:
					insertSalesEmployee();
					break;
				case 4:
					employeeGrossPay();
					break;
				case 5:
					highestSalesPeriod();
					break;
				case 6:
					createProject();
					break;
				case 7:
					System.out.println("Goodbye!");
					repeatInt++;
					System.exit(0);
					break;
				default:
					break;
				}
			} else {
				loggedin = loginScreen(sc);
			}
		}

		sc.close();
	}

	private static void highestSalesPeriod() {
		conn = null;
		statement = null;
		rs = null;
		query = "Select * from SalesTeam;";

		try {
			conn = Database.getConnection(username, password);
			statement = conn.prepareStatement(query);
			rs = statement.executeQuery();
			String name = "";
			BigDecimal salesTotal = null;

			while (rs.next()) {
				name = rs.getString("name");
				salesTotal = rs.getBigDecimal("salesTotal");
				System.out.println("Name:" + name + "       Highest Sales Total: £" + salesTotal);
			}

		} catch (SQLException e1) {
			e1.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			Database.closeConnection(conn);
		}

	}

	public static void employeeGrossPay() {
		conn = null;
		statement = null;
		rs = null;
		query = "Select * from FinanceTeam;";

		try {
			conn = Database.getConnection(username, password);
			statement = conn.prepareStatement(query);
			rs = statement.executeQuery();
			String name = "";
			BigDecimal grossPay = null;

			while (rs.next()) {
				name = rs.getString("name");
				grossPay = rs.getBigDecimal("Gross Pay");
				System.out.println("Name:" + name + "\nGross Pay: £" + grossPay + "\n");
			}

		} catch (SQLException e1) {
			e1.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			Database.closeConnection(conn);
		}

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

	public static void printMenu(String username) {
		System.out.println("\n Please select one of the following options: \n");
		int[] arr = hmap.get(username);

		for (int i = 0; i < arr.length; i++) {
			System.out.println((i+1) + ": " + options[arr[i]]);
		}
	}

	public static void select() {
		conn = null;
		statement = null;
		rs = null;
		query = "Select * from PeopleTeam;";

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

	public static void createProject() {
		conn = null;
		statement = null;
		preparedStmt = null;

		Scanner sc = new Scanner(System.in);

		String query = "INSERT INTO " + "project(projectName)" + "VALUES (?);";

		System.out.println("Please enter the name of the new project");
		String name = sc.nextLine();

		try {
			conn = Database.getConnection(username, password);
			statement = conn.prepareStatement(query);

			// create the mysql insert preparedstatement
			preparedStmt = conn.prepareStatement(query);
			preparedStmt.setString(1, name);

			// execute the preparedstatement
			preparedStmt.execute();
		} catch (SQLException e1) {
			e1.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			Database.closeConnection(conn);
			System.out.println("The project " + name + " has been successfully added.");
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
