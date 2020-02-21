import java.util.*;
import java.lang.*;
import java.io.*;

public class Customer implements Serializable{
	private static final long serialVersionUID = 1L;
	public static final String Customer_STRING = "C";
	private String Customer_name;
	private String Customer_id;
	private String Customer_phone;
	private String Customer_address;
	private Double Customer_billing;
	
	//private List<Waitlist> waitlistedProducts = new LinkedList<Waitlist>();
	public Customer(String Name, String phone,  String address, Double billing)
	{
		this.Customer_name    = Name;
		this.Customer_phone   = phone;
		this.Customer_address = address;
		this.Customer_billing = billing;

		Customer_id = Customer_STRING + (CustomerIDServer.instance()).getID();//generates id through the client id server
	}

	public String getName()
	{
		return Customer_name;
	}

	public String getAddress()
	{
		return Customer_address;
	}

	public String getPhone()
	{
		return Customer_phone;
	}
	public Double getBilling()
 	 {
		return Customer_billing;
	}
	public Double updateBalance(Double orderPrice)
	{
		this.Customer_billing = Customer_billing - orderPrice;
		return Customer_billing;
	}

	public String getID()
	{
		return Customer_id;
	}
	public CustomerOrder newOrder()
	{
		CustomerOrder order = new CustomerOrder();
		return order;
	}

	public String toString()
	{
		return "Customer: " + getName() + ".  Phone: " + getPhone()+ ". ID: "+ getID()+". Address: "+getAddress()+ " Balance: $" + getBilling();

	}

}