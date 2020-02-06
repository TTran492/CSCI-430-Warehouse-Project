import java.util.*;
import java.text.*;
import java.io.*;

public class UserState extends WarehouseState{
  private static UserState userstate;
  private BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
  private static Warehouse warehouse;
  private static final int EXIT                          = 0;
  private static final int LIST_PRODUCTS                 = 7;
  private static final int ADD_CLIENT_ORDER              = 11;
  private static final int ACCEPT_CLIENT_PAYMENT         = 13;
  private static final int GET_WAIT_ORD_FOR_CLIENT       = 16;
  private static final int HELP                          = 21;

  private UserState(){
    warehouse = Warehouse.instance();
  }

  public static UserState instance(){
    if (userstate == null){
      return userstate = new UserState();
    }
    return userstate;
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
    System.out.println(LIST_PRODUCTS + " to list all products");
    System.out.println(ADD_CLIENT_ORDER + " to add an order");
    System.out.println(ACCEPT_CLIENT_PAYMENT + " to make a payment");
    System.out.println(GET_WAIT_ORD_FOR_CLIENT + " to get a list of waitlisted orders");
    System.out.println(HELP + "  for help\n");
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

  public void AddClientOrder()
  {
      String clientID = WarehouseContext.instance().getUser();
      Client client = warehouse.searchClient(clientID);
      ClientOrder order = warehouse.CreateClientOrder(client);
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
        boolean success = warehouse.AddProdToOrder(product, q, order, client);
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
      double NewClientBalance = warehouse.updateClientBalance(client, Total);
      System.out.println("Client " + client.getName() + " new balance is " + NewClientBalance);

  }

  public void AcceptClientPayment()
  {
    String clientID = WarehouseContext.instance().getUser();
    Client client = warehouse.searchClient(clientID);
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
    Payment = -1 * Payment; //used so we can use the same updateBalance method in client
    Double newTotal = warehouse.updateClientBalance(client, Payment);
    System.out.println("Client " + client.getName() + " new balance is $" + newTotal);
  }

  public void GetWaitlistedOrdersForClient()
  {
    String clientID = WarehouseContext.instance().getUser();
    Client c = warehouse.searchClient(clientID);
    int i = 1;
    Iterator<Waitlist> c_Traversal = warehouse.getWaitlistedProducts(c);
    Waitlist w;
    while (c_Traversal.hasNext())
    {
      w = c_Traversal.next();
      System.out.println(i + ".) Product: " + w.getProduct().getID() + ", Amount Waitlisted: " + w.getQuantity());
      i++;
    }
  }

  public void process()
  {
    int command;
    help();
    while ((command = getCommand()) != EXIT)
    {
      switch (command)
      {
        case LIST_PRODUCTS:                 listProducts();
                                            break;
        case ADD_CLIENT_ORDER:              AddClientOrder();
                                            break;
        case ACCEPT_CLIENT_PAYMENT:         AcceptClientPayment();
                                            break;
        case GET_WAIT_ORD_FOR_CLIENT:       GetWaitlistedOrdersForClient();
                                            break;
        case HELP:                          help();
                                            break;
      }
    }
    logout();
  }

  public void run(){
    process();
  }

  public void logout()
  {
    if ((WarehouseContext.instance()).getLogin() == WarehouseContext.IsClerk)
    {
      (WarehouseContext.instance()).setUser("salesclerk");
      (WarehouseContext.instance()).changeState(1); //exit with a code 1
    }
    else if ((WarehouseContext.instance()).getLogin() == WarehouseContext.IsManager)
    {
      (WarehouseContext.instance()).changeState(1); //exit with a code 1
    }
    else if (WarehouseContext.instance().getLogin() == WarehouseContext.IsUser)
    {
      (WarehouseContext.instance()).changeState(0); // exit with a code 0
    }
    else
    {
      (WarehouseContext.instance()).changeState(3); //exit code 2, indicates Error
    }
  }

}
