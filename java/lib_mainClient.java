import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;


// Opening up the main class
public class Main_Client {
	
	// Main Class , we initialize the GUI and set it to visible
	
	public static void main(String args[])
	{
		// Use the OS theme for Java Swing
		
		try { 
		    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
		    e.printStackTrace();
		}
		
		mainGUI myGUI = new mainGUI();
		myGUI.setVisible(true);
	}
	
	// Initializing GUI
	static class mainGUI extends JFrame 
	{
		private String usernameFinal;
		
		//Default Constructor & GUI initialization
		mainGUI()
		{
			
			super("Librosia");
			setResizable(false);
			setSize(650,600);
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			setLocationRelativeTo(null);

		
			logonPage fpan = new logonPage();
			add(fpan);
			setLocationRelativeTo(null);
		}
		
		// PaneWindow Initialization
		public class paneWindow extends JTabbedPane
		{
			paneWindow()
			{
				setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				findBook finBook = new findBook();
				searchBook seaBook = new searchBook();
				reservations res = new reservations();
				add("My Account",res);
				add("Explore Catalogue",finBook);
				add("Advanced Search",seaBook);	
			}	
		}
		
			public class historyL extends JFrame
			{
				private	String[] findArr= new String[3];
				private JList histList;
				private ArrayList<String> histBook = new ArrayList<String>();
				
				historyL()
				{
					super("History");
					setResizable(false);
					setSize(400,400);
					setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
					setLocationRelativeTo(null);
					histList = new JList(histBook.toArray());
					histList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
					histList.setVisibleRowCount(10);
					histList.setLayoutOrientation(JList.VERTICAL);
					JScrollPane listScroller1 = new JScrollPane(histList);
					listScroller1.setPreferredSize(new Dimension(200, 200));
					add(listScroller1);
					findArr[0]="getHist";
					findArr[1]=usernameFinal;
					try {
						query(findArr);
					} catch (ClassNotFoundException e) {
						// For debugging purposes
						e.printStackTrace();
					} catch (IOException e) {
						// For debugging purposes
						e.printStackTrace();
					}	
				}
				
				private void query(String[] x) throws IOException, ClassNotFoundException
				{
					try
					{
						Socket socket = new Socket("localhost",3678);
						ObjectOutputStream outToServer = new ObjectOutputStream(socket.getOutputStream());
						ObjectInputStream inFromServer = new ObjectInputStream(socket.getInputStream());
						
						outToServer.writeObject(x);
						outToServer.flush();
					
						histBook = (ArrayList<String>) inFromServer.readObject();
						
						if (histBook.isEmpty())
						{
							histBook.add("No Recent Transaction");
						}
						
						histList.setListData(histBook.toArray());	
					}
					finally
					{
						//Empty 'finally' block
					}
				}
			}
		
		//Logon and createAcc pages
				public class logonPage extends JPanel
				{
					private JButton b1;
					private JButton b2;
					private JTextField id;
					private JPasswordField pass;
					private JLabel txt1;
					private JLabel txt2;
					private JLabel txt3;
					private JLabel txt4;
					private String check;
					
					logonPage()
					{
						setLayout(new GridBagLayout());
						GridBagConstraints c = new GridBagConstraints();
						handler myhandler=new handler();
						// Creating frame elements
						b1=new JButton("Login");
						b1.addActionListener(myhandler);
						
						b2=new JButton("Create Account");
						b2.addActionListener(myhandler);
						
						id=new JTextField(20);
						id.addActionListener(myhandler);
						
						pass=new JPasswordField(20);
						pass.addActionListener(myhandler);
						
						txt1=new JLabel("Username");
						
						txt2=new JLabel("Password");
						
						txt3=new JLabel("Librosia System");
						txt3.setHorizontalAlignment(JLabel.CENTER);
						txt3.setVerticalAlignment(JLabel.TOP);
						txt3.setFont(new Font("Apple Casual", Font.BOLD, 30));
						
						txt4=new JLabel(" ");
						txt4.setForeground(Color.RED);
						txt4.setHorizontalAlignment(JLabel.CENTER);
						txt4.setVerticalAlignment(JLabel.CENTER);
						
						// Initializing elements position
						c.insets=new Insets(50,10,50,10);
						c.gridx=0;
						c.gridy=0;
						c.gridwidth=3;
						c.gridheight=2;
						c.fill=GridBagConstraints.VERTICAL;
						add(txt3,c);
						
						c.gridwidth=1;
						c.gridheight=1;
						c.insets=new Insets(5,5,5,5);
						c.gridx=0;
						c.gridy=2;
						c.fill=GridBagConstraints.HORIZONTAL;
						add(txt1,c);
						
						c.gridx=0;
						c.gridy=3;
						c.fill=GridBagConstraints.HORIZONTAL;
						add(txt2,c);
						
						c.gridx=1;
						c.gridy=2;
						c.fill=GridBagConstraints.HORIZONTAL;
						add(id,c);
						
						c.gridx=1;
						c.gridy=3;
						c.fill=GridBagConstraints.HORIZONTAL;
						add(pass,c);
						
						c.insets=new Insets(5,5,5,5);
						c.gridx=2;
						c.gridy=2;
						c.fill=GridBagConstraints.HORIZONTAL;
						add(b1,c);
						
						c.gridx=2;
						c.gridy=3;
						c.fill=GridBagConstraints.HORIZONTAL;
						add(b2,c);
						
						c.insets=new Insets(0,0,0,0);
						c.gridx=0;
						c.gridy=4;
						c.gridwidth=4;
						c.gridheight=4;
						c.fill=GridBagConstraints.HORIZONTAL;
						add(txt4,c);
					}
					
