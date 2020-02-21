import java.util.*;
import java.text.*;
import java.io.*;
public class UserInterface {
  private static UserInterface userInterface;
  private static Warehouse warehouse;
  private BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
  private static final int EXIT = 0;
  private static final int ADD_CUSTOMER = 1;
  private static final int ADD_SUPPLIER = 2;
  private static final int ADD_MANUFACTURER = 3;
  private static final int ADD_PRODUCT = 4;
  private static final int CHANGE_SALEPRICE = 5;
  private static final int CHANGE_CUSTOMERADDRESS = 6;
  private static final int CHANGE_PRODUCTQUANTITY = 7;
  private static final int ASSIGN_PRODTOMANUFACTURER = 8;
  private static final int ADD_CUSTOMER_ORDER = 9;
  private static final int ACCEPT_C_PAYMENT = 10;
  private static final int LIST_CUSTOMERS = 11;
  private static final int LIST_C_WITH_OB = 12;
  private static final int LIST_MANUFACTURERS = 13;
  private static final int LIST_PRODUCTS = 14;
  private static final int LIST_PROD_BY_MANU = 15;
  private static final int LIST_SUPP_OF_PROD = 16;
  private static final int RETRIEVE = 17;
  private static final int SAVE = 18;
  private static final int HELP = 19;
  private UserInterface() {
    if (yesOrNo("Look for saved data and  use it?")) {
      retrieve();
    } else {
      warehouse = Warehouse.instance();
    }
  }
  public static UserInterface instance() {
    if (userInterface == null) {
      return userInterface = new UserInterface();
    } else {
      return userInterface;
    }
  }
  public String getToken(String prompt) {
    do {
      try {
        System.out.println(prompt);
        String line = reader.readLine();
        StringTokenizer tokenizer = new StringTokenizer(line,"\n\r\f");
        if (tokenizer.hasMoreTokens()) {
          return tokenizer.nextToken();
        }
      } catch (IOException ioe) {
        System.exit(0);
      }
    } while (true);
  }
  private boolean yesOrNo(String prompt) {
    String more = getToken(prompt + " (Y|y)[es] or anything else for no");
    if (more.charAt(0) != 'y' && more.charAt(0) != 'Y') {
      return false;
    }
    return true;
  }
  public int getNumber(String prompt) {
    do {
      try {
        String item = getToken(prompt);
        Integer num = Integer.valueOf(item);
        return num.intValue();
      } catch (NumberFormatException nfe) {
        System.out.println("Please input a number ");
      }
    } while (true);
  }
  public Calendar getDate(String prompt) {
    do {
      try {
        Calendar date = new GregorianCalendar();
        String item = getToken(prompt);
        DateFormat df = SimpleDateFormat.getDateInstance(DateFormat.SHORT);
        date.setTime(df.parse(item));
        return date;
      } catch (Exception fe) {
        System.out.println("Please input a date as mm/dd/yy");
      }
    } while (true);
  }
  public int getCommand() {
    do {
      try {
        int value = Integer.parseInt(getToken("Enter command:" + HELP + " for help"));
        if (value >= EXIT && value <= HELP) {
          return value;
        }
      } catch (NumberFormatException nfe) {
        System.out.println("Enter a number");
      }
    } while (true);
  }

