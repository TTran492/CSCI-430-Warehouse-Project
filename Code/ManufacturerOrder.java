import java.util.*;
import java.io.*;
import java.lang.*;


public class ManufacturerOrder implements Serializable{
  private static final long serialVersionUID = 1L;
  public static final String MANUFACTURER_ORDER_STRING = "MO";
  private String ID;
  private Boolean RECEIVED_FLAG;
  private Manufacturer manufacturer;
  private List<Product> products = new LinkedList<Product>();
  private List<Integer> quantities = new LinkedList<Integer>();

  public ManufacturerOrder(Manufacturer m){
    this.manufacturer = m;
    this.RECEIVED_FLAG = false;
    ID = MANUFACTURER_ORDER_STRING + (ManufacturerOrderIDServer.instance()).getID();
  }

  public boolean getOrderStatus(){
    return RECEIVED_FLAG;
  }

  public boolean receiveOrder(){
    this.RECEIVED_FLAG = true;
    return RECEIVED_FLAG;
  }

  public boolean addProductToOrder(Product p, int q){
    boolean success = products.add(p);
    success = quantities.add(q);
    return success;
  }

  public String getID(){
    return ID;
  }

  public Iterator<Product> getProds()
  {
    return products.iterator();
  }

  public Iterator<Integer> getQs()
  {
    return quantities.iterator();
  }

  public Manufacturer getManufacturer()
  {
    return manufacturer;
  }
}
