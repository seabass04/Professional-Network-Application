/*
 * Template JAVA User Interface
 * =============================
 *
 * Database Management Systems
 * Department of Computer Science &amp; Engineering
 * University of California - Riverside
 *
 * Target DBMS: 'Postgres'
 *
 */


import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.ArrayList;

/**
 * This class defines a simple embedded SQL utility class that is designed to
 * work with PostgreSQL JDBC drivers.
 *
 */
public class ProfNetwork {

   // reference to physical database connection.
   private Connection _connection = null;

   // handling the keyboard inputs through a BufferedReader
   // This variable can be global for convenience.
   static BufferedReader in = new BufferedReader(
                                new InputStreamReader(System.in));

   /**
    * Creates a new instance of ProfNetwork
    *
    * @param hostname the MySQL or PostgreSQL server hostname
    * @param database the name of the database
    * @param username the user name used to login to the database
    * @param password the user login password
    * @throws java.sql.SQLException when failed to make a connection.
    */
   public ProfNetwork (String dbname, String dbport, String user, String passwd) throws SQLException {

      System.out.print("Connecting to database...");
      try{
         // constructs the connection URL
         String url = "jdbc:postgresql://localhost:" + dbport + "/" + dbname;
         System.out.println ("Connection URL: " + url + "\n");

         // obtain a physical connection
         this._connection = DriverManager.getConnection(url, user, passwd);
         System.out.println("Done");
      }catch (Exception e){
         System.err.println("Error - Unable to Connect to Database: " + e.getMessage() );
         System.out.println("Make sure you started postgres on this machine");
         System.exit(-1);
      }//end catch
   }//end ProfNetwork

   /**
    * Method to execute an update SQL statement.  Update SQL instructions
    * includes CREATE, INSERT, UPDATE, DELETE, and DROP.
    *
    * @param sql the input SQL string
    * @throws java.sql.SQLException when update failed
    */
   public void executeUpdate (String sql) throws SQLException {
      // creates a statement object
      Statement stmt = this._connection.createStatement ();

      // issues the update instruction
      stmt.executeUpdate (sql);

      // close the instruction
      stmt.close ();
   }//end executeUpdate

   /**
    * Method to execute an input query SQL instruction (i.e. SELECT).  This
    * method issues the query to the DBMS and outputs the results to
    * standard out.
    *
    * @param query the input query string
    * @return the number of rows returned
    * @throws java.sql.SQLException when failed to execute the query
    */
   public int executeQueryAndPrintResult (String query) throws SQLException {
      // creates a statement object
      Statement stmt = this._connection.createStatement ();

      // issues the query instruction
      ResultSet rs = stmt.executeQuery (query);

      /*
       ** obtains the metadata object for the returned result set.  The metadata
       ** contains row and column info.
       */
      ResultSetMetaData rsmd = rs.getMetaData ();
      int numCol = rsmd.getColumnCount ();
      int rowCount = 0;

      // iterates through the result set and output them to standard out.
      boolean outputHeader = true;
      while (rs.next()){
	 if(outputHeader){
	    for(int i = 1; i <= numCol; i++){
		System.out.print(rsmd.getColumnName(i) + "\t");
	    }
	    System.out.println();
	    outputHeader = false;
	 }
         for (int i=1; i<=numCol; ++i)
            System.out.print (rs.getString (i) + "\t");
         System.out.println ();
         ++rowCount;
      }//end while
      stmt.close ();
      return rowCount;
   }//end executeQuery

   /**
    * Method to execute an input query SQL instruction (i.e. SELECT).  This
    * method issues the query to the DBMS and returns the results as
    * a list of records. Each record in turn is a list of attribute values
    *
    * @param query the input query string
    * @return the query result as a list of records
    * @throws java.sql.SQLException when failed to execute the query
    */
   public List<List<String>> executeQueryAndReturnResult (String query) throws SQLException {
      // creates a statement object
      Statement stmt = this._connection.createStatement ();

      // issues the query instruction
      ResultSet rs = stmt.executeQuery (query);

      /*
       ** obtains the metadata object for the returned result set.  The metadata
       ** contains row and column info.
       */
      ResultSetMetaData rsmd = rs.getMetaData ();
      int numCol = rsmd.getColumnCount ();
      int rowCount = 0;

      // iterates through the result set and saves the data returned by the query.
      boolean outputHeader = false;
      List<List<String>> result  = new ArrayList<List<String>>();
      while (rs.next()){
          List<String> record = new ArrayList<String>();
         for (int i=1; i<=numCol; ++i)
            record.add(rs.getString (i));
         result.add(record);
      }//end while
      stmt.close ();
      return result;
   }//end executeQueryAndReturnResult

