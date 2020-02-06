import java.util.*;
import java.lang.*;
import java.io.*;

public class ManufacturerOrderList implements Serializable {
    private static final long serialVersionUID = 1L;
    private List<ManufacturerOrder> ManufacturerOrders = new LinkedList<ManufacturerOrder>();
    private static ManufacturerOrderList manufacturerOrderList;

    private ManufacturerOrderList() {}

    public boolean addOrder(ManufacturerOrder order){
      return ManufacturerOrders.add(order);
    }

    public static ManufacturerOrderList instance() {
          if (manufacturerOrderList == null) {
              return (manufacturerOrderList = new ManufacturerOrderList());
          }
          else {
              return manufacturerOrderList;
          }
    }

    public ManufacturerOrder search(String orderID)
    {
        for (Iterator iterator = ManufacturerOrders.iterator(); iterator.hasNext(); )
        {
            ManufacturerOrder manufacturerOrder = (ManufacturerOrder) iterator.next();
            if (manufacturerOrder.getID().equals(orderID))
            {
                return manufacturerOrder;
            }
        }
        return null;
    }

    private void writeObject(java.io.ObjectOutputStream output) {
          try {
              output.defaultWriteObject();
              output.writeObject(manufacturerOrderList);
          }
          catch(IOException ioe) {
              System.out.println(ioe);
          }
    }

    private void readObject(java.io.ObjectInputStream input) {
          try {
              if (manufacturerOrderList != null) {
                  return;
              }
              else {
                  input.defaultReadObject();
                  if (manufacturerOrderList == null) {
                      manufacturerOrderList = (ManufacturerOrderList) input.readObject();
                  }
                  else {
                      input.readObject();
                  }
              }
          }
          catch(IOException ioe) {
              System.out.println("in ManufacturerOrders readObject \n" + ioe);
          }
          catch(ClassNotFoundException cnfe) {
              cnfe.printStackTrace();
          }
      }
}
