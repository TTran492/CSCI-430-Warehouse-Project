import java.util.*;
import java.lang.*;
import java.io.*;

public class Supplier implements Serializable {
  private static final long serialVersionUID = 1L;
  private Manufacturer manu;
  private int quantity;
  private double price;

  public Supplier(Manufacturer m, int quantity, double price){
    this.manu = m;
    this.quantity = quantity;
    this.price = price;
  }

  public Manufacturer getManufacturer()
  {
    return manu;
  }

  public int getQuantity()
  {
    return quantity;
  }

  public double getPrice()
  {
    return price;
  }

  public void setNewQuantity(int q)
  {
    this.quantity = quantity - q;
  }
}