  public void help() {
    System.out.println("Enter a number between 0 and 19 as explained below:");
    System.out.println(EXIT + " to Exit\n");
    System.out.println(ADD_CUSTOMER + " to add a customer");
    System.out.println(ADD_SUPPLIER + " to add a supplier");
    System.out.println(ADD_MANUFACTURER + " to add a manufacturer");
    System.out.println(ADD_PRODUCT + " to add a product");
    System.out.println(CHANGE_SALEPRICE + " to to change sale price of a product");
    System.out.println(CHANGE_CUSTOMERADDRESS + " to change customer's address");
    System.out.println(CHANGE_PRODUCTQUANTITY + " to change quantity of a product");
	System.out.println(ASSIGN_PRODTOMANUFACTURER + " to assign a product to a manufacturer");
	System.out.println(ADD_CUSTOMER_ORDER + " to add an item and order for a customer");
	System.out.println(ACCEPT_C_PAYMENT + " to accept a payment from a customer");
	System.out.println(LIST_CUSTOMERS + " to get a list of customers");
	System.out.println(LIST_C_WITH_OB + " to get a list of customers with outstanding balance");
    System.out.println(LIST_MANUFACTURERS + " to get a list of manufacturers");
    System.out.println(LIST_PRODUCTS + " to get a list of products");
	System.out.println(LIST_PROD_BY_MANU + " to list products supplied by a specified manufacturer");
	System.out.println(LIST_SUPP_OF_PROD + " to list suppliers and their prices of a product");
	System.out.println(RETRIEVE + " retrieve warehouse");
    System.out.println(SAVE + " to  save data");
    System.out.println(HELP + " for help");
  }

  public void addCustomer() {
    
	String name = getToken("Enter member name");
    String address = getToken("Enter address");
    String phone = getToken("Enter phone");
	double billing;
    while (true)
    {
      String balance = getToken("Enter customer billing info(balance): ");
      try {
        billing = Double.parseDouble(balance);
        break; // will only get to here if input was a double
        } catch (NumberFormatException ignore) {
        System.out.println("Invalid input");
      }
    }
    Customer result;
    result = warehouse.addCustomer(name, phone, address, billing);
    if (result == null) {
      System.out.println("Could not add customer");
    }
    System.out.println(result);
	
	
  }

 public void addSupplier() {

      System.out.println("Dummy Action");
  }

public void addManufacturer() {
	String name = getToken("Enter manufacturer name");
    String address = getToken("Enter address");
    String phone = getToken("Enter phone");
    Manufacturer result;
    result = warehouse.addManufacturer(name, phone, address);
    if (result == null) {
      System.out.println("Could not add manufacturer");
    }
	System.out.println(result);
  }
  
  public void addProduct() {
      String Name = getToken("Enter product name: ");
    Product result;
    result = warehouse.addProduct(Name);
    if (result == null)
    {
      System.out.println("Could not add product.");
    }
    else
    {
      System.out.println(result);
    }
  }
    
  public void changeSalePrice() {
      System.out.println("Dummy Action");
  }
    
  public void changeCustomerAddress() {
      System.out.println("Dummy Action");
  }
    
  public void changeProductQuantity() {
      System.out.println("Dummy Action");
  }
  
public void assignProduct()
  {
    String pID = getToken("Enter product ID: ");
    Product product;
    if ((product=warehouse.searchProduct(pID)) == null)
    {
      System.out.println("Product does not exist.");
      return;
    }

    String mID = getToken("Enter manufacturer ID: ");
    Manufacturer m;
    if ((m=warehouse.searchManufacturer(mID)) == null)
    {
      System.out.println("No such manufacturer.");
      return;
    }

    double p;
    while (true)
    {
      String price = getToken("Enter product unit price: ");
      try {
        p = Double.parseDouble(price);
        break; // will only get to here if input was a double
        } catch (NumberFormatException ignore) {
        System.out.println("Invalid input");
      }
    }

    int q;
    while (true)
    {
      String quantity = getToken("Enter product quantity:  (if unknown or NA, enter 0)");
      try {
        q = Integer.parseInt(quantity);
        break; // will only get to here if input was an int
        } catch (NumberFormatException ignore) {
        System.out.println("Invalid input");
      }
    }  
	
	product = warehouse.assignProdToManufacturer(pID, mID, p, q);
      if (product != null)
      {
        System.out.println("Product " + product.getProdName() + " assigned to " + m.getName() + " successfully.");
      }
      else
      {
        System.out.println("Product could not be assigned.");
      }
  }
  public void addItemToCart(){
		String cID = getToken("Enter customer ID");
		Customer customer = warehouse.searchCustomer(cID);
		if (customer == null){
			System.out.println("No such customer");
			return;
		}
		CustomerOrder order = warehouse.CreateCustomerOrder(customer);
		      do{
        String pID = getToken("Enter product ID: ");
        Product product = warehouse.searchProduct(pID);
        while (product == null)
        {
          System.out.println("Could not find product.");
          pID = getToken("Enter product ID: ");
          product = warehouse.searchProduct(pID);
        }
        int q;
        while (true)
        {
          String quantity = getToken("Enter quantity: ");
          try {
            q = Integer.parseInt(quantity);
            break; // will only get to here if input was an int
            } catch (NumberFormatException ignore) {
              System.out.println("Invalid input");
          }
        }
        boolean success = warehouse.AddProdToOrder(product, q, order, customer);
        if (success)
        {
          System.out.println("Product added to order successfully.");
        }
        else
        {
          System.out.println("Product couldn't be added to order.");
        }
      }while(yesOrNo("Add another product to order? "));
	  double Total = warehouse.GetOrderTotal(order); 
      System.out.println("Order total is: $" + Total);
      double NewCustomerBalance = warehouse.updateCustomerBalance(customer, Total);
      System.out.println("Customer " + customer.getName() + " new balance is " + NewCustomerBalance);
  }
  
