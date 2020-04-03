import java.util.*;
import java.io.*;
public class ManufacturerList implements Serializable{
  private static final long serialVersionUID = 1L;
  private List<Manufacturer> manufacturers = new LinkedList<Manufacturer>();
  private static ManufacturerList manufacturerList;

  private ManufacturerList(){
  }

  public static ManufacturerList instance()
  {
    if (manufacturerList == null)
    {
      return (manufacturerList = new ManufacturerList());
    }
    else
    {
      return manufacturerList;
    }
  }

  public boolean insertManufacturer(Manufacturer manufacturer)
  {
    manufacturers.add(manufacturer);
    return true;
  }

  public Iterator<Manufacturer> getManufacturers()
  {
    return manufacturers.iterator();
  }

  public Manufacturer search(String manufacturerID)
  {
      for (Iterator iterator = manufacturers.iterator(); iterator.hasNext(); )
      {
          Manufacturer manufacturer = (Manufacturer) iterator.next();
          if (manufacturer.getID().equals(manufacturerID))
          {
              return manufacturer;
          }
      }
      return null;
  }



  private void writeObject(java.io.ObjectOutputStream output)
  {
    try{
      output.defaultWriteObject();
      output.writeObject(manufacturerList);
    }catch(IOException ioe){
      ioe.printStackTrace();
    }
  }

  private void readObject(java.io.ObjectInputStream input)
  {
    try{
      if(manufacturerList != null)
      {
        return;
      }
      else
      {
        input.defaultReadObject();
        if(manufacturerList == null)
        {
          manufacturerList = (ManufacturerList) input.readObject();
        }
        else
        {
          input.readObject();
        }
      }
    }catch(IOException ioe){
      ioe.printStackTrace();
    }catch(ClassNotFoundException cnfe){
      cnfe.printStackTrace();
    }
  }

  public String toString()
  {
    return manufacturers.toString();
  }

}