					private class handler implements ActionListener 
					{
						public void actionPerformed(ActionEvent event) 
						{
							if(event.getSource()==b1)
							{
								String[] usLogon = new String[3];
								
								
								if (id.getText().isEmpty() || pass.getText().isEmpty() )
								{
									txt4.setText("Please enter a valid username/password");
								}
								else
								{
									usLogon[0]="usLogon";
									usLogon[1] =id.getText();
									usLogon[2]=String.valueOf(pass.getPassword());

									try {
										queryServer(usLogon);
									} catch (ClassNotFoundException e) {
										e.printStackTrace();
									} catch (IOException e) {
										e.printStackTrace();
									}

									if (check.startsWith("ok") )
									{
										usernameFinal=id.getText();
										paneWindow xcd = new paneWindow();
										invalidate();
										setVisible(false);
									    	removeAll();
									    
									    getContentPane().add(xcd);
										}
										
										else if (check.startsWith("nop"))
										{
											txt4.setText("The username/password entered is incorrect");
										}
								}
			
							}
							
							if(event.getSource()==b2)
							{
								invalidate();
								setVisible(false);
							    removeAll();
							    
								createAcc temp = new createAcc();
								getContentPane().add(temp);
								
							}
						}
					}
					
					private void queryServer(String[] x) throws IOException, ClassNotFoundException
					{
						try
						{
							Socket socket = new Socket("localhost",3678);
							ObjectOutputStream outToServer = new ObjectOutputStream(socket.getOutputStream());
							ObjectInputStream inFromServer = new ObjectInputStream(socket.getInputStream());
							
							outToServer.writeObject(x);
							outToServer.flush();
							
							
							check = (String) inFromServer.readObject();
							
							System.out.println(check);							
						}
						finally
						{
							//Empty 'finally' block
						}
					}
				}
				
				class createAcc extends JPanel
				{
					private JButton b1;
					private JButton b2;
					private JTextField id;
					private JPasswordField pass;
					private JPasswordField repass;
					private JLabel txt1;
					private JLabel txt2;
					private JLabel txt3;
					private JLabel txt4;
					private JLabel txt5;
					private ButtonGroup btngr;
					private String check;
					createAcc()
					{
						setLayout(new GridBagLayout());
						GridBagConstraints c = new GridBagConstraints();
						
						handler myhandler=new handler();
						// Creating frame elements
						
						b1=new JButton("Confirm Creation");
						b1.addActionListener(myhandler);
						
						b2=new JButton("Cancel");
						b2.addActionListener(myhandler);
						
						id=new JTextField(25);
						id.addActionListener(myhandler);
						
						pass=new JPasswordField(25);
						pass.addActionListener(myhandler);
						
						repass=new JPasswordField(25);
						repass.addActionListener(myhandler);
						
						txt1=new JLabel("Account Creation");
						txt1.setHorizontalAlignment(JLabel.CENTER);
						txt1.setVerticalAlignment(JLabel.CENTER);
						txt1.setFont(new Font("Apple Casual", Font.BOLD, 28));
						
						txt2=new JLabel("Username");
						txt2.setHorizontalAlignment(JLabel.CENTER);
					
						
						txt3=new JLabel("Password");
						txt3.setHorizontalAlignment(JLabel.CENTER);
						
						
						txt4=new JLabel("Confirm Password");
						txt4.setHorizontalAlignment(JLabel.CENTER);
						
						txt5=new JLabel(" ");
						txt5.setForeground(Color.RED);
						txt5.setHorizontalAlignment(JLabel.CENTER);
						txt5.setVerticalAlignment(JLabel.CENTER);
						
						
						c.insets=new Insets(30,5,30,5);
						c.gridx=0;
						c.gridy=0;
						c.gridwidth=3;
						c.fill=GridBagConstraints.HORIZONTAL;
						add(txt1,c);
						c.gridwidth=1;
						c.insets=new Insets(5,12,5,12);
						c.gridx=0;
						c.gridy=1;
						c.fill=GridBagConstraints.HORIZONTAL;
						add(txt2,c);
						
						c.gridx=0;
						c.gridy=2;
						c.fill=GridBagConstraints.HORIZONTAL;
						add(txt3,c);
						
						c.gridx=0;
						c.gridy=3;
						c.fill=GridBagConstraints.HORIZONTAL;
						add(txt4,c);
						c.gridx=1;
						c.gridy=1;
						c.fill=GridBagConstraints.HORIZONTAL;
						add(id,c);
						c.gridx=1;
						c.gridy=2;
						c.fill=GridBagConstraints.HORIZONTAL;
						add(pass,c);
						c.gridx=1;
						c.gridy=3;
						c.fill=GridBagConstraints.HORIZONTAL;
						add(repass,c);
					
						c.gridx=2;
						c.gridy=2;
						c.fill=GridBagConstraints.HORIZONTAL;
						add(b1,c);
						c.gridx=2;
						c.gridy=3;
						c.fill=GridBagConstraints.HORIZONTAL;
						add(b2,c);
						
						c.gridx=1;
						c.gridy=4;
						c.fill=GridBagConstraints.HORIZONTAL;
						add(txt5,c);
						
					}
					