  public void acceptPayment(){
	  int C_count = 1;
    Customer customer;
    String cID = getToken("Enter customer ID: ");
    while((customer = warehouse.searchCustomer(cID)) == null){
      System.out.println("Customer doesn't exist.");
      if(C_count++ == 3){
        System.out.println("You have reached the maximum try. Try next time.\n");
        return;
      }
     cID = getToken("Enter valid Customer ID: ");
    }
    Double Payment;
    while (true)
    {
      String p = getToken("Enter payment amount: ");
      try {
        Payment = Double.parseDouble(p);
        break;
        } catch (NumberFormatException ignore) {
          System.out.println("Invalid input");
      }
    }
    Payment = -1 * Payment; //used so we can use the same updateBalance method in customer
    Double newTotal = warehouse.updateCustomerBalance(customer, Payment);
    System.out.println("Customer " + customer.getName() + "'s new balance is $" + newTotal);
  }
  
  public void listCustomers() {
    Iterator<Customer>  allCustomers = warehouse.getCustomers();
    if(allCustomers.hasNext()==false){
      System.out.println("No Customers exist in the system. \n");
    }else{
      System.out.println("------------------------------------------------------------------------------------");
        while ((allCustomers.hasNext()) != false)
        {
          Customer customer = allCustomers.next();
          System.out.println(customer.toString());
        }
        System.out.println("------------------------------------------------------------------------------------\n");
      }
  }

	public void ListCustomersWithOutstandingBalance()
  {
    Iterator<Customer> allCustomers = warehouse.getCustomers();
    Customer c;
    int i = 1;
     System.out.println("---------------------------------------");
        while (allCustomers.hasNext())
        {
          c = allCustomers.next();
          if (c.getBilling() < 0)
          {
            System.out.println(i + ".) " + c.getID() + ", Remaining Balance: $" + c.getBilling());
            i++;
          }
        }
        System.out.println("---------------------------------------\n");
  }

public void listManufacturers() {
      Iterator<Manufacturer>  allManufacturers = warehouse.getManufacturers();
    if(allManufacturers.hasNext() == false){
      System.out.println("No Manufacturer in the System.\n");
      return;
    }else{
      System.out.println("-------------------------------------------------");
      while ((allManufacturers.hasNext()) != false)
      {
        Manufacturer manufacturer = allManufacturers.next();
        System.out.println(manufacturer.toString());
      }
      System.out.println("-------------------------------------------------\n");
    }
  }
  
