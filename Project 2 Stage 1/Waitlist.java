import java.util.*;
import java.io.*;
import java.lang.*;
public class Waitlist implements Serializable {
  private static final long serialVersionUID = 1L;
  private Product product;
  private Customer customer;
  private int quantity;

  public Waitlist(Customer c, Product p, int q){
    this.customer   = c;
    this.product  = p;
    this.quantity = q;
  }

  public Product getProduct(){
    return product;
  }

  public Customer getCustomer(){
    return customer;
  }

  public int getQuantity(){
    return quantity;
  }

  public void updateQuantity(int newQ){
    this.quantity = newQ;
  }
}
