//Who worked on this part : Ahmad , Mohammad
//Roles : Ahmad-> Function connecting to the MySQL database and extacting data + database init/values + Socket programming to connect the server/client  
//      : Mohammad -> Socket programming to connect the server/client  +Function connecting to the MySQL database and extacting data + database init/values    
//      :


//Used Libraries 
//Most of them have been imported through examples

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.ZoneId;

//Primary class Main_Server
public class Main_Server { 
	
//final Variables associated to mySQL connections
static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";  
static final String DB_URL = "jdbc:mysql://localhost/library?useSSL=false";
static final String USER = "root";
static final String PASS = "passwordadmin";


 //Creating a serverSocket for the connection
private static ServerSocket welcomeSocket;	


//Init the mySQL driver
public Main_Server() throws ClassNotFoundException
{
	Class.forName("com.mysql.jdbc.Driver");
}

//function acceptConnections does most of the work associated with multithreading / socket prog
public void acceptConnections() {

	try {
		  
		  welcomeSocket = new ServerSocket(3678); 
		  
		     }
		     catch (IOException e) {
		       System.err.println("ServerSocket instantiation failure");
		       e.printStackTrace();
		       System.exit(0);
		     }
    
    
    while(true) { 
  	  
  	  try {
  		  Socket newConnection = welcomeSocket.accept(); 
  		  ServerThread st = new ServerThread(newConnection);
  		  new Thread(st).start();
  	  }
  	  catch (IOException ioe) {
  	        System.err.println("server accept failed");
  	      }
}
}




  public static void main(String argv[]) throws Exception 
    { 
	  
	   Main_Server server = null;
	   try {
	       //Instantiate an object of this class. This will load the JDBC database driver
	      server = new Main_Server();
	   }
	    catch (ClassNotFoundException e) {
	      System.out.println("unable to load JDBC driver");
	      e.printStackTrace();
	      System.exit(1);
	    }
	   
	   server.acceptConnections();
    }
  
  // Socket programming & MYSQL datafetch
	  
  class ServerThread implements Runnable {

	  private Socket connectionSocket;
	  private ArrayList<String> results = new ArrayList<String>() ;
	   
	  private void queryAltServer(String[] x) throws IOException, ClassNotFoundException
	  {
	  	
	  	try
	  	{
	  		Socket socket1 = new Socket("localhost",3680);
	  		ObjectOutputStream outToServer = new ObjectOutputStream(socket1.getOutputStream());
	  		ObjectInputStream inFromServer = new ObjectInputStream(socket1.getInputStream());
	  		
	  		outToServer.writeObject(x);
	  		outToServer.flush();
	  	
	  		results = ((ArrayList<String>)inFromServer.readObject());
	  		
	  		

	  	}
	  	finally
	  	{
	  		
	  	}
	  }  
	  
	    public ServerThread(Socket socket) {
	    //Inside the constructor: store the passed object in the data member  
	      this.connectionSocket = socket;
	    }

