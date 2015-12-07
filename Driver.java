import java.io.* ;
import java.sql.* ;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;
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
    
    static Connection sqlcon2  = null;
    static Statement sqlStatement2  = null;
    static ResultSet myResultSet2  = null;
    
    //Indexes
    final static int SHOP_USER_IDX    = 0;
    final static int ORDERS_IDX       = 1;
    final static int ORDER1_IDX       = 2;
    final static int CONTAINS_IDX     = 3;
    final static int PRODUCT_IDX      = 4;
    final static int LOCATION_IDX     = 5;
    final static int SHELF_IDX        = 6;
    final static int SUPPLYS_IDX      = 7;
    final static int SUPPLIER_IDX     = 8;
    final static int INSIDE_IDX       = 9;
    final static int PARENT_IDX       = 10;
    final static int CATEGORY_IDX     = 11;
    final static int AVAILABLE_IDX    = 12;
    final static int AVAILABLECAT_IDX = 13;
    final static int DISCOUNT_IDX     = 14;
    
    //Initial IDs
    final static int SHOP_USER_INIT    = 100000;
    final static int ORDERS_INIT       = 150000;
    final static int ORDER1_INIT       = 200000;
    final static int CONTAINS_INIT     = 250000;
    final static int PRODUCT_INIT      = 300000;
    final static int LOCATION_INIT     = 350000;
    final static int SHELF_INIT        = 400000;
    final static int SUPPLYS_INIT      = 450000;
    final static int SUPPLIER_INIT     = 500000;
    final static int INSIDE_INIT       = 530000;
    final static int PARENT_INIT       = 560000;
    final static int CATEGORY_INIT     = 600000;
    final static int AVAILABLE_INIT    = 630000;
    final static int AVAILABLECAT_INIT = 660000;
    final static int DISCOUNT_INIT     = 700000;
    
	static int[] lastIds = new int[15];
	
	public static void main(String[] args)  throws SQLException
	{
		  //Scanner only uses newlines as delimiters
		  sc.useDelimiter(Pattern.compile("[\\r\\n;]+"));
	      try {
			  DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
		
			  // Connect to the database
			  sqlcon= DriverManager.getConnection ("jdbc:oracle:thin:hr/hr@oracle.cise.ufl.edu:1521:orcl", "kmarin", "Tuxy3530");
			  sqlcon2= DriverManager.getConnection ("jdbc:oracle:thin:hr/hr@oracle.cise.ufl.edu:1521:orcl", "kmarin", "Tuxy3530");
		
			  // Create a Statement
			  sqlStatement = sqlcon.createStatement();
			  sqlStatement2 = sqlcon2.createStatement();
			  
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
			System.out.println("QUERY: " + query);
			System.out.println("SQLException2: " + e.getMessage());
			e.printStackTrace();
		}
		return results;
	}
	
	public static ResultSet executeQuery2(String query) {
		ResultSet results = null;
		try {
			results = sqlStatement2.executeQuery(query);
		} catch (SQLException e) {
			System.out.println("QUERY: " + query);
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
		ResultSet idRes = executeQuery("select id from " + table + " ORDER BY id");
		try {
			int id = 0;
			while(idRes.next())
			{
				id = idRes.getInt(1);
			}
			//System.out.println(table + ": " + id);
			return id;
		} catch (SQLException e) {
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
		
		ResultSet allProds = executeQuery("SELECT id, name, price FROM PRODUCT ORDER by price");
		try {
			while(allProds.next())
			{
				int id = allProds.getInt(1);
				String name = allProds.getString(2);
				int price = allProds.getInt(3);
				if(name.contains(keyword)) {
					System.out.println(id + ": " + name + " $" + price);
				}
			}
			System.out.print("Sort by price, high-to-low? [Y] or [N]");
			
			if (sc.next().equals("Y")) {
				allProds = executeQuery("SELECT id, name, price FROM PRODUCT ORDER by price desc");	
				
				while(allProds.next())
				{
					int id = allProds.getInt(1);
					String name = allProds.getString(2);
					int price = allProds.getInt(3);
					if(name.contains(keyword)) {
						System.out.println(id + ": " + name + " $" + price);
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
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
				System.out.println("Logged in as: " + custLogAttempt.getString(3));
				if (isStaff) {
					loggedInStaff();
				} else {
					loggedInCust();
				}
			}
		} catch (SQLException e) {
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
		loggedInCust();
	}
	
	private static void addProductToOrder() {
		System.out.println("Which order?");
		printUserOrders();
		int orderId = sc.nextInt();
		
		
		System.out.print("Enter product ID: ");
		int prodId = sc.nextInt();
		System.out.println("Quantity of product: ");
		int quantity = sc.nextInt();
		
		String q = "SELECT id FROM PRODUCT WHERE id = " + prodId;
		ResultSet thisProd = executeQuery(q);
		int thisProdId = 0;
		int id;
		if (lastIds[CONTAINS_IDX] == 0) {
			id = CONTAINS_INIT;
		} else {
			id = lastIds[CONTAINS_IDX] + 1;
		}
		lastIds[CONTAINS_IDX] = id;
		
		try {
			thisProd.next();
			thisProdId = thisProd.getInt(1);
			q = "INSERT INTO CONTAINS VALUES (" + id +", " + quantity +", " + thisProdId + ", " + orderId + ")";
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return;
	}
	
	private static void printUserOrders() {
		String q = "SELECT order1_id FROM ORDERS WHERE user_id = " + loggedInUser;
		ResultSet usersOrderIds = executeQuery(q);
		int orderId = 0;
		try {
			while (usersOrderIds.next()) {
				orderId = usersOrderIds.getInt(1);
				String q2 = "SELECT id, date_placed FROM ORDER1 WHERE id = " + orderId;
				ResultSet fullOrders = executeQuery2(q2);
				fullOrders.next();
				System.out.println("ID: " + fullOrders.getInt(1) + " Date: " + fullOrders.getString(2));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static void loggedInStaff() {
		System.out.println("Select a number:"
				+ "\n1 Update account info"
				+ "\n2 Delete account"
				+ "\n3 Users"
				+ "\n4 Products"
				+ "\n5 Orders"
				+ "\n6 Categories"
				+ "\n7 Discounts"
				+ "\n8 More Choices"
				+ "\n9 Logout");
		
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
			case 5:
				modifyOrders(true);
				break;
			case 6:
				modifyCategories();
				break;
			case 7:
				modifyDiscounts();
				break;
			case 8:
				staffMore();
				break;
			case 9:
				logoutUser();
				break;
		}
	}
	
	private static void staffMore() {
		System.out.println("Select a number:"
				+ "\n1 Back"
				+ "\n2 Shelves"
				+ "\n3 Suppliers");
		
		int op = sc.nextInt();
		
		switch (op) {
			case 1:
				loggedInStaff();
				break;
			case 2:
				modifyShelves();
			case 3:
				modifySuppliers();
				break;
		}
		
		loggedInStaff();
	}
	
	/////////
	//USERS//
	/////////
	private static void modifyUsers() {
		System.out.println("List[L], Add [A], Update [U], or Delete [D] a User?");
		String sel = sc.next();
		
		if (sel.equals("L")) {
			printAllUsers();
		} else if (sel.equals("A")) {
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
			id = SHOP_USER_INIT;
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
	
//	public static void addProductToOrder() {
//		System.out.println("Which product would you like to add to your order?"
//				+ "\n1 Product1"
//				+ "\n2 Product2"
//				+ "\n3 Product3");
//		int prodSel = sc.nextInt();
//		
//		//TODO NEEDS MORE
//	}
	
	private static void modifyOrders(boolean isStaff) {
		System.out.println("Add [A], Update [U], (staff only)Get Report[R] or Delete [D] an order?");
		String sel = sc.next();
		
		if (sel.equals("A")) {
			addNewOrder();
		} else if (sel.equals("U")) {
			updateOrder(isStaff);
		}
		  else if(sel.equals("D")){ 
				deleteOrder(isStaff); 
			}
		  else if(sel.equals("R")){
			  	if(isStaff == false)
				System.out.println("Only staff may get reports");
			  	else
			  		getReport(isStaff);
		  }
		if(isStaff == true){
			loggedInStaff();
		}
		else
			loggedInCust();
			
	}
	
	private static void getReport(boolean isStaff) {
		System.out.println("Which order do you want a report from? (ID)");
		printAllOrders(isStaff);
		int idToUpdate = sc.nextInt();
		String q = "SELECT product_id from contains where order1_id =" + idToUpdate;
		ResultSet prodIds = executeQuery(q);
		ArrayList<String> shelfs = new ArrayList<String>();
		ArrayList<String> prods = new ArrayList<String>();
		try {
			int i = 0;
		while(prodIds.next()){
			String r = "SELECT name from shelf where (select shelf_id from location where product_id =" + prodIds.getInt(1) + " ) = id";
			ResultSet shelf = executeQuery2(r);
			shelf.next();
			shelfs.add(shelf.getString(1));
			i++;
		}
		prodIds = executeQuery(q);
		i=0;
		while(prodIds.next()){
			String r = "SELECT name from product where id = " + prodIds.getInt(1);
			ResultSet prod = executeQuery2(r);
			prod.next();
			prods.add(prod.getString(1));
			i++;	
		}
		}catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		
		}
		
		for(int i = 0; i < prods.size(); i++){
			System.out.println(shelfs.get(i) + "         " + prods.get(i));
		}
		return;
		
	
}

	private static void addNewOrder() {
		
		boolean paid = false;
		DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		Date date = new Date();
		String currDate = dateFormat.format(date);
		
		int id;
		if (lastIds[ORDER1_IDX] == 0) {
			id = ORDER1_INIT;
		} else {
			id = lastIds[ORDER1_IDX] + 1;
		}
		lastIds[ORDER1_IDX] = id;
		
		String q = "INSERT INTO ORDER1 VALUES (" + id +", " + 0 +", '" + currDate + "', '" + paid + "')";
		
		executeQuery(q);
		
		int ordersId;
		if (lastIds[ORDERS_IDX] == 0) {
			ordersId = ORDERS_INIT;
		} else {
			ordersId = lastIds[ORDERS_IDX] + 1;
		}
		lastIds[ORDERS_IDX] = ordersId;
		
		
		String r = "INSERT INTO ORDERS VALUES (" + ordersId + ", " + loggedInUser + ", " + id + ")";
		executeQuery(r);
		
		return;
	}
	
	private static void updateOrder(boolean isStaff) {
		System.out.println("Which order do you want to update? (ID)");
		printAllOrders(isStaff);
		
		int idToUpdate = sc.nextInt();
		
		//TODO Need to link this to products and figure out how to add and delete
		// Need to be able to add AND delete products
		return;
	}
	
	private static void deleteOrder(boolean isStaff) {
		System.out.println("Which order do you want to delete? (ID)");
		printAllOrders(isStaff);
		
		int idToDelete = sc.nextInt();
		String q = "DELETE FROM ORDER1 WHERE id = " + idToDelete;
		executeQuery(q);
		return;
	}
	
	
		//TODO figure out a query to just return the orders of the current user as well as return products in the order
	private static void printAllOrders(boolean isStaff) {
		if(isStaff == true){
		String q = "SELECT id, total_price, date_placed, paid FROM ORDER1 ORDER by id";
		ResultSet allOrders = executeQuery(q);
		
		try {
			while(allOrders.next())
			{
				
				int id = allOrders.getInt(1);
				int price = allOrders.getInt(2);
				String date = allOrders.getString(3);
				String paid = allOrders.getString(4);
				System.out.println(id + ": " + price + " - " + date + " - " + paid);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		}
		else{
			String r = "SELECT order1_id FROM ORDERS WHERE user_id = " + loggedInUser + " ORDER BY id";
			ResultSet allOrdersCheck = executeQuery(r);
			try {
				while(allOrdersCheck.next())
				{
					
					int id = allOrdersCheck.getInt(1);
					r = "SELECT id, total_price, date_placed, paid FROM ORDER1 WHERE id = " + id + " ORDER by id";
					ResultSet singleOrders = executeQuery(r);
					while(singleOrders.next()){
						int singId = singleOrders.getInt(1);
						int price = singleOrders.getInt(2);
						String date = singleOrders.getString(3);
						String paid = singleOrders.getString(4);
						System.out.println(singId + ": " + price + " - " + date + " - " + paid);
					}
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return;

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
			id = SHOP_USER_INIT;
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
		System.out.println("List[L], Add [A], Update [U], or Delete [D] a Product?");
		String sel = sc.next();
		
		if (sel.equals("L")) {
			printAllProducts();
		} else if (sel.equals("A")) {
			addNewProduct();
		} else if (sel.equals("U")) {
			updateProduct();
		} else if (sel.equals("D")) {
			deleteProduct();
		}
		
		loggedInStaff();
	}
	
	private static void addNewProduct() {
		setIds();
		
		System.out.print("Product name: ");
		String name = sc.next();
		
		System.out.print("Product price: ");
		String price = sc.next();
		
		System.out.print("Product stock quantity: ");
		int stockQuantity = sc.nextInt();
		
		System.out.print("Product description: ");
		String description = sc.next();
		
		System.out.println("Product location (ID): ");
		printAllShelfs();
		int shelfId = sc.nextInt();
		
		System.out.println("Product supplier (ID): ");
		printAllSuppliers();
		int supplierId = Integer.parseInt(sc.next());
		
		System.out.println("Product Category (ID): ");
		printAllCategories();
		int categoryId = Integer.parseInt(sc.next());
		
		int id;
		if (lastIds[PRODUCT_IDX] == 0) {
			id = PRODUCT_INIT;
		} else {
			id = lastIds[PRODUCT_IDX] + 1;
		}
		lastIds[PRODUCT_IDX] = id;
		
		int locationId;
		if (lastIds[LOCATION_IDX] == 0) {
			locationId = LOCATION_INIT;
		} else {
			locationId = lastIds[LOCATION_IDX] + 1;
		}
		lastIds[LOCATION_IDX] = locationId;
		
		String q = "INSERT INTO LOCATION VALUES (" + locationId + ", " + id + ", " + shelfId + ")";
		executeQuery(q);
		
		int supplysId;
		if (lastIds[SUPPLYS_IDX] == 0) {
			supplysId = SUPPLYS_INIT;
		} else {
			supplysId = lastIds[SUPPLYS_IDX] + 1;
		}
		lastIds[SUPPLYS_IDX] = supplysId;
		
		q = "INSERT INTO SUPPLYS VALUES (" + supplysId + ", " + id + ", " + supplierId + ")";
		executeQuery(q);
		
		int inId;
		if (lastIds[INSIDE_IDX] == 0) {
			inId = INSIDE_INIT;
		} else {
			inId = lastIds[INSIDE_IDX] + 1;
		}
		lastIds[INSIDE_IDX] = inId;

		q = "INSERT INTO INSIDE VALUES (" + inId + ", " + id + ", " + categoryId + ")";
		executeQuery(q);
		
		q = "INSERT INTO PRODUCT (id, name, price, stock_quantity, description, active) VALUES (" + id + ", '" + name + "', " + price + ", " + stockQuantity + ", '" + description + "', 'true')";
		executeQuery(q);
		loggedInStaff();
	}
	
	private static void updateProduct() {
		System.out.println("Which product do you want to update? (ID)");
		printAllProducts();
		
		int idToUpdate = sc.nextInt();
		
		//Name
		System.out.print("Update product name? [Y] or [N]");
		if (sc.next().equals("Y")) {
			System.out.print("New name: ");
			String q = "UPDATE PRODUCT SET name = '" + sc.next() + "' WHERE id = " + idToUpdate;
			executeQuery(q);
		}
		
		//Price
		System.out.print("Update product price? [Y] or [N]");
		if (sc.next().equals("Y")) {
			System.out.print("New price: ");
			String q = "UPDATE PRODUCT SET price= '" + sc.nextInt() + "' WHERE id = " + idToUpdate;
			executeQuery(q);
		}
		
		//More stock
		System.out.print("More product in stock? [Y] or [N]");
		if (sc.next().equals("Y")) {
			System.out.print("How much? ");
			String q = "SELECT stock_quantity FROM PRODUCT WHERE id = " + idToUpdate;
			ResultSet existingProduct = executeQuery(q);
			int existingAmt = 0;
			try {
				while (existingProduct.next()) {
					existingAmt = existingProduct.getInt(1);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			existingAmt += sc.nextInt();
			q = "UPDATE PRODUCT SET stock_quantity= '" + existingAmt + "' WHERE id = " + idToUpdate;
			executeQuery(q);
		}

		//Less stock, more shelf
		System.out.print("Move product to shelf? [Y] or [N]");
		if (sc.next().equals("Y")) {
			System.out.print("How much? ");
			String q = "SELECT stock_quantity FROM PRODUCT WHERE id = " + idToUpdate;
			ResultSet existingProduct = executeQuery(q);
			int existingAmt = 0;
			try {
				while (existingProduct.next()) {
					existingAmt = existingProduct.getInt(1);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			int amtMoved = sc.nextInt();
			existingAmt -= amtMoved;
			q = "UPDATE PRODUCT SET stock_quantity = " + existingAmt + " WHERE id = " + idToUpdate;
			executeQuery(q);
			
			q = "SELECT shelf_id FROM LOCATION WHERE product_id = " + idToUpdate;
			ResultSet thisShelf = executeQuery(q);
			int currAmtOnShelf = 0;
			int thisShelfId = 0;
			try {
				thisShelf.next();
				thisShelfId = thisShelf.getInt(1);
				q = "SELECT available_quantity FROM SHELF WHERE id = " + thisShelfId;
				ResultSet thisAmount = executeQuery(q);
				thisAmount.next();
				currAmtOnShelf = thisAmount.getInt(1);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			currAmtOnShelf += amtMoved;
			q = "UPDATE SHELF SET available_quantity = " + currAmtOnShelf + " WHERE id = " + thisShelfId;
			executeQuery(q);
		}
		
		//Description
		System.out.print("Update product description? [Y] or [N]");
		if (sc.next().equals("Y")) {
			System.out.print("New price: ");
			String q = "UPDATE PRODUCT SET description= '" + sc.next() + "' WHERE id = " + idToUpdate;
			executeQuery(q);
		}
		
		//Deactivate
		System.out.print("Deactivate? [Y] or [N]");
		if (sc.next().equals("Y")) {
			String q = "UPDATE PRODUCT SET active= 'false' WHERE id = " + idToUpdate;
			executeQuery(q);
		}
		
		//Product location
		System.out.print("Update product location? [Y] or [N]");
		if (sc.next().equals("Y")) {
			System.out.print("New product location (ID): ");
			printAllShelfs();
			int newShelfId = sc.nextInt();
			String q = "UPDATE LOCATION SET shelf_id= " + newShelfId + " WHERE product_id = " + idToUpdate;
			executeQuery(q);
		}
		
		//Product supplier
		System.out.print("Update product supplier? [Y] or [N]");
		if (sc.next().equals("Y")) {
			System.out.print("New product supplier (ID): ");
			printAllSuppliers();
			int newSupplierId = sc.nextInt();
			//TODO
			String q = "UPDATE SUPPLYS SET supplier_id= " + newSupplierId + " WHERE product_id = " + idToUpdate;
			executeQuery(q);
		}
		
		//Product category
		System.out.print("Update product category? [Y] or [N]");
		if (sc.next().equals("Y")) {
			System.out.print("New product category (ID): ");
			printAllCategories();
			int newCategoryId = sc.nextInt();
			//TODO
			String q = "UPDATE INSIDE SET category_id= " + newCategoryId + " WHERE product_id = " + idToUpdate;
			executeQuery(q);
		}
		loggedInStaff();
	}
	
	private static void deleteProduct() {
		System.out.println("Which product do you want to delete? (ID)");
		printAllProducts();
		
		int idToDelete = sc.nextInt();
		
		//Name
		String q = "DELETE FROM PRODUCT WHERE id = " + idToDelete;
		executeQuery(q);
		
		q = "DELETE FROM SUPPLYS WHERE product_id = " + idToDelete;
		executeQuery(q);
		
		q = "DELETE FROM INSIDE WHERE product_id = " + idToDelete;
		executeQuery(q);
		
		loggedInStaff();
	}
	
	private static void printAllProducts() {
		String q = "SELECT id, name, price, description, stock_quantity, active FROM PRODUCT ORDER by id";
		ResultSet allProducts = executeQuery(q);
		
		try {
			while(allProducts.next())
			{
				int id = allProducts.getInt(1);
				String name = allProducts.getString(2);
				String price = allProducts.getString(3);
				String description = allProducts.getString(4);
				int stock_quantity = allProducts.getInt(5);
				String active = allProducts.getString(6);
				if (active.equals("true")) {
					active = "Active";
				} else {
					active = "Not Active";
				}
				System.out.println(id + ": " + name + " - " + description + " - " + price + " - " + active + " [" + stock_quantity + "]");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return;
	}
	
	//////////////
	//CATEGORIES//
	//////////////
	private static void modifyCategories() {
		System.out.println("List [L], Add [A], Update [U], or Delete [D] a Category?");
		String sel = sc.next();
		
		if (sel.equals("L")) {
			printAllCategories();
		} else if (sel.equals("A")) {
			addNewCategory();
		} else if (sel.equals("U")) {
			updateCategory();
		} else if (sel.equals("D")) {
			deleteCategory();
		}
		
		loggedInStaff();
	}
	
	private static void addNewCategory() {
		System.out.print("Category name: ");
		String name = sc.next();
		
		System.out.print("Category description: ");
		String description = sc.next();
		
		System.out.println("Parent Category? (ID, 'N' for none)");
		printAllCategories();
		String parCar = sc.next();
		
		int id;
		if (lastIds[CATEGORY_IDX] == 0) {
			id = CATEGORY_INIT;
		} else {
			id = lastIds[CATEGORY_IDX] + 1;
		}
		lastIds[CATEGORY_IDX] = id;
		
		if (!parCar.equals("N")) {
			int parCarId = Integer.parseInt(parCar);
			
			int parentId;
			if (lastIds[PARENT_IDX] == 0) {
				parentId = PARENT_INIT;
			} else {
				parentId = lastIds[PARENT_IDX] + 1;
			}
			lastIds[PARENT_IDX] = parentId;
			
			String q = "INSERT INTO PARENT VALUES (" + parentId + ", " + id + ", " + parCarId + ")";
			executeQuery(q);
		}		
		
		String q = "INSERT INTO CATEGORY VALUES (" + id +", '" + name +"', '" + description + "')";
		executeQuery(q);
		
		loggedInStaff();
	}
	
	private static void updateCategory() {
		System.out.println("Which category do you want to update? (ID)");
		printAllCategories();
		
		int idToUpdate = sc.nextInt();
		//Name
		System.out.print("Update name? [Y] or [N]");
		if (sc.next().equals("Y")) {
			System.out.print("New name: ");
			String q = "UPDATE CATEGORY SET name = '" + sc.next() + "' WHERE id = " + idToUpdate;
			executeQuery(q);
		}
		
		//Description
		System.out.print("Update description? [Y] or [N]");
		if (sc.next().equals("Y")) {
			System.out.print("New description: ");
			String q = "UPDATE CATEGORY SET description = '" + sc.next() + "' WHERE id = " + idToUpdate;
			executeQuery(q);
		}
		
		//Parent category
		System.out.println("Update Parent Category? [Y] or [N]");
		if (sc.next().equals("Y")) {
			System.out.println("Which category? (ID, 'N' for none)");
			printAllCategories();
			String parCar = sc.next();
			
			if (!parCar.equals("N")) {
				int parCarId = Integer.parseInt(parCar);
				
				int parentId;
				if (lastIds[PARENT_IDX] == 0) {
					parentId = PARENT_INIT;
				} else {
					parentId = lastIds[PARENT_IDX] + 1;
				}
				lastIds[PARENT_IDX] = parentId;
				
				String q = "INSERT INTO PARENT VALUES (" + parentId + ", " + idToUpdate + ", " + parCarId + ")";
				executeQuery(q);
			} else {
				String q = "DELETE FROM PARENT WHERE child_category_id = " + idToUpdate;
				executeQuery(q);
			}
		}
		
		loggedInStaff();		
	}
	
	private static void deleteCategory() {
		System.out.println("Which category do you want to update? (ID)");
		printAllCategories();
		
		int idToDelete = sc.nextInt();
		String q = "DELETE FROM CATEGORY WHERE id = " + idToDelete;
		executeQuery(q);
		loggedInStaff();
	}
	
	private static void printAllCategories() {
		String q = "SELECT id, name, description FROM CATEGORY ORDER by id";
		ResultSet allCategories = executeQuery(q);
		
		try {
			while(allCategories.next())
			{
				int id = allCategories.getInt(1);
				String name = allCategories.getString(2);
				String description = allCategories.getString(3);
				System.out.println(id + ": " + name + " - " + description);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return;
	}
	
	///////////
	//SHELVES//
	///////////
	private static void modifyShelves() {
		System.out.println("List[L], Add [A], Update [U], or Delete [D] a Shelf?");
		String sel = sc.next();
		
		if (sel.equals("L")) {
			printAllShelfs();
		} else if (sel.equals("A")) {
			addNewShelf();
		} else if (sel.equals("U")) {
			updateShelf();
		} else if (sel.equals("D")) {
			deleteShelf();
		}
		
		loggedInStaff();
	}
	
	private static void addNewShelf() {
		System.out.print("Shelf name: ");
		String name = sc.next();
		
		System.out.print("Available Quantity on Shelf: ");
		int quantity = sc.nextInt();
		
		int id;
		if (lastIds[SHELF_IDX] == 0) {
			id = SHELF_INIT;
		} else {
			id = lastIds[SHELF_IDX] + 1;
		}
		lastIds[SHELF_IDX] = id;
		
		String q = "INSERT INTO SHELF (id, name, available_quantity) VALUES (" + id +", '" + name +"', " + quantity + ")";
		executeQuery(q);
		
		loggedInStaff();
	}
	
	private static void updateShelf() {
		System.out.println("Which shelf do you want to update? (ID)");
		printAllShelfs();
		
		int idToUpdate = sc.nextInt();
		//Name
		System.out.print("Update name? [Y] or [N]");
		if (sc.next().equals("Y")) {
			System.out.print("New name: ");
			String q = "UPDATE SHELF SET name = '" + sc.next() + "' WHERE id = " + idToUpdate;
			executeQuery(q);
		}
		
		//Quantity
		System.out.print("Update available quantity? [Y] or [N]");
		if (sc.next().equals("Y")) {
			System.out.print("New available quanity: ");
			String q = "UPDATE SHELF SET available_quantity = '" + sc.next() + "' WHERE id = " + idToUpdate;
			executeQuery(q);
		}
		
		loggedInStaff();		
	}
	
	private static void deleteShelf() {
		System.out.println("Which shelf do you want to update? (ID)");
		printAllShelfs();
		
		int idToDelete = sc.nextInt();
		String q = "DELETE FROM SHELF WHERE id = " + idToDelete;
		executeQuery(q);
		loggedInStaff();
	}
	
	private static void printAllShelfs() {
		String q = "SELECT id, name, available_quantity FROM SHELF ORDER by id";
		ResultSet allShelfs = executeQuery(q);
		
		try {
			while(allShelfs.next())
			{
				int id = allShelfs.getInt(1);
				String name = allShelfs.getString(2);
				int quantity = allShelfs.getInt(3);
				System.out.println(id + ": " + name + " - " + quantity);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return;
	}
	
	/////////////
	//DISCOUNTS//
	/////////////
	private static void modifyDiscounts() {
		System.out.println("List [L], Add [A], Update [U], or Delete [D] a Discount?");
		String sel = sc.next();
		
		if (sel.equals("L")) {
			printAllDiscounts();
		} else if (sel.equals("A")) {
			addNewDiscount();
		} else if (sel.equals("U")) {
			updateDiscount();
		} else if (sel.equals("D")) {
			deleteDiscount();
		}
		
		loggedInStaff();
	}
		
	private static void addNewDiscount() {
		System.out.print("Discount name: ");
		String name = sc.next();
		
		int id;
		if (lastIds[DISCOUNT_IDX] == 0) {
			id = DISCOUNT_INIT;
		} else {
			id = lastIds[DISCOUNT_IDX] + 1;
		}
		lastIds[DISCOUNT_IDX] = id;
		
		String q;
		
		System.out.print("Discount for Product or Category? [P] or [C]");
		String type = sc.next();
		if (type.equals("P")) {
			int availId;
			if (lastIds[AVAILABLE_IDX] == 0) {
				availId = AVAILABLE_INIT;
			} else {
				availId = lastIds[AVAILABLE_IDX] + 1;
			}
			lastIds[AVAILABLE_IDX] = availId;
			
			System.out.println("Which Product? (ID)");
			printAllProducts();
			int productId = sc.nextInt();
			
			q = "INSERT INTO AVAILABLE VALUES (" + availId + ", " + productId + ", " + id + ")";
			executeQuery(q);
		} else {
			int availCatId;
			if (lastIds[AVAILABLECAT_IDX] == 0) {
				availCatId = AVAILABLECAT_INIT;
			} else {
				availCatId = lastIds[AVAILABLECAT_IDX] + 1;
			}
			lastIds[AVAILABLECAT_IDX] = availCatId;
			
			System.out.println("Which Category? (ID)");
			printAllCategories();
			int catId = sc.nextInt();
			
			q = "INSERT INTO AVAILABLE VALUES (" + availCatId + ", " + id + ", " + catId + ")";
			executeQuery(q);
		}
		
		System.out.print("Discount amount: ");
		int val = sc.nextInt();
		
		q = "INSERT INTO DISCOUNT VALUES (" + id +", '" + name + "', " + val + ")";
		executeQuery(q);
		
		loggedInStaff();
	}
	
	private static void updateDiscount() {
		System.out.println("Which discount do you want to update? (ID)");
		printAllDiscounts();
		
		int idToUpdate = sc.nextInt();
		
		//Name
		System.out.print("Update name? [Y] or [N]");
		if (sc.next().equals("Y")) {
			System.out.print("New name: ");
			String q = "UPDATE DISCOUNT SET name = '" + sc.next() + "' WHERE id = " + idToUpdate;
			executeQuery(q);
		}
		
		//Value
		System.out.print("Update value? [Y] or [N]");
		if (sc.next().equals("Y")) {
			System.out.print("New value: ");
			String q = "UPDATE DISCOUNT SET value = " + sc.next() + " WHERE id = " + idToUpdate;
			executeQuery(q);
		}
				
		loggedInStaff();		
	}
	
	private static void deleteDiscount() {
		System.out.println("Which discount do you want to update? (ID)");
		printAllDiscounts();
		
		int idToDelete = sc.nextInt();
		String q = "DELETE FROM DISCOUNT WHERE id = " + idToDelete;
		executeQuery(q);
		loggedInStaff();
	}
	
	private static void printAllDiscounts() {
		String q = "SELECT id, name, value FROM DISCOUNT ORDER by id";
		ResultSet allDiscounts = executeQuery(q);
		
		try {
			while(allDiscounts.next())
			{
				int id = allDiscounts.getInt(1);
				String name = allDiscounts.getString(2);
				int value = allDiscounts.getInt(3);
				System.out.println(id + ": " + name + " - " + value);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return;
	}
	
	/////////////
	//SUPPLIERS//
	/////////////
	private static void modifySuppliers() {
		System.out.println("List [L], Add [A], Update [U], or Delete [D] a Supplier?");
		String sel = sc.next();
		
		if (sel.equals("L")) {
			printAllSuppliers();
		} else if (sel.equals("A")) {
			addNewSupplier();
		} else if (sel.equals("U")) {
			updateSupplier();
		} else if (sel.equals("D")) {
			deleteSupplier();
		}
		
		loggedInStaff();
	}
	
	private static void addNewSupplier() {
		System.out.print("Supplier name: ");
		String name = sc.next();
				
		int id;
		if (lastIds[SUPPLIER_IDX] == 0) {
			id = SUPPLIER_INIT;
		} else {
			id = lastIds[SUPPLIER_IDX] + 1;
		}
		lastIds[SUPPLIER_IDX] = id;
		
		String q = "INSERT INTO SUPPLIER VALUES (" + id +", '" + name + "')";
		executeQuery(q);
		
		loggedInStaff();
	}
	
	private static void updateSupplier() {
		System.out.println("Which supplier do you want to update? (ID)");
		printAllSuppliers();
		
		int idToUpdate = sc.nextInt();
		//Name
		System.out.print("Update name? [Y] or [N]");
		if (sc.next().equals("Y")) {
			System.out.print("New name: ");
			String q = "UPDATE SUPPLIER SET name = '" + sc.next() + "' WHERE id = " + idToUpdate;
			executeQuery(q);
		}
				
		loggedInStaff();		
	}
	
	private static void deleteSupplier() {
		System.out.println("Which supplier do you want to update? (ID)");
		printAllSuppliers();
		
		int idToDelete = sc.nextInt();
		String q = "DELETE FROM SUPPLIER WHERE id = " + idToDelete;
		executeQuery(q);
		loggedInStaff();
	}
	
	private static void printAllSuppliers() {
		String q = "SELECT id, name FROM SUPPLIER ORDER by id";
		ResultSet allSuppliers = executeQuery(q);
		
		try {
			while(allSuppliers.next())
			{
				int id = allSuppliers.getInt(1);
				String name = allSuppliers.getString(2);
				System.out.println(id + ": " + name);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return;
	}
	
	private static void printAllLocations() {
		String q = "SELECT id, product_id, shelf_id FROM LOCATION ORDER by id";
		ResultSet allSuppliers = executeQuery(q);
		
		try {
			while(allSuppliers.next())
			{
				int id = allSuppliers.getInt(1);
				int productId = allSuppliers.getInt(2);
				int shelfId = allSuppliers.getInt(3);
				System.out.println(id + "pro: " + productId + " shelf: " + shelfId);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return;
	}	
}