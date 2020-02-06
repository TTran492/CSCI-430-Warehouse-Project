import java.util.*;
import java.io.*;
import java.text.*;
import java.util.Scanner;
public class Warehouse implements Serializable{
  private static final long serialVersionUID = 1L;
  public static final int PRODUCT_NOT_FOUND = 1;
  public static final int MANUFACTURER_NOT_FOUND = 2;
  public static final int CLIENT_NOT_FOUND = 3;

  private ManufacturerList manufacturerList;
  private ProductList productList;
  private ClientList clientList;
  private ManufacturerOrderList manufacturerOrderList;

  private static Warehouse warehouse;

  private Warehouse()
  {
    manufacturerList      = ManufacturerList.instance();
    clientList            = ClientList.instance();
    productList           = ProductList.instance();
    manufacturerOrderList = ManufacturerOrderList.instance();
  }

  public static Warehouse instance()
  {
    if (warehouse == null)
    {
      ManufacturerIDServer.instance();
      ProductIDServer.instance();
      ClientIDServer.instance();
      ManufacturerOrderIDServer.instance();
      return (warehouse = new Warehouse());
    }
    else {
      return warehouse;
    }
  }

  public Client addClient(String Name, String Phone, String Address, Double Billing)
  {
    Client client = new Client(Name, Phone, Address, Billing);
    if (clientList.insertClient(client))
    {
      return (client);
    }
    else
    {
      return null;
    }
  }

  public Product addProduct(String Name)
  {
    Product product = new Product(Name);
    if (productList.insertProduct(product))
    {
      return (product);
    }
    else
    {
      return null;
    }
  }

  public Manufacturer addManufacturer(String Name, String Phone, String Address)
  {
    Manufacturer manufacturer = new Manufacturer(Name, Phone, Address);
    if (manufacturerList.insertManufacturer(manufacturer))
    {
      return (manufacturer);
    }
    else
    {
      return null;
    }
  }

  public boolean addManuOrder(ManufacturerOrder order)
  {
    return manufacturerOrderList.addOrder(order);
  }

  public Iterator<Client> getClients()
  {
    return clientList.getClients();
  }

  public Iterator<Product> getProducts()
  {
    return productList.getProducts();
  }

  public Iterator<Manufacturer> getManufacturers()
  {
    return manufacturerList.getManufacturers();
  }

  public Product assignProdToManufacturer(String pID, String mID, Double price, int quantity)
  {
    Product product = productList.search(pID);
    if (product == null)
    {
      return null;
    }

    Manufacturer manufacturer = manufacturerList.search(mID);
    if (manufacturer == null)
    {
      return null;
    }

    Supplier S = product.SearchSuppList(manufacturer);
    if (S != null)
    {
      return null;
    }

    boolean success = product.link(manufacturer, quantity, price);
    success = manufacturer.assignProduct(product);
    if (success){
      return product;
    } else{
      return null;
    }
  }

  public Product unassignProdFromManufacturer(String pID, String mID)
  {
    Product product = productList.search(pID);
    if (product == null)
    {
      return null;
    }

    Manufacturer manufacturer = manufacturerList.search(mID);
    if (manufacturer == null)
    {
      return null;
    }

    Supplier S = product.SearchSuppList(manufacturer);
    if (S == null)
    {
      System.out.println("Product already isn't assigned to this manufacturer.");
      return null;
    }

    boolean success = product.unlink(S);
    success = manufacturer.removeProduct(product);
    if (success){
      return product;
    } else{
      System.out.println("Error 4");
      return null;
    }
  }

  public Client searchClient(String cID)
  {
    return clientList.search(cID);
  }

  public Product searchProduct(String pID)
  {
    return productList.search(pID);
  }

  public Manufacturer searchManufacturer(String mID)
  {
    return manufacturerList.search(mID);
  }

  public Iterator<Supplier> getSuppliersOfProduct(Product p)
  {
    return p.getSuppliers();
  }

  public Iterator<Product> getProductsByManufacturer(Manufacturer m)
  {
    return m.getProducts();
  }

  public Supplier searchProductSupplier(Product product, Manufacturer manu){
    return product.SearchSuppList(manu);
  }

