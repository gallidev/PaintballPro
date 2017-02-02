package networkingOld;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import java.awt.GridLayout;

/**
 * Displays the currently connected client(s) score information in a scroll-able JTable object on a JPanel.
 */
public class ScoreBoard extends JPanel
{
	private ClientSender sender;
	private ClientReceiver reciever;
	
	private JScrollPane scoreboard;
	
	private DefaultTableModel tableModel; 
	private JTable Jtable; 
	
	private ScoreCheckThread scoreThread;
	
	/**
	 * Constructs the required objects to produce a scroll-able table of connected client(s) scores.
	 * @param sender The class which sends messages to the Server.
	 * @param reciever The class which handles messages received from the Server.
	 */
	public ScoreBoard(ClientSender sender,ClientReceiver reciever)
	{
		super();
		
		this.sender = sender;
		this.reciever = reciever;
		
		//a scrollable panel
		scoreboard = new JScrollPane();
		
		setLayout(new GridLayout(1,1));
		
		//Create new table model and set cells as uneditable 
		//source - 'http://stackoverflow.com/questions/18795791/how-to-make-individual-cell-of-a-jtable-uneditable'
		tableModel = new DefaultTableModel() {
		    public boolean isCellEditable(int rowIndex, int mColIndex) {
		        return false;
		    }
		};
		
		//Create a new table object.
		Jtable = new JTable(tableModel); 
		Jtable.setFocusable(false);
		//Do not allow clients to select the rows of the table.
		Jtable.setRowSelectionAllowed(false);
		
		// Create a couple of columns 
		tableModel.addColumn("ID"); 
		tableModel.addColumn("Nickname"); 
		tableModel.addColumn("Score");
		
		//Update the table information
		updateText();
		
		//Periodically updates the client scores.
		scoreThread = new ScoreCheckThread(this);
		scoreThread.start();
	}
	
	/**
	 * Updates the displayed connected client score information with up-to-date data.
	 */
	public synchronized void updateText()
	{
		//Retrieve information from the server about currently connect client(s) and their scores.
		//Remove all current rows from the table.
		//Add the new data to the table.y.
		
		//Tell the server we want the scores of the current clients connected.
		sender.sendMessage("Retrieve Scores");
		
		String response = "";

		int startPos = 0; //Start of messages retrieved
		int endPos = 0; //End of messages retrieved     ... Both used for remove messages after.
		
		//Give enough time to retrieve all of the data.
		try {
			Thread.sleep(500); //CHANGED FROM 1000 TO 500
		} catch (InterruptedException e) {
			System.err.println("Interrupted while sleeping.");
		}
		
		//Cycle through all of the messages waiting for the client.
		for(int i = 0; i < reciever.messages.size(); i++)
		{
			response = reciever.messages.get(i);
			
			//If the message indicates the start of data we want
			if(response.compareTo("Start: Scores") == 0)
			{
				startPos = i;
				
				//Remove all existing rows in the table.
				for(int k = tableModel.getRowCount() - 1; k >= 0; k--)
				{
					tableModel.removeRow(k);
				}
				
				//Add first row with column headers, in bold using HTML formatting.
				//source - 'http://forums.codeguru.com/showthread.php?369283-Formatting-JTable-rows'
				tableModel.addRow(new Object[]{"<html><b>ID</b></html>","<html><b>Nickname</b></html>","<html><b>Score</b></html>"});
				
				for(int j = i+1; j < reciever.messages.size(); j++)
				{
					response = reciever.messages.get(j);
					
					//If all relevant data has been retrieved.
					if(response.compareTo("End: Scores") == 0)
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
						tableModel.addRow(new Object[]{values[0],values[1],values[2]});
					}
				}
			}
		}
		//If some messages have been retrieved, delete the range.
		if(endPos > 0)
		{
			reciever.removeMessages(startPos, endPos);
		}
		//Redisplay the tables.
		repaint();
	}
	
	/**
	 * Interrupts the threads running on the client, started by this class... allows for smooth exit of client GUI.
	 */
	public void stopThread()
	{
		scoreThread.interrupt();
	}
}