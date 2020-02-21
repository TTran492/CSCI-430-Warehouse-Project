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

  private Warehouse()
  {
    manufacturerList      = ManufacturerList.instance();
    customerList          = CustomerList.instance();
    productList           = ProductList.instance();
    
  }

  public static Warehouse instance()
  {
    if (warehouse == null)
    {
      ManufacturerIDServer.instance();
      ProductIDServer.instance();
      CustomerIDServer.instance();
      
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

public boolean AddProdToOrder(Product product, int quantity, CustomerOrder order, Customer c)
  {
    return order.AddProdToOrder(product, quantity, c);
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


  public static Warehouse retrieve()
  {
    try {
      FileInputStream file = new FileInputStream("WarehouseData");
      ObjectInputStream input = new ObjectInputStream(file);
      input.readObject();
	  CustomerIDServer.retrieve(input);
      ManufacturerIDServer.retrieve(input);
      ProductIDServer.retrieve(input);
      //ManufacturerOrderIDServer.retrieve(input);
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
      //output.writeObject(ManufacturerOrderIDServer.instance());
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