  public void FulfillWaitlist(Product p, int NewQ, Supplier supplier){
    Iterator<Waitlist> iterator = p.getWaitlistedClients();
    Waitlist w;
    Client c;
    int WaitlistedQ;
    Scanner input = new Scanner(System.in);
    while ((iterator.hasNext()) && (NewQ > 0))
    {
      w = iterator.next();
      WaitlistedQ = w.getQuantity();
      c = w.getClient();
      System.out.println("\n" + w.toString());
      System.out.println("Fulfill client's waitlist? (if items left < client waitlist,\nfulfill most as possible)\nEnter y for yes or anything else for no");
      String decision = input.next();
      if (decision.equals("y"))
      {
        if ((NewQ - WaitlistedQ) >= 0)
        {
          NewQ = NewQ - WaitlistedQ;
          double price = WaitlistedQ * supplier.getPrice();
          c.updateBalance(price);
          iterator.remove();
          Waitlist Wlist = c.searchWaitListOnProduct(p);
          boolean success = c.removeWaitlistedProduct(Wlist);
        }
        else
        {
          double price = NewQ * supplier.getPrice();
          w.updateQuantity(WaitlistedQ - NewQ);
          NewQ = NewQ - NewQ;
          c.updateBalance(price);
        }
      }

    }
    supplier.setNewQuantity(supplier.getQuantity() - NewQ);
  }

  public boolean AddProdToOrder(Product product, int quantity, ClientOrder order, Client c)
  {
    return order.AddProdToOrder(product, quantity, c);
  }

  public boolean AddProductsToManuOrder(Product prod, int q, ManufacturerOrder o)
  {
    return o.addProductToOrder(prod, q);
  }

  public boolean AddOrderToManufacturer(Manufacturer m, ManufacturerOrder o)
  {
    return m.add_Order(o);
  }

  public ManufacturerOrder CreateManufacturerOrder(Manufacturer m)
  {
    ManufacturerOrder order = new ManufacturerOrder(m);
    return order;
  }

  public ClientOrder CreateClientOrder(Client client)
  {
    return client.newOrder();
  }

  public double GetOrderTotal(ClientOrder order)
  {
    return order.getTotal();
  }

  public double updateClientBalance(Client c, Double orderTotal)
  {
    return c.updateBalance(orderTotal);
  }

  public Iterator<Waitlist> getWaitlistedClients(Product p)
  {
    return p.getWaitlistedClients();
  }

  public Iterator<Waitlist> getWaitlistedProducts(Client c)
  {
    return c.getWaitlistedProducts();
  }

  public Iterator<ManufacturerOrder> getManuOrders(Manufacturer m)
  {
    return m.getOrders();
  }

  public ManufacturerOrder searchManuOrders(String oID)
  {
    return manufacturerOrderList.search(oID);
  }

  public Client searchMembership(String clientID){
    return clientList.search(clientID);
  }

  public boolean SecuritySystem(int ManOrClerk, String ID, String Password){
    if (ManOrClerk == 0) //manager
    {
      if (ID.equals("manager") && Password.equals("manager"))
      {
        return true;
      }
      else
      {
        return false;
      }
    }
    else //clerk
    {
      if (ID.equals("salesclerk") && Password.equals("salesclerk"))
      {
        return true;
      }
      else
      {
        return false;
      }
    }
  }

  public static Warehouse retrieve()
  {
    try {
      FileInputStream file = new FileInputStream("WarehouseData");
      ObjectInputStream input = new ObjectInputStream(file);
      input.readObject();
	    ClientIDServer.retrieve(input);
      ManufacturerIDServer.retrieve(input);
      ProductIDServer.retrieve(input);
      ManufacturerOrderIDServer.retrieve(input);
      return warehouse;
    } catch (IOException ioe){
      ioe.printStackTrace();
      return null;
    } catch (ClassNotFoundException cnfe){
      cnfe.printStackTrace();
      return null;
    }
  }

  public static boolean save()
  {
    try{
      FileOutputStream file = new FileOutputStream("WarehouseData");
      ObjectOutputStream output = new ObjectOutputStream(file);
      output.writeObject(warehouse);
      output.writeObject(ClientIDServer.instance());
      output.writeObject(ManufacturerIDServer.instance());
      output.writeObject(ProductIDServer.instance());
      output.writeObject(ManufacturerOrderIDServer.instance());
      return true;
    } catch (IOException ioe){
      ioe.printStackTrace();
      return false;
    }
  }

  private void writeObject(java.io.ObjectOutputStream output)
  {
    try{
      output.defaultWriteObject();
      output.writeObject(warehouse);
    } catch (IOException ioe){
      System.out.println(ioe);
    }
  }

  private void readObject(java.io.ObjectInputStream input)
  {
    try{
      input.defaultReadObject();
      if (warehouse == null)
      {
        warehouse = (Warehouse) input.readObject();
      }
      else
      {
        input.readObject();
      }
    } catch(IOException ioe){
      ioe.printStackTrace();
    } catch (Exception e){
      e.printStackTrace();
    }
  }

  public String toString()
  {
    return clientList + "\n" + manufacturerList + "\n" + productList + "\n" + manufacturerOrderList;
  }

}
