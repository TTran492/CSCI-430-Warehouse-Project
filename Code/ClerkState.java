import java.util.*;
import java.text.*;
import java.io.*;

public class ClerkState extends WarehouseState{
  private BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
  private static ClerkState clerkstate;
  private static Warehouse warehouse;
  private static final int EXIT                          = 0;
  private static final int ADD_CLIENT                    = 1;
  private static final int ADD_PRODUCT                   = 2;
  private static final int LIST_CLIENTS                  = 6;
  private static final int LIST_PRODUCTS                 = 7;
  private static final int LIST_MANUFACTURERS            = 8;
  private static final int LIST_SUPP_OF_PROD             = 9;
  private static final int LIST_PROD_BY_MANU             = 10;
  private static final int PLACE_ORDER_WITH_MANUFACTURER = 12;
  private static final int LIST_C_W_OUTST_BAL            = 14;
  private static final int GET_WAIT_ORD_FOR_PROD         = 15;
  private static final int GET_WAIT_ORD_FOR_CLIENT       = 16;
  private static final int GET_LIST_ORDERS_MANU          = 17;
  private static final int RECEIVE_MANU_ORDER            = 18;
  private static final int USERMENU                      = 19;
  private static final int HELP                          = 21;

  private ClerkState(){
    warehouse = Warehouse.instance();
  }

  public static ClerkState instance(){
    if (clerkstate == null){
      return clerkstate = new ClerkState();
    }
    return clerkstate;
  }

  public String getToken(String prompt)
  {
    do{
      try{
        System.out.println(prompt);
        String line = reader.readLine();
        StringTokenizer tokenizer = new StringTokenizer(line, "\n\r\f");
        if (tokenizer.hasMoreTokens())
        {
          return tokenizer.nextToken();
        }
      } catch (IOException ioe){
        System.exit(0);
      }
    } while (true);
  }

  private boolean yesOrNo(String prompt)
  {
    String more = getToken(prompt + " (Y|y)es or anything else for no");
    if (more.charAt(0) != 'y' && more.charAt(0) != 'Y')
    {
      return false;
    }
    else
    {
      return true;
    }
  }

  public int getNumber(String prompt)
  {
    do {
      try {
        String item = getToken(prompt);
        Integer num = Integer.valueOf(item);
        return num.intValue();
      } catch (NumberFormatException nfe){
        System.out.println("Please input a number ");
      }
    } while (true);
  }

  public Calendar getDate(String prompt)
  {
    do {
      try{
        Calendar date = new GregorianCalendar();
        String item = getToken(prompt);
        DateFormat df = SimpleDateFormat.getDateInstance(DateFormat.SHORT);
        date.setTime(df.parse(item));
        return date;
      } catch (Exception fe){
        System.out.println("Please input a date as mm/dd/yy");
      }
    } while (true);
  }

  public int getCommand()
  {
    do {
      try{
        int value = Integer.parseInt(getToken("Enter command:    (" + HELP + "for help)"));
        if (value >= EXIT && value <= HELP)
        {
            return value;
        }
      }catch (NumberFormatException nfe){
        System.out.println("Enter a number");
      }
    }while (true);
  }

  public void help()
  {
    System.out.println("\nEnter a number between 0 and 21 as explained below:");
    System.out.println(EXIT + " to Exit");
    System.out.println(ADD_CLIENT + " to add a client");
    System.out.println(ADD_PRODUCT + " to add a product");
    System.out.println(LIST_CLIENTS + " to list all clients");
    System.out.println(LIST_PRODUCTS + " to list all products");
    System.out.println(LIST_MANUFACTURERS + " to list all manufacturers");
    System.out.println(LIST_SUPP_OF_PROD + " to list suppliers of a specified product");
    System.out.println(LIST_PROD_BY_MANU + " to list products supplied by a specified manufacturer");
    System.out.println(PLACE_ORDER_WITH_MANUFACTURER + " to place an order with a manufacturer");
    System.out.println(LIST_C_W_OUTST_BAL + " to list clients with an outstanding balance");
    System.out.println(GET_WAIT_ORD_FOR_PROD + " to get a list of waitlisted orders for a product");
    System.out.println(GET_WAIT_ORD_FOR_CLIENT + " to get a list of waitlisted orders for a client");
    System.out.println(GET_LIST_ORDERS_MANU + " to get a list of orders placed with a manufacturer");
    System.out.println(RECEIVE_MANU_ORDER + " to receive and process a manufacturer order");
    System.out.println(USERMENU + " to switch to the user menu");
    System.out.println(HELP + "  for help\n");
  }