  public void listProducts() {
      Product P_temp;
    Iterator<Product> Product_Traversal = warehouse.getProducts();
    while ((Product_Traversal.hasNext()) != false)
    {
      P_temp = Product_Traversal.next();
      System.out.println(P_temp.getProdName());
    }
  }
  
  public void listProductsByManufacturer()
  {
    String m = getToken("Please enter manufacturer ID: ");
    Manufacturer manufacturer = warehouse.searchManufacturer(m);
    if (manufacturer != null)
    {
      Product p_temp;
      Iterator<Product> p_traversal = warehouse.getProductsByManufacturer(manufacturer);
      while (p_traversal.hasNext() != false)
      {
          p_temp = p_traversal.next();
          System.out.println(p_temp.getProdName());
      }
    }
    else
    {
      System.out.println("Manufacturer doesn't exist");
    }
  }
  
  public void listSuppliersOfProduct()
  {
    Double price;
     Product P_temp;
    Iterator<Product> Product_Traversal = warehouse.getProducts();
    while ((Product_Traversal.hasNext()) != false)
    {
      P_temp = Product_Traversal.next();
      System.out.println(P_temp.getProdName());
    System.out.println("-----------------------------------------------");
      Supplier supplier;
      Iterator<Supplier> allSuppliers = warehouse.getSuppliersOfProduct(P_temp);
      while ((allSuppliers.hasNext()) != false)
      {
        supplier = allSuppliers.next();
        System.out.println("Supplier: " + supplier.getManufacturer().getName() + ". Price: $" + supplier.getPrice() + " Quantity: " + supplier.getQuantity());
      }
      System.out.println("-----------------------------------------------\n");
    }
  }

  
  private void save() {
    if (Warehouse.save()) {
      System.out.println(" The warehouse has been successfully saved in the file WarehouseData \n" );
    } else {
      System.out.println(" There has been an error in saving \n" );
    }
  }
  
  private void retrieve() {
    try {
      Warehouse tempWarehouse = Warehouse.retrieve();
      if (tempWarehouse != null) {
        System.out.println(" The warehouse has been successfully retrieved from the file WarehouseData \n" );
        warehouse = tempWarehouse;
      } else {
        System.out.println("File doesnt exist; creating new warehouse" );
        warehouse = Warehouse.instance();
      }
    } catch(Exception cnfe) {
      cnfe.printStackTrace();
    }
  }
  
  public void process() {
    int command;
    help();
    while ((command = getCommand()) != EXIT) {
      switch (command) {
        case ADD_CUSTOMER:      addCustomer();
                                break;
        case ADD_SUPPLIER:      addSupplier();
                                break;
        case ADD_MANUFACTURER:  addManufacturer();
                                break;
        case ADD_PRODUCT:       addProduct();
                                break;
        case CHANGE_SALEPRICE:  changeSalePrice();
                                break;
        case CHANGE_CUSTOMERADDRESS:       changeCustomerAddress();
                                break;
        case CHANGE_PRODUCTQUANTITY:        changeProductQuantity();
								break;
		case ASSIGN_PRODTOMANUFACTURER:		assignProduct();
                                break;
		case ADD_CUSTOMER_ORDER:	addItemToCart();
								break;
		case ACCEPT_C_PAYMENT:		acceptPayment();
								break;
        case LIST_CUSTOMERS:       			listCustomers();
                                break;
		case LIST_C_WITH_OB:	ListCustomersWithOutstandingBalance();
								break;
        case LIST_MANUFACTURERS:      		listManufacturers();
                                break;
        case LIST_PRODUCTS:  				listProducts();
                                break;
		case LIST_PROD_BY_MANU:				listProductsByManufacturer();
								break;
		case LIST_SUPP_OF_PROD: 			listSuppliersOfProduct();
								break;
        case SAVE:              save();
                                break;
        case RETRIEVE:          retrieve();
                                break;		
        case HELP:              help();
                                break;
      }
    }
  }
  public static void main(String[] s) {
    UserInterface.instance().process();
  }
}
