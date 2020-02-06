import java.io.*;
public class ManufacturerOrderIDServer implements Serializable{
	private int idCounter;
	private static ManufacturerOrderIDServer server;

	private ManufacturerOrderIDServer()
	{
		idCounter = 1;
	}

	public static ManufacturerOrderIDServer instance()
	{
		if (server == null)
		{
			return (server = new ManufacturerOrderIDServer());
		}
		else
		{
			return server;
		}
	}

	public int getCurrentID()
	{
		return idCounter;
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
			server = (ManufacturerOrderIDServer) input.readObject();
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
				server = (ManufacturerOrderIDServer) input.readObject();
			}else{
				input.readObject();
			}
		}catch (IOException ioe){
			ioe.printStackTrace();
		}
	}
}