  public void addClient()
  {
    String Name = getToken("Enter client name: ");
    String Address = getToken("Enter client address: ");
    String Phone = getToken("Enter client phone: ");
    double Billing;
    while (true)
    {
      String balance = getToken("Enter client billing info(balance): ");
      try {
        Billing = Double.parseDouble(balance);
        break; // will only get to here if input was a double
        } catch (NumberFormatException ignore) {
        System.out.println("Invalid input");
      }
    }
    Client result;
    result = warehouse.addClient(Name, Phone, Address, Billing);
    if (result == null)
    {
      System.out.println("Could not add client.");
    }
    else
    {
      System.out.println(result);
    }
  }

  public void addProduct()
  {
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

  public void listClients()
  {
    Client C_temp;
    Iterator<Client>  Client_Traversal = warehouse.getClients();
    while ((Client_Traversal.hasNext()) != false)
    {
      C_temp = Client_Traversal.next();
      System.out.println(C_temp.getName());
    }
  }

  public void listProducts()
  {
    Product P_temp;
    Iterator<Product> Product_Traversal = warehouse.getProducts();
    while ((Product_Traversal.hasNext()) != false)
    {
      P_temp = Product_Traversal.next();
      System.out.println(P_temp.getProdName());
    }
  }

  public void listManufacturers()
  {
    Manufacturer M_temp;
    Iterator<Manufacturer>  Manu_Traversal = warehouse.getManufacturers();
    while ((Manu_Traversal.hasNext()) != false)
    {
      M_temp = Manu_Traversal.next();
      System.out.println(M_temp.getName());
    }
  }

  public void listSuppliersOfProduct()
  {
    Supplier supplier;
    Double price;
    String pID = getToken("Enter the product ID: ");
    Product product = warehouse.searchProduct(pID);
    if (product != null)
    {
      Iterator<Supplier> s_traversal = warehouse.getSuppliersOfProduct(product);
      while ((s_traversal.hasNext()) != false)
      {
        supplier = s_traversal.next();
        System.out.println("Supplier: " + supplier.getManufacturer().getName() + ". Supply Price: $" + supplier.getPrice() + " Quantity: " + supplier.getQuantity());
      }
    }
    else
    {
      System.out.println("Product not found");
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

  public void PlaceOrderWithManufacturer()
  {
    String pID;
    int quantity;
    Product product;
    Supplier supplier;
    Boolean success;
    String mID = getToken("Enter manufacturer ID: ");
    Manufacturer manufacturer = warehouse.searchManufacturer(mID);
    if (manufacturer == null)
    {
      System.out.println("Manufacturer doesn't exist");
      return;
    }
    ManufacturerOrder order = warehouse.CreateManufacturerOrder(manufacturer);
    if (order == null){
      return;
    }
    do {
      pID = getToken("Enter product ID: ");
      product = warehouse.searchProduct(pID);
      while (product == null)
      {
        System.out.println("No such product: ");
        pID = getToken("Enter product ID: ");
        product = warehouse.searchProduct(pID);
      }

      supplier = warehouse.searchProductSupplier(product, manufacturer);
      while (supplier == null)
      {
        System.out.println("Product isn't supplied by specified manufacturer");
        pID = getToken("Enter product ID: ");
        product = warehouse.searchProduct(pID);
        while (product == null)
        {
          System.out.println("No such product: ");
          pID = getToken("Enter product ID: ");
          product = warehouse.searchProduct(pID);
        }
        supplier = warehouse.searchProductSupplier(product, manufacturer);
      }


      while (true)
      {
        String q = getToken("Enter quantity: ");
        try {
          quantity = Integer.parseInt(q);
          break; // will only get to here if input was an int
          } catch (NumberFormatException ignore) {
            System.out.println("Invalid input");
        }
      }
      success = warehouse.AddProductsToManuOrder(product, quantity, order);
      if (!success){
        System.out.println("Couldn't add to order.");
        return;
      }
    }while (yesOrNo("Add another product to order? "));
    success = warehouse.AddOrderToManufacturer(manufacturer, order);
    success = warehouse.addManuOrder(order);
    if (success)
    {
      System.out.println("Order added successfully");
      System.out.println("Order ID: " + order.getID());
    }

    else
    {
      System.out.println("Failed to add order");
    }
  }

  public void ListClientsWithOutstandingBalance()
  {
    Iterator<Client> C_Traversal = warehouse.getClients();
    Client c;
    int i = 1;
    while (C_Traversal.hasNext())
    {
      c = C_Traversal.next();
      if (c.getBilling() < 0)
      {
        System.out.println(i + ".) " + c.getID() + ", Bal: $" + c.getBilling());
        i++;
      }
    }
  }

  public void GetWaitlistedOrdersForProd()
  {
    String pID = getToken("Enter product ID: ");
    Product p = warehouse.searchProduct(pID);
    int i = 1;
    if (p == null)
    {
      System.out.println("Product doesn't exist");
      return;
    }
    Iterator<Waitlist> w_Traversal = warehouse.getWaitlistedClients(p);
    Waitlist w;
    while (w_Traversal.hasNext())
    {
      w = w_Traversal.next();
      System.out.println(i + ".) Client: " + w.getClient().getID() + ", Amount Waitlisted: " + w.getQuantity());
      i++;
    }
  }

  public void GetWaitlistedOrdersForClient()
  {
    String cID = getToken("Enter client ID: ");
    Client c = warehouse.searchClient(cID);
    int i = 1;
    if (c == null)
    {
      System.out.println("Client doesn't exist");
      return;
    }
    Iterator<Waitlist> c_Traversal = warehouse.getWaitlistedProducts(c);
    Waitlist w;
    while (c_Traversal.hasNext())
    {
      w = c_Traversal.next();
      System.out.println(i + ".) Product: " + w.getProduct().getID() + ", Amount Waitlisted: " + w.getQuantity());
      i++;
    }
  }

  private void ListOrdersPlacedWithManufacturer()
  {
    String mID = getToken("Enter manufacturer ID: ");
    Manufacturer m = warehouse.searchManufacturer(mID);
    int i = 1;
    if (m == null)
    {
      System.out.println("Manufacturer doesn't exist");
      return;
    }
    Iterator<ManufacturerOrder> o_Traversal = warehouse.getManuOrders(m);
    ManufacturerOrder order;
    while (o_Traversal.hasNext())
    {
      System.out.println("ORDER NUMBER: " + i + "\n---------------");
      order = o_Traversal.next();
      System.out.println("Oder ID: " + order.getID());
      System.out.println("Received: " + order.getOrderStatus());
      Iterator<Product> p_Traversal = order.getProds();
      Product p;
      Iterator<Integer> q_Traversal = order.getQs();
      int q;
      while (p_Traversal.hasNext() && q_Traversal.hasNext())
      {
        int j = 1;
        p = p_Traversal.next();
        q = q_Traversal.next();
        System.out.println("Product: " + p.getID() + ", Quantity: " + q);
        j++;
      }
      i++;
      System.out.println("");
    }
  }

  private void ReceiveManufacturerOrder()
  {
    String oID = getToken("Enter the order ID: ");
    ManufacturerOrder order = warehouse.searchManuOrders(oID);
    if (order == null)
    {
      System.out.println("No such order");
      return;
    }
    if (order.getOrderStatus() == true)
    {
      System.out.println("Order has already been processed and received");
      return;
    }
    Manufacturer manufacturer = order.getManufacturer();
    Iterator<Product> p_Traversal = order.getProds();
    Product p;
    Iterator<Integer> q_Traversal = order.getQs();
    int q;
    System.out.println("Order details");
    System.out.println("-------------");
    while (p_Traversal.hasNext() && q_Traversal.hasNext())
    {
      int j = 1;
      p = p_Traversal.next();
      q = q_Traversal.next();
      System.out.println("Product: " + p.getID() + ", Quantity: " + q);
      j++;
    }
    boolean add = yesOrNo("Accept order?");
    if (add)
    {
      Iterator<Product> prod_Traversal = order.getProds();
      Product prod;
      Iterator<Integer> quant_Traversal = order.getQs();
      int quant;
      Supplier supplier;
      while (prod_Traversal.hasNext() && quant_Traversal.hasNext())
      {
        int j = 1;
        prod = prod_Traversal.next();
        quant = quant_Traversal.next();
        supplier = warehouse.searchProductSupplier(prod, manufacturer);
        if (supplier.getQuantity() == 0) //could be a waitlist
        {
          supplier.setNewQuantity(-1 * quant);
          warehouse.FulfillWaitlist(prod, quant, supplier);
        }
        else //waitlist still possible
        {
          supplier.setNewQuantity(-1 * quant);
          warehouse.FulfillWaitlist(prod, quant, supplier);
        }
        j++;
      }
      order.receiveOrder();
      System.out.println("Order received successfully. Inventory has been updated");
    }
  }

  public boolean usermenu(){
    String userID = getToken("Please input the user ID: ");
    if (Warehouse.instance().searchMembership(userID) != null){
      (WarehouseContext.instance()).setUser(userID);
      (WarehouseContext.instance()).changeState(1);
      return true;

    }
    else{
      System.out.println("Invalid user id.");
      return false;
    }
  }

  public void terminate(){
    if ((WarehouseContext.instance()).getLogin() == WarehouseContext.IsClerk)
    {
      (WarehouseContext.instance()).changeState(0); //exit with a code 0
    }
    else if ((WarehouseContext.instance()).getLogin() == WarehouseContext.IsManager)
    {
      (WarehouseContext.instance()).changeState(2); //exit with a code 2
    }
  }

  public void process()
  {
    boolean done = false;
    help();
    int command = getCommand();
    while ((command != 0) && (done == false))
    {
      switch (command)
      {
        case ADD_CLIENT:                    addClient();
                                            break;
        case ADD_PRODUCT:                   addProduct();
                                            break;
        case LIST_CLIENTS:                  listClients();
                                            break;
        case LIST_PRODUCTS:                 listProducts();
                                            break;
        case LIST_MANUFACTURERS:            listManufacturers();
                                            break;
        case LIST_SUPP_OF_PROD:             listSuppliersOfProduct();
                                            break;
        case LIST_PROD_BY_MANU:             listProductsByManufacturer();
                                            break;
        case PLACE_ORDER_WITH_MANUFACTURER: PlaceOrderWithManufacturer();
                                            break;
        case LIST_C_W_OUTST_BAL:            ListClientsWithOutstandingBalance();
                                            break;
        case GET_WAIT_ORD_FOR_PROD:         GetWaitlistedOrdersForProd();
                                            break;
        case GET_WAIT_ORD_FOR_CLIENT:       GetWaitlistedOrdersForClient();
                                            break;
        case GET_LIST_ORDERS_MANU:          ListOrdersPlacedWithManufacturer();
                                            break;
        case RECEIVE_MANU_ORDER:            ReceiveManufacturerOrder();
                                            break;
        case USERMENU:                      if (usermenu()){
                                              done = true;
                                              command = 0;
                                            }
                                            break;
        case HELP:                          help();
                                            break;
      }
      if (!done) {
        command = getCommand();
      }
      else{
        break;
      }
    }
    if (!done) {
      terminate();
    }
  }
  public void run(){
    process();
  }
}
