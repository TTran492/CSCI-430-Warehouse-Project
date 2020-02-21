import java.util.*;
import java.io.*;
import java.lang.*;


public class CustomerOrder implements Serializable{
  private static final long serialVersionUID = 1L;
  private double TotalPrice = 0;
  private List<Product> products = new LinkedList<Product>();

  public CustomerOrder(){}

  public boolean AddProdToOrder(Product product, int quantity, Customer customer){
    boolean success = products.add(product);
    if(success)
    {

      Iterator<Supplier> suppliers = product.getSuppliers();
      Supplier temp_s;
      int TempQuantity = 0;
      double temp_p;
      while ((suppliers.hasNext()))
      {
        temp_s = suppliers.next();
        TempQuantity = temp_s.getQuantity();
        if (TempQuantity > 0)
        {
          if (TempQuantity >= quantity)
          {
            temp_s.setNewQuantity(quantity);
            temp_p = temp_s.getPrice();
            this.TotalPrice = TotalPrice + (quantity * temp_p);
            quantity = 0;
          }
          else
          {
            quantity = quantity - TempQuantity;
            temp_s.setNewQuantity(TempQuantity);
            temp_p = temp_s.getPrice();
            this.TotalPrice = TotalPrice + (TempQuantity * temp_p);
          }
        }
      }
/*     
	 if (quantity != 0)
      {
        Waitlist w = new Waitlist(customer, product, quantity);
        success = customer.addProductToWaitlist(w);
        success = product.addCustomerToWaitlist(w);
        if (success){
          System.out.println("Order successful, but " + quantity + " were added to a waitlist.");
        }
        else{
          System.out.println("Order successful, but products weren't successfully added to a waitlist.");
        }
      }
*/
      return true;
    }
    else
    {
      return false;
    }
  }

  public double getTotal(){
    return TotalPrice;
  }


}
