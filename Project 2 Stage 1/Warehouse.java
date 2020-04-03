import java.util.*;
import java.io.*;
import java.text.*;
import java.util.Scanner;
public class Warehouse implements Serializable{
  private static final long serialVersionUID = 1L;
  public static final int PRODUCT_NOT_FOUND = 1;
  public static final int MANUFACTURER_NOT_FOUND = 2;
  public static final int CUSTOMER_NOT_FOUND = 3;

  private ManufacturerList manufacturerList;
  private ProductList productList;
  private CustomerList customerList;
  private static Warehouse warehouse;
  //private CustomerOrderList customerOrderList;
  private ManufacturerOrderList manufacturerOrderList;
  
  private Warehouse()
  {
    manufacturerList      = ManufacturerList.instance();
    customerList          = CustomerList.instance();
    productList           = ProductList.instance();
    //customerOrderList 	  = CustomerOrderList.instance();
	manufacturerOrderList = ManufacturerOrderList.instance();
  }

  public static Warehouse instance()
  {
    if (warehouse == null)
    {
      ManufacturerIDServer.instance();
      ProductIDServer.instance();
      CustomerIDServer.instance();
	  //CustomerOrderIDServer.instance();
	  ManufacturerOrderIDServer.instance();
      return (warehouse = new Warehouse());
    }
    else {
      return warehouse;
    }
  }

  public Customer addCustomer(String Name, String Phone, String Address, Double Billing)
  {
    Customer customer = new Customer(Name, Phone, Address, Billing);
    if (customerList.insertCustomer(customer))
    {
      return (customer);
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

  public Iterator<Customer> getCustomers()
  {
    return customerList.getCustomers();
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

  public void FulfillWaitlist(Product p, int NewQ, Supplier supplier){
    Iterator<Waitlist> iterator = p.getWaitlistedClients();
    Waitlist w;
    Customer c;
    int WaitlistedQ;
    while ((iterator.hasNext()) && (NewQ >= 0))
    {
      w = iterator.next();
      WaitlistedQ = w.getQuantity();
      c = w.getCustomer();
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
    supplier.setNewQuantity(supplier.getQuantity() - NewQ);
  }

public boolean AddProdToOrder(Product product, int quantity, CustomerOrder order, Customer c)
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
  
   public boolean addManuOrder(ManufacturerOrder order)
  {
    return manufacturerOrderList.addOrder(order);
  }

   public ManufacturerOrder CreateManufacturerOrder(Manufacturer m)
  {
    ManufacturerOrder order = new ManufacturerOrder(m);
    return order;
  }

  public CustomerOrder CreateCustomerOrder(Customer customer)
  {
    return customer.newOrder();
  }

  public double GetOrderTotal(CustomerOrder order)
  {
    return order.getTotal();
  }

  public double updateCustomerBalance(Customer customer, Double orderTotal)
  {
    return customer.updateBalance(orderTotal);
  }

  public Customer searchCustomer(String cID)
  {
    return customerList.search(cID);
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

  public Iterator<ManufacturerOrder> getManuOrders(Manufacturer m)
  {
    return m.getOrders();
  }

  public ManufacturerOrder searchManuOrders(String oID)
  {
    return manufacturerOrderList.search(oID);
  }

 public Supplier searchProductSupplier(Product product, Manufacturer manu){
    return product.SearchSuppList(manu);
  }

  public Customer searchMembership(String customerID){
    return customerList.search(customerID);
  }

  public Iterator<Waitlist> getWaitlistedClients(Product p)
  {
    return p.getWaitlistedClients();
  }

  public Iterator<Waitlist> getWaitlistedProducts(Customer c)
  {
    return c.getWaitlistedProducts();
  }


  public static Warehouse retrieve()
  {
    try {
      FileInputStream file = new FileInputStream("WarehouseData");
      ObjectInputStream input = new ObjectInputStream(file);
      input.readObject();
	  CustomerIDServer.retrieve(input);
      ManufacturerIDServer.retrieve(input);
      ProductIDServer.retrieve(input);
      //CustomerOrderIDServer.retrieve(input);
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
      output.writeObject(CustomerIDServer.instance());
      output.writeObject(ManufacturerIDServer.instance());
      output.writeObject(ProductIDServer.instance());
      //output.writeObject(CustomerOrderIDServer.instance());
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
    return customerList + "\n" + manufacturerList + "\n" + productList + "\n";
  }

}
