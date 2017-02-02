package networkingOld;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import java.awt.GridLayout;

/**
 * Displays the currently connected client(s) information in a scroll-able JTable object on a JPanel.
 */
public class ClientLobby extends JPanel
{
	private ClientSender sender;
	private ClientReceiver reciever;
	
	private JScrollPane scoreboard;
	
	private DefaultTableModel tableModel; 
	private JTable Jtable; 
	
	private int clientID;
	
	private LobbyCheckThread lobbyCheck;
	private CheckRecieverThread RecieverMessages;
	
	/**
	 * Constructs the required objects to produce a scroll-able table of connected client(s).
	 * @param sender The class which sends messages to the Server.
	 * @param reciever The class which handles messages received from the Server.
	 * @param frame The GUI display JFrame.
	 * @param clientName The nickname of the connected client.
	 * @param clientID The id of the connected client.
	 */
	public ClientLobby(ClientSender sender,ClientReceiver reciever, JFrame frame, String clientName, int clientID)
	{
		super();
		
		this.sender = sender;
		this.reciever = reciever;
		this.clientID = clientID;
		
		//A scroll-able panel.
		scoreboard = new JScrollPane();
		
		setLayout(new GridLayout(1,1));
		
		//Create new table model and set cells as uneditable 
		//source - 'http://stackoverflow.com/questions/18795791/how-to-make-individual-cell-of-a-jtable-uneditable'
		tableModel = new DefaultTableModel() {
		    public boolean isCellEditable(int rowIndex, int mColIndex) {
		        return false;
		    }
		};
		//Create new JTable
		Jtable = new JTable(tableModel);
		//Only select one row at a time (don't want selecting multiple clients!)
		Jtable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		// Create a couple of columns
		tableModel.addColumn("ID");
		tableModel.addColumn("Nickname"); 
		tableModel.addColumn("Game Status");

		//Populate fields with retrieved data
		updateText();
		
		//Thread checking for messages from other users relating to the Lobby. 
		RecieverMessages = new CheckRecieverThread(sender, reciever,frame,clientName,clientID);
		RecieverMessages.start();
		
		//Periodically updates the client list.
		lobbyCheck = new LobbyCheckThread(this);
		lobbyCheck.start();
	}
	
	/**
	 * Updates the displayed connected client information with up-to-date data.
	 */
	public synchronized void updateText()
	{
		//Retrieve information from the server about currently connect client(s).
		//Remove all current rows from the table.
		//Add the new data to the table.
		//As boolean represents in game or not - convert to suitable text to display.
		
		//Tell the server we want the current clients connected.
		sender.sendMessage("Retrieve Lobby List");
		
		String response = "";
	
		int startPos = 0; //Start of messages retrieved
		int endPos = 0; //End of messages retrieved     ... Both used for remove messages after.
		
		//Give enough time to retrieve all of the data.
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			System.err.println("Interrupted while sleeping.");
		}
		
		//Cycle through all of the messages waiting for the client.
		for(int i = 0; i < reciever.messages.size(); i++)
		{
			response = reciever.messages.get(i);
			
			//If the message indicates the start of data we want
			if(response.compareTo("Start: Lobby") == 0)
			{
				startPos = i;
				
				//Remove all existing rows in the table.
				for(int k = tableModel.getRowCount() - 1; k >= 0; k--)
				{
					tableModel.removeRow(k);
				}
				
				//Add first row with column headers, in bold using HTML formatting.
				//source - 'http://forums.codeguru.com/showthread.php?369283-Formatting-JTable-rows'
				tableModel.addRow(new Object[]{"<html><b>ID</b></html>","<html><b>Nickname</b></html>","<html><b>Game Status</b></html>"});
				
				for(int j = i+1; j < reciever.messages.size(); j++)
				{
					response = reciever.messages.get(j);
					
					//If all relevant data has been retrieved.
					if(response.compareTo("End: Lobby") == 0)
					{
						endPos = j;
						//Add the table.
						this.add(scoreboard.add(Jtable));
						break;
					}
					else
					{
						//All data for one client is stored in a string, seperated by :
						String values[] = response.split(":");
						//If currently in a game
						if((values[2].toLowerCase()).compareTo("true") == 0)
						{
							tableModel.addRow(new Object[]{values[0],values[1],"In Game"});
						}
						//If not currently in a game
						else if((values[2].toLowerCase()).compareTo("false") == 0)
						{
							tableModel.addRow(new Object[]{values[0],values[1],"Not In Game"});
						}
					}
				}
			}
		}
		//If some messages have been retrieved, delete the range.
		if(endPos > 0)
			reciever.removeMessages(startPos, endPos);

