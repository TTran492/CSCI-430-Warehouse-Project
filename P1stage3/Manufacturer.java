import java.util.*;
import java.lang.*;
import java.io.*;

public class Manufacturer implements Serializable{
	private static final long serialVersionUID = 1L;
	public static final String MANUFACTURER_STRING = "M";
	private String Name;
	private String ID;
	private String address;
	private String phone;
	private List<Product> Products = new LinkedList<Product>();
	private List<ManufacturerOrder> orders = new LinkedList<ManufacturerOrder>();

	public Manufacturer(String Name, String phone, String address)
	{
		this.Name    = Name;
		this.address = address;
		this.phone   = phone;
		ID = MANUFACTURER_STRING + (ManufacturerIDServer.instance()).getID();
	}

	public boolean assignProduct(Product P)
	{
		if (Products.add(P))
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	public int getSizeProdList()
	{
		return Products.size();
	}

	public boolean removeProduct(Product P)
	{
		if (Products.remove(P))
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	public boolean equals(String id) {
			return this.ID.equals(id);
	}

	public String getName()
	{
		return Name;
	}

	public String getAddress()
	{
		return address;
	}

	public String getPhone()
	{
		return phone;
	}


	public String getID()
	{
		return ID;
	}

	public Iterator<Product> getProducts()
	{
		return Products.iterator();
	}

	public Boolean add_Order(ManufacturerOrder o)
	{
		return orders.add(o);
	}

	public Iterator<ManufacturerOrder> getOrders()
	{
		return orders.iterator();
	}

	public String toString()
	{
		return "Manufacturer: " + getName() + ". ID: " + getID() + ". Phone: " + getPhone();
	}

}