					private class handler implements ActionListener 
					{
						public void actionPerformed(ActionEvent event) 
						{
							
							String[] idPass = new String[3];
							if(event.getSource()==b1)
							{
								if (id.getText().isEmpty() || pass.getText().isEmpty() || repass.getText().isEmpty()  )
								{
									txt5.setText("Please enter a valid username/password");
								}
								else if ( !(Arrays.equals(pass.getPassword(), repass.getPassword())))
								{
									
									txt5.setText("Both Password fields need to match");

								}
								else
								{
									idPass[0]="idPass";
									idPass[1]=id.getText();
									idPass[2]=String.valueOf(pass.getPassword());
									try {
										queryServer(idPass);
									} catch (ClassNotFoundException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									} catch (IOException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									
									if (check.startsWith("ok") )
									{
										txt5.setText("Account Successfully Created");
										b2.setText("Back");
									}
									
									else if (check.startsWith("dup"))
									{
										txt5.setText("An Account with the same name already exists");
									}
									
									
								}
							}
							
							if(event.getSource()==b2)
							{
								invalidate();
							    setVisible(false);
							    removeAll();
								logonPage temp = new logonPage();
								getContentPane().add(temp);
							}
						}
						
						
					}
					
					private void queryServer(String[] x) throws IOException, ClassNotFoundException
					{	
						try
						{
							Socket socket = new Socket("localhost",3678);
							ObjectOutputStream outToServer = new ObjectOutputStream(socket.getOutputStream());
							ObjectInputStream inFromServer = new ObjectInputStream(socket.getInputStream());
							
							outToServer.writeObject(x);
							outToServer.flush();
							
							check = (String) inFromServer.readObject();
							System.out.println(check);
						}
						finally
						{
							//Empty 'finally' block
						}
					}
				}
		
		// reservations panel initialization
		public class reservations extends JPanel 
		{
			private JLabel title; // done
			private JLabel mybooks; // done
			private JLabel libcatalogue; // done
			private JLabel search; // done
			
			private JTextField searchfield; // done
			
			private JButton history;// done
			private JButton relRes;// done
			private JButton relReq;// done
			private JLabel info;// done
			private JButton refresh;// done
			
			private JList mybookslist; // done
			private JList myoldlist; // done
			
			private ArrayList<String> reservationsBook = new ArrayList<String>();
			private ArrayList<String> requestsBook = new ArrayList<String>();
			
