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


public class secondaryServer {

    //final Variables associated to mySQL connections
    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost/libraryalt?useSSL=false";
    static final String USER = "root";
    static final String PASS = "passwordadmin";


    //Creating a serverSocket for the connection
    private static ServerSocket welcomeSocket;


    //Init the mySQL driver
    public secondaryServer() throws ClassNotFoundException {
        Class.forName("com.mysql.jdbc.Driver");
    }

    //function acceptConnections does most of the work associated with multithreading / socket prog
    public void acceptConnections() {

        try {

            welcomeSocket = new ServerSocket(3680);

        } catch (IOException e) {
            System.err.println("ServerSocket instantiation failure");
            e.printStackTrace();
            System.exit(0);
        }


        while (true) {

            try {
                Socket newConnection = welcomeSocket.accept();
                ServerThread st = new ServerThread(newConnection);
                new Thread(st).start();
            } catch (IOException ioe) {
                System.err.println("server accept failed");
            }
        }
    }



    public static void main(String argv[]) throws Exception {

        secondaryServer server = null;
        try {
            //Instantiate an object of this class. This will load the JDBC database driver
            server = new secondaryServer();
        } catch (ClassNotFoundException e) {
            System.out.println("unable to load JDBC driver");
            e.printStackTrace();
            System.exit(1);
        }

        server.acceptConnections();
    }

    // Socket programming & MYSQL datafetch

    class ServerThread implements Runnable {

        private Socket connectionSocket;
        private ArrayList < String > results = new ArrayList < String > ();


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
                e2.printStackTrace();
            }


            ObjectInputStream inFromClient = null;
            try {
                inFromClient = new ObjectInputStream(connectionSocket.getInputStream());
            } catch (IOException e1) {
                e1.printStackTrace();
            }

            //Read the sentence sent by the user
            String[] userQuery = new String[3];
            String outString = null;
            try {
                userQuery = (String[]) inFromClient.readObject();
            } catch (ClassNotFoundException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            }

            if (userQuery[0].startsWith("searchBook")) {
                results.clear();
                // Initializing the connection the database and fetching the requested data 
                try {
                    conn = DriverManager.getConnection(DB_URL, USER, PASS);
                    stmt = conn.createStatement();
                    String sql = "";
                    System.out.println(sql);
                    sql = "SELECT title FROM books";

                    ResultSet rs = stmt.executeQuery(sql);


                    System.out.println(userQuery[1]);



                    while (rs.next()) {
                        String tempTitle = rs.getString("title");
                        if (tempTitle.toLowerCase().contains(userQuery[1].toLowerCase())) {
                            results.add(tempTitle + " (FROM SECONDARY CATALOG)");
                        }
                    }





                    rs.close();
                    stmt.close();
                    conn.close();

                } catch (SQLException e) {
                    e.printStackTrace();
                }

                try {
                    outToClient.writeObject(results);
                } catch (IOException e) {
                    e.printStackTrace();


                }

            }

            if (userQuery[0].startsWith("getBooks")) {

                // Initializing the connection the database and fetching the requested data 
                try {
                    conn = DriverManager.getConnection(DB_URL, USER, PASS);
                    stmt = conn.createStatement();
                    String sql = "";
                    System.out.println(sql);
                    sql = "SELECT title,date_of_pub FROM books WHERE genre='" + userQuery[1] + "'";

                    ResultSet rs = stmt.executeQuery(sql);
                    results.clear();

                    while (rs.next()) {

                        String temp;

                        //Concatinating the data into a single string
                        String title = rs.getString("title");
                        int date = rs.getInt("date_of_pub");
                        temp = (title + " ; " + date + " (FROM SECONDARY CATALOG)");
                        //storing the data into the array of string myList
                        results.add(temp);



                    }

                    rs.close();
                    stmt.close();
                    conn.close();

                } catch (SQLException e) {
                    e.printStackTrace();
                }

                try {
                    outToClient.writeObject(results);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
    }
}
