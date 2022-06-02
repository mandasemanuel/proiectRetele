package proiect_retele_main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class Client extends JFrame implements ActionListener
{
    public static void main(String[] args)
    {
        System.out.println("Starting Connection");
        Client myClient = new Client();
        myClient.setVisible(true);
    }
    
   private final int CONNECTION_PORT=4444;
   private JLabel serverNameLabel;
   private JTextField serverEnterField;


   private JLabel fileNameLabel;
   public JTextField fileNameField;


   private JButton button;

   private JTextArea labeltabone;
   private JTextArea labeltabtwo;


   private JButton save;

   private PrintWriter output;
   private BufferedReader input;
   private Socket clientSocket;


   private String message = ""; 
   private String chatServer; 
   private Socket client; 


   public Client()
   {

      super( "Client" );


      setSize(new Dimension(400, 400));

      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


      JPanel clientPanel = new JPanel();

      clientPanel.setLayout(new GridLayout(1, 2, 5, 5));


      serverNameLabel = new JLabel("Server Name: ");
      clientPanel.add(serverNameLabel);


      serverEnterField = new JTextField(15);
      clientPanel.add(serverEnterField);
      serverEnterField.setText("localhost");


      JPanel filePanel = new JPanel();



      fileNameLabel = new JLabel ("Enter File Name");
      filePanel.add(fileNameLabel);


      fileNameField = new JTextField(20);
      filePanel.add(fileNameField);
      fileNameField.setText("sample.txt");


      button = new JButton("Send File Request");
      button.addActionListener(this);
      filePanel.add(button);



      JTabbedPane tabbedPane = new JTabbedPane();


      labeltabone = new JTextArea(20, 40);
      JPanel labelpanel1 = new JPanel();

      labelpanel1.add(labeltabone);
      tabbedPane.addTab("Messages", null, labelpanel1, "First Panel" );


      labeltabtwo = new JTextArea(20, 40);
      JPanel labelpanel2 = new JPanel();
      labelpanel2.add(labeltabtwo);

      labelpanel2.setBackground(Color.gray);

      save = new JButton("Save");
      save.addActionListener(this);
      labelpanel2.add(save, BorderLayout.SOUTH);
      tabbedPane.addTab("Edit", null, labelpanel2, "Second Panel");


      JPanel masterPanel = new JPanel();
      masterPanel.setLayout(new GridLayout(2,0));



      masterPanel.add(clientPanel, BorderLayout.NORTH);
      masterPanel.add(filePanel, BorderLayout.NORTH);
      masterPanel.add(tabbedPane, BorderLayout.CENTER);


      getContentPane().add(masterPanel, BorderLayout.NORTH);
      getContentPane().add(new JScrollPane(tabbedPane), BorderLayout.CENTER);


      pack();


      setLocationRelativeTo(null);

   }


    public void actionPerformed(ActionEvent e)
    {
         if (e.getSource().equals(button))
        {
            if (connect())
            {
                readFile();
            }
        }
        else if (e.getSource().equals(save))
        {
                try
                {
                    fileSave();
                } catch (FileNotFoundException ex)
                {
                    Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex)
                {
                    Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                }
        } 
    }


    public boolean connect()
    {
        String serverName = serverEnterField.getText();

        if (serverName == null)
        {
            JOptionPane.showMessageDialog(this, "Enter Server Name");
            serverEnterField.requestFocus();
            return false;
        }
        clientSocket = null;
        try {

            clientSocket = new Socket(serverName, CONNECTION_PORT);
            return true;
        }
        catch(UnknownHostException e)
        {
            errorMessage("Unknown Host" + e.getMessage());
        }
        catch (IOException e)
        {
            errorMessage("IO Exception msg 2" + e.getMessage());
        }
        return false;

    }


    public void readFile()
    {
        if (clientSocket == null)
        {
            JOptionPane.showMessageDialog(this, "<<>>Disconnected");
            serverEnterField.requestFocus();
            return;
        }


        String fileName = fileNameField.getText();


        if (fileName == null)
        {
            JOptionPane.showMessageDialog(this,"Enter File Name.");
            fileNameField.requestFocus();
            return;
        }


        try{
            input = new BufferedReader(new InputStreamReader
                    (clientSocket.getInputStream()));

            output = new PrintWriter(clientSocket.getOutputStream(), true);


            printMessage(">>>>>Sending Request...");
            output.println(fileName);


            printMessage("<<<<<<Waiting on Server");


            while(!input.ready())
            {

            }
                //
                printMessage("<<<<<<Receiving Data");
                String line;

                StringBuffer sb = new StringBuffer();


                while((line = input.readLine()) != null)
                {

                    sb.append(line + '\n');
                }



                printMessage("!!!!!Your response has arrived!!!!!");


                printEdit(sb.toString());


                printMessage("Click on the Edit Tab for your file");


                output.close();
                input.close();

        }
            catch(IOException e)
            {

                errorMessage("Generic IO Exception. Details: " + e.getMessage());
            }
        }


    public void fileSave() throws FileNotFoundException, IOException
    {


        BufferedWriter thisFile = new BufferedWriter(new FileWriter
                (fileNameField.getText()));



        thisFile.write(labeltabtwo.getText());


        thisFile.close();


        printMessage("\n\nYour file has been saved!!\n\n");

    }

    private void printMessage(String serverMsg)
    {

        labeltabone.setText(labeltabone.getText()+ serverMsg + "\n");
            this.repaint();
    }

    private void printEdit(String editMsg)
    {
        labeltabtwo.setText(editMsg + "\n");
            this.repaint();
    }


    private void logMessage(String mssg)
    {
		labeltabone.setText(labeltabone.getText() + mssg + "\n");
		this.repaint();
    }

    private void errorMessage(String error)
    {
		logMessage("errorMessage()" + error);
    }
}