   /**
    * Method to execute an input query SQL instruction (i.e. SELECT).  This
    * method issues the query to the DBMS and returns the number of results
    *
    * @param query the input query string
    * @return the number of rows returned
    * @throws java.sql.SQLException when failed to execute the query
    */
   public int executeQuery (String query) throws SQLException {
       // creates a statement object
       Statement stmt = this._connection.createStatement ();

       // issues the query instruction
       ResultSet rs = stmt.executeQuery (query);

       int rowCount = 0;

       // iterates through the result set and count nuber of results.
       while(rs.next()){
          rowCount++;
       }//end while
       stmt.close ();
       return rowCount;
   }

   /**
    * Method to fetch the last value from sequence. This
    * method issues the query to the DBMS and returns the current
    * value of sequence used for autogenerated keys
    *
    * @param sequence name of the DB sequence
    * @return current value of a sequence
    * @throws java.sql.SQLException when failed to execute the query
    */
   public int getCurrSeqVal(String sequence) throws SQLException {
	Statement stmt = this._connection.createStatement ();

	ResultSet rs = stmt.executeQuery (String.format("Select currval('%s')", sequence));
	if (rs.next())
		return rs.getInt(1);
	return -1;
   }

   /**
    * Method to close the physical connection if it is open.
    */
   public void cleanup(){
      try{
         if (this._connection != null){
            this._connection.close ();
         }//end if
      }catch (SQLException e){
         // ignored.
      }//end try
   }//end cleanup

   /**
    * The main execution method
    *
    * @param args the command line arguments this inclues the <mysql|pgsql> <login file>
    */
   public static void main (String[] args) {
      if (args.length != 3) {
         System.err.println (
            "Usage: " +
            "java [-classpath <classpath>] " +
            ProfNetwork.class.getName () +
            " <dbname> <port> <user>");
         return;
      }//end if

      Greeting();
      ProfNetwork esql = null;
      try{
         // use postgres JDBC driver.
         Class.forName ("org.postgresql.Driver").newInstance ();
         // instantiate the ProfNetwork object and creates a physical
         // connection.
         String dbname = args[0];
         String dbport = args[1];
         String user = args[2];
         esql = new ProfNetwork (dbname, dbport, user, "");

         boolean keepon = true;
         while(keepon) {
            // These are sample SQL statements
            System.out.println("\nMAIN MENU");
            System.out.println("---------");
            System.out.println("1. Create user");
            System.out.println("2. Log in");
            System.out.println("9. < EXIT");
            String authorisedUser = null;
            switch (readChoice()){
               case 1: CreateUser(esql); break;
               case 2: authorisedUser = LogIn(esql); break;
               case 9: keepon = false; break;
               default : System.out.println("Unrecognized choice!"); break;
            }//end switch
            if (authorisedUser != null) {
              boolean usermenu = true;
              while(usermenu) {
                System.out.println("\nMAIN MENU");
                System.out.println("---------");
                System.out.println("1. Compose message");
		        System.out.println("2. View Message(s)");
		        System.out.println("3. Change Password");
		        System.out.println("4. Search People");
                System.out.println("5. Send Connection Request");
                System.out.println("6. View Connection Request(s)");
                System.out.println("7. Change Request Status");
                System.out.println(".........................");
                System.out.println("9. Log out");
                switch (readChoice()){
                   case 1: NewMessage(esql, authorisedUser); break;
                   case 2: ViewMessage(esql, authorisedUser); break;
                   case 3: ChangePassword(esql, authorisedUser); break;
                   case 4: SearchPeople(esql); break;
		           case 5: NewConnection(esql); break;
		           case 6: ViewConnectionRequest(esql, authorisedUser); break;
		           case 7: ChangeConnection(esql); break;
                   case 9: usermenu = false; break;
                   default : System.out.println("Unrecognized choice!"); break;
                }
              }
            }
         }//end while
      }catch(Exception e) {
         System.err.println (e.getMessage ());
      }finally{
         // make sure to cleanup the created table and close the connection.
         try{
            if(esql != null) {
               System.out.print("Disconnecting from database...");
               esql.cleanup ();
               System.out.println("Done\n\nBye !");
            }//end if
         }catch (Exception e) {
            // ignored.
         }//end try
      }//end try
   }//end main

   public static void Greeting(){
      System.out.println(
         "\n\n*******************************************************\n" +
         "              User Interface      	               \n" +
         "*******************************************************\n");
   }//end Greeting