		//Redisplay the tables.
		repaint();
	}
	
	/**
	 * Unselect any currently selected rows in the table.
	 */
	public void unselect()
	{
		//Clear currently selected client.
		Jtable.clearSelection();
	}
	
	/**
	 * Send a request to a selected client to play a game of tic-tac-toe with them.
	 */
	public void sendRequest()
	{
		//Check to make sure another client is selected (not header or no selection).
		//Store selected id, updates information, if the id changes then they have left/moved in the table.
		//Get the game status of the selected client and make sure they are not currently in a game.
		//Send request message to the other client and retrieve response.
		
		//If none selected or first row (column header) has been selected.
		if(Jtable.getSelectedRow() == -1 || Jtable.getSelectedRow() == 0)
			JOptionPane.showMessageDialog(null, "You must select a player before you can send a game request.");
		else
		{
			try{
				//ID of client before updating table.
				int idBefore = Integer.parseInt((String)Jtable.getValueAt(Jtable.getSelectedRow(), 0));
			
				//If the selected client isn't the client doing the selecting
				if(idBefore != clientID)
				{
					int index = Jtable.getSelectedRow();
					//Update data
					updateText();
					//The id of the user selected still
					int idAfter = Integer.parseInt((String)Jtable.getValueAt(index, 0));
					//Game status of the client being selected
					String gameStatus = (String)Jtable.getValueAt(index, 2);
					//If the player is the same as before (so the list hasn't changed due to a disconnection
					if(idBefore == idAfter)
					{
						//If not currently in game
						if(gameStatus.compareTo("In Game") != 0)
						{
							//Send play request
							sender.sendMessage("Play With:" + idAfter);
							//Sleep to give enough time for a response.
							try {
								Thread.sleep(1000);
							} catch (InterruptedException e) {
								System.out.println("Interrupted while sleeping.");
							}
							//Retrieve response from the other client.
							for(int i = 0; i < reciever.messages.size(); i++)
							{
								String response = reciever.messages.get(i);
								if(response.compareTo("Start: PlayWith") == 0)
								{
									JOptionPane.showMessageDialog(null, reciever.messages.get(i+1));
									reciever.removeMessages(i, i+2);
									break;
								}
							}
						}
						else
							JOptionPane.showMessageDialog(null, "Sorry, that player is now in game, please try again.");
					}
					else
						JOptionPane.showMessageDialog(null, "That player has disconnected or been moved in the Lobby List, please try again.");
				}
				else
					JOptionPane.showMessageDialog(null, "You cannot play against yourself!");
			//Exception may occur if there is a table entry for a client that has disconnected and been removed from all tables
			//but user attempts to select as the board has not updated yet (if last client disconnects).
			} catch(ArrayIndexOutOfBoundsException e){
				JOptionPane.showMessageDialog(null, "That player may have disconnected, cannot play with them.");
			}
		}
	}
	
	/**
	 * Interrupts the threads running on the client, started by this class... allows for smooth exit of client GUI.
	 */
	public void stopThread()
	{
		lobbyCheck.interrupt();
		RecieverMessages.interrupt();
	}
}
