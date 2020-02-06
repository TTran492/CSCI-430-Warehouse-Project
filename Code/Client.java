
import java.util.*;
import java.lang.*;
import java.io.*;

public class Client implements Serializable{
	private static final long serialVersionUID = 1L;
	public static final String CLIENT_STRING = "C";
	private String Client_name;
	private String Client_id;
	private String Client_phone;
	private String Client_address;
	private Double Client_billing;
	private List<Waitlist> waitlistedProducts = new LinkedList<Waitlist>();
	public Client(String Name, String phone,  String address, Double billing)
	{
		this.Client_name    = Name;
		this.Client_phone   = phone;
		this.Client_address = address;
		this.Client_billing = billing;

		Client_id = CLIENT_STRING + (ClientIDServer.instance()).getID();//generates id through the client id server
	}

	public String getName()
	{
		return Client_name;
	}

	public String getAddress()
	{
		return Client_address;
	}

	public String getPhone()
	{
		return Client_phone;
	}

	public Double getBilling()
  {
		return Client_billing;
	}

	public Double updateBalance(Double orderPrice)
	{
		this.Client_billing = Client_billing - orderPrice;
		return Client_billing;
	}

	public String getID()
	{
		return Client_id;
	}

	public ClientOrder newOrder()
	{
		ClientOrder order = new ClientOrder();
		return order;
	}

	public boolean addProductToWaitlist(Waitlist w){
		return waitlistedProducts.add(w);
	}

	public boolean removeWaitlistedProduct(Waitlist w)
	{
		return waitlistedProducts.remove(w);
	}

	public Iterator<Waitlist> getWaitlistedProducts()
	{
		return waitlistedProducts.iterator();
	}

	public Waitlist searchWaitListOnProduct(Product p)
	{
		Iterator<Waitlist> iterator = waitlistedProducts.iterator();
		Waitlist w;
		while (iterator.hasNext())
		{
			w = iterator.next();
			if (w.getProduct() == p)
			{
				return w;
			}
		}
		return null;
	}

	public String toString()
	{
		return "Client: " + getName() + ".  Phone: " + getPhone()+ ". ID: "+ getID()+". Address: "+getAddress() + " Balance: $" + getBilling();
	}

}