   /*
    * Reads the users choice given from the keyboard
    * @int
    **/
   public static int readChoice() {
      int input;
      // returns only if a correct value is given.
      do {
         System.out.print("Please make your choice: ");
         try { // read the integer, parse it and break.
            input = Integer.parseInt(in.readLine());
            break;
         }catch (Exception e) {
            System.out.println("Your input is invalid!");
            continue;
         }//end try
      }while (true);
      return input;
   }//end readChoice

   /*
    * Creates a new user with privided login, passowrd and phoneNum
    * An empty block and contact list would be generated and associated with a user
    **/
   public static void CreateUser(ProfNetwork esql){
      try{
         System.out.print("\tEnter user login: ");
         String login = in.readLine();
         System.out.print("\tEnter user password: ");
         String password = in.readLine();
         System.out.print("\tEnter user email: ");
         String email = in.readLine();

	     String query = String.format("INSERT INTO USR (userId, password, email) VALUES ('%s','%s','%s')", login, password, email);
         esql.executeUpdate(query);
         System.out.println ("User successfully created!");
      }catch(Exception e){
         System.err.println (e.getMessage ());
      }
   }//end

   /*
    * Check log in credentials for an existing user
    * @return User login or null is the user does not exist
    **/
   public static String LogIn(ProfNetwork esql){
      try{
         System.out.print("\tEnter user login: ");
         String login = in.readLine();
         System.out.print("\tEnter user password: ");
         String password = in.readLine();

         String query = String.format("SELECT * FROM USR WHERE userId = '%s' AND password = '%s'", login, password);
         int userNum = esql.executeQuery(query);
	 if (userNum > 0)
		return login;
         return null;
      }catch(Exception e){
         System.err.println (e.getMessage ());
         return null;
      }
   }//end

// Rest of the functions definition go in here
  public static void NewConnection(ProfNetwork esqL){

  }

  public static void ViewConnectionRequest(ProfNetwork esqL, String usr){
      try{
         System.out.println("line 386");
         // String query = String.format("SELECT userId, status FROM CONNECTION_USR WHERE connectionId = '%s' AND status != 'Accept'", usr);
         String query = String.format("SELECT * FROM CONNECTION_USR");
         esqL.executeQueryAndPrintResult(query);
         System.out.println("line 389");
		  
      }catch(Exception e){
         System.err.println (e.getMessage ());
      }
  }

  public static void ChangeConnection(ProfNetwork esqL){

  }
  public static void NewMessage(ProfNetwork esqL, String usr){
	try{
         System.out.println("\tEnter user: ");
         String recieveruser = in.readLine();
	
	String q = String.format("SELECT * FROM USR WHERE userid = '%s'", recieveruser);
	int results = esqL.executeQuery(q);

	if (results > 0){
	  System.out.println("\tEnter message: ");
	  String msg = in.readLine();

	  String q1 = String.format("SELECT * FROM Message");
	  //esqL.executeQueryAndPrintResult(q1);
	  int msgid = esqL.executeQuery(q1) + 1;
	  System.out.println(msgid);
	  String msgidstring = String.valueOf(msgid);
	  //TODO Get curret time
	  String sendTime = "10/9/2011 9:49:00 PM";
	  String deleteStatus = "0"; 
	  String status = "Delivered";
	  String q2 = String.format("INSERT INTO Message(msgId, senderId, receiverId, contents,sendTime, deleteStatus, status) VALUES (%s, '%s', '%s', '%s', '%s', %s, '%s')", msgidstring, usr, recieveruser, msg, sendTime, deleteStatus, status);

	  esqL.executeUpdate(q2);

	System.out.println("Message Sent!");

	String test = String.format("SELECT * FROM Message WHERE msgId = %s", msgidstring);
	esqL.executeQueryAndPrintResult(test);
	}
	else{

	  System.out.println("User not found");
	}      
	}catch(Exception e){
         System.err.println (e.getMessage ());
      }

  }
  
  public static void SendRequest(ProfNetwork esqL){

  }

  public static void ChangePassword(ProfNetwork esql, String usr){
	try{
         System.out.println("\tEnter new password: ");
         String newpass = in.readLine();

	 //Creating empty contact\block lists for a user
	 //String query = String.format("INSERT INTO USR (userId, password, email, contact_list) VALUES ('%s','%s','%s')", login, password, email);
	 String query = String.format("UPDATE USR SET password = '%s' where userId = '%s' ", newpass, usr);


         esql.executeUpdate(query);
         System.out.println ("Password Changed Successfully");
      }catch(Exception e){
         System.err.println (e.getMessage ());
      }

  }
  
  public static void SearchPeople(ProfNetwork esql){
	try{
         System.out.print("\tEnter user to search: ");
         String usr = in.readLine();

	    String query = String.format("SELECT name, userId, email FROM USR WHERE name = '%s' ", usr);

         esql.executeQueryAndPrintResult(query);
      }catch(Exception e){
         System.err.println (e.getMessage ());
      }
  }

