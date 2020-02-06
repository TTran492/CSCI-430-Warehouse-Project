import java.util.*;
import java.text.*;
import java.io.*;

public class ManagerState extends WarehouseState{
  private BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
  private static Warehouse warehouse;
  private static WarehouseContext context;
  private static ManagerState instance;
  private static final int EXIT                          = 0;

  private static final int ADD_MANUFACTURER              = 3;
  private static final int ASSIGN_PRODUCT                = 4;
  private static final int UNASSIGN_PRODUCT              = 5;

  private static final int CLERK_MENU                      = 19;
  private static final int HELP                          = 21;

  private ManagerState(){
    super();
    warehouse = Warehouse.instance();
  }

  public static ManagerState instance(){
    if (instance == null){
      instance = new ManagerState();
    }
    return instance;
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

    System.out.println(ADD_MANUFACTURER + " to add a manufacturer");
    System.out.println(ASSIGN_PRODUCT + " to assign a product to a manufacturer");
    System.out.println(UNASSIGN_PRODUCT + " to unassign a product from manufacturer");

    System.out.println(CLERK_MENU + " to switch to the clerk menu");
    System.out.println(HELP + "  for help\n");
  }

  public void addManufacturer()
  {
    String Name = getToken("Enter manufacturer name: ");
    String Address = getToken("Enter manufacturer address: ");
    String Phone = getToken("Enter manufacturer phone: ");
    Manufacturer result;
    String managerID = WarehouseContext.instance().getUser();
    String managerPassword = getToken("Please enter manager password: ");
    if (Warehouse.instance().SecuritySystem(0, managerID, managerPassword))
    {
      result = warehouse.addManufacturer(Name, Phone, Address);
      if (result == null)
      {
        System.out.println("Could not add manufacturer.");
      }
      else
      {
        System.out.println("Added successfully.");
        System.out.println(result);
      }
    }
    else{
      System.out.println("Incorrect password.");
    }
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

    String managerID = WarehouseContext.instance().getUser();
    String managerPassword = getToken("Please enter manager password: ");
    if (Warehouse.instance().SecuritySystem(0, managerID, managerPassword))
    {
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
    else
    {
        System.out.println("Incorrect password.");
    }

  }

  public void unassignProduct()
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

    String managerID = WarehouseContext.instance().getUser();
    String managerPassword = getToken("Please enter manager password: ");
    if (Warehouse.instance().SecuritySystem(0, managerID, managerPassword))
    {
      product = warehouse.unassignProdFromManufacturer(pID, mID);
      if (product != null)
      {
        System.out.println("Product " + product.getProdName() + " unassigned from " + m.getName() + " successfully.");
      }
      else
      {
        System.out.println("Product could not be unassigned.");
      }
    }
    else{
      System.out.println("Incorrect password.");
    }
  }

  public boolean clerkmenu(){
      String clerkID = getToken("Please enter clerk username: ");
      String clerkPassword = getToken("Please enter clerk password: ");
      if (Warehouse.instance().SecuritySystem(1, clerkID, clerkPassword)){
      (WarehouseContext.instance()).setUser(clerkID);
      (WarehouseContext.instance()).changeState(1);
      return true;
      }
      else{
        System.out.println("Invalid login.");
        return false;
      }
    }

  public void terminate(int exitcode){
    (WarehouseContext.instance()).changeState(exitcode);
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
        case ADD_MANUFACTURER:              addManufacturer();
                                            break;
        case ASSIGN_PRODUCT:                assignProduct();
                                            break;
        case UNASSIGN_PRODUCT:              unassignProduct();
                                            break;
        case CLERK_MENU:                    if (clerkmenu()){
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
    if (!done){
      terminate(0);
    }

  }

  public void run(){
    process();
  }
}