	 //This is where you place the code you want to run in a thread
	 //Every instance of a ServerThread will handle one client (TCP connection)
	    public void run() {
	    	
	    	 String clientSentence; 
	         String mySentence; 
	         
	         Connection conn = null;
	   	  Statement stmt = null;
	         
	   	  
	       	  
	             
	              
	              //Initializing input / output streams

	   	ObjectOutputStream outToClient = null;
				try {
					outToClient = new ObjectOutputStream(connectionSocket.getOutputStream());
				} catch (IOException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				} 

        // 
	             ObjectInputStream inFromClient = null;
				try {
					inFromClient = new ObjectInputStream(connectionSocket.getInputStream());
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} 
        
          //Read the sentence sent by the user
	            String[] userQuery = new String[3];
	            String[] outQuery = new String[3];
	            String outString = null;
	            try {
					userQuery = (String[]) inFromClient.readObject();
				} catch (ClassNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
	            
	            // START OF  QUERIES
	            
	            
	            if (userQuery[0].startsWith("idPass"))
	            {
	            	
	            	
	            	try {
						conn = DriverManager.getConnection(DB_URL,USER,PASS);
						  stmt = conn.createStatement();
			       	      String sql="";
			       	      System.out.println(sql);
			       	      sql="SELECT username FROM users WHERE username='"+userQuery[1]+"'";
			       	      
			       	      ResultSet rs = stmt.executeQuery(sql);
			       	      
			       	      if (rs.next())
			       	      {
			       	    	outString="dup";
			       	      }
			       	      else
			       	      {
			       	    	outString="ok";
			       	    	sql="INSERT INTO users (id, username, pass)" +"VALUES (?, ?, ?)";
			       	    	
			       	    	PreparedStatement preparedStatement = conn.prepareStatement(sql);
			       	    	preparedStatement.setInt(1, 0);
			       	    	preparedStatement.setString(2, userQuery[1]);
			       	    	preparedStatement.setString(3, userQuery[2]);
			       	    	preparedStatement.executeUpdate(); 
			       	    	
			       	    	
			       	    	System.out.println(sql);
			       	    	
			       	      }
			       	      
			       	      rs.close();
			       	      stmt.close();
			       	      conn.close();
						
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		       	    
	            	 try {
	 					outToClient.writeObject(outString);
	 				} catch (IOException e) {
	 					// TODO Auto-generated catch block
	 					e.printStackTrace();
	 				} 

	            }
	            
	            if (userQuery[0].startsWith("usLogon"))
	            {
	            	 // Initializing the connection the database and fetching the requested data 
	            	try {
						conn = DriverManager.getConnection(DB_URL,USER,PASS);
						  stmt = conn.createStatement();
			       	      String sql="";
			       	      sql="SELECT pass FROM users WHERE username='"+userQuery[1]+"'";
			       	      
			       	      ResultSet rs = stmt.executeQuery(sql);
			       	      String tempPass= null;
			       	      if (rs.next())
			       	      {
			       	    	  
			       	    	
			       	    		
				       	    	
				       	    	
				       	    	tempPass = rs.getString("pass");
				       	    	System.out.println(tempPass);
				       	    	System.out.println(userQuery[2]);
				       	    	
				       	    	
			       	    	
			       	    	
			       	    	if (tempPass.startsWith(userQuery[2]))
			       	    	{
			       	    		outString="ok";
			       	    	}
			       	    	
			       	    	else
			       	    	{
			       	    		outString="nop";
			       	    		
			       	    	}
			       	      }
			       	      else
			       	      {
			       	    	outString="nop";
			       	      }
			       	      
			       	      rs.close();
			       	      stmt.close();
			       	      conn.close();
						
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		       	    
	            	 try {
	 					outToClient.writeObject(outString);
	 				} catch (IOException e) {
	 					// TODO Auto-generated catch block
	 					e.printStackTrace();
	 				} 

	            }
	            
	            if (userQuery[0].startsWith("genre"))
	            {
	            	
	            	
	            	// Initializing the connection the database and fetching the requested data 
	            	try {
						conn = DriverManager.getConnection(DB_URL,USER,PASS);
						  stmt = conn.createStatement();
			       	      String sql="";
			       	   sql="SELECT DISTINCT genre FROM books ORDER BY genre";
			       	      
			       	      //SELECT DISTINCT name FROM info WHERE status = 1 ORDER BY id
			       	 ResultSet rs = stmt.executeQuery(sql);
			       	 
			       	 results.clear();
			       	 
			       	while(rs.next()){
		       	          
		       	    	  
		       	    	
		       	          //Concatinating the data into a single string
		       	          String title = rs.getString("genre");
		   
		       	          
	                  //storing the data into the array of string myList
		       	          results.add(title);
		       	          
		       	          
		       	      
		       		}
			       	rs.close();
		       	      stmt.close();
		       	      conn.close();
		       	      
		       		}catch(SQLException se){
		       		      
		       		      se.printStackTrace();
		       		   }catch(Exception e){
		       		      
		       		      e.printStackTrace();
		       		   }finally{
		       		      
		       		      try{
		       		         if(stmt!=null)
		       		            stmt.close();
		       		      }catch(SQLException se2){
		       		      }
		       		      try{
		       		         if(conn!=null)
		       		            conn.close();
		       		      }catch(SQLException se){
		       		         se.printStackTrace();
		       		      }
		       		   }
	            	
	            	 try {
	 					outToClient.writeObject(results);
	 				} catch (IOException e) {
	 					// TODO Auto-generated catch block
	 					e.printStackTrace();
	 				} 
	            }
	            
	            if (userQuery[0].startsWith("getBooks"))
	            {
	            	
	            	 // Initializing the connection the database and fetching the requested data 
	            	try {
						conn = DriverManager.getConnection(DB_URL,USER,PASS);
						  stmt = conn.createStatement();
			       	      String sql="";
			       	      System.out.println(sql);
			       	      sql="SELECT title,date_of_pub FROM books WHERE genre='"+userQuery[1]+"'";
			       	      
			       	      ResultSet rs = stmt.executeQuery(sql);
			       	      results.clear();
			       	      
			       	      outQuery[0]="getBooks";
		       	    	  outQuery[1]=userQuery[1];
		       	    	  
		       	    	try {
							queryAltServer(outQuery);
						} catch (ClassNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
			       	      
			       	   while(rs.next()){
			       	          
			       	    	  String temp;
			       	    	
			       	          //Concatinating the data into a single string
			       	          String title = rs.getString("title");
			       	          int date = rs.getInt("date_of_pub");
			       	          temp=(title+" ; "+date);
		                  //storing the data into the array of string myList
			       	          results.add(temp);
			       	          
			       	          
			       	      
			       		}
			       	      
			       	      rs.close();
			       	      stmt.close();
			       	      conn.close();
						
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		       	    
	            	 try {
	 					outToClient.writeObject(results);
	 				} catch (IOException e) {
	 					// TODO Auto-generated catch block
	 					e.printStackTrace();
	 				} 

	            }
	            
	            if (userQuery[0].startsWith("searchBook"))
	            {
	            	results.clear();
	            	 // Initializing the connection the database and fetching the requested data 
	            	try {
						conn = DriverManager.getConnection(DB_URL,USER,PASS);
						  stmt = conn.createStatement();
			       	      String sql="";
			       	      System.out.println(sql);
			       	      sql="SELECT title FROM books";
			       	      
			       	      ResultSet rs = stmt.executeQuery(sql);
			       	      
			       	      System.out.println(userQuery[1]);
			       	      
			       	      
			       	      
			       	      outQuery[0]="searchBook";
		       	    	  outQuery[1]=userQuery[1];
		       	    	  
		       	    	try {
							queryAltServer(outQuery);
						} catch (ClassNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
			       	      
			       	      while (rs.next())
			       	      {
			       	    	  String tempTitle = rs.getString("title");
			       	    	if(tempTitle.toLowerCase().contains(userQuery[1].toLowerCase()))
			       	    	{
			       	    		results.add(tempTitle);
			       	    	}
			       	      }
			       	      
			       	    
			       	 
			       	      
			       	      rs.close();
			       	      stmt.close();
			       	      conn.close();
						
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		       	    
	            	 try {
	 					outToClient.writeObject(results);
	 				} catch (IOException e) {
	 					// TODO Auto-generated catch block
	 					e.printStackTrace();
	 				} 

	            }
	            
	            
	            if (userQuery[0].startsWith("getBookInfo"))
	            {
	            	 // Initializing the connection the database and fetching the requested data 
	            	try {
						conn = DriverManager.getConnection(DB_URL,USER,PASS);
						  stmt = conn.createStatement();
			       	      String sql="";
			       	 
			       	      sql="SELECT * FROM Reserved_Books";
			       	      outString="Available";
			       	      ResultSet rs = stmt.executeQuery(sql);
			       	      while (rs.next())
			       	      {
			       	    	String tempPass = rs.getString("title");
			       	    	System.out.println(tempPass);
			       	    	if(tempPass.toLowerCase().startsWith(userQuery[1].toLowerCase()))
			       	    	{
			       	    		outString="Reserved";
			       	    	}
			       	    	
			       	      }
			       	      
			       	      
			       	      rs.close();
			       	      stmt.close();
			       	      conn.close();
						
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		       	    
	            	 try {
	 					outToClient.writeObject(outString);
	 				} catch (IOException e) {
	 					// TODO Auto-generated catch block
	 					e.printStackTrace();
	 				} 

	            }
	          
	           
	            
	            
	           
	              
	            if (userQuery[0].startsWith("resBook"))
	            {
	            	 // Initializing the connection the database and fetching the requested data 
	            	try {
						conn = DriverManager.getConnection(DB_URL,USER,PASS);
						  stmt = conn.createStatement();
						  String sql="";
						 
						  sql="SELECT id FROM users WHERE username='"+userQuery[2]+"'";
						  System.out.println(userQuery[2]);
			       	      
			       	      ResultSet rs = stmt.executeQuery(sql);
			       	      rs.next();
						  int tempId=rs.getInt("id");
						  System.out.println(tempId);
						  
						  
						  
						  rs.close();
			       	      
			       	     
			       	      sql="INSERT INTO Reserved_Books (idRes, title, Mtimestamp1)" +"VALUES (?, ?, ?)";
		       	    	
			       	      PreparedStatement preparedStatement = conn.prepareStatement(sql);
			       	      preparedStatement.setInt(1, tempId);
			       	      preparedStatement.setString(2, userQuery[1]);
			       	      Timestamp ts = Timestamp.valueOf(LocalDateTime.now(ZoneId.of("UTC+3")));
			       	      preparedStatement.setTimestamp(3, ts);
		       	    	  preparedStatement.executeUpdate(); 
		       	    	  
		       	    	outString = "Reservation Successful!";
		       	    	  
		       	    	sql="INSERT INTO user_History (idRes, title, QueryType, Mtimestamp)" +"VALUES (?, ?,?, ?)";
		       	    	
			       	      PreparedStatement preparedStatementHistory = conn.prepareStatement(sql);
			       	      preparedStatementHistory.setInt(1, tempId);
			       	   		preparedStatementHistory.setString(2, userQuery[1]);
			       	   	preparedStatementHistory.setString(3, "Reservation");
			       	      Timestamp ts1 = Timestamp.valueOf(LocalDateTime.now(ZoneId.of("UTC+3")));
			       	   preparedStatementHistory.setTimestamp(4, ts);
			       	preparedStatementHistory.executeUpdate(); 
			       	
			       	      
		       	    	stmt.close();
			       	     conn.close();
			       	     
						
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		       	    
	            	 try {
	 					outToClient.writeObject(outString);
	 				} catch (IOException e) {
	 					// TODO Auto-generated catch block
	 					e.printStackTrace();
	 				} 

	            }
	             
	            
	            
	            if (userQuery[0].startsWith("reqBook"))
	            {
	            	System.out.println("ENTER");
	            	 // Initializing the connection the database and fetching the requested data 
	            	try {
						conn = DriverManager.getConnection(DB_URL,USER,PASS);
						  stmt = conn.createStatement();
						  String sql="";
						 
						  sql="SELECT id FROM users WHERE username='"+userQuery[2]+"'";
						  System.out.println(userQuery[2]);
			       	      
			       	      ResultSet rs = stmt.executeQuery(sql);
			       	      rs.next();
						  int tempId=rs.getInt("id");
						  System.out.println(tempId);
						  rs.close();
						  
						  sql="SELECT * FROM reserved_books WHERE title='"+userQuery[1]+"'";
						  ResultSet xQuery = stmt.executeQuery(sql);
						  xQuery.next();
						  int idTemp = xQuery.getInt("idRes");
						  
						  sql="SELECT * FROM requested_books WHERE title='"+userQuery[1]+"' AND idRes='"+tempId+"'";
						  ResultSet xQuery1 = stmt.executeQuery(sql);
						  
			
						  if(idTemp==tempId)
						  {
							  outString = "You already reserved this book!"; 
						  }
						  
						  else if (xQuery1.next())
						  {
							  
								  outString = "You already requested this book!";
							 
							
							  
						  }
						  else
						  {
							  sql="INSERT INTO Requested_Books (idRes, title, Mtimestamp)" +"VALUES (?, ?, ?)";
				       	    	
				       	      PreparedStatement preparedStatement = conn.prepareStatement(sql);
				       	      preparedStatement.setInt(1, tempId);
				       	      preparedStatement.setString(2, userQuery[1]);
				       	      Timestamp ts = Timestamp.valueOf(LocalDateTime.now(ZoneId.of("UTC+3")));
				       	      preparedStatement.setTimestamp(3, ts);
			       	    	  preparedStatement.executeUpdate(); 
			       	    	  
			       	    	sql="INSERT INTO user_History (idRes, title,QueryType, Mtimestamp)" +"VALUES (?, ? ,?, ?)";
			       	    	
				       	      PreparedStatement preparedStatementHistory = conn.prepareStatement(sql);
				       	      preparedStatementHistory.setInt(1, tempId);
				       	   		preparedStatementHistory.setString(2, userQuery[1]);
				       	   	preparedStatementHistory.setString(3, "Request");
				       	      Timestamp ts1 = Timestamp.valueOf(LocalDateTime.now(ZoneId.of("UTC+3")));
				       	   preparedStatementHistory.setTimestamp(4, ts);
				       	preparedStatementHistory.executeUpdate(); 
				       	
			       	    	outString = "Request Successful!";
						  }
						  
						 
			       	      
			       	     
			       	      
						  xQuery1.close();
						  xQuery.close();
						  stmt.close();
			       	     conn.close();
			       	     
						
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		       	    
	            	 try {
	 					outToClient.writeObject(outString);
	 				} catch (IOException e) {
	 					// TODO Auto-generated catch block
	 					e.printStackTrace();
	 				} 

	            }
	            
	            
	            
	            if (userQuery[0].startsWith("getResReq"))
	            {
	            	
	            	 // Initializing the connection the database and fetching the requested data 
	            	try {
						conn = DriverManager.getConnection(DB_URL,USER,PASS);
						  stmt = conn.createStatement();
			       	      String sql="";
			       	      sql="SELECT id FROM users WHERE username='"+userQuery[2]+"'";
 
			       	      ResultSet rs = stmt.executeQuery(sql);
			       	      rs.next();
						  int tempId=rs.getInt("id");
						  System.out.println(tempId);
						  rs.close();
						  
			       	      System.out.println(sql);
			       	      if (userQuery[1].startsWith("getRes"))
			       	      {
			       	    	sql="SELECT title FROM reserved_books WHERE idRes='"+tempId+"'";
			       	      }
			       	      else
			       	      {
			       	    	sql="SELECT title FROM requested_books WHERE idRes='"+tempId+"'";
			       	      }
			       	      
			       	      ResultSet rsx = stmt.executeQuery(sql);
			       	      results.clear();

			       	      
			       	   while(rsx.next()){
			       	          
			       	    	
			       	          //Concatinating the data into a single string
			       	          String title = rsx.getString("title");
			       
		                  //storing the data into the array of string myList
			       	          results.add(title);
			       	          
			       	          
			       	      
			       		}
			       	      
			       	      rsx.close();
			       	      stmt.close();
			       	      conn.close();
						
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		       	    
	            	 try {
	 					outToClient.writeObject(results);
	 				} catch (IOException e) {
	 					// TODO Auto-generated catch block
	 					e.printStackTrace();
	 				} 

	            }
	             
	            
	            
	            if (userQuery[0].startsWith("relReq"))
	            {
	            	results.clear();
	            	 // Initializing the connection the database and fetching the requested data 
	            	try {
						conn = DriverManager.getConnection(DB_URL,USER,PASS);
						  stmt = conn.createStatement();
			       	      String sql="";
			       	      
			       	      sql="SELECT id FROM users WHERE username='"+userQuery[2]+"'";
			       	 
			       	      ResultSet rs = stmt.executeQuery(sql);
			       	      rs.next();
						  int tempId=rs.getInt("id");
						  System.out.println(tempId);
						  rs.close();
			       	      
			       	      
			       	      
			       	      sql="delete from requested_books where idres = ? and title = ?";
			       	      
			       	   PreparedStatement preparedStmt = conn.prepareStatement(sql);
			           preparedStmt.setInt(1, tempId);
			           preparedStmt.setString(2, userQuery[1]);
			           // execute the preparedstatement
			           preparedStmt.execute();
			           
			           sql="INSERT INTO user_History (idRes, title, QueryType, Mtimestamp)" +"VALUES (?, ?,?, ?)";
		       	    	
			       	      PreparedStatement preparedStatementHistory1 = conn.prepareStatement(sql);
			       	   preparedStatementHistory1.setInt(1, tempId);
			       	preparedStatementHistory1.setString(2, userQuery[1]);
			       	preparedStatementHistory1.setString(3, "Cancel Request");
			       	      Timestamp ts1 = Timestamp.valueOf(LocalDateTime.now(ZoneId.of("UTC+3")));
			       	   preparedStatementHistory1.setTimestamp(4, ts1);
			       	preparedStatementHistory1.executeUpdate(); 
			       	      

			           sql="SELECT title FROM requested_books WHERE idRes='"+tempId+"'";
		       	     
		       	      ResultSet rsx = stmt.executeQuery(sql);
		       	      results.clear();

		       	      
		       	   while(rsx.next()){
		       	          
		       	    	
		       	          //Concatinating the data into a single string
		       	          String title = rsx.getString("title");
		       
	                  //storing the data into the array of string myList
		       	          results.add(title);
		       	          
		       	          
		       	      
		       		}
		       	      
		       	      rsx.close();
		       	      stmt.close();
		       	      conn.close();
			       	      
			       	   
			       	      
			       	    
						
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		       	    
	            	 try {
	 					outToClient.writeObject(results);
	 				} catch (IOException e) {
	 					// TODO Auto-generated catch block
	 					e.printStackTrace();
	 				} 

	            }
	            
	            if (userQuery[0].startsWith("relRes"))
	            {
	            	results.clear();
	            	 // Initializing the connection the database and fetching the requested data 
	            	try {
						conn = DriverManager.getConnection(DB_URL,USER,PASS);
						  stmt = conn.createStatement();
			       	      String sql="";
			       	      
			       	      sql="SELECT id FROM users WHERE username='"+userQuery[2]+"'";
			       	 
			       	      ResultSet rs = stmt.executeQuery(sql);
			       	      rs.next();
						  int tempId=rs.getInt("id");
						  System.out.println(tempId);
						  rs.close();
			       	      
			       	      
			       	      
			       	      sql="delete from reserved_books where idRes = ? and title = ?";
			       	      
			       	   PreparedStatement preparedStmt = conn.prepareStatement(sql);
			           preparedStmt.setInt(1, tempId);
			           preparedStmt.setString(2, userQuery[1]);
			           // execute the preparedstatement
			           preparedStmt.execute();
			       	      
			           sql="INSERT INTO user_History (idRes, title, QueryType, Mtimestamp)" +"VALUES (?, ?,?, ?)";
		       	    	
			       	      PreparedStatement preparedStatementHistory1 = conn.prepareStatement(sql);
			       	   preparedStatementHistory1.setInt(1, tempId);
			       	preparedStatementHistory1.setString(2, userQuery[1]);
			       	preparedStatementHistory1.setString(3, "Cancel Reservation");
			       	      Timestamp ts1 = Timestamp.valueOf(LocalDateTime.now(ZoneId.of("UTC+3")));
			       	   preparedStatementHistory1.setTimestamp(4, ts1);
			       	preparedStatementHistory1.executeUpdate(); 

			           sql="SELECT title FROM reserved_books WHERE idRes='"+tempId+"'";
		       	     
		       	      ResultSet rsx = stmt.executeQuery(sql);
		       	      results.clear();

		       	      
		       	   while(rsx.next()){
		       	          
		       	    	
		       	          //Concatinating the data into a single string
		       	          String title = rsx.getString("title");
		       
	                  //storing the data into the array of string myList
		       	          results.add(title);
		       	          
		       	          
		       	      
		       		}
		       	      
		       	      rsx.close();
		       	      stmt.close();
		       	      conn.close();
			       	      
			       	   
			       	      
			       	    
						
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		       	    
	            	 try {
	 					outToClient.writeObject(results);
	 				} catch (IOException e) {
	 					// TODO Auto-generated catch block
	 					e.printStackTrace();
	 				} 

	            }
	            
	            if (userQuery[0].startsWith("getHist"))
	            {
	            	results.clear();
	            	 // Initializing the connection the database and fetching the requested data 
	            	try {
						conn = DriverManager.getConnection(DB_URL,USER,PASS);
						  stmt = conn.createStatement();
			       	      String sql="";
			       	      
			       	      sql="SELECT id FROM users WHERE username='"+userQuery[1]+"'";
			       	 
			       	      ResultSet rs = stmt.executeQuery(sql);
			       	      rs.next();
						  int tempId=rs.getInt("id");
						  System.out.println(tempId);
						  rs.close();
			       	      
			       	      
			       	    

			           sql="SELECT title,QueryType,Mtimestamp FROM user_history WHERE idRes='"+tempId+"' AND Mtimestamp >= NOW() - INTERVAL 1 MINUTE";
		       	     
		       	      ResultSet rsx = stmt.executeQuery(sql);
		       	      results.clear();

		       	      
		       	   while(rsx.next()){
		       	          
		       	    	
		       	          //Concatinating the data into a single string
		       	          String title = rsx.getString("title");
		       	          String query = rsx.getString("QueryType");
		       	          Timestamp myts;
		       	       myts= rsx.getTimestamp("Mtimestamp");
		       	       String tstamp=myts.toGMTString();
		       	         String addFinal = (title + " - " + query + " - " + tstamp);
		       
	                  //storing the data into the array of string myList
		       	          results.add(addFinal);
		       	          
		       	          
		       	      
		       		}
		       	      
		       	      rsx.close();
		       	      stmt.close();
		       	      conn.close();
			       	      
			       	   
			       	      
			       	    
						
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		       	    
	            	 try {
	 					outToClient.writeObject(results);
	 				} catch (IOException e) {
	 					// TODO Auto-generated catch block
	 					e.printStackTrace();
	 				} 

	            }
	            
	            
	             
	            

	            	    
  }
	    
	    
  }    
}

  