  public static void ViewMessage(ProfNetwork esql, String usr){
	try{
        while(true){
            System.out.println("\nMESSAGE MENU");
            System.out.println("---------");
            System.out.println("1. View Sent Message(s)");
            System.out.println("2. View Recieved Message(s)");
            System.out.println("3. Read Message");
            System.out.println("4. Delete Message");
            System.out.println("9. Exit");
            System.out.println("---------");
            System.out.print("Enter option: ");
            String option = in.readLine();

            if(option.equals("1")){
                String query = String.format("SELECT msgId, receiverId FROM Message WHERE senderId = '%s' AND deleteStatus != 1 AND deleteStatus != 3", usr);
                esql.executeQueryAndPrintResult(query);
            }
            else if(option.equals("2")){
                String query = String.format("SELECT msgId, senderId FROM Message WHERE receiverId = '%s' AND deleteStatus != 2 AND deleteStatus != 3" , usr);
                esql.executeQueryAndPrintResult(query);
            }
            else if(option.equals("3")){
                System.out.print("\tEnter message ID: ");
                String msgid = in.readLine();
                
                String senderquery = String.format("SELECT * FROM Message WHERE msgId = %s AND senderId = '%s' AND deleteStatus != 1 AND deleteStatus != 3", msgid, usr);
                int sendermsg = esql.executeQuery(senderquery);
            
                String receiverquery = String.format("SELECT * FROM Message WHERE msgId = %s AND receiverId = '%s' AND deleteStatus != 2 AND deleteStatus != 3", msgid, usr);
                int receivermsg = esql.executeQuery(receiverquery);
                
                if(sendermsg > 0){
                    String query = String.format("SELECT msgId, senderId, contents FROM Message WHERE msgId = %s AND senderId = '%s' " , msgid, usr);
                    esql.executeQueryAndPrintResult(query);
                    
                    query = String.format("UPDATE Message SET status = 'Read' where msgId = '%s' ", msgid);
                    esql.executeUpdate(query);
                }
                else if(receivermsg > 0){
                    String query = String.format("SELECT msgId, senderId, contents FROM Message WHERE msgId = %s AND receiverId = '%s' " , msgid, usr);
                    esql.executeQueryAndPrintResult(query);
                    
                    query = String.format("UPDATE Message SET status = 'Read' where msgId = '%s' ", msgid);
                    esql.executeUpdate(query);
                }
                else{
                    System.out.println("Invliad Message ID");
                }
    
            }
            else if(option.equals("4")){
                System.out.print("\tEnter message ID: ");
                String msgid = in.readLine();

                String senderquery = String.format("SELECT * FROM Message WHERE msgId = %s AND senderId = '%s'", msgid, usr);
                int sendermsg = esql.executeQuery(senderquery);
            
                String receiverquery = String.format("SELECT * FROM Message WHERE msgId = %s AND receiverId = '%s'", msgid, usr);
                int receivermsg = esql.executeQuery(receiverquery);

                if(sendermsg > 0){
                    //get current del status
                    String query = String.format("SELECT deleteStatus FROM Message where msgId = %s", msgid);
                    List<List<String>> res = esql.executeQueryAndReturnResult(query);
                    int currDelStatus = Integer.valueOf(res.get(0).get(0));
                   
                    //get new del status
                    int newDelStatus = (currDelStatus == 0) ? 1 : 3;

                    //update del status
                    query = String.format("UPDATE Message SET deleteStatus = %s where msgId = '%s' ", newDelStatus, msgid);
                    esql.executeUpdate(query);

                    System.out.println("Message Deleted!");
                }
                else if(receivermsg > 0){
                    //get current del status
                    String query = String.format("SELECT deleteStatus FROM Message where msgId = %s", msgid);
                    List<List<String>> res = esql.executeQueryAndReturnResult(query);
                    int currDelStatus = Integer.valueOf(res.get(0).get(0));
                   
                    //get new del status
                    int newDelStatus = (currDelStatus == 0) ? 2 : 3;

                    //update del status
                    query = String.format("UPDATE Message SET deleteStatus = %s where msgId = '%s' ", newDelStatus, msgid);
                    esql.executeUpdate(query);

                    System.out.println("Message Deleted!");
                }
                else{
                    System.out.println("Invliad Message ID");
                }
            }
            else if(option.equals("9")){
                System.out.println("Exiting Message Menu\n");
                break;
            }
            else{
                System.out.println("Invalid Option");
            }
        }
	}catch(Exception e){
          System.err.println (e.getMessage ());
	}
  }

}//end ProfNetwork
