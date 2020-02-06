import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.text.*;
import java.io.*;
public class LoginState extends WarehouseState implements ActionListener{
  private static final int CLERK_LOGIN = 0;
  private static final int USER_LOGIN = 1;
  private static final int MANAGER_LOGIN = 2;
  private static final int EXIT  = 3;
  private BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
  private WarehouseContext context;
  private JFrame frame;
  private static LoginState instance;
  private AbstractButton userButton, logoutButton, clerkButton, managerButton;

  private LoginState(){
    super();
    // context = WarehouseContext.instance();
  }

  public static LoginState instance(){
    if (instance == null) {
      instance = new LoginState();
    }
    return instance;
  }

  public void actionPerformed(ActionEvent event){
    if (event.getSource().equals(this.userButton)){
      this.user();
    }
    else if (event.getSource().equals(this.logoutButton)){
      (WarehouseContext.instance()).changeState(3);
    }
    else if (event.getSource().equals(this.clerkButton)){
      this.clerk();
    }
    else if (event.getSource().equals(this.managerButton)){
      this.manager();
    }
  }

  public void clear(){
    frame.getContentPane().removeAll();
    frame.paint(frame.getGraphics());
  }

  public int getCommand(){
    do{
      try{
        int value = Integer.parseInt(getToken("Enter command: "));
        if (value <= EXIT && value >= CLERK_LOGIN){
          return value;
        }
      } catch (NumberFormatException nfe){
        System.out.println("Enter a number");
      }
    } while (true);
  }

  public String getToken(String prompt){
    do {
      try{
        System.out.println(prompt);
        String line = reader.readLine();
        StringTokenizer tokenizer = new StringTokenizer(line, "\n\r\f");
        if (tokenizer.hasMoreTokens()) {
          return tokenizer.nextToken();
        }
      } catch (IOException ioe){
        System.exit(0);
      }
    } while (true);
  }

  private boolean yesOrNo(String prompt){
    String more = getToken(prompt + " (Y|y)es or anything else for no");
    if (more.charAt(0) != 'y' && more.charAt(0) != 'Y'){
      return false;
    }
    return true;
  }

  public void clerk(){
    String clerkID = getToken("Please enter clerk username: ");
    String clerkPassword = getToken("Please enter clerk password: ");
    if (Warehouse.instance().SecuritySystem(1, clerkID, clerkPassword)){
    (WarehouseContext.instance()).setLogin(WarehouseContext.IsClerk);
    (WarehouseContext.instance()).setUser(clerkID);
    clear();
    (WarehouseContext.instance()).changeState(0);
    }
    else{
      JOptionPane.showMessageDialog(frame, "Invalid login credentials");
      (WarehouseContext.instance()).setCurrentState(3);
      (WarehouseContext.instance()).process();
    }
  }

  private void user(){
    String userID = getToken("Please input the user ID: ");
    if (Warehouse.instance().searchMembership(userID) != null){
      (WarehouseContext.instance()).setLogin(WarehouseContext.IsUser);
      (WarehouseContext.instance()).setUser(userID);
      clear();
      (WarehouseContext.instance()).changeState(1);
    }
    else{
      JOptionPane.showMessageDialog(frame, "Invalid user ID.");
    }
  }

  public void manager(){
    String managerID = getToken("Please enter manager username: ");
    String managerPassword = getToken("Please enter manager password: ");
    if (Warehouse.instance().SecuritySystem(0, managerID, managerPassword)){
      (WarehouseContext.instance()).setLogin(WarehouseContext.IsManager);
      (WarehouseContext.instance()).setUser(managerID);
      clear();
      (WarehouseContext.instance()).changeState(2);
    }
    else{
      JOptionPane.showMessageDialog(frame, "Invalid login credentials");
      (WarehouseContext.instance()).setCurrentState(3);
      (WarehouseContext.instance()).process();
    }
  }

  /*public void process(){
    int command;
    System.out.println("Please input 0 to login as a clerk\n" +
                       "             1 to login as a user\n" +
                       "             2 to login as a manager\n" +
                       "             3 to exit the system\n");
    while ((command = getCommand()) != EXIT){
      switch (command){
        case CLERK_LOGIN:     clerk();
                              break;
        case USER_LOGIN:      user();
                              break;
        case MANAGER_LOGIN:   manager();
                              break;
        default:              System.out.println("Invalid choice");
      }
      System.out.println("Please input 0 to login as a clerk\n" +
                         "             1 to login as a user\n" +
                         "             2 to login as a manager\n" +
                         "             3 to exit the system\n");
    }
    (WarehouseContext.instance()).changeState(3);
  }*/

  public void run(){
    frame = WarehouseContext.instance().getFrame();
    frame.getContentPane().removeAll();
    frame.getContentPane().setLayout(new FlowLayout());
      userButton    = new JButton("user");
      clerkButton   = new JButton("clerk");
      managerButton = new ManagerButton();
      logoutButton  = new JButton("logout");
      userButton.addActionListener(this);
      logoutButton.addActionListener(this);
      clerkButton.addActionListener(this);
    frame.getContentPane().add(this.userButton);
    frame.getContentPane().add(this.clerkButton);
    frame.getContentPane().add(this.managerButton);
    frame.getContentPane().add(this.logoutButton);
    frame.setVisible(true);
    frame.paint(frame.getGraphics());
    frame.toFront();
    frame.requestFocus();


    //process();
  }
}
