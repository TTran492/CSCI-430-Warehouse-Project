import java.io.*;
public class CustomerIDServer implements Serializable{
	private int idCounter;
	private static CustomerIDServer server;

	private CustomerIDServer()
	{
		idCounter = 1;
	}

	public static CustomerIDServer instance()
	{
		if (server == null)
		{
			return (server = new CustomerIDServer());
		}
		else
		{
			return server;
		}
	}

	public int getID()
	{
		return idCounter++;
	}

	public String toString()
	{
		return ("IDServer" + idCounter);
	}

	public static void retrieve(ObjectInputStream input)
	{
		try{
			server = (CustomerIDServer) input.readObject();
		} catch(IOException ioe){
			ioe.printStackTrace();
		} catch(Exception cnfe){
			cnfe.printStackTrace();
		}
	}

	private void writeObject(java.io.ObjectOutputStream output) throws IOException
	{
		try{
			output.defaultWriteObject();
			output.writeObject(server);
		}catch(IOException ioe){
			ioe.printStackTrace();
		}
	}

	private void readObject(java.io.ObjectInputStream input) throws IOException, ClassNotFoundException{
		try{
			input.defaultReadObject();
			if(server == null){
				server = (CustomerIDServer) input.readObject();
			}else{
				input.readObject();
			}
		}catch (IOException ioe){
			ioe.printStackTrace();
		}
	}
}
