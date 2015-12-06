import java.io.* ;
import java.sql.* ;
import java.util.*;
import java.util.regex.Pattern;

/*
 * ID guide:
 * 10: Shop_user
 * 15: Orders
 * 20: Order1
 * 25: Contains
 * 30: Product
 * 35: Location
 * 40: Shelf
 * 45: Supplys
 * 50: Supplier
 * 53: Inside
 * 56: Parent
 * 60: Category 
 * 63: Available
 * 66: AvailableCat
 * 70: Discount
 * 
 * 6 digits long
 */

public class Driver
{ 
	static Scanner sc = new Scanner(System.in);	
	static int loggedInUser = 0;
    
	static Connection sqlcon  = null;
    static Statement sqlStatement  = null;
    static ResultSet myResultSet  = null;
    
    final static int SHOP_USER_IDX = 0;
    final static int ORDERS_IDX = 1;
    final static int ORDER1_IDX = 2;
    final static int CONTAINS_IDX = 3;
    final static int PRODUCT_IDX = 4;
    final static int LOCATION_IDX = 5;
    final static int SHELF_IDX = 6;
    final static int SUPPLYS_IDX = 7;
    final static int SUPPLIER_IDX = 8;
    final static int INSIDE_IDX = 9;
    final static int PARENT_IDX = 10;
    final static int CATEGORY_IDX = 11;
    final static int AVAILABLE_IDX = 12;
    final static int AVAILABLECAT_IDX = 13;
    final static int DISCOUNT_IDX = 14;
    
	static int[] lastIds = new int[15];
	
	public static void main(String[] args)  throws SQLException
	{
		  //Scanner only uses newlines as delimiters
		  sc.useDelimiter(Pattern.compile("[\\r\\n;]+"));
	      try {
			  DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
		
			  // Connect to the database
			  sqlcon= DriverManager.getConnection ("jdbc:oracle:thin:hr/hr@oracle.cise.ufl.edu:1521:orcl", "kmarin", "Tuxy3530");
		
			  // Create a Statement
			  sqlStatement = sqlcon.createStatement();
			  
			  // call sqlStatement.executeQuery ()
			  //String q = "SELECT p.name, s.name FROM Product p, Shelf s, Order o, Contains c WHERE (p.id == c.product_id) AND (o.id == c.order_id) AND (s.id == p.shelf_id)";
			  
			//  String q = "INSERT INTO SHOP_USER VALUES (100, '123 Avenue', 'Kevin Marin', 'password', 'kevin@marin.com', 'true')";
			  
			  //String q = "select tablespace_name, table_name from user_tables";
			  
			  //System.out.println(q);
				
			  //String q = "select name from Shop_user";

			 //q = "INSERT INTO SHOP_USER VALUES (101, '123 Avenue', 'John Barton', 'password', 'john@barton.com', 'true')";
			  
			 //myResultSet = sqlStatement.executeQuery(q);
			  
			  // Move to next row and & its contents to the html output
//		  while(myResultSet.next())
//		  {
//			  String name = myResultSet.getString(2);
//			  System.out.println("NAME: " + name);
//		  }
//	
		} catch (SQLException ex)
		{
			System.out.println("SQLException:" + ex.getMessage() + "<BR>");
		} 
		
	    setIds();
	    
	    topLevel();
	}
	
	public static ResultSet executeQuery(String query) {
		ResultSet results = null;
		try {
			results = sqlStatement.executeQuery(query);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("SQLException2: " + e.getMessage());
			e.printStackTrace();
		}
		return results;
	}
	
