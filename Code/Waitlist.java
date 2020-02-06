import java.util.*;
import java.io.*;
import java.lang.*;
public class Waitlist implements Serializable {
  private static final long serialVersionUID = 1L;
  private Product product;
  private Client client;
  private int quantity;

  public Waitlist(Client c, Product p, int q){
    this.client   = c;
    this.product  = p;
    this.quantity = q;
  }

  public Product getProduct(){
    return product;
  }

  public Client getClient(){
    return client;
  }

  public int getQuantity(){
    return quantity;
  }

  public void updateQuantity(int newQ){
    this.quantity = newQ;
  }

  public String toString(){
    return "Client: " + getClient().getID() + ", Product: " + getProduct() + ", Quantity: " + getQuantity();
  }
}