			private	String[] findArr= new String[3];

			
			reservations()
			{
				
				setLayout(new GridBagLayout());
				GridBagConstraints c = new GridBagConstraints();
				handler myhandler = new handler();
				listHandler liHan = new listHandler();
				title= new JLabel("Welcome "+usernameFinal);
				title.setFont(new Font("Apple Casual",Font.BOLD,28));
				c.insets=new Insets(5,10,40,5);
				c.gridx=0;
				c.gridy=0;
				c.fill=GridBagConstraints.HORIZONTAL;
				add(title,c);
				
				mybooks=new JLabel("Current Reservations:");
				mybooks.setFont(new Font("Apple Casual",Font.PLAIN,12));
				c.insets=new Insets(5,5,5,5);
				c.gridx=0;
				c.gridy=1;
				c.gridheight=1;
				c.gridwidth=1;
				c.fill=GridBagConstraints.HORIZONTAL;
				add(mybooks,c);
				
				mybookslist=new JList(reservationsBook.toArray());
				mybookslist.addListSelectionListener(liHan);
				mybookslist.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
				mybookslist.setVisibleRowCount(10);
				mybookslist.setLayoutOrientation(JList.VERTICAL);
				JScrollPane listScroller = new JScrollPane(mybookslist);
				listScroller.setPreferredSize(new Dimension(200, 200));
				
				c.insets=new Insets(5,5,5,5);
				c.gridx=0;
				c.gridy=2;
				c.gridheight=3;
				c.gridwidth=1;
				c.fill=GridBagConstraints.HORIZONTAL;
				add(listScroller,c);
				
				libcatalogue=new JLabel("Requested Reservations:");
				libcatalogue.setFont(new Font("Apple Casual",Font.PLAIN,12));
				c.insets=new Insets(5,5,5,5);
				c.gridx=1;
				c.gridy=1;
				c.gridheight=1;
				c.gridwidth=1;
				c.fill=GridBagConstraints.HORIZONTAL;
				add(libcatalogue,c);
				
				myoldlist=new JList(requestsBook.toArray());
				myoldlist.addListSelectionListener(liHan);
				myoldlist.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
				myoldlist.setVisibleRowCount(10);
				myoldlist.setLayoutOrientation(JList.VERTICAL);
				JScrollPane listScroller1 = new JScrollPane(myoldlist);
				listScroller1.setPreferredSize(new Dimension(200, 200));
				
				c.insets=new Insets(5,5,5,5);
				c.gridx=1;
				c.gridy=2;
				c.gridheight=3;
				c.gridwidth=1;
				c.fill=GridBagConstraints.NONE;
				add(listScroller1,c);
				
				relRes = new JButton("Cancel Reservation");
				relRes.addActionListener(myhandler);
				relRes.setEnabled(false);
				c.insets=new Insets(5,5,5,5);
				c.gridx=0;
				c.gridy=5;
				c.gridheight=1;
				c.gridwidth=1;
				c.fill=GridBagConstraints.HORIZONTAL;
				add(relRes,c);
				
				relReq = new JButton("Cancel Request");
				relReq.setEnabled(false);
				relReq.addActionListener(myhandler);
				c.insets=new Insets(5,5,5,5);
				c.gridx=1;
				c.gridy=5;
				c.gridheight=1;
				c.gridwidth=1;
				c.fill=GridBagConstraints.HORIZONTAL;
				add(relReq,c);
				
				history = new JButton("View History");
				history.addActionListener(myhandler);
				c.insets=new Insets(5,5,5,5);
				c.gridx=2;
				c.gridy=2;
				c.gridheight=1;
				c.gridwidth=1;
				c.fill=GridBagConstraints.HORIZONTAL;
				add(history,c);
				
				refresh = new JButton("Refresh");
				refresh.addActionListener(myhandler);
				c.insets=new Insets(5,5,5,5);
				c.gridx=2;
				c.gridy=4;
				c.gridheight=1;
				c.gridwidth=1;
				c.fill=GridBagConstraints.HORIZONTAL;
				add(refresh,c);
				
				info = new JLabel("View History of Last 10 minutes");
				info.setFont(new Font("Apple Casual",Font.BOLD,10));
				c.insets=new Insets(5,5,5,5);
				c.gridx=2;
				c.gridy=3;
				c.gridheight=1;
				c.gridwidth=1;
				c.fill=GridBagConstraints.HORIZONTAL;
				add(info,c);
				
				findArr[0]="getResReq";
				findArr[2]=usernameFinal;
				findArr[1]="getRes";
				try {
					querygetReservedRequest(findArr);
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				findArr[1]="getReq";
				try {
					querygetReservedRequest(findArr);
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}

				
			}
			
			private class handler implements ActionListener 
			{
				public void actionPerformed(ActionEvent event) 
				{
					
					if ( event.getSource()==relReq)
					{
						findArr[0]="relReq";
						findArr[1]=myoldlist.getSelectedValue().toString();
						findArr[2]=usernameFinal;
						try {
							queryremReservedRequest(findArr);
						} catch (ClassNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						relReq.setEnabled(false);
					}
					
					if ( event.getSource()==relRes)
					{
						
						findArr[0]="relRes";
						findArr[1]=mybookslist.getSelectedValue().toString();
						findArr[2]=usernameFinal;
						try {
							queryremReservedRequest(findArr);
						} catch (ClassNotFoundException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						}
						
						relRes.setEnabled(false);
						
					}

					if ( event.getSource()==refresh)
					{
						
						findArr[0]="getResReq";
						findArr[2]=usernameFinal;
						findArr[1]="getRes";
						try {
							querygetReservedRequest(findArr);
						} catch (ClassNotFoundException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						}
						
						findArr[1]="getReq";
						try {
							querygetReservedRequest(findArr);
						} catch (ClassNotFoundException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						}	
					}
					
					if ( event.getSource()==history)
					{
						historyL xcd = new historyL();
						xcd.setVisible(true);
					}	
				}
			}
			
			private class listHandler implements ListSelectionListener
			{
				public void valueChanged(ListSelectionEvent event) 
				{
					if (event.getSource()==mybookslist)
					{
						relRes.setEnabled(true);
					}
					
					if (event.getSource()==myoldlist)
					{
						relReq.setEnabled(true);
					}	
				}
			}
			private void queryremReservedRequest(String[] x) throws IOException, ClassNotFoundException
			{
				
				try
				{
					Socket socket = new Socket("localhost",3678);
					ObjectOutputStream outToServer = new ObjectOutputStream(socket.getOutputStream());
					ObjectInputStream inFromServer = new ObjectInputStream(socket.getInputStream());
					
					outToServer.writeObject(x);
					outToServer.flush();
				
					if (findArr[0]=="relRes")
					{
						reservationsBook = (ArrayList<String>) inFromServer.readObject();
						
						mybookslist.setListData(reservationsBook.toArray());
					}
					
					if (findArr[0]=="relReq")
					{
						requestsBook = (ArrayList<String>) inFromServer.readObject();
						
						myoldlist.setListData(requestsBook.toArray());
					}	
				}
				finally
				{
					//Empty 'finally' block
				}
			}
			
			private void querygetReservedRequest(String[] x) throws IOException, ClassNotFoundException
			{
				
				try
				{
					Socket socket = new Socket("localhost",3678);
					ObjectOutputStream outToServer = new ObjectOutputStream(socket.getOutputStream());
					ObjectInputStream inFromServer = new ObjectInputStream(socket.getInputStream());
					
					outToServer.writeObject(x);
					outToServer.flush();
				
					if (findArr[1]=="getRes")
					{
						reservationsBook = (ArrayList<String>) inFromServer.readObject();
						
						mybookslist.setListData(reservationsBook.toArray());
					}
					
					if (findArr[1]=="getReq")
					{
						requestsBook = (ArrayList<String>) inFromServer.readObject();
						
						myoldlist.setListData(requestsBook.toArray());
					}
					
				}
				finally
				{
					//Empty 'finally' block
				}
			}
			
			
		}
		
		
		public class searchBook extends JPanel 
		{
			private JList findBook ;
			private ArrayList<String> searchBook = new ArrayList<String>();
			private JButton placeRequest;
			private	String[] findArr= new String[3];
			private JButton searchBut;
			private JTextField searchValue;
			private	JLabel	bookInfo;
			private JLabel msgSuc;
			
			searchBook()
			{
				setLayout(new GridBagLayout());
				GridBagConstraints c = new GridBagConstraints();
				handler myhandler = new handler();
				
				JLabel title= new JLabel("Search");
				title.setFont(new Font("Apple Casual",Font.BOLD,28));
				c.insets=new Insets(5,10,40,5);
				c.gridx=0;
				c.gridy=0;
				c.gridheight=1;
				c.gridwidth=3;
				c.fill=GridBagConstraints.HORIZONTAL;
				add(title,c); 
				
				JLabel search = new JLabel("Search Book :");
				c.insets=new Insets(5,5,5,5);
				c.gridx=0;
				c.gridy=1;
				c.gridheight=1;
				c.gridwidth=2;
				c.fill=GridBagConstraints.HORIZONTAL;
				add(search,c);
				
				searchValue = new JTextField(20);
				c.insets=new Insets(5,5,5,5);
				c.gridx=0;
				c.gridy=2;
				c.gridheight=1;
				c.gridwidth=2;
				c.fill=GridBagConstraints.HORIZONTAL;
				add(searchValue,c);
				
				searchBut = new JButton("Search");
				searchBut.addActionListener(myhandler);
				c.insets=new Insets(5,5,5,5);
				c.gridx=0;
				c.gridy=3;
				c.gridheight=1;
				c.gridwidth=2;
				c.fill=GridBagConstraints.HORIZONTAL;
				add(searchBut,c);
				
				
				
				JLabel mybooks=new JLabel("Search Results:");
				mybooks.setFont(new Font("Apple Casual",Font.PLAIN,12));
				c.insets=new Insets(5,5,5,5);
				c.gridx=3;
				c.gridy=1;
				c.gridheight=1;
				c.gridwidth=2;
				c.fill=GridBagConstraints.HORIZONTAL;
				add(mybooks,c);
				
				listHandler listHand = new listHandler();
				findBook=new JList(searchBook.toArray());
				findBook.addListSelectionListener(listHand);
				findBook.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
				findBook.setVisibleRowCount(10);
				findBook.setLayoutOrientation(JList.VERTICAL);
				JScrollPane listScroller = new JScrollPane(findBook);
				listScroller.setPreferredSize(new Dimension(200, 200));
				
				c.insets=new Insets(5,5,5,5);
				c.gridx=3;
				c.gridy=2;
				c.gridheight=3;
				c.gridwidth=3;
				c.fill=GridBagConstraints.HORIZONTAL;
				add(listScroller,c);
				
				placeRequest = new JButton("Request Book");
				placeRequest.addActionListener(myhandler);
				placeRequest.setEnabled(false);
				c.insets=new Insets(5,5,5,5);
				c.gridx=3;
				c.gridy=5;
				c.gridheight=1;
				c.gridwidth=3;
				c.fill=GridBagConstraints.HORIZONTAL;
				add(placeRequest,c);
				
				bookInfo=new JLabel("");
				bookInfo.setPreferredSize(new Dimension(100,50));
				bookInfo.setFont(new Font("Apple Casual",Font.PLAIN,12));
				bookInfo.setHorizontalAlignment(JLabel.CENTER);
				c.insets=new Insets(5,5,5,10);
				c.gridx=6;
				c.gridy=3;
				c.gridheight=1;
				c.gridwidth=1;
				c.fill=GridBagConstraints.NONE;
				add(bookInfo,c);
				
				msgSuc=new JLabel("");
				msgSuc.setPreferredSize(new Dimension(170,50));
				msgSuc.setFont(new Font("Apple Casual",Font.PLAIN,10));
				msgSuc.setHorizontalAlignment(JLabel.CENTER);
				c.insets=new Insets(5,5,5,5);
				c.gridx=6;
				c.gridy=4;
				c.gridheight=1;
				c.gridwidth=1;
				c.fill=GridBagConstraints.NONE;
				add(msgSuc,c);
			}
			
			private class handler implements ActionListener 
			{
				public void actionPerformed(ActionEvent event) 
				{
					
					if ( event.getSource()==placeRequest)
					{
						if(placeRequest.getText()=="Reserve Book")
						{
							findArr[0]="resBook";
							String temp = findBook.getSelectedValue().toString();
							findArr[1]= temp;
							findArr[2] = usernameFinal;
							
							try {
								queryReserveRequestBook(findArr);
							} catch (ClassNotFoundException e) {
								e.printStackTrace();
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
						else if (placeRequest.getText()=="Request Book")
						{
							findArr[0]="reqBook";
							String temp = findBook.getSelectedValue().toString();
							findArr[1]= temp;
							findArr[2] = usernameFinal;
							
							try {
								queryReserveRequestBook(findArr);
							} catch (ClassNotFoundException e) {
								e.printStackTrace();
							} catch (IOException e) {
								e.printStackTrace();
							}
						}	
					}
					
					if (event.getSource()==searchBut)
					{
						findArr[0]="searchBook";
						findArr[1]=searchValue.getText();
						
						try {
							querySearchBook(findArr);
						} catch (ClassNotFoundException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						}
						
					}
				}
			}
			
			private void queryReserveRequestBook(String[] x) throws IOException, ClassNotFoundException
			{
				
				try
				{
					Socket socket = new Socket("localhost",3678);
					ObjectOutputStream outToServer = new ObjectOutputStream(socket.getOutputStream());
					ObjectInputStream inFromServer = new ObjectInputStream(socket.getInputStream());
					
					outToServer.writeObject(x);
					outToServer.flush();
				
					msgSuc.setText((String)inFromServer.readObject());
					placeRequest.setEnabled(false);
					
				}
				finally
				{
					//Empty 'finally' block
				}
			}
			
			private void querySearchBook(String[] x) throws IOException, ClassNotFoundException
			{
				try
				{
					Socket socket = new Socket("localhost",3678);
					ObjectOutputStream outToServer = new ObjectOutputStream(socket.getOutputStream());
					ObjectInputStream inFromServer = new ObjectInputStream(socket.getInputStream());
					
					outToServer.writeObject(x);
					outToServer.flush();
				
					searchBook = ((ArrayList<String>)inFromServer.readObject());
					findBook.setListData(searchBook.toArray());
				}
				finally
				{
					//Empty 'finally' block
				}
			}
			
			private class listHandler implements ListSelectionListener
			{
				public void valueChanged(ListSelectionEvent event) 
				{
					if (event.getSource()==findBook)
					{
						findArr[0]="getBookInfo";
						findArr[1]= findBook.getSelectedValue().toString();
						System.out.println(findArr[1]);
						
						
						try {
							queryGetBookInfoServer(findArr);
						} catch (ClassNotFoundException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}	
				}
			}
			
			private void queryGetBookInfoServer(String[] x) throws IOException, ClassNotFoundException
			{
				
				try
				{
					Socket socket = new Socket("localhost",3678);
					ObjectOutputStream outToServer = new ObjectOutputStream(socket.getOutputStream());
					ObjectInputStream inFromServer = new ObjectInputStream(socket.getInputStream());
					
					outToServer.writeObject(x);
					outToServer.flush();
					
					bookInfo.setText((String)inFromServer.readObject());
					if (bookInfo.getText().startsWith("Available"))
					{
						placeRequest.setText("Reserve Book");
					}
					if (bookInfo.getText().startsWith("Reserved"))
					{
						placeRequest.setText("Request Book");
					}
					placeRequest.setEnabled(true);	
				}
				finally
				{
					//Empty "finally" block
				}
			}
		}

		public class findBook extends JPanel
		{
			private JLabel username; 
			private JLabel mybooks;
			private JLabel libcatalogue; 
			private JLabel search; 
			
			private JTextField searchfield; 
			
			private JButton placeRequest;
			private JButton history;
			
			private JComboBox bookgenre;
			private JRadioButton all;
			private JRadioButton available;
			private JRadioButton reservation;
			
			private JList mybookslist;
			private JList serverbooks;
			
			private ArrayList<String> genre = new ArrayList<String>();
			private ArrayList<String> genBook = new ArrayList<String>();
			
			private	String[] findArr= new String[3];
			
			private JLabel bookInfo;
			private JLabel msgSuc;

			findBook()
			{
					setLayout(new GridBagLayout());
					GridBagConstraints c = new GridBagConstraints();
			
					username= new JLabel("Local Catalog");
					username.setFont(new Font("Apple Casual",Font.BOLD,28));
					c.insets=new Insets(5,10,40,5);
					c.gridx=0;
					c.gridy=0;
					c.fill=GridBagConstraints.HORIZONTAL;
					add(username,c);
					
					mybooks=new JLabel("Select Genre:");
					mybooks.setFont(new Font("Apple Casual",Font.PLAIN,12));
					c.insets=new Insets(5,5,5,5);
					c.gridx=0;
					c.gridy=1;
					c.gridheight=1;
					c.gridwidth=1;
					c.fill=GridBagConstraints.HORIZONTAL;
					add(mybooks,c);
				
					mybookslist=new JList(genre.toArray());
					mybookslist.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
					mybookslist.setVisibleRowCount(10);
					mybookslist.setLayoutOrientation(JList.VERTICAL);
					
					listHandler handlerList = new listHandler();
					handler myhandler = new handler();
					
					mybookslist.addListSelectionListener(handlerList);
					JScrollPane listScroller = new JScrollPane(mybookslist);
					listScroller.setPreferredSize(new Dimension(200, 200));
					
					c.insets=new Insets(5,5,5,5);
					c.gridx=0;
					c.gridy=2;
					c.gridheight=3;
					c.gridwidth=1;
					c.fill=GridBagConstraints.HORIZONTAL;
					add(listScroller,c);
					
					libcatalogue=new JLabel("Available Books :");
					libcatalogue.setFont(new Font("Apple Casual",Font.PLAIN,12));
					c.insets=new Insets(5,5,5,5);
					c.gridx=1;
					c.gridy=1;
					c.gridheight=1;
					c.gridwidth=1;
					c.fill=GridBagConstraints.HORIZONTAL;
					add(libcatalogue,c);
					
					serverbooks=new JList(genBook.toArray());
					serverbooks.addListSelectionListener(handlerList);
					serverbooks.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
					serverbooks.setVisibleRowCount(10);
					serverbooks.setLayoutOrientation(JList.VERTICAL);
					JScrollPane listScroller1 = new JScrollPane(serverbooks);
					listScroller1.setPreferredSize(new Dimension(200, 200));
					
					c.insets=new Insets(5,5,5,5);
					c.gridx=1;
					c.gridy=2;
					c.gridheight=3;
					c.gridwidth=1;
					c.fill=GridBagConstraints.NONE;
					add(listScroller1,c);
					
					placeRequest = new JButton("Reserve Book");
					placeRequest.addActionListener(myhandler);
					placeRequest.setEnabled(false);
					c.insets=new Insets(5,5,5,5);
					c.gridx=1;
					c.gridy=5;
					c.gridheight=1;
					c.gridwidth=1;
					c.fill=GridBagConstraints.HORIZONTAL;
					add(placeRequest,c);
					
					
					bookInfo=new JLabel("");
					bookInfo.setPreferredSize(new Dimension(100,50));
					bookInfo.setFont(new Font("Apple Casual",Font.PLAIN,12));
					bookInfo.setHorizontalAlignment(JLabel.CENTER);
					c.insets=new Insets(5,5,5,10);
					c.gridx=2;
					c.gridy=3;
					c.gridheight=1;
					c.gridwidth=1;
					c.fill=GridBagConstraints.NONE;
					add(bookInfo,c);
					
					
					msgSuc=new JLabel("");
					msgSuc.setPreferredSize(new Dimension(170,50));
					msgSuc.setFont(new Font("Apple Casual",Font.PLAIN,10));
					msgSuc.setHorizontalAlignment(JLabel.CENTER);
					c.insets=new Insets(5,5,5,5);
					c.gridx=2;
					c.gridy=4;
					c.gridheight=1;
					c.gridwidth=1;
					c.fill=GridBagConstraints.NONE;
					add(msgSuc,c);
					
					findArr[0]="genre";
				
						try {
							queryGenreServer(findArr);
						} catch (ClassNotFoundException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						}
				}
			
			private class handler implements ActionListener 
			{
				public void actionPerformed(ActionEvent event) 
				{
					if ( event.getSource()==placeRequest)
					{
						if(placeRequest.getText()=="Reserve Book")
						{
							findArr[0]="resBook";
							String temp = serverbooks.getSelectedValue().toString();
							findArr[1]= temp.substring(0, temp.indexOf(";"));
							findArr[2] = usernameFinal;
							
							try {
								queryReserveRequestBook(findArr);
							} catch (ClassNotFoundException e) {
								e.printStackTrace();
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
						else if (placeRequest.getText()=="Request Book")
						{
							findArr[0]="reqBook";
							String temp = serverbooks.getSelectedValue().toString();
							findArr[1]= temp.substring(0, temp.indexOf(";"));
							findArr[2] = usernameFinal;
							
							try {
								queryReserveRequestBook(findArr);
							} catch (ClassNotFoundException e) {
								e.printStackTrace();
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}
				}
			}
			
			private void queryReserveRequestBook(String[] x) throws IOException, ClassNotFoundException
			{
				
				try
				{
					Socket socket = new Socket("localhost",3678);
					ObjectOutputStream outToServer = new ObjectOutputStream(socket.getOutputStream());
					ObjectInputStream inFromServer = new ObjectInputStream(socket.getInputStream());
					
					outToServer.writeObject(x);
					outToServer.flush();
				
					msgSuc.setText((String)inFromServer.readObject());
					placeRequest.setEnabled(false);		
				}
				finally
				{
					//Empty "finally" block
				}
			}
			
			private class listHandler implements ListSelectionListener
			{
				public void valueChanged(ListSelectionEvent event) 
				{
					if (event.getSource()==mybookslist)
					{
						findArr[0]="getBooks";
						findArr[1]=mybookslist.getSelectedValue().toString();
						try {
							queryGetBookServer(findArr);
						} catch (ClassNotFoundException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					
					if (event.getSource()==serverbooks)
					{
						findArr[0]="getBookInfo";
						String temp = serverbooks.getSelectedValue().toString();
						findArr[1]= temp.substring(0, temp.indexOf(";"));
						System.out.println(findArr[1]);
						
						
						try {
							queryGetBookInfoServer(findArr);
						} catch (ClassNotFoundException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}
			
			private void queryGetBookInfoServer(String[] x) throws IOException, ClassNotFoundException
			{
				
				try
				{
					Socket socket = new Socket("localhost",3678);
					ObjectOutputStream outToServer = new ObjectOutputStream(socket.getOutputStream());
					ObjectInputStream inFromServer = new ObjectInputStream(socket.getInputStream());
					
					outToServer.writeObject(x);
					outToServer.flush();
					bookInfo.setText((String)inFromServer.readObject());
					if (bookInfo.getText().startsWith("Available"))
					{
						placeRequest.setText("Reserve Book");
					}
					if (bookInfo.getText().startsWith("Reserved"))
					{
						placeRequest.setText("Request Book");
					}
					placeRequest.setEnabled(true);
					msgSuc.setText("");
					System.out.println(genre);
				}
				finally
				{
					//Empty "finally" block
				}
			}
			private void queryGetBookServer(String[] x) throws IOException, ClassNotFoundException
			{
				
				try
				{
					Socket socket = new Socket("localhost",3678);
					ObjectOutputStream outToServer = new ObjectOutputStream(socket.getOutputStream());
					ObjectInputStream inFromServer = new ObjectInputStream(socket.getInputStream());
					
					outToServer.writeObject(x);
					outToServer.flush();
				
					bookInfo.setText("");
					msgSuc.setText("");
					placeRequest.setEnabled(false);
					
					genBook = (ArrayList<String>) inFromServer.readObject();
					
					serverbooks.setListData(genBook.toArray());
					System.out.println(genre);
				}
				finally
				{
					//Empty "finally" block
				}
			}
			private void queryGenreServer(String[] x) throws IOException, ClassNotFoundException
			{
				
				try
				{
					Socket socket = new Socket("localhost",3678);
					ObjectOutputStream outToServer = new ObjectOutputStream(socket.getOutputStream());
					ObjectInputStream inFromServer = new ObjectInputStream(socket.getInputStream());
					outToServer.writeObject(x);
					outToServer.flush();
					genre = (ArrayList<String>) inFromServer.readObject();
					mybookslist.setListData(genre.toArray());
					System.out.println(genre);
				}
				finally
				{
					//Empty "finally" block
				}
			}
			}
	}
}