	public static void setIds() {
		
		// Set last used ID of all tables 
		lastIds[SHOP_USER_IDX] = setLastId("SHOP_USER");
		lastIds[ORDERS_IDX] = setLastId("ORDERS");
		lastIds[ORDER1_IDX] = setLastId("ORDER1");
		lastIds[CONTAINS_IDX] = setLastId("CONTAINS");
		lastIds[PRODUCT_IDX] = setLastId("PRODUCT");
		lastIds[LOCATION_IDX] = setLastId("LOCATION");
		lastIds[SHELF_IDX] = setLastId("SHELF");
		lastIds[SUPPLYS_IDX] = setLastId("SUPPLYS");
		lastIds[SUPPLIER_IDX] = setLastId("SUPPLIER");
		lastIds[INSIDE_IDX] = setLastId("INSIDE");
		lastIds[PARENT_IDX] = setLastId("PARENT");
		lastIds[CATEGORY_IDX] = setLastId("CATEGORY");
		lastIds[AVAILABLE_IDX] = setLastId("AVAILABLE");
		lastIds[AVAILABLECAT_IDX] = setLastId("AVAILABLECAT");
		lastIds[DISCOUNT_IDX] = setLastId("DISCOUNT");
		
		return;
	}
	
	private static int setLastId(String table) {
		ResultSet idRes = executeQuery("select id from " + table);
		try {
			if (!idRes.next() ) {
				return 0;
			} else {
				//Get last used ID
				int i = 1;
				int finalId = 0;
				while (idRes.next()) {
					finalId = idRes.getInt(i);
				}
				return finalId;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}
	
	public static void topLevel() {
		System.out.print("Customer or Staff? [C] or [S]");
	    String role = sc.next();
	    
	    if (role.equals("C")) {
	    	customer();
	    } else if (role.equals("S")) {
	    	staff();
	    } else if (role.equals("X")) {
			executeQuery("DELETE FROM SHOP_USER");
			topLevel();
	    }
	}
	
	public static void customer() {
		System.out.println("Select a number:"
				+ "\n1 Search available products"
				+ "\n2 Log in"
				+ "\n3 Create account");
		
		int op = sc.nextInt();
		
		switch (op) {
			case 1:
				search();
				break;
			case 2:
				login(false);
				break;
			case 3:
				createUser(false);
				break;
		}
	}
	
	public static void search() {
		System.out.print("Search by keyword:");
		String keyword = sc.next();
		//TODO insert query
		topLevel();
	}
	
	public static void staff() {
		System.out.println("Select a number: "
				+ "\n1 Login"
				+ "\n2 Create Staff");
		
		int ans = sc.nextInt();
		
		if (ans == 1) {
			login(true);
		} else {
			createUser(true);
		}
		
	}
	
	public static void login(boolean isStaff) {
		System.out.print("Input email: ");
		String email = sc.next();
		
		System.out.print("Input password: ");
		String password = sc.next();

		boolean succ = false;
		
		String q = "SELECT * FROM SHOP_USER WHERE (email = '" + email + "' AND password = '" + password + "' AND is_staff = '" + isStaff + "')";
		
		ResultSet custLogAttempt = executeQuery(q);
		
		try {
			if (!custLogAttempt.next()) {
				System.out.print("Unsuccessful login. Try again? [Y] or [N]");
				String ans = sc.next();
				if (ans.equals("Y")) {
					login(isStaff);
				} else if (ans.equals("N")) {
					topLevel();
				}
			} else {
				loggedInUser = custLogAttempt.getInt(1);
				System.out.println("Logged in as: " + custLogAttempt.getString(3) + ", loggedInCust: " + loggedInUser);
				if (isStaff) {
					loggedInStaff();
				} else {
					loggedInCust();
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void loggedInCust() {
		System.out.println("Select a number:"
				+ "\n1 Update account info"
				+ "\n2 Delete account"
				+ "\n3 Add product to order"
				+ "\n4 Checkout"
				+ "\n5 Logout");
		
		int op = sc.nextInt();
		
		switch (op) {
			case 1:
				updateUser(false);
				break;
			case 2:
				deleteCurrUser();
				break;
			case 3:
				addProductToOrder();
				break;
//			case 4:
//				checkout();
//				break;
			case 5:
				logoutUser();
				break;
		}
	}
	
	public static void loggedInStaff() {
		System.out.println("Select a number:"
				+ "\n1 Update account info"
				+ "\n2 Delete account"
				+ "\n3 Users"
				+ "\n4 Products"
				+ "\n5 Orders"
				+ "\n6 Categories"
				+ "\n7 Discounts"
				+ "\n8 Logout");
		
		int op = sc.nextInt();
		
		switch (op) {
			case 1:
				updateUser(true);
				break;
			case 2:
				deleteCurrUser();
				break;
			case 3:
				modifyUsers();
				break;
			case 4: 
				modifyProducts();
				break;
//			case 5:
//				modifyOrders();
//				break;
//			case 6:
//				modifyCategories();
//				break;
//			case 7:
//				modifyDiscounts();
			case 8:
				logoutUser();
				break;
		}
	}
	
	/////////
	//USERS//
	/////////
	private static void modifyUsers() {
		System.out.println("Add [A], Update [U], or Delete [D] a User?");
		String sel = sc.next();
		
		if (sel.equals("A")) {
			addNewUser();
		} else if (sel.equals("U")) {
			updateUser();
		} else if (sel.equals("D")) {
			deleteUser();
		}
		
		loggedInStaff();
	}
	
	private static void addNewUser() {
		System.out.print("Staff member? [Y] or [N]: ");
		String staffStr = sc.next();
		boolean isStaff;
		if (staffStr.equals("Y")) {
			isStaff = true;
		} else {
			isStaff = false;
		}
		
		System.out.print("Input name: ");
		String name = sc.next();
		
		System.out.print("Input address: ");
		String address = sc.next();
		
		System.out.print("Input email: ");
		String email = sc.next();
		
		System.out.print("Input password: ");
		String password = sc.next();
		
		int id;
		if (lastIds[SHOP_USER_IDX] == 0) {
			id = 100000;
		} else {
			id = lastIds[SHOP_USER_IDX] + 1;
		}
		lastIds[SHOP_USER_IDX] = id;
		
		String q = "INSERT INTO SHOP_USER VALUES (" + id +", '" + address +"', '" + name + "', '" + password + "', '" + email + "', '" + isStaff + "')";
		
		executeQuery(q);
		
		loggedInStaff();
	}
	
	private static void updateUser() {
		System.out.println("Which user do you want to update? (ID)");
		printAllUsers();
		
		int idToUpdate = sc.nextInt();
		//Name
		System.out.print("Update name? [Y] or [N]");
		if (sc.next().equals("Y")) {
			System.out.print("New name: ");
			String q = "UPDATE SHOP_USER SET name = '" + sc.next() + "' WHERE id = " + idToUpdate;
			executeQuery(q);
		}
		
		//Address
		System.out.print("Update address? [Y] or [N]");
		if (sc.next().equals("Y")) {
			System.out.print("New address: ");
			String q = "UPDATE SHOP_USER SET address = '" + sc.next() + "' WHERE id = " + idToUpdate;
			executeQuery(q);
		}
		
		//Email
		System.out.print("Update email? [Y] or [N]");
		if (sc.next().equals("Y")) {
			System.out.print("New email: ");
			String q = "UPDATE SHOP_USER SET email = '" + sc.next() + "' WHERE id = " + idToUpdate;
			executeQuery(q);
		}
		
		//Password
		System.out.print("Update password? [Y] or [N]");
		if (sc.next().equals("Y")) {
			System.out.print("New password: ");
			String q = "UPDATE SHOP_USER SET password = '" + sc.next() + "' WHERE id = " + idToUpdate;
			executeQuery(q);
		}
		
		loggedInStaff();
	}
	
	private static void printAllUsers() {
		String q = "SELECT id, name, address, email, is_staff FROM SHOP_USER ORDER by id";
		ResultSet allUsers = executeQuery(q);
		
		try {
			while(allUsers.next())
			{
				int id = allUsers.getInt(1);
				String name = allUsers.getString(2);
				String address = allUsers.getString(3);
				String email = allUsers.getString(4);
				String isStaff = allUsers.getString(5);
				if (isStaff.equals("true")) {
					isStaff = "Staff";
				} else {
					isStaff = "Customer";
				}
				System.out.println(id + ": " + name + " - " + email + " - " + address + " - " + isStaff);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return;
	}
	
	private static void deleteUser() {
		System.out.println("Which user do you want to delete? (ID)");
		printAllUsers();
		
		int idToDelete = sc.nextInt();
		String q = "DELETE FROM SHOP_USER WHERE id = " + idToDelete;
		executeQuery(q);
		loggedInStaff();
	}
	
	public static void updateUser(boolean isStaff) {
		//Name
		System.out.print("Update name? [Y] or [N]");
		if (sc.next().equals("Y")) {
			System.out.print("New name: ");
			String q = "UPDATE SHOP_USER SET name = '" + sc.next() + "' WHERE id = " + loggedInUser;
			executeQuery(q);
		}
		
		//Address
		System.out.print("Update address? [Y] or [N]");
		if (sc.next().equals("Y")) {
			System.out.print("New address: ");
			String q = "UPDATE SHOP_USER SET address = '" + sc.next() + "' WHERE id = " + loggedInUser;
			executeQuery(q);
		}
		
		//Email
		System.out.print("Update email? [Y] or [N]");
		if (sc.next().equals("Y")) {
			System.out.print("New email: ");
			String q = "UPDATE SHOP_USER SET email = '" + sc.next() + "' WHERE id = " + loggedInUser;
			executeQuery(q);
		}
		
		//Password
		System.out.print("Update password? [Y] or [N]");
		if (sc.next().equals("Y")) {
			System.out.print("New password: ");
			String q = "UPDATE SHOP_USER SET password = '" + sc.next() + "' WHERE id = " + loggedInUser;
			executeQuery(q);
		}
		
		if (isStaff) {
			loggedInStaff();
		} else {
			loggedInCust();
		}
	}
	
	public static void deleteCurrUser() {
		System.out.print("Are you sure you want to delete your account? [Y] or [N]");
		String ans = sc.next();
		
		if (ans.equals("Y")) {
			System.out.println("LIC: " + loggedInUser);
			String q = "DELETE FROM SHOP_USER WHERE id = " + loggedInUser;
			executeQuery(q);
			loggedInUser = 0;
			topLevel();
		} else {
			loggedInCust();
		}
	}
	
	public static void addProductToOrder() {
		System.out.println("Which product would you like to add to your order?"
				+ "\n1 Product1"
				+ "\n2 Product2"
				+ "\n3 Product3");
		int prodSel = sc.nextInt();
		
		//TODO NEEDS MORE
	}
	
	public static void logoutUser() {
		loggedInUser = 0;
		topLevel();
	}
	
	public static void createUser(boolean isStaff) {
		System.out.print("Input name: ");
		String name = sc.next();
		
		System.out.print("Input address: ");
		String address = sc.next();
		
		System.out.print("Input email: ");
		String email = sc.next();
		
		System.out.print("Input password: ");
		String password = sc.next();
		
		int id;
		if (lastIds[SHOP_USER_IDX] == 0) {
			id = 100000;
		} else {
			id = lastIds[SHOP_USER_IDX] + 1;
		}
		lastIds[SHOP_USER_IDX] = id;
		
		loggedInUser = id;
		
		String q = "INSERT INTO SHOP_USER VALUES (" + id +", '" + address +"', '" + name + "', '" + password + "', '" + email + "', '" + isStaff + "')";
		
		executeQuery(q);
		if (isStaff) {
			loggedInStaff();
		} else {
			loggedInCust();
		}
	}
	
	////////////
	//PRODUCTS//
	////////////
	private static void modifyProducts() {
		System.out.println("Add [A], Update [U], or Delete [D] a User?");
		String sel = sc.next();
		
		if (sel.equals("A")) {
			addNewProduct();
		} else if (sel.equals("U")) {
			updateProduct();
		} else if (sel.equals("D")) {
			deleteProduct();
		}
		
		loggedInStaff();
	}
	
	private static void addNewProduct() {
		
	}
	
	private static void updateProduct() {
		
	}
	
	private static void deleteProduct() {
		
	}
	
